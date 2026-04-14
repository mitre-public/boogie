package org.mitre.boogie.xml;

import static java.util.Objects.requireNonNull;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import org.glassfish.jaxb.runtime.IDResolver;
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
public final class StreamingUnmarshaller implements Function<InputStream, Optional<ArincRecords>> {

  private static final Logger LOG = LoggerFactory.getLogger(StreamingUnmarshaller.class);

  private final List<Class<?>> jaxbContextClasses;
  private final Map<String, XmlRecordHandler<?>> handlersByElement;

  public StreamingUnmarshaller(List<Class<?>> jaxbContextClasses, List<XmlRecordHandler<?>> handlers) {
    this.jaxbContextClasses = requireNonNull(jaxbContextClasses);
    this.handlersByElement = handlers.stream()
        .collect(Collectors.toMap(XmlRecordHandler::elementName, Function.identity()));
  }

  @Override
  public Optional<ArincRecords> apply(InputStream inputStream) {
    ArincRecords records = ArincRecords.standard();
    try {
      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
      XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

      JAXBContext context = JAXBContext.newInstance(jaxbContextClasses.toArray(new Class[0]));
      Unmarshaller unmarshaller = context.createUnmarshaller();
      unmarshaller.setProperty(IDResolver.class.getName(), PassthroughIdResolver.INSTANCE);

      try {
        XMLEvent event;
        while ((event = xmlEventReader.peek()) != null) {
          if (event.isStartElement()) {
            String localPart = event.asStartElement().getName().getLocalPart();
            XmlRecordHandler<?> handler = handlersByElement.get(localPart);
            if (handler != null) {
              handleElement(unmarshaller, xmlEventReader, handler, records);
            }
          }
          xmlEventReader.nextEvent();
        }
      } catch (Exception e) {
        LOG.info("could not parse an xml element: ", e);
      }
      return Optional.of(records);
    } catch (Exception e) {
      LOG.error("Could not parse XML file: ", e);
      return Optional.empty();
    }
  }

  private <T> void handleElement(
      Unmarshaller unmarshaller,
      XMLEventReader reader,
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
