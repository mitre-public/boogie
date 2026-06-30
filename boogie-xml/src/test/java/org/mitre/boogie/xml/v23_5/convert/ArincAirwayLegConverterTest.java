package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincAirwayLeg;
import org.mitre.boogie.xml.v23_5.generated.AirwayLeg;

class ArincAirwayLegConverterTest {

  private static final ArincAirwayLegConverter CONVERTER = ArincAirwayLegConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    assertEquals(Optional.empty(), CONVERTER.apply(null));
  }

  @Test
  void invalidReturnsEmpty() {
    AirwayLeg leg = new AirwayLeg();
    assertEquals(Optional.empty(), CONVERTER.apply(leg));
  }

  @Test
  void validLegConverts() {
    Optional<ArincAirwayLeg> result = CONVERTER.apply(TestGeneratedObjects.newValidAirwayLeg(10, "WYPT1"));
    assertTrue(result.isPresent());

    ArincAirwayLeg leg = result.get();
    assertAll(
        () -> assertNotNull(leg.recordInfo()),
        () -> assertEquals(10, leg.sequenceNumber()),
        () -> assertEquals(Optional.of("FIX-WYPT1"), leg.fixRef()),
        () -> assertEquals(Optional.of("WYPT1"), leg.fixIdent()),
        () -> assertEquals(Optional.of("DCA"), leg.recNavaidIdent()),
        () -> assertEquals(Optional.of("NAVAID-DCA"), leg.recNavaidRef()),
        () -> assertEquals(Optional.of("CRUISE-TBL-1"), leg.cruiseTableRef()),
        () -> assertEquals(Optional.of("OFFICIALLY_DESIGNATED_EXCEPT_RNAV_HELICOPTER"), leg.airwayRouteType()),
        () -> assertEquals(Optional.of("HIGH_ALT"), leg.level()),
        () -> assertEquals(Optional.of("ONE_WAY_FORWARD"), leg.legDirectionRestriction()),
        () -> assertEquals(90.5, leg.outboundCourseValue().orElse(null), 0.001),
        () -> assertEquals(Optional.of(true), leg.outboundCourseIsTrue()),
        () -> assertEquals(270.5, leg.inboundCourseValue().orElse(null), 0.001),
        () -> assertEquals(Optional.of(false), leg.inboundCourseIsTrue()),
        () -> assertEquals(125.3, leg.routeDistanceFrom().orElse(null), 0.001),
        () -> assertEquals(50.0, leg.rho().orElse(null), 0.001),
        () -> assertEquals(180.0, leg.theta().orElse(null), 0.001),
        () -> assertEquals(2.0, leg.rnp().orElse(null), 0.001),
        () -> assertEquals(Optional.of(100L), leg.verticalScaleFactor())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    AirwayLeg leg = TestGeneratedObjects.newValidAirwayLeg(10, "WYPT1");
    leg.setInboundCourse(null);
    leg.setOutboundCourse(null);
    leg.setRho(null);
    leg.setTheta(null);
    leg.setRnp(null);
    leg.setLevel(null);
    leg.setVerticalScaleFactor(null);
    leg.setFixRef(null);
    leg.setRecNavaidRef(null);
    leg.setCruiseTableRef(null);
    leg.setRouteDistanceFrom(null);

    Optional<ArincAirwayLeg> result = CONVERTER.apply(leg);
    assertTrue(result.isPresent());

    ArincAirwayLeg converted = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), converted.inboundCourseValue()),
        () -> assertEquals(Optional.empty(), converted.inboundCourseIsTrue()),
        () -> assertEquals(Optional.empty(), converted.outboundCourseValue()),
        () -> assertEquals(Optional.empty(), converted.outboundCourseIsTrue()),
        () -> assertEquals(Optional.empty(), converted.rho()),
        () -> assertEquals(Optional.empty(), converted.theta()),
        () -> assertEquals(Optional.empty(), converted.rnp()),
        () -> assertEquals(Optional.empty(), converted.level()),
        () -> assertEquals(Optional.empty(), converted.verticalScaleFactor()),
        () -> assertEquals(Optional.empty(), converted.routeDistanceFrom()),
        () -> assertEquals(Optional.empty(), converted.fixRef()),
        () -> assertEquals(Optional.empty(), converted.recNavaidRef()),
        () -> assertEquals(Optional.empty(), converted.cruiseTableRef())
    );
  }
}
