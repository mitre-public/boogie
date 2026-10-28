package org.mitre.tdp.boogie.alg.facade;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRWAY;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRWAY_TO_APPROACH;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRWAY_TO_STAR;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.APPROACH;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.FIX;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID_TO_APPROACH;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID_TO_STAR;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.STAR;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.STAR_TO_APPROACH;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.alg.resolve.ElementType;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;

class SequentialLegCollapserTest {
  private static final RedundantLegCombiner COLLAPSER = new RedundantLegCombiner();
  private static final ExpandedRouteLeg CF = new ExpandedRouteLeg("STAR", STAR, "", Leg.cfBuilder(Fix.builder().fixIdentifier("VAN").latLong(LatLong.of(0.0, 0.0)).build(), 10, 100.0).rnp(2.).build());

  private static final Leg VAN = Leg.tfBuilder(Fix.builder().fixIdentifier("VAN").latLong(LatLong.of(0.0, 0.0)).build(), 10).build();
  private static final Leg DAVID = Leg.tfBuilder(Fix.builder().fixIdentifier("DAVID").latLong(LatLong.of(0.0, 0.0)).build(), 10).build();
  private static final Leg VI = Leg.builder(PathTerminator.VI, 10).outboundMagneticCourse(10.0).build();
  private static final Leg FM = Leg.builder(PathTerminator.FM, 10).build();
  private static final Leg ALEX = Leg.dfBuilder(Fix.builder().fixIdentifier("VAN").latLong(LatLong.of(0.0, 0.0)).build(), 10).build();
  private static final Map<ElementType, ExpandedRouteLeg> legsByType = ImmutableMap.of(
      SID, new ExpandedRouteLeg("SID", SID, "", Leg.tfBuilder(Fix.builder().fixIdentifier("VAN").latLong(LatLong.of(0.0, 0.0)).build(), 10).rnp(2.).build()),
      STAR, new ExpandedRouteLeg("STAR", STAR, "", Leg.cfBuilder(Fix.builder().fixIdentifier("VAN").latLong(LatLong.of(0.0, 0.0)).build(), 10, 200.0).rnp(2.).build()),
      APPROACH, new ExpandedRouteLeg("APCH", APPROACH, "", Leg.tfBuilder(Fix.builder().fixIdentifier("VAN").latLong(LatLong.of(0.0, 0.0)).build(), 10).rnp(3.).build()),
      AIRWAY, new ExpandedRouteLeg("AWY", AIRWAY, "", Leg.tfBuilder(Fix.builder().fixIdentifier("VAN").latLong(LatLong.of(0.0, 0.0)).build(), 10).rnp(4.).build()),
      FIX, new ExpandedRouteLeg("DIR", FIX, "", Leg.tfBuilder(Fix.builder().fixIdentifier("VAN").latLong(LatLong.of(0.0, 0.0)).build(), 10).rnp(5.).build())
  );

  @Test
  void testMatchingAssociatedFixIdentifiers() {
    assertAll(
        () -> assertTrue(COLLAPSER.matchingAssociatedFixIdentifiers(
            VAN,
            VAN
        ), "Identical waypoint names should pass"),

        () -> assertFalse(COLLAPSER.matchingAssociatedFixIdentifiers(
            VAN,
            DAVID
        ), "Mismatched waypoint names should fail"),

        () -> assertFalse(COLLAPSER.matchingAssociatedFixIdentifiers(
            VI,
            VAN
        ), "null waypoint names should not throw and exception, but fail")
    );
  }

  @Test
  void testIsSimilarFixTerminatingLeg() {
    assertAll(
        () -> assertTrue(COLLAPSER.isSimilarFixTerminatingLeg(
            VAN,
            ALEX
        ), "Identical waypoint and both xF legs should be true"),

        () -> assertFalse(COLLAPSER.isSimilarFixTerminatingLeg(
            DAVID,
            ALEX
        ), "Different waypoint and both xF legs should be false"),

        () -> assertFalse(COLLAPSER.isSimilarFixTerminatingLeg(
            VAN,
            FM
        ), "Identical waypoint but not both xF legs should be false")
    );
  }

  @Test
  void testCFCollapse() {
    assertEquals(100.0, COLLAPSER.safelyMergeLegs(CF, legsByType.get(APPROACH)).outboundMagneticCourse().get());
  }

  /**
   * An approach transition that ends in an RF leg into the fix where the final route begins (IF): the merged leg keeps the RF
   * path terminator, so it must keep the arc that terminator flies - center fix, arc radius and turn direction - and take only
   * the constraints from the IF.
   */
  @Test
  void testRfIntoInitialFixKeepsTheArc() {
    Fix yebun = Fix.builder().fixIdentifier("YEBUN").latLong(LatLong.of(34.2, -118.4)).build();
    Fix center = Fix.builder().fixIdentifier("CFCGT").latLong(LatLong.of(34.2455, -118.5128)).build();
    ExpandedRouteLeg rf = new ExpandedRouteLeg("APCH", APPROACH, "", Leg.builder(PathTerminator.RF, 40)
        .associatedFix(yebun).centerFix(center).arcRadius(2.74).turnDirection(TurnDirection.left()).build());
    ExpandedRouteLeg initial = new ExpandedRouteLeg("APCH", APPROACH, "", Leg.builder(PathTerminator.IF, 10)
        .associatedFix(yebun).altitudeConstraint(Range.atLeast(3000.)).build());

    ExpandedRouteLeg merged = COLLAPSER.safelyMergeLegs(rf, initial);

    assertAll(
        () -> assertEquals(PathTerminator.RF, merged.pathTerminator()),
        () -> assertEquals(Optional.of("CFCGT"), merged.centerFix().map(Fix::fixIdentifier)),
        () -> assertEquals(Optional.of(2.74), merged.arcRadius()),
        () -> assertEquals(Optional.of(TurnDirection.left()), merged.turnDirection()),
        () -> assertEquals(Range.atLeast(3000.), merged.altitudeConstraint(), "constraints still come from the IF")
    );
  }

