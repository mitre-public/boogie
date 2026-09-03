package org.mitre.tdp.boogie.arinc;

import org.mitre.tdp.boogie.arinc.utils.AsciiDigits;

import java.util.Optional;

import static java.util.Objects.checkFromToIndex;

/**
 * Shared fixed-width representation used by east/west variation and declination fields.
 */
public abstract class EastWestVariation implements FieldSpec<Double> {

  @Override
  public final Optional<Double> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    if (endOffset - startOffset != fieldLength()) {
      return Optional.empty();
    }

    char direction = source.charAt(startOffset);
    if (direction != 'E' && direction != 'W') {
      return Optional.empty();
    }

    double variation = AsciiDigits.parseDoubleOrNaN(source, startOffset + 1, endOffset);
    if (Double.isNaN(variation)) {
      return Optional.empty();
    }

    variation /= 10.0;
    return Optional.of(direction == 'W' ? -variation : variation);
  }
}
