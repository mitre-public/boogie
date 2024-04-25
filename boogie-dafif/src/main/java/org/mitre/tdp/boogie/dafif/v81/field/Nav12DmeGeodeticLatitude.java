package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE LATITUDE EXPRESSED IN A GEOGRAPHIC COORDINATE (THE INTERSECTION OF LINES OR REFERENCE IN CONJUNCTION WITH LONGITUDE USED TO DETERMINE POSITION OR LOCATION).  (DAFIF)
 *
 * DME DEFINES THAT THE POINT IS DERIVED FROM SURVEY.
 * EXAMPLE(S):
 * S16510000
 * N34522230
 * NULL
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * N00000000 - N89595999
 * OR
 * N90000000
 * OR
 * S00000001 - S89595999
 * OR
 * S90000000
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class Nav12DmeGeodeticLatitude extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 9;
  }

  @Override
  public int fieldCode() {
    return 200;
  }

  @Override
  public String regex() {
    return "(((N|S)[0-9]{8})?)";
  }
}