package org.mitre.tdp.boogie.convert.routes;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.routes.ImmutableRunway;
import org.mitre.tdp.boogie.contract.routes.Runway;

public final class ConvertRunway implements Function<org.mitre.tdp.boogie.Runway, Runway> {
  public static final ConvertRunway INSTANCE = new ConvertRunway();

  private static final ConvertLatLong LAT_LONG = ConvertLatLong.INSTANCE;

  private ConvertRunway() {

  }

  @Override
  public Runway apply(org.mitre.tdp.boogie.Runway runway) {
    return ImmutableRunway.builder()
        .runwayIdentifier(runway.runwayIdentifier())
        .airportIdentifier(runway.airportIdentifier())
        .airportRegion(runway.airportRegion())
        .arrivalRunwayEnd(LAT_LONG.apply(runway.arrivalRunwayEnd()))
        .departureRunwayEnd(runway.departureRunwayEnd().map(LAT_LONG))
        .width(runway.width())
        .length(runway.length())
        .build();
  }
}
