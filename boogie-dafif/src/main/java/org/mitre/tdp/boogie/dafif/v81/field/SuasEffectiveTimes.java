package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Days, hours, and dates when the special use airspace is in effect.
 * <p>DAFIF 8.1 data dictionary field 302.</p>
 */
public final class SuasEffectiveTimes extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 38;
  }

  @Override
  public int fieldCode() {
    return 302;
  }

  @Override
  public String regex() {
    return "(([\\WA-Z0-9]{1,38})?)";
  }
}
