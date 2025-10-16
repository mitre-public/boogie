package org.mitre.boogie.xml.v23_4.convert;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincAirportGate;
import org.mitre.boogie.xml.model.ArincRunway;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.RunwaySurfaceCode;
import org.mitre.boogie.xml.v23_4.generated.Airport;

public final class ArincAirportConverter implements Function<Airport, Optional<ArincAirport>> {
  public static final ArincAirportConverter INSTANCE = new ArincAirportConverter();

  private static final ArincAirportValidator VALIDATOR = new ArincAirportValidator();
  private static final ArincPortConverter PORT_CONVERTER = ArincPortConverter.INSTANCE;

  private ArincAirportConverter() {
  }

  @Override
  public Optional<ArincAirport> apply(Airport airport) {
    return Optional.ofNullable(airport)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincAirport convert(Airport airport) {
    ArincPortInfo portInfo = PORT_CONVERTER.apply(airport);
    String surfaceCode = Optional.ofNullable(airport.getLongestRunwaySurfaceCode())
        .map(Enum::name)
        .filter(RunwaySurfaceCode.VALID::contains)
        .map(RunwaySurfaceCode::valueOf)
        .map(Enum::name)
        .orElse(null);
    //todo do this
    List<ArincRunway> runways = List.of();
    List<ArincAirportGate> gates = List.of();

    return ArincAirport.builder()
        .portInfo(portInfo)
        .longestRunway(airport.getLongestRunway())
        .longestRunwaySurfaceCode(surfaceCode)
        .runways(runways)
        .airportGates(gates)
        .build();
  }
}
