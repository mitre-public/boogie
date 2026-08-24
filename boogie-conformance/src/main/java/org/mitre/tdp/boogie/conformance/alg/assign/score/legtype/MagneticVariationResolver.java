package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

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

  /**
   * Resolves the current leg's published outbound course to true degrees.
   *
   * <p>The magnetic-variation lookup is lazy, so a true-referenced course does not require a recommended navaid, associated
   * fix, or modeled variation.
   */
  public Optional<Double> outboundTrueCourse(ConformablePoint point, FlyableLeg flyableLeg) {
    return flyableLeg.current().outboundCourse()
        .map(course -> course.trueDegrees(() -> magneticVariation(point, flyableLeg)));
  }

  /**
   * Resolves the current leg's theta to true degrees. Theta is always magnetic, independently of the outbound course reference.
   */
  public Optional<Double> thetaTrueCourse(ConformablePoint point, FlyableLeg flyableLeg) {
    return flyableLeg.current().theta()
        .map(theta -> magneticVariation(point, flyableLeg).magneticToTrue(theta));
  }
}
