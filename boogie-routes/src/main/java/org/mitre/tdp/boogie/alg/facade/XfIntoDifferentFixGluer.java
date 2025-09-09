package org.mitre.tdp.boogie.alg.facade;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.fn.LeftMerger;

/**
 * This class acts as the older section gluer into approach elements.
 * Previous coding can end with a route discontinuity, overlap, or cleanly with an approach.
 * This class ensures that the connection is legitimate from an ARINC 424 path definition point of view.
 */
final class XfIntoDifferentFixGluer implements UnaryOperator<List<ExpandedRouteLeg>> {
  private final BiPredicate<ExpandedRouteLeg, ExpandedRouteLeg> differentLocation = (c, n) -> c.associatedFix()
      .filter(cf -> n.associatedFix().filter(nf -> nf.distanceInNmTo(cf) > 1e-5).isPresent()) //checks the float value
      .isPresent();
  private final BiPredicate<ExpandedRouteLeg, ExpandedRouteLeg> fixNamedDifferent = (c, n) -> c.associatedFix().filter(cf -> n.associatedFix().filter(nf -> nf.fixIdentifier().equals(cf.fixIdentifier())).isEmpty()).isEmpty();
  private final BiPredicate<ExpandedRouteLeg, ExpandedRouteLeg> nextIsApproach = (c, n) -> n.elementType().equals(ElementType.APPROACH);
  private final BiPredicate<ExpandedRouteLeg, ExpandedRouteLeg> originFixTerminating = (c, n) -> c.pathTerminator().isFixTerminating();
  private final BiPredicate<ExpandedRouteLeg, ExpandedRouteLeg> destFixOriginating = (c, n) -> n.pathTerminator().isFixOriginating();
  private final BiPredicate<ExpandedRouteLeg, ExpandedRouteLeg> shouldMerge = (c, n) -> nextIsApproach
      .and(originFixTerminating)
      .and(destFixOriginating)
      .and(fixNamedDifferent)
      .and(differentLocation)
      .test(c, n);
  private final BiFunction<ExpandedRouteLeg, ExpandedRouteLeg, ExpandedRouteLeg> merge = new DfMerge();

  XfIntoDifferentFixGluer() {
  }

  @Override
  public List<ExpandedRouteLeg> apply(List<ExpandedRouteLeg> expandedRouteLegs) {
    LeftMerger<ExpandedRouteLeg> merger = new LeftMerger<>(
        shouldMerge,
        merge
    );
    return expandedRouteLegs.stream().collect(merger.asCollector());
  }
}
