package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public interface AltitudeFlightLevel extends FieldSpec<Double>, FilterTrimEmptyInput<Double> {

  @Override
  default Double parseValue(String fieldValue) {
    return fieldValue.startsWith("FL")
        ? Float.parseFloat(fieldValue.substring(2)) * 100.0
        : Float.parseFloat(fieldValue);
  }
}
