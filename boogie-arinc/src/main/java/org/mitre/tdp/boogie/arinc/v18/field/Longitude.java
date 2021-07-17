package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.util.CoordinateParser;

/**
 * The Longitude field contains the longitude of the geographic position of the navigational feature identified in the record.
 */
public final class Longitude implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {
  @Override
  public int fieldLength() {
    return 10;
  }

  @Override
  public String fieldCode() {
    return "5.37";
  }

  @Override
  public Double parseValue(String fieldValue) {
    String reformatted = fieldValue.substring(1).concat(fieldValue.substring(0, 1));
    return CoordinateParser.convertToDegrees(CoordinateParser.reformatLonCoordinate(reformatted));
  }
}
