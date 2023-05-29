package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.split.RouteToken;

/**
 *
 */
public final class ResolvedLeg {
  /**
   * The {@link RouteToken} from the route string used to generate this reference.
   */
  private final RouteToken routeToken;
  /**
   * A pointer to the {@link ResolvedToken} which generated the given leg for the top level split.
   */
  private final ResolvedToken resolvedToken;
  /**
   * The actual resolved leg of the route.
   */
  private final Leg leg;

  public ResolvedLeg(RouteToken routeToken, ResolvedToken resolvedToken, Leg leg) {
    this.routeToken = requireNonNull(routeToken);
    this.resolvedToken = requireNonNull(resolvedToken);
    this.leg = requireNonNull(leg);
  }

  public RouteToken routeToken() {
    return routeToken;
  }

  public ResolvedToken resolvedToken() {
    return resolvedToken;
  }

  public Leg leg() {
    return leg;
  }
}
