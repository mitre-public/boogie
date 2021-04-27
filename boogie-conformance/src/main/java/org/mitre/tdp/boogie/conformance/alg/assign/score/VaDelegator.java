package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * Returns true if the given {@link FlyableLeg} contains the appropriate information to be modeled with the VA modeler.
 */
public final class VaDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {
  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return PathTerm.VA.equals(flyableLeg.current().type())
        && PathTerm.VA.hasRequiredFields(flyableLeg.current())
        && conformablePoint.pressureAltitude().isPresent()
        && conformablePoint.trueCourse().isPresent();
  }
}
