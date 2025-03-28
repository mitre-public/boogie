package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * SPEED IN KNOTS.  THE SPEED LIMIT WILL APPLY TO LEGS FROM THE BEGINNING OF THE PROCEDURE TO  THE TERMINATION LEG.  THE SPEED LIMIT ON SIDS MAY START AT THE BEGINNING OF THE PROCEDURE.   THE SPEED LIMIT ON STARS MAY START HALFWAY THROUGH THE PROCEDURE.
 *
 * EXAMPLE(S):
 * 90
 * 310
 * NULL
 *
 *  HOLDING PATTERN:  MAXIMUM SPEED IN THE HOLDING PATTERN.  THE SPEED IS SHOWN IN KNOTS.   APPLIES TO DESCRIPTOR CODES HA, HF, HM.
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 40-999 (LEADING ZEROS SUPPRESSED)
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * ON SID PROCEDURE RECORDS, THE SPEED LIMIT WILL APPLY TO ALL LEGS UP TO AND INCLUDING THE TERMINATION OF THE LEG ON WHICH THE SPEED IS CODED FROM THE BEGINNING OF THE PROCEDURE OR A PREVIOUS SPEED LIMIT.  IF A DIFFERENT SPEED IS CODED ON A SUBSEQUENT LEG, THE LIMIT WILL BE APPLIED FOR THAT LEG AND FROM THAT LEG BACKWARDS TO THE PREVIOUS TERMINATOR WHICH CONTAINED A SPEED LIMIT.
 * ON STAR AND APPROACH PROCEDURE RECORDS, THE SPEED LIMIT WILL BE APPLIED FORWARD TO THE END OF THE PROCEDURE OR UNTIL SUPERCEDED BY ANOTHER SPEED LIMIT.
 *
 * WHEN MULTIPLE SPEED RESTRICTIONS BASED UPON AIRCRAFT PERFORMANCE FOR A SINGLE SEGMENT ARE PROVIDED BY HOST SOURCE, THE JET RESTRICTION WILL BE CODED. FOR INSTANCE, ON A STAR THAT HAS A SPEED RESTRICTION OF 230 FOR JET AND 180 FOR TURBO PROP, 230 WILL BE CODED.
 */
public final class SpeedLimit extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 3;
  }

  @Override
  public int fieldCode() {
    return 295;
  }

  @Override
  public String regex() {
    return "(([1-9][0-9]{2}|[4-9][0-9])?)";
  }
}