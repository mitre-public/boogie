package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * DEFINES WHETHER THE LEVEL OF SERVICE DESIGNATED IN THE 'LEVEL' OF SERVICE NAME FIELD IS AUTHORIZED (A) OR NOT AUTHORIZED (N) FOR A PROCEDURE.
 *
 * LOS1:  UTILIZED FOR PRECISION RNAV, AND RNP
 * LOS2:  UTILIZED FOR LNAV/VNAV RNAV, AND RNP
 * LOS3:  UTILIZED FOR LNAV/RNAV, AND RNP
 *
 * EXAMPLE(S):
 * A
 * N
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * A, N
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class LevelOfService extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 445;
  }

  @Override
  public String regex() {
    return "(A|N)";
  }
}