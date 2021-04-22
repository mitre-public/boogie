package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.Optional;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.util.Declinations;

/**
 * Local class for hierarchical lookup of the local magnetic variation given a point and a leg.
 *
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
    return flyableLeg.current().recommendedNavaid().map(Fix::magneticVariation)
        .orElseGet(() -> Optional.of(flyableLeg).map(FlyableLeg::current).map(Leg::pathTerminator).map(Fix::magneticVariation)
            .orElseGet(() -> flyableLeg.next().map(Leg::pathTerminator).map(Fix::magneticVariation)
                .orElseGet(() -> magneticVariation(Declinations.declination(point.latitude(), point.longitude(), point.pressureAltitude(), point.time())))));
  }

  /**
   * Returns a new modeled {@link MagneticVariation} based on the WMM.
   */
  private static MagneticVariation magneticVariation(double modeled) {
    return new MagneticVariation() {
      @Override
      public Optional<Double> published() {
        return Optional.empty();
      }

      @Override
      public double modeled() {
        return modeled;
      }
    };
  }
}
