package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Sequence number of a boundary or special use airspace segment (0 through 99999).
 * <p>DAFIF 8.1 data dictionary field 286.</p>
 */
public final class SegmentNumber extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 286;
  }

  @Override
  public String regex() {
    return "(0|[1-9][0-9]{0,4})";
  }
}
