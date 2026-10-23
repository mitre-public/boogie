package org.mitre.boogie.xml.assemble;

import static org.junit.jupiter.api.Assertions.*;
import static org.mitre.boogie.xml.assemble.ProcedureTestFixtures.*;

import java.time.Duration;
import java.util.List;

import com.google.common.collect.Range;
import org.mitre.tdp.boogie.MagneticVariation;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.boogie.xml.model.ArincProcedure;
import org.mitre.boogie.xml.model.ArincProcedureLeg;
import org.mitre.boogie.xml.model.ArincTransition;

class ProcedureAssemblyStrategyTest {

  private static final ProcedureAssemblyStrategy<Procedure, Transition, Leg, Fix> STRATEGY = ProcedureAssemblyStrategy.standard();

  @Test
  void convertProcedure_sid() {
    ArincProcedure proc = testProcedure("GLAVN1", "Sid", false);

    Transition transition = Transition.builder()
        .transitionIdentifier("RW09L")
        .transitionType(TransitionType.RUNWAY)
        .build();

    Procedure result = STRATEGY.convertProcedure(proc, "KDTW", List.of(transition));

    assertAll(
        () -> assertEquals("GLAVN1", result.procedureIdentifier()),
        () -> assertEquals("KDTW", result.airportIdentifier()),
        () -> assertEquals(ProcedureType.SID, result.procedureType()),
        () -> assertEquals(RequiredNavigationEquipage.CONV, result.requiredNavigationEquipage()),
        () -> assertEquals(1, result.transitions().size())
    );
  }

  @Test
  void convertProcedure_star() {
    ArincProcedure proc = testProcedure("HOBBT2", "Star", false);

    Procedure result = STRATEGY.convertProcedure(proc, "KJFK", List.of());

    assertAll(
        () -> assertEquals("HOBBT2", result.procedureIdentifier()),
        () -> assertEquals("KJFK", result.airportIdentifier()),
        () -> assertEquals(ProcedureType.STAR, result.procedureType()),
        () -> assertEquals(RequiredNavigationEquipage.CONV, result.requiredNavigationEquipage())
    );
  }

  @Test
  void convertProcedure_rnavApproach() {
    ArincProcedure proc = testProcedure("R04R", "Approach", true);

    Procedure result = STRATEGY.convertProcedure(proc, "KDTW", List.of());

    assertAll(
        () -> assertEquals(ProcedureType.APPROACH, result.procedureType()),
        () -> assertEquals(RequiredNavigationEquipage.RNAV, result.requiredNavigationEquipage())
    );
  }

  @Test
  void convertTransition_sidRunwayTransition() {
    ArincProcedure proc = testProcedure("GLAVN1", "Sid", false);
    ArincTransition transition = ArincTransition.builder()
        .identifier("RW09L")
        .transitionType("SidRunwayTransition")
        .build();

    Leg leg = Leg.builder(PathTerminator.CF, 10).build();

    Transition result = STRATEGY.convertTransition(proc, transition, List.of(leg));

    assertAll(
        () -> assertEquals("RW09L", result.transitionIdentifier().orElseThrow()),
        () -> assertEquals(TransitionType.RUNWAY, result.transitionType()),
        () -> assertEquals(1, result.legs().size())
    );
  }

  @Test
  void convertTransition_starCommonRoute() {
    ArincProcedure proc = testProcedure("HOBBT2", "Star", false);
    ArincTransition transition = ArincTransition.builder()
        .transitionType("StarCommonRoute")
        .build();

    Transition result = STRATEGY.convertTransition(proc, transition, List.of());

    assertAll(
        () -> assertTrue(result.transitionIdentifier().isEmpty()),
        () -> assertEquals(TransitionType.COMMON, result.transitionType())
    );
  }

  @Test
  void convertTransition_missedApproach() {
    ArincProcedure proc = testProcedure("I04R", "Approach", false);
    ArincTransition transition = ArincTransition.builder()
        .transitionType("MissedApproach")
        .build();

    Transition result = STRATEGY.convertTransition(proc, transition, List.of());

    assertEquals(TransitionType.MISSED, result.transitionType());
  }

  @Test
  void convertTransition_approachTransition() {
    ArincProcedure proc = testProcedure("I04R", "Approach", false);
    ArincTransition transition = ArincTransition.builder()
        .identifier("JMACK")
        .transitionType("ApproachTransition")
        .build();

    Transition result = STRATEGY.convertTransition(proc, transition, List.of());

    assertEquals(TransitionType.APPROACH, result.transitionType());
  }

  @Test
  void convertTransition_finalApproach() {
    ArincProcedure proc = testProcedure("I04R", "Approach", false);
    ArincTransition transition = ArincTransition.builder()
        .transitionType("FinalApproach")
        .build();

    Transition result = STRATEGY.convertTransition(proc, transition, List.of());

    assertEquals(TransitionType.COMMON, result.transitionType());
  }

  @Test
  void convertLeg_withAllFields() {
    ArincProcedureLeg leg = ArincProcedureLeg.builder()
        .sequenceNumber(10)
        .pathAndTermination("TF")
        .fixIdent("JMACK")
        .courseValue(90.5)
        .theta(45.0)
        .rho(10.0)
        .rnp(2.0)
        .distance(25.0)
        .legDistance(40.0)
        .verticalAngle(3.0)
        .turnDirection("LEFT")
        .isFlyOver(true)
        .isHolding(true)
        .build();

    Fix associatedFix = testFix("JMACK");
    Fix recNavaid = testFix("DTW");
    Fix centerFix = testFix("CENTER");

    Leg result = STRATEGY.convertLeg(leg, associatedFix, recNavaid, centerFix);

    assertAll(
        () -> assertEquals(PathTerminator.TF, result.pathTerminator()),
        () -> assertEquals(10, result.sequenceNumber()),
        () -> assertEquals("JMACK", result.associatedFix().orElseThrow().fixIdentifier()),
        () -> assertEquals("DTW", result.recommendedNavaid().orElseThrow().fixIdentifier()),
        () -> assertEquals("CENTER", result.centerFix().orElseThrow().fixIdentifier()),
        () -> assertEquals(90.5, result.outboundMagneticCourse().orElseThrow(), 0.01),
        () -> assertEquals(45.0, result.theta().orElseThrow(), 0.01),
        () -> assertEquals(10.0, result.rho().orElseThrow(), 0.01),
        () -> assertEquals(2.0, result.rnp().orElseThrow(), 0.01),
        () -> assertEquals(25.0, result.routeDistance().orElseThrow(), 0.01),
        () -> assertEquals(-3.0, result.verticalAngle().orElseThrow(), 0.01),
        () -> assertTrue(result.isFlyOverFix()),
        () -> assertTrue(result.isPublishedHoldingFix())
    );
  }

  @Test
  void convertLeg_withNullFixes() {
    ArincProcedureLeg leg = ArincProcedureLeg.builder()
        .sequenceNumber(20)
        .pathAndTermination("IF")
        .build();

    Leg result = STRATEGY.convertLeg(leg, null, null, null);

    assertAll(
        () -> assertEquals(PathTerminator.IF, result.pathTerminator()),
        () -> assertEquals(20, result.sequenceNumber()),
        () -> assertTrue(result.associatedFix().isEmpty()),
        () -> assertTrue(result.recommendedNavaid().isEmpty()),
        () -> assertTrue(result.centerFix().isEmpty()),
        () -> assertFalse(result.isFlyOverFix()),
        () -> assertFalse(result.isPublishedHoldingFix())
    );
  }

  @Test
  void convertLeg_unknownPathTerminator_defaultsToTF() {
    ArincProcedureLeg leg = ArincProcedureLeg.builder()
        .sequenceNumber(30)
        .pathAndTermination("ZZ")
        .build();

    Leg result = STRATEGY.convertLeg(leg, null, null, null);

    assertEquals(PathTerminator.TF, result.pathTerminator());
  }
  @Test
  void preservesRfRadiusAndTimedHold() {
    Leg rf = STRATEGY.convertLeg(ArincProcedureLeg.builder().sequenceNumber(10)
        .pathAndTermination("RF").arcRadius(2.75).build(), null, null, null);
    Leg hold = STRATEGY.convertLeg(ArincProcedureLeg.builder().sequenceNumber(20)
        .pathAndTermination("HM").holdTime("PT1M30S").build(), null, null, null);
    assertEquals(2.75, rf.arcRadius().orElseThrow());
    assertEquals(Duration.ofSeconds(90), hold.holdTime().orElseThrow());
    assertTrue(hold.routeDistance().isEmpty());
  }

  @Test
  void flightPlanningDistanceDoesNotBecomeTerminationDistance() {
    Leg result = STRATEGY.convertLeg(ArincProcedureLeg.builder().sequenceNumber(10)
        .pathAndTermination("CD").legDistance(12.0).build(), null, null, null);
    assertTrue(result.routeDistance().isEmpty());
  }

  @Test
  void preservesConstraintBounds() {
    var cases = List.of(
        Range.<Double>all(),
        Range.singleton(200.0),
        Range.atLeast(200.0),
        Range.atMost(250.0),
        Range.closed(200.0, 250.0));
    for (var expected : cases) {
      var builder = ArincProcedureLeg.builder().sequenceNumber(10).pathAndTermination("TF");
      if (expected.hasLowerBound() && expected.hasUpperBound() && expected.lowerEndpoint().equals(expected.upperEndpoint())) {
        builder.speedLimitAt(200L).altitudeConstraintAt(200);
      } else {
        if (expected.hasLowerBound()) {
          builder.speedLimitAtOrAbove(200L).altitudeConstraintAtOrAbove(200);
        }
        if (expected.hasUpperBound()) {
          builder.speedLimitAtOrBelow(250L).altitudeConstraintAtOrBelow(250);
        }
      }
      Leg result = STRATEGY.convertLeg(builder.build(), null, null, null);
      assertEquals(expected, result.speedConstraint());
      assertEquals(expected, result.altitudeConstraint());
    }
  }

  @Test
  void trueCourseUsesNavaidVariationBeforeFixVariation() {
    Fix fix = Fix.builder().fixIdentifier("FIX").latLong(testFix("FIX").latLong()).magneticVariation(MagneticVariation.ofDegrees(-5)).build();
    Fix nav = Fix.builder().fixIdentifier("NAV").latLong(testFix("NAV").latLong()).magneticVariation(MagneticVariation.ofDegrees(10)).build();
    var source = ArincProcedureLeg.builder().sequenceNumber(10).pathAndTermination("CF")
        .courseValue(5.0).courseIsTrue(true).build();
    assertEquals(355.0, STRATEGY.convertLeg(source, fix, nav, null).outboundMagneticCourse().orElseThrow());
    assertEquals(10.0, STRATEGY.convertLeg(source, fix, null, null).outboundMagneticCourse().orElseThrow());
    assertTrue(STRATEGY.convertLeg(source, null, null, null).outboundMagneticCourse().isEmpty());
  }

}
