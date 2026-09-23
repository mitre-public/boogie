package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.NameFormat;
import org.mitre.tdp.boogie.arinc.v18.field.NavaidClass;
import org.mitre.tdp.boogie.arinc.v18.field.WaypointType;

class TestCompositeFieldDecoding {

  private static final List<LegacyField> FIELDS = List.of(
      new LegacyField(new WaypointType(), "CAD", List.of(
          Set.of("C", "I", "N", "R", "U", "V", "W", "A", "M", "O"),
          Set.of("A", "B", "C", "D", "E", "F", "I", "K", "L", "M", "N", "O", "P", "S", "U", "V", "W"),
          Set.of("D", "E", "F", "Z"))),
      new LegacyField(new NavaidClass(), "VDTDN", List.of(
          Set.of("V", "H", "S", "M"),
          Set.of("D", "T", "I", "M", "O", "C", "N", "P"),
          Set.of("T", "U", "H", "M", "L", "C", " "),
          Set.of("D", "A", "B", "W", " "),
          Set.of("B", "A", "N", " "))),
      new LegacyField(new NameFormat(), "PO ", List.of(
          Set.of("A", "B", "D", "F", "H", "I", "L", "M", "N", "P", "Q", "R", "T", "U"),
          Set.of("O", "M"),
          Set.of()))
  );

  @Test
  void everyUtf16CharacterAtEachPositionMatchesLegacyDecoding() {
    for (LegacyField field : FIELDS) {
      char[] input = field.sample().toCharArray();
      for (int position = 0; position < input.length; position++) {
        char original = input[position];
        for (int character = Character.MIN_VALUE; character <= Character.MAX_VALUE; character++) {
          input[position] = (char) character;
          assertLegacyResult(field, new String(input));
        }
        input[position] = original;
      }
    }
  }

  @Test
  void incorrectWidthsAreRejectedWithoutTrimmingOrPadding() {
    for (LegacyField field : FIELDS) {
      for (String input : List.of("", " ", field.sample().substring(1), field.sample() + " ", " " + field.sample(),
          "\t" + field.sample() + "\n")) {
        assertEquals(Optional.empty(), field.parser().apply(input));
        assertLegacyResult(field, input);
      }
      assertEquals(Optional.empty(), field.parser().parse("prefix", 3, 3));
      assertEquals(Optional.empty(), field.parser().parse("", 0, 0));
      assertEquals(Optional.empty(), field.parser().parse("prefix", 6, 6));
    }
  }

  @Test
  void unsupportedCharactersBecomeSpacesWithoutRejectingTheField() {
    assertEquals(Optional.of(" AZ"), new WaypointType().apply("aAZ"));
    assertEquals(Optional.of("R  "), new WaypointType().apply("R?x"));
    assertEquals(Optional.of("VD DN"), new NavaidClass().apply("VD?DN"));
    assertEquals(Optional.of("H    "), new NavaidClass().apply("H\t\u00a0?x"));
    assertEquals(Optional.of("AO "), new NameFormat().apply("AOZ"));
    assertEquals(Optional.of(" M "), new NameFormat().apply("?M\t"));

    for (LegacyField field : FIELDS) {
      for (String character : List.of(" ", "?", "a", "\t", "\u0000", "\u00a0", "\u2003", "\ud800")) {
        String input = character.repeat(field.sample().length());
        assertEquals(Optional.of(" ".repeat(input.length())), field.parser().apply(input));
        assertLegacyResult(field, input);
      }
    }
  }

  @Test
  void mixedValidAndInvalidColumnsAreNormalizedIndependently() {
    for (LegacyField field : FIELDS) {
      for (int invalidPositions = 0; invalidPositions < 1 << field.sample().length(); invalidPositions++) {
        for (char replacement : new char[]{' ', '?', '\t', '\u00a0', '\udfff'}) {
          char[] input = field.sample().toCharArray();
          for (int position = 0; position < input.length; position++) {
            if ((invalidPositions & (1 << position)) != 0) {
              input[position] = replacement;
            }
          }
          assertLegacyResult(field, new String(input));
        }
      }
    }
  }

  @Test
  void nameFormatsAlwaysReplaceTheReservedThirdColumnWithASpace() {
    FieldSpec<String> parser = new NameFormat();
    for (String input : List.of("POA", "POM", "PO ", "PO\t", "PO\u00a0", "PO\ud800")) {
      assertEquals(Optional.of("PO "), parser.apply(input));
      assertEquals(Optional.of("PO "), parser.parse("prefix" + input + "suffix", 6, 9));
    }
  }

  @Test
  void parsersRejectNullAndInvalidRangesBeforeCheckingWidth() {
    int[][] invalidRanges = {{-1, 1}, {0, 7}, {2, 1}, {-1, -1}, {7, 7}, {Integer.MIN_VALUE, Integer.MAX_VALUE}};
    for (LegacyField field : FIELDS) {
      String description = field.parser().getClass().getSimpleName();
      assertThrows(NullPointerException.class, () -> field.parser().apply(null), description);
      assertThrows(NullPointerException.class, () -> field.parser().parse(null, 0, 0), description);
      for (int[] range : invalidRanges) {
        assertThrows(IndexOutOfBoundsException.class, () -> field.parser().parse("??????", range[0], range[1]), description);
      }
    }
  }

  private static void assertLegacyResult(LegacyField field, String input) {
    Optional<String> expected = field.parseLegacy(input);
    assertEquals(expected, field.parser().apply(input), () -> description(field, input));
    assertEquals(expected, field.parser().parse("prefix" + input + "suffix", 6, 6 + input.length()),
        () -> description(field, input) + " at nonzero offset");
  }

  private static String description(LegacyField field, String input) {
    return field.parser().getClass().getSimpleName() + " input " + input.chars().boxed().toList();
  }

  private record LegacyField(FieldSpec<String> parser, String sample, List<Set<String>> allowedColumns) {

    // Preserve the original per-character substring, set membership, and concatenation as an independent oracle.
    private Optional<String> parseLegacy(String input) {
      return Optional.of(input).filter(value -> value.length() == allowedColumns.size()).map(value -> {
        String normalized = "";
        for (int position = 0; position < allowedColumns.size(); position++) {
          String character = value.substring(position, position + 1);
          normalized = normalized.concat(allowedColumns.get(position).contains(character) ? character : " ");
        }
        return normalized;
      });
    }
  }
}
