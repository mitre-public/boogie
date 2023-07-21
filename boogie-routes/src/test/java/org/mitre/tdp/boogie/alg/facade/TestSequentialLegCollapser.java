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
import org.mitre.tdp.boogie.alg.resolve.ElementType;

import com.google.common.collect.ImmutableMap;

class TestSequentialLegCollapser {
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
