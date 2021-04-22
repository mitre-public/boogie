package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * Returns true if the given {@link FlyableLeg} contains the appropriate information to be modeled with the RF modeler.
 */
public final class RfDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {
  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return PathTerm.RF.equals(flyableLeg.current().type())
        && PathTerm.RF.hasRequiredFields(flyableLeg.current())
        && flyableLeg.current().pathTerminator() != null && flyableLeg.current().pathTerminator().latLong() != null
        && flyableLeg.previous().map(Leg::pathTerminator).map(Fix::latLong).isPresent();
  }
}
