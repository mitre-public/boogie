package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A LOCATION IDENTIFIER ASSIGNED BY THE FEDERAL AVIATION ADMINISTRATION (FAA) AS DESIGNATED
 * IN THE FAA LOCATION IDENTIFIERS HANDBOOK 7350.* OR HOST COUNTRY PUBLICATION.  (DAFIF)
 *
 * 	NOTE:  THE SECONDARY FAA/HOST/IDENT APPLIES ONLY WHEN THE SECONDARY AIRPORT BIT INDICATES 	'Y' (YES).
 *
 * EXAMPLE(S):
 * UB18
 * ECA
 * KPAO
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 *  THREE POSITIONS:  COMBINATIONS OF NUMBERS 0-9, LETTERS A-Z
 * 	OR
 * 	FOUR POSITIONS:  COMBINATIONS OF NUMBERS 0-9, LETTERS A-Z
 * 	OR
 * 	NULL"
 *
 * 	SOURCE: HOST NATION PUBLICATION
 */
public final class SecondaryFaaHost extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 4;
  }

  @Override
  public int fieldCode() {
    return 277;
  }

  @Override
  public String regex() {
    return "(([0-9A-Z]{3,4})?)";
  }
}
