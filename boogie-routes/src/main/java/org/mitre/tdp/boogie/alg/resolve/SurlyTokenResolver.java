package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nullable;

import org.mitre.tdp.boogie.alg.split.RouteToken;

/**
 * This class is decorator for the SectionResolver used inside RouteExpander. Functionally it works the exact same way however,
 * it throws an exception when the SectionResolver doesn't find any matching infrastructure for a section of a route.
 */
final class SurlyTokenResolver implements RouteTokenResolver {

  private final RouteTokenResolver routeTokenResolver;

  SurlyTokenResolver(RouteTokenResolver routeTokenResolver) {
    this.routeTokenResolver = requireNonNull(routeTokenResolver, "RouteTokenResolver cannot be null");
  }

  @Override
  public ResolvedSection resolve(@Nullable RouteToken previous, RouteToken current, @Nullable RouteToken next) {
    ResolvedSection resolvedSection = routeTokenResolver.resolve(previous, current, next);
    if (resolvedSection.elements().isEmpty()) {
      throw missingSectionErrorMessage(current);
    }
    return resolvedSection;
  }

  private IllegalStateException missingSectionErrorMessage(RouteToken current) {
    return new IllegalStateException("RouteTokenResolver could not find any matching infrastructure for: " + current.infrastructureName());
  }
}