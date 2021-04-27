package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * Delegator function which introspects a given {@link ConformablePoint} and {@link FlyableLeg} returning true when an CF leg
 * modeler (extractor + scorer) can be applied to the combination.
 */
public final class CfDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {
  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return PathTerm.CF.equals(flyableLeg.current().type())
        && PathTerm.CF.hasRequiredFields(flyableLeg.current())
        && conformablePoint.pressureAltitude().isPresent();
  }
}
