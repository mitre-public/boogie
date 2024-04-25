package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * STATEMENT OF THE NAVIGATION PERFORMANCE ACCURACY NECESSARY FOR OPERATION WITHIN A DEFINED AIRSPACE, ALSO CALLED SYSTEM USE ACCURACY.  RNP VALUE IS EXPRESSED IN NAUTICAL MILES. ENTERED INTO THE FIELD IN NAUTICAL MILES (TWO DIGITS) WITH A ZERO OR NEGATIVE EXPONENT (ONE DIGIT). FOR VALUES LESS THAN 1, THE THIRD DIGIT WILL INDICATE HOW MANY PLACES TO THE LEFT TO MOVE THE DECIMAL.
 *
 * EXAMPLE(S):
 * RNP VALUE         	DAFIF OUTPUT
 * 10.0 NM               	100
 * 5.0 NM                  	050
 * 4.0 NM                  040
 * 2.0 NM                  	020
 * 1.0 NM                  	010
 * 0.3 NM                  	031
 * 0.15 NM                	152
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * COMBINATIONS OF NUMBERS 0-9
 * OR
 * NULL
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 *
 * INTENDED USE:o
 * TRM_SEG:  POPULATED WHEN SOURCED.
 */
public final class RequiredNavPerformance extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 3;
  }

  @Override
  public int fieldCode() {
    return 266;
  }

  @Override
  public String regex() {
    return "(([0-9]{2}[0-2])?)";
  }
}