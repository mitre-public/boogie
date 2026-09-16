package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;

class ProcedureSpeedLimitsTest {

  @Test
  void starSpeedsContinueForwardUntilReplacedWithoutChangingPublishedRecords() {
    var first = segment(1, "5", "RW14L", 10, "ONE").build();
    var limited = segment(1, "5", "RW14L", 20, "TWO").speedLimit1(210.0).build();
    var inherited = segment(1, "5", "RW14L", 30, "THREE").build();
    var slower = segment(1, "5", "RW14L", 40, "FOUR").speedLimit1(180.0).build();
    var last = segment(1, "5", "RW14L", 50, "FIVE").build();
    var limits = ProcedureSpeedLimits.resolve(List.of(last, first, slower, inherited, limited));
    assertAll(
        () -> assertTrue(limits.get(first).isEmpty()),
        () -> assertEquals(210.0, limits.get(inherited).get(0).knots()),
        () -> assertEquals(180.0, limits.get(last).get(0).knots()),
        () -> assertTrue(inherited.speedLimit1().isEmpty())
    );
  }

  @Test
  void sidSpeedsApplyBackwardAndHoldSpeedIsLocal() {
    var first = segment(2, "5", "ALL", 10, "ONE").build();
    var limit = segment(2, "5", "ALL", 20, "TWO").speedLimit1(210.0).build();
    var middle = segment(2, "5", "ALL", 30, "THREE").build();
    var hold = segment(2, "5", "ALL", 40, "FOUR").trackDescriptionCode("HA").speedLimit1(180.0).build();
    var later = segment(2, "5", "ALL", 50, "FIVE").speedLimit1(250.0).build();
    var last = segment(2, "5", "ALL", 60, "SIX").build();
    var limits = ProcedureSpeedLimits.resolve(List.of(first, limit, middle, hold, later, last));
    assertAll(
        () -> assertEquals(210.0, limits.get(first).get(0).knots()),
        () -> assertEquals(250.0, limits.get(middle).get(0).knots()),
        () -> assertEquals(180.0, limits.get(hold).get(0).knots()),
        () -> assertTrue(limits.get(last).isEmpty())
    );
  }

  @Test
  void approachQualifiersSurviveAndHoldLimitsDoNotLeakPastTheHold() {
    var first = segment(3, "I", "", 10, "ONE").speedLimit1(210.0).speedLimitAircraftType1("J")
        .speedLimitAltitude1("FL100").speedLimit2(180.0).speedLimitAircraftType2("T").build();
    var hold = segment(3, "I", "", 20, "HOLD").trackDescriptionCode("HF").speedLimit1(160.0).build();
    var last = segment(3, "I", "", 30, "LAST").build();
    var limits = ProcedureSpeedLimits.resolve(List.of(first, hold, last));
    assertAll(
        () -> assertEquals(160.0, limits.get(hold).get(0).knots()),
        () -> assertEquals(2, limits.get(last).size()),
        () -> assertEquals("J", limits.get(last).get(0).aircraft()),
        () -> assertEquals("FL100", limits.get(last).get(0).belowAltitude()),
        () -> assertEquals("T", limits.get(last).get(1).aircraft()),
        () -> assertTrue(limits.get(last).stream().noneMatch(ProcedureSpeedLimits.Limit::isUnqualified))
    );
  }

  @Test
  void inheritsAcrossConnectedTransitionsWithoutLeakingAcrossBranches() {
    var entry = segment(1, "4", "ENTRY", 10, "JOIN").speedLimit1(210.0).build();
    var common = segment(1, "5", "ALL", 10, "JOIN").build();
    var commonEnd = segment(1, "5", "ALL", 20, "EXIT").build();
    var runway = segment(1, "6", "RW09", 10, "EXIT").build();
    var other = segment(1, "4", "OTHER", 10, "ELSE").speedLimit1(160.0).build();
    var limits = ProcedureSpeedLimits.resolve(List.of(entry, common, commonEnd, runway, other));
    assertAll(
        () -> assertEquals(210.0, limits.get(common).get(0).knots()),
        () -> assertEquals(210.0, limits.get(runway).get(0).knots()),
        () -> assertEquals(160.0, limits.get(other).get(0).knots())
    );
  }

  @Test
  void commonRouteOverridesAnEarlierLimitAtTheSameConnectingFix() {
    var entry = segment(1, "4", "ENTRY", 10, "JOIN").speedLimit1(210.0).build();
    var common = segment(1, "5", "ALL", 10, "JOIN").speedLimit1(180.0).build();
    var runway = segment(1, "6", "RW09", 10, "JOIN").build();
    var limits = ProcedureSpeedLimits.resolve(List.of(entry, common, runway));
    assertAll(
        () -> assertEquals(210.0, limits.get(entry).get(0).knots()),
        () -> assertEquals(180.0, limits.get(common).get(0).knots()),
        () -> assertEquals(180.0, limits.get(runway).get(0).knots())
    );
  }

  @Test
  void conflictingBranchesDoNotInventARestrictionForTheSharedTransition() {
    var one = segment(1, "4", "ONE", 10, "JOIN").speedLimit1(210.0).build();
    var two = segment(1, "4", "TWO", 10, "JOIN").speedLimit1(180.0).build();
    var common = segment(1, "5", "ALL", 10, "JOIN").build();
    var explicit = segment(1, "5", "ALL", 20, "NEXT").speedLimit1(170.0).build();
    var limits = ProcedureSpeedLimits.resolve(List.of(one, two, common, explicit));
    assertAll(
        () -> assertEquals(210.0, limits.get(one).get(0).knots()),
        () -> assertEquals(180.0, limits.get(two).get(0).knots()),
        () -> assertTrue(limits.get(common).isEmpty()),
        () -> assertEquals(170.0, limits.get(explicit).get(0).knots())
    );
  }

  @Test
  void sidLimitsCrossConnectedTransitionsBackward() {
    var runway = segment(2, "4", "RW09", 10, "JOIN").build();
    var common = segment(2, "5", "ALL", 10, "JOIN").build();
    var commonEnd = segment(2, "5", "ALL", 20, "EXIT").build();
    var enroute = segment(2, "6", "EXIT", 10, "EXIT").speedLimit1(220.0).build();
    var limits = ProcedureSpeedLimits.resolve(List.of(runway, common, commonEnd, enroute));
    assertAll(
        () -> assertEquals(220.0, limits.get(runway).get(0).knots()),
        () -> assertEquals(220.0, limits.get(common).get(0).knots())
    );
  }

  private static DafifTerminalSegment.Builder segment(int procedureType, String type, String transition, int sequence, String fix) {
    return DafifTerminalSegment.builder().airportIdentification("US00001").terminalProcedureType(procedureType)
        .terminalIdentifier("TEST1").terminalApproachType(type).transitionIdentifier(transition).terminalSequenceNumber(sequence)
        .icaoCode("KAAA").trackDescriptionCode("TF").termSegWaypointIdentifier(fix).waypointCountryCode("US").cycleDate(202601);
  }
}
