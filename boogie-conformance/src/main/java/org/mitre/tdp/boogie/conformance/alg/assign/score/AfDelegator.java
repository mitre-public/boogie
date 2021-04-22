package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * Delegator function which introspects a given {@link ConformablePoint} and {@link FlyableLeg} returning true when an AF leg
 * modeler (extractor + scorer) can be applied to the combination.
 */
public final class AfDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {
  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return PathTerm.AF.equals(flyableLeg.current().type())
        && PathTerm.AF.hasRequiredFields(flyableLeg.current())
        && flyableLeg.current().pathTerminator() != null && flyableLeg.current().pathTerminator().latLong() != null
        && flyableLeg.previous().map(Leg::pathTerminator).map(Fix::latLong).isPresent();
  }
}
