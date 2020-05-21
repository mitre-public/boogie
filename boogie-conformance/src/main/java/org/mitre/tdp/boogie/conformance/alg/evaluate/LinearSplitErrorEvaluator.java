package org.mitre.tdp.boogie.conformance.alg.evaluate;

import static org.mitre.tdp.boogie.conformance.alg.evaluate.ConformanceEvaluator.offTrackDistance;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Pair;
import org.mitre.caasd.commons.Speed;
import org.mitre.caasd.commons.math.PiecewiseLinearSplitter;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;
import org.mitre.tdp.boogie.utils.Iterators;

import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;

/**
 * Leverages a modified version of the Douglas-Peucker algorithm to partition the set of conformable
 * points into level/non-level segments based on how the cross track distance from the leg set changes
 * over time.
 *
 * <p>In this paradigm when the cross track distance is changing rapidly the aircraft is considered non
 * conforming to the leg - while when it is staying constant (and not offset) its considered on-track.
 * e.g. level portions are essentially flying parallel to the route.
 */
public interface LinearSplitErrorEvaluator extends PrecomputedEvaluator {

  /**
   * The maximum error between the final linear fit curve and the data points.
   */
  default Distance maxError() {
    return Distance.ofNauticalMiles(1.0);
  }

  /**
   * The rate of change threshold to use to consider a segment level/non-level.
   */
  default Speed rateOfChangeThreshold() {
    return Speed.of(10.0, Speed.Unit.KNOTS);
  }

  /**
   * Returns the mapping of {time, conforming?} based on the computed linear split change points.
   */
  @Override
  default NavigableMap<Instant, Boolean> conformanceTimes(List<Pair<ConformablePoint, ConsecutiveLegs>> conformingPairs) {
    NavigableMap<Instant, Pair<Speed, Distance>> piecewiseSlopes = getPiecewiseSplits(conformingPairs);
    return Maps.transformValues(piecewiseSlopes, this::isLevelAndNotOffset);
  }

  /**
   * Returns whether the given speed/distance pair is level and offset from the assigned leg.
   */
  default boolean isLevelAndNotOffset(Pair<Speed, Distance> pair) {
    Speed slope = pair.first();
    Distance maxCtd = pair.second();
    return (slope.isLessThan(rateOfChangeThreshold()))
        && (maxCtd.isLessThan(maxError().times(2.0)));
  }

  /**
   * Returns as a map the output of the {@link PiecewiseLinearSplitter} with the additional context of in
   * the value of the slope and max error within the split segment.
   */
  default NavigableMap<Instant, Pair<Speed, Distance>> getPiecewiseSplits(List<Pair<ConformablePoint, ConsecutiveLegs>> conformingPairs) {
    PiecewiseLinearSplitter splitter = new PiecewiseLinearSplitter(maxError().inNauticalMiles());

    List<Long> times = new ArrayList<>();
    List<Double> crossTrackDistances = new ArrayList<>();

    conformingPairs.forEach(pair -> offTrackDistance(pair.first(), pair.second())
        .ifPresent(distance -> {
          times.add(pair.first().time().toEpochMilli());
          crossTrackDistances.add(distance.inNauticalMiles());
        }));

    List<Double> normTimes = times.stream()
        .map(time -> normTime(time, times.get(0)))
        .collect(Collectors.toList());

    int[] splits = splitter.computeSplitsFor(normTimes, crossTrackDistances);

    NavigableMap<Instant, Pair<Speed, Distance>> conforming = new TreeMap<>();
    Iterators.pairwise(Ints.asList(splits),
        (i1, i2) -> conforming.put(
            Instant.ofEpochMilli(times.get(i1)),
            computeSlopeAndMaxDeviation(normTimes, crossTrackDistances, i1, i2)));

    return conforming;
  }

  /**
   * Computes the slope and max deviation over the course of the split starting at i0 and ending at the element
   * before i1.
   */
  default Pair<Speed, Distance> computeSlopeAndMaxDeviation(List<Double> times, List<Double> distances, int i0, int i1) {
    double t0 = times.get(i0);
    double t1 = times.get(i1 - 1);

    double d0 = distances.get(i0);
    double d1 = distances.get(i1 - 1);

    double maxError = distances.subList(i0, i1).stream()
        .mapToDouble(d -> d).max()
        .orElseThrow(RuntimeException::new);

    Speed roc = Distance.ofNauticalMiles(d1 - d0)
        .dividedBy(Duration.ofMillis((long) (t1 - t0) * 60000L));

    return Pair.of(roc, Distance.ofNauticalMiles(maxError));

  }

  default double normTime(long time, long t0) {
    return (double) (time - t0) / 60000.0d;
  }
}
