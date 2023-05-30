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
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.resolve.ElementType;

import com.google.common.collect.ImmutableMap;

class TestSequentialLegCollapser {

  private static final RedundantLegCombiner collapser = new RedundantLegCombiner();

  @Test
  void testMatchingAssociatedFixIdentifiers() {
    assertAll(
        () -> assertTrue(collapser.matchingAssociatedFixIdentifiers(
            mockLeg("VAN", PathTerminator.TF),
            mockLeg("VAN", PathTerminator.DF)
        ), "Identical waypoint names should pass"),

        () -> assertFalse(collapser.matchingAssociatedFixIdentifiers(
            mockLeg("VAN", PathTerminator.TF),
            mockLeg("DAVID", PathTerminator.TF)
        ), "Mismatched waypoint names should fail"),

        () -> assertFalse(collapser.matchingAssociatedFixIdentifiers(
            mockLeg(null, PathTerminator.TF),
            mockLeg("VAN", PathTerminator.TF)
        ), "Null waypoint names should not throw and exception, but fail")
    );
  }

  @Test
  void testIsSimilarFixTerminatingLeg() {
    assertAll(
        () -> assertTrue(collapser.isSimilarFixTerminatingLeg(
            mockLeg("VAN", PathTerminator.TF),
            mockLeg("VAN", PathTerminator.DF)
        ), "Identical waypoint and both xF legs should be true"),

        () -> assertFalse(collapser.isSimilarFixTerminatingLeg(
            mockLeg("DAVID", PathTerminator.TF),
            mockLeg("VAN", PathTerminator.DF)
        ), "Different waypoint and both xF legs should be false"),

        () -> assertFalse(collapser.isSimilarFixTerminatingLeg(
            mockLeg("VAN", PathTerminator.TF),
            mockLeg("VAN", PathTerminator.FM)
        ), "Identical waypoint but not both xF legs should be false")
    );
  }

  private static final Map<ElementType, ExpandedRouteLeg> legsByType = ImmutableMap.of(
      SID, new ExpandedRouteLeg("SID", SID, "", mockLeg("DAVID", PathTerminator.TF, 1.)),
      STAR, new ExpandedRouteLeg("STAR", STAR, "", mockLeg("DAVID", PathTerminator.TF, 2.)),
      APPROACH, new ExpandedRouteLeg("APCH", APPROACH, "", mockLeg("DAVID", PathTerminator.TF, 3.)),
      AIRWAY, new ExpandedRouteLeg("AWY", AIRWAY, "", mockLeg("DAVID", PathTerminator.TF, 4.)),
      FIX, new ExpandedRouteLeg("DIR", FIX, "", mockLeg("DAVID", PathTerminator.TF, 5.))
  );

  @Test
  void testPreferredLeg() {
    assertAll(
        // AIRWAY
        () -> assertEquals(AIRWAY, collapser.preferredLeg(legsByType.get(FIX), legsByType.get(AIRWAY)).elementType()),
        () -> assertEquals(AIRWAY, collapser.preferredLeg(legsByType.get(AIRWAY), legsByType.get(FIX)).elementType()),

        // SID
        () -> assertEquals(SID, collapser.preferredLeg(legsByType.get(SID), legsByType.get(AIRWAY)).elementType()),
        () -> assertEquals(SID, collapser.preferredLeg(legsByType.get(SID), legsByType.get(FIX)).elementType()),

        // STAR
        () -> assertEquals(STAR, collapser.preferredLeg(legsByType.get(AIRWAY), legsByType.get(STAR)).elementType()),
        () -> assertEquals(STAR, collapser.preferredLeg(legsByType.get(SID), legsByType.get(STAR)).elementType()),
        () -> assertEquals(STAR, collapser.preferredLeg(legsByType.get(FIX), legsByType.get(STAR)).elementType()),

        // APPROACH
        () -> assertEquals(APPROACH, collapser.preferredLeg(legsByType.get(AIRWAY), legsByType.get(APPROACH)).elementType()),
        () -> assertEquals(APPROACH, collapser.preferredLeg(legsByType.get(SID), legsByType.get(APPROACH)).elementType()),
        () -> assertEquals(APPROACH, collapser.preferredLeg(legsByType.get(FIX), legsByType.get(APPROACH)).elementType()),
        () -> assertEquals(APPROACH, collapser.preferredLeg(legsByType.get(STAR), legsByType.get(APPROACH)).elementType())
    );
  }

  @Test
  void testSpecialElementTypeBetween() {
    assertAll(
        () -> assertEquals(Optional.of(SID_TO_APPROACH), collapser.specialElementTypeBetween(SID, APPROACH)),
        () -> assertEquals(Optional.of(AIRWAY_TO_APPROACH), collapser.specialElementTypeBetween(AIRWAY, APPROACH)),
        () -> assertEquals(Optional.of(STAR_TO_APPROACH), collapser.specialElementTypeBetween(STAR, APPROACH)),
        () -> assertEquals(Optional.of(AIRWAY_TO_STAR), collapser.specialElementTypeBetween(AIRWAY, STAR)),
        () -> assertEquals(Optional.of(SID_TO_STAR), collapser.specialElementTypeBetween(SID, STAR)),
        () -> assertEquals(Optional.empty(), collapser.specialElementTypeBetween(APPROACH, STAR))
    );
  }

  @Test
  void testSafelyMergeLegs() {
    ExpandedRouteLeg sidToStar = collapser.safelyMergeLegs(legsByType.get(SID), legsByType.get(STAR));
    ExpandedRouteLeg starToApproach = collapser.safelyMergeLegs(legsByType.get(STAR), legsByType.get(APPROACH));

    assertAll(
        "Check the leg values are updated effectively during the merge.",
        () -> assertEquals(SID_TO_STAR, sidToStar.elementType()),
        () -> assertEquals(Optional.of(2.), sidToStar.rnp()),

        () -> assertEquals(STAR_TO_APPROACH, starToApproach.elementType()),
        () -> assertEquals(Optional.of(3.), starToApproach.rnp())
    );
  }

  private static Leg mockLeg(String fixName, PathTerminator pathTerminator) {
    return mockLeg(fixName, pathTerminator, 0.);
  }

  private static Leg mockLeg(String fixName, PathTerminator pathTerminator, double rnp) {
    Fix fix = mock(Fix.class);
    when(fix.fixIdentifier()).thenReturn(fixName);

    Leg leg = spy(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    when(leg.rnp()).thenReturn(Optional.of(rnp));
    if (fixName == null) {
      when(leg.associatedFix()).thenReturn(Optional.empty());
    } else {
      when(leg.associatedFix()).thenReturn((Optional) Optional.of(fix));
    }

    return leg;
  }
}
