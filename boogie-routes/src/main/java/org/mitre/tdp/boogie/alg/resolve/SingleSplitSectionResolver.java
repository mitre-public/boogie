package org.mitre.tdp.boogie.alg.resolve;

import java.util.List;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.alg.split.RouteToken;

/**
 * A {@link SingleSplitSectionResolver} is an extension of a {@link SectionResolver} which doesn't rely on context from the previous
 * or next sections of the route string to perform resolution.
 */
@FunctionalInterface
public interface SingleSplitSectionResolver extends SectionResolver {

  List<ResolvedElement> resolve(RouteToken sectionSplit);

  @Override
  default ResolvedSection resolve(@Nullable RouteToken previous, RouteToken current, @Nullable RouteToken next) {
    return new ResolvedSection(current, resolve(current));
  }
}
