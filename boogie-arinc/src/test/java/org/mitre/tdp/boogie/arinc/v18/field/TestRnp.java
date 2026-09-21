package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

class TestRnp {

  private static final Rnp parser = new Rnp();

  @Test
  void testBogus() {
    assertTrue(parser.apply("0  ").isEmpty());
  }

  @Test
  void testParseValidRnp100() {
    assertEquals(Optional.of(10.), parser.apply("100"));
  }

  @Test
  void testParseValidRnp013() {
    assertEquals(0.001, parser.apply("013").orElseThrow(AssertionError::new), 0.00001);
  }

  @Test
  void testParseValidRnp010() {
    assertEquals(1., parser.apply("010").orElseThrow(AssertionError::new), 0.00001);
  }

  @Test
  void testParseValidRnp302() {
    assertEquals(0.3, parser.apply("302").orElseThrow(AssertionError::new), 0.00001);
  }

  @Test
  void testParseValidRnp031() {
    assertEquals(0.3, parser.apply("031").orElseThrow(AssertionError::new), 0.00001);
  }

  @Test
  void testParseValidRnp990() {
    assertEquals(99.0, parser.apply("990").orElseThrow(AssertionError::new), 0.00001);
  }

  @Test
  void testFiltersInvalidInputs() {
    assertEquals(Optional.empty(), parser.apply("A01"));
  }

  @Test
  void testAllThreeDigitCodesMatchLegacyValuesExactly() {
    for (int code = 0; code < 1_000; code++) {
      String input = new String(new char[]{
          (char) ('0' + code / 100), (char) ('0' + code / 10 % 10), (char) ('0' + code % 10)
      });
      assertLegacyEquivalent(input);
    }
  }

  @Test
  void testCommonRawCodeResultsAreSharedAcrossCallsAndInstances() {
    Rnp anotherParser = new Rnp();
    for (String input : List.of("   ", "010", "050", "031", "020", "100", "051", "040")) {
      Optional<Double> parsed = parser.apply(input);
      assertSame(parsed, parser.apply(input));
      assertSame(parsed, anotherParser.apply(input));
      assertSame(parsed, anotherParser.parse("prefix" + input + "suffix", 6, 9));
    }
  }

  @Test
  void testEmptyAndAsciiWhitespaceRangesRemainAbsent() {
    assertLegacyEquivalent("");
    assertEquals(Optional.empty(), parser.parse("013", 1, 1));
    for (char padding = 0; padding <= ' '; padding++) {
      assertLegacyEquivalent(String.valueOf(padding).repeat(3));
    }
    assertLegacyEquivalent(" \u0000\t\r\n\u001f ");
  }

  @Test
  void testNonstandardInputsPreserveLegacyValuesAndExceptions() {
    for (String input : List.of(
        "", " ", "0", "01", "0  ", " 01", "01 ", " 013", "013 ", "\t013\n", "\u0000013\u0000",
        "\u00a0013", "013\u00a0", "A01", "0A1", "01A", "0 1", "01\t", "1.3", "1e3", "NaN", "Infinity",
        "-00", "-01", "-12", "-99", "+00", "+01", "+12", "+99", "-013", "+013", "-123", "-1999",
        "++1", "--1", "+-1", "1+3", "01-2", "0130", "00999", "00000000000000000000000000000000",
        "012147483647", "012147483648", "+19999999999", "01000000000000000000000003", "999999999999999999999999",
        "\u0660\u0661\u0663", "\uff10\uff11\uff13", "0\u06613", "-\u0661\u0663", "\ud835\udfce13"
    )) {
      assertLegacyEquivalent(input);
    }
  }

  @Test
  void testInvalidRangesPreserveLegacyExceptionTypes() {
    for (int[] range : new int[][]{
        {-1, 2}, {0, 4}, {2, 1}, {-1, -1}, {4, 4}, {-2, 1}, {0, Integer.MAX_VALUE},
        {Integer.MIN_VALUE, 0}, {Integer.MIN_VALUE, Integer.MIN_VALUE + 3},
        {Integer.MAX_VALUE - 1, Integer.MIN_VALUE + 1}, {Integer.MAX_VALUE, Integer.MIN_VALUE + 2}
    }) {
      assertSameOutcome(
          () -> legacyParse("013", range[0], range[1]),
          () -> parser.parse("013", range[0], range[1]),
          "Range " + range[0] + " to " + range[1]
      );
    }
    assertSameOutcome(() -> legacyParse(null, 0, 0), () -> parser.parse(null, 0, 0), "Null source");
    assertSameOutcome(() -> legacyParse(null, 0, 3), () -> parser.parse(null, 0, 3), "Null source with width three");
  }

  private static void assertLegacyEquivalent(String input) {
    String description = "Input " + input.chars().boxed().toList();
    assertSameOutcome(() -> legacyParse(input, 0, input.length()), () -> parser.apply(input), description);
    String source = "prefix" + input + "suffix";
    assertSameOutcome(
        () -> legacyParse(source, 6, 6 + input.length()),
        () -> parser.parse(source, 6, 6 + input.length()),
        description + " at nonzero offset"
    );
  }

  private static void assertSameOutcome(
      Supplier<Optional<Double>> legacy, Supplier<Optional<Double>> actual, String description) {
    Optional<Double> expected;
    try {
      expected = legacy.get();
    } catch (RuntimeException expectedException) {
      RuntimeException actualException = assertThrows(expectedException.getClass(), actual::get, description);
      assertEquals(expectedException.getClass(), actualException.getClass(), description);
      return;
    }
    assertEquals(expected.map(Double::doubleToLongBits), actual.get().map(Double::doubleToLongBits), description);
  }

  private static Optional<Double> legacyParse(String source, int startOffset, int endOffset) {
    return Optional.of(source.substring(startOffset, endOffset))
        .filter(ValidArincNumeric.INSTANCE)
        .filter(s -> s.trim().length() > 2)
        .map(s -> {
          int value = Integer.parseInt(s.substring(0, 2));
          int exp = -Integer.parseInt(s.substring(2));
          return value * Math.pow(10., exp);
        });
  }
}
