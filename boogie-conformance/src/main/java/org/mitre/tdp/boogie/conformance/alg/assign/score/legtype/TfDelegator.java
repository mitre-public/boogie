package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;

/**
 * Returns true if the given {@link FlyableLeg} contains the appropriate information to be modeled with the TF modeler.
 */
public final class TfDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {

  private static final PathTerminatorBasedLegValidator validator = new PathTerminatorBasedLegValidator();

  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return PathTerminator.TF.equals(flyableLeg.current().pathTerminator())
        && validator.test(flyableLeg.current())
        && flyableLeg.current().associatedFix().isPresent()
        && flyableLeg.previous().flatMap(Leg::associatedFix).isPresent();
  }
}
