package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;

/**
 * Delegator function which introspects a given {@link ConformablePoint} and {@link FlyableLeg} returning true when a CA leg
 * modeler (extractor + scorer) can be applied to the combination.
 */
public final class CaDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {

  private static final PathTerminatorBasedLegValidator validator = new PathTerminatorBasedLegValidator();

  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return PathTerminator.CA.equals(flyableLeg.current().pathTerminator())
        && validator.test(flyableLeg.current())
        && conformablePoint.pressureAltitude().isPresent()
        && conformablePoint.trueCourse().isPresent();
  }
}
