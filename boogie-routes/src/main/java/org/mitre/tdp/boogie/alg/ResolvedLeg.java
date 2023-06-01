package org.mitre.tdp.boogie.alg;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.chooser.RouteChooser;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.split.RouteToken;

/**
 * A resolved leg is a container class for a leg which has been chosen as part of the final route after the {@link RouteChooser}
 * has examined the input sequence of available {@link ResolvedTokens}.
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
  // Leg.Record<T>, T = Akela Leg
  // DirectTo = Akela Fix

  private ResolvedLeg(RouteToken routeToken, ResolvedToken resolvedToken, Leg leg) {
    this.routeToken = requireNonNull(routeToken);
    this.resolvedToken = requireNonNull(resolvedToken);
    this.leg = requireNonNull(leg);
  }

  public static ResolvedLeg create(RouteToken routeToken, ResolvedToken resolvedToken, Leg leg) {
    return new ResolvedLeg(routeToken, resolvedToken, leg);
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
