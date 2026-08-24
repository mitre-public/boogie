package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.util.CoordinateParser.sign;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincDecimalParser;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

import com.google.common.collect.ImmutableSet;

/**
 * For VHF NAVAIDS, the “Station Declination” field contains the angular difference between true north and the zero degree radial of
 * the NAVAID at the time the NAVAID was last site checked. For ILS localizers, the field contains the angular difference between true
 * north and magnetic north at the localizer antenna site at the time the magnetic bearing of the localizer course was established.
 * <br>
 * Valid modifier values for the declination string are:
 * <br>
 * 1. E - Declination is East of True North
 * 2. W - Declination is West of True North
 * 3. T - Station is oriented to True North in an area in which the local variation is not zero.
 * 4. G - Station is oriented to Grid North
 * <br>
 * This parser elides grid references and non-zero true references. {@code T0000} is retained as an effective declination
 * of zero degrees.
 * <br>
 * e.g. E0072, E0000, T0000, G0000
 */
public final class StationDeclination implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.66";
  }

  private static final ImmutableSet<String> allowedModifiers = ImmutableSet.of("E", "W", "T", "G");

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .map(String::trim)
        .filter(s -> s.length() == fieldLength())
        .filter(s -> allowedModifiers.contains(s.substring(0, 1)))
        .filter(s -> ValidArincNumeric.INSTANCE.test(s.substring(1)))
        .flatMap(StationDeclination::parse);
  }

  private static Optional<Double> parse(String value) {
    return parseTrueReference(value).or(() -> parseEastWestDeclination(value));
  }

  private static Optional<Double> parseTrueReference(String value) {
    return Optional.of(value)
        .filter("T0000"::equals)
        .map(ignored -> 0.0);
  }

  private static Optional<Double> parseEastWestDeclination(String value) {
    return Optional.of(value)
        .filter(s -> s.substring(0, 1).matches("E|W"))
        .flatMap(s -> ArincDecimalParser.INSTANCE.parseDoubleWithTenths(s.substring(1))
            .map(parsed -> sign(s.substring(0, 1)) * parsed));
  }
}
