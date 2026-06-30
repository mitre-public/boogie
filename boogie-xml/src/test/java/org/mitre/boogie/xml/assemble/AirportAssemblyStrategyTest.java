package org.mitre.boogie.xml.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincHelipad;
import org.mitre.boogie.xml.model.ArincRunway;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordType;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.Runway;

class AirportAssemblyStrategyTest {

  private static final AirportAssemblyStrategy<Airport, Runway, Helipad> STRATEGY = AirportAssemblyStrategy.standard();

  private static final LatLong AIRPORT_POSITION = LatLong.of(42.2124, -83.3534);
  private static final LatLong RWY09L_POSITION = LatLong.of(42.2130, -83.3600);
  private static final LatLong RWY27R_POSITION = LatLong.of(42.2130, -83.3400);
  private static final MagneticVariation MAG_VAR = MagneticVariation.ofDegrees(-5.0);
  private static final String CYCLE = "2504";

  @Test
  void convertAirport_basic() {
    ArincAirport airport = testAirport("KDTW", AIRPORT_POSITION);

    Runway rwy = Runway.builder().runwayIdentifier("RW09L").origin(RWY09L_POSITION).build();
    Helipad pad = Helipad.builder().padIdentifier("H1").origin(AIRPORT_POSITION).build();

    Airport result = STRATEGY.convertAirport(airport, List.of(rwy), List.of(pad));

    assertAll(
        () -> assertEquals("KDTW", result.airportIdentifier()),
        () -> assertEquals(AIRPORT_POSITION, result.latLong()),
        () -> assertTrue(result.magneticVariation().isPresent()),
        () -> assertEquals(1, result.runways().size()),
        () -> assertEquals(1, result.helipads().size())
    );
  }

  @Test
  void convertRunway_withTrueBearing() {
    ArincAirport airport = testAirport("KDTW", AIRPORT_POSITION);
    ArincRunway origin = testRunway("RW09L", RWY09L_POSITION, 90.0, true, 90.0, 10000L);
    ArincRunway reciprocal = testRunway("RW27R", RWY27R_POSITION, 270.0, true, 270.0, 10000L);

    Runway result = STRATEGY.convertRunway(airport, origin, reciprocal, null, null);

    assertAll(
        () -> assertEquals("RW09L", result.runwayIdentifier()),
        () -> assertEquals(RWY09L_POSITION, result.origin()),
        () -> assertTrue(result.course().isPresent()),
        () -> assertEquals(90.0, result.course().get().inDegrees(), 0.01),
        () -> assertTrue(result.length().isPresent())
    );
  }

  @Test
  void convertRunway_withMagneticBearing() {
    ArincAirport airport = testAirport("KDTW", AIRPORT_POSITION);
    ArincRunway origin = testRunway("RW09L", RWY09L_POSITION, 95.0, false, null, 10000L);

    Runway result = STRATEGY.convertRunway(airport, origin, null, null, null);

    assertAll(
        () -> assertEquals("RW09L", result.runwayIdentifier()),
        () -> assertTrue(result.course().isPresent(), "Should convert magnetic bearing to true")
    );
  }

  @Test
  void convertRunway_noRunwayBearing_fallsBackToReciprocal() {
    ArincAirport airport = testAirport("KDTW", AIRPORT_POSITION);
    ArincRunway origin = testRunway("RW09L", RWY09L_POSITION, null, null, null, 10000L);
    ArincRunway reciprocal = testRunway("RW27R", RWY27R_POSITION, null, null, null, 10000L);

    Runway result = STRATEGY.convertRunway(airport, origin, reciprocal, null, null);

    assertAll(
        () -> assertEquals("RW09L", result.runwayIdentifier()),
        () -> assertTrue(result.course().isPresent(), "Should compute course from origin to reciprocal")
    );
  }

  @Test
  void convertRunway_noBearingNoReciprocal() {
    ArincAirport airport = testAirport("KDTW", AIRPORT_POSITION);
    ArincRunway origin = testRunway("RW09L", RWY09L_POSITION, null, null, null, null);

    Runway result = STRATEGY.convertRunway(airport, origin, null, null, null);

    assertAll(
        () -> assertEquals("RW09L", result.runwayIdentifier()),
        () -> assertTrue(result.course().isEmpty(), "No bearing info and no reciprocal"),
        () -> assertTrue(result.length().isEmpty())
    );
  }

  @Test
  void convertHelipad() {
    ArincHelipad pad = testHelipad("H1", AIRPORT_POSITION);

    Helipad result = STRATEGY.convertHelipad(pad);

    assertAll(
        () -> assertEquals("H1", result.padIdentifier()),
        () -> assertEquals(AIRPORT_POSITION, result.origin())
    );
  }

  private static ArincAirport testAirport(String identifier, LatLong position) {
    ArincPortInfo portInfo = ArincPortInfo.builder()
        .pointInfo(testPointInfo(identifier, position))
        .recordInfo(testRecordInfo())
        .build();

    return ArincAirport.builder()
        .portInfo(portInfo)
        .build();
  }

  private static ArincRunway testRunway(String identifier, LatLong position, Double bearing, Boolean isTrueBearing, Double trueBearing, Long length) {
    return ArincRunway.builder()
        .pointInfo(testPointInfo(identifier, position))
        .recordInfo(testRecordInfo())
        .runwayBearing(bearing)
        .runwayBearingIsTrueBearing(isTrueBearing)
        .runwayTrueBearing(trueBearing)
        .runwayLength(length)
        .build();
  }

  private static ArincHelipad testHelipad(String identifier, LatLong position) {
    return ArincHelipad.builder()
        .pointInfo(testPointInfo(identifier, position))
        .recordInfo(testRecordInfo())
        .build();
  }

  private static ArincPointInfo testPointInfo(String identifier, LatLong position) {
    return ArincPointInfo.builder()
        .identifier(identifier)
        .icaoCode("K6")
        .latLong(position)
        .magneticVariation(MAG_VAR)
        .referenceId(identifier)
        .build();
  }

  private static ArincRecordInfo testRecordInfo() {
    return ArincRecordInfo.builder()
        .cycleDate(CYCLE)
        .recordType(ArincRecordType.STANDARD)
        .build();
  }
}
