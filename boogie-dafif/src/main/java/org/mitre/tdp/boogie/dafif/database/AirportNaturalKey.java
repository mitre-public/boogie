package org.mitre.tdp.boogie.dafif.database;

public record AirportNaturalKey(String icaoCode, String countryCode) {
  public static AirportNaturalKey from(String icaoCode, String countryCode) {
    return new AirportNaturalKey(icaoCode, countryCode);
  }
}
