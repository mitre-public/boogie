package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

public class TestTfScorer {

  @Test
  public void testTfScorerReturnsEmptyWithNoPreviousLeg() {
    Fix pathTerminator = mock(Fix.class);
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    when(leg.type()).thenReturn(PathTerm.TF);

    FlyableLeg legs = new FlyableLeg(null, leg, null);

    ConformablePoint point = mock(ConformablePoint.class);
    assertFalse(legs.onLegScorer().score(point, legs).isPresent());
  }

  @Test
  public void testTfScorerReturnsEmptyWithNoPreviousFix() {
    Fix pathTerminator = mock(Fix.class);
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    when(leg.type()).thenReturn(PathTerm.TF);

    Leg prev = mock(Leg.class);

    FlyableLeg legs = new FlyableLeg(prev, leg, null);

    ConformablePoint point = mock(ConformablePoint.class);
    assertFalse(legs.onLegScorer().score(point, legs).isPresent());
  }

  @Test
  public void testTfScorerReturnsEmptyWithNoPreviousFixLocation() {
    Fix pathTerminator = mock(Fix.class);
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    when(leg.type()).thenReturn(PathTerm.TF);

    Leg prev = mock(Leg.class);
    Fix fix = mock(Fix.class);
    when(prev.pathTerminator()).thenReturn(fix);

    FlyableLeg legs = new FlyableLeg(prev, leg, null);

    ConformablePoint point = mock(ConformablePoint.class);
    assertFalse(legs.onLegScorer().score(point, legs).isPresent());
  }

  @Test
  public void testEndpointModifiedCrossTrackDistanceWithinLegRight() {
    HasPosition p1 = () -> LatLong.of(0.0, 0.0);
    HasPosition p2 = () -> LatLong.of(0.0, 1.0);

    HasPosition proj = p1.projectOut(180.0, 10.0);
    HasPosition pt = () -> LatLong.of(proj.latitude(), 0.5);

    assertEquals(10.0, TfScorer.endpointModifiedCrossTrackDistance(p1, p2, pt), 0.0001);
  }

  @Test
  public void testEndpointModifiedCrossTrackDistanceWithinLegLeft() {
    HasPosition p1 = () -> LatLong.of(0.0, 0.0);
    HasPosition p2 = () -> LatLong.of(0.0, 1.0);

    HasPosition proj = p1.projectOut(0.0, 10.0);
    HasPosition pt = () -> LatLong.of(proj.latitude(), 0.5);

    assertEquals(-10.0, TfScorer.endpointModifiedCrossTrackDistance(p1, p2, pt), 0.0001);
  }

  @Test
  public void testEndpointModifiedCrossTrackDistanceOutsideLegRightStart() {
    HasPosition p1 = () -> LatLong.of(0.0, 0.0);
    HasPosition p2 = () -> LatLong.of(0.0, 1.0);

    HasPosition proj = p1.projectOut(180.0, 10.0);
    HasPosition pt = () -> LatLong.of(proj.latitude(), -0.5);

    assertEquals(p1.distanceInNmTo(pt), TfScorer.endpointModifiedCrossTrackDistance(p1, p2, pt), 0.0001);
  }

  @Test
  public void testEndpointModifiedCrossTrackDistanceOutsideLegLeftStart() {
    HasPosition p1 = () -> LatLong.of(0.0, 0.0);
    HasPosition p2 = () -> LatLong.of(0.0, 1.0);

    HasPosition proj = p1.projectOut(0.0, 10.0);
    HasPosition pt = () -> LatLong.of(proj.latitude(), -0.5);

    assertEquals(-1.0 * p1.distanceInNmTo(pt), TfScorer.endpointModifiedCrossTrackDistance(p1, p2, pt), 0.0001);
  }

  @Test
  public void testEndpointModifiedCrossTrackDistanceOutsideLegRightEnd() {
    HasPosition p1 = () -> LatLong.of(0.0, 0.0);
    HasPosition p2 = () -> LatLong.of(0.0, 1.0);

    HasPosition proj = p1.projectOut(180.0, 10.0);
    HasPosition pt = () -> LatLong.of(proj.latitude(), 1.5);

    assertEquals(p2.distanceInNmTo(pt), TfScorer.endpointModifiedCrossTrackDistance(p1, p2, pt), 0.0001);
  }

  @Test
  public void testEndpointModifiedCrossTrackDistanceOutsideLegLeftEnd() {
    HasPosition p1 = () -> LatLong.of(0.0, 0.0);
    HasPosition p2 = () -> LatLong.of(0.0, 1.0);

    HasPosition proj = p1.projectOut(0.0, 10.0);
    HasPosition pt = () -> LatLong.of(proj.latitude(), 1.5);

    assertEquals(-1.0 * p2.distanceInNmTo(pt), TfScorer.endpointModifiedCrossTrackDistance(p1, p2, pt), 0.0001);
  }
}
