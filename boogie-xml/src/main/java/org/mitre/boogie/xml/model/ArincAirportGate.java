package org.mitre.boogie.xml.model;

import java.util.Objects;

public class ArincAirportGate {
  String name;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    ArincAirportGate that = (ArincAirportGate) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}
