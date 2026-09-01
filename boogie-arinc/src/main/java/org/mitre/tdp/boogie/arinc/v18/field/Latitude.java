package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincCoordinate;

/**
 * The “Latitude” field contains the latitude of the navigational feature identified in the record.
 * <br>
 * Geographic positions whose latitudes must be included in the data are defined during route design, many of them in official
 * government publications. The field is constructed as follows. The first character position contains the alpha character “N” or
 * “S” indicating whether the latitude is north or south of the equator. “N” is entered for latitudes falling on the equator.
 * The following eight numeric characters define the latitude in degrees, minutes, seconds, tenths of seconds and hundredths of
 * seconds. Degree, minute and second symbols and the decimal point are suppressed.
 * <br>
 * e.g. N39513881
 */
public final class Latitude extends ArincCoordinate {

  public Latitude() {
    super(2, 'N', 'S');
  }

  @Override
  public int fieldLength() {
    return 9;
  }

  @Override
  public String fieldCode() {
    return "5.36";
  }

}
