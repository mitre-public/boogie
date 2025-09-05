package org.mitre.tdp.boogie.alg.facade;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.alg.resolve.ElementType;

/**
 * This class is used to identify when a different section and element type goes into an approach.
 * <p></p>
 * This must be run before the redundant leg combiner.
 */
final class SomethingIntoApproach implements BiPredicate<ExpandedRouteLeg, ExpandedRouteLeg> {
  static final SomethingIntoApproach INSTANCE = new SomethingIntoApproach();
  private SomethingIntoApproach() {}
  @Override
  public boolean test(ExpandedRouteLeg leg, ExpandedRouteLeg leg2) {
    return !leg.section().equals(leg2.section()) && leg2.elementType().equals(ElementType.APPROACH);
  }
}
