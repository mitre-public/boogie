package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.alg.split.RouteTokenVisitor;

final class FixResolver implements SingleTokenResolver {

  private final LookupService<Fix> lookupService;

  FixResolver(LookupService<Fix> lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedToken> resolve(RouteToken sectionSplit) {

    String section = sectionSplit.infrastructureName();

    // check to see if the parsed section is a tailored waypoint reference
    // if so extract the course/distance suffix from the fix identifier
    String s = section.matches(SectionHeuristics.TAILORED.pattern())
        ? section.substring(0, section.length() - 6)
        : section;

    // TODO - do this please
//    boolean isDirect = RouteTokenVisitor.isDirect(sectionSplit);
//
//    boolean isTailored = RouteTokenVisitor.isTailored(sectionSplit);


    String wildcards = RouteTokenVisitor.wildcards(sectionSplit);

    // supplier?
//    if (isDirect) {
//      new DirectToFixElement(fix);
//    } else {
//      new InitialFixElement(fix);
//    }

    return lookupService
        .apply(s).stream()
        .map(fix -> section.equals(s)
            ? new FixToken(fix, wildcards)
            : new TailoredToken(fix, section, wildcards))
        .collect(Collectors.toList());
  }
}
