package org.mitre.tdp.boogie.arinc.utils;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

/**
 * Class of common string utilities useful in ARINC string parsing.
 */
public final class ArincStrings {

  public static final Predicate<String> isBlank = s -> s.trim().isEmpty();

  // MISSING DECIMAL LOCATIONS
  private static final int TENTHS = 1;
  private static final int HUNDREDTHS = 2;
  private static final int THOUSANDTHS = 3;

  private ArincStrings() {
    throw new IllegalStateException("Cannot instantiate static utility class.");
  }

  /**
   * Returns a copy of the given string with all of the internal characters replaced with blanks.
   */
  public static String copyWithBlanks(String s) {
    return IntStream.range(0, s.length()).mapToObj(i -> " ").reduce("", String::concat);
  }

  /**
   * Common utility for parsing doubles encoded as #### where the last number is the tenths.
   *
   * e.g. 0456 -> 45.6, 1245 -> 124.5, 0005 -> 0.5, 0001234 -> 123.4
   */
  public static Double parseDoubleWithTenths(String s) {
    return parseDoubleMissingDecimal(s, TENTHS);
  }

  public static Float parseFloatWithTenths(String s) {
    return parseDoubleWithTenths(s).floatValue();
  }

  public static Double parseDoubleWithHundredths(String s) {
    return parseDoubleMissingDecimal(s, HUNDREDTHS);
  }

  public static Double parseDoubleWithThousandths(String s) {
    return parseDoubleMissingDecimal(s, THOUSANDTHS);
  }

  public static Double parseDoubleMissingDecimal(String s, Integer missingDecimalUnits) {
    return Double.parseDouble(s) / Math.pow(10, missingDecimalUnits);
  }

  /**
   * Attempts to convert the given string field to the provided enum class - if the {@link Enum#valueOf(Class, String)} call
   * fails this method throws a {@link FieldSpecParseException} instead of an {@link IllegalArgumentException}.
   */
  public static <E extends Enum<E>> Optional<E> toEnumValue(String string, Class<E> clz) {
    try {
      return Optional.of(E.valueOf(clz, string));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
