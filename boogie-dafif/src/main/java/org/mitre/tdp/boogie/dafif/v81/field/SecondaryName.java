package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE OFFICIAL SECONDARY NAME DESIGNATED BY THE OPERATING AGENCY, THE FAA OR IN OFFICIAL
 * PUBLICATIONS OF THE COUNTRY THE AIRPORT IS LOCATED IN.  ALLOWABLE ABBREVIATIONS AND
 * 	CONTRACTIONS ARE IN THE FAA CONTRACTION HANDBOOK, HOST NATION AIPS, AND DMA 8310.2.
 * 	(DAFIF 	AND AAFIF INPUT INSTRUCTIONS)
 *
 * 	NOTE:  THE SECONDARY AIRPORT NAME APPLIES ONLY WHEN THE SECONDARY AIRPORT INDICATES 'Y'  	(YES).
 *
 * 	NOTE:  THIS FIELD DOES NOT APPLY TO THE ALTERNATE NAME.
 *
 * EXAMPLE(S):
 * JOINT BASE PEARL HARBOR HICKAM
 * MADRID CUATRO VIENTOS
 *
 * FIELD TYPE: A/N
 *
 *
 * ALLOWED VALUES:
 *  FIRST POSITION:  COMBINATIONS OF LETTERS A-Z
 *  SECOND POSITION:  COMBINATIONS OF NUMBERS 0-9, LETTERS A-Z, AND SPACES
 * 	OR
 * 	NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class SecondaryName extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 38;
  }

  @Override
  public int fieldCode() {
    return 279;
  }

  @Override
  public String regex() {
    return "(([A-Z][A-Z0-9 ]{0,37})?)";
  }
}
