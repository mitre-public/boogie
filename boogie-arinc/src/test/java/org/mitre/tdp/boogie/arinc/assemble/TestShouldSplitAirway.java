package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;

class TestShouldSplitAirway {

  private static final ShouldSplitAirway SHOULD_SPLIT = ShouldSplitAirway.INSTANCE;

  @Test
  void increasingSequencesWithinEachThousandStayTogether() {
    for (int prefix = 0; prefix < 10; prefix++) {
      int first = prefix * 1_000;
      assertFalse(SHOULD_SPLIT.test(leg("J1", first), leg("J1", first + 999)));
      assertFalse(SHOULD_SPLIT.test(leg("J1", first + 10), leg("J1", first + 20)));
    }
  }

  @Test
  void crossingAThousandBoundarySplitsTheAirway() {
    for (int boundary = 1_000; boundary < 10_000; boundary += 1_000) {
      assertTrue(SHOULD_SPLIT.test(leg("J1", boundary - 1), leg("J1", boundary)));
    }
    assertTrue(SHOULD_SPLIT.test(leg("J1", 10), leg("J1", 3_050)));
  }

  @Test
  void equalOrDecreasingSequencesSplitTheAirway() {
    int[] sequences = {Integer.MIN_VALUE, -10_000, -1, 0, 10, 999, 1_000, 1_020, 9_999, 10_000, Integer.MAX_VALUE};
    for (int sequence : sequences) {
      assertTrue(SHOULD_SPLIT.test(leg("J1", sequence), leg("J1", sequence)));
      if (sequence > Integer.MIN_VALUE) {
        assertTrue(SHOULD_SPLIT.test(leg("J1", sequence), leg("J1", sequence - 1)));
      }
    }
  }

  @Test
  void routeIdentifiersAreComparedByValue() {
    assertFalse(SHOULD_SPLIT.test(leg(new String("J1"), 10), leg(new String("J1"), 20)));
    assertTrue(SHOULD_SPLIT.test(leg("J1", 10), leg("J2", 20)));
  }

  @Test
  void matchesFormattedPrefixForEveryAdjacentFourDigitSequence() {
    for (int previous = 0; previous < 9_999; previous++) {
      assertMatchesFormattedPrefix(previous, previous + 1);
    }
  }

  @Test
  void matchesFormattedPrefixAtIntegerAndDecimalBoundaries() {
    int[] sequences = {
        Integer.MIN_VALUE, -1_000_000_000, -100_000, -10_001, -10_000, -9_999,
        -1_001, -1_000, -999, -100, -10, -1, 0, 1, 9, 10, 99, 100, 999,
        1_000, 1_001, 1_999, 2_000, 9_999, 10_000, 10_001, 19_999, 20_000,
        99_999, 100_000, 199_999, 200_000, 999_999_999, 1_000_000_000,
        1_999_999_999, 2_000_000_000, Integer.MAX_VALUE
    };
    for (int previous : sequences) {
      for (int next : sequences) {
        assertMatchesFormattedPrefix(previous, next);
      }
    }
  }

  @Test
  void matchesFormattedPrefixForArbitraryIntegerSequences() {
    Random random = new Random(424);
    for (int sample = 0; sample < 2_000; sample++) {
      assertMatchesFormattedPrefix(random.nextInt(), random.nextInt());
    }
  }

  private static void assertMatchesFormattedPrefix(int previous, int next) {
    boolean expected = !String.format("%04d", previous).startsWith(String.format("%04d", next).substring(0, 1))
        || next <= previous;
    assertEquals(expected, SHOULD_SPLIT.test(leg("J1", previous), leg("J1", next)),
        () -> "Sequence pair: " + previous + " -> " + next);
  }

  private static ArincAirwayLeg leg(String routeIdentifier, int sequenceNumber) {
    return new ArincAirwayLeg.Builder()
        .routeIdentifier(routeIdentifier)
        .sequenceNumber(sequenceNumber)
        .fileRecordNumber(0)
        .build();
  }
}
