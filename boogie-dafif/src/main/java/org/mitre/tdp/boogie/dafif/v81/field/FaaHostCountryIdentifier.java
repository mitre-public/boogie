package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A LOCATION IDENTIFIER ASSIGNED BY THE FEDERAL AVIATION ADMINISTRATION (FAA) AS
 * DESIGNATED IN THE FAA LOCATION IDENTIFIERS HANDBOOK 7350.* OR HOST COUNTRY PUBLICATION.
 * IF THE AIRPORT HAS NO ICAO OR HOST COUNTRY IDENTIFIER, THE FAA_HOST_ID FIELD WILL BE
 * POPULATED WITH A SYSTEM GENERATED ALPHA-NUMERIC IDENTIFIER.  THIS IDENTIFIER WILL CONSIST
 * OF THE COUNTRIES TWO CHARACTER ICAO REGION CODE FOLLOWED BY TWO DIGITS.
 *
 * EXAMPLE(S): 0408 00CA ET26 ZZV N
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * "THREE POSITIONS:  COMBINATIONS OF NUMBERS 0-9, LETTERS A-Z 	OR 	FOUR POSITIONS:  COMBINATIONS OF NUMBERS 0-9, LETTERS A-Z 	OR 	N"
 *
 * INTENDED USE:
 *  AN "N" INDICATES UNKNOWN, OR NO FAA OR HOST NATION IDENTIFIER IS ASSIGNED.
 *
 * SOURCE: TRANSLATE/FORMAT FROM HOST NATION PUBLICATION
 */
public final class FaaHostCountryIdentifier extends TrimmableString {

  @Override
  public int maxFieldLength() {
    return 4;
  }

  @Override
  public int fieldCode() {
    return 109;
  }

  @Override
  public String regex() {
    return "([0-9A-Z]{3,4}|N)";
  }

}
