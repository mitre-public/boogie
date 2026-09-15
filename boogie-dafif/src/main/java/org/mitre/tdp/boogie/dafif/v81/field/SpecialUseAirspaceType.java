package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Special use airspace type: A alert, D danger, M military operations, P prohibited, R restricted, T temporary reserved, W warning.
 * <p>DAFIF 8.1 data dictionary field 293.</p>
 */
public final class SpecialUseAirspaceType extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 293;
  }

  @Override
  public String regex() {
    return "(A|D|M|P|R|T|W)";
  }
}
