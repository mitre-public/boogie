package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A NUMBER OR LETTER CODE ASSIGNED TO THE SPECIFIC TYPE OF NAVAID.
 *
 *  1 - VOR
 *  2 - VORTAC
 *  3 - TACAN
 *  4 - VOR-DME
 *  5 - NDB
 *  7 - NDB-DME
 *  9 - DME
 *  D - (ILS) DME
 *  P - MLS DME
 *  S - MLS
 *  Z - (ILS) LOCALIZER
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * 1, 2, 3, 4, 5, 7, 9, D, P, S, Z
 * OR
 * NULL
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * NAV1_TYPE:  POPULATED WHEN PATH TERMINATOR OR PARTICULAR PHASE OF THE PROCEDURE REQUIRES IT.  SEE REQUIRED FIELD MATRIX IN APPENDIX D.
 * NAV2_TYPE:  NOT MAINTAINED IN DAFIF 8.1.
 *
 * MLS COMPONENTS NO LONGER POPULATED BY NGA PER DECISION OF DWG.
 */
public final class Navaid12Type extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 207;
  }

  @Override
  public String regex() {
    return "((1|2|3|4|5|7|9|D|P|S|Z)?)";
  }
}