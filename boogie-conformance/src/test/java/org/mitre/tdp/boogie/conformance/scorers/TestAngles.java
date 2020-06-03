package org.mitre.tdp.boogie.conformance.scorers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.conformance.scorers.Angles.between;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.TurnDirection;

public class TestAngles {

  @Test
  public void testRightAngleBetween() {
    assertTrue(between(20.0, 10.0, 30.0, TurnDirection.RIGHT));
    assertFalse(between(5.0, 10.0, 30.0, TurnDirection.RIGHT));
  }

  @Test
  public void testRightWrappedAngleBetween() {
    assertTrue(between(356.0, 350.0, 10.0, TurnDirection.RIGHT));
    assertFalse(between(248.0, 350.0, 10.0, TurnDirection.RIGHT));
  }

  @Test
  public void testLeftAngleBetween() {
    assertTrue(between(20.0, 30.0, 10.0, TurnDirection.LEFT));
    assertFalse(between(5.0, 30.0, 10.0, TurnDirection.LEFT));
  }

  @Test
  public void testLeftWrappedAngleBetween() {
    assertTrue(between(356.0, 10.0, 350.0, TurnDirection.LEFT));
    assertFalse(between(248.0, 10.0, 350.0, TurnDirection.LEFT));
  }

  @Test
  public void testEitherAngleBetween() {
    assertTrue(between(123.0, 121.0, 126.0, TurnDirection.EITHER));
  }
}
