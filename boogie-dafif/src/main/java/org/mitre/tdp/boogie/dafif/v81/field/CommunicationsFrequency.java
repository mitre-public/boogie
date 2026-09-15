package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Communications frequency with explicit K (kilohertz) or M (megahertz) units and no implied decimal.
 * <p>DAFIF 8.1 data dictionary field 75.</p>
 */
public final class CommunicationsFrequency extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 9;
  }

  @Override
  public int fieldCode() {
    return 75;
  }

  @Override
  public String regex() {
    return "((?=.{9}$)[0-9]+(?:\\.[0-9]+)? +[KM])?";
  }
}
