package org.mitre.tdp.boogie.alg.chooser;

import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRPORT;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRWAY;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.APPROACH;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.FIX;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.LATLON;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.STAR;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.TAILORED;

import java.util.function.Function;

import org.mitre.tdp.boogie.alg.ExpandedRouteLeg;
import org.mitre.tdp.boogie.alg.resolve.AirportToken;
import org.mitre.tdp.boogie.alg.resolve.AirwayToken;
import org.mitre.tdp.boogie.alg.resolve.ApproachToken;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.FixToken;
import org.mitre.tdp.boogie.alg.resolve.LatLonToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedLeg;
import org.mitre.tdp.boogie.alg.resolve.SidToken;
import org.mitre.tdp.boogie.alg.resolve.StarToken;
import org.mitre.tdp.boogie.alg.resolve.TailoredToken;
import org.mitre.tdp.boogie.alg.split.RouteTokenVisitor;

/**
 * Functional class which is responsible for converting a {@link ResolvedLeg} to an {@link ExpandedRouteLeg} and adding the
 * appropriate categorical features to it's definition.
 */
final class ResolvedLegConverter implements Function<ResolvedLeg, ExpandedRouteLeg> {

  public static final ResolvedLegConverter INSTANCE = new ResolvedLegConverter();

  private ResolvedLegConverter() {
  }

  @Override
  public ExpandedRouteLeg apply(ResolvedLeg resolvedLeg) {
    return new ExpandedRouteLeg(
        resolvedLeg.split().infrastructureName(),
        fromResolvedElement(resolvedLeg.sourceElement()),
        RouteTokenVisitor.wildcards(resolvedLeg.split()),
        resolvedLeg.leg()
    );
  }

  /**
   * This isn't a best practice implementation but this class as a whole is mainly provided for ease-of-use in downstream apps.
   */
  public static ElementType fromResolvedElement(ResolvedToken resolvedToken) {
    if (resolvedToken instanceof AirportToken) {
      return AIRPORT;
    } else if (resolvedToken instanceof AirwayToken) {
      return AIRWAY;
    } else if (resolvedToken instanceof FixToken) {
      return FIX;
    } else if (resolvedToken instanceof SidToken) {
      return SID;
    } else if (resolvedToken instanceof StarToken) {
      return STAR;
    } else if (resolvedToken instanceof ApproachToken) {
      return APPROACH;
    } else if (resolvedToken instanceof LatLonToken) {
      return LATLON;
    } else if (resolvedToken instanceof TailoredToken) {
      return TAILORED;
    } else {
      throw new IllegalArgumentException("Unknown how to map input ResolvedElement to ElementType. ResolvedElement class was: ".concat(resolvedToken.getClass().getSimpleName()));
    }
  }
}
