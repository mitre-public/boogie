package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Latitude” field contains the latitude of the navigational feature identified in the record.
 * <br>
 * Geographic positions whose latitudes must be included in the data base are defined during route design, many of them in official
 * government publications. The field is constructed as follows. The first character position contains the alpha character “N” or
 * “S” indicating whether the latitude is north or south of the equator. “N” is entered for latitudes falling on the equator.
 * The following eight numeric characters define the latitude in degrees, minutes, seconds, tenths of seconds and hundredths of
 * seconds. Degree, minute and second symbols and the decimal point are suppressed.
 * <br>
 * e.g. N39513881
 */
public final class Latitude implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 9;
  }

  @Override
  public String fieldCode() {
    return "5.36";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .filter(this::isNumeric)
        // move the trailing N/S to the back as expected by the coordinate parser
        .map(this::convert);
  }

  private double convert(String d) {

    double sign = d.charAt(0) == 'N' ? 1 : -1;

    double degree = charNumber(d, 1) * 10 + charNumber(d, 2);
    double minute = (charNumber(d, 3) * 10 + charNumber(d, 4)) / 60.;
    double second = (charNumber(d, 5) * 10 + charNumber(d, 6)) / 3600.;
    double decimal = charNumber(d, 7) / 36000. + charNumber(d, 8) / 360000.;

    return sign * (degree + minute + second + decimal);
  }

  // elide the first non-numeric character
  private boolean isNumeric(String d) {
    return Character.isDigit(d.charAt(1))
        && Character.isDigit(d.charAt(2))
        && Character.isDigit(d.charAt(3))
        && Character.isDigit(d.charAt(4))
        && Character.isDigit(d.charAt(5))
        && Character.isDigit(d.charAt(6))
        && Character.isDigit(d.charAt(7))
        && Character.isDigit(d.charAt(8));
  }

  private int charNumber(String s, int idx) {
    return Character.getNumericValue(s.charAt(idx));
  }
}
