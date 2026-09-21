package org.mitre.boogie.xml.exi;

import static java.util.Objects.requireNonNull;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.siemens.ct.exi.core.CodingMode;
import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.EncodingOptions;
import com.siemens.ct.exi.core.FidelityOptions;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.grammars.Grammars;
import com.siemens.ct.exi.core.grammars.SchemaLessGrammars;
import com.siemens.ct.exi.core.helpers.DefaultEXIFactory;
import com.siemens.ct.exi.grammars.GrammarFactory;
import com.siemens.ct.exi.main.api.sax.EXIResult;
import com.siemens.ct.exi.main.api.sax.EXISource;
import com.siemens.ct.exi.main.api.stream.StAXDecoder;
import com.siemens.ct.exi.main.api.stream.StAXEncoder;

/**
 * Streaming XML/EXI conversion and StAX access for JAXB and other XML consumers.
 *
 * <p>A codec is reusable: every operation creates its own encoder or decoder while reusing
 * compiled schemas. Streams belong to the caller and are never closed by the codec, including
 * when a returned StAX reader or writer is closed. Finish a StAX document with
 * {@link XMLStreamWriter#writeEndDocument()} to flush the complete EXI stream.
 *
 * <p>EXI output includes the cookie, encoding options and schema ID. An incoming schema ID is
 * resolved only through this codec's registry. Unknown IDs fail instead of fetching a schema.
 */
public final class ExiCodec {

  private final ExiOptions options;
  private final Map<String, ExiSchema> decodingSchemas;

  /** Create a codec that also registers its encoding schema for decoding, if present. */
  public ExiCodec(ExiOptions options) {
    this(options, List.of());
  }

  /**
   * Register additional schemas for decoding streams produced with other schema sets.
   * Conflicting schema objects with the same ID are rejected.
   */
  public ExiCodec(ExiOptions options, Collection<ExiSchema> decodingSchemas) {
    this.options = requireNonNull(options, "options");
    requireNonNull(decodingSchemas, "decodingSchemas");
    Map<String, ExiSchema> schemas = new LinkedHashMap<>();
    options.schema().ifPresent(schema -> register(schemas, schema));
    decodingSchemas.forEach(schema -> register(schemas, requireNonNull(schema, "schema")));
    this.decodingSchemas = Map.copyOf(schemas);
  }

  /** Encode one XML document. DTDs and external entities are not supported. */
  public void encode(InputStream xml, OutputStream exi) throws IOException {
    requireNonNull(xml, "xml");
    requireNonNull(exi, "exi");
    try {
      EXIResult result = new EXIResult(createFactory());
      result.setOutputStream(nonClosing(exi));
      XMLReader parser = createXmlReader();
      parser.setContentHandler(result.getHandler());
      parser.parse(new InputSource(nonClosing(xml)));
    } catch (EXIException | SAXException | ParserConfigurationException | RuntimeException e) {
      throw new IOException("Could not encode XML as EXI", e);
    }
  }

  /** Decode one EXI document to UTF-8 XML. XML formatting and prefix spelling may change. */
  public void decode(InputStream exi, OutputStream xml) throws IOException {
    requireNonNull(exi, "exi");
    requireNonNull(xml, "xml");
    try {
      EXISource source = new EXISource(createFactory());
      source.setInputSource(new InputSource(nonClosing(exi)));
      TransformerFactory transformerFactory = TransformerFactory.newDefaultInstance();
      transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
      transformerFactory.newTransformer().transform(source, new StreamResult(nonClosing(xml)));
    } catch (EXIException | TransformerException | RuntimeException e) {
      throw new IOException("Could not decode EXI as XML", e);
    }
  }

  /** Create a reader positioned at the EXI document's START_DOCUMENT event. */
  public XMLStreamReader createReader(InputStream exi) throws XMLStreamException {
    requireNonNull(exi, "exi");
    try {
      StAXDecoder reader = new StAXDecoder(createFactory()) {
        @Override
        public String getNamespaceURI(String prefix) {
          if (prefix == null) {
            throw new IllegalArgumentException("A namespace prefix must not be null");
          }
          if (XMLConstants.XML_NS_PREFIX.equals(prefix)) {
            return XMLConstants.XML_NS_URI;
          }
          if (XMLConstants.XMLNS_ATTRIBUTE.equals(prefix)) {
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
          }
          // EXIficient's reader method only checks declarations on the current element.
          return getNamespaceContext().getNamespaceURI(prefix);
        }
      };
      reader.setInputStream(nonClosing(exi));
      return reader;
    } catch (EXIException | IOException | RuntimeException e) {
      throw new XMLStreamException("Could not create EXI reader", e);
    }
  }

