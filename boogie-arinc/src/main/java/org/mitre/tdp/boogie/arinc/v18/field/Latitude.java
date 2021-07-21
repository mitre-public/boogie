package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.util.CoordinateParser;

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
    return Optional.of(fieldValue).map(String::trim)
        .filter(s -> !s.isEmpty())
        // move the trailing N/S to the back as expected by the coordinate parser
        .map(s -> s.substring(1).concat(s.substring(0, 1)))
        .flatMap(CoordinateParser::reformatLatCoordinate)
        .map(CoordinateParser::convertToDegrees);
  }
}
