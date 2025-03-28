package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE LONGITUDE OF A POINT EXPRESSED IN A DECIMAL DEGREE COORDINATE (THE INTERSECTION OF LINES OF REFERENCE IN CONJUNCTION WITH LATITUDE USED TO DETERMINE POSITION OR LOCATION).
 *
 * EXAMPLE(S):
 * 5.404300
 * -177.394028
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
 * SOURCE: SYSTEM GENERATED
 *
 * INTENDED USE:
 * CALCULATED BY CONVERTING SPECIFIC COORDINATES FROM DMS FORMAT TO DDS.  IF THERE IS NO DMS  VALUE (NULL FIELD), THERE WILL BE NO DDS VALUE POPULATED.
 */
public final class DegreesLongitude extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 11;
  }

  @Override
  public int fieldCode() {
    return 89;
  }

  @Override
  public String regex() {
    return "((([0-9]|[1-9][0-9]|1[0-7][0-9])\\.[0-9]{6}|[\\-](([0-9]|[1-9][0-9]|1[0-7][0-9])\\.[0-9]{5}[1-9]|([0-9]|[1-9][0-9]|1[0-7][0-9])\\.[0-9]{4}[0-9][1-9]|([0-9]|[1-9][0-9]|1[0-7][0-9])\\.[0-9]{3}[1-9][0-9]{2}|([0-9]|[1-9][0-9]|1[0-7][0-9])\\.[0-9]{2}[1-9][0-9]{3}|([0-9]|[1-9][0-9]|1[0-7][0-9])\\.[0-9][1-9][0-9]{4}|([0-9]|[1-9][0-9]|1[0-7][0-9])\\.[1-9][0-9]{5}|([1-9]|[1-9][0-9]|1[0-7][0-9])\\.[0-9]{6})|180.000000)?)";
  }
}