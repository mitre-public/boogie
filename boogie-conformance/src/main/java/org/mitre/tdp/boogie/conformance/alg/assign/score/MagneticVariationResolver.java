package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.Optional;

import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * Local class for hierarchical lookup of the local magnetic variation given a point and a leg.
 * <br>
 * Note this class is really only meant for finding magvars for the purpose of scoring - the worst-case-scenario lookup is at
 * best a coarse grained look at the local magvar.
 */
public final class MagneticVariationResolver {

  private static final MagneticVariationResolver INSTANCE = new MagneticVariationResolver();

  public static MagneticVariationResolver getInstance() {
    return INSTANCE;
  }

  /**
   * Returns - in order of priority - the {@link MagneticVariation} at the recommended navaid, else at the path terminator fix,
   * or else as modeled at the location of the aircraft.
   */
  public MagneticVariation magneticVariation(ConformablePoint point, FlyableLeg flyableLeg) {
    return flyableLeg.current().recommendedNavaid().flatMap(Fix::magneticVariation)
        .or(() -> Optional.of(flyableLeg).map(FlyableLeg::current).flatMap(Leg::associatedFix).flatMap(Fix::magneticVariation))
        .or(() -> flyableLeg.next().flatMap(Leg::associatedFix).flatMap(Fix::magneticVariation))
        .orElseGet(() -> MagneticVariation.ofDegrees(Declinations.declination(point.latitude(), point.longitude(), point.pressureAltitude().orElse(null), point.time())));
  }
}
