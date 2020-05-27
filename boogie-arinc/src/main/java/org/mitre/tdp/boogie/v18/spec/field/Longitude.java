package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.util.CoordinateParser;

/**
 * The Longitude field contains the longitude of the geographic position of the navigational feature identified in the record.
 */
public class Longitude implements FieldSpec<Double> {
  @Override
  public int fieldLength() {
    return 10;
  }

  @Override
  public String fieldCode() {
    return "5.37";
  }

  @Override
  public Double parse(String fieldString) {
    String reformatted = fieldString.substring(1).concat(fieldString.substring(0, 1));
    return CoordinateParser.convertToDegrees(CoordinateParser.reformatLonCoordinate(reformatted));
  }
}
