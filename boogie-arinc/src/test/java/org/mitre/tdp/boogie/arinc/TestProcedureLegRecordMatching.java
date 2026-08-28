package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;

class TestProcedureLegRecordMatching {

  @Test
  void versionsMatchLegacyMatcherForEveryRelevantCharacterCombination() {
    assertAll(records().flatMap(record -> specs().map(spec -> (Executable) () ->
        assertEquals(legacyMatches(record), spec.matchesRecord(record),
            () -> spec.getClass().getName() + " differed for " + describe(record)))));
  }

  @Test
  void versionsPreserveLegacyMalformedRecordBehavior() {
    String matchingButTruncated = truncatedRecord('P', 'D');
    String nonMatchingTruncated = truncatedRecord('P', 'A');

    assertAll(Stream.concat(
        Stream.of(
            () -> assertThrows(StringIndexOutOfBoundsException.class, () -> legacyMatches(matchingButTruncated)),
            () -> assertThrows(NullPointerException.class, () -> legacyMatches(null))
        ),
        specs().flatMap(spec -> Stream.of(
            (Executable) () -> assertEquals(legacyMatches(""), spec.matchesRecord("")),
            (Executable) () -> assertEquals(legacyMatches(nonMatchingTruncated),
                spec.matchesRecord(nonMatchingTruncated)),
            (Executable) () -> assertThrows(StringIndexOutOfBoundsException.class,
                () -> spec.matchesRecord(matchingButTruncated)),
            (Executable) () -> assertThrows(NullPointerException.class, () -> spec.matchesRecord(null))
        ))
    ));
  }

  private static Stream<RecordSpec> specs() {
    return Stream.of(
        new org.mitre.tdp.boogie.arinc.v18.ProcedureLegSpec(),
        new org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec(),
        new org.mitre.tdp.boogie.arinc.v20.ProcedureLegSpec(),
        new org.mitre.tdp.boogie.arinc.v21.ProcedureLegSpec(),
        new org.mitre.tdp.boogie.arinc.v22.ProcedureLegSpec()
    );
  }

  private static Stream<String> records() {
    return Stream.of('P', 'H', 'X')
        .flatMap(section -> Stream.of('D', 'E', 'F', 'A')
            .flatMap(subSection -> Stream.of('0', '1', '2', 'A')
                .map(continuation -> record(section, subSection, continuation))));
  }

  private static String record(char section, char subSection, char continuation) {
    char[] record = new char[132];
    Arrays.fill(record, ' ');
    record[4] = section;
    record[12] = subSection;
    record[38] = continuation;
    return new String(record);
  }

  private static String truncatedRecord(char section, char subSection) {
    char[] record = new char[13];
    Arrays.fill(record, ' ');
    record[4] = section;
    record[12] = subSection;
    return new String(record);
  }

  private static boolean legacyMatches(String arincRecord) {
    boolean airportRecord = Stream.of("PD", "PE", "PF").anyMatch(candidate ->
        arincRecord.regionMatches(4, candidate, 0, 1)
            && arincRecord.regionMatches(12, candidate, 1, 1));
    boolean heliportRecord = Stream.of("HD", "HE", "HF").anyMatch(candidate ->
        arincRecord.regionMatches(4, candidate, 0, 1)
            && arincRecord.regionMatches(12, candidate, 1, 1));
    return (airportRecord || heliportRecord)
        && PrimaryRecord.INSTANCE.test(arincRecord.substring(38, 39));
  }

  private static String describe(String record) {
    return List.of(record.charAt(4), record.charAt(12), record.charAt(38)).toString();
  }
}
