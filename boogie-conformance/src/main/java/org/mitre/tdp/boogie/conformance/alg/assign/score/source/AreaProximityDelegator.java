package org.mitre.tdp.boogie.conformance.alg.assign.score.source;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.generate.AreaProximity;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

import com.google.common.annotations.Beta;

/**
 * Identifies when the area proximity object is the source of the leg.
 */
@Beta
public final class AreaProximityDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {
  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return flyableLeg.previous().isEmpty()
        && flyableLeg.next().isEmpty()
        && flyableLeg.routes().stream().map(Route::source).allMatch(r -> AreaProximity.class.isAssignableFrom(r.getClass()));
  }
}
