package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE IDENTIFICATION (CALL SIGN) ASSIGNED BY THE CONTROLLING AUTHORITY TO THE NAVAID USED AS  THE REFERENCED NAVAID FOR THE SPECIFIED WAYPOINT.  (DAFIF)
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * COMBINATIONS OF NUMBERS 0-9 AND LETTERS A-Z
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * NAV1_IDENT:  POPULATED WHEN PATH TERMINATOR OR PARTICULAR PHASE OF THE PROCEDURE REQUIRES IT.  SEE REQUIRED FIELD MATRIX IN APPENDIX D.
 * NAV2_IDENT:  NOT MAINTAINED IN DAFIF 8.1.
 */
public final class Navaid12Identifier extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 4;
  }

  @Override
  public int fieldCode() {
    return 205;
  }

  @Override
  public String regex() {
    return "(([A-Z0-9]{1,4})?)";
  }
}