package org.mitre.boogie.xml;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlType;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;
import org.mitre.boogie.xml.exi.ExiCodec;
import org.mitre.boogie.xml.exi.ExiOptions;
import org.mitre.boogie.xml.exi.ExiSchema;
import org.mitre.boogie.xml.model.ArincRecords;

class StreamingExiUnmarshallerTest {

  private static final String NAMESPACE = "urn:boogie:streaming-test";

  private static ExiSchema schema;

  @TempDir
  static Path temporaryDirectory;

  @BeforeAll
  static void compileSchema() throws Exception {
    Path xsd = temporaryDirectory.resolve("records.xsd");
    Files.writeString(xsd, """
        <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
                   targetNamespace="urn:boogie:streaming-test" elementFormDefault="qualified">
          <xs:element name="records">
            <xs:complexType>
              <xs:sequence>
                <xs:element name="item" maxOccurs="unbounded">
                  <xs:complexType>
                    <xs:sequence>
                      <xs:element name="name" type="xs:string"/>
                    </xs:sequence>
                    <xs:attribute name="id" type="xs:ID" use="required"/>
                    <xs:attribute name="reference" type="xs:IDREF" use="required"/>
                  </xs:complexType>
                </xs:element>
              </xs:sequence>
            </xs:complexType>
          </xs:element>
        </xs:schema>
        """);
    schema = ExiSchema.compile("urn:boogie:streaming-test:v1", xsd);
  }

  // Adjacent records and an inherited namespace exercise the StAX cursor after JAXB unmarshalling.
  private static final String DOCUMENT = "<records xmlns=\"" + NAMESPACE + "\">"
      + "<item id=\"first\" reference=\"second\"><name>  Montréal &amp; 東京  </name></item>"
      + "<item id=\"second\" reference=\"first\"><name>Second</name></item>"
      + "<item id=\"third\" reference=\"outside-document\"><name>Third</name></item>"
      + "</records>";

  @ParameterizedTest
  @EnumSource(InputFormat.class)
  void readsAdjacentNamespacedRecordsAndPreservesRawIdReferences(InputFormat format) throws Exception {
    List<SampleRecord> values = new ArrayList<>();
    TrackingInputStream input = new TrackingInputStream(format.encode(DOCUMENT));
    ArincRecords target = ArincRecords.standard();

    Optional<ArincRecords> result = unmarshaller(format, values).apply(input, target);

    assertTrue(result.isPresent());
    assertEquals(target, result.orElseThrow());
    assertEquals(3, values.size());
    assertAll(
        () -> assertEquals(List.of("first", "second", "third"), values.stream().map(value -> value.id).toList()),
        () -> assertEquals(List.of("second", "first", "outside-document"), values.stream().map(value -> value.reference).toList()),
        () -> assertEquals(List.of("  Montréal & 東京  ", "Second", "Third"), values.stream().map(value -> value.name).toList()),
        () -> assertFalse(input.closed, "The caller owns the input stream")
    );
  }

  @ParameterizedTest
  @EnumSource(InputFormat.class)
  void rejectsTruncatedDocumentsWithoutReturningSuccessfulPartialResults(InputFormat format) throws Exception {
    byte[] bytes = format.encode(DOCUMENT);
    int length = format == InputFormat.XML ? bytes.length - "</records>".length() : bytes.length / 2;
    TrackingInputStream input = new TrackingInputStream(Arrays.copyOf(bytes, length));

    Optional<ArincRecords> result = unmarshaller(format, new ArrayList<>()).apply(input);

    assertAll(
        () -> assertTrue(result.isEmpty(), "Incomplete documents must fail"),
        () -> assertFalse(input.closed, "The caller owns the input stream even after a failure")
    );
  }

  private static StreamingUnmarshaller unmarshaller(InputFormat format, List<SampleRecord> values) {
    List<Class<?>> classes = List.of(SampleRecord.class);
    List<XmlRecordHandler<?>> handlers = List.of(XmlRecordHandler.of(
        "item", SampleRecord.class, Optional::of, (records, value) -> values.add(value)));
    StreamingUnmarshaller.Builder builder = StreamingUnmarshaller.builder()
        .jaxbContextClasses(classes)
        .handlers(handlers);
    if (format != InputFormat.XML) {
      builder.exiOptions(format.options());
    }
    return builder.build();
  }

  private enum InputFormat {
    XML,
    EXI,
    EXI_COMPRESSED,
    EXI_SCHEMA,
    EXI_SCHEMA_COMPRESSED;

    ExiOptions options() {
      ExiOptions options = this == EXI_SCHEMA || this == EXI_SCHEMA_COMPRESSED
          ? ExiOptions.schemaInformed(schema).withStrict(true)
          : ExiOptions.schemaLess();
      return options.withCompression(this == EXI_COMPRESSED || this == EXI_SCHEMA_COMPRESSED);
    }

    byte[] encode(String xml) throws Exception {
      byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
      if (this == XML) {
        return bytes;
      }
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      new ExiCodec(options()).encode(new ByteArrayInputStream(bytes), output);
      return output.toByteArray();
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(namespace = NAMESPACE)
  public static final class SampleRecord {

    @XmlAttribute
    @XmlID
    public String id;

    @XmlAttribute
    @XmlIDREF
    public Object reference;

    @XmlElement(namespace = NAMESPACE)
    public String name;
  }

  private static final class TrackingInputStream extends ByteArrayInputStream {

    private boolean closed;

    private TrackingInputStream(byte[] bytes) {
      super(bytes);
    }

    @Override
    public void close() {
      closed = true;
    }
  }
}
