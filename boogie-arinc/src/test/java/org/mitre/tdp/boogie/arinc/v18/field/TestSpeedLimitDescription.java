package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestSpeedLimitDescription {

  private static final SpeedLimitDescription parser = new SpeedLimitDescription();

  @Test
  void testBlankDescriptionIsAllowed() {
    assertEquals(Optional.of("@"), parser.apply(" "), "Space should be allowed and mapped to '@'");
  }

  @Test
  void testApersandDescriptionIsAllowed() {
    assertEquals(Optional.of("@"), parser.apply("@"));
  }

  @Test
  void testPlusDescriptionIsAllowed() {
    assertEquals(Optional.of("+"), parser.apply("+"));
  }

  @Test
  void testMinusDescriptionIsAllowed() {
    assertEquals(Optional.of("-"), parser.apply("-"));
  }

  @Test
  void testFiltersUnsupportedInput() {
    assertEquals(Optional.empty(), parser.apply("HI"));
  }

  @Test
  void testExactVocabularyAtStandaloneAndNonzeroOffsets() {
    Map<String, String> expected = Map.of(" ", "@", "@", "@", "+", "+", "-", "-");
    for (char character = 0; character < 128; character++) {
      String input = String.valueOf(character);
      Optional<String> value = Optional.ofNullable(expected.get(input));
      assertEquals(value, parser.apply(input), "Character " + (int) character);
      assertEquals(value, parser.parse("--" + input + "++", 2, 3), "Character " + (int) character);
    }
  }

  @Test
  void testRejectsPaddingNonAsciiAndInvalidRanges() {
    for (String input : List.of("", "  ", " +", "+ ", "@@", "\t+\n", "\u00a0", "\u2003", "\u012b", "\u0140", "\uff0b")) {
      assertEquals(Optional.empty(), parser.apply(input));
      assertEquals(Optional.empty(), parser.parse("x" + input + "y", 1, 1 + input.length()));
    }
    for (int[] range : new int[][]{{-1, 0}, {0, 2}, {1, 0}, {2, 2}}) {
      assertThrows(IndexOutOfBoundsException.class, () -> parser.parse("+", range[0], range[1]));
    }
  }

  @Test
  void testSuccessfulResultsAreSharedAcrossCallsAndInstances() {
    SpeedLimitDescription anotherParser = new SpeedLimitDescription();
    for (String input : List.of(" ", "@", "+", "-")) {
      Optional<String> parsed = parser.apply(input);
      assertSame(parsed, parser.apply(input));
      assertSame(parsed, anotherParser.apply(input));
      assertSame(parsed, anotherParser.parse("x" + input + "y", 1, 2));
    }
  }
}
