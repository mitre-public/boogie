package org.mitre.tdp.boogie.arinc;

import static org.mitre.tdp.boogie.arinc.utils.FieldSliceParser.parseDouble;

import java.util.Optional;

public abstract class ArincDouble implements FieldSpec<Double> {

  @Override
  public final Optional<Double> apply(String fieldValue) {
    return apply(fieldValue, 0, fieldValue.length());
  }

  @Override
  public final Optional<Double> apply(String source, int startOffset, int endOffset) {
    return parseDouble(source, startOffset, endOffset, suppressedDecimalPlaces());
  }

  /**
   * Number of decimal places suppressed in the fixed-width source representation.
   */
  protected int suppressedDecimalPlaces() {
    return 0;
  }
}
