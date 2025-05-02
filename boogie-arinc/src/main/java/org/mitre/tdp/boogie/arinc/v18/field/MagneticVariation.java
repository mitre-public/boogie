package org.mitre.tdp.boogie.arinc.v18.field;

import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.util.CoordinateParser.sign;

import java.util.HashSet;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincDecimalParser;

/**
 * The “Magnetic Variation” field specifies the angular difference between True North and Magnetic North at the location defined in the record.
 * “Dynamic Magnetic Variation” is a computer model derived value and takes location and date into consideration. For the “Station Declination”
 * used in some record types, refer to Section 5.66.
 * <br>
 * e.g. E0140, E0000, T0000
 * <br>
 * As in {@link InboundMagneticCourse} this class filters out variations listed as true north. Use {@link org.mitre.tdp.boogie.Declinations} instead for your work.
 */
public final class MagneticVariation implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.39";
  }

  private static final HashSet<String> allowedDirections = newHashSet("E", "W");

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .filter(s -> !s.startsWith("T"))
        .filter(s -> isNumeric(s.substring(1)))
        .filter(s -> allowedDirections.contains(s.substring(0, 1)))
        .flatMap(s -> ArincDecimalParser.INSTANCE.parseDoubleWithTenths(s.substring(1)).map(value -> sign(s.substring(0, 1)) * value));
  }
}
