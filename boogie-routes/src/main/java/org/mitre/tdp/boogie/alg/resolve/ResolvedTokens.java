package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import org.mitre.tdp.boogie.alg.split.RouteToken;

/**
 * Wrapper class for a section containing context flags from the filed route string itself (e.g. direct/suppressed) as well
 * as a collection of possible infrastructure elements that were determined to be associated with the section id via the
 * {@link RouteTokenResolver}.
 */
public final class ResolvedTokens {

  /**
   * The split section with wildcards and etc associated with this collection of route elements.
   */
  private final RouteToken routeToken;
  /**
   * List of resolved infrastructure elements.
   *
   * <p>This is the superset of all potential elements the filed section of the route string could possible be referring to.
   */
  private final Collection<ResolvedToken> resolvedTokens;

  public ResolvedTokens(RouteToken routeToken, Collection<ResolvedToken> resolvedTokens) {
    this.routeToken = requireNonNull(routeToken);
    this.resolvedTokens = resolvedTokens;
  }

  public RouteToken routeToken() {
    return routeToken;
  }

  public Collection<ResolvedToken> resolvedTokens() {
    return resolvedTokens;
  }
}
