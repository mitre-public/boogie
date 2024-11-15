package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Optional.ofNullable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.utils.AiracCycle;
import org.mitre.tdp.boogie.util.Declinations;

/**
 * Strategy class for generating user-defined records from 424 airport information. Used with {@link AirportAssembler}.
 */
public interface AirportAssemblyStrategy<A, R> {

  /**
   * Airport assembly strategy for building {@link Airport.Standard} and {@link Runway.Standard} definitions from airport records
   * defined in a 424 file.
   */
  static AirportAssemblyStrategy<Airport, Runway> standard() {
    return new Standard();
  }

  /**
   * Converts the incoming 424 airport record and a collection of runways into an output user-defined type.
   *
   * @param airport          the input 424 airport definition record
   * @param convertedRunways the collection of runways associated with the airport converted with the {@code .convertRunway(...)}
   *                         method
   */
  A convertAirport(ArincAirport airport, List<R> convertedRunways);

  /**
   * Convert a collection of runway-associated 424 records into a single user-defined runway types.
   *
   * @param airport    the 424 airport record associated with the runway
   * @param origin     the 424 definition of the origin of the runway, for two runways (01L, 17L) this method will be called once with
   *                   01L as the origin and 17L as the reciprocal and once in reverse
   * @param reciprocal the 424 definition of the reciprocal end of the runway (see the above doc)
   * @param ilsGls1    the first ilsGls record for the origin runway
   * @param ilsGls2    the second ilsGls record for the origin runway
   */
  R convertRunway(ArincAirport airport, ArincRunway origin, ArincRunway reciprocal, ArincLocalizerGlideSlope ilsGls1, ArincLocalizerGlideSlope ilsGls2);

  final class Standard implements AirportAssemblyStrategy<Airport, Runway> {

    private Standard() {
    }

    @Override
    public Airport.Standard convertAirport(ArincAirport airport, List<Runway> convertedRunways) {
      return Airport.builder()
          .airportIdentifier(airport.airportIdentifier())
          .latLong(LatLong.of(airport.latitude(), airport.longitude()))
          .magneticVariation(magneticVariation(airport))
          .runways(convertedRunways)
          .build();
    }

    @Override
    public Runway.Standard convertRunway(ArincAirport airport, ArincRunway origin, ArincRunway reciprocal, ArincLocalizerGlideSlope ilsGls1, ArincLocalizerGlideSlope ilsGls2) {

      MagneticVariation magneticVariation = magneticVariation(airport);

      Optional<Course> trueCourse = origin.runwayMagneticBearing()
          .map(magneticVariation::magneticToTrue)
          .map(Course::ofDegrees)
          .or(() -> ofNullable(reciprocal).map(r -> courseBetween(origin, r)));

      return Runway.builder()
          .runwayIdentifier(origin.runwayIdentifier())
          .origin(LatLong.of(origin.latitude(), origin.longitude()))
          .length(origin.runwayLength().map(Integer::doubleValue).map(Distance::ofFeet).orElse(null))
          .course(trueCourse.orElse(null))
          .build();
    }

    private Course courseBetween(ArincRunway origin, ArincRunway reciprocal) {
      return LatLong.of(origin.latitude(), origin.longitude()).courseTo(LatLong.of(reciprocal.latitude(), reciprocal.longitude()));
    }

    private MagneticVariation magneticVariation(ArincAirport airport) {

      Instant cycleDate = AiracCycle.startDate(airport.lastUpdateCycle());

      double degrees = airport.magneticVariation()
          .orElseGet(() -> Declinations.declination(airport.latitude(), airport.longitude(), cycleDate));

      return MagneticVariation.ofDegrees(degrees);
    }
  }
}
