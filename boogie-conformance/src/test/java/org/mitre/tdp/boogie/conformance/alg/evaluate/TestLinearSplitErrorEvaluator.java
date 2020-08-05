package org.mitre.tdp.boogie.conformance.alg.evaluate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.caasd.commons.Speed;
import org.mitre.caasd.commons.math.XyDataset;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPair;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPairImpl;

public class TestLinearSplitErrorEvaluator {

  @Test
  public void testIsLevelAndNotOffsetWhenBelowROCThresholdAndBelowOffsetThreshold() {
    LinearSplitErrorEvaluator splitter = LinearSplitErrorEvaluator.newInstance(Distance.ofNauticalMiles(1.0), Speed.of(10.0, Speed.Unit.KNOTS));
    assertTrue(splitter.isLevelAndNotOffset(Pair.of(Speed.of(5.0, Speed.Unit.KNOTS), Distance.ofNauticalMiles(1.0))));
  }

  @Test
  public void isNotLevelAndNotOffsetWhenAboveROCThreshold() {
    LinearSplitErrorEvaluator splitter = LinearSplitErrorEvaluator.newInstance(Distance.ofNauticalMiles(1.0), Speed.of(10.0, Speed.Unit.KNOTS));
    assertFalse(splitter.isLevelAndNotOffset(Pair.of(Speed.of(15.0, Speed.Unit.KNOTS), Distance.ofNauticalMiles(1.0))));
  }

  @Test
  public void isNotLevelAndNotOffsetWhenAboveOffsetThreshold() {
    LinearSplitErrorEvaluator splitter = LinearSplitErrorEvaluator.newInstance(Distance.ofNauticalMiles(1.0), Speed.of(10.0, Speed.Unit.KNOTS));
    assertFalse(splitter.isLevelAndNotOffset(Pair.of(Speed.of(5.0, Speed.Unit.KNOTS), Distance.ofNauticalMiles(3.0))));
  }

  private ConformablePoint conformablePointAt(LatLong latLong) {
    ConformablePoint conformablePoint = mock(ConformablePoint.class);
    when(conformablePoint.latLong()).thenReturn(latLong);
    when(conformablePoint.latitude()).thenCallRealMethod();
    when(conformablePoint.longitude()).thenCallRealMethod();
    when(conformablePoint.distanceInNmTo(any())).thenCallRealMethod();
    when(conformablePoint.courseInDegrees(any())).thenCallRealMethod();
    when(conformablePoint.toString()).thenReturn(latLong.latitude() + ":" + latLong.longitude());
    return conformablePoint;
  }

  /**
   * 1) Generate a single leg between {0.0, 0.0} and {0.0, 5.0}.
   * 2) Generate points moving along the leg in the longitudinal direction, and away from the leg at a constant rate for the
   * first 5 steps and back towards it for the last 5. Movement looks like a ^ above the leg.
   *
   * This means we expect two linear splits - and we expect the speed in knots to be reasonable and the average distance off
   * the route should.
   */
  @Test
  public void testLinearSplitterErrorEvaluator() {
    Fix term1 = mock(Fix.class);
    when(term1.latLong()).thenReturn(LatLong.of(0.0, 0.0));
    when(term1.latitude()).thenCallRealMethod();
    when(term1.longitude()).thenCallRealMethod();
    when(term1.distanceInNmTo(any())).thenCallRealMethod();
    when(term1.distanceInRadians(any())).thenCallRealMethod();
    when(term1.courseInDegrees(any())).thenCallRealMethod();
    when(term1.toString()).thenReturn(0.0 + ":" + 0.0);

    Leg l1 = mock(Leg.class);
    when(l1.pathTerminator()).thenReturn(term1);
    when(l1.type()).thenReturn(PathTerm.TF);
    when(l1.toString()).thenReturn(0.0 + ":" + 0.0);

    Fix term2 = mock(Fix.class);
    when(term2.latLong()).thenReturn(LatLong.of(0.0, 5.0));
    when(term2.latitude()).thenCallRealMethod();
    when(term2.longitude()).thenCallRealMethod();
    when(term2.distanceInNmTo(any())).thenCallRealMethod();
    when(term2.distanceInRadians(any())).thenCallRealMethod();
    when(term2.courseInDegrees(any())).thenCallRealMethod();
    when(term1.toString()).thenReturn(0.0 + ":" + 5.0);

    Leg l2 = mock(Leg.class);
    when(l2.pathTerminator()).thenReturn(term2);
    when(l2.type()).thenReturn(PathTerm.TF);
    when(l2.toString()).thenReturn(0.0 + ":" + 5.0);

    LegPair legPair = new LegPairImpl(l1, l2);

    Distance distance = Distance.ofNauticalMiles(15.0);
    Speed speed = Speed.of(400.0, Speed.Unit.KNOTS);

    Duration timeStep = speed.timeToTravel(distance);
    double latStep = LatLong.of(0.0, 0.0).project(Course.ofDegrees(0.0), distance).latitude();

    NavigableMap<ConformablePoint, LegPair> pairs = IntStream.range(0, 10)
        .mapToObj(i -> {
          double lat = i < 5 ? latStep * i : latStep * (10 - i);
          double lon = i / 2.0d;
          LatLong ll = LatLong.of(lat, lon);

          ConformablePoint point = conformablePointAt(ll);
          when(point.time()).thenReturn(Instant.EPOCH.plus(Duration.ofMillis(timeStep.toMillis() * i)));

          return Pair.of(point, legPair);
        })
        .collect(Collectors.toMap(Pair::first, Pair::second, (a, b) -> {throw new RuntimeException();}, TreeMap::new));

    LinearSplitErrorEvaluator splitter = LinearSplitErrorEvaluator.newInstance(Distance.ofNauticalMiles(1.0), Speed.of(10.0, Speed.Unit.KNOTS));

    XyDataset dataset = splitter.convertToXYData(pairs);
    XyDataset[] splits = splitter.piecewiseSplits(dataset);

    NavigableMap<Duration, Pair<Speed, Distance>> piecewiseSlopes = splitter.asOffsetToFittedSpeedAverageDistance(splits);
    assertEquals(2, piecewiseSlopes.size());

    Pair<Speed, Distance> first = piecewiseSlopes.firstEntry().getValue();

    assertEquals(Speed.of(-400.0, Speed.Unit.KNOTS).inKnots(), first.first().inKnots(), 0.001);
    assertEquals(Distance.ofNauticalMiles(-37.5).inNauticalMiles(), first.second().inNauticalMiles(), 0.001);

    Pair<Speed, Distance> last = piecewiseSlopes.lastEntry().getValue();

    assertEquals(Speed.of(400.0, Speed.Unit.KNOTS).inKnots(), last.first().inKnots(), 0.001);
    assertEquals(Distance.ofNauticalMiles(-37.5).inNauticalMiles(), last.second().inNauticalMiles(), 0.001);
  }
}
