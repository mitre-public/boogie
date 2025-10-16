package org.mitre.boogie.xml.util;

import java.util.Optional;
import java.util.function.Function;

/**
 * This class converts the arinc integer version of a coordinate into decimal degrees.
 */
public final class DecimalDegrees implements Function<Coordinate, Double> {
  public static final DecimalDegrees INSTANCE = new DecimalDegrees();

  private DecimalDegrees() {
  }

  @Override
  public Double apply(Coordinate coordinate) {
    double posNeg = Optional.of(coordinate)
        .map(Coordinate::nsew)
        .map(this::posNeg)
        .orElseThrow(() -> new IllegalStateException("No north, south, east, west on coordinate"));
    double degree = Optional.ofNullable(coordinate.deg())
        .map(Integer::doubleValue)
        .orElse(0.0);
    double min = Optional.ofNullable(coordinate.min())
        .map(Integer::doubleValue)
        .map(m -> m / 60.0)
        .orElse(0.0);
    double sec = Optional.ofNullable(coordinate.sec())
        .map(Integer::doubleValue)
        .map(s -> s / 3600.0)
        .orElse(0.0);
    double hsec = Optional.ofNullable(coordinate.hsec())
        .map(Integer::doubleValue)
        .map(hs -> hs / 216000.0)
        .orElse(0.0);
    return posNeg * (degree + min + sec + hsec);
  }

  private double posNeg(String nsew) {
    return switch (nsew) {
      case "NORTH", "EAST" -> 1.0;
      case "SOUTH", "WEST" -> -1.0;
      default -> throw new IllegalArgumentException();
    };
  }
}
