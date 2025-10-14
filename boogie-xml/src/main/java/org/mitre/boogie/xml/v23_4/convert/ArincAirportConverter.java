package org.mitre.boogie.xml.v23_4.convert;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.v23_4.generated.Airport;

public final class ArincAirportConverter implements Function<Airport, ArincAirport> {

  @Override
  public ArincAirport apply(Airport airport) {
    List<ArincWaypoint> wpts = airport.getTerminalWaypoint().stream()
        .map(ArincWaypointConverter.INSTANCE)
        .flatMap(Optional::stream)
        .toList();

    return null;
  }
}
