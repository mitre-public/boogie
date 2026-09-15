package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Airspace floor in feet AMSL or AGL, flight level, GND, SURFACE, BY NOTAM, or U (unknown).
 * <p>DAFIF 8.1 data dictionary field 423.</p>
 */
public final class LowerAltitude extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 9;
  }

  @Override
  public int fieldCode() {
    return 423;
  }

  @Override
  public String regex() {
    return "((FL)[1-9][0-9]|(FL)[1-9][0-9][0-9]|(SURFACE)|(GND)|(BY NOTAM)|[0-9]{5}(AMSL)|[0-9]{5}(AGL)|(U))";
  }
}
