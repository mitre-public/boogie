package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static org.apache.commons.math3.util.FastMath.exp;
import static org.apache.commons.math3.util.FastMath.log;

import java.util.function.UnaryOperator;

public final class WeightFunctions {

  private WeightFunctions() {
    throw new IllegalStateException("Utility Class");
  }

  /**
   * Builds a logistic function with the provided inflection point and a ninety percent point at the provided value.
   */
  public static UnaryOperator<Double> simpleLogistic(double midPoint, double fivePercentPoint) {
    double k = -1.0 * (log((1.0 / 0.05) - 1) / (fivePercentPoint - midPoint));
    return d -> 1.0 / (1.0 + exp(-k * (d - midPoint)));
  }

  /**
   * Generates a new linear function starting at (0, 1) and continuing through x1, y1 and then = y1 after.
   */
  public static UnaryOperator<Double> simpleLinear(double x1, double y1) {
    return linearBetween(0., 1., x1, y1);
  }

  /**
   * Generates a new function which is linear between the two points and when {@code d < x0 = y0 & d > x1 = y1}.
   */
  public static UnaryOperator<Double> linearBetween(double x0, double y0, double x1, double y1) {
    return d -> {
      if (d > x0 && d < x1) {
        return ((y1 - y0) / (x1 - x0)) * d;
      } else {
        return d < x0 ? y0 : y1;
      }
    };
  }
}
