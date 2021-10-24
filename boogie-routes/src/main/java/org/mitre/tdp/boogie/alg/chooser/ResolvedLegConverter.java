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
import org.mitre.tdp.boogie.alg.resolve.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.AirwayElement;
import org.mitre.tdp.boogie.alg.resolve.ApproachElement;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.FixElement;
import org.mitre.tdp.boogie.alg.resolve.LatLonElement;
import org.mitre.tdp.boogie.alg.resolve.ResolvedElement;
import org.mitre.tdp.boogie.alg.resolve.ResolvedLeg;
import org.mitre.tdp.boogie.alg.resolve.SidElement;
import org.mitre.tdp.boogie.alg.resolve.StarElement;
import org.mitre.tdp.boogie.alg.resolve.TailoredElement;

/**
 * Functional class which is responsible for converting a {@link ResolvedLeg} to an {@link ExpandedRouteLeg} and adding the
 * appropriate categorical features to it's definition.
 */
public final class ResolvedLegConverter implements Function<ResolvedLeg, ExpandedRouteLeg> {

  public static final ResolvedLegConverter INSTANCE = new ResolvedLegConverter();

  private ResolvedLegConverter() {
  }

  @Override
  public ExpandedRouteLeg apply(ResolvedLeg resolvedLeg) {
    return new ExpandedRouteLeg(
        resolvedLeg.split().value(),
        fromResolvedElement(resolvedLeg.sourceElement()),
        resolvedLeg.split().wildcards(),
        resolvedLeg.leg()
    );
  }

  /**
   * This isn't a best practice implementation but this class as a whole is mainly provided for ease-of-use in downstream apps.
   */
  public static ElementType fromResolvedElement(ResolvedElement resolvedElement) {
    if (resolvedElement instanceof AirportElement) {
      return AIRPORT;
    } else if (resolvedElement instanceof AirwayElement) {
      return AIRWAY;
    } else if (resolvedElement instanceof FixElement) {
      return FIX;
    } else if (resolvedElement instanceof SidElement) {
      return SID;
    } else if (resolvedElement instanceof StarElement) {
      return STAR;
    } else if (resolvedElement instanceof ApproachElement) {
      return APPROACH;
    } else if (resolvedElement instanceof LatLonElement) {
      return LATLON;
    } else if (resolvedElement instanceof TailoredElement) {
      return TAILORED;
    } else {
      throw new IllegalArgumentException("Unknown how to map input ResolvedElement to ElementType. ResolvedElement class was: ".concat(resolvedElement.getClass().getSimpleName()));
    }
  }
}
