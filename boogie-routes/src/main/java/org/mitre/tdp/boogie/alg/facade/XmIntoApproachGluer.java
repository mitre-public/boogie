package org.mitre.tdp.boogie.alg.facade;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.fn.LeftMerger;

/**
 * This deals with if there are XM legs still in the route that we want dropped per contract.
 */
final class XmIntoApproachGluer implements UnaryOperator<List<ExpandedRouteLeg>> {
  private final BiPredicate<ExpandedRouteLeg, ExpandedRouteLeg> nextIsApproach = (c, n) -> n.elementType().equals(ElementType.APPROACH);
  private final BiPredicate<ExpandedRouteLeg, ExpandedRouteLeg> currentIsManuallyTerminating = (c, n) -> c.pathTerminator().isManualTerminating();
  private final BiPredicate<ExpandedRouteLeg, ExpandedRouteLeg> shouldMerge = (c,n) -> nextIsApproach.and(currentIsManuallyTerminating).test(c, n);
  /**
   * This can only happen on sid/star into approach -or- approach transition into approach.
   * Lucky for us that means the next leg has a fix to just use as a DF into.
   */
  private final BiFunction<ExpandedRouteLeg, ExpandedRouteLeg, ExpandedRouteLeg> merge = new DfMerge();
  XmIntoApproachGluer() {}

  @Override
  public List<ExpandedRouteLeg> apply(List<ExpandedRouteLeg> expandedRouteLegs) {
    LeftMerger<ExpandedRouteLeg> merger = new LeftMerger<>(
        shouldMerge,
        merge
    );
    return expandedRouteLegs.stream().collect(merger.asCollector());
  }
}
