package org.mitre.boogie.xml.assemble;

import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Optional;

import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincHelipad;
import org.mitre.boogie.xml.model.ArincLocalizerGlideSlope;
import org.mitre.boogie.xml.model.ArincRunway;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.util.MagneticVariationResolver;
import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.Runway;

/**
 * Strategy class for generating user-defined records from XML airport information. Used with {@link AirportAssembler}.
 *
 * <p>Because the XML model is hierarchical (runways, helipads, ILS are nested within the airport), the assembler does not
 * require an external terminal area database — all associated data is contained within the {@link ArincAirport} itself.
 */
public interface AirportAssemblyStrategy<A, R, H> {

  static AirportAssemblyStrategy<Airport, Runway, Helipad> standard() {
    return new Standard();
  }

  /**
   * Converts the incoming XML airport record and a collection of runways/helipads into an output user-defined type.
   *
   * @param airport          the input XML airport definition record
   * @param convertedRunways the collection of runways associated with the airport converted with {@code convertRunway}
   * @param convertedHelipads the collection of helipads associated with the airport converted with {@code convertHelipad}
   */
  A convertAirport(ArincAirport airport, List<R> convertedRunways, List<H> convertedHelipads);

  /**
   * Convert a runway and its associated records into a single user-defined runway type.
   *
   * @param airport    the XML airport record associated with the runway
   * @param origin     the XML definition of the origin end of the runway
   * @param reciprocal the XML definition of the reciprocal end of the runway, may be null
   * @param ilsGls1    the primary localizer/glideslope record for this runway end, may be null
   * @param ilsGls2    the secondary localizer/glideslope record for this runway end, may be null
   */
  R convertRunway(ArincAirport airport, ArincRunway origin, ArincRunway reciprocal, ArincLocalizerGlideSlope ilsGls1, ArincLocalizerGlideSlope ilsGls2);

  H convertHelipad(ArincHelipad pad);

  final class Standard implements AirportAssemblyStrategy<Airport, Runway, Helipad> {

    private Standard() {
    }

    @Override
    public Airport convertAirport(ArincAirport airport, List<Runway> convertedRunways, List<Helipad> convertedHelipads) {
      ArincPortInfo portInfo = airport.portInfo();
      ArincPointInfo point = portInfo.pointInfo();

      return Airport.builder()
          .airportIdentifier(point.identifier())
          .latLong(point.latLong())
          .magneticVariation(magneticVariation(portInfo))
          .runways(convertedRunways)
          .helipads(convertedHelipads)
          .build();
    }

    @Override
    public Runway convertRunway(ArincAirport airport, ArincRunway origin, ArincRunway reciprocal, ArincLocalizerGlideSlope ilsGls1, ArincLocalizerGlideSlope ilsGls2) {
      MagneticVariation magneticVariation = magneticVariation(airport.portInfo());

      Optional<Course> trueCourse = origin.runwayTrueBearing()
          .map(Course::ofDegrees)
          .or(() -> origin.runwayBearing().flatMap(bearing ->
              origin.runwayBearingIsTrueBearing()
                  .filter(Boolean::booleanValue)
                  .map(t -> Course.ofDegrees(bearing))
                  .or(() -> Optional.of(Course.ofDegrees(magneticVariation.magneticToTrue(bearing))))))
          .or(() -> ofNullable(reciprocal).map(r -> courseBetween(origin, r)));

      return Runway.builder()
          .runwayIdentifier(origin.pointInfo().identifier())
          .origin(origin.pointInfo().latLong())
          .length(origin.runwayLength().map(Long::doubleValue).map(Distance::ofFeet).orElse(null))
          .course(trueCourse.orElse(null))
          .build();
    }

    @Override
    public Helipad convertHelipad(ArincHelipad pad) {
      return Helipad.builder()
          .padIdentifier(pad.pointInfo().identifier())
          .origin(pad.pointInfo().latLong())
          .build();
    }

    private Course courseBetween(ArincRunway origin, ArincRunway reciprocal) {
      return origin.pointInfo().latLong().courseTo(reciprocal.pointInfo().latLong());
    }

    private MagneticVariation magneticVariation(ArincPortInfo portInfo) {
      return MagneticVariationResolver.INSTANCE.apply(portInfo.pointInfo(), portInfo.recordInfo().cycleDate());
    }
  }
}
