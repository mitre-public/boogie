package org.mitre.boogie.xml.assemble;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.boogie.xml.util.MagneticVariationResolver;
import org.mitre.boogie.xml.model.ArincAirportGate;
import org.mitre.boogie.xml.model.ArincGnssLandingSystem;
import org.mitre.boogie.xml.model.ArincHelipad;
import org.mitre.boogie.xml.model.ArincLocalizerGlideSlope;
import org.mitre.boogie.xml.model.ArincLocalizerGlideslopeMarker;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincRunway;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;

/**
 * Strategy class for converting XML model fix-like record types into client-defined fix data models.
 *
 * <p>The input types are the record types which can be referenced as fixes in airway/procedure leg definitions.
 *
 * <p>This class can be used with {@link FixAssemblyStrategy#standard()} to generate lightweight Boogie-defined {@link Fix}
 * implementations.
 */
public interface FixAssemblyStrategy<F> {

  static FixAssemblyStrategy<Fix> standard() {
    return new Standard();
  }

  F convertWaypoint(ArincWaypoint waypoint);

  F convertNdbNavaid(ArincNdbNavaid navaid);

  F convertVhfNavaid(ArincVhfNavaid navaid);

  /**
   * Converts a generic point-info-bearing record into a fix. Used as a fallback for record types that have a
   * {@link ArincPointInfo} but no dedicated converter override.
   */
  F convertPoint(ArincPointInfo pointInfo, ArincRecordInfo recordInfo);

  F convertAirport(ArincPortInfo airport);

  F convertRunway(ArincRunway runway);

  F convertAirportGate(ArincAirportGate gate);

  F convertHelipad(ArincHelipad helipad);

  F convertLocalizerGlideSlope(ArincLocalizerGlideSlope localizerGlideSlope);

  F convertMarker(ArincLocalizerGlideslopeMarker marker);

  F convertGnssLandingSystem(ArincGnssLandingSystem gnssLandingSystem);

  final class Standard implements FixAssemblyStrategy<Fix> {

    private Standard() {
    }

    @Override
    public Fix convertWaypoint(ArincWaypoint waypoint) {
      ArincPointInfo point = waypoint.pointInfo();
      MagneticVariation magneticVariation = MagneticVariationResolver.INSTANCE.apply(point, waypoint.recordInfo().cycleDate());

      return Fix.builder()
          .fixIdentifier(point.identifier())
          .latLong(point.latLong())
          .magneticVariation(magneticVariation)
          .build();
    }

    @Override
    public Fix convertNdbNavaid(ArincNdbNavaid navaid) {
      ArincPointInfo point = navaid.pointInfo();
      MagneticVariation magneticVariation = MagneticVariationResolver.INSTANCE.apply(point, navaid.recordInfo().cycleDate());

      return Fix.builder()
          .fixIdentifier(point.identifier())
          .latLong(point.latLong())
          .magneticVariation(magneticVariation)
          .build();
    }

    @Override
    public Fix convertPoint(ArincPointInfo point, ArincRecordInfo recordInfo) {
      MagneticVariation magneticVariation = MagneticVariationResolver.INSTANCE.apply(point, recordInfo.cycleDate());

      return Fix.builder()
          .fixIdentifier(point.identifier())
          .latLong(point.latLong())
          .magneticVariation(magneticVariation)
          .build();
    }

    @Override
    public Fix convertVhfNavaid(ArincVhfNavaid navaid) {
      ArincPointInfo point = navaid.pointInfo();

      MagneticVariation magneticVariation = navaid.stationDeclination()
          .or(point::magneticVariation)
          .orElseGet(() -> MagneticVariationResolver.INSTANCE.apply(point, navaid.recordInfo().cycleDate()));

      return Fix.builder()
          .fixIdentifier(point.identifier())
          .latLong(point.latLong())
          .magneticVariation(magneticVariation)
          .build();
    }

    @Override
    public Fix convertAirport(ArincPortInfo airport) {
      return convertPoint(airport.pointInfo(), airport.recordInfo());
    }

    @Override
    public Fix convertRunway(ArincRunway runway) {
      return convertPoint(runway.pointInfo(), runway.recordInfo());
    }

    @Override
    public Fix convertAirportGate(ArincAirportGate gate) {
      return convertPoint(gate.pointInfo(), gate.recordInfo());
    }

    @Override
    public Fix convertHelipad(ArincHelipad helipad) {
      return convertPoint(helipad.pointInfo(), helipad.recordInfo());
    }

    @Override
    public Fix convertLocalizerGlideSlope(ArincLocalizerGlideSlope localizerGlideSlope) {
      return convertPoint(localizerGlideSlope.pointInfo(), localizerGlideSlope.recordInfo());
    }

    @Override
    public Fix convertMarker(ArincLocalizerGlideslopeMarker marker) {
      return convertPoint(marker.pointInfo(), marker.recordInfo());
    }

    @Override
    public Fix convertGnssLandingSystem(ArincGnssLandingSystem gnssLandingSystem) {
      return convertPoint(gnssLandingSystem.pointInfo(), gnssLandingSystem.recordInfo());
    }
  }
}
