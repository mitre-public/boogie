package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A SUPPLEMENTARY LETTER ADDED AFTER THE ATS IDENTIFIER WHICH INDICATES THE TYPE OF SERVICE PROVIDED ON THE ROUTE.
 *
 * CURRENT VALUES FROM ICAO ANNEX 11 INCLUDE:
 *   F-ON THE ROUTE OR A PORTION THEREOF, ADVISORY SERVICE ONLY IS PROVIDED
 *   G-ON THE ROUTE OR A PORTION THEREOF, FLIGHT INFORMATION SERVICE ONLY IS PROVIDED.
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * A-Z
 * OR
 * NULL
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class AtsDesignator extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 440;
  }

  @Override
  public String regex() {
    return "(F|G|^$)";
  }
}