package org.mitre.tdp.boogie.alg;

import static org.mitre.tdp.boogie.alg.LegMergerFactory.isLeadingTrailingDF;
import static org.mitre.tdp.boogie.alg.LegMergerFactory.isSubsequentSimilarLeg;
import static org.mitre.tdp.boogie.alg.LegMergerFactory.isTrailingInternalIF;

import java.util.List;
import java.util.function.BiPredicate;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedLeg;
import org.mitre.tdp.boogie.fn.LeftMerger;

final class ExpandedRouteFactory {

  private ExpandedRouteFactory() {
    throw new IllegalStateException("Unable to instantiate static factory class.");
  }

  static ExpandedRoute newExpandedRouteFrom(@Nullable RouteSummary routeSummary, List<ResolvedLeg> resolvedLegs) {
    BiPredicate<ExpandedRoute.ExpandedRouteLeg, ExpandedRoute.ExpandedRouteLeg> pred = isLeadingTrailingDF();
    LeftMerger<ExpandedRoute.ExpandedRouteLeg> legMerger = new LeftMerger<>(pred.or(isTrailingInternalIF()).or(isSubsequentSimilarLeg()), (l1, l2) -> l1);

    List<ExpandedRoute.ExpandedRouteLeg> legs = resolvedLegs.stream()
        .map(resolvedLeg -> new ExpandedRoute.ExpandedRouteLeg(
            resolvedLeg.split().value(),
            ElementType.fromResolvedElement(resolvedLeg.sourceElement()),
            resolvedLeg.split().wildcards(),
            resolvedLeg.leg())
        ).collect(legMerger.asCollector());

    return new ExpandedRoute(routeSummary, legs);
  }
}
