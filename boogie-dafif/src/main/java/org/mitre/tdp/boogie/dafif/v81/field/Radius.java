package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Radius in nautical miles. Radius 1 is the outer radius; radius 2 is the inner radius of a concentric circle.
 * <p>DAFIF 8.1 data dictionary field 256.</p>
 */
public final class Radius extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 6;
  }

  @Override
  public int fieldCode() {
    return 256;
  }

  @Override
  public String regex() {
    return "(((0|[1-9][0-9]{0,2})\\.([0-9]{2})|(^0\\.00))?)";
  }
}
