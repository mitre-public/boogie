package org.mitre.tdp.boogie.dafif.database;

import java.util.Objects;

/**
 * This key is meant to represent half a single dafif runway record.
 */
public class RunwayKey {
  private final String airportIdentifier;
  private final String lowIdent;
  private final String highIdent;


  public RunwayKey(String airportIdentifier, String lowIdent, String highIdent) {
    this.airportIdentifier = airportIdentifier;
    this.lowIdent = lowIdent;
    this.highIdent = highIdent;
  }

  public String airportIdentifier() {
    return airportIdentifier;
  }

  public String lowIdent() {
    return lowIdent;
  }

  public String highIdent() {
    return highIdent;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    RunwayKey runwayKey = (RunwayKey) o;
    return Objects.equals(airportIdentifier, runwayKey.airportIdentifier) && Objects.equals(lowIdent, runwayKey.lowIdent) && Objects.equals(highIdent, runwayKey.highIdent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airportIdentifier, lowIdent, highIdent);
  }

  @Override
  public String toString() {
    return "RunwayKey{" +
        "airportIdentifier='" + airportIdentifier + '\'' +
        ", lowIdent='" + lowIdent + '\'' +
        ", highIdent='" + highIdent + '\'' +
        '}';
  }
}
