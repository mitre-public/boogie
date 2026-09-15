package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Unique boundary identifier: two controlling-authority ICAO letters followed by five digits.
 * <p>DAFIF 8.1 data dictionary field 50.</p>
 */
public final class BoundaryIdentification extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 7;
  }

  @Override
  public int fieldCode() {
    return 50;
  }

  @Override
  public String regex() {
    return "([A-Z]{2}[0-9]{5})";
  }
}
