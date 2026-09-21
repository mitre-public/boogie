package org.mitre.boogie.xml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mitre.boogie.xml.exi.ExiAssertions;
import org.mitre.boogie.xml.exi.ExiCodec;
import org.mitre.boogie.xml.exi.ExiOptions;
import org.mitre.boogie.xml.exi.ExiSchema;
import org.mitre.boogie.xml.fixtures.ArincFixtureSchema;
import org.mitre.boogie.xml.model.ArincRecords;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OneshotExiParserTest {

  @Test
  @EnabledIfSystemProperty(named = "boogie.xml.conversionFixtures", matches = "true", disabledReason = "Fixture generation is opt-in")
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
  @EnabledIfSystemProperty(named = "boogie.xml.conversionFixtures", matches = "true", disabledReason = "Fixture generation is opt-in")
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
    try (InputStream input = parserFixture()) {
      new ExiCodec(options).encode(input, encoded);
    }

    byte[] exi = encoded.toByteArray();
    ArincRecords expected;
    try (InputStream input = parserFixture()) {
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
          () -> assertEquals(1, assembled.airports().size(), "Assembled airports"),
          () -> assertEquals(4, assembled.fixes().size(), "Assembled fixes"),
          () -> assertEquals(1, assembled.airways().size(), "Assembled airways"),
          () -> assertEquals(1, assembled.procedures().size(), "Assembled procedures"),
          () -> assertEquals(1, assembled.heliports().size(), "Assembled heliports"),
          () -> assertEquals(List.of("ALPHA", "BRAVO"), assembled.airways().iterator().next().legs().stream()
              .map(leg -> leg.associatedFix().orElseThrow().fixIdentifier()).toList()),
          () -> assertEquals("TST", assembled.procedures().iterator().next().transitions().iterator().next()
              .legs().get(1).recommendedNavaid().orElseThrow().fixIdentifier())
      );
    }
  }

  private static void assertModelValues(Object expected, Object actual, String description) {
    assertEquals(modelValues(expected), modelValues(actual), description + " must retain all field values");
  }

  private static InputStream fixture() {
    return OneshotExiParserTest.class.getResourceAsStream("/v23_4/gibberish-sample.xml");
  }

  private static InputStream parserFixture() {
    return OneshotExiParserTest.class.getResourceAsStream("/v23_4/parser-sample.xml");
  }

  private static ExiSchema fixtureSchema() throws Exception {
    return ArincFixtureSchema.generated(Path.of("build", "exi", "gibberish-sample-schemas"));
  }

  private static void assertWrittenExi(Path exiFile, ExiOptions options) throws Exception {
    // Use decoder defaults so the file's header must supply the encoding settings.
    ExiCodec decoder = new ExiCodec(ExiOptions.schemaLess(), options.schema().stream().toList());
    assertAll(exiFile + ": EXI artifact",
        () -> ExiAssertions.assertCookie(exiFile),
        () -> {
          try (InputStream source = fixture()) {
            ExiAssertions.assertSameStructure(source, exiFile, decoder);
          }
        });
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
