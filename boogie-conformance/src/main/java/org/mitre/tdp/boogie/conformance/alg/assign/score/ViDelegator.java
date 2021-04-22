package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * Returns true if the given {@link FlyableLeg} contains the appropriate information to be modeled with the VI modeler.
 */
public final class ViDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {
  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return PathTerm.VI.equals(flyableLeg.current().type())
        && PathTerm.VI.hasRequiredFields(flyableLeg.current())
        && conformablePoint.trueCourse().isPresent();
  }
}
