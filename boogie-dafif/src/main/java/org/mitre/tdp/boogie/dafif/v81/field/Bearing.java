package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Bearing from the center coordinates or referenced navaid, in degrees and tenths (0.1 through 360.0).
 * <p>DAFIF 8.1 data dictionary field 47.</p>
 */
public final class Bearing extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 47;
  }

  @Override
  public String regex() {
    return "((0\\.[1-9]|360\\.0|(([1-9][0-9]{0,1}|[1-2][0-9]{1,2}|3[0-5][0-9])\\.[0-9]))?)";
  }
}
