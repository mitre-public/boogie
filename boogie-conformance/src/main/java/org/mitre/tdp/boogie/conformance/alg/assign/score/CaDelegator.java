package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * Delegator function which introspects a given {@link ConformablePoint} and {@link FlyableLeg} returning true when a CA leg
 * modeler (extractor + scorer) can be applied to the combination.
 */
public final class CaDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {
  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return PathTerm.CA.equals(flyableLeg.current().type())
        && PathTerm.CA.hasRequiredFields(flyableLeg.current())
        && conformablePoint.pressureAltitude().isPresent()
        && conformablePoint.trueCourse().isPresent();
  }
}
