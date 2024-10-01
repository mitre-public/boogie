package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “FIR/UIR Identifier” field identifies the Flight Information Region and Upper Information Region of airspace
 * with defined dimensions within which Flight Information Service and Alerting Service are provided.
 * The Identifier is for the controlling Area Control Center or Flight Information Center.
 */
public class FirUirIdentifier extends TrimmableString {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.116";
  }
}
