package org.mitre.tdp.boogie.arinc.utils;

import java.util.Optional;

import static java.util.Objects.checkFromToIndex;

/**
 * Allocation-conscious parsers for fields stored within a fixed-width record.
 * <p>
 * The source offsets are trimmed and parsed in place so callers do not need to create an intermediate substring.
 */
public final class FieldSliceParser {

  private FieldSliceParser() {
  }

  public static Optional<String> parseTrimmedString(String source, int startOffset, int endOffset) {
    long bounds = trimmedBounds(source, startOffset, endOffset);
    int start = start(bounds);
    int end = end(bounds);

    return start == end ? Optional.empty() : Optional.of(source.substring(start, end).intern());
  }

  public static Optional<Integer> parseInteger(String source, int startOffset, int endOffset) {
    long bounds = trimmedBounds(source, startOffset, endOffset);
    int start = start(bounds);
    int end = end(bounds);
    int digitsStart = digitsStart(source, start, end);

    if (digitsStart == end || !containsOnlyDigits(source, digitsStart, end)) {
      return Optional.empty();
    }

    return Optional.of(Integer.parseInt(source, start, end, 10));
  }

  public static Optional<Double> parseDouble(String source, int startOffset, int endOffset, int suppressedDecimalPlaces) {

    long bounds = trimmedBounds(source, startOffset, endOffset);
    int start = start(bounds);
    int end = end(bounds);
    int digitsStart = digitsStart(source, start, end);

    if (digitsStart == end) {
      return Optional.empty();
    }

    double value = 0.0;
    for (int index = digitsStart; index < end; index++) {
      char character = source.charAt(index);
      if (!isDigit(character)) {
        return Optional.empty();
      }
      value = value * 10.0 + character - '0';
    }

    value /= decimalScale(suppressedDecimalPlaces);

    return Optional.of(source.charAt(start) == '-' ? -value : value);
  }

  public static Optional<Double> parseDouble(String source, int suppressedDecimalPlaces) {
    return parseDouble(source, 0, source.length(), suppressedDecimalPlaces);
  }

  public static Optional<Double> parseEastWestDouble(String source, int startOffset, int endOffset, int suppressedDecimalPlaces) {
    return parseEastWestDouble(source, startOffset, endOffset, suppressedDecimalPlaces, -1);
  }

  public static Optional<Double> parseEastWestDouble(String source, int startOffset, int endOffset, int suppressedDecimalPlaces, int requiredLength) {
    long bounds = trimmedBounds(source, startOffset, endOffset);
    int start = start(bounds);
    int end = end(bounds);

    if ((requiredLength >= 0 && end - start != requiredLength) || end - start < 2) {
      return Optional.empty();
    }

    char direction = source.charAt(start);
    if ((direction != 'E' && direction != 'W') || !isDigit(source.charAt(start + 1))) {
      return Optional.empty();
    }

    Optional<Double> magnitude = parseDouble(source, start + 1, end, suppressedDecimalPlaces);
    return direction == 'W' && magnitude.isPresent() ? Optional.of(-magnitude.get()) : magnitude;
  }

  public static Optional<String> parseContinuationNumber(String source, int startOffset, int endOffset) {
    long bounds = trimmedBounds(source, startOffset, endOffset);
    int start = start(bounds);
    int end = end(bounds);

    for (int index = start; index < end; index++) {
      char character = source.charAt(index);
      if (isDigit(character) || (character >= 'A' && character <= 'Z')) {
        return Optional.of(source.substring(start, end));
      }
    }

    return Optional.empty();
  }

  private static long trimmedBounds(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());

    while (startOffset < endOffset && source.charAt(startOffset) <= ' ') {
      startOffset++;
    }
    while (startOffset < endOffset && source.charAt(endOffset - 1) <= ' ') {
      endOffset--;
    }

    // Keep the two offsets in a primitive so trimming does not allocate a range holder for every decoded field.
    return ((long) startOffset << Integer.SIZE) | (endOffset & 0xffff_ffffL);
  }

  private static int start(long bounds) {
    return (int) (bounds >>> Integer.SIZE);
  }

  private static int end(long bounds) {
    return (int) bounds;
  }

  private static int digitsStart(String source, int startOffset, int endOffset) {
    if (startOffset == endOffset) {
      return endOffset;
    }

    char first = source.charAt(startOffset);
    return first == '+' || first == '-' ? startOffset + 1 : startOffset;
  }

  private static boolean containsOnlyDigits(String source, int startOffset, int endOffset) {
    for (int index = startOffset; index < endOffset; index++) {
      if (!isDigit(source.charAt(index))) {
        return false;
      }
    }
    return true;
  }

  private static boolean isDigit(char character) {
    return character >= '0' && character <= '9';
  }

  private static double decimalScale(int suppressedDecimalPlaces) {
    return switch (suppressedDecimalPlaces) {
      case 0 -> 1.0;
      case 1 -> 10.0;
      case 2 -> 100.0;
      case 3 -> 1000.0;
      default -> Math.pow(10.0, suppressedDecimalPlaces);
    };
  }
}
