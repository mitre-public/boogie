package org.mitre.tdp.boogie.conformance.alg.evaluate;

import java.time.Instant;
import java.util.List;
import java.util.NavigableMap;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;

public class PrecomputedEvaluatorWrapper implements ConformanceEvaluator {

  private final PrecomputedEvaluator evaluator;
  private final NavigableMap<Instant, Boolean> conformanceTimes;

  private PrecomputedEvaluatorWrapper(
      PrecomputedEvaluator evaluator,
      NavigableMap<Instant, Boolean> conformanceTimes) {
    this.evaluator = evaluator;
    this.conformanceTimes = conformanceTimes;
  }

  public PrecomputedEvaluator evaluator() {
    return evaluator;
  }

  @Override
  public boolean conforming(ConformablePoint point, ConsecutiveLegs consecutiveLegs) {
    return conformanceTimes.lowerEntry(point.time()).getValue();
  }

  public PrecomputedEvaluatorWrapper wrapWithContext(
      PrecomputedEvaluator evaluator,
      List<Pair<ConformablePoint, ConsecutiveLegs>> pairs) {

    return new PrecomputedEvaluatorWrapper(
        evaluator,
        evaluator.conformanceTimes(pairs));
  }
}
