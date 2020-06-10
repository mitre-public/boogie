package org.mitre.tdp.boogie.v18.spec.common;

import org.mitre.tdp.boogie.FieldSpec;

public interface AltitudeFlightLevel extends FieldSpec<Double>, FilterTrimEmptyInput<Double> {

  @Override
  default Double parseValue(String fieldValue) {
    return fieldValue.startsWith("FL")
        ? Float.parseFloat(fieldValue.substring(2)) * 100.0
        : Float.parseFloat(fieldValue);
  }
}
