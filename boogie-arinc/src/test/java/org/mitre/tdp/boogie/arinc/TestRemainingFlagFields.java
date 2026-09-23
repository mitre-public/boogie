package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryCode;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.DirectionRestriction;
import org.mitre.tdp.boogie.arinc.v18.field.EuIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirectionValid;

class TestRemainingFlagFields {

  private static final Set<String> LEGACY_TRUE_VALUES = Set.of("Y", "y", "TRUE", "True", "true", "YES", "Yes", "yes");
  private static final Set<String> LEGACY_LEVEL_VALUES = Set.of("B", "H", "L");
  private static final Set<String> LEGACY_DIRECTION_VALUES = Set.of(" ", "F", "B");
  private static final Map<String, String> LEGACY_BOUNDARY_VALUES = Map.of(
      "U", "USA", "C", "CAN", "P", "PAC", "L", "LAM", "S", "SAM",
      "1", "SPA", "E", "EUR", "2", "EEU", "M", "MES", "A", "AFR"
  );

  // Keep the original substring-based decoding as an independent compatibility oracle.
  private static final List<LegacyField> FIELDS = List.of(
      new LegacyField(new TurnDirectionValid(), input -> Optional.of(input)
          .map(String::trim)
          .map(value -> LEGACY_TRUE_VALUES.contains(value.trim()))),
      new LegacyField(new EuIndicator(), input -> Optional.of(input).map("Y"::equalsIgnoreCase)),
      new LegacyField(Level.SPEC, input -> Optional.of(input)
          .map(String::trim)
          .filter(LEGACY_LEVEL_VALUES::contains)
          .map(Level::valueOf)),
      new LegacyField(new BoundaryCode(), input -> Optional.of(input)
          .map(String::trim)
          .filter(value -> !value.isEmpty())
          .map(LEGACY_BOUNDARY_VALUES::get)
          .map(CustomerAreaCode::valueOf)),
      new LegacyField(new DirectionRestriction(), input -> Optional.of(input).filter(LEGACY_DIRECTION_VALUES::contains))
  );

  @Test
  void allSingleCharacterValuesMatchLegacyDecoding() {
    for (int character = Character.MIN_VALUE; character <= Character.MAX_VALUE; character++) {
      String input = String.valueOf((char) character);
      for (LegacyField field : FIELDS) {
        assertLegacyResult(field, input);
      }
    }
  }

  @Test
  void standaloneValuesPreserveLegacyWidthsAndBooleanSpellings() {
    for (String input : List.of(
        "", " ", "  ", "Y", "y", "N", "n", "T", "F", "0", "1", "false", "FALSE",
        " Y ", " y ", "YY", "Y Y", "Y\u0000Y", "SPEC", "BB", " B ", " H ", " L ",
        "USA", " U ", "U U", "UU", " F ", " B ", "FB", "\ud83d\ude80"
    )) {
      for (LegacyField field : FIELDS) {
        assertLegacyResult(field, input);
      }
    }
    for (String word : List.of("TRUE", "YES")) {
      for (int casing = 0; casing < 1 << word.length(); casing++) {
        char[] characters = word.toCharArray();
        for (int index = 0; index < characters.length; index++) {
          if ((casing & (1 << index)) != 0) {
            characters[index] = Character.toLowerCase(characters[index]);
          }
        }
        String input = new String(characters);
        for (LegacyField field : FIELDS) {
          assertLegacyResult(field, input);
          assertLegacyResult(field, " \t" + input + "\r\n");
        }
      }
    }
  }

  @Test
  void trimmingPreservesAsciiControlsAndSignificantUnicodeWhitespace() {
    for (char padding = 0; padding <= ' '; padding++) {
      assertPaddingMatchesLegacy(String.valueOf(padding));
    }
    for (String padding : List.of("\u0085", "\u00a0", "\u1680", "\u2003", "\u2028", "\u202f", "\u3000", "\ufeff")) {
      assertPaddingMatchesLegacy(padding);
    }
  }

  @Test
  void emptyRangesRetainEachFieldsAbsentOrFalseResult() {
    for (LegacyField field : FIELDS) {
      Optional<?> expected = field.parseLegacy().apply("");
      assertEquals(expected, field.parser().parse("", 0, 0));
      assertEquals(expected, field.parser().parse("prefix", 0, 0));
      assertEquals(expected, field.parser().parse("prefix", 3, 3));
      assertEquals(expected, field.parser().parse("prefix", 6, 6));
    }
  }

  @Test
  void parsersRejectNullAndInvalidRangesBeforeDecoding() {
    int[][] invalidRanges = {{-1, 1}, {0, 4}, {2, 1}, {-1, -1}, {4, 4}, {Integer.MIN_VALUE, Integer.MAX_VALUE}};
    for (LegacyField field : FIELDS) {
      FieldSpec<?> parser = field.parser();
      String description = parser.getClass().getSimpleName();
      assertThrows(NullPointerException.class, () -> parser.apply(null), description);
      assertThrows(NullPointerException.class, () -> parser.parse(null, 0, 0), description);
      for (int[] range : invalidRanges) {
        assertThrows(IndexOutOfBoundsException.class, () -> parser.parse("???", range[0], range[1]), description);
      }
    }
  }

  @Test
  void commonSingleCharacterResultsAreSharedAcrossRepeatedParsing() {
    assertSharedResults(new TurnDirectionValid(), List.of("Y", "y", " ", "N"));
    assertSharedResults(new EuIndicator(), List.of("Y", "y", " ", "N"));
    assertSharedResults(Level.SPEC, List.of("B", "H", "L"));
    assertSharedResults(new BoundaryCode(), List.of("U", "C", "P", "L", "S", "1", "E", "2", "M", "A"));
    assertSharedResults(new DirectionRestriction(), List.of(" ", "F", "B"));
  }

  private static void assertPaddingMatchesLegacy(String padding) {
    for (LegacyField field : FIELDS) {
      assertLegacyResult(field, padding);
      for (String value : List.of("Y", "y", "TRUE", "Yes", "B", "H", "L", "U", "1", "2", "F")) {
        assertLegacyResult(field, padding + value + padding);
      }
    }
  }

  private static void assertLegacyResult(LegacyField field, String input) {
    Optional<?> expected = field.parseLegacy().apply(input);
    assertEquals(expected, field.parser().apply(input), () -> description(field.parser(), input));
    assertEquals(expected, field.parser().parse("prefix" + input + "suffix", 6, 6 + input.length()),
        () -> description(field.parser(), input) + " at nonzero offset");
  }

  private static void assertSharedResults(FieldSpec<?> parser, List<String> inputs) {
    for (String input : inputs) {
      Optional<?> parsed = parser.apply(input);
      assertSame(parsed, parser.apply(new String(input.toCharArray())), () -> description(parser, input));
      assertSame(parsed, parser.parse("prefix" + input + "suffix", 6, 6 + input.length()),
          () -> description(parser, input) + " at nonzero offset");
    }
  }

  private static String description(FieldSpec<?> parser, String input) {
    return parser.getClass().getSimpleName() + " input " + input.chars().boxed().toList();
  }

  private record LegacyField(FieldSpec<?> parser, Function<String, Optional<?>> parseLegacy) {
  }
}
