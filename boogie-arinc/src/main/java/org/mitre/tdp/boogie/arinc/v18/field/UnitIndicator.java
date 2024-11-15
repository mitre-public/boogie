package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * Restrictive Airspace lower and upper limits are specified as above Mean Sea Level (MSL) or Above
 * Ground Level (AGL). This field permits the unit of measurement to be indicated.
 *
 * The units of lower and upper limits are derived from official government source. The alpha character
 * M will indicate MSL and the alpha character A will indicate AGL.
 */

public final class UnitIndicator extends TrimmableString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.133";
  }
}
