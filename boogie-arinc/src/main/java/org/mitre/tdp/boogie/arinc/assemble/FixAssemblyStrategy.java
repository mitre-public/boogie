package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.AiracCycle;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincModel;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;

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
   * Returns a new implementation of a {@link FixAssemblyStrategy} decorating the provided delegate strategy with a thin layer
   * of caching such that when the same input object is provided for conversion, the previously assembled version of it will be
   * used if it's already been converted once.
   *
   * <p>This helps keep down memory when assembling procedures and airways which reference the same underlying fix multiple times
   * in multiple different records.
   *
   * @param delegate the delegate strategy to use to build the cached fixes
   */
  static <F> FixAssemblyStrategy<F> caching(FixAssemblyStrategy<F> delegate) {
    requireNonNull(delegate, "Delegate strategy cannot be null.");
    return delegate instanceof Caching ? delegate : new Caching<>(delegate);
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

  /**
   * Converts the incoming 424 Helipad record to the user defined type.
   * @param helipad the arinc 424 helipad
   * @return the user defined type
   */
  F convertHelipad(ArincHelipad helipad);

  final class Standard implements FixAssemblyStrategy<Fix> {

    private Standard() {
    }

    @Override
    public Fix convertWaypoint(ArincWaypoint waypoint) {

      Instant cycleDate = AiracCycle.startDate(waypoint.lastUpdateCycle());

      MagneticVariation magneticVariation = waypoint.magneticVariation()
          .map(MagneticVariation::ofDegrees)
          .orElseGet(() -> MagneticVariation.from(waypoint.latitude(), waypoint.longitude(), cycleDate));

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
          .orElseGet(() -> MagneticVariation.from(navaid.latitude(), navaid.longitude(), cycleDate));

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
          .magneticVariation(MagneticVariation.from(navaid.latitude(), navaid.longitude(), cycleDate))
          .build();
    }

    @Override
    public Fix convertAirport(ArincAirport airport) {

      Instant cycleDate = AiracCycle.startDate(airport.lastUpdateCycle());

      MagneticVariation magneticVariation = airport.magneticVariation()
          .map(MagneticVariation::ofDegrees)
          .orElseGet(() -> MagneticVariation.from(airport.latitude(), airport.longitude(), cycleDate));

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
          .magneticVariation(MagneticVariation.from(runway.latitude(), runway.longitude(), cycleDate))
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
          .magneticVariation(MagneticVariation.from(location.latitude(), location.longitude(), cycleDate))
          .build();
    }

    @Override
    public Fix convertGnssLandingSystem(ArincGnssLandingSystem gnss) {

      Instant cycleDate = AiracCycle.startDate(gnss.lastUpdatedCycle());

      return Fix.builder()
          .fixIdentifier(gnss.glsRefPathIdentifier())
          .latLong(LatLong.of(gnss.stationLatitude(), gnss.stationLongitude()))
          .magneticVariation(MagneticVariation.from(gnss.stationLatitude(), gnss.stationLongitude(), cycleDate))
          .build();
    }

    @Override
    public Fix convertHelipad(ArincHelipad helipad) {
      Instant cycleDate = AiracCycle.startDate(helipad.cycle());
      return Fix.builder()
          .fixIdentifier(helipad.helipadIdentifier())
          .latLong(LatLong.of(helipad.latitude(), helipad.longitude()))
          .magneticVariation(MagneticVariation.from(helipad.latitude(), helipad.longitude(), cycleDate))
          .build();
    }

    @Override
    public Fix convertHelipad(ArincHelipad helipad) {
      Instant cycleDate = AiracCycle.startDate(helipad.cycle());
      return Fix.builder()
          .fixIdentifier(helipad.helipadIdentifier())
          .latLong(LatLong.of(helipad.latitude(), helipad.longitude()))
          .magneticVariation(magneticVariation(helipad.latitude(), helipad.longitude(), cycleDate))
          .build();
    }

    private IllegalStateException missingField(String fieldName) {
      return new IllegalStateException("Missing required field: " + fieldName);
    }
  }

  final class Caching<F> implements FixAssemblyStrategy<F> {

    private final FixAssemblyStrategy<F> delegate;

    private final ConcurrentHashMap<ArincModel, F> cache;

    private Caching(FixAssemblyStrategy<F> delegate) {
      this.delegate = requireNonNull(delegate);
      this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public F convertWaypoint(ArincWaypoint waypoint) {
      return cache.computeIfAbsent(waypoint, w -> delegate.convertWaypoint((ArincWaypoint) w));
    }

    @Override
    public F convertNdbNavaid(ArincNdbNavaid navaid) {
      return cache.computeIfAbsent(navaid, n -> delegate.convertNdbNavaid((ArincNdbNavaid) n));
    }

    @Override
    public F convertVhfNavaid(ArincVhfNavaid navaid) {
      return cache.computeIfAbsent(navaid, n -> delegate.convertVhfNavaid((ArincVhfNavaid) n));
    }

    @Override
    public F convertAirport(ArincAirport airport) {
      return cache.computeIfAbsent(airport, a -> delegate.convertAirport((ArincAirport) a));
    }

    @Override
    public F convertRunway(ArincRunway runway) {
      return cache.computeIfAbsent(runway, r -> delegate.convertRunway((ArincRunway) r));
    }

    @Override
    public F convertLocalizerGlideSlope(ArincLocalizerGlideSlope ilsGls) {
      return cache.computeIfAbsent(ilsGls, i -> delegate.convertLocalizerGlideSlope((ArincLocalizerGlideSlope) i));
    }

    @Override
    public F convertGnssLandingSystem(ArincGnssLandingSystem gnss) {
      return cache.computeIfAbsent(gnss, g -> delegate.convertGnssLandingSystem((ArincGnssLandingSystem) g));
    }

    @Override
    public F convertHelipad(ArincHelipad helipad) {
      return cache.computeIfAbsent(helipad, h -> delegate.convertHelipad((ArincHelipad) h));
    }
  }
}
