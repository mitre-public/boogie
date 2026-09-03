package org.mitre.tdp.boogie.arinc.utils;

import static java.util.Objects.checkFromToIndex;

/**
 * Parsing utilities for non-empty slices containing only ASCII digits.
 */
public final class AsciiDigits {

  private AsciiDigits() {
  }

  /**
   * Parses an unsigned ASCII digit slice, returning {@link Double#NaN} when the slice is empty or contains a non-digit.
   */
  public static double parseDoubleOrNaN(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    if (startOffset == endOffset) {
      return Double.NaN;
    }

    double value = 0.0;
    for (int index = startOffset; index < endOffset; index++) {
      char character = source.charAt(index);
      if (character < '0' || character > '9') {
        return Double.NaN;
      }
      value = value * 10.0 + character - '0';
    }
    return value;
  }
}
