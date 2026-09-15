package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Optional subdivision of a special use airspace; a blank sector is valid.
 * <p>DAFIF 8.1 data dictionary field 281.</p>
 */
public final class Sector extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 2;
  }

  @Override
  public int fieldCode() {
    return 281;
  }

  @Override
  public String regex() {
    return "(([A-Z0-9]{1,2})?)";
  }
}
