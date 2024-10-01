package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “FIR/UIR Name” field contains the official name of the controlling agency of the FIR/UIR of which this record is an element.
 * The areas without a specific “FIR/UIR” designation will be labeled “NO FIR”.
 */
public class FirUirName extends TrimmableString {
  @Override
  public int fieldLength() {
    return 25;
  }

  @Override
  public String fieldCode() {
    return "5.125";
  }
}
