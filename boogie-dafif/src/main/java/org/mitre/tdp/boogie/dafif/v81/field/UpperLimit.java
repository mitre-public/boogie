package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE UPPER VERTICAL LIMIT OF CONTROL FOR AN ATS ROUTE SEGMENT.
 * FOR FLIGHT LEVELS SEE DAFIF SPECIFICATION, CHAPTER 3, TERMS AND DEFINITIONS.
 *
 * EXAMPLE(S):
 * 02000
 * 47900
 * FL100
 * FL98
 * UNLTD
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * 00000 - 99999 (NUMERIC VALUES PADDED WITH LEADING ZEROS)
 * OR
 * FL10 - FL999
 * OR
 * UNLTD
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * ***CRITICAL CHANGE***
 *  DAFIF8.1: "UNLTD" DENOTES UNLIMITED.
 */
public final class UpperLimit extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 330;
  }

  @Override
  public String regex() {
    return "(([0-9]{5}|FL([1-9][0-9]{1,2})|UNLTD)?)";
  }
}