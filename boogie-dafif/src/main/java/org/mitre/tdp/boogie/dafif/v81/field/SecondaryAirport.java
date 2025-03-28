package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A ONE CHARACTER TRIGGER BIT USED TO IDENTIFY THE OCCURRENCE OF A SECONDARY AIRPORT AT THE 	PRIMARY AIRPORT
 * LOCATION.  THE SECONDARY AIRPORT FIELD IS CODED “Y” IN THE FAMILY RECORD.  	AN EXAMPLE OF A SECONDARY
 * AIRPORT REQUIREMENT IS ANDREWS AFB (PRIMARY) AND NAS 	WASHINGTON (SECONDARY).  BOTH HAVE A UNIQUE
 * ICAO IDENTIFIER AND USER REQUIREMENTS FOR 	MAINTAINING DISTINCTION.  IF NO SECONDARY AIRPORT REQUIREMENT
 * EXISTS, THE FIELD WILL BE NULL.
 *
 * EXAMPLE(S):
 * NULL
 * Y
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * Y
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class SecondaryAirport extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 276;
  }

  @Override
  public String regex() {
    return "((Y)?)";
  }
}
