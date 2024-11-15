package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “ATA/IATA” field contains the Airport/Heliport ATA/IATA designator code to which the data contained in the record relates.
 */
public final class IataDesignator extends TrimmableString {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.107";
  }
}
