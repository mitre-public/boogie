package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;

/**
 * The “Altitude Limitation” field is used to define the altitude(s) at which the limitation applies.
 *
 * Used in conjunction with {@link AltitudeDescription}.
 */
public class AltitudeLimit implements FieldSpec<Pair<Float, Float>>, FilterTrimEmptyInput<Pair<Float, Float>> {
  @Override
  public int fieldLength() {
    return 6;
  }

  @Override
  public String fieldCode() {
    return "5.209";
  }

  @Override
  public Pair<Float, Float> parseValue(String fieldValue) {
    return Pair.of(
        Float.parseFloat(fieldValue.substring(0, 3)) * 100.0f,
        Float.parseFloat(fieldValue.substring(3)) * 100.0f);
  }
}
