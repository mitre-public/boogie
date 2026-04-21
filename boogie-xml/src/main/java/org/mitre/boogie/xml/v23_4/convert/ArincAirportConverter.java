package org.mitre.boogie.xml.v23_4.convert;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincAirportGate;
import org.mitre.boogie.xml.model.ArincRunway;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.RunwaySurfaceCode;
import org.mitre.boogie.xml.v23_4.generated.Airport;

public final class ArincAirportConverter implements Function<Airport, Optional<ArincAirport>> {
  public static final ArincAirportConverter INSTANCE = new ArincAirportConverter();

  private static final ArincAirportValidator VALIDATOR = new ArincAirportValidator();
  private static final ArincPortConverter PORT_CONVERTER = ArincPortConverter.INSTANCE;
  private static final ArincRunwayConverter RUNWAY_CONVERTER = ArincRunwayConverter.INSTANCE;
  private static final ArincAirportGateConverter GATE_CONVERTER = ArincAirportGateConverter.INSTANCE;

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
    List<ArincRunway> runways = airport.getRunway().stream()
        .map(RUNWAY_CONVERTER)
        .flatMap(Optional::stream)
        .toList();

    List<ArincAirportGate> gates = airport.getAirportGate().stream()
        .map(GATE_CONVERTER)
        .flatMap(Optional::stream)
        .toList();

    return ArincAirport.builder()
        .portInfo(portInfo)
        .longestRunway(airport.getLongestRunway())
        .longestRunwaySurfaceCode(surfaceCode)
        .runways(runways)
        .airportGates(gates)
        .build();
  }
}
