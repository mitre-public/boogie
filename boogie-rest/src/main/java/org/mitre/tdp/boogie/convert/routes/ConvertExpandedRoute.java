package org.mitre.tdp.boogie.convert.routes;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.contract.routes.ExpandedRoute;
import org.mitre.tdp.boogie.contract.routes.ImmutableExpandedRoute;

public final class ConvertExpandedRoute implements Function<org.mitre.tdp.boogie.alg.ExpandedRoute, ExpandedRoute> {
  public static final ConvertExpandedRoute INSTANCE = new ConvertExpandedRoute();

  private static final ConvertExpandedRouteLeg LEG = ConvertExpandedRouteLeg.INSTANCE;
  private static final ConvertRouteSummary SUMMARY = ConvertRouteSummary.INSTANCE;

  private ConvertExpandedRoute() {

  }

  @Override
  public ExpandedRoute apply(org.mitre.tdp.boogie.alg.ExpandedRoute expandedRoute) {
    return ImmutableExpandedRoute.builder()
        .routeSummary(expandedRoute.routeSummary().map(SUMMARY))
        .legs(expandedRoute.legs().stream().map(LEG).collect(Collectors.toList()))
        .build();
  }
}
