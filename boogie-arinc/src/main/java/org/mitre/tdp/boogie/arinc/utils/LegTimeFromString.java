package org.mitre.tdp.boogie.arinc.utils;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;

/**
 * This class ensures that ARINC data to Duration transformations happen the same.
 */
public class LegTimeFromString implements Function<String, Duration> {

  public static  final LegTimeFromString INSTANCE = new LegTimeFromString();

  private LegTimeFromString() {

  }

  @Override
  public Duration apply(String mins) {
    return Optional.ofNullable(mins)
        .flatMap(ArincDecimalParser.INSTANCE::parseDoubleWithTenths)
        .map(m -> Duration.ofSeconds((int) (m * 60)))
        .orElseThrow(() -> new IllegalArgumentException("Time string does not translate correctly: " + mins));
  }
}
