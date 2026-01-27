package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;

public final class VrDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {
  private final PathTerminatorBasedLegValidator validator = new PathTerminatorBasedLegValidator();
  public VrDelegator() {}
  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return PathTerminator.VR.equals(flyableLeg.current().pathTerminator())
        && validator.test(flyableLeg.current())
        && conformablePoint.trueCourse().isPresent();
  }
}

