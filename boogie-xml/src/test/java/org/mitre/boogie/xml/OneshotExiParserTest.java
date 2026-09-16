package org.mitre.boogie.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.SchemaOutputResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mitre.boogie.xml.exi.ExiAssertions;
import org.mitre.boogie.xml.exi.ExiCodec;
import org.mitre.boogie.xml.exi.ExiOptions;
import org.mitre.boogie.xml.exi.ExiSchema;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    assertWrittenExi(exiFile, options);
  }

  @Test
  void writesGibberishSampleSchemaInformedExi() throws Exception {
    ExiOptions options = ExiOptions.schemaInformed(fixtureSchema()).withCompression(true);
    Path exiFile = Path.of("build", "exi", "gibberish-sample-schema-informed.exi");
    Files.createDirectories(exiFile.getParent());
    try (InputStream input = fixture();
         OutputStream encoded = new BufferedOutputStream(Files.newOutputStream(exiFile))) {
      new ExiCodec(options).encode(input, encoded);
    }
    assertWrittenExi(exiFile, options);
  }

  @ParameterizedTest(name = "schema-informed = {0}")
  @ValueSource(booleans = {false, true})
  void parsesTheArincFixtureAsExiIntoModelsAndAssembledRecords(boolean schemaInformed) throws Exception {
    ExiOptions options = (schemaInformed ? ExiOptions.schemaInformed(fixtureSchema()) : ExiOptions.schemaLess())
        .withCompression(true);
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
          () -> assertModelValues(expected.waypoints(), actual.waypoints(), "Waypoint models"),
          () -> assertModelValues(expected.airports(), actual.airports(), "Airport models and nested procedures"),
          () -> assertModelValues(expected.ndbNavaids(), actual.ndbNavaids(), "NDB models"),
          () -> assertModelValues(expected.vhfNavaids(), actual.vhfNavaids(), "VHF models"),
          () -> assertModelValues(expected.arincAirways(), actual.arincAirways(), "Airway models and references"),
          () -> assertModelValues(expected.holdingPatterns(), actual.holdingPatterns(), "Holding pattern models"),
          () -> assertModelValues(expected.heliports(), actual.heliports(), "Heliport models"),
          () -> assertEquals(5, assembled.airports().size(), "Assembled airports"),
          () -> assertEquals(13, assembled.fixes().size(), "Assembled fixes"),
          () -> assertEquals(5, assembled.airways().size(), "Assembled airways"),
          () -> assertEquals(75, assembled.procedures().size(), "Assembled procedures"),
          () -> assertEquals(5, assembled.heliports().size(), "Assembled heliports")
      );
    }
  }

  private static void assertModelValues(Object expected, Object actual, String description) {
    // Avoid rendering the entire publication in an assertion failure (hundreds of MB of text).
    assertTrue(modelValues(expected).equals(modelValues(actual)), description + " must retain all field values");
  }

  private static InputStream fixture() {
    return OneshotExiParserTest.class.getResourceAsStream("/v23_4/gibberish-sample.xml");
  }

  private static ExiSchema fixtureSchema() throws Exception {
    // The official ARINC XSDs are external integration inputs, absent from a clean checkout.
    // Generate a test schema from the checked-in JAXB classes and retain it with the EXI artifact.
    Path directory = Files.createDirectories(Path.of("build", "exi", "gibberish-sample-schemas"));
    Map<String, Path> schemas = new HashMap<>();
    JAXBContext.newInstance(AeroPublication.class).generateSchema(new SchemaOutputResolver() {
      @Override
      public Result createOutput(String namespaceUri, String suggestedFileName) {
        Path file = directory.resolve(suggestedFileName);
        schemas.put(namespaceUri, file);
        return new StreamResult(file.toFile());
      }
    });
    Path rootXsd = Objects.requireNonNull(schemas.get(""), "Missing generated publication schema");
    return ExiSchema.compile("urn:boogie:test:arinc424:jaxb:23.4", rootXsd);
  }

  private static void assertWrittenExi(Path exiFile, ExiOptions options) throws Exception {
    ExiAssertions.assertCookie(exiFile);
    // Use decoder defaults so the file's header must supply the encoding settings.
    ExiCodec decoder = new ExiCodec(ExiOptions.schemaLess(), options.schema().stream().toList());
    try (InputStream source = fixture()) {
      ExiAssertions.assertSameStructure(source, exiFile, decoder);
    }
  }

  // Model equals methods include JAXB supplemental data, whose generated classes use identity
  // equality. Compare every model/JAXB field by value, including nested legs and IDREF strings.
  private static Object modelValues(Object value) {
    if (value instanceof BigDecimal decimal) {
      // EXI preserves a decimal's numeric value, not its original number of trailing zeroes.
      return decimal.stripTrailingZeros();
    }
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
