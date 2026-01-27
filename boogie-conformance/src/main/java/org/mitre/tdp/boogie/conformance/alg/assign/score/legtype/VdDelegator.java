package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;

public final class VdDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {
  private final PathTerminatorBasedLegValidator validator = new PathTerminatorBasedLegValidator();
  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return PathTerminator.VD.equals(flyableLeg.current().pathTerminator())
        && validator.test(flyableLeg.current())
        && conformablePoint.trueCourse().isPresent();
  }
}
