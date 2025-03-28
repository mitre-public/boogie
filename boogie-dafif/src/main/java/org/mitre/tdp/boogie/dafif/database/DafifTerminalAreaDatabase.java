package org.mitre.tdp.boogie.dafif.database;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;

import com.google.common.collect.Multimap;

public final class DafifTerminalAreaDatabase {
  private final Map<AirportKey, DafifAirport> airports;
  private final Multimap<AirportKey, DafifRunway> runways;
  private final Multimap<AirportKey, DafifAddRunway> addRunways;
  private final Multimap<IlsKey, DafifIls> ils;

  public DafifTerminalAreaDatabase(Map<AirportKey, DafifAirport> airports, Multimap<AirportKey, DafifRunway> runways, Multimap<AirportKey, DafifAddRunway> addRunways, Multimap<IlsKey, DafifIls> ils) {
    this.airports = airports;
    this.runways = runways;
    this.addRunways = addRunways;
    this.ils = ils;
  }

  public Optional<DafifAirport> airport(String airportIdentifier) {
    return Optional.ofNullable(airports.get(new AirportKey(airportIdentifier)));
  }

  public Collection<DafifRunway> runwaysAt(String airportIdentifier) {
    return runways.get(new AirportKey(airportIdentifier));
  }

  public Optional<DafifRunway> runway(String airportIdentifier, String runwayIdentifier) {
    return runways.get(new AirportKey(airportIdentifier)).stream()
        .filter(r -> r.lowEndIdentifier().equals(runwayIdentifier) || r.highEndIdentifier().equals(runwayIdentifier))
        .findFirst();
  }

  public Optional<DafifAddRunway> addRunway(String airportIdentifier, String runwayIdentifier) {
    return addRunways.get(new AirportKey(airportIdentifier)).stream()
        .filter(r -> r.lowEndRunwayIdentifier().equals(runwayIdentifier) || r.highEndRunwayIdentifier().equals(runwayIdentifier))
        .findFirst();
  }

  public Collection<DafifAddRunway> addRunwaysAt(String airportIdentifier) {
    return addRunways.get(new AirportKey(airportIdentifier));
  }

  public Optional<DafifAddRunway> addRunwayFor(DafifRunway runway) {
    return addRunways.get(new AirportKey(runway.airportIdentification())).stream()
        .filter(a -> a.highEndRunwayIdentifier().equals(runway.highEndIdentifier()) && a.lowEndRunwayIdentifier().equals(runway.lowEndIdentifier()))
        .findFirst();
  }

  public Collection<DafifIls> ilsComponents(String airportIdentifier, String runwayIdentifier) {
    return ils.get(new IlsKey(airportIdentifier, runwayIdentifier));
  }

  public Collection<DafifIls> ilsComponentsForRunway(DafifRunway runway)  {
    Collection<DafifIls> lowEnd = ilsComponents(runway.airportIdentification(), runway.lowEndIdentifier());
    Collection<DafifIls> highEnd = ilsComponents(runway.airportIdentification(), runway.highEndIdentifier());
    return Stream.of(lowEnd, highEnd).flatMap(Collection::stream).collect(Collectors.toList());
  }
}
