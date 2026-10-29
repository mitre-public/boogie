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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.resolve.ElementType;

import com.google.common.collect.ImmutableMap;

class SequentialLegCollapserTest {
  private static final RedundantFixTerminatingLegCombiner COLLAPSER = new RedundantFixTerminatingLegCombiner();
  private static final MatchingFixIdents IDENT_MATCHER = MatchingFixIdents.INSTANCE;
  private static final SimilarFixTerminatingLeg SIMILAR_FIX_TERMINATING_LEG = SimilarFixTerminatingLeg.INSTANCE;
  private static final PreferredLeg PREFERRED_LEG = PreferredLeg.INSTANCE;
  private static final CollapsedElementType COLLAPSED_ELEMENT_TYPE = CollapsedElementType.INSTANCE;
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
        () -> assertTrue(IDENT_MATCHER.test(
            VAN,
            VAN
        ), "Identical waypoint names should pass"),

        () -> assertFalse(IDENT_MATCHER.test(
            VAN,
            DAVID
        ), "Mismatched waypoint names should fail"),

        () -> assertFalse(IDENT_MATCHER.test(
            VI,
            VAN
        ), "null waypoint names should not throw and exception, but fail")
    );
  }

  @Test
  void testIsSimilarFixTerminatingLeg() {
    assertAll(
        () -> assertTrue(SIMILAR_FIX_TERMINATING_LEG.test(
            VAN,
            ALEX
        ), "Identical waypoint and both xF legs should be true"),

        () -> assertFalse(SIMILAR_FIX_TERMINATING_LEG.test(
            DAVID,
            ALEX
        ), "Different waypoint and both xF legs should be false"),

        () -> assertFalse(SIMILAR_FIX_TERMINATING_LEG.test(
            VAN,
            FM
        ), "Identical waypoint but not both xF legs should be false")
    );
  }

  @Test
  void testCFCollapse() {
    assertEquals(100.0, COLLAPSER.apply(List.of(CF, legsByType.get(APPROACH))).get(0).outboundMagneticCourse().get());
  }

  @Test
  void testPreferredLeg() {
    assertAll(
        // AIRWAY
        () -> assertEquals(AIRWAY, PREFERRED_LEG.apply(legsByType.get(FIX), legsByType.get(AIRWAY)).elementType()),
        () -> assertEquals(AIRWAY, PREFERRED_LEG.apply(legsByType.get(AIRWAY), legsByType.get(FIX)).elementType()),

        // SID
        () -> assertEquals(SID, PREFERRED_LEG.apply(legsByType.get(SID), legsByType.get(AIRWAY)).elementType()),
        () -> assertEquals(SID, PREFERRED_LEG.apply(legsByType.get(SID), legsByType.get(FIX)).elementType()),

        // STAR
        () -> assertEquals(STAR, PREFERRED_LEG.apply(legsByType.get(AIRWAY), legsByType.get(STAR)).elementType()),
        () -> assertEquals(STAR, PREFERRED_LEG.apply(legsByType.get(SID), legsByType.get(STAR)).elementType()),
        () -> assertEquals(STAR, PREFERRED_LEG.apply(legsByType.get(FIX), legsByType.get(STAR)).elementType()),

        // APPROACH
        () -> assertEquals(APPROACH, PREFERRED_LEG.apply(legsByType.get(AIRWAY), legsByType.get(APPROACH)).elementType()),
        () -> assertEquals(APPROACH, PREFERRED_LEG.apply(legsByType.get(SID), legsByType.get(APPROACH)).elementType()),
        () -> assertEquals(APPROACH, PREFERRED_LEG.apply(legsByType.get(FIX), legsByType.get(APPROACH)).elementType()),
        () -> assertEquals(APPROACH, PREFERRED_LEG.apply(legsByType.get(STAR), legsByType.get(APPROACH)).elementType())
    );
  }

  @Test
  void testSpecialElementTypeBetween() {
    assertAll(
        () -> assertEquals(Optional.of(SID_TO_APPROACH), COLLAPSED_ELEMENT_TYPE.apply(SID, APPROACH)),
        () -> assertEquals(Optional.of(AIRWAY_TO_APPROACH), COLLAPSED_ELEMENT_TYPE.apply(AIRWAY, APPROACH)),
        () -> assertEquals(Optional.of(STAR_TO_APPROACH), COLLAPSED_ELEMENT_TYPE.apply(STAR, APPROACH)),
        () -> assertEquals(Optional.of(AIRWAY_TO_STAR), COLLAPSED_ELEMENT_TYPE.apply(AIRWAY, STAR)),
        () -> assertEquals(Optional.of(SID_TO_STAR), COLLAPSED_ELEMENT_TYPE.apply(SID, STAR)),
        () -> assertEquals(Optional.empty(), COLLAPSED_ELEMENT_TYPE.apply(APPROACH, STAR))
    );
  }

  @Test
  void testSafelyMergeLegs() {
    ExpandedRouteLeg sidToStar = COLLAPSER.apply(List.of(legsByType.get(SID), legsByType.get(STAR))).get(0);
    ExpandedRouteLeg starToApproach = COLLAPSER.apply(List.of(legsByType.get(STAR), legsByType.get(APPROACH))).get(0);

    assertAll(
        "Check the leg values are updated effectively during the merge.",
        () -> assertEquals(SID_TO_STAR, sidToStar.elementType()),
        () -> assertEquals(Optional.of(2.), sidToStar.rnp()),

        () -> assertEquals(STAR_TO_APPROACH, starToApproach.elementType()),
        () -> assertEquals(Optional.of(2.), starToApproach.rnp())
    );
  }
}
