package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The Longitude field contains the longitude of the geographic position of the navigational feature identified in the record.
 */
public final class Longitude implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 10;
  }

  @Override
  public String fieldCode() {
    return "5.37";
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

    double sign = d.charAt(0) == 'E' ? 1 : -1;

    double degree = charNumber(d, 1) * 100 + charNumber(d, 2) * 10 + charNumber(d, 3);
    double minute = (charNumber(d, 4) * 10 + charNumber(d, 5)) / 60.;
    double second = (charNumber(d, 6) * 10 + charNumber(d, 7)) / 3600.;
    double decimal = charNumber(d, 8) / 36000. + charNumber(d, 9) / 360000.;

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
        && Character.isDigit(d.charAt(8))
        && Character.isDigit(d.charAt(9));
  }

  private int charNumber(String s, int idx) {
    return Character.getNumericValue(s.charAt(idx));
  }
}
