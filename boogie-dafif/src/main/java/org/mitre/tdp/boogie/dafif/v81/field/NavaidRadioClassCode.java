package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * FACILITY CLASS CODE EXPRESSED BY THE FOLLOWING CODES:
 *
 *    H - NON-DIRECTIONAL RADIO BEACON (HOMING), POWER 50 WATTS TO LESS THAN 2000 WATTS.
 *
 *    L - NORMAL ANTICIPATED INTERFERENCE-FREE SERVICE, 40 NM UP TO 18,000 FEET.
 *
 *    T - NORMAL ANTICIPATED INTERFERENCE-FREE SERVICE, 25 NM UP TO 12,000 FEET.
 *
 *    MH - NON-DIRECTIONAL RADIO BEACON (HOMING), POWER LESS THAN 50 WATTS.
 *
 *    HH - NON-DIRECTIONAL RADIO BEACON (HOMING), POWER 2000 WATTS OR MORE.
 *
 *    HA - NORMAL ANTICIPATED INTERFERENCE FREE SERVICE, BELOW 18,000 FEET - 40 NM; 14,500 -17,999 FEET - 100 NM (CONTIGUOUS 48 STATES ONLY); 18,000 FEET TO FL 450 - 130 NM; ABOVE  FL 450 - 100NM.
 *
 *    CL - COMPASS LOCATOR, POWER 25 WATTS OR LESS, DISTANCE/RADIUS 15 NM.
 *
 *    U - UNKNOWN
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * H, L, T, MH, HH, HA, CL, U
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class NavaidRadioClassCode extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 2;
  }

  @Override
  public int fieldCode() {
    return 255;
  }

  @Override
  public String regex() {
    return "(H|L|T|MH|HH|HA|CL|U)";
  }
}