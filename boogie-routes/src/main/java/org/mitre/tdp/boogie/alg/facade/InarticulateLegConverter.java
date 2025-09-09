package org.mitre.tdp.boogie.alg.facade;

import java.util.List;
import java.util.function.Function;

import org.mitre.tdp.boogie.alg.ResolvedLeg;

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
