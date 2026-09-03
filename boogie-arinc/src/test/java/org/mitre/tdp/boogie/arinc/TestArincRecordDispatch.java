package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.continuationColumn6;
import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.continuationColumn13;
import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.prefix;
import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.primaryColumn13;
import static org.mitre.tdp.boogie.arinc.RecordDiscriminator.primaryColumn6;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;

class TestArincRecordDispatch {

  @Test
  void declaredCandidatesPreserveFirstMatchAcrossBothSubsectionColumns() {
    RecordSpec impossible = spy(spec("impossible", List.of(primaryColumn6('E', 'R', 21))));
    RecordSpec ndb = spec("ndb", List.of(primaryColumn6('P', 'N', 21)));
    RecordSpec waypoint = spec("waypoint", List.of(primaryColumn13('P', 'C', 21)));
    String overlappingRecord = record('P', 'N', 'C');

    ArincRecord ndbFirst = ArincRecordParser.standard(impossible, ndb, waypoint).parse(overlappingRecord).orElseThrow();
    ArincRecord waypointFirst = ArincRecordParser.standard(impossible, waypoint, ndb).parse(overlappingRecord).orElseThrow();

    assertAll(
        () -> verify(impossible, never()).matchesRecord(anyString()),
        () -> assertTrue(ndbFirst.specForField("ndb").isPresent()),
        () -> assertTrue(waypointFirst.specForField("waypoint").isPresent())
    );
  }

  @Test
  void aSpecSelectedByMultipleDiscriminatorsIsTestedOnce() {
    RecordSpec overlapping = spy(spec(
        "overlapping",
        List.of(primaryColumn6('P', 'N', 21), primaryColumn13('P', 'C', 21))
    ));
    String rawRecord = record('P', 'N', 'C');

    ArincRecord parsed = ArincRecordParser.standard(overlapping).parse(rawRecord).orElseThrow();

    assertAll(
        () -> verify(overlapping, times(1)).matchesRecord(rawRecord),
        () -> assertTrue(parsed.specForField("overlapping").isPresent())
    );
  }

