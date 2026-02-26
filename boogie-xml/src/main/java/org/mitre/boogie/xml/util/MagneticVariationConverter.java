package org.mitre.boogie.xml.util;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.MagneticVariation;

/**
 * This class converts the arinc value magvar to an optional magvar (no magvar when its true)
 */
public final class MagneticVariationConverter implements Function<MagVar, Optional<MagneticVariation>> {
  public static final MagneticVariationConverter INSTANCE = new MagneticVariationConverter();

  private MagneticVariationConverter() {
  }

  private static double ew(String ew) {
    return switch (ew) {
      case "EAST" -> -1.0;
      case "WEST" -> 1.0;
      default -> throw new IllegalArgumentException("Unknown ew: " + ew);
    };
  }

  @Override
  public Optional<MagneticVariation> apply(MagVar magVar) {
    return Optional.ofNullable(magVar)
        .filter(i -> "EAST".equals(i.ew()) || "WEST".equals(i.ew()))
        .map(this::from);
  }

  private MagneticVariation from(MagVar magVar) {
    double posNeg = ew(magVar.ew());
    double value = posNeg * magVar.value();
    return MagneticVariation.ofDegrees(value);
  }
}
