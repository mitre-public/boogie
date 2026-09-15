package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Arc definition: B distance and bearing, E end coordinates, or R plotted coordinates.
 * <p>DAFIF 8.1 data dictionary field 91.</p>
 */
public final class Derivation extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 91;
  }

  @Override
  public String regex() {
    return "((B|E|R)?)";
  }
}
