package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Meteorological conditions or flight rules in which the airspace can be used.
 * <p>DAFIF 8.1 data dictionary field 344.</p>
 */
public final class SuasWeather extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 8;
  }

  @Override
  public int fieldCode() {
    return 344;
  }

  @Override
  public String regex() {
    return "((VFR|IFR|VMC|IMC|VFR\\-IFR|VMC\\-IMC|BY NOTAM)?)";
  }
}
