package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * INDICATES THE RADIUS OF AN ARC FROM A CENTER POINT IN NAUTICAL MILES TO THE THOUSANDTHS OF A NAUTICAL MILE.
 *
 * EXAMPLE(S):
 * 2.4
 * 8.86
 * 4
 * 3.426
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 0.001 - 999.999 (LEADING ZEROS ARE SUPPRESSED UNLESS VALUE IS LESS THAN 1.0 AND TRAILING ZEROS ARE SUPPRESSED)
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * POPULATED FOR 'RF' LEGS IN THE TRM_SEG FILE.
 */
public final class ArcRadius extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 7;
  }

  @Override
  public int fieldCode() {
    return 439;
  }

  @Override
  public String regex() {
    return "(([0-9]{1,3}\\.(00[1-9]|0[1-9]{1,2}|[0-9]{2}[1-9]|[1-9]{0,3})|0|[1-9][0-9]{0,2})?)";
  }
}