package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE LATITUDE EXPRESSED IN A GEOGRAPHIC COORDINATE (THE INTERSECTION OF LINES OR REFERENCE IN CONJUNCTION WITH LONGITUDE USED TO DETERMINE POSITION OR LOCATION).
 *
 * DME DEFINES THAT THE POINT IS DERIVED FROM SURVEY.
 *
 * EXAMPLE(S):
 * 57.774700
 * -16.850278
 * NULL
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 0.000000 THROUGH 90.000000
 * OR
 * -0.000001 THROUGH -90.000000
 * (LEADING ZEROS SUPPRESSED UNLESS VALUE IS LESS THAN 1.000000 OR GREATER THAN -1.000000, THERE WILL ALWAYS BE 6 DIGITS AFTER THE DECIMAL)
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * OUTPUT FORMAT IS IN DECIMAL DEGREES.
 */
public final class Nav12DmeDegreesLatitude extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 10;
  }

  @Override
  public int fieldCode() {
    return 198;
  }

  @Override
  public String regex() {
    return "(((\\-)?[0-9]{1,2}\\.[0-9]{6}|( ){7}-90|( ){8}90)?)";
  }
}