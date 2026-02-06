package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;

/**
 * Delegator function which introspects a given {@link ConformablePoint} and {@link FlyableLeg} returning true when an AF leg
 * modeler (extractor + scorer) can be applied to the combination.
 */
public final class AfDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {

  private static final PathTerminatorBasedLegValidator validator = new PathTerminatorBasedLegValidator();

  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return PathTerminator.AF.equals(flyableLeg.current().pathTerminator())
        && validator.test(flyableLeg.current())
        && flyableLeg.previous().flatMap(Leg::associatedFix).map(Fix::latLong).isPresent();
  }
}
