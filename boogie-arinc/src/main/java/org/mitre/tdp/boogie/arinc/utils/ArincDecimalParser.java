package org.mitre.tdp.boogie.arinc.utils;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * All decimal values in the ARINC specification are listed with the decimal point suppressed and with some number of significant
 * figures listed. This class serves as a single point of entry for parsing these values.
 * <br>
 * This class returns an Optional as it checks internally whether the given string value is a {@link ValidArincNumeric}.
 */
public final class ArincDecimalParser implements BiFunction<String, Integer, Optional<Double>> {

  public static final ArincDecimalParser INSTANCE = new ArincDecimalParser();

  @Override
  public Optional<Double> apply(String value, Integer missingDecimalUnits) {
    requireNonNull(value);
    requireNonNull(missingDecimalUnits);

    return ValidArincNumeric.INSTANCE.test(value)
        ? Optional.of(Double.parseDouble(value) / Math.pow(10, missingDecimalUnits))
        : Optional.empty();
  }

  // the most common locations of the missing decimal point - these methods are supplied for utility purposes
  private static final int TENTHS = 1;
  private static final int HUNDREDTHS = 2;
  private static final int THOUSANDTHS = 3;

  /**
   * Common utility for parsing doubles encoded as #### where the last number is the tenths.
   * <br>
   * e.g. {@code 0456 -> 45.6}, {@code 1245 -> 124.5}, {@code 0005 -> 0.5}, {@code 0001234 -> 123.4}
   */
  public Optional<Double> parseDoubleWithTenths(String value) {
    return apply(value, TENTHS);
  }

  public Optional<Double> parseDoubleWithHundredths(String value) {
    return apply(value, HUNDREDTHS);
  }

  public Optional<Double> parseDoubleWithThousandths(String value) {
    return apply(value, THOUSANDTHS);
  }
}
