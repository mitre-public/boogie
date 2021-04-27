package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.conformance.alg.assign.score.TfFeatureExtractor.SegmentDistances;

class TestTfFeatureScorer {

  @Test
  void testEndpointModifiedCrossTrackDistanceWithinLegRight() {
    HasPosition p1 = () -> LatLong.of(0.0, 0.0);
    HasPosition p2 = () -> LatLong.of(0.0, 1.0);

    HasPosition proj = p1.projectOut(180.0, 10.0);
    HasPosition pt = () -> LatLong.of(proj.latitude(), 0.5);

    assertEquals(10.0, SegmentDistances.of(p1, p2, pt).otd, 0.0001);
  }

  @Test
  void testEndpointModifiedCrossTrackDistanceWithinLegLeft() {
    HasPosition p1 = () -> LatLong.of(0.0, 0.0);
    HasPosition p2 = () -> LatLong.of(0.0, 1.0);

    HasPosition proj = p1.projectOut(0.0, 10.0);
    HasPosition pt = () -> LatLong.of(proj.latitude(), 0.5);

    SegmentDistances segmentDistances = SegmentDistances.of(p1, p2, pt);

    Function<SegmentDistances, Double> extractor1 = distances -> distances.otd;
    Function<SegmentDistances, Double> extractor2 = distances -> TfFeatureScorer.prePostPenalizer(5, 3).apply(distances.otd, distances.ppd);

    assertEquals(-10.0, extractor1.apply(segmentDistances), 0.0001);
    assertEquals(-10.0, extractor2.apply(segmentDistances), 0.0001);
  }

  @Test
  void testEndpointModifiedCrossTrackDistanceOutsideLegRightStart() {
    HasPosition p1 = () -> LatLong.of(0.0, 0.0);
    HasPosition p2 = () -> LatLong.of(0.0, 1.0);

    HasPosition proj = p1.projectOut(180.0, 10.0);
    HasPosition pt = () -> LatLong.of(proj.latitude(), -0.5);

    SegmentDistances segmentDistances = SegmentDistances.of(p1, p2, pt);

    Function<SegmentDistances, Double> extractor1 = distances -> distances.otd;
    Function<SegmentDistances, Double> extractor2 = distances -> TfFeatureScorer.prePostPenalizer(5, 3).apply(distances.otd, distances.ppd);

    assertEquals(p1.distanceInNmTo(pt), extractor1.apply(segmentDistances), 0.0001);
    assertEquals(5.0 * p1.distanceInNmTo(pt), extractor2.apply(segmentDistances), 0.0001);
  }

  @Test
  void testEndpointModifiedCrossTrackDistanceOutsideLegLeftStart() {
    HasPosition p1 = () -> LatLong.of(0.0, 0.0);
    HasPosition p2 = () -> LatLong.of(0.0, 1.0);

    HasPosition proj = p1.projectOut(0.0, 10.0);
    HasPosition pt = () -> LatLong.of(proj.latitude(), -0.5);

    SegmentDistances segmentDistances = SegmentDistances.of(p1, p2, pt);

    Function<SegmentDistances, Double> extractor1 = distances -> distances.otd;
    Function<SegmentDistances, Double> extractor2 = distances -> TfFeatureScorer.prePostPenalizer(5, 3).apply(distances.otd, distances.ppd);

    assertEquals(-1.0 * p1.distanceInNmTo(pt), extractor1.apply(segmentDistances), 0.0001);
    assertEquals(-5.0 * p1.distanceInNmTo(pt), extractor2.apply(segmentDistances), 0.0001);
  }

  @Test
  void testEndpointModifiedCrossTrackDistanceOutsideLegRightEnd() {
    HasPosition p1 = () -> LatLong.of(0.0, 0.0);
    HasPosition p2 = () -> LatLong.of(0.0, 1.0);

    HasPosition proj = p1.projectOut(180.0, 10.0);
    HasPosition pt = () -> LatLong.of(proj.latitude(), 1.5);

    SegmentDistances segmentDistances = SegmentDistances.of(p1, p2, pt);

    Function<SegmentDistances, Double> extractor1 = distances -> distances.otd;
    Function<SegmentDistances, Double> extractor2 = distances -> TfFeatureScorer.prePostPenalizer(5, 3).apply(distances.otd, distances.ppd);

    assertEquals(p2.distanceInNmTo(pt), extractor1.apply(segmentDistances), 0.0001);
    assertEquals(3.0 * p2.distanceInNmTo(pt), extractor2.apply(segmentDistances), 0.0001);
  }

  @Test
  void testEndpointModifiedCrossTrackDistanceOutsideLegLeftEnd() {
    HasPosition p1 = () -> LatLong.of(0.0, 0.0);
    HasPosition p2 = () -> LatLong.of(0.0, 1.0);

    HasPosition proj = p1.projectOut(0.0, 10.0);
    HasPosition pt = () -> LatLong.of(proj.latitude(), 1.5);

    SegmentDistances segmentDistances = SegmentDistances.of(p1, p2, pt);

    Function<SegmentDistances, Double> extractor1 = distances -> distances.otd;
    Function<SegmentDistances, Double> extractor2 = distances -> TfFeatureScorer.prePostPenalizer(5, 3).apply(distances.otd, distances.ppd);

    assertEquals(-1.0 * p2.distanceInNmTo(pt), extractor1.apply(segmentDistances), 0.0001);
    assertEquals(-3.0 * p2.distanceInNmTo(pt), extractor2.apply(segmentDistances), 0.0001);
  }
}
