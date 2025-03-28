package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A DATE IN THE FORMAT DDMMMYY THAT REPRESENTS THE DATE OF THE MOST RECENT PROCEDURAL CHANGE REFLECTED ON THE SOURCE DOCUMENT FOR THE ASSOCIATED PROCEDURE.
 *
 * EXAMPLE(S):
 * 04APR13
 * 18OCT12
 * NULL
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * FIRST TWO POSITIONS:  NUMBERS 0-9
 *  THIRD TO FIFTH POSITIONS:  LETTERS A-Z
 *  LAST TWO POSITIONS:  NUMBERS 0-9
 *  OR
 *  NULL
 *
 * SOURCE: DOD PRODUCT SPECIFICATION
 */
public final class ProcedureAmendmentDate extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 7;
  }

  @Override
  public int fieldCode() {
    return 444;
  }

  @Override
  public String regex() {
    return "(([0-9]{2}[A-Za-z]{3}[0-9]{2})?)";
  }
}