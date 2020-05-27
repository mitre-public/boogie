package org.mitre.tdp.boogie.v18.spec.common;

import org.mitre.tdp.boogie.FieldSpec;

public interface AltitudeFlightLevel extends FieldSpec<Float> {

  @Override
  default Float parse(String fieldString) {
    return fieldString.startsWith("FL")
        ? Float.parseFloat(fieldString.substring(2)) * 100.0f
        : Float.parseFloat(fieldString);
  }
}
