package org.mitre.boogie.xml;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.exi.ExiCodec;
import org.mitre.boogie.xml.exi.ExiOptions;
import org.mitre.boogie.xml.exi.ExiSchema;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;

class OneshotExiParserTest {

  @Test
  void writesGibberishSampleExi() throws Exception {
    ExiOptions options = ExiOptions.schemaLess().withCompression(true);
    Path exiFile = Path.of("build", "exi", "gibberish-sample-no-schema.exi");
    Files.createDirectories(exiFile.getParent());
    try (InputStream input = fixture();
         OutputStream encoded = new BufferedOutputStream(Files.newOutputStream(exiFile))) {
      new ExiCodec(options).encode(input, encoded);
    }
  }

  @Test
  void writesGibberishSampleSchemaInformedExi() throws Exception {
    Path rootXsd = Path.of(OneshotExiParserTest.class
        .getResource("/v23_4/schemas/Records/AeroPublication.xsd").toURI());
    ExiSchema schema = ExiSchema.compile("urn:boogie:arinc424:23.4", rootXsd);
    ExiOptions options = ExiOptions.schemaInformed(schema).withCompression(true);
    Path exiFile = Path.of("build", "exi", "gibberish-sample-schema-informed.exi");
    Files.createDirectories(exiFile.getParent());
    try (InputStream input = fixture();
         OutputStream encoded = new BufferedOutputStream(Files.newOutputStream(exiFile))) {
      new ExiCodec(options).encode(input, encoded);
    }
  }

  @Test
  @Tag("CIFP")
  @Tag("INTEGRATION")
  void writesCifpXmlAndExi() throws Exception {
    writeXmlAndExi(CifpXmlFixture.load().publication(), "cifp-2101");
  }

  @Test
  @Tag("DAFIF")
  @Tag("INTEGRATION")
  void writesDafifXmlAndExi() throws Exception {
    writeXmlAndExi(DafifXmlFixture.load().publication(), "dafif-2601");
  }

  private static void writeXmlAndExi(AeroPublication publication, String fileName) throws Exception {
    Path outputDirectory = Path.of("build", "exi");
    Files.createDirectories(outputDirectory);
    Path xmlFile = outputDirectory.resolve(fileName + ".xml");
    Marshaller marshaller = JAXBContext.newInstance(AeroPublication.class).createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    try (OutputStream output = new BufferedOutputStream(Files.newOutputStream(xmlFile))) {
      marshaller.marshal(publication, output);
    }

    ExiOptions schemaLess = ExiOptions.schemaLess().withCompression(true);
    try (InputStream input = Files.newInputStream(xmlFile);
         OutputStream output = new BufferedOutputStream(Files.newOutputStream(
             outputDirectory.resolve(fileName + "-no-schema.exi")))) {
      new ExiCodec(schemaLess).encode(input, output);
    }

    Path rootXsd = Path.of(OneshotExiParserTest.class
        .getResource("/v23_4/schemas/Records/AeroPublication.xsd").toURI());
    ExiSchema schema = ExiSchema.compile("urn:boogie:arinc424:23.4", rootXsd);
    ExiOptions schemaInformed = ExiOptions.schemaInformed(schema).withCompression(true);
    try (InputStream input = Files.newInputStream(xmlFile);
         OutputStream output = new BufferedOutputStream(Files.newOutputStream(
             outputDirectory.resolve(fileName + "-schema-informed.exi")))) {
      new ExiCodec(schemaInformed).encode(input, output);
    }
  }

  @Test
  void parsesTheArincFixtureAsExiIntoModelsAndAssembledRecords() throws Exception {
    ExiOptions options = ExiOptions.schemaLess().withCompression(true);
    ByteArrayOutputStream encoded = new ByteArrayOutputStream();
    try (InputStream input = fixture()) {
      new ExiCodec(options).encode(input, encoded);
    }

    byte[] exi = encoded.toByteArray();
    ArincRecords expected;
    try (InputStream input = fixture()) {
      expected = OneshotXmlModelParser.standard(ArincXmlVersion.V23_4).parseFrom(input);
    }
    var modelParser = OneshotXmlModelParser.builder()
        .version(ArincXmlVersion.V23_4)
        .exiOptions(options)
        .build();
    var assemblingParser = OneshotXmlParser.standardBuilder(ArincXmlVersion.V23_4)
        .exiOptions(options)
        .build();
    try (InputStream modelInput = new ByteArrayInputStream(exi);
         InputStream assemblingInput = new ByteArrayInputStream(exi)) {
      ArincRecords actual = modelParser.parseFrom(modelInput);
      var assembled = assemblingParser.assembleFrom(assemblingInput);

      assertAll(
          () -> assertEquals(modelValues(expected.waypoints()), modelValues(actual.waypoints()), "Waypoint models"),
          () -> assertEquals(modelValues(expected.airports()), modelValues(actual.airports()), "Airport models and nested procedures"),
          () -> assertEquals(modelValues(expected.ndbNavaids()), modelValues(actual.ndbNavaids()), "NDB models"),
          () -> assertEquals(modelValues(expected.vhfNavaids()), modelValues(actual.vhfNavaids()), "VHF models"),
          () -> assertEquals(modelValues(expected.arincAirways()), modelValues(actual.arincAirways()), "Airway models and references"),
          () -> assertEquals(modelValues(expected.holdingPatterns()), modelValues(actual.holdingPatterns()), "Holding pattern models"),
          () -> assertEquals(modelValues(expected.heliports()), modelValues(actual.heliports()), "Heliport models"),
          () -> assertEquals(5, assembled.airports().size(), "Assembled airports"),
          () -> assertEquals(13, assembled.fixes().size(), "Assembled fixes"),
          () -> assertEquals(5, assembled.airways().size(), "Assembled airways"),
          () -> assertEquals(75, assembled.procedures().size(), "Assembled procedures"),
          () -> assertEquals(5, assembled.heliports().size(), "Assembled heliports")
      );
    }
  }

  private static InputStream fixture() {
    return OneshotExiParserTest.class.getResourceAsStream("/v23_4/gibberish-sample.xml");
  }

  // Model equals methods include JAXB supplemental data, whose generated classes use identity
  // equality. Compare every model/JAXB field by value, including nested legs and IDREF strings.
  private static Object modelValues(Object value) {
    if (value instanceof Optional<?> optional) {
      return optional.map(OneshotExiParserTest::modelValues);
    }
    if (value instanceof Set<?> values) {
      // Preserve multiplicity if distinct model instances have the same complete field values.
      return values.stream().map(OneshotExiParserTest::modelValues)
          .collect(Collectors.groupingBy(element -> element, Collectors.counting()));
    }
    if (value instanceof Collection<?> values) {
      return values.stream().map(OneshotExiParserTest::modelValues).toList();
    }
    if (value == null || value.getClass().isEnum()
        || !value.getClass().getPackageName().startsWith("org.mitre.boogie.xml")) {
      return value;
    }
    Map<String, Object> fields = new HashMap<>();
    fields.put("type", value.getClass().getName());
    for (Class<?> type = value.getClass(); type != Object.class; type = type.getSuperclass()) {
      for (Field field : type.getDeclaredFields()) {
        if (!Modifier.isStatic(field.getModifiers()) && !field.isSynthetic()) {
          field.setAccessible(true);
          try {
            fields.put(type.getName() + "." + field.getName(), modelValues(field.get(value)));
          } catch (IllegalAccessException e) {
            throw new AssertionError("Cannot compare model field " + field, e);
          }
        }
      }
    }
    return fields;
  }
}
