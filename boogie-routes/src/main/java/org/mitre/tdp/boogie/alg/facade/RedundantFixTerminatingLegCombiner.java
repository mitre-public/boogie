package org.mitre.tdp.boogie.alg.facade;

import org.mitre.tdp.boogie.fn.LeftMerger;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * A sequential leg combiner attempts to <i>safely</i> combine the definitions of two legs when certain conditions are met.
 * <br>
 * The primary goal is to merge legs identified within the expanded route which terminate in similar fixes (as a side effect of
 * the overall expansion process), e.g. a STAR ending at the fix an approach starts from.
 * <br>
 * However the word "safely" is used here because simply choosing one implementation of the leg from the expansion is not by
 * itself sufficient to capture all of the correct information about how the aircraft will fly the leg - see {@link LegMerge}
 * for how the two are combined.
 */
final class RedundantFixTerminatingLegCombiner implements UnaryOperator<List<ExpandedRouteLeg>> {
  @Override
  public List<ExpandedRouteLeg> apply(List<ExpandedRouteLeg> legs) {
    LeftMerger<ExpandedRouteLeg> merger = new LeftMerger<>(
        SimilarFixTerminatingLeg.INSTANCE::test,
        (previous, next) -> LegMerge.between(previous, next).merge(previous, next)
    );
    return legs.stream().collect(merger.asCollector());
  }
}
