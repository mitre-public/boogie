package org.mitre.tdp.boogie.arinc.utils;

import java.util.function.Function;

public final class AltitudeFlightLevelParser implements Function<String, Double> {

  public static final AltitudeFlightLevelParser INSTANCE = new AltitudeFlightLevelParser();

  @Override
  public Double apply(String fieldValue) {
    return fieldValue.startsWith("FL") ? Float.parseFloat(fieldValue.substring(2)) * 100.0 : Float.parseFloat(fieldValue);
  }
}
