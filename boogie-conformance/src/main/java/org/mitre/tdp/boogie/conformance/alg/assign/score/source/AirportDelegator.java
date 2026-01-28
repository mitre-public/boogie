package org.mitre.tdp.boogie.conformance.alg.assign.score.source;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

import com.google.common.annotations.Beta;

/**
 * This class determines if the leg is a singleton airport leg.
 */
@Beta
public final class AirportDelegator implements BiPredicate<ConformablePoint, FlyableLeg> {
  @Override
  public boolean test(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return flyableLeg.previous().isEmpty()
        && flyableLeg.next().isEmpty()
        && flyableLeg.routes().stream().map(Route::source).allMatch(r -> Airport.class.isAssignableFrom(r.getClass()));
  }
}
