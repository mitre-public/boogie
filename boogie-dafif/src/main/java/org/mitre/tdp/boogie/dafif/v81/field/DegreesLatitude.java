package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE LATITUDE OF A POINT EXPRESSED IN DECIMAL DEGREE COORDINATES (THE INTERSECTION OF LINES  OF REFERENCE IN CONJUNCTION WITH DECIMAL DEGREE LONGITUDE USED TO DETERMINE POSITION OR  LOCATION).
 *
 * EXAMPLE(S):
 * NULL
 * 0.000000
 * 17.141770
 * -31.436500
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
 * SOURCE: SYSTEM GENERATED
 *
 * INTENDED USE:
 * CALCULATED BY CONVERTING SPECIFIC COORDINATES FROM DMS FORMAT TO DDS.  IF THERE IS NO DMS  VALUE (NULL FIELD), THERE WILL BE NO DDS VALUE POPULATED.
 */
public final class DegreesLatitude extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 10;
  }

  @Override
  public int fieldCode() {
    return 87;
  }

  @Override
  public String regex() {
    return "(([\\-](([0-9]|[1-8][0-9])\\.[0-9]{5}[1-9]|([0-9]|[1-8][0-9])\\.[0-9]{4}[0-9][1-9]|([0-9]|[1-8][0-9])\\.[0-9]{3}[1-9][0-9]{2}|([0-9]|[1-8][0-9])\\.[0-9]{2}[1-9][0-9]{3}|([0-9]|[1-8][0-9])\\.[0-9][1-9][0-9]{4}|([0-9]|[1-8][0-9])\\.[1-9][0-9]{5}|([1-9]|[1-8][0-9])\\.[0-9]{6})|([0-9]|[1-8][0-9])\\.[0-9]{6}|(\\-)?(90.000000))?)";
  }
}