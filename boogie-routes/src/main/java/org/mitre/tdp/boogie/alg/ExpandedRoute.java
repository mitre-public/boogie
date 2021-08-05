package org.mitre.tdp.boogie.alg;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.alg.resolve.ResolvedLeg;

/**
 * Container class for a {@link RouteSummary} (if one was generated) and a leg sequence as resolved by the {@link RouteExpander}.
 */
public final class ExpandedRoute implements Serializable {

  /**
   * See {@link RouteSummary}.
   */
  private final RouteSummary routeSummary;
  private final List<ResolvedLeg> legs;

  public ExpandedRoute(RouteSummary routeSummary, List<ResolvedLeg> resolvedLegs) {
    this.routeSummary = routeSummary;
    this.legs = resolvedLegs;
  }

  public Optional<RouteSummary> routeSummary() {
    return Optional.of(routeSummary);
  }

  public List<ResolvedLeg> legs() {
    return legs;
  }
}
