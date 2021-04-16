package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.util.CoordinateParser;

/**
 * The “Latitude” field contains the latitude of the navigational feature identified in the record.
 */
public class Latitude implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {

  @Override
  public int fieldLength() {
    return 9;
  }

  @Override
  public String fieldCode() {
    return "5.36";
  }

  @Override
  public Double parseValue(String fieldValue) {
    String reformatted = fieldValue.substring(1).concat(fieldValue.substring(0, 1));
    return CoordinateParser.convertToDegrees(CoordinateParser.reformatLatCoordinate(reformatted));
  }
}
