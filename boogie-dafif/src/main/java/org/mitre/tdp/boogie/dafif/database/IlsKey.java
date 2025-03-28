package org.mitre.tdp.boogie.dafif.database;

import java.util.Objects;

public class IlsKey {
  private final String airportIdentifier;
  private final String runwayIdentifier;

  public IlsKey(String airportIdentifier, String runwayIdentifier) {
    this.airportIdentifier = airportIdentifier;
    this.runwayIdentifier = runwayIdentifier;
  }

  public String airportIdentifier() {
    return airportIdentifier;
  }

  public String runwayIdentifier() {
    return runwayIdentifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    IlsKey ilsKey = (IlsKey) o;
    return Objects.equals(airportIdentifier, ilsKey.airportIdentifier) && Objects.equals(runwayIdentifier, ilsKey.runwayIdentifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airportIdentifier, runwayIdentifier);
  }

  @Override
  public String toString() {
    return "IlsKey{" +
        "airportIdentifier='" + airportIdentifier + '\'' +
        ", runwayIdentifier='" + runwayIdentifier + '\'' +
        '}';
  }
}
