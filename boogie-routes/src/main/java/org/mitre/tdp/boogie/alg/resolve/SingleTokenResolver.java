package org.mitre.tdp.boogie.alg.resolve;

import java.util.List;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.alg.split.RouteToken;

/**
 * A {@link SingleTokenResolver} is an extension of a {@link RouteTokenResolver} which doesn't rely on context from the previous
 * or next sections of the route string to perform resolution.
 */
@FunctionalInterface
public interface SingleTokenResolver extends RouteTokenResolver {

  List<ResolvedToken> resolve(RouteToken routeToken);

  @Override
  default ResolvedTokens resolve(@Nullable RouteToken previous, RouteToken current, @Nullable RouteToken next) {
    return new ResolvedTokens(current, resolve(current));
  }
}
