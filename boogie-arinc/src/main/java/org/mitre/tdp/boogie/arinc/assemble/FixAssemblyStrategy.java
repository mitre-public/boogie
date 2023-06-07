package org.mitre.tdp.boogie.arinc.assemble;

import static org.mitre.tdp.boogie.util.Declinations.magneticVariation;

import java.time.Instant;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.utils.AiracCycle;

/**
 * Strategy class for generated fix-like records from various 424 record types. The input types are all the record types which
 * can be referenced as the associated fix, recommended navaid, or center fix of procedure/airway legs.
 */
public interface FixAssemblyStrategy<F> {

  /**
   * Fix assembly strategy for building {@link Fix.Standard} definitions from various types of input 424 records.
   */
  static FixAssemblyStrategy<Fix> standard() {
    return new Standard();
  }

  /**
   * Converts the incoming 424 waypoint record (enroute or terminal) into the user-defined type.
   *
   * @param waypoint input 424 waypoint record
   */
  F convertWaypoint(ArincWaypoint waypoint);

  /**
   * Converts the incoming 424 NDB (Non-Directional Beacon) navaid record into the user-defined type.
   *
   * @param navaid input 424 navaid record
   */
  F convertNdbNavaid(ArincNdbNavaid navaid);

  /**
   * Converts the incoming 424 VHF (Very-High Frequency) navaid record into the user-defined type.
   *
   * @param navaid input 424 navaid record
   */
  F convertVhfNavaid(ArincVhfNavaid navaid);

  /**
   * Converts the incoming 424 airport record into a user-defined type. It's not uncommon to see an airport reference point used
   * as the origin point of a {@link PathTerminator#FM} leg.
   *
   * @param airport input 424 airport record
   */
  F convertAirport(ArincAirport airport);

  /**
   * Converts the incoming 424 runway record into the user-defined type. It's not uncommon to see runways used as center fixes of
   * {@link PathTerminator#RF} or {@link PathTerminator#AF} legs.
   *
   * @param runway the input 424 runway record
   */
  F convertRunway(ArincRunway runway);

  /**
   * Converts the incoming 424 ILS/GLS record into the user-defined type. Similar to runways these are often used as the center
   * fixes of {@link PathTerminator#RF} or {@link PathTerminator#AF} legs.
   *
   * @param ilsGls the input 424 ILS/GLS record
   */
  F convertLocalizerGlideSlope(ArincLocalizerGlideSlope ilsGls);

  /**
   * Converts the incoming 424 GNSS landing system record into the user-defined type. Similar to runways these are often used as
   * the center fixes of {@link PathTerminator#RF} or {@link PathTerminator#AF} legs.
   *
   * @param gnss the input GNSS landing system record
   */
  F convertGnssLandingSystem(ArincGnssLandingSystem gnss);

  final class Standard implements FixAssemblyStrategy<Fix> {

    private Standard() {
    }

    @Override
    public Fix convertWaypoint(ArincWaypoint waypoint) {

      Instant cycleDate = AiracCycle.startDate(waypoint.lastUpdateCycle());

      MagneticVariation magneticVariation = waypoint.magneticVariation()
          .map(MagneticVariation::ofDegrees)
          .orElseGet(() -> magneticVariation(waypoint.latitude(), waypoint.longitude(), cycleDate));

      return Fix.builder()
          .fixIdentifier(waypoint.waypointIdentifier())
          .latLong(LatLong.of(waypoint.latitude(), waypoint.longitude()))
          .magneticVariation(magneticVariation)
          .build();
    }

    @Override
    public Fix convertNdbNavaid(ArincNdbNavaid navaid) {

      Instant cycleDate = AiracCycle.startDate(navaid.lastUpdateCycle());

      MagneticVariation magneticVariation = navaid.magneticVariation()
          .map(MagneticVariation::ofDegrees)
          .orElseGet(() -> magneticVariation(navaid.latitude(), navaid.longitude(), cycleDate));

      return Fix.builder()
          .fixIdentifier(navaid.ndbIdentifier())
          .latLong(LatLong.of(navaid.latitude(), navaid.longitude()))
          .magneticVariation(magneticVariation)
          .build();
    }

    @Override
    public Fix convertVhfNavaid(ArincVhfNavaid navaid) {

      Instant cycleDate = AiracCycle.startDate(navaid.lastUpdateCycle());

      return Fix.builder()
          .fixIdentifier(navaid.vhfIdentifier())
          .latLong(LatLong.of(navaid.latitude(), navaid.longitude()))
          .magneticVariation(magneticVariation(navaid.latitude(), navaid.longitude(), cycleDate))
          .build();
    }

    @Override
    public Fix convertAirport(ArincAirport airport) {

      Instant cycleDate = AiracCycle.startDate(airport.lastUpdateCycle());

      MagneticVariation magneticVariation = airport.magneticVariation()
          .map(MagneticVariation::ofDegrees)
          .orElseGet(() -> magneticVariation(airport.latitude(), airport.longitude(), cycleDate));

      return Fix.builder()
          .fixIdentifier(airport.airportIdentifier())
          .latLong(LatLong.of(airport.latitude(), airport.longitude()))
          .magneticVariation(magneticVariation)
          .build();
    }

    @Override
    public Fix convertRunway(ArincRunway runway) {

      Instant cycleDate = AiracCycle.startDate(runway.lastUpdateCycle());

      return Fix.builder()
          .fixIdentifier(runway.runwayIdentifier())
          .latLong(LatLong.of(runway.latitude(), runway.longitude()))
          .magneticVariation(magneticVariation(runway.latitude(), runway.longitude(), cycleDate))
          .build();
    }

    @Override
    public Fix convertLocalizerGlideSlope(ArincLocalizerGlideSlope ilsGls) {

      Instant cycleDate = AiracCycle.startDate(ilsGls.lastUpdateCycle());

      LatLong location = LatLong.of(
          ilsGls.localizerLatitude().or(ilsGls::glideSlopeLatitude).orElseThrow(() -> missingField("ILS/GLS Latitude")),
          ilsGls.localizerLongitude().or(ilsGls::glideSlopeLongitude).orElseThrow(() -> missingField("ILS/GLS Longitude"))
      );

      return Fix.builder()
          .fixIdentifier(ilsGls.localizerIdentifier())
          .latLong(location)
          .magneticVariation(magneticVariation(location.latitude(), location.longitude(), cycleDate))
          .build();
    }

    @Override
    public Fix convertGnssLandingSystem(ArincGnssLandingSystem gnss) {

      Instant cycleDate = AiracCycle.startDate(gnss.lastUpdatedCycle());

      return Fix.builder()
          .fixIdentifier(gnss.glsRefPathIdentifier())
          .latLong(LatLong.of(gnss.stationLatitude(), gnss.stationLongitude()))
          .magneticVariation(magneticVariation(gnss.stationLatitude(), gnss.stationLongitude(), cycleDate))
          .build();
    }

    private IllegalStateException missingField(String fieldName) {
      return new IllegalStateException("Missing required field: " + fieldName);
    }
  }
}
