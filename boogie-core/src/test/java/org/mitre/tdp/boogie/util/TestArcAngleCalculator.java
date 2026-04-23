package org.mitre.tdp.boogie.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.TurnDirection;

class TestArcAngleCalculator {

  @Test
  void testRightTurn() {
    double result = ArcAngleCalculator.from(10.0, 20.0, TurnDirection.right());
    assertEquals(10.0, result, 0.001);
  }

  @Test
  void testLeftTurn() {
    double result = ArcAngleCalculator.from(20.0, 10.0, TurnDirection.left());
    assertEquals(10.0, result, 0.001);
  }

  @Test
  void testEitherDirection() {
    double result = ArcAngleCalculator.from(350.0, 10.0, TurnDirection.either());
    assertEquals(20.0, result, 0.001);
  }

  @Test
  void testEitherDirectionShortestPath() {
    double result = ArcAngleCalculator.from(10.0, 350.0, TurnDirection.either());
    assertEquals(20.0, result, 0.001);
  }

  @Test
  void testRightTurnWrapAround() {
    double result = ArcAngleCalculator.from(350.0, 20.0, TurnDirection.right());
    assertEquals(30.0, result, 0.001);
  }

  @Test
  void testLeftTurnWrapAround() {
    double result = ArcAngleCalculator.from(20.0, 350.0, TurnDirection.left());
    assertEquals(30.0, result, 0.001);
  }

  @Test
  void testRightTurnZeroAngle() {
    double result = ArcAngleCalculator.from(0.0, 0.0, TurnDirection.right());
    assertEquals(0.0, result, 0.001);
  }

  @Test
  void testLeftTurnZeroAngle() {
    double result = ArcAngleCalculator.from(0.0, 0.0, TurnDirection.left());
    assertEquals(0.0, result, 0.001);
  }

  @Test
  void testRightTurnFullCircle() {
    double result = ArcAngleCalculator.from(0.0, 360.0, TurnDirection.right());
    assertEquals(0.0, result, 0.001);
  }

  @Test
  void testLeftTurnFullCircle() {
    double result = ArcAngleCalculator.from(360.0, 0.0, TurnDirection.left());
    assertEquals(0.0, result, 0.001);
  }
}
