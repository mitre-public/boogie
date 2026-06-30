package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincProcedureLeg;
import org.mitre.boogie.xml.v23_4.generated.ApproachLeg;
import org.mitre.boogie.xml.v23_4.generated.SidLeg;

class ArincProcedureLegConverterTest {

  private static final ArincProcedureLegConverter CONVERTER = ArincProcedureLegConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    assertEquals(Optional.empty(), CONVERTER.apply(null));
  }

  @Test
  void invalidReturnsEmpty() {
    SidLeg leg = new SidLeg();
    assertEquals(Optional.empty(), CONVERTER.apply(leg));
  }

  @Test
  void convertsMinimalSidLeg() {
    Optional<ArincProcedureLeg> result = CONVERTER.apply(TestGeneratedObjects.newValidSidLeg(10));
    assertTrue(result.isPresent());

    ArincProcedureLeg leg = result.get();
    assertAll(
        () -> assertNotNull(leg.recordInfo()),
        () -> assertEquals(10, leg.sequenceNumber()),
        () -> assertEquals("TF", leg.pathAndTermination())
    );
  }

  @Test
  void convertsFullSidLeg() {
    Optional<ArincProcedureLeg> result = CONVERTER.apply(TestGeneratedObjects.newFullSidLeg(10));
    assertTrue(result.isPresent());

    ArincProcedureLeg leg = result.get();
    assertAll(
        // Leg fields
        () -> assertNotNull(leg.recordInfo()),
        () -> assertEquals(10, leg.sequenceNumber()),
        () -> assertEquals(Optional.of("FIX-WYPT1"), leg.fixRef()),
        () -> assertEquals(Optional.of("WYPT1"), leg.fixIdent()),
        () -> assertEquals(Optional.of("DCA"), leg.recNavaidIdent()),
        () -> assertEquals(Optional.of("NAVAID-DCA"), leg.recNavaidRef()),
        // ProcedureLeg fields
        () -> assertEquals("TF", leg.pathAndTermination()),
        () -> assertEquals(Optional.of("LEFT"), leg.turnDirection()),
        () -> assertEquals(Optional.of(true), leg.isTurnDirectionValid()),
        () -> assertEquals(5.0, leg.arcRadius().orElse(null), 0.001),
        () -> assertEquals(Optional.of("MODIFIED_OR_ASSIGNED"), leg.atcIndicator()),
        () -> assertEquals(10.5, leg.distance().orElse(null), 0.001),
        () -> assertEquals(12.3, leg.legDistance().orElse(null), 0.001),
        () -> assertEquals(Optional.of("A"), leg.legInboundIndicator()),
        () -> assertEquals(Optional.of("INBOUND"), leg.legInboundOutboundIndicator()),
        () -> assertEquals(50.0, leg.rho().orElse(null), 0.001),
        () -> assertEquals(2.0, leg.rnp().orElse(null), 0.001),
        () -> assertEquals(180.0, leg.theta().orElse(null), 0.001),
        () -> assertEquals(90.5, leg.courseValue().orElse(null), 0.001),
        () -> assertEquals(Optional.of(true), leg.courseIsTrue()),
        () -> assertEquals(Optional.of("CTR01"), leg.centerFix()),
        () -> assertEquals(Optional.of("CTRFIX-01"), leg.centerFixRef()),
        () -> assertEquals(Optional.of(18000), leg.transitionsAltitudeLevel()),
        () -> assertEquals(Optional.of(100L), leg.verticalScaleFactor()),
        () -> assertEquals(Optional.of("PT4M"), leg.holdTime()),
        // MagneticVariation
        () -> assertEquals(Optional.of("WEST"), leg.procedureDesignMagVarDirection()),
        () -> assertEquals(3.5, leg.procedureDesignMagVarValue().orElse(null), 0.001),
        // SpeedLimit
        () -> assertEquals(Optional.of(250L), leg.speedLimitAt()),
        () -> assertEquals(Optional.of(200L), leg.speedLimitAtOrAbove()),
        () -> assertEquals(Optional.of(300L), leg.speedLimitAtOrBelow()),
        // AltitudeConstraint (FL 40 = 4000, FL 80 = 8000, 5000 MSL = 5000)
        () -> assertEquals(Optional.of(5000), leg.altitudeConstraintAt()),
        () -> assertEquals(Optional.of(4000), leg.altitudeConstraintAtOrAbove()),
        () -> assertEquals(Optional.of(8000), leg.altitudeConstraintAtOrBelow()),
        // AltitudeTermination (FL 60 = 6000)
        () -> assertEquals(Optional.of("LEG_TERMINATION"), leg.altitudeTerminationType()),
        () -> assertEquals(Optional.of(6000), leg.altitudeTerminationValue()),
        // RaceTrackAltitude (3000 MSL)
        () -> assertEquals(Optional.of(3000), leg.raceTrackAltitude()),
        // ProcedureWaypointDescription
        () -> assertEquals(Optional.of(true), leg.isEssential()),
        () -> assertEquals(Optional.of(false), leg.isFlyOver()),
        () -> assertEquals(Optional.of(true), leg.isHolding()),
        () -> assertEquals(Optional.of(false), leg.isNoProcedureTurn()),
        () -> assertEquals(Optional.of(false), leg.isPhantomFix()),
        () -> assertEquals(Optional.of(true), leg.isSourceProvidedEnrouteWaypoint()),
        () -> assertEquals(Optional.of(false), leg.isTaaProcedureTurn()),
        () -> assertEquals(Optional.of(true), leg.isAtcCompulsoryReportingPoint()),
        // SidLeg-specific fields
        () -> assertEquals(Optional.of(true), leg.isEngineOutDisarmPoint()),
        () -> assertEquals(Optional.of(false), leg.isInitialDepartureFix()),
        () -> assertEquals(Optional.of(true), leg.isQuietClimbRestorePoint()),
        // ApproachLeg fields should be empty for a SidLeg
        () -> assertEquals(Optional.empty(), leg.verticalAngle()),
        () -> assertEquals(Optional.empty(), leg.glideSlopeCrossingAltitude()),
        () -> assertEquals(Optional.empty(), leg.isFacf()),
        () -> assertEquals(Optional.empty(), leg.isFaf())
    );
  }

  @Test
  void convertsFullStarLeg() {
    Optional<ArincProcedureLeg> result = CONVERTER.apply(TestGeneratedObjects.newFullStarLeg(20));
    assertTrue(result.isPresent());

    ArincProcedureLeg leg = result.get();
    assertAll(
        () -> assertEquals(20, leg.sequenceNumber()),
        () -> assertEquals("TF", leg.pathAndTermination()),
        () -> assertEquals(Optional.of("WYPT1"), leg.fixIdent()),
        // StarLeg-specific field
        () -> assertEquals(-3.0, leg.verticalAngle().orElse(null), 0.001),
        // SidLeg fields should be empty for a StarLeg
        () -> assertEquals(Optional.empty(), leg.isEngineOutDisarmPoint()),
        () -> assertEquals(Optional.empty(), leg.isInitialDepartureFix()),
        () -> assertEquals(Optional.empty(), leg.isQuietClimbRestorePoint()),
        // ApproachLeg fields should be empty
        () -> assertEquals(Optional.empty(), leg.glideSlopeCrossingAltitude()),
        () -> assertEquals(Optional.empty(), leg.isFacf())
    );
  }

  @Test
  void convertsFullApproachLeg() {
    Optional<ArincProcedureLeg> result = CONVERTER.apply(TestGeneratedObjects.newFullApproachLeg(30));
    assertTrue(result.isPresent());

    ArincProcedureLeg leg = result.get();
    assertAll(
        () -> assertEquals(30, leg.sequenceNumber()),
        () -> assertEquals("TF", leg.pathAndTermination()),
        () -> assertEquals(Optional.of("WYPT1"), leg.fixIdent()),
        // ApproachLeg-specific fields
        () -> assertEquals(-3.0, leg.verticalAngle().orElse(null), 0.001),
        () -> assertEquals(Optional.of(200), leg.glideSlopeCrossingAltitude()),
        // ApproachWaypointDescription
        () -> assertEquals(Optional.of(true), leg.isFacf()),
        () -> assertEquals(Optional.of(false), leg.isMissedApproachPoint()),
        () -> assertEquals(Optional.of(true), leg.isFaf()),
        () -> assertEquals(Optional.of(false), leg.isFinalEndPoint()),
        () -> assertEquals(Optional.of(true), leg.isFixTurningFinalApproach()),
        () -> assertEquals(Optional.of(false), leg.isInitialApproachFix()),
        () -> assertEquals(Optional.of(true), leg.isIntermediateApproachFix()),
        () -> assertEquals(Optional.of("UNNAMED_IN_FINAL_APPROACH_SEGMENT"), leg.stepDownFix()),
        // SidLeg fields should be empty for an ApproachLeg
        () -> assertEquals(Optional.empty(), leg.isEngineOutDisarmPoint()),
        () -> assertEquals(Optional.empty(), leg.isInitialDepartureFix()),
        () -> assertEquals(Optional.empty(), leg.isQuietClimbRestorePoint())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    SidLeg leg = TestGeneratedObjects.newFullSidLeg(10);
    leg.setCourse(null);
    leg.setFixRef(null);
    leg.setRecNavaidRef(null);
    leg.setTurnDirection(null);
    leg.setArcRadius(null);
    leg.setAtcIndicator(null);
    leg.setDistance(null);
    leg.setLegDistance(null);
    leg.setLegInboundOutboundIndicator(null);
    leg.setRho(null);
    leg.setRnp(null);
    leg.setTheta(null);
    leg.setCenterFixRef(null);
    leg.setVerticalScaleFactor(null);
    leg.setHoldTime(null);
    leg.setProcedureDesignMagVar(null);
    leg.setSpeedLimit(null);
    leg.setAltitudeConstraint(null);
    leg.setAltitudeTermination(null);
    leg.setRaceTrackAltitude(null);
    leg.setWaypointDescriptor(null);

    Optional<ArincProcedureLeg> result = CONVERTER.apply(leg);
    assertTrue(result.isPresent());

    ArincProcedureLeg converted = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), converted.courseValue()),
        () -> assertEquals(Optional.empty(), converted.courseIsTrue()),
        () -> assertEquals(Optional.empty(), converted.fixRef()),
        () -> assertEquals(Optional.empty(), converted.recNavaidRef()),
        () -> assertEquals(Optional.empty(), converted.turnDirection()),
        () -> assertEquals(Optional.empty(), converted.arcRadius()),
        () -> assertEquals(Optional.empty(), converted.atcIndicator()),
        () -> assertEquals(Optional.empty(), converted.distance()),
        () -> assertEquals(Optional.empty(), converted.legDistance()),
        () -> assertEquals(Optional.empty(), converted.legInboundOutboundIndicator()),
        () -> assertEquals(Optional.empty(), converted.rho()),
        () -> assertEquals(Optional.empty(), converted.rnp()),
        () -> assertEquals(Optional.empty(), converted.theta()),
        () -> assertEquals(Optional.empty(), converted.centerFixRef()),
        () -> assertEquals(Optional.empty(), converted.verticalScaleFactor()),
        () -> assertEquals(Optional.empty(), converted.holdTime()),
        () -> assertEquals(Optional.empty(), converted.procedureDesignMagVarDirection()),
        () -> assertEquals(Optional.empty(), converted.procedureDesignMagVarValue()),
        () -> assertEquals(Optional.empty(), converted.speedLimitAt()),
        () -> assertEquals(Optional.empty(), converted.speedLimitAtOrAbove()),
        () -> assertEquals(Optional.empty(), converted.speedLimitAtOrBelow()),
        () -> assertEquals(Optional.empty(), converted.altitudeConstraintAt()),
        () -> assertEquals(Optional.empty(), converted.altitudeConstraintAtOrAbove()),
        () -> assertEquals(Optional.empty(), converted.altitudeConstraintAtOrBelow()),
        () -> assertEquals(Optional.empty(), converted.altitudeTerminationType()),
        () -> assertEquals(Optional.empty(), converted.altitudeTerminationValue()),
        () -> assertEquals(Optional.empty(), converted.raceTrackAltitude()),
        () -> assertEquals(Optional.empty(), converted.isEssential()),
        () -> assertEquals(Optional.empty(), converted.isFlyOver()),
        () -> assertEquals(Optional.empty(), converted.isHolding()),
        () -> assertEquals(Optional.empty(), converted.isNoProcedureTurn()),
        () -> assertEquals(Optional.empty(), converted.isPhantomFix()),
        () -> assertEquals(Optional.empty(), converted.isSourceProvidedEnrouteWaypoint()),
        () -> assertEquals(Optional.empty(), converted.isTaaProcedureTurn()),
        () -> assertEquals(Optional.empty(), converted.isAtcCompulsoryReportingPoint())
    );
  }

  @Test
  void approachLegWithNullApproachWaypointDescription() {
    ApproachLeg leg = TestGeneratedObjects.newFullApproachLeg(10);
    leg.setApproachWaypointDescription(null);

    Optional<ArincProcedureLeg> result = CONVERTER.apply(leg);
    assertTrue(result.isPresent());

    ArincProcedureLeg converted = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), converted.isFacf()),
        () -> assertEquals(Optional.empty(), converted.isMissedApproachPoint()),
        () -> assertEquals(Optional.empty(), converted.isFaf()),
        () -> assertEquals(Optional.empty(), converted.isFinalEndPoint()),
        () -> assertEquals(Optional.empty(), converted.isFixTurningFinalApproach()),
        () -> assertEquals(Optional.empty(), converted.isInitialApproachFix()),
        () -> assertEquals(Optional.empty(), converted.isIntermediateApproachFix()),
        () -> assertEquals(Optional.empty(), converted.stepDownFix()),
        // verticalAngle and glideSlopeCrossingAltitude still set
        () -> assertEquals(-3.0, converted.verticalAngle().orElse(null), 0.001),
        () -> assertEquals(Optional.of(200), converted.glideSlopeCrossingAltitude())
    );
  }
}
