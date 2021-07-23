package org.mitre.tdp.boogie.arinc.utils;

/**
 * Class of common string utilities useful in ARINC string parsing.
 */
public final class ArincStrings {

  // MISSING DECIMAL LOCATIONS
  private static final int TENTHS = 1;
  private static final int HUNDREDTHS = 2;
  private static final int THOUSANDTHS = 3;

  private ArincStrings() {
    throw new IllegalStateException("Cannot instantiate static utility class.");
  }

  /**
   * Common utility for parsing doubles encoded as #### where the last number is the tenths.
   *
   * e.g. 0456 -> 45.6, 1245 -> 124.5, 0005 -> 0.5, 0001234 -> 123.4
   */
  public static Double parseDoubleWithTenths(String s) {
    return parseDoubleMissingDecimal(s, TENTHS);
  }

  public static Float parseFloatWithTenths(String s) {
    return parseDoubleWithTenths(s).floatValue();
  }

  public static Double parseDoubleWithHundredths(String s) {
    return parseDoubleMissingDecimal(s, HUNDREDTHS);
  }

  public static Double parseDoubleWithThousandths(String s) {
    return parseDoubleMissingDecimal(s, THOUSANDTHS);
  }

  public static Double parseDoubleMissingDecimal(String s, Integer missingDecimalUnits) {
    return Double.parseDouble(s) / Math.pow(10, missingDecimalUnits);
  }
}
