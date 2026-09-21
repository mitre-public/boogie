package org.mitre.boogie.xml;

import static java.util.Objects.requireNonNull;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.google.common.annotations.Beta;
import org.glassfish.jaxb.runtime.IDResolver;
import org.mitre.boogie.xml.exi.ExiCodec;
import org.mitre.boogie.xml.exi.ExiOptions;
import org.mitre.boogie.xml.model.ArincRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

/**
 * General-purpose streaming XML unmarshaller that processes an ARINC 424 XML input stream element-by-element
 * using a configurable set of {@link XmlRecordHandler}s.
 *
 * <p>This avoids loading the entire XML document into memory. Each recognized element is unmarshalled
 * individually via JAXB, validated and converted by its handler, and added to an {@link ArincRecords}
 * collection.
 *
 * <p>Handler sets and JAXB context classes for well-known versions are available via {@link ArincXmlVersion}.
 */
@Beta
public final class StreamingUnmarshaller implements Function<InputStream, Optional<ArincRecords>> {

  private static final Logger LOG = LoggerFactory.getLogger(StreamingUnmarshaller.class);
  private static final ReaderFactory XML_READER_FACTORY = input -> XMLInputFactory.newInstance().createXMLStreamReader(input);

  private final List<Class<?>> jaxbContextClasses;
  private final Map<String, XmlRecordHandler<?>> handlersByElement;
  private final ReaderFactory readerFactory;

  private StreamingUnmarshaller(List<Class<?>> jaxbContextClasses, Map<String, XmlRecordHandler<?>> handlersByElement, ReaderFactory readerFactory) {
    this.jaxbContextClasses = jaxbContextClasses;
    this.handlersByElement = handlersByElement;
    this.readerFactory = readerFactory;
  }

  /**
   * Configure a streaming unmarshaller. XML is the default input format; choose a version or supply
   * JAXB context classes and record handlers before building.
   */
  public static Builder builder() {
    return new Builder(XML_READER_FACTORY);
  }

  public static StreamingUnmarshaller fromVersion(ArincXmlVersion version) {
    return builder().version(version).build();
  }

  /**
   * Create an EXI unmarshaller for the given ARINC model version.
   */
  public static StreamingUnmarshaller fromVersion(ArincXmlVersion version, ExiOptions exiOptions) {
    return builder().version(version).exiOptions(exiOptions).build();
  }

  /** Creates a reader positioned at the beginning of a document. */
  @FunctionalInterface
  public interface ReaderFactory {

    XMLStreamReader createReader(InputStream input) throws XMLStreamException;
  }

  public static final class Builder {

    private List<Class<?>> jaxbContextClasses;
    private List<XmlRecordHandler<?>> handlers;
    private ReaderFactory readerFactory;

    private Builder(ReaderFactory readerFactory) {
      this.readerFactory = readerFactory;
    }

    /** Select the JAXB classes and record handlers for an ARINC model version. */
    public Builder version(ArincXmlVersion version) {
      requireNonNull(version, "version");
      this.jaxbContextClasses = version.jaxbContextClasses();
      this.handlers = version.handlers();
      return this;
    }

    public Builder jaxbContextClasses(List<Class<?>> jaxbContextClasses) {
      this.jaxbContextClasses = jaxbContextClasses;
      return this;
    }

    public Builder handlers(List<XmlRecordHandler<?>> handlers) {
      this.handlers = handlers;
      return this;
    }

    /** Select XML input, replacing any previously configured reader factory. */
    public Builder xml() {
      return readerFactory(XML_READER_FACTORY);
    }

    /** Select EXI input using schema-less or schema-informed options. */
    public Builder exiOptions(ExiOptions exiOptions) {
      ExiCodec codec = new ExiCodec(requireNonNull(exiOptions, "exiOptions"));
      return readerFactory(codec::createReader);
    }

    /**
     * Supply a reader factory, for example a codec with additional decoding schemas. The unmarshaller
     * closes each reader after use while retaining caller ownership of the input stream.
     */
    public Builder readerFactory(ReaderFactory readerFactory) {
      this.readerFactory = readerFactory;
      return this;
    }

    public StreamingUnmarshaller build() {
      List<Class<?>> contextClasses = List.copyOf(requireNonNull(jaxbContextClasses, "jaxbContextClasses"));
      Map<String, XmlRecordHandler<?>> handlersByElement = List.copyOf(requireNonNull(handlers, "handlers")).stream()
          .collect(Collectors.toUnmodifiableMap(XmlRecordHandler::elementName, Function.identity()));
      return new StreamingUnmarshaller(contextClasses, handlersByElement, requireNonNull(readerFactory, "readerFactory"));
    }
  }

  @Override
  public Optional<ArincRecords> apply(InputStream inputStream) {
    return apply(inputStream, ArincRecords.standard());
  }

  /**
   * Stream the configured XML or EXI input into the provided {@link ArincRecords} instance.
   *
   * <p>This overload allows callers to inject a custom {@link ArincRecords} implementation, for example one that
   * assembles records on-the-fly during streaming rather than buffering them.
   * The caller retains ownership of the input stream. A malformed document returns an empty result;
   * records already delivered to a supplied collection are not rolled back.
   */
  public Optional<ArincRecords> apply(InputStream inputStream, ArincRecords records) {
    requireNonNull(inputStream);
    requireNonNull(records);
    try {
      JAXBContext context = JAXBContext.newInstance(jaxbContextClasses.toArray(new Class[0]));
      Unmarshaller unmarshaller = context.createUnmarshaller();
      unmarshaller.setProperty(IDResolver.class.getName(), PassthroughIdResolver.INSTANCE);

      // Some StAX providers close their source when the reader is closed.
      InputStream source = new FilterInputStream(inputStream) {
        @Override
        public void close() {
          // The caller owns the input stream.
        }
      };
      XMLStreamReader reader = readerFactory.createReader(source);
      try {
        while (reader.getEventType() != XMLStreamConstants.END_DOCUMENT) {
          if (reader.isStartElement()) {
            XmlRecordHandler<?> handler = handlersByElement.get(reader.getLocalName());
            if (handler != null) {
              handleElement(unmarshaller, reader, handler, records);
              // JAXB already advanced beyond the element, possibly onto its next sibling.
              continue;
            }
          }
          if (!reader.hasNext()) {
            throw new XMLStreamException("Input ended before the document was complete.");
          }
          reader.next();
        }
      } finally {
        reader.close();
      }
      return Optional.of(records);
    } catch (Exception e) {
      LOG.error("Could not parse XML or EXI input: ", e);
      return Optional.empty();
    }
  }

  private <T> void handleElement(
      Unmarshaller unmarshaller,
      XMLStreamReader reader,
      XmlRecordHandler<T> handler,
      ArincRecords records) throws Exception {
    T value = unmarshaller.unmarshal(reader, handler.jaxbClass()).getValue();
    handler.accept(value, records);
  }

  /**
   * Custom {@link IDResolver} that returns the raw ID string for IDREF fields instead of attempting
   * to resolve them to their target objects. This is needed in streaming unmarshalling where referenced
   * objects may not be in scope.
   */
  private static final class PassthroughIdResolver extends IDResolver {

    static final PassthroughIdResolver INSTANCE = new PassthroughIdResolver();

    private PassthroughIdResolver() {
    }

    @Override
    public void bind(String id, Object obj) throws SAXException {
    }

    @Override
    public Callable<?> resolve(String id, Class targetType) throws SAXException {
      return () -> id;
    }
  }
}
