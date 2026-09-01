package org.mitre.tdp.boogie.arinc;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.utils.AsciiDigits;

/**
 * Shared parser for ARINC fields containing either feet or an {@code FL} value.
 */
public abstract class AltitudeField extends TrimmableField<Double> {

  @Override
  protected final Optional<Double> parseTrimmed(String source, int startOffset, int endOffset) {
    return isFlightLevel(source, startOffset, endOffset) ? parseDigits(source, startOffset + 2, endOffset, 100.0) : parseAltitude(source, startOffset, endOffset);
  }

  @Override
  protected final boolean acceptsSourceLength(int length) {
    return length == fieldLength();
  }

  private static boolean isFlightLevel(String source, int startOffset, int endOffset) {
    return endOffset - startOffset >= 2
        && source.charAt(startOffset) == 'F'
        && source.charAt(startOffset + 1) == 'L';
  }

  private static Optional<Double> parseAltitude(String source, int startOffset, int endOffset) {
    char first = source.charAt(startOffset);

    if (first == '+' || first == '-') {
      return parseDigits(source, startOffset + 1, endOffset, first == '-' ? -1.0 : 1.0);
    }

    return parseDigits(source, startOffset, endOffset, 1.0);
  }

  private static Optional<Double> parseDigits(String source, int startOffset, int endOffset, double multiplier) {
    double value = AsciiDigits.parseDoubleOrNaN(source, startOffset, endOffset);
    return Double.isNaN(value) ? Optional.empty() : Optional.of(value * multiplier);
  }
}
