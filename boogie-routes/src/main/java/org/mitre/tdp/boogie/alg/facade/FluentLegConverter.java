package org.mitre.tdp.boogie.alg.facade;

import java.util.List;
import java.util.function.Function;

import org.mitre.tdp.boogie.alg.ResolvedLeg;

/**
 * This class is responsible for the contract with external users on the continuity of the route that the expander produces.
 * This class intends to ensure that the route created could be used in an FMS later on without additional collapsing logic.
 * As such there are opinions about things based on user input (e.g., dropping FM legs at the end of a star).
 */
final class FluentLegConverter implements Function<List<ResolvedLeg>, List<ExpandedRouteLeg>> {
  private final DirectToConverter directToConverter = new DirectToConverter();
  private final ResolvedLegConverter resolvedLegConverter = new ResolvedLegConverter();
  private final XfIntoDifferentFixGluer xFIntoDifferentFixGluer = new XfIntoDifferentFixGluer();
  private final XmIntoApproachGluer xmIntoApproachGluer = new XmIntoApproachGluer();
  private final RedundantLegCombiner redundantLegCombiner = new RedundantLegCombiner();

  private FluentLegConverter() {}

  static FluentLegConverter newInstance() {
    return new FluentLegConverter();
  }

  @Override
  public List<ExpandedRouteLeg> apply(List<ResolvedLeg> resolvedLegs) {
    return directToConverter
        .andThen(resolvedLegConverter)
        .andThen(xFIntoDifferentFixGluer)
        .andThen(xmIntoApproachGluer)
        .andThen(redundantLegCombiner)
        .apply(resolvedLegs);
  }
}
