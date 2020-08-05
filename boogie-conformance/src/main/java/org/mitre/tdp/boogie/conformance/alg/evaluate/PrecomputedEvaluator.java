package org.mitre.tdp.boogie.conformance.alg.evaluate;

import java.time.Instant;
import java.util.NavigableMap;
import java.util.Optional;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPair;

import com.google.common.collect.Maps;

/**
 * This is an interface for {@link ConformanceEvaluator}s we don't want to have to run multiple times to
 * generate the output and which also require potentially the full context of a track to determine a-priori
 * conformance times.
 *
 * <p>From a holistic perspective there is a class of algorithms which can produce refined results when
 * given the full context of the input data - we want to support pre-computing these algorithms and then
 * simply serving their results via the {@link ConformanceEvaluator} interface rather than having them
 * directly implement it.
 *
 * <p>These algorithms can be wrapped in the {@link PrecomputedEvaluatorWrapper} class which will serve
 * their results as a common {@link ConformanceEvaluator}.
 */
@FunctionalInterface
public interface PrecomputedEvaluator {

  /**
   * Returns a {@link NavigableMap} giving the start time of either a conforming (true) or non-conforming
   * (false) portion of the track.
   */
  NavigableMap<Instant, Boolean> conformanceTimes(NavigableMap<ConformablePoint, LegPair> conformablePairs);

  /**
   * Utility method of inputs where not each point may necessarily be assigned to an explicit LegPair.
   */
  default NavigableMap<Instant, Boolean> optionalConformanceTimes(NavigableMap<ConformablePoint, Optional<LegPair>> conformableMap) {
    return conformanceTimes(Maps.transformValues(Maps.filterEntries(conformableMap, entry -> entry.getValue().isPresent()), Optional::get));
  }
}