  @Test
  void candidatesSharingADiscriminatorParticipateInOriginalSpecOrder() {
    RecordSpec firstCandidate = spec("first", List.of(primaryColumn13('P', 'C', 21)));
    RecordSpec waypoint = spec("waypoint", List.of(primaryColumn13('P', 'C', 21)));
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
    RecordSpec irrelevant = spy(spec("irrelevant", List.of(prefix("OTHER"))));
    RecordSpec prefixed = spec("prefixed", List.of(prefix("S"), prefix("SCAN")));
    RecordSpec waypoint = spec("waypoint", List.of(primaryColumn13('P', 'C', 21)));
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
        () -> verify(irrelevant, never()).matchesRecord(anyString()),
        () -> assertTrue(prefixFirst.specForField("prefixed").isPresent()),
        () -> assertTrue(twoRecord.specForField("prefixed").isPresent()),
        () -> assertTrue(waypointFirst.specForField("waypoint").isPresent())
    );
  }

  @Test
  void supportsShortFixedWidthPrefixRecords() {
    RecordSpec prefixed = spec("prefixed", 5, List.of(prefix("SH")));

    assertAll(
        () -> assertEquals("SHORT", ArincRecordParser.standard(prefixed).parse("SHORT").orElseThrow().rawRecord()),
        () -> assertThrows(IllegalArgumentException.class,
            () -> ArincRecordParser.standard(prefixed).parse("SHORTER"))
    );
  }

  @Test
  void supportsBit63AndRejectsMoreSpecsThanFitInTheDispatchMask() {
    RecordSpec miss = spec("miss", List.of(primaryColumn6('Y', 'Y', 21)));
    RecordSpec winner = spec("winner", List.of(primaryColumn6('X', 'X', 21)));
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
  void aRecordSpecSnapshotsAndExposesImmutableDiscriminators() {
    List<RecordDiscriminator> discriminators = new ArrayList<>();
    discriminators.add(prefix("ONE"));
    RecordSpec recordSpec = spec("prefixed", 5, discriminators);

    discriminators.set(0, prefix("TWO"));

    assertAll(
        () -> assertTrue(recordSpec.matchesRecord("ONE  ")),
        () -> assertFalse(recordSpec.matchesRecord("TWO  ")),
        () -> assertThrows(UnsupportedOperationException.class, () -> recordSpec.recordDiscriminators().clear())
    );
  }

  @Test
  void aColumn6ContinuationDiscriminatorChecksNumberAndApplicationType() {
    RecordSpec continuation = spec(
        "continuation",
        List.of(continuationColumn6('E', 'R', 21, 'E'))
    );

    assertAll(
        () -> assertTrue(continuation.matchesRecord(continuationRecord('E', 'R', '2', 'E'))),
        () -> assertFalse(continuation.matchesRecord(continuationRecord('E', 'R', '1', 'E'))),
        () -> assertFalse(continuation.matchesRecord(continuationRecord('E', 'R', '2', 'X'))),
        () -> assertTrue(ArincRecordParser.standard(continuation)
            .parse(continuationRecord('E', 'R', '2', 'E')).isPresent())
    );
  }

  @Test
  void builtInDispatchMatchesLinearSelectionAcrossEveryVersion() throws IOException {
    List<String> records = new ArrayList<>(fixtureRecords());
    records.addAll(syntheticRecords());

    assertAll(Arrays.stream(ArincVersion.values()).map(version -> () -> assertVersionMatchesLinearly(version, records)));
  }

  @Test
  void builtInRecordSpecsAreMutuallyExclusiveAcrossEveryVersion() throws IOException {
    List<String> records = new ArrayList<>(fixtureRecords());
    List<String> syntheticRecords = syntheticRecords();
    // The final synthetic record intentionally overlaps column-6 and column-13 specs for dispatch-order testing.
    records.addAll(syntheticRecords.subList(0, syntheticRecords.size() - 1));

    assertAll(Arrays.stream(ArincVersion.values()).map(version -> () ->
        assertBuiltInSpecsAreMutuallyExclusive(version, records)));
  }

  @Test
  void rejectsInvalidDiscriminatorValues() {
    assertAll(
        () -> assertThrows(IllegalArgumentException.class, () -> primaryColumn6('\u0080', 'A', 21)),
        () -> assertThrows(IllegalArgumentException.class, () -> primaryColumn13('P', '\u0080', 21)),
        () -> assertThrows(IllegalArgumentException.class, () -> primaryColumn13('P', 'A', -1)),
        () -> assertThrows(IllegalArgumentException.class, () -> continuationColumn13('P', 'A', 21, '\0')),
        () -> assertThrows(IllegalArgumentException.class, () -> continuationColumn13('P', 'A', 21, '\u0080')),
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

  private static void assertBuiltInSpecsAreMutuallyExclusive(ArincVersion version, List<String> records) {
    for (int lineNumber = 0; lineNumber < records.size(); lineNumber++) {
      String rawRecord = records.get(lineNumber);
      List<RecordSpec> matchingSpecs = version.specs().stream()
          .filter(spec -> spec.matchesRecord(rawRecord))
          .toList();

      assertTrue(
          matchingSpecs.size() <= 1,
          version + " record " + lineNumber + " matched multiple specs: "
              + matchingSpecs.stream().map(spec -> spec.getClass().getSimpleName()).toList()
      );
    }

    assertAll(version.specs().stream().map(spec -> () ->
        assertTrue(
            records.stream().anyMatch(spec::matchesRecord),
            version + " has no representative record for " + spec.getClass().getSimpleName()
        )
    ));
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

  private static String continuationRecord(
      char section,
      char subsection,
      char continuationNumber,
      char applicationType
  ) {
    char[] record = record(section, subsection, ' ').toCharArray();
    record[21] = continuationNumber;
    record[22] = applicationType;
    return new String(record);
  }

  private static String withPrefix(String record, String prefix) {
    char[] characters = record.toCharArray();
    prefix.getChars(0, prefix.length(), characters, 0);
    return new String(characters);
  }

  private static RecordSpec spec(
      String fieldName,
      List<RecordDiscriminator> discriminators
  ) {
    return spec(fieldName, 132, discriminators);
  }

  private static RecordSpec spec(
      String fieldName,
      int recordLength,
      List<RecordDiscriminator> discriminators
  ) {
    return new RecordSpec(discriminators) {
      @Override
      public int recordLength() {
        return recordLength;
      }

      @Override
      public List<RecordField<?>> recordFields() {
        return List.of(new RecordField<>(fieldName, new BlankSpec(recordLength)));
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
