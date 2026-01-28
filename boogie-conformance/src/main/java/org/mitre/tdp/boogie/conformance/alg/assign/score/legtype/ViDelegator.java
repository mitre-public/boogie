package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;

/**
 * Returns true if the given {@link FlyableLeg} contains the appropriate information to be modeled with the VI modeler.
 */
public final class ViDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {

  private static final PathTerminatorBasedLegValidator validator = new PathTerminatorBasedLegValidator();

  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return PathTerminator.VI.equals(flyableLeg.current().pathTerminator())
        && validator.test(flyableLeg.current())
        && conformablePoint.trueCourse().isPresent()
        && flyableLeg.next().flatMap(Leg::associatedFix).isPresent();
  }
}
