package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The Multiple Code field will be used to indicate Restrictive Airspace Areas or MSA Centers having the
 * same designator but subdivided or differently divided by lateral and/or vertical detail.
 *
 * This field will be used when official government publications for Restrictive Airspace divides an area with
 * the same designator into different areas of Activation, altitude or other defining characteristics. For
 * MSA Centers, this provides different sectorization and altitudes for the MSA published with the same
 * center. The field will contain an alpha/numeric character uniquely identifying each area or MSA. The
 * first record affected could contain the character A and multiple primary records could contain the
 * character B, C, D, 0, 1 etc., as required.
 */

public final class MultipleCode extends TrimmableString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.130";
  }
}
