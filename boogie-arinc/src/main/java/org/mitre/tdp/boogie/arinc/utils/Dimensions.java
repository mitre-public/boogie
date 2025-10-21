package org.mitre.tdp.boogie.arinc.utils;

import java.util.Optional;
public record Dimensions(Double x, Double y, Double diameter) {
  public Optional<Double> xPossible() {
    return Optional.ofNullable(x);
  }

  public Optional<Double> yPossible() {
    return Optional.ofNullable(y);
  }

  public Optional<Double> diameterPossible() {
    return Optional.ofNullable(diameter);
  }
}
