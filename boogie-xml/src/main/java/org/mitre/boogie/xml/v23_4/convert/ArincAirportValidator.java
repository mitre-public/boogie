package org.mitre.boogie.xml.v23_4.convert;

import static java.util.Objects.nonNull;

import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.Airport;

public final class ArincAirportValidator implements Predicate<Airport> {
  @Override
  public boolean test(Airport airport) {
    return nonNull(airport.getName())
        && nonNull(airport.getLocation());
  }
}
