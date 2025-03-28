package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * “The SID/STAR Route Identifier” field contains the name of the SID or STAR, using the basic indicator, validity indicator and
 * route indicator abbreviated to six characters with the naming rules in Chapter 7 of this document.
 * <br>
 * SID/STAR route identifier codes should be derived from official government publications describing the terminal procedures
 * structure.
 */
public final class SidStarIdentifier extends TrimmableString {

  @Override
  public int fieldLength() {
    return 6;
  }

  @Override
  public String fieldCode() {
    return "5.9";
  }
}
