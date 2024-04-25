package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE LONGITUDE EXPRESSED IN A GEOGRAPHIC COORDINATE (THE INTERSECTION OF LINES  OF REFERENCE IN CONJUNCTION WITH LATITUDE USED TO DETERMINE POSITION OR LOCATION).  (DAFIF)
 *
 * DME DEFINES THAT THE POINT IS DERIVED FROM SURVEY.
 *
 * EXAMPLE(S):
 * E033373210
 * W076052113
 * NULL
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * E000000000 - E179595999
 * OR
 * E180000000
 * OR
 * W000000001 - W179595999
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class Nav12DmeGeodeticLongitude extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 10;
  }

  @Override
  public int fieldCode() {
    return 201;
  }

  @Override
  public String regex() {
    return "(((E|W)[0-9]{9})?)";
  }
}