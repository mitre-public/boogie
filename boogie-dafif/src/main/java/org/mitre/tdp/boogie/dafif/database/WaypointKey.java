package org.mitre.tdp.boogie.dafif.database;

import java.util.Objects;

public class WaypointKey {
  private final String waypointIdentifier;
  private final String countryCode;

  public WaypointKey(String waypointIdentifier, String countryCode) {
    this.waypointIdentifier = waypointIdentifier;
    this.countryCode = countryCode;
  }

  public String waypointIdentifier() {
    return waypointIdentifier;
  }

  public String countryCode() {
    return countryCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    WaypointKey that = (WaypointKey) o;
    return Objects.equals(waypointIdentifier, that.waypointIdentifier) && Objects.equals(countryCode, that.countryCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(waypointIdentifier, countryCode);
  }

  @Override
  public String toString() {
    return "WaypointKey{" +
        "waypointIdentifier='" + waypointIdentifier + '\'' +
        ", countryCode='" + countryCode + '\'' +
        '}';
  }
}
