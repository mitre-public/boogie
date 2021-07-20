package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.arinc.FieldSpec2;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * The “Altitude Limitation” field is used to define the altitude(s) at which the limitation applies.
 * <br>
 * Used in conjunction with {@link AltitudeDescription}.
 */
public final class AltitudeLimit implements FieldSpec2<Pair<Double, Double>> {

  @Override
  public int fieldLength() {
    return 6;
  }

  @Override
  public String fieldCode() {
    return "5.209";
  }

  @Override
  public Optional<Pair<Double, Double>> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .filter(ValidArincNumeric.INSTANCE)
        .map(String::trim)
        .filter(s -> s.length() == 6)
        .map(s -> Pair.of(
            Double.parseDouble(s.substring(0, 3)) * 100.0d,
            Double.parseDouble(s.substring(3)) * 100.0d)
        );
  }
}
