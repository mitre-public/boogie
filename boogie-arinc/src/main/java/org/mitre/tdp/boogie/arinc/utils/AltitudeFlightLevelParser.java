package org.mitre.tdp.boogie.arinc.utils;

import static org.apache.commons.lang3.StringUtils.isNumeric;

import java.util.Optional;
import java.util.function.Function;

/**
 * ARINC contains a variety of fields which can be interpreted as either a Flight Level (FL prefixed) or as a number of feet
 * (all numeric). This class disambiguates (and parses) the two returning a parsed double in feet.
 * <br>
 * This class may return {@link Optional#empty()} if the provided field content doesn't meet the required expectations to be
 * parsed.
 * <br>
 * Valid sets of Altitude or Flight Levels include:
 * <br>
 * 05000, FL050, 18000, FL180 00600, -0012, 29000, FL290
 * <br>
 * So there can be negatives for the all numeric values - as such we use the {@link ValidArincNumeric} to test non-FL cases.
 */
public final class AltitudeFlightLevelParser implements Function<String, Optional<Double>> {

  public static final AltitudeFlightLevelParser INSTANCE = new AltitudeFlightLevelParser();

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .filter(s -> s.startsWith("FL") ? isNumeric(s.substring(2)) : ValidArincNumeric.INSTANCE.test(s))
        .map(s -> s.startsWith("FL") ? Double.parseDouble(s.substring(2)) * 100.0 : Double.parseDouble(s));
  }
}
