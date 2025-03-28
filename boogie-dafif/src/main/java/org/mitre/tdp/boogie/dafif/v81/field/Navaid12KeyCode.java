package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * SYSTEM GENERATED KEY TO DISTINGUISH BETWEEN THE SAME TYPE OF NAVAIDS WITH THE SAME IDENT  AND THE SAME COUNTRY CODE.  (DAFIF)
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 1-99
 *  OR
 *  NULL
 *
 * SOURCE: SYSTEM GENERATED
 *
 * INTENDED USE:
 * NAV1_KEY_CD:  POPULATED WHEN PATH TERMINATOR OR PARTICULAR PHASE OF THE PROCEDURE REQUIRES IT.  SEE REQUIRED FIELD MATRIX IN APPENDIX D.
 * NAV2_KEY_CD:  NOT MAINTAINED IN DAFIF 8.1.
 */
public final class Navaid12KeyCode extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 2;
  }

  @Override
  public int fieldCode() {
    return 206;
  }

  @Override
  public String regex() {
    return "(([1-9]|[1-9][0-9])?)";
  }
}