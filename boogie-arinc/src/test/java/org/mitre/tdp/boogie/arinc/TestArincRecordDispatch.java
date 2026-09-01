package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.column13;
import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.column6;
import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.prefix;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;

class TestArincRecordDispatch {

  @Test
  void declaredCandidatesPreserveFirstMatchAcrossBothSubsectionColumns() {
    AtomicInteger impossibleMatcherCalls = new AtomicInteger();
    RecordSpec impossible = spec(
        "impossible",
        List.of(column6('E', 'R')),
        rawRecord -> {
          impossibleMatcherCalls.incrementAndGet();
          return true;
        }
    );
    RecordSpec ndb = spec("ndb", List.of(column6('P', 'N')), rawRecord -> true);
    RecordSpec waypoint = spec("waypoint", List.of(column13('P', 'C')), rawRecord -> true);
    String overlappingRecord = record('P', 'N', 'C');

    ArincRecord ndbFirst = ArincRecordParser.standard(impossible, ndb, waypoint).parse(overlappingRecord).orElseThrow();
    ArincRecord waypointFirst = ArincRecordParser.standard(impossible, waypoint, ndb).parse(overlappingRecord).orElseThrow();

    assertAll(
        () -> assertEquals(0, impossibleMatcherCalls.get()),
        () -> assertTrue(ndbFirst.specForField("ndb").isPresent()),
        () -> assertTrue(waypointFirst.specForField("waypoint").isPresent())
    );
  }

  @Test
  void aSpecSelectedByMultipleDiscriminatorsIsTestedOnce() {
    AtomicInteger matcherCalls = new AtomicInteger();
    RecordSpec overlapping = spec(
        "overlapping",
        List.of(column6('P', 'N'), column13('P', 'C')),
        rawRecord -> {
          matcherCalls.incrementAndGet();
          return true;
        }
    );

    ArincRecordParser.standard(overlapping).parse(record('P', 'N', 'C')).orElseThrow();

    assertEquals(1, matcherCalls.get());
  }

  @Test
  void candidatesSharingADiscriminatorParticipateInOriginalSpecOrder() {
    RecordSpec firstCandidate = spec("first", List.of(column13('P', 'C')), rawRecord -> true);
    RecordSpec waypoint = spec("waypoint", List.of(column13('P', 'C')), rawRecord -> true);
    String waypointRecord = record('P', ' ', 'C');

    ArincRecord firstCandidateFirst = ArincRecordParser.standard(firstCandidate, waypoint).parse(waypointRecord).orElseThrow();
    ArincRecord waypointFirst = ArincRecordParser.standard(waypoint, firstCandidate).parse(waypointRecord).orElseThrow();

    assertAll(
        () -> assertTrue(firstCandidateFirst.specForField("first").isPresent()),
        () -> assertTrue(waypointFirst.specForField("waypoint").isPresent())
    );
  }

  @Test
  void prefixAndSectionCandidatesParticipateInOriginalSpecOrder() {
    AtomicInteger irrelevantMatcherCalls = new AtomicInteger();
    RecordSpec irrelevant = spec(
        "irrelevant",
        List.of(prefix("OTHER")),
        rawRecord -> {
          irrelevantMatcherCalls.incrementAndGet();
          return true;
        }
    );
    RecordSpec prefixed = spec("prefixed", List.of(prefix("S"), prefix("SCAN")), rawRecord -> true);
    RecordSpec waypoint = spec("waypoint", List.of(column13('P', 'C')), rawRecord -> true);
    String addressedRecord = withPrefix(record('P', ' ', 'C'), "SCAN");
    String two = withPrefix(record('P', ' ', 'C'), "SUSA");

    ArincRecord prefixFirst = ArincRecordParser.standard(irrelevant, prefixed, waypoint)
        .parse(addressedRecord)
        .orElseThrow();
    ArincRecord twoRecord = ArincRecordParser.standard(irrelevant, prefixed, waypoint)
        .parse(two)
        .orElseThrow();
    ArincRecord waypointFirst = ArincRecordParser.standard(irrelevant, waypoint, prefixed)
        .parse(addressedRecord)
        .orElseThrow();

    assertAll(
        () -> assertEquals(0, irrelevantMatcherCalls.get()),
        () -> assertTrue(prefixFirst.specForField("prefixed").isPresent()),
        () -> assertTrue(twoRecord.specForField("prefixed").isPresent()),
        () -> assertTrue(waypointFirst.specForField("waypoint").isPresent())
    );
  }

  @Test
  void supportsShortFixedWidthPrefixRecords() {
    RecordSpec prefixed = spec("prefixed", 5, List.of(prefix("SH")), rawRecord -> true);

    assertAll(
        () -> assertEquals("SHORT", ArincRecordParser.standard(prefixed).parse("SHORT").orElseThrow().rawRecord()),
        () -> assertThrows(IllegalArgumentException.class,
            () -> ArincRecordParser.standard(prefixed).parse("SHORTER"))
    );
  }

  @Test
  void supportsBit63AndRejectsMoreSpecsThanFitInTheDispatchMask() {
    RecordSpec miss = spec("miss", List.of(column6('X', 'X')), rawRecord -> false);
    RecordSpec winner = spec("winner", List.of(column6('X', 'X')), rawRecord -> true);
    List<RecordSpec> sixtyFourSpecs = Stream.concat(
        IntStream.range(0, 63).mapToObj(index -> miss),
        Stream.of(winner)
    ).toList();
    List<RecordSpec> sixtyFiveSpecs = Stream.concat(
        IntStream.range(0, 64).mapToObj(index -> miss),
        Stream.of(winner)
    ).toList();

    assertAll(
        () -> assertTrue(ArincRecordParser.standard(sixtyFourSpecs)
            .parse(record('X', 'X', 'X')).orElseThrow().specForField("winner").isPresent()),
        () -> assertThrows(IllegalArgumentException.class, () -> ArincRecordParser.standard(sixtyFiveSpecs))
    );
  }

  @Test
  void builtInDispatchMatchesLinearSelectionAcrossEveryVersion() throws IOException {
    List<String> records = new ArrayList<>(fixtureRecords());
    records.addAll(syntheticRecords());

    assertAll(Arrays.stream(ArincVersion.values()).map(version -> () -> assertVersionMatchesLinearly(version, records)));
  }

  @Test
  void rejectsInvalidDiscriminatorValues() {
    assertAll(
        () -> assertThrows(IllegalArgumentException.class, () -> column6('\u0080', 'A')),
        () -> assertThrows(IllegalArgumentException.class, () -> column13('P', '\u0080')),
        () -> assertThrows(IllegalArgumentException.class, () -> RecordDiscriminator.prefix("")),
        () -> assertThrows(IllegalArgumentException.class, () -> RecordDiscriminator.prefix("HDR\u0080"))
    );
  }

  private static void assertVersionMatchesLinearly(ArincVersion version, List<String> records) {
    ArincRecordParser dispatched = ArincRecordParser.standard(version.specs());

    for (int lineNumber = 0; lineNumber < records.size(); lineNumber++) {
      String rawRecord = records.get(lineNumber);
      Optional<RecordSpec> expectedSpec = version.specs().stream()
          .filter(spec -> spec.matchesRecord(rawRecord))
          .findFirst();
      Optional<ArincRecord> actual = dispatched.parse(rawRecord);
      String context = version + " record " + lineNumber;

      assertEquals(expectedSpec.isPresent(), actual.isPresent(), context);
      expectedSpec.ifPresent(spec -> {
        ArincRecord parsedRecord = actual.orElseThrow();
        assertEquals(rawRecord.substring(0, spec.recordLength()), parsedRecord.rawRecord(), context);
        spec.recordFields().forEach(field ->
            assertSame(field.fieldSpec(), parsedRecord.specForField(field.fieldName()).orElseThrow(), context + " field " + field.fieldName()));
      });
    }
  }

  private static List<String> syntheticRecords() {
    return Stream.of(
        headerRecord(),
        record('D', ' ', ' '),
        record('D', 'B', ' '),
        record('E', 'A', ' '),
        record('E', 'P', ' '),
        record('E', 'R', ' '),
        record('P', 'N', ' '),
        record('U', 'C', ' '),
        record('U', 'F', ' '),
        record('U', 'R', ' '),
        record('P', ' ', 'A'),
        airportExtensionRecord(),
        record('P', ' ', 'B'),
        record('P', ' ', 'C'),
        record('P', ' ', 'D'),
        record('P', ' ', 'E'),
        record('P', ' ', 'F'),
        record('P', ' ', 'G'),
        record('P', ' ', 'H'),
        record('P', ' ', 'I'),
        record('P', ' ', 'T'),
        record('H', ' ', 'A'),
        record('H', ' ', 'C'),
        record('H', ' ', 'D'),
        record('H', ' ', 'E'),
        record('H', ' ', 'F'),
        record('H', ' ', 'H'),
        record('H', ' ', 'I'),
        record('H', ' ', 'T'),
        record('P', 'N', 'C')
    ).toList();
  }

  private static String record(char section, char column6, char column13) {
    char[] record = new char[132];
    Arrays.fill(record, ' ');
    record[4] = section;
    record[5] = column6;
    record[12] = column13;
    record[19] = '0';
    record[21] = '0';
    record[22] = '0';
    record[24] = '0';
    record[38] = '0';
    return new String(record);
  }

  private static String headerRecord() {
    char[] record = record('X', 'X', 'X').toCharArray();
    "HDR01".getChars(0, 5, record, 0);
    return new String(record);
  }

  private static String airportExtensionRecord() {
    char[] record = record('P', ' ', 'A').toCharArray();
    record[21] = '2';
    record[22] = 'E';
    return new String(record);
  }

  private static String withPrefix(String record, String prefix) {
    char[] characters = record.toCharArray();
    prefix.getChars(0, prefix.length(), characters, 0);
    return new String(characters);
  }

  private static RecordSpec spec(
      String fieldName,
      List<RecordDiscriminator> discriminators,
      Predicate<String> matcher
  ) {
    return spec(fieldName, 132, discriminators, matcher);
  }

  private static RecordSpec spec(
      String fieldName,
      int recordLength,
      List<RecordDiscriminator> discriminators,
      Predicate<String> matcher
  ) {
    return new RecordSpec() {
      @Override
      public int recordLength() {
        return recordLength;
      }

      @Override
      public List<RecordField<?>> recordFields() {
        return List.of(new RecordField<>(fieldName, new BlankSpec(recordLength)));
      }

      @Override
      public List<RecordDiscriminator> recordDiscriminators() {
        return discriminators;
      }

      @Override
      public boolean matchesRecord(String arincRecord) {
        return matcher.test(arincRecord);
      }
    };
  }

  private static List<String> fixtureRecords() throws IOException {
    try (InputStream input = requireNonNull(
        TestArincRecordDispatch.class.getResourceAsStream("/kjfk-and-friends.txt"))) {
      return new String(input.readAllBytes(), StandardCharsets.US_ASCII).lines().toList();
    }
  }
}
