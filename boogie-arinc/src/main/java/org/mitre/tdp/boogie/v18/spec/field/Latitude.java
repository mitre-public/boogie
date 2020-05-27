package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.util.CoordinateParser;

/**
 * The “Latitude” field contains the latitude of the navigational feature identified in the record.
 */
public class Latitude implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 9;
  }

  @Override
  public String fieldCode() {
    return "5.36";
  }

  @Override
  public Double parse(String fieldString) {
    String reformatted = fieldString.substring(1).concat(fieldString.substring(0, 1));
    return CoordinateParser.convertToDegrees(CoordinateParser.reformatLatCoordinate(reformatted));
  }
}
