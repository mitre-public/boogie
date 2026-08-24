package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.ReferencedCourse;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.v18.AirportConverter;
import org.mitre.tdp.boogie.arinc.v18.AirportSpec;
import org.mitre.tdp.boogie.arinc.v18.RunwayConverter;
import org.mitre.tdp.boogie.arinc.v18.RunwaySpec;

class TestAirportAssemblyStrategy {

  private static final AirportAssemblyStrategy<Airport, Runway, Helipad> STRATEGY = AirportAssemblyStrategy.standard();

  private static final String CYYH = "SCANP CYYHCYAYYH     110000040YSN69324794W093343728T000000090250      1800018000CT00YTWGE    TALOYOAK                      113812605";
  private static final String CYYH_RW15 = "SCANP CYYHCYGRW15    104009151T N69330518W093350470-0873          000900000  100R     0000     050                         110002705";

  @Test
  void trueBearingFromCyyhRemainsTrueThroughAssembly() {
    ArincAirport airport = new AirportConverter().apply(
        ArincRecordParser.standard(new AirportSpec()).parse(CYYH).orElseThrow()
    ).orElseThrow();
    ArincRunway runway = new RunwayConverter().apply(
        ArincRecordParser.standard(new RunwaySpec()).parse(CYYH_RW15).orElseThrow()
    ).orElseThrow();

    Runway result = STRATEGY.convertRunway(airport, runway, null, null, null);

    assertAll(
        () -> assertEquals(ReferencedCourse.trueCourse(151.0), runway.runwayBearing().orElseThrow()),
        () -> assertEquals(151.0, result.course().orElseThrow().inDegrees(), 0.0001)
    );
  }

  @Test
  void trueBearingDoesNotUseAirportMagneticVariation() {
    ArincAirport airport = airport(12.0);
    ArincRunway runway = runway("RW15", LatLong.of(69.5514, -93.5846), ReferencedCourse.trueCourse(151.0));

    Runway result = STRATEGY.convertRunway(airport, runway, null, null, null);

    assertEquals(151.0, result.course().orElseThrow().inDegrees(), 0.0001);
  }

  @Test
  void magneticBearingUsesAirportMagneticVariation() {
    ArincAirport airport = airport(12.0);
    ArincRunway runway = runway("RW15", LatLong.of(69.5514, -93.5846), ReferencedCourse.magnetic(151.0));

    Runway result = STRATEGY.convertRunway(airport, runway, null, null, null);

    assertEquals(163.0, result.course().orElseThrow().inDegrees(), 0.0001);
  }

  @Test
  void coordinateDerivedFallbackIsAlreadyTrue() {
    ArincAirport airport = airport(12.0);
    LatLong originPosition = LatLong.of(42.2130, -83.3600);
    LatLong reciprocalPosition = LatLong.of(42.2130, -83.3400);
    ArincRunway origin = runway("RW09L", originPosition, null);
    ArincRunway reciprocal = runway("RW27R", reciprocalPosition, null);
    Course expected = originPosition.courseTo(reciprocalPosition);

    Runway result = STRATEGY.convertRunway(airport, origin, reciprocal, null, null);

    assertEquals(expected.inDegrees(), result.course().orElseThrow().inDegrees(), 0.0001);
  }

  private static ArincAirport airport(double magneticVariation) {
    return new ArincAirport.Builder()
        .airportIdentifier("TEST")
        .latitude(42.2124)
        .longitude(-83.3534)
        .magneticVariation(magneticVariation)
        .fileRecordNumber(1)
        .lastUpdateCycle("2608")
        .build();
  }

  private static ArincRunway runway(String identifier, LatLong position, ReferencedCourse bearing) {
    return new ArincRunway.Builder()
        .runwayIdentifier(identifier)
        .runwayBearing(bearing)
        .latitude(position.latitude())
        .longitude(position.longitude())
        .fileRecordNumber(1)
        .lastUpdateCycle("2608")
        .build();
  }
}
