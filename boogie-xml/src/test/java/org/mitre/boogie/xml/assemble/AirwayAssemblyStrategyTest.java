package org.mitre.boogie.xml.assemble;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.model.ArincAirwayLeg;

class AirwayAssemblyStrategyTest {

  private static final AirwayAssemblyStrategy<Airway, Fix, Leg> STRATEGY = AirwayAssemblyStrategy.standard();

  @Test
  void convertAirway_setsIdentifierAndLegs() {
    ArincAirway airway = ArincAirway.builder()
        .identifier("J60")
        .airwayRouteType("J")
        .build();

    Leg leg1 = Leg.builder(PathTerminator.TF, 10).build();
    Leg leg2 = Leg.builder(PathTerminator.TF, 20).build();

    Airway result = STRATEGY.convertAirway(airway, List.of(leg1, leg2));

    assertAll(
        () -> assertEquals("J60", result.airwayIdentifier()),
        () -> assertEquals(2, result.legs().size())
    );
  }

  @Test
  void convertLeg_withAssociatedFixAndNavaid() {
    ArincAirwayLeg leg = ArincAirwayLeg.builder()
        .sequenceNumber(10)
        .fixIdent("JMACK")
        .recNavaidIdent("DTW")
        .outboundCourseValue(90.5)
        .theta(45.0)
        .rho(10.0)
        .rnp(2.0)
        .routeDistanceFrom(25.0)
        .build();

    Fix associatedFix = Fix.builder()
        .fixIdentifier("JMACK")
        .latLong(LatLong.of(42.0, -83.0))
        .magneticVariation(MagneticVariation.ofDegrees(-5.0))
        .build();

    Fix recNavaid = Fix.builder()
        .fixIdentifier("DTW")
        .latLong(LatLong.of(42.2, -83.3))
        .magneticVariation(MagneticVariation.ofDegrees(-5.0))
        .build();

    Leg result = STRATEGY.convertLeg(leg, associatedFix, recNavaid);

    assertAll(
        () -> assertEquals(PathTerminator.TF, result.pathTerminator()),
        () -> assertEquals(10, result.sequenceNumber()),
        () -> assertEquals("JMACK", result.associatedFix().orElseThrow().fixIdentifier()),
        () -> assertEquals("DTW", result.recommendedNavaid().orElseThrow().fixIdentifier()),
        () -> assertEquals(90.5, result.outboundMagneticCourse().orElseThrow(), 0.01),
        () -> assertEquals(45.0, result.theta().orElseThrow(), 0.01),
        () -> assertEquals(10.0, result.rho().orElseThrow(), 0.01),
        () -> assertEquals(2.0, result.rnp().orElseThrow(), 0.01),
        () -> assertEquals(25.0, result.routeDistance().orElseThrow(), 0.01)
    );
  }

  @Test
  void convertLeg_withNullFixes() {
    ArincAirwayLeg leg = ArincAirwayLeg.builder()
        .sequenceNumber(20)
        .build();

    Leg result = STRATEGY.convertLeg(leg, null, null);

    assertAll(
        () -> assertEquals(20, result.sequenceNumber()),
        () -> assertTrue(result.associatedFix().isEmpty()),
        () -> assertTrue(result.recommendedNavaid().isEmpty())
    );
  }
}
