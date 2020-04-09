package org.mitre.tdp.boogie.conformance.alg.evaluate;

import java.time.Instant;
import java.util.List;
import java.util.NavigableMap;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;

/**
 * This is an interface for {@link ConformanceEvaluator}s we don't want to have to run multiple times
 * to generate the output and which also require potentially the full context of a track to determine
 * a-priori conformance times.
 *
 * <p>From a holistic perspective there is a class of algorithms which can produce refined results when
 * given the full context of the input data - we want to support pre-computing these algorithms and then
 * simply serving their results via the {@link ConformanceEvaluator} interface rather than having them
 * directly implement it.
 *
 * <p>These algorithms can be wrapped in the {@link PrecomputedEvaluatorWrapper} class which will serve
 * their results as a typical {@link ConformanceEvaluator}.
 */
@FunctionalInterface
public interface PrecomputedEvaluator {

  /**
   * Returns a {@link NavigableMap} giving the start time of either a conforming (true) or non-conforming
   * (false) portion of the track.
   */
  NavigableMap<Instant, Boolean> conformanceTimes(List<Pair<ConformablePoint, ConsecutiveLegs>> conformingPairs);
}
