package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.util.CoordinateParser;

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
        // move the trailing N/S to the back as expected by the coordinate parser
        .map(s -> s.substring(1).concat(s.substring(0, 1)))
        .flatMap(CoordinateParser::reformatLonCoordinate)
        .map(CoordinateParser::convertToDegrees);
  }
}
