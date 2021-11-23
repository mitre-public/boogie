package org.mitre.tdp.boogie.conformance.alg.evaluate;

import static org.mitre.tdp.boogie.conformance.alg.evaluate.MaxOffTrackDistanceEvaluator.offTrackDistance;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.FastMath;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Pair;
import org.mitre.caasd.commons.Speed;
import org.mitre.caasd.commons.math.FastLinearApproximation;
import org.mitre.caasd.commons.math.PiecewiseLinearSplitter;
import org.mitre.caasd.commons.math.XyDataset;
import org.mitre.tdp.boogie.ConformablePoint;

/**
 * Leverages a modified version of the Douglas-Peucker algorithm to partition the set of conformable points into level/non-level
 * segments based on how the cross track distance from the leg set changes over time.
 *
 * <p>In this paradigm when the cross track distance is changing rapidly the aircraft is considered non conforming to the leg -
 * while when it is staying constant (and not offset) its considered on-track.
 * e.g. level portions are essentially flying parallel to the route.
 */
public class LinearSplitErrorEvaluator implements PrecomputedEvaluator {

  /**
   * The maximum error between the final linear fit curve and the data points.
   */
  private final Distance maxError;
  /**
   * The rate of change threshold to use to consider a segment level/non-level.
   */
  private final Speed rateOfChangeThreshold;

  public LinearSplitErrorEvaluator() {
    this(Distance.ofNauticalMiles(1.0), Speed.of(10.0, Speed.Unit.KNOTS));
  }

  public LinearSplitErrorEvaluator(Distance maxError, Speed rateOfChangeThreshold) {
    this.maxError = maxError;
    this.rateOfChangeThreshold = rateOfChangeThreshold;
  }

  public Distance maxError() {
    return maxError;
  }

  public Speed rateOfChangeThreshold() {
    return rateOfChangeThreshold;
  }

  /**
   * Returns the mapping of {time, conforming?} based on the computed linear split change points.
   */
  @Override
  public NavigableMap<Instant, Boolean> conformanceTimes(NavigableMap<ConformablePoint, LegPair> conformablePairs) {
    NavigableMap<Duration, Pair<Speed, Distance>> piecewiseSlopes = computeOffsetToFittedSpeedAverageDistance(conformablePairs);
    Instant t0 = conformablePairs.firstKey().time();

    return piecewiseSlopes.entrySet().stream().collect(Collectors.toMap(
        e -> t0.plus(e.getKey()),
        e -> isLevelAndNotOffset(e.getValue()),
        (k1, k2) -> {throw new IllegalStateException("Key collision in map. K1: ".concat(k1.toString()).concat(" K2: ".concat(k2.toString())));},
        TreeMap::new
    ));
  }

  /**
   * Returns a map containing the Duration offset from the initial point in the conforming point to the fitted speed and max
   * cross track distance over the course of the split.
   */
  public NavigableMap<Duration, Pair<Speed, Distance>> computeOffsetToFittedSpeedAverageDistance(NavigableMap<ConformablePoint, LegPair> conformingPairs) {
    XyDataset dataset = convertToXYData(conformingPairs);
    XyDataset[] splits = piecewiseSplits(dataset);
    return asOffsetToFittedSpeedAverageDistance(splits);
  }

  /**
   * Returns whether the given speed/distance pair is level and offset from the assigned leg.
   */
  public boolean isLevelAndNotOffset(Pair<Speed, Distance> pair) {
    Speed slope = pair.first();
    Distance maxCtd = pair.second();
    return (slope.abs().isLessThan(rateOfChangeThreshold()))
        && (maxCtd.abs().isLessThan(maxError().times(2.0)));
  }

  /**
   * Converts the returned split {@link XyDataset} into a map of:
   *
   * key = offset from t0 in millis
   * value = {Slope (as cross-track speed in knots), maxY (absVal)}.
   *
   * This map can later be transformed to get the time intervals and the cross t
   */
  public NavigableMap<Duration, Pair<Speed, Distance>> asOffsetToFittedSpeedAverageDistance(XyDataset[] splits) {
    NavigableMap<Duration, Pair<Speed, Distance>> offsetToFittedSpeedAverageDuration = new TreeMap<>();

    Arrays.stream(splits).forEach(xyDataset -> {
      FastLinearApproximation approximation = xyDataset.approximateFit();
      double maxY = xyDataset.yData().stream().mapToDouble(FastMath::abs).max().orElse(0.0);

      Pair<Speed, Distance> pair = Pair.of(
          Speed.of(approximation.slope(), Speed.Unit.KNOTS),
          Distance.ofNauticalMiles(maxY));

      Long epoch = hoursToEpoch(approximation.minX());
      Duration offset = Duration.ofMillis(epoch);
      offsetToFittedSpeedAverageDuration.put(offset, pair);
    });

    return offsetToFittedSpeedAverageDuration;
  }

  public XyDataset[] piecewiseSplits(XyDataset dataset) {
    PiecewiseLinearSplitter splitter = new PiecewiseLinearSplitter(maxError().inNauticalMiles());
    return dataset.split(splitter);
  }

  /**
   * Converts the given list of pairs of conformable points to their assigned legs to a {@link XyDataset}.
   *
   * x = epoch time in hours
   * y = cross track distance from leg in nm
   */
  public XyDataset convertToXYData(NavigableMap<ConformablePoint, LegPair> conformingPairs) {
    List<Double> times = new ArrayList<>();
    List<Double> crossTrackDistances = new ArrayList<>();

    long t0 = conformingPairs.firstKey().time().toEpochMilli();
    conformingPairs.forEach((point, pair) -> offTrackDistance(point, pair)
        .ifPresent(distance -> {
          times.add(epochToHours(point.time().toEpochMilli() - t0));
          crossTrackDistances.add(distance.inNauticalMiles());
        }));

    return new XyDataset(times, crossTrackDistances);
  }

  private Double epochToHours(Long epoch) {
    return epoch.doubleValue() / hour;
  }

  private Long hoursToEpoch(Double hours) {
    return (long) (hours * hour);
  }

  double hour = 60.0d * 60.0d * 1000.0d;
}
