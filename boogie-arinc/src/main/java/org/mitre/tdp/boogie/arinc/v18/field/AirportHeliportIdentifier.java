package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The content of this field is derived from official government sources. It will be the four character ICAO Location Identifier
 * of the airport or heliport when such is published. It will be the three or four character Domestic Identifier when published
 * and no ICAO Location Identifier is available for the airport or heliport.
 * <br>
 * When used on Airport or Heliport Flight Planning Continuation Records, it will be the Airport or Heliport Identifier owning
 * the terminal controlled airspace referenced in that record.
 */
public final class AirportHeliportIdentifier extends TrimmableString {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.6";
  }
}
