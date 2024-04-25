package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * STANDARD ICAO METHOD OF REPORTING PAVEMENT STRENGTH FOR PAVEMENTS WITH BEARING 	STRENGTH GREATER THAN 12,500 POUNDS.  (ICAO)
 *
 * 	THE SEVEN CHARACTER CODE IS CONSTRUCTED BY THE FOLLOWING:
 *
 * 	1.  THE THREE CHARACTER PCN NUMBER
 *
 * 	2.  TYPE OF PAVEMENT
 * 	     R - RIGID
 * 	     F - FLEXIBLE
 *
 * 	3.  PAVEMENT SUBGRADE CATEGORY
 * 	     A - HIGH
 * 	     B - MEDIUM
 * 	     C - LOW
 * 	     D - ULTRA-LOW
 *
 * 	4.  MAXIMUM TIRE PRESSURE AUTHORIZED FOR PAVEMENT
 * 	     W - HIGH, NO LIMIT
 * 	     X - MEDIUM, LIMITED TO 217 PSI
 * 	     Y - LOW, LIMITED TO 181 PSI
 * 	     Z - VERY LOW, LIMITED TO 73 PSI
 *
 * 	5.  PAVEMENT EVALUATION METHOD
 * 	     T - TECHNICAL EVALUATION
 * 	     U - BY HISTORY OF VARIOUS AIRCRAFT USING THE PAVEMENT
 *
 * EXAMPLE(S): 166RBWT
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 *  POSITIONS 1-3:  000-199
 * 	POSITION 4:  R, F
 * 	POSITION 5:  A, B, C, D
 * 	POSITION 6:  W, X, Y, Z
 * 	POSITION 7:  T, U
 * 	OR
 * 	NULL
 *
 * HOST:
 * HOST NATION PUBLICATION
 */
public class PavementClassificationNumber extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 7;
  }

  @Override
  public int fieldCode() {
    return 230;
  }

  @Override
  public String regex() {
    return "(([0-1][0-9]{2}[R|F][A|B|C|D][W|X|Y|Z][T|U])?)";
  }
}
