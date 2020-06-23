package org.mitre.tdp.boogie.conformance.scorers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.conformance.scorers.Angles.between;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.TurnDirection;

public class TestAngles {

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

  @Test
  public void testRightAngleBetween() {
    assertTrue(between(20.0, 10.0, 30.0, right()));
    assertFalse(between(5.0, 10.0, 30.0, right()));
  }

  @Test
  public void testRightWrappedAngleBetween() {
    assertTrue(between(356.0, 350.0, 10.0, right()));
    assertFalse(between(248.0, 350.0, 10.0, right()));
  }

  @Test
  public void testLeftAngleBetween() {
    assertTrue(between(20.0, 30.0, 10.0, left()));
    assertFalse(between(5.0, 30.0, 10.0, left()));
  }

  @Test
  public void testLeftWrappedAngleBetween() {
    assertTrue(between(356.0, 10.0, 350.0, left()));
    assertFalse(between(248.0, 10.0, 350.0, left()));
  }

  @Test
  public void testEitherAngleBetween() {
    assertTrue(between(123.0, 121.0, 126.0, either()));
  }
}