  /** Create a writer for one complete EXI document, suitable for JAXB marshalling. */
  public XMLStreamWriter createWriter(OutputStream exi) throws XMLStreamException {
    requireNonNull(exi, "exi");
    try {
      StAXEncoder writer = new StAXEncoder(createFactory());
      writer.setOutputStream(nonClosing(exi));
      return writer;
    } catch (EXIException | IOException | RuntimeException e) {
      throw new XMLStreamException("Could not create EXI writer", e);
    }
  }

  private EXIFactory createFactory() throws EXIException {
    EXIFactory factory = DefaultEXIFactory.newInstance();
    factory.setGrammars(options.schema().map(ExiSchema::grammars).orElseGet(SchemaLessGrammars::new));
    factory.setCodingMode(options.compression() ? CodingMode.COMPRESSION : CodingMode.BIT_PACKED);
    FidelityOptions fidelity = options.strict() ? FidelityOptions.createStrict() : FidelityOptions.createDefault();
    fidelity.setFidelity(FidelityOptions.FEATURE_LEXICAL_VALUE, options.preserveLexicalValues());
    if (options.preserveLexicalValues() && !options.strict()) {
      // QName spellings such as xsi:type require their original namespace bindings.
      fidelity.setFidelity(FidelityOptions.FEATURE_PREFIX, true);
    }
    factory.setFidelityOptions(fidelity);
    EncodingOptions encoding = factory.getEncodingOptions();
    encoding.setOption(EncodingOptions.INCLUDE_COOKIE);
    encoding.setOption(EncodingOptions.INCLUDE_OPTIONS);
    encoding.setOption(EncodingOptions.INCLUDE_SCHEMA_ID);
    encoding.setOption(EncodingOptions.INCLUDE_XSI_SCHEMALOCATION);
    factory.setSchemaIdResolver(this::resolveSchema);
    return factory;
  }

  private Grammars resolveSchema(String schemaId) throws EXIException {
    if (schemaId == null) {
      return new SchemaLessGrammars();
    }
    if (schemaId.isEmpty()) {
      return GrammarFactory.newInstance().createXSDTypesOnlyGrammars();
    }
    ExiSchema schema = decodingSchemas.get(schemaId);
    if (schema == null) {
      throw new EXIException("Unknown EXI schema ID: " + schemaId);
    }
    return schema.grammars();
  }

  private static void register(Map<String, ExiSchema> schemas, ExiSchema schema) {
    ExiSchema existing = schemas.putIfAbsent(schema.schemaId(), schema);
    if (existing != null && existing != schema) {
      throw new IllegalArgumentException("Duplicate EXI schema ID: " + schema.schemaId());
    }
  }

  private static XMLReader createXmlReader() throws ParserConfigurationException, SAXException {
    SAXParserFactory factory = SAXParserFactory.newDefaultInstance();
    factory.setNamespaceAware(true);
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    XMLReader reader = factory.newSAXParser().getXMLReader();
    reader.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    reader.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    reader.setErrorHandler(new DefaultHandler() {
      @Override
      public void error(SAXParseException e) throws SAXException {
        throw e;
      }

      @Override
      public void fatalError(SAXParseException e) throws SAXException {
        throw e;
      }
    });
    return reader;
  }

  private static InputStream nonClosing(InputStream input) {
    return new FilterInputStream(input) {
      @Override
      public void close() {
        // The caller owns the stream, even when a parser closes its InputSource.
      }
    };
  }

  private static OutputStream nonClosing(OutputStream output) {
    return new FilterOutputStream(output) {
      @Override
      public void write(byte[] bytes, int offset, int length) throws IOException {
        out.write(bytes, offset, length);
      }

      @Override
      public void close() throws IOException {
        flush();
      }
    };
  }
}
