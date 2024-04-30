package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * DENOTES THE CRITERIA UTILIZED TO DESIGN THE TERMINAL PROCEDURE.
 *
 * EXAMPLE(S):
 * TERPS
 * PANS-OPS
 * MIPS
 * NATLXXX(WHERE XXX EQUALS THE ISO 3166 THREE CHARACTER COUNTRY CODE.  I.E.  NATLFRA)
 * UNKNOWN
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * COMBINATIONS OF NUMBERS 0-9, LETTERS A-Z, SPACES AND SPECIAL CHARACTERS
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class ProcedureDesignCriteria extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 15;
  }

  @Override
  public int fieldCode() {
    return 443;
  }

  @Override
  public String regex() {
    return "(([0-9A-Za-z \\-]{1,15})?)";
  }
}