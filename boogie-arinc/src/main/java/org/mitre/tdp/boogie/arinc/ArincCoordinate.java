package org.mitre.tdp.boogie.arinc;

import java.util.Optional;

import static java.util.Objects.checkFromToIndex;

/**
 * Shared fixed-width representation used by latitude and longitude fields.
 */
public abstract class ArincCoordinate implements FieldSpec<Double> {

  private final int degreeDigits;
  private final char positiveDirection;
  private final char negativeDirection;

  protected ArincCoordinate(int degreeDigits, char positiveDirection, char negativeDirection) {
    this.degreeDigits = degreeDigits;
    this.positiveDirection = positiveDirection;
    this.negativeDirection = negativeDirection;
  }

  @Override
  public final Optional<Double> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    if (endOffset - startOffset != fieldLength()) {
      return Optional.empty();
    }

    char direction = source.charAt(startOffset);
    if (direction != positiveDirection && direction != negativeDirection) {
      return Optional.empty();
    }

    for (int index = startOffset + 1; index < endOffset; index++) {
      char character = source.charAt(index);
      if (character < '0' || character > '9') {
        return Optional.empty();
      }
    }

    int degreeEnd = startOffset + degreeDigits + 1;
    int minuteEnd = degreeEnd + 2;
    int secondEnd = minuteEnd + 2;

    double degrees = number(source, startOffset + 1, degreeEnd);
    double minutes = number(source, degreeEnd, minuteEnd) / 60.0;
    double seconds = number(source, minuteEnd, secondEnd) / 3600.0;
    double decimals = digit(source, secondEnd) / 36000.0 + digit(source, secondEnd + 1) / 360000.0;
    double coordinate = degrees + minutes + seconds + decimals;

    return Optional.of(direction == positiveDirection ? coordinate : -coordinate);
  }

  private static int number(String source, int startOffset, int endOffset) {
    int value = 0;
    for (int index = startOffset; index < endOffset; index++) {
      value = value * 10 + digit(source, index);
    }
    return value;
  }

  private static int digit(String source, int offset) {
    return source.charAt(offset) - '0';
  }
}
