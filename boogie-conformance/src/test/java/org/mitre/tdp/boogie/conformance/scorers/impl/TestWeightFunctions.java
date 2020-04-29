package org.mitre.tdp.boogie.conformance.scorers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

class TestWeightFunctions {

  @Test
  void testLogisticScoringLoHi() {
    Function<Double, Double> fn = WeightFunctions.simpleLogistic(1.0, 0.1);

    assertEquals(0.05, fn.apply(0.1), 0.01, "The function score at the 5% point should be 0.05");
    assertEquals(0.5, fn.apply(1.0), 0.01, "The function score at the midpoint should be 0.5");
    assertEquals(0.95, fn.apply(1.9), 0.01, "The function score at the 95% point should be 0.95");
  }

  @Test
  public void testLogisticScoringHiLo() {
    Function<Double, Double> fn = WeightFunctions.simpleLogistic(1.0, 2.0);

    assertEquals(0.05, fn.apply(2.0), 0.01);
    assertEquals(0.5, fn.apply(1.0), 0.01);
    assertEquals(0.95, fn.apply(0.0), 0.01);
  }
}
