package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.TurnDirection;

class TestRadialAngle {

  @Test
  void testRightAngleBetween() {
    assertTrue(new RadialAngle(10.0, 30.0, right()).contains(20.0));
    assertFalse(new RadialAngle(10.0, 30.0, right()).contains(5.0));
  }

  @Test
  void testRightWrappedAngleBetween() {
    assertTrue(new RadialAngle(350.0, 10.0, right()).contains(356.0));
    assertFalse(new RadialAngle(350.0, 10.0, right()).contains(248.0));
  }

  @Test
  void testLeftAngleBetween() {
    assertTrue(new RadialAngle(30.0, 10.0, left()).contains(20.0));
    assertFalse(new RadialAngle(30.0, 10.0, left()).contains(5.0));
  }

  @Test
  void testLeftWrappedAngleBetween() {
    assertTrue(new RadialAngle(10.0, 350.0, left()).contains(356.0));
    assertFalse(new RadialAngle(10.0, 350.0, left()).contains(248.0));
  }

  @Test
  void testEitherAngleBetween() {
    assertTrue(new RadialAngle(121.0, 126.0, either()).contains(123.0));
  }

  @Test
  void testWrappingAt360() {
    assertFalse(new RadialAngle(10., 360., left()).contains(350.));
    assertFalse(new RadialAngle(80., 360., left()).contains(90.));
  }

  private TurnDirection right() {
    TurnDirection td = mock(TurnDirection.class);
    when(td.isRight()).thenReturn(true);
    when(td.isLeft()).thenReturn(false);
    return td;
  }

  private TurnDirection left() {
    TurnDirection td = mock(TurnDirection.class);
    when(td.isRight()).thenReturn(false);
    when(td.isLeft()).thenReturn(true);
    return td;
  }

  private TurnDirection either() {
    TurnDirection td = mock(TurnDirection.class);
    when(td.isRight()).thenReturn(true);
    when(td.isLeft()).thenReturn(true);
    return td;
  }
}
