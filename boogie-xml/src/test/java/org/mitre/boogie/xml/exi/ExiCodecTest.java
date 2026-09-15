package org.mitre.boogie.xml.exi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class ExiCodecTest {

  private static final String SCHEMA_ID = "urn:boogie:test:exi:record:1";
  private static final String RECORD_NS = "urn:boogie:test:record";
  private static final String TYPES_NS = "urn:boogie:test:types";

  private static final String XML = """
      <r:record xmlns:r="urn:boogie:test:record" xmlns:t="urn:boogie:test:types"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:type="t:ExtendedRecordType" t:quality="tested" id="record-1">
        <t:count>+0007</t:count>
        <t:amount>0012.500</t:amount>
        <t:active>1</t:active>
        <t:label>café &amp; 東京</t:label>
        <t:note>Extended record</t:note>
      </r:record>
      """;

  @TempDir
  Path directory;

  @ParameterizedTest
  @CsvSource({"false, false", "false, true", "true, false", "true, true"})
  void roundTripsNamespacesTypesAndLexicalValues(boolean schemaInformed, boolean compressed) throws Exception {
    ExiSchema schema = compileSchema();
    ExiOptions options = (schemaInformed ? ExiOptions.schemaInformed(schema) : ExiOptions.schemaLess())
        .withCompression(compressed)
        .withPreserveLexicalValues(true);
    ExiCodec encoder = new ExiCodec(options);
    ExiCodec decoder = new ExiCodec(ExiOptions.schemaLess(), List.of(schema));

    byte[] encoded = encode(encoder, XML);
    String decoded = decode(decoder, encoded);

    assertEquals(shape(parse(XML).getDocumentElement()), shape(parse(decoded).getDocumentElement()));
    assertFalse(Arrays.equals(XML.getBytes(StandardCharsets.UTF_8), encoded));
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void preservesTypedValuesWhenLexicalPreservationIsDisabled(boolean compressed) throws Exception {
    ExiCodec codec = new ExiCodec(ExiOptions.schemaInformed(compileSchema())
        .withCompression(compressed)
        .withPreserveLexicalValues(false));

    Document decoded = parse(decode(codec, encode(codec, XML)));

    assertEquals(BigInteger.valueOf(7), new BigInteger(value(decoded, "count")));
    assertEquals(0, new BigDecimal("12.5").compareTo(new BigDecimal(value(decoded, "amount"))));
    assertTrue(List.of("true", "1").contains(value(decoded, "active")));
    assertEquals("café & 東京", value(decoded, "label"));
    assertEquals("tested", decoded.getDocumentElement().getAttributeNS(TYPES_NS, "quality"));
  }

  @Test
  void rejectsUnknownSchemaIdAndAcceptsRegisteredSchema() throws Exception {
    ExiSchema schema = compileSchema();
    byte[] encoded = encode(new ExiCodec(ExiOptions.schemaInformed(schema)
        .withPreserveLexicalValues(true)), XML);

    IOException error = assertThrows(IOException.class,
        () -> decode(new ExiCodec(ExiOptions.schemaLess()), encoded));
    assertTrue(causes(error).contains(SCHEMA_ID), causes(error));

    ExiCodec registered = new ExiCodec(ExiOptions.schemaLess(), List.of(schema));
    assertEquals(shape(parse(XML).getDocumentElement()),
        shape(parse(decode(registered, encoded)).getDocumentElement()));
  }

  @Test
  void strictSchemaRejectsDeviationsThatNonStrictSchemaCanRoundTrip() throws Exception {
    ExiOptions options = ExiOptions.schemaInformed(compileSchema()).withPreserveLexicalValues(true);
    String valid = XML.replace("xsi:type=\"t:ExtendedRecordType\"", "")
        .replace("<t:note>Extended record</t:note>", "");
    ExiCodec strict = new ExiCodec(options.withStrict(true));
    assertEquals(shape(parse(valid).getDocumentElement()),
        shape(parse(decode(strict, encode(strict, valid))).getDocumentElement()));

    String unexpectedElement = valid.replace("</r:record>", "<t:unexpected>extra</t:unexpected></r:record>");

    assertThrows(IOException.class, () -> encode(strict, unexpectedElement));

    ExiCodec relaxed = new ExiCodec(options.withStrict(false));
    assertEquals(shape(parse(unexpectedElement).getDocumentElement()),
        shape(parse(decode(relaxed, encode(relaxed, unexpectedElement))).getDocumentElement()));
  }

  @Test
  void readerUsesHeaderSettingsAndResolvesInheritedNamespaceBindings() throws Exception {
    String xml = """
        <r:root xmlns:r="urn:boogie:test:record" xmlns:t="urn:boogie:test:types"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <r:child xsi:type="t:Kind">+0007</r:child>
        </r:root>
        """;
    ExiCodec encoder = new ExiCodec(ExiOptions.schemaLess()
        .withCompression(true)
        .withPreserveLexicalValues(true));
    ExiCodec decoder = new ExiCodec(ExiOptions.schemaLess());
    XMLStreamReader reader = decoder.createReader(new ByteArrayInputStream(encode(encoder, xml)));
    try {
      reader.nextTag();
      assertEquals("root", reader.getLocalName());
      reader.nextTag();
      assertEquals("child", reader.getLocalName());
      assertEquals(RECORD_NS, reader.getNamespaceURI("r"));
      assertEquals(TYPES_NS, reader.getNamespaceURI("t"));
      assertEquals(XMLConstants.XML_NS_URI, reader.getNamespaceURI(XMLConstants.XML_NS_PREFIX));
      assertEquals("t:Kind", reader.getAttributeValue(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "type"));
      assertEquals("+0007", reader.getElementText());
    } finally {
      reader.close();
    }
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void rejectsTruncatedAndNonExiInput(boolean compressed) throws Exception {
    ExiCodec codec = new ExiCodec(ExiOptions.schemaLess().withCompression(compressed));
    byte[] encoded = encode(codec, XML);

    assertThrows(IOException.class, () -> decode(codec, new byte[0]));
    assertThrows(IOException.class, () -> decode(codec, XML.getBytes(StandardCharsets.UTF_8)));
    assertThrows(IOException.class, () -> decode(codec, Arrays.copyOf(encoded, encoded.length / 2)));
  }

  @Test
  void reusesSchemaAndCodecWithoutClosingCallerStreams() throws Exception {
    ExiCodec codec = new ExiCodec(ExiOptions.schemaInformed(compileSchema())
        .withCompression(true)
        .withPreserveLexicalValues(true));

    for (int i = 0; i < 3; i++) {
      String xml = XML.replace("record-1", "record-" + i);
      TrackedInputStream input = new TrackedInputStream(xml.getBytes(StandardCharsets.UTF_8));
      TrackedOutputStream encoded = new TrackedOutputStream();
      codec.encode(input, encoded);
      assertFalse(input.closed);
      assertFalse(encoded.closed);

      TrackedInputStream binary = new TrackedInputStream(encoded.toByteArray());
      TrackedOutputStream decoded = new TrackedOutputStream();
      codec.decode(binary, decoded);
      assertFalse(binary.closed);
      assertFalse(decoded.closed);
      assertEquals(shape(parse(xml).getDocumentElement()),
          shape(parse(decoded.toString(StandardCharsets.UTF_8)).getDocumentElement()));
    }
  }

  @ParameterizedTest
  @CsvSource({"false, false", "false, true", "true, false", "true, true"})
  void supportsDirectJaxbWritingAndReading(boolean schemaInformed, boolean compressed) throws Exception {
    JAXBContext context = JAXBContext.newInstance(TestRecord.class);
    TestRecord expected = new TestRecord();
    expected.id = "test-17";
    expected.quality = "tested";
    expected.label = "café & 東京";
    expected.count = 17;
    expected.amount = new BigDecimal("12.5");
    expected.active = true;
    ExiOptions options = schemaInformed
        ? ExiOptions.schemaInformed(compileSchema()).withStrict(true)
        : ExiOptions.schemaLess();
    ExiCodec codec = new ExiCodec(options.withCompression(compressed));
    TrackedOutputStream encoded = new TrackedOutputStream();

    XMLStreamWriter writer = codec.createWriter(encoded);
    try {
      context.createMarshaller().marshal(expected, writer);
    } finally {
      writer.close();
    }
    assertFalse(encoded.closed);

    TrackedInputStream input = new TrackedInputStream(encoded.toByteArray());
    XMLStreamReader reader = codec.createReader(input);
    TestRecord actual;
    try {
      actual = (TestRecord) context.createUnmarshaller().unmarshal(reader);
    } finally {
      reader.close();
    }
    assertFalse(input.closed);
    assertEquals(expected.id, actual.id);
    assertEquals(expected.quality, actual.quality);
    assertEquals(expected.label, actual.label);
    assertEquals(expected.count, actual.count);
    assertEquals(0, expected.amount.compareTo(actual.amount));
    assertEquals(expected.active, actual.active);
  }

  private ExiSchema compileSchema() throws IOException {
    Path typesDirectory = Files.createDirectories(directory.resolve("types"));
    Files.writeString(typesDirectory.resolve("record-types.xsd"), """
        <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
            targetNamespace="urn:boogie:test:types" xmlns:t="urn:boogie:test:types"
            elementFormDefault="qualified" attributeFormDefault="qualified">
          <xs:complexType name="RecordType">
            <xs:sequence>
              <xs:element name="count" type="xs:int"/>
              <xs:element name="amount" type="xs:decimal"/>
              <xs:element name="active" type="xs:boolean"/>
              <xs:element name="label" type="xs:string"/>
            </xs:sequence>
            <xs:attribute name="quality" type="xs:string" use="required"/>
            <xs:attribute name="id" type="xs:string" form="unqualified" use="required"/>
          </xs:complexType>
          <xs:complexType name="ExtendedRecordType">
            <xs:complexContent>
              <xs:extension base="t:RecordType">
                <xs:sequence><xs:element name="note" type="xs:string"/></xs:sequence>
              </xs:extension>
            </xs:complexContent>
          </xs:complexType>
        </xs:schema>
        """);
    Path root = directory.resolve("record.xsd");
    Files.writeString(root, """
        <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
            targetNamespace="urn:boogie:test:record" xmlns:t="urn:boogie:test:types"
            elementFormDefault="qualified">
          <xs:import namespace="urn:boogie:test:types" schemaLocation="types/record-types.xsd"/>
          <xs:element name="record" type="t:RecordType"/>
        </xs:schema>
        """);
    return ExiSchema.compile(SCHEMA_ID, root);
  }

  private static byte[] encode(ExiCodec codec, String xml) throws IOException {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    codec.encode(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)), result);
    return result.toByteArray();
  }

  private static String decode(ExiCodec codec, byte[] exi) throws IOException {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    codec.decode(new ByteArrayInputStream(exi), result);
    return result.toString(StandardCharsets.UTF_8);
  }

  private static Document parse(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
    factory.setNamespaceAware(true);
    return factory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
  }

  private static String value(Document document, String localName) {
    return document.getElementsByTagNameNS(TYPES_NS, localName).item(0).getTextContent();
  }

  private static ElementShape shape(Element element) {
    Map<String, String> attributes = new TreeMap<>();
    for (int i = 0; i < element.getAttributes().getLength(); i++) {
      Node attribute = element.getAttributes().item(i);
      if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(attribute.getNamespaceURI())) {
        continue;
      }
      String value = attribute.getNodeValue();
      if (XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI.equals(attribute.getNamespaceURI())
          && "type".equals(attribute.getLocalName())) {
        int separator = value.indexOf(':');
        String prefix = separator < 0 ? null : value.substring(0, separator);
        String localName = separator < 0 ? value : value.substring(separator + 1);
        value = "{" + element.lookupNamespaceURI(prefix) + "}" + localName;
      }
      attributes.put(expandedName(attribute), value);
    }
    List<Object> children = new ArrayList<>();
    for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
      if (child instanceof Element childElement) {
        children.add(shape(childElement));
      } else if ((child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE)
          && !child.getNodeValue().isBlank()) {
        children.add(child.getNodeValue());
      }
    }
    return new ElementShape(expandedName(element), attributes, children);
  }

  private static String expandedName(Node node) {
    return "{" + (node.getNamespaceURI() == null ? "" : node.getNamespaceURI()) + "}" + node.getLocalName();
  }

  private static String causes(Throwable error) {
    StringBuilder result = new StringBuilder();
    for (Throwable cause = error; cause != null; cause = cause.getCause()) {
      result.append(cause).append('\n');
    }
    return result.toString();
  }

  private record ElementShape(String name, Map<String, String> attributes, List<Object> children) {}

  private static final class TrackedInputStream extends ByteArrayInputStream {
    private boolean closed;

    private TrackedInputStream(byte[] input) {
      super(input);
    }

    @Override
    public void close() throws IOException {
      closed = true;
      super.close();
    }
  }

  private static final class TrackedOutputStream extends ByteArrayOutputStream {
    private boolean closed;

    @Override
    public void close() throws IOException {
      closed = true;
      super.close();
    }
  }

  @XmlRootElement(name = "record", namespace = RECORD_NS)
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(propOrder = {"count", "amount", "active", "label"})
  public static class TestRecord {
    @XmlAttribute
    public String id;

    @XmlAttribute(namespace = TYPES_NS)
    public String quality;

    @XmlElement(namespace = TYPES_NS)
    public String label;

    @XmlElement(namespace = TYPES_NS)
    public int count;

    @XmlElement(namespace = TYPES_NS)
    public BigDecimal amount;

    @XmlElement(namespace = TYPES_NS)
    public boolean active;
  }
}
