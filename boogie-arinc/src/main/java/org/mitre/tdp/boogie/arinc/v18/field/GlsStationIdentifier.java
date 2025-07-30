package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The GLS Identifier field defines the identification code for retrieval of such a transmitter from a database.
 * This is not a transmitted identifier.
 * <br>
 * The content of this field will be the Airport or Heliport ICAO
 * Location Identifier Code at which the transmitter is installed.
 */
public final class GlsStationIdentifier extends TrimmableString {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.243";
  }
}
