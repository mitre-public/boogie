package org.mitre.tdp.boogie.v18.spec.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;

/**
 * The “Altitude Limitation” field is used to define the altitude(s) at which the limitation applies.
 *
 * Used in conjunction with {@link AltitudeDescription}.
 */
public class AltitudeLimit implements FieldSpec<Pair<Double, Double>>, FilterTrimEmptyInput<Pair<Double, Double>> {
  @Override
  public int fieldLength() {
    return 6;
  }

  @Override
  public String fieldCode() {
    return "5.209";
  }

  @Override
  public Pair<Double, Double> parseValue(String fieldValue) {
    checkSpec(this, fieldValue, isNumeric(fieldValue));
    return Pair.of(
        Double.parseDouble(fieldValue.substring(0, 3)) * 100.0d,
        Double.parseDouble(fieldValue.substring(3)) * 100.0d);
  }
}
