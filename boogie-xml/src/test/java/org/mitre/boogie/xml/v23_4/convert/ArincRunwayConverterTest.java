package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincRunway;
import org.mitre.boogie.xml.model.fields.RunwayAccuracyCompliance;
import org.mitre.boogie.xml.model.fields.RunwayLeftRightCenterType;
import org.mitre.boogie.xml.model.fields.RunwaySurfaceCode;
import org.mitre.boogie.xml.model.fields.RunwayUsageIndicator;
import org.mitre.boogie.xml.v23_4.generated.Runway;

class ArincRunwayConverterTest {
  private final ArincRunwayConverter converter = ArincRunwayConverter.INSTANCE;

  @Test
  void nullRunwayReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(null));
  }

  @Test
  void invalidRunwayReturnsEmpty() {
    Runway runway = new Runway();
    assertEquals(Optional.empty(), converter.apply(runway));
  }

  @Test
  void validRunwayConverts() {
    Optional<ArincRunway> result = converter.apply(TestGeneratedObjects.newValidRunway());
    assertTrue(result.isPresent());

    ArincRunway rwy = result.get();

    assertAll(
        () -> assertNotNull(rwy.recordInfo()),
        () -> assertNotNull(rwy.pointInfo()),
        () -> assertEquals("K6", rwy.pointInfo().icaoCode()),
        () -> assertEquals("RW09L", rwy.pointInfo().identifier()),

        () -> assertEquals(Optional.of(false), rwy.isWithoutLocation()),
        () -> assertEquals(Optional.of(false), rwy.isDerivedLocation()),

        () -> assertEquals(Integer.valueOf(9), rwy.runwayNumber()),
        () -> assertEquals(Optional.of(RunwayLeftRightCenterType.LEFT), rwy.runwayLeftRightCenterType()),

        () -> assertEquals(Optional.of(10000L), rwy.runwayLength()),
        () -> assertEquals(Optional.of(150L), rwy.runwayWidth()),
        () -> assertEquals(90.5, rwy.runwayBearing().orElse(null), 0.001),
        () -> assertEquals(Optional.of(true), rwy.runwayBearingIsTrueBearing()),
        () -> assertEquals(90.5, rwy.runwayTrueBearing().orElse(null), 0.001),
        () -> assertEquals(0.3, rwy.runwayGradient().orElse(null), 0.001),
        () -> assertEquals(100.5, rwy.ltpEllipsoidHeight().orElse(null), 0.001),

        () -> assertEquals(Optional.of(313), rwy.landingThresholdElevation()),
        () -> assertEquals(Optional.of(1000L), rwy.displacedThresholdDistance()),
        () -> assertNotNull(rwy.runwayEndLocation().orElse(null)),
        () -> assertEquals(Optional.of(310), rwy.runwayEndElevation()),
        () -> assertEquals(Optional.of(500L), rwy.stopway()),
        () -> assertEquals(Optional.of(50L), rwy.thresholdCrossingHeight()),
        () -> assertEquals(Optional.of(312), rwy.touchDownZoneElevation()),
        () -> assertEquals(Optional.of(200), rwy.starterExtension()),

        () -> assertEquals(Optional.of(9500L), rwy.takeOffRunwayAvailable()),
        () -> assertEquals(Optional.of(9800L), rwy.takeOffDistanceAvailable()),
        () -> assertEquals(Optional.of(10000L), rwy.accelerateStopDistanceAvailable()),
        () -> assertEquals(Optional.of(9000L), rwy.landingDistanceAvailable()),

        () -> assertEquals(Optional.of(RunwaySurfaceCode.HARD), rwy.surfaceCode()),
        () -> assertEquals(Optional.of(RunwayUsageIndicator.TAKEOFF_AND_LANDING), rwy.runwayUsageIndicator()),

        () -> assertEquals(Optional.of(RunwayAccuracyCompliance.COMPLIANT), rwy.runwayAccuracyCompliance()),
        () -> assertEquals(Optional.of(org.mitre.boogie.xml.model.fields.LandingThresholdElevationAccuracyCompliance.COMPLIANT), rwy.landingThresholdElevationAccuracyCompliance()),

        () -> assertEquals(Optional.of("Main runway"), rwy.runwayDescription())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    Runway runway = TestGeneratedObjects.newValidRunway();
    runway.setRunwayBearing(null);
    runway.setRunwayEndLocation(null);
    runway.setSurfaceCode(null);
    runway.setRunwayAccuracy(null);
    runway.setRunwayLength(null);

    Optional<ArincRunway> result = converter.apply(runway);
    assertTrue(result.isPresent());

    ArincRunway rwy = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), rwy.runwayBearing()),
        () -> assertEquals(Optional.empty(), rwy.runwayBearingIsTrueBearing()),
        () -> assertEquals(Optional.empty(), rwy.runwayEndLocation()),
        () -> assertEquals(Optional.empty(), rwy.runwayAccuracyCompliance()),
        () -> assertEquals(Optional.empty(), rwy.landingThresholdElevationAccuracyCompliance()),
        () -> assertEquals(Optional.empty(), rwy.runwayLength())
    );
  }
}
