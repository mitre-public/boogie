package org.mitre.tdp.boogie.dafif.assemble;

import java.util.Collection;
import java.util.List;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.utils.DafifMagVars;
import org.mitre.tdp.boogie.dafif.utils.extractors.AirportReferencePointExtractor;
import org.mitre.tdp.boogie.dafif.utils.extractors.LandingCoordExtractor;

public interface AirportAssemblyStrategy<A, R> {
  A convertAirport(DafifAirport airport, List<R> convertedRunways);
  List<R> convertRunway(DafifRunway runway, DafifAddRunway addRunway, Collection<DafifIls> ils);

  static AirportAssemblyStrategy<Airport, Runway>  standard() {
    return new Standard();
  }

  final class Standard implements AirportAssemblyStrategy<Airport, Runway> {

    @Override
    public Airport convertAirport(DafifAirport airport, List<Runway> convertedRunways) {
      MagneticVariation magvar = airport.magVarOfRecord()
          .map(DafifMagVars::fromRecord)
          .orElseGet(() -> DafifMagVars.fromDynamic(airport.magneticVariation()));

      LatLong location = AirportReferencePointExtractor.INSTANCE.apply(airport).orElseThrow(() -> new IllegalArgumentException("This airport has no location: ".concat(airport.toString())));

      return Airport.builder()
          .airportIdentifier(airport.airportIdentification())
          .runways(convertedRunways)
          .magneticVariation(magvar)
          .latLong(location)
          .build();
    }

    @Override
    public List<Runway> convertRunway(DafifRunway runway, DafifAddRunway addRunway, Collection<DafifIls> ils) {
      Pair<LatLong, LatLong> landingThresholds = LandingCoordExtractor.INSTANCE.apply(Pair.of(runway, addRunway));

      Runway le = Runway.builder()
          .runwayIdentifier(runway.lowEndIdentifier())
          .course(landingThresholds.first().courseTo(landingThresholds.second()))
          .length(landingThresholds.first().distanceTo(landingThresholds.second()))
          .origin(landingThresholds.first())
          .elevation(runway.lowEndElevation().map(Double::parseDouble).map(Distance::ofFeet).orElse(null))
          .build();

      Runway he = Runway.builder()
          .runwayIdentifier(runway.highEndIdentifier())
          .course(landingThresholds.second().courseTo(landingThresholds.first()))
          .length(landingThresholds.second().distanceTo(landingThresholds.first()))
          .origin(landingThresholds.second())
          .elevation(runway.highEndElevation().map(Double::parseDouble).map(Distance::ofFeet).orElse(null))
          .build();

      return List.of(le, he);
    }
  }
}
