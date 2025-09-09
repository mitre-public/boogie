package org.mitre.tdp.boogie.alg.facade;

import java.util.List;
import java.util.function.Function;

import org.mitre.tdp.boogie.alg.ResolvedLeg;

/**
 * This class is only converts the resolved legs to expanded route legs. No changing or combining.
 */
final class InarticulateLegConverter implements Function<List<ResolvedLeg>, List<ExpandedRouteLeg>> {
  private final ResolvedLegConverter  resolvedLegConverter = new ResolvedLegConverter();

  private InarticulateLegConverter() {
  }

  static InarticulateLegConverter newInstance() {
    return new InarticulateLegConverter();
  }

  @Override
  public List<ExpandedRouteLeg> apply(List<ResolvedLeg> resolvedLegs) {
    return resolvedLegConverter.apply(resolvedLegs);
  }
}
