package org.mitre.tdp.boogie.conformance.alg.evaluate;

import static org.mitre.tdp.boogie.conformance.alg.evaluate.ConformanceEvaluator.offTrackDistance;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Pair;
import org.mitre.caasd.commons.Speed;
import org.mitre.caasd.commons.math.FastLinearApproximation;
import org.mitre.caasd.commons.math.PiecewiseLinearSplitter;
import org.mitre.caasd.commons.math.XyDataset;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;

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
    NavigableMap<Duration, Pair<Speed, Distance>> piecewiseSlopes = computeOffsetToFittedSpeedAverageDistance(conformingPairs);
    Instant t0 = conformingPairs.get(0).first().time();

    return piecewiseSlopes.entrySet().stream().collect(Collectors.toMap(
        e -> t0.plus(e.getKey()),
        e -> isLevelAndNotOffset(e.getValue()),
        (k1, k2) -> {throw new RuntimeException();},
        TreeMap::new
    ));
  }

  default NavigableMap<Duration, Pair<Speed, Distance>> computeOffsetToFittedSpeedAverageDistance(List<Pair<ConformablePoint, ConsecutiveLegs>> conformingPairs) {
    XyDataset dataset = convertToXYData(conformingPairs);
    XyDataset[] splits = piecewiseSplits(dataset);
    return asOffsetToFittedSpeedAverageDistance(splits);
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
   * Converts the returned split {@link XyDataset} into a map of:
   *
   * key = offset from t0 in millis
   * value = {Slope (as cross-track speed in knots), average y (as average distance from leg in nm)}.
   *
   * This map can later be transformed to get the time intervals and the cross t
   */
  default NavigableMap<Duration, Pair<Speed, Distance>> asOffsetToFittedSpeedAverageDistance(XyDataset[] splits) {
    NavigableMap<Duration, Pair<Speed, Distance>> offsetToFittedSpeedAverageDuration = new TreeMap<>();

    Arrays.stream(splits).forEach(xyDataset -> {
      FastLinearApproximation approximation = xyDataset.approximateFit();

      Pair<Speed, Distance> pair = Pair.of(
          Speed.of(approximation.slope(), Speed.Unit.KNOTS),
          Distance.ofNauticalMiles(approximation.averageY()));

      Duration offset = Duration.ofMillis(hoursToEpoch(approximation.minX()));
      offsetToFittedSpeedAverageDuration.put(offset, pair);
    });

    return offsetToFittedSpeedAverageDuration;
  }

  default XyDataset[] piecewiseSplits(XyDataset dataset) {
    PiecewiseLinearSplitter splitter = new PiecewiseLinearSplitter(maxError().inNauticalMiles());
    return dataset.split(splitter);
  }

  /**
   * Converts the given list of pairs of conformable points to their assigned legs to a {@link XyDataset}.
   *
   * x = epoch time in hours
   * y = cross track distance from leg in nm
   */
  default XyDataset convertToXYData(List<Pair<ConformablePoint, ConsecutiveLegs>> conformingPairs) {
    List<Double> times = new ArrayList<>();
    List<Double> crossTrackDistances = new ArrayList<>();

    conformingPairs.forEach(pair -> offTrackDistance(pair.first(), pair.second())
        .ifPresent(distance -> {
          times.add(epochToHours(pair.first().time().toEpochMilli()));
          crossTrackDistances.add(distance.inNauticalMiles());
        }));

    return new XyDataset(times, crossTrackDistances);
  }

  default Double epochToHours(Long epoch) {
    return epoch.doubleValue() / hour;
  }

  default Long hoursToEpoch(Double hours) {
    return (long) (hours * hour);
  }

  double hour = 60.0d * 60.0d * 1000.0d;

  /**
   * Creates a new anonymous instance of a {@link LinearSplitErrorEvaluator}.
   */
  static LinearSplitErrorEvaluator newInstance() {
    return new LinearSplitErrorEvaluator() {};
  }

  static LinearSplitErrorEvaluator newInstance(Distance maxError, Speed rateOfChangeThreshold) {
    return new LinearSplitErrorEvaluator() {
      @Override
      public Distance maxError() {
        return maxError;
      }

      @Override
      public Speed rateOfChangeThreshold() {
        return rateOfChangeThreshold;
      }
    };
  }
}