  /** The same for a DME arc: the recommended navaid, rho, theta and boundary course are the AF leg's own. */
  @Test
  void testAfIntoInitialFixKeepsTheArc() {
    Fix end = Fix.builder().fixIdentifier("ARCND").latLong(LatLong.of(40.1, -100.0)).build();
    Fix dme = Fix.builder().fixIdentifier("DME").latLong(LatLong.of(40.0, -100.0)).build();
    ExpandedRouteLeg af = new ExpandedRouteLeg("APCH", APPROACH, "", Leg.afBuilder(end, dme, 30, 270.0)
        .rho(10.0).theta(180.0).turnDirection(TurnDirection.right()).build());
    ExpandedRouteLeg initial = new ExpandedRouteLeg("APCH", APPROACH, "", Leg.builder(PathTerminator.IF, 10).associatedFix(end).build());

    ExpandedRouteLeg merged = COLLAPSER.safelyMergeLegs(af, initial);

    assertAll(
        () -> assertEquals(PathTerminator.AF, merged.pathTerminator()),
        () -> assertEquals(Optional.of("DME"), merged.recommendedNavaid().map(Fix::fixIdentifier)),
        () -> assertEquals(Optional.of(10.0), merged.rho()),
        () -> assertEquals(Optional.of(180.0), merged.theta()),
        () -> assertEquals(Optional.of(270.0), merged.outboundMagneticCourse()),
        () -> assertEquals(Optional.of(TurnDirection.right()), merged.turnDirection())
    );
  }

  @Test
  void testPreferredLeg() {
    assertAll(
        // AIRWAY
        () -> assertEquals(AIRWAY, COLLAPSER.preferredLeg(legsByType.get(FIX), legsByType.get(AIRWAY)).elementType()),
        () -> assertEquals(AIRWAY, COLLAPSER.preferredLeg(legsByType.get(AIRWAY), legsByType.get(FIX)).elementType()),

        // SID
        () -> assertEquals(SID, COLLAPSER.preferredLeg(legsByType.get(SID), legsByType.get(AIRWAY)).elementType()),
        () -> assertEquals(SID, COLLAPSER.preferredLeg(legsByType.get(SID), legsByType.get(FIX)).elementType()),

        // STAR
        () -> assertEquals(STAR, COLLAPSER.preferredLeg(legsByType.get(AIRWAY), legsByType.get(STAR)).elementType()),
        () -> assertEquals(STAR, COLLAPSER.preferredLeg(legsByType.get(SID), legsByType.get(STAR)).elementType()),
        () -> assertEquals(STAR, COLLAPSER.preferredLeg(legsByType.get(FIX), legsByType.get(STAR)).elementType()),

        // APPROACH
        () -> assertEquals(APPROACH, COLLAPSER.preferredLeg(legsByType.get(AIRWAY), legsByType.get(APPROACH)).elementType()),
        () -> assertEquals(APPROACH, COLLAPSER.preferredLeg(legsByType.get(SID), legsByType.get(APPROACH)).elementType()),
        () -> assertEquals(APPROACH, COLLAPSER.preferredLeg(legsByType.get(FIX), legsByType.get(APPROACH)).elementType()),
        () -> assertEquals(APPROACH, COLLAPSER.preferredLeg(legsByType.get(STAR), legsByType.get(APPROACH)).elementType())
    );
  }

  @Test
  void testSpecialElementTypeBetween() {
    assertAll(
        () -> assertEquals(Optional.of(SID_TO_APPROACH), COLLAPSER.specialElementTypeBetween(SID, APPROACH)),
        () -> assertEquals(Optional.of(AIRWAY_TO_APPROACH), COLLAPSER.specialElementTypeBetween(AIRWAY, APPROACH)),
        () -> assertEquals(Optional.of(STAR_TO_APPROACH), COLLAPSER.specialElementTypeBetween(STAR, APPROACH)),
        () -> assertEquals(Optional.of(AIRWAY_TO_STAR), COLLAPSER.specialElementTypeBetween(AIRWAY, STAR)),
        () -> assertEquals(Optional.of(SID_TO_STAR), COLLAPSER.specialElementTypeBetween(SID, STAR)),
        () -> assertEquals(Optional.empty(), COLLAPSER.specialElementTypeBetween(APPROACH, STAR))
    );
  }

  @Test
  void testSafelyMergeLegs() {
    ExpandedRouteLeg sidToStar = COLLAPSER.safelyMergeLegs(legsByType.get(SID), legsByType.get(STAR));
    ExpandedRouteLeg starToApproach = COLLAPSER.safelyMergeLegs(legsByType.get(STAR), legsByType.get(APPROACH));

    assertAll(
        "Check the leg values are updated effectively during the merge.",
        () -> assertEquals(SID_TO_STAR, sidToStar.elementType()),
        () -> assertEquals(Optional.of(2.), sidToStar.rnp()),

        () -> assertEquals(STAR_TO_APPROACH, starToApproach.elementType()),
        () -> assertEquals(Optional.of(3.), starToApproach.rnp())
    );
  }
}
