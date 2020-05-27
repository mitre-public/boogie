package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The content of this field is derived from official government sources. It will be the four character
 * ICAO Location Identifier of the airport or heliport when such is published. It will be the three or
 * four character Domestic Identifier when published and no ICAO Location Identifier is available for the
 * airport or heliport
 */
public class AirportHeliportIdentifier implements FreeFormString {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.6";
  }
}
