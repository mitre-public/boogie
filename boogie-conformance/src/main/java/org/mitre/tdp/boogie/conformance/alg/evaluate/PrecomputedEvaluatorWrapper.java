package org.mitre.tdp.boogie.conformance.alg.evaluate;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.NavigableMap;
import java.util.Optional;

import org.mitre.tdp.boogie.ConformablePoint;

public final class PrecomputedEvaluatorWrapper implements ConformanceEvaluator {

  private final PrecomputedEvaluator evaluator;
  private final NavigableMap<Instant, Boolean> conformanceTimes;

  public PrecomputedEvaluatorWrapper(
      PrecomputedEvaluator evaluator,
      NavigableMap<Instant, Boolean> conformanceTimes) {
    this.evaluator = requireNonNull(evaluator);
    this.conformanceTimes = conformanceTimes;
  }

  public PrecomputedEvaluator evaluator() {
    return evaluator;
  }

  @Override
  public Optional<Boolean> conforming(ConformablePoint point, LegPair consecutiveLegs) {
    return Optional.ofNullable(conformanceTimes.lowerEntry(point.time()).getValue());
  }

  /**
   * Wraps the given {@link PrecomputedEvaluator} as a {@link ConformanceEvaluator} for use in conformance estimation.
   */
  public PrecomputedEvaluatorWrapper wrapAndPrecompute(PrecomputedEvaluator evaluator, NavigableMap<ConformablePoint, LegPair> pairs) {
    requireNonNull(evaluator);
    return new PrecomputedEvaluatorWrapper(evaluator, evaluator.conformanceTimes(pairs));
  }
}
