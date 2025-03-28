package org.mitre.tdp.boogie.dafif.database;

import java.util.Objects;

public class AirportKey {
  private final String airportIdentifier;

  public AirportKey(String airportIdentifier) {
    this.airportIdentifier = airportIdentifier;
  }

  public String airportIdentifier() {
    return airportIdentifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    AirportKey that = (AirportKey) o;
    return Objects.equals(airportIdentifier, that.airportIdentifier);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(airportIdentifier);
  }

  @Override
  public String toString() {
    return "AirportKey{" +
        "airportIdentifier='" + airportIdentifier + '\'' +
        '}';
  }
}
