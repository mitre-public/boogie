package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Airspace ceiling in feet AMSL or AGL, flight level, UNLTD, BY NOTAM, or U (unknown).
 * <p>DAFIF 8.1 data dictionary field 329.</p>
 */
public final class UpperAltitude extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 10;
  }

  @Override
  public int fieldCode() {
    return 329;
  }

  @Override
  public String regex() {
    return "(FL[1-9][0-9]{1,3}|UNLTD|BY NOTAM|[0-9]{6}(AMSL|AGL)|U)";
  }
}
