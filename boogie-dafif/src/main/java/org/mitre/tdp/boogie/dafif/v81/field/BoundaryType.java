package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * DAFIF boundary type, from 01 (advisory area) through 15 (functional airspace block).
 * <p>DAFIF 8.1 data dictionary field 51.</p>
 */
public final class BoundaryType extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 2;
  }

  @Override
  public int fieldCode() {
    return 51;
  }

  @Override
  public String regex() {
    return "(0[1-9]|1[0-5])";
  }
}
