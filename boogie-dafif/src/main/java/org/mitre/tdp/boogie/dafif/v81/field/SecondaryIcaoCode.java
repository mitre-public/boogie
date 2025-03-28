package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE OFFICIAL SECONDARY INTERNATIONAL CIVIL AVIATION ORGANIZATION (ICAO) LOCATION
 * IDENTIFIER.  IT CAN BE EITHER A TWO '2' OR FOUR '4' CHARACTER CODE.  (DAFIF)
 *
 * EXAMPLE(S):
 * EBMB
 * PHNL
 *
 * 	NOTE:  THE SECONDARY ICAO APPLIES ONLY WHEN THE SECONDARY AIRPORT BIT INDICATES 'Y' (YES).
 *
 * 	FIELD TYPE: A
 *
 * 	ALLOWED VALUES:
 * 	POSITIONS 1-2:  COMBINATIONS OF LETTERS A-Z
 *  POSITIONS 3-4:  COMBINATIONS OF LETTERS A-Z AND/OR SPACES
 * 	OR
 * 	NULL
 *
 * 	SOURCE: HOST NATION PUBLICATION
 */
public final class SecondaryIcaoCode extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 4;
  }

  @Override
  public int fieldCode() {
    return 278;
  }

  @Override
  public String regex() {
    return "(([A-Z]{2}|[A-Z]{4})?)";
  }
}
