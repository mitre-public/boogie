package org.mitre.tdp.boogie.util;

public final class Heading {
  /**
   * Normalizes a heading to be within the 0-359 degree range.
   * @param heading The input heading, which can be negative or over 360.
   * @return The normalized heading, between 0 and 359.
   */
  public static double normalizeHeading(double heading) {
    return (heading % 360 + 360) % 360;
  }

  /**
   * Calculates the reciprocal of a given true heading.
   *
   * @param trueHeading The current true heading in degrees (0-359).
   * @return The reciprocal true heading in degrees (0-359).
   */
  public static double reciprocalHeading(double trueHeading) {
    double reciprocal = trueHeading + 180;
    return normalizeHeading(reciprocal);
  }
}
