package org.mitre.tdp.boogie.alg.facade;

import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides the facade for the implementations of the {@link org.mitre.tdp.boogie.alg.RouteExpander}
 */
@FunctionalInterface
public interface ExpanderFacade {

  Logger LOG = LoggerFactory.getLogger(ExpanderFacade.class);

  Optional<ExpandedRoute> expand(String route, RouteDetails details);

  default void logInputs(String route, RouteDetails details) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("- Beginning expansion of route: {}", route);
      LOG.debug(String.format("  %20s %20s %30s", "Departure Runway", "Arrival Runway", "Equipage Preferences"));
      LOG.debug(
          String.format("  %20s %20s %30s",
              details.departureRunway().orElse("None"),
              details.arrivalRunway().orElse("None"),
              details.equipagePreference().stream().map(Enum::name).collect(Collectors.joining(">"))
          )
      );
    }
  }

  default ExpandedRoute updateSummary(ExpandedRoute expandedRoute, String route, RouteDetails details) {
    return expandedRoute.updateSummary(summary -> summary.toBuilder()
        .route(route)
        .departureRunway(details.departureRunway().orElse(null))
        .arrivalRunway(details.arrivalRunway().orElse(null))
        .build());
  }
}
