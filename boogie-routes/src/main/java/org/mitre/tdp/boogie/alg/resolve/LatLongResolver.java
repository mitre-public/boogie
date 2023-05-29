package org.mitre.tdp.boogie.alg.resolve;

import static org.mitre.tdp.boogie.alg.resolve.CoordinateFormatStandard.supported;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedToken.directToLatLong;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedToken.standardLatLong;
import static org.mitre.tdp.boogie.alg.split.RouteTokenVisitor.isTailoredBefore;

import java.util.List;
import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.util.CoordinateParser;

final class LatLongResolver implements SingleTokenResolver {

  LatLongResolver() {
  }

  @Override
  public List<ResolvedToken> resolve(RouteToken routeToken) {
    return Optional.of(routeToken)
        .filter(t -> supported(t.infrastructureName()))
        .map(this::makeResolved)
        .map(List::of)
        .orElseGet(List::of);
  }

  private ResolvedToken makeResolved(RouteToken token) {
    LatLong latLong = parseLatLong(token.infrastructureName());
    return isTailoredBefore(token) ? standardLatLong(latLong) : directToLatLong(latLong);
  }

  private LatLong parseLatLong(String s) {
    return CoordinateParser.parse(
        CoordinateFormatStandard.makeLat(s).orElseThrow(),
        CoordinateFormatStandard.makeLon(s).orElseThrow()
    );
  }
}
