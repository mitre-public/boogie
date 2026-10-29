package org.mitre.tdp.boogie.arinc;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mitre.tdp.boogie.arinc.v18.AirportSpec;
import org.mitre.tdp.boogie.arinc.v18.AirportValidator;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegSpec;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegValidator;
import org.mitre.tdp.boogie.arinc.v18.RunwaySpec;
import org.mitre.tdp.boogie.arinc.v18.RunwayValidator;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidValidator;
import org.mitre.tdp.boogie.arinc.v18.WaypointSpec;
import org.mitre.tdp.boogie.arinc.v18.WaypointValidator;

/**
 * Checks the CIFP-derived "specials" files (see {@link SpecialsFileGenerator}) look like the FAA specials files to the parsers:
 * fixed-width 132 character records, the expected record types, and records which fail validation only because columns
 * 124-132 (file record number + cycle date) are blank.
 */
class OneshotRecordParserSpecialsTest {

  private static final ArincRecordParser PARSER = ArincVersion.V18.parser();

  private static List<String> resource(String name) throws IOException, URISyntaxException {
    return Files.readAllLines(Path.of(OneshotRecordParserSpecialsTest.class.getResource(name).toURI()));
  }

  private static Class<?> specType(String line) {
    return ArincVersion.V18.specs().stream()
        .filter(spec -> spec.matchesRecord(line))
        .findFirst()
        .<Class<?>>map(RecordSpec::getClass)
        .orElse(Void.class);
  }

  private static Predicate<ArincRecord> validator(Class<?> specType, BiConsumer<ArincRecord, String> missing) {
    return Map.<Class<?>, Predicate<ArincRecord>>of(
        VhfNavaidSpec.class, new VhfNavaidValidator(missing),
        WaypointSpec.class, new WaypointValidator(missing),
        AirportSpec.class, new AirportValidator(missing),
        ProcedureLegSpec.class, new ProcedureLegValidator(missing),
        RunwaySpec.class, new RunwayValidator(missing)
    ).get(specType);
  }

  @ParameterizedTest
  @ValueSource(strings = {"/specials/KGUC_H24_AS_SPECIAL.ari"})
  void testRecordsAreFixedWidth(String file) throws IOException, URISyntaxException {
    List<String> lines = resource(file);
    assertAll(
        () -> assertEquals(53, lines.size()),
        () -> assertTrue(lines.stream().allMatch(line -> line.length() == 132), "All records are 132 characters"),
        () -> assertTrue(lines.get(0).startsWith("HDR "), "Specials header, not HDR01")
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {"/specials/KGUC_H24_AS_SPECIAL.ari"})
  void testParsedRecordTypes(String file) throws IOException, URISyntaxException {
    Map<Class<?>, Long> counts = resource(file).stream().collect(groupingBy(
        OneshotRecordParserSpecialsTest::specType,
        counting()
    ));

    assertEquals(
        Map.of(
            VhfNavaidSpec.class, 1L,
            WaypointSpec.class, 21L,
            AirportSpec.class, 1L,
            ProcedureLegSpec.class, 26L,
            RunwaySpec.class, 1L,
            Void.class, 3L // header, FAF 'W' continuation and MSA records are unsupported
        ),
        counts
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {"/specials/KGUC_H24_AS_SPECIAL.ari"})
  void testRecordsOnlyMissRecordNumberAndCycle(String file) throws IOException, URISyntaxException {
    List<String> lines = resource(file);
    List<String> missing = new ArrayList<>();

    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      Optional<ArincRecord> asIs = PARSER.parse(line);
      if (asIs.isEmpty()) {
        continue;
      }

      String filled = line.substring(0, 123) + String.format("%05d2101", i);
      Predicate<ArincRecord> validator = validator(specType(line), (r, field) -> missing.add(field));
      ArincRecord record = asIs.get();
      ArincRecord filledRecord = PARSER.parse(filled).orElseThrow();

      assertAll(
          line,
          () -> assertTrue(record.optionalField("fileRecordNumber").isEmpty(), "fileRecordNumber"),
          () -> assertTrue(record.optionalField("lastUpdateCycle").isEmpty(), "lastUpdateCycle"),
          () -> assertTrue(validator.negate().test(record), "Invalid as-is"),
          () -> assertTrue(validator.test(filledRecord), "Valid with record number and cycle")
      );
    }

    assertTrue(missing.stream().allMatch("fileRecordNumber"::equals), "Missing fields: " + missing);
  }

  @ParameterizedTest
  @ValueSource(strings = {"/specials/KGUC_H24_AS_SPECIAL.ari"})
  void testOneshotAssemblesNothing(String file) throws IOException, URISyntaxException {
    byte[] bytes = String.join("\n", resource(file)).getBytes(StandardCharsets.US_ASCII);
    var records = OneshotRecordParser.standard(ArincVersion.V18).assembleFrom(new ByteArrayInputStream(bytes));

    assertAll(
        () -> assertEquals(0, records.airports().size(), "Airports"),
        () -> assertEquals(0, records.fixes().size(), "Fixes"),
        () -> assertEquals(0, records.procedures().size(), "Procedures")
    );
  }
}
