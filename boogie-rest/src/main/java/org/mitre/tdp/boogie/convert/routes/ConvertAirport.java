package org.mitre.tdp.boogie.convert.routes;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.contract.routes.*;

public final class ConvertAirport implements Function<org.mitre.tdp.boogie.Airport, Airport> {
  public static final ConvertAirport INSTANCE = new ConvertAirport();

  private static final ConvertRunway CONVERT_RUNWAY = ConvertRunway.INSTANCE;
  private static final ConvertLatLong LAT_LONG = ConvertLatLong.INSTANCE;

  private ConvertAirport() {

  }

  private static List<Runway> fromAirportsRunways(org.mitre.tdp.boogie.Airport airport) {
    return airport.runways().stream().map(CONVERT_RUNWAY).collect(Collectors.toList());
  }

  private static Fix fromAirport(org.mitre.tdp.boogie.Airport airport) {
    return ImmutableFix.builder()
        .fixIdentifier(airport.airportIdentifier())
        .fixRegion(airport.airportRegion())
        .publishedVariation(airport.publishedVariation())
        .modeledVariation(airport.modeledVariation())
        .latLong(LAT_LONG.apply(airport.latLong()))
        .elevation(airport.elevation())
        .build();
  }

  @Override
  public Airport apply(org.mitre.tdp.boogie.Airport airport) {
    return ImmutableAirport.builder()
        .airportIdentifier(airport.airportIdentifier())
        .airportRegion(airport.airportRegion())
        .runways(fromAirportsRunways(airport))
        .fix(fromAirport(airport))
        .build();
  }
}
