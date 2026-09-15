package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Most restrictive applicable airspace class, A through G.
 * <p>DAFIF 8.1 data dictionary field 62.</p>
 */
public final class AirspaceClass extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 62;
  }

  @Override
  public String regex() {
    return "(([A-G])?)";
  }
}
