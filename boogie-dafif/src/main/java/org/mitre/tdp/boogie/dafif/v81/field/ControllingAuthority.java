package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Controlling authority or agency for the airspace.
 * <p>DAFIF 8.1 data dictionary field 78.</p>
 */
public final class ControllingAuthority extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 38;
  }

  @Override
  public int fieldCode() {
    return 78;
  }

  @Override
  public String regex() {
    return "(([A-Z0-9\\W]{1,38})?)";
  }
}
