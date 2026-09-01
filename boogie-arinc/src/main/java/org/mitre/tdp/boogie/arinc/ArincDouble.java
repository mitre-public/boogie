package org.mitre.tdp.boogie.arinc;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.utils.AsciiDigits;

public abstract class ArincDouble extends TrimmableField<Double> {

  @Override
  protected final Optional<Double> parseTrimmed(String source, int startOffset, int endOffset) {
    int digitsStart = digitsStart(source, startOffset);
    if (digitsStart == endOffset) {
      return Optional.empty();
    }

    double value = AsciiDigits.parseDoubleOrNaN(source, digitsStart, endOffset);
    if (Double.isNaN(value)) {
      return Optional.empty();
    }

    value /= decimalScale();
    value = source.charAt(startOffset) == '-' ? -value : value;

    return isValidValue(value) ? Optional.of(value) : Optional.empty();
  }

  /**
   * Number of decimal places suppressed in the fixed-width source representation.
   */
  protected int suppressedDecimalPlaces() {
    return 0;
  }

  /**
   * Additional validation for a parsed value.
   */
  protected boolean isValidValue(double value) {
    return true;
  }

  private static int digitsStart(String source, int startOffset) {
    char first = source.charAt(startOffset);
    return first == '+' || first == '-' ? startOffset + 1 : startOffset;
  }

  private double decimalScale() {
    int places = suppressedDecimalPlaces();
    return switch (places) {
      case 0 -> 1.0;
      case 1 -> 10.0;
      case 2 -> 100.0;
      case 3 -> 1000.0;
      default -> Math.pow(10.0, places);
    };
  }
}
