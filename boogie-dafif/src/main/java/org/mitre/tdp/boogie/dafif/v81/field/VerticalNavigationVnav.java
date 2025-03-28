package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE VERTICAL NAVIGATION FIELD DEFINES THE VERTICAL NAVIGATION PATH PRESCRIBED FOR THE PROCEDURE (VERTICAL ANGLE).  THE VERTICAL ANGLE SHOULD CAUSE THE AIRCRAFT TO FLY AT THE LAST CODED ALTITUDE AND THEN DESCEND ON THE ANGLE, PROJECTED BACK FROM THE FIX AND ALTITUDE CODE FOR THAT FIX AT WHICH THE ANGLE IS CODED.  VERTICAL ANGLE IS PROVIDED ONLY FOR DESCENDING VERTICAL NAVIGATION.  VALUES FROM OFFICIAL GOVERNMENT SOURCES WILL BE USED WHEN AVAILABLE (E.G. PUBLISHED ELECTRONIC GLIDE SLOPE OR VNAV ANGLE) DISPLAYED TO THE HUNDREDTHS OF A DEGREE.
 *
 * EXAMPLE(S):
 * 3.00
 * 1.62
 * 0.00
 * NULL
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 0.00 -13.00 (VALUES ARE PADDED WITH TRAILING ZEROS)
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE:
 * WHEN VNAV IS CALCULATED FOR FAA RNAV PROCEDURES THAT DO NOT HAVE A SUPPLIED VERTICAL DECENT ANGLE (DAFIF CALLS IT VNAV) DUE TO OBSTACLES IN THE VISUAL SEGMENT, A REMARK WILL BE PLACED IN TRM_REMARK.REMARKS IN RMK_SEQ 2 THAT READS: 'VNAV CALCULATED'. THIS REMARK IS INTENDED TO GIVE USERS THE ABILITY TO IDENTIFY FAA RNAV PROCEDURES IN THE FILE THAT CONTAIN NGA CALCULATED VALUES.
 *
 * NOTE:  THE RANGE FOR DAFIF 8.1 DIFFERS FROM DAFIF 8.0 (NO PROCEDURES WITH A "VNAV" VALUE  GREATER THAN "9.99" WILL BE INCLUDED  IN DAFIF 8.0).
 *
 * NGA has developed a MAP Altitude Calculator Tool for calculating VNAV.  In the absense of source values, the tool will be used to calculate the VNAV angle using industry standard calculations and rules.  If source values are provided, this tool will be used to verify and validate those values.
 */
public final class VerticalNavigationVnav extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 333;
  }

  @Override
  public String regex() {
    return "((([0-9]|1[0-2])\\.[0-9]{2}|13.00)?)";
  }
}