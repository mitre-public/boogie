package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Special use airspace identifier, including spaces and parenthesized suffixes when present.
 * <p>DAFIF 8.1 data dictionary field 303.</p>
 */
public final class SuasIdentification extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 12;
  }

  @Override
  public int fieldCode() {
    return 303;
  }

  @Override
  public String regex() {
    return "([ 0-9A-Z\\(\\)]{1,12})";
  }
}
