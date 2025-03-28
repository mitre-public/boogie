package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedToken.directToFix;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedToken.standardFix;
import static org.mitre.tdp.boogie.alg.split.RouteTokenVisitor.isTailoredBefore;

import java.util.List;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

final class FixResolver implements SingleTokenResolver {

  private final LookupService<Fix> lookupService;

  FixResolver(LookupService<Fix> lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedToken> resolve(RouteToken token) {
    return lookupService.apply(token.infrastructureName()).stream()
        .map(fix -> isTailoredBefore(token) ? standardFix(fix) : directToFix(fix))
        .collect(toList());
  }
}
