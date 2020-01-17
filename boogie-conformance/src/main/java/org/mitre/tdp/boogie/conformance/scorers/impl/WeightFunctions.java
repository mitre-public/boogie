package org.mitre.tdp.boogie.conformance.scorers.impl;

import java.util.function.Function;

import static org.apache.commons.math3.util.FastMath.exp;
import static org.apache.commons.math3.util.FastMath.log;

public class WeightFunctions {

  /**
   * Builds a logistic function with the provided inflection point and a ninety percent point at
   * the provided value.
   */
  public static Function<Double, Double> simpleLogistic(double midPoint, double fivePercentPoint) {
    double k = -1.0 * (log((1.0 / 0.05) - 1) / (fivePercentPoint - midPoint));
    return d -> 1.0 / (1.0 + exp(-k * (d - midPoint)));
  }
}
