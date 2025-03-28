package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE LONGITUDE EXPRESSED IN A GEOGRAPHIC COORDINATE (THE INTERSECTION OF LINES  OF REFERENCE IN CONJUNCTION WITH LATITUDE USED TO DETERMINE POSITION OR LOCATION).  (DAFIF)
 *
 * DME DEFINES THAT THE POINT IS DERIVED FROM SURVEY.
 *
 * EXAMPLE(S)
 * 145.743889
 * -7.974889
 * -0.483194
 * NULL
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 0.000000 THROUGH 180.000000
 * OR
 * -0.000001 THROUGH -179.999999
 * (LEADING ZEROS SUPPRESSED UNLESS VALUE IS LESS THAN 1.000000 OR GREATER THAN -1.000000, THERE WILL ALWAYS BE 6 DIGITS AFTER THE DECIMAL)
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * OUTPUT FORMAT IS IN DECIMAL DEGREES.  VALUES ALWAYS CONTAIN 6 NUMERICS TO THE RIGHT OF THE DECIMAL.  LEADING ZEROS SUPRESSED UNLESS VALUE IS LESS THAN 1.000000 OR GREATER THAN -1.000000.
 */
public final class Nav12DmeDegreesLongitude extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 11;
  }

  @Override
  public int fieldCode() {
    return 199;
  }

  @Override
  public String regex() {
    return "(((\\-)?([0-9]|[1-9][0-9]|1[0-7][0-9])\\.[0-9]{6}|( ){7}-180|( ){8}180)?)";
  }
}