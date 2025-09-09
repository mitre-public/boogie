package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mitre.tdp.boogie.MockObjects.*;
import static org.mitre.tdp.boogie.PathTerminator.VI;
import static org.mitre.tdp.boogie.PathTerminator.VM;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedureGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airways;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

class AnyApproachTest {

  private static final LinkingStrategy STRATEGY = new LinkingStrategy(TokenMapper.standard());

  /**
   * No long cares if the leg is actually correct and will link it anyway. ALso helps if this gets used on missed approach in the future.
   */
  @Test
  void testNonFixOriginatingApproach() {
    assertDoesNotThrow(() -> STRATEGY.links(fixTerminatingStar(0.5), nonFixOriginatingApproach(0.5)));
  }

  /**
   * Tests case where distance between final fix terminating leg from the star and fix originating leg of the approach is zero.
   * The intended adjustment to the LinkedLegs in this instance is to do nothing.
   */
  @Test
  void testFixTerminatingStarAndZeroDistanceInBetween() {

    List<LinkedLegs> starApproachLinkedLegs = new ArrayList<>(STRATEGY.links(fixTerminatingStar(0.5), fixOriginatingApproach(0.5)));

    assertAll(
        () -> assertEquals(1, starApproachLinkedLegs.size()),

        () -> assertEquals(PathTerminator.TF, starApproachLinkedLegs.get(0).source().pathTerminator()),
        () -> assertEquals("fix terminating star leg", starApproachLinkedLegs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.FD, starApproachLinkedLegs.get(0).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(0).target().associatedFix().get().fixIdentifier())
    );
  }

  @Test
  void testManuallyTerminatingStar() {
    List<LinkedLegs> starApproachLinkedLegs = new ArrayList<>(STRATEGY.links(manualTerminatingStar(0.5), fixOriginatingApproach(0.5)));
    System.out.println(starApproachLinkedLegs);
    assertAll(
        () -> assertEquals("manual terminating star leg", starApproachLinkedLegs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(0).target().associatedFix().get().fixIdentifier()),
        () -> assertEquals(0.0, starApproachLinkedLegs.get(0).linkWeight(), "same spot")
    );
  }

  @Test
  void testManuallyTerminatingNoFixSid() {
    List<LinkedLegs> starApproachLinkedLegs = new ArrayList<>(STRATEGY.links(manualTerminatingSidWithOnlyOneFix(), fixOriginatingApproach(0.5)));
    System.out.println(starApproachLinkedLegs);
    assertAll(
        () -> assertEquals(VM, starApproachLinkedLegs.get(0).source().pathTerminator()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(0).target().associatedFix().get().fixIdentifier()),
        () -> assertEquals(1e-5, starApproachLinkedLegs.get(0).linkWeight(), "same spot")
    );
  }

  @Test
  void testManuallyTerminatingNoFixStar() {
    List<LinkedLegs> starApproachLinkedLegs = new ArrayList<>(STRATEGY.links(manualTerminatingStarWithOnlyOneFix(), fixOriginatingApproach(0.5)));
    System.out.println(starApproachLinkedLegs);
    assertAll(
        () -> assertEquals(VM, starApproachLinkedLegs.get(0).source().pathTerminator()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(0).target().associatedFix().get().fixIdentifier()),
        () -> assertEquals(1e-5, starApproachLinkedLegs.get(0).linkWeight(), "same spot")
    );
  }

  @Test
  void viIntoApproach() {
    List<LinkedLegs> starApproachLinkedLegs = new ArrayList<>(STRATEGY.links(viApproachTransition(), fixOriginatingApproach(0.5)));
    System.out.println(starApproachLinkedLegs);
    assertAll(
        () -> assertEquals(VI, starApproachLinkedLegs.get(0).source().pathTerminator()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(0).target().associatedFix().get().fixIdentifier()),
        () -> assertEquals(1e-5, starApproachLinkedLegs.get(0).linkWeight(), "same spot")
    );
  }



  /**
   * Tests case where distance between fix terminating leg in the star and fix originating leg in the approach is non zero.
   * The intended adjustment to the LinkedLegs in this instance is to clone the fix originating leg in the approach, convert it to a DF leg
   * and insert it in between the final star leg and initial approach leg.
   */
  @Disabled("No longer screwing with adding things here")
  @Test
  void testFixTerminatingStarAndNonZeroDistanceInBetween() {

    List<LinkedLegs> starApproachLinkedLegs = new ArrayList<>(STRATEGY.links(
        fixTerminatingStar(0.5),
        fixOriginatingApproach(0.75))
    );

    assertAll(
        () -> assertEquals(2, starApproachLinkedLegs.size()),

        () -> assertEquals(PathTerminator.TF, starApproachLinkedLegs.get(0).source().pathTerminator()),
        () -> assertEquals("fix terminating star leg", starApproachLinkedLegs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.DF, starApproachLinkedLegs.get(0).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(0).target().associatedFix().get().fixIdentifier()),

        () -> assertEquals(PathTerminator.DF, starApproachLinkedLegs.get(1).source().pathTerminator()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(1).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.FD, starApproachLinkedLegs.get(1).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(1).target().associatedFix().get().fixIdentifier())
    );
  }

  /**
   * Tests case where distance between the manual terminating leg of the star and the fix originating leg of the approach is non zero.
   * The intended adjustment to the LinkedLegs in this instance is to replace the manual terminating leg of the star with a
   * clone of the fix origiating leg in the approach. The cloned leg will also be converted to a DF leg. This will require a link between
   * the closest leg prior to the manual terminating star leg to the cloned df leg.
   */
  @Test
  @Disabled("No longer adding in things here or changing them")
  void testManualTerminatingStarAndNonZeroDistanceInBetween() {

    List<LinkedLegs> starApproachLinkedLegs = new ArrayList<>(STRATEGY.links(
        manualTerminatingStarWithOnlyOneFix(),
        fixOriginatingApproach(0.75))
    );

    assertAll(
        () -> assertEquals(2, starApproachLinkedLegs.size()),

        () -> assertEquals(PathTerminator.IF, starApproachLinkedLegs.get(0).source().pathTerminator()),
        () -> assertEquals("first star leg", starApproachLinkedLegs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.DF, starApproachLinkedLegs.get(0).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(0).target().associatedFix().get().fixIdentifier()),

        () -> assertEquals(PathTerminator.DF, starApproachLinkedLegs.get(1).source().pathTerminator()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(1).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.FD, starApproachLinkedLegs.get(1).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(1).target().associatedFix().get().fixIdentifier())

    );
  }

  /**
   * Tests case where distance between the manual terminating leg of the star and the fix originating leg of the approach is zero.
   * The intended adjustment to the LinkedLegs in this instance is to remove the manual terminating leg and create a link between the
   * closest previous leg of the star with a fix and the initial approach leg.
   */
  @Test
  @Disabled("The FM if should link but should not change legs now")
  void testManualTerminatingStarAndZeroDistanceInBetween() {

    List<LinkedLegs> starApproachLinkedLegs = new ArrayList<>(STRATEGY.links(
        manualTerminatingStar(0.5),
        fixOriginatingApproach(0.5))
    );

    assertAll(
        () -> assertEquals(1, starApproachLinkedLegs.size()),

        () -> assertEquals(PathTerminator.IF, starApproachLinkedLegs.get(0).source().pathTerminator()),
        () -> assertEquals("first star leg", starApproachLinkedLegs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.FD, starApproachLinkedLegs.get(0).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(0).target().associatedFix().get().fixIdentifier())
    );
  }

  /**
   * Tests case where distance between a manual terminating leg from a sid (with only one fix) and the fix originating leg of the approach is zero.
   * The intended adjustment to the LinkedLegs in this instance is to keep the manual terminating leg from the sid because there is no
   * other fix it can refer to and add a cloned DF of the approach in between the sid and approach legs.
   * Therefore the linked legs would go from VA(no fix) -> VM(fix1) -> FD(fix1)
   * To VA(no fix) -> DF(fix1) -> FD(fix1)
   */
  @Test
  @Disabled("Keep the DM leg now")
  void testManualTerminatingSidWithOnlyOneFixAndZeroDistanceInBetween() {

    List<LinkedLegs> legs = new ArrayList<>(STRATEGY.links(
        manualTerminatingSidWithOnlyOneFix(),
        fixOriginatingApproach(0.5))
    );

    assertAll(
        () -> assertEquals(1, legs.size()),

        () -> assertEquals(PathTerminator.DF, legs.get(0).source().pathTerminator()),
        () -> assertEquals("fix originating approach leg", legs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.FD, legs.get(0).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", legs.get(0).target().associatedFix().get().fixIdentifier())
    );
  }

  /**
   * Tests case where distance between a manual terminating leg from a sid (with only one fix) and the fix originating leg of the approach is non zero.
   * The intended adjustment to the LinkedLegs in this instance is to keep the manual terminating leg from the sid because there is no
   * other fix it can refer to and add a cloned DF of the approach in between the sid and approach legs.
   * Therefore the linked legs would go from VA(no fix) -> VM(fix1) -> FD(fix2)
   * To VA(no fix) -> VM(fix1) -> DF(fix2) -> FD(fix2)
   */
  @Test
  @Disabled("Not removing the VM leg and the VM leg should link to the FD no issue")
  void testManualTerminatingSidWithOnlyOneFixAndNonZeroDistanceInBetween() {

    List<LinkedLegs> legs = new ArrayList<>(STRATEGY.links(
        manualTerminatingSidWithOnlyOneFix(),
        fixOriginatingApproach(0.75))
    );

    assertAll(
        () -> assertEquals(2, legs.size()),

        () -> assertEquals(VM, legs.get(0).source().pathTerminator()),
        () -> assertEquals("manual terminating sid leg", legs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.DF, legs.get(0).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", legs.get(0).target().associatedFix().get().fixIdentifier()),

        () -> assertEquals(PathTerminator.DF, legs.get(1).source().pathTerminator()),
        () -> assertEquals("fix originating approach leg", legs.get(1).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.FD, legs.get(1).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", legs.get(1).target().associatedFix().get().fixIdentifier())
    );
  }

  /**
   * Since all fixes are fix terminating legs and in this instance there is zero distance, the appropriate response is to do nothing
   */
  @Test
  void testFixToApproachWithZeroDistance() {

    List<LinkedLegs> legs = new ArrayList<>(STRATEGY.links(
        ResolvedToken.directToFix(createFix("myFix", 0, 0)),
        fixOriginatingApproach(0))
    );

    assertAll(
        () -> assertEquals(1, legs.size()),
        () -> assertEquals(PathTerminator.DF, legs.get(0).source().pathTerminator()),
        () -> assertEquals("myFix", legs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.FD, legs.get(0).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", legs.get(0).target().associatedFix().get().fixIdentifier())
    );
  }

  /**
   * Since all fixes are fix terminating legs and in this instance there is non zero distance, so the appropriate response is
   * make a DF clone the approach leg, and insert in between
   */
  @Test
  @Disabled("not dong this in the linker now, it just links")
  void testFixToApproachWithNonZeroDistance() {

    List<LinkedLegs> legs = new ArrayList<>(STRATEGY.links(
        ResolvedToken.directToFix(createFix("myFix", 0, 0)),
        fixOriginatingApproach(0.5))
    );

    assertAll(
        () -> assertEquals(2, legs.size()),

        () -> assertEquals(PathTerminator.DF, legs.get(0).source().pathTerminator()),
        () -> assertEquals("myFix", legs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.DF, legs.get(0).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", legs.get(0).target().associatedFix().get().fixIdentifier()),

        () -> assertEquals(PathTerminator.DF, legs.get(1).source().pathTerminator()),
        () -> assertEquals("fix originating approach leg", legs.get(1).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.FD, legs.get(1).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", legs.get(1).target().associatedFix().get().fixIdentifier())
    );
  }

  /**
   * Airway to approach test where fix terminating airway leg has non zero distance from approach leg so create DF clone
   * of first approach leg.
   */
  @Test
  @Disabled("doe snot really matter if its an airway either, not doign this now here")
  void testAirwayToApproachWithNonZeroDistance() {

    List<LinkedLegs> legs = new ArrayList<>(STRATEGY.links(
        ResolvedToken.standardAirway(Airways.UM219()),
        fixOriginatingApproach(0.5))
    );

    assertAll(
        () -> assertEquals(2, legs.size()),

        () -> assertEquals(PathTerminator.TF, legs.get(0).source().pathTerminator()),
        () -> assertEquals("MYDIA", legs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.DF, legs.get(0).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", legs.get(0).target().associatedFix().get().fixIdentifier()),

        () -> assertEquals(PathTerminator.DF, legs.get(1).source().pathTerminator()),
        () -> assertEquals("fix originating approach leg", legs.get(1).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.FD, legs.get(1).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", legs.get(1).target().associatedFix().get().fixIdentifier())
    );
  }

  private ResolvedToken.StarEnrouteCommon fixTerminatingStar(double endingLongitude) {

    Leg l1_1 = createLeg("first star leg", 0.0, 0.0, PathTerminator.IF);
    Leg l1_2 = createLeg("fix terminating star leg", 0.0, endingLongitude, PathTerminator.TF);

    Transition ab = transition("fixTerminatingStar", "ALPHA1", "APT", TransitionType.ENROUTE, ProcedureType.STAR, Arrays.asList(l1_1, l1_2));
    return ResolvedToken.starEnrouteCommon(newProcedureGraph(newProcedure(List.of(ab))));
  }

  private ResolvedToken.StarEnrouteCommon manualTerminatingStar(double endingLongitude) {

    Leg l1_1 = createLeg("first star leg", 0.0, 0.0, PathTerminator.IF);
    Leg l1_2 = createLeg("manual terminating star leg", 0.0, endingLongitude, PathTerminator.FM);

    Transition ab = transition("manualTerminatingStar", "ALPHA1", "APT", TransitionType.ENROUTE, ProcedureType.STAR, Arrays.asList(l1_1, l1_2));
    return ResolvedToken.starEnrouteCommon(newProcedureGraph(newProcedure(List.of(ab))));
  }

  private ResolvedToken.SidEnrouteCommon manualTerminatingSidWithOnlyOneFix() {

    Leg l1_1 = Leg.builder(PathTerminator.VA, 0).build();
    Leg l1_2 = VM();

    Transition ab = transition("manualTerminatingSid", "ALPHA1", "APT", TransitionType.ENROUTE, ProcedureType.SID, Arrays.asList(l1_1, l1_2));
    return ResolvedToken.sidEnrouteCommon(newProcedureGraph(newProcedure(List.of(ab))));
  }

  private ResolvedToken.SidEnrouteCommon manualTerminatingStarWithOnlyOneFix() {

    Leg l1_1 = Leg.builder(PathTerminator.VA, 0).build();
    Leg l1_2 = VM();

    Transition ab = transition("manualTerminatingStar", "ALPHA1", "APT", TransitionType.COMMON, ProcedureType.STAR, Arrays.asList(l1_1, l1_2));
    return ResolvedToken.sidEnrouteCommon(newProcedureGraph(newProcedure(List.of(ab))));
  }

  private ResolvedToken.SidEnrouteCommon viApproachTransition() {

    Leg l1_1 = Leg.builder(PathTerminator.IF, 0).build();
    Leg l1_2 = VI();

    Transition ab = transition("manualTerminatingStar", "ALPHA1", "APT", TransitionType.APPROACH, ProcedureType.APPROACH, Arrays.asList(l1_1, l1_2));
    return ResolvedToken.sidEnrouteCommon(newProcedureGraph(newProcedure(List.of(ab))));
  }

  private ResolvedToken.StandardApproach fixOriginatingApproach(double startingLongitude) {

    Leg l2_1 = createLeg("fix originating approach leg", 0.0, startingLongitude, PathTerminator.FD);
    Leg l2_2 = createLeg("next approach leg", 0.0, startingLongitude + 0.5, PathTerminator.TF);

    Transition bc = transition("fixOriginatingApproach", "ALPHA2", "APT", TransitionType.APPROACH, ProcedureType.APPROACH, Arrays.asList(l2_1, l2_2));
    return ResolvedToken.standardApproach(newProcedureGraph(newProcedure(List.of(bc))));
  }

  private ResolvedToken.StandardApproach nonFixOriginatingApproach(double startingLongitude) {

    Leg l2_1 = createLeg("non fix originating approach leg", 0.0, startingLongitude, PathTerminator.TF);
    Leg l2_2 = createLeg("next approach leg", 0.0, startingLongitude + 0.5, PathTerminator.TF);

    Transition bc = transition("nonFixOriginatingApproach", "ALPHA2", "APT", TransitionType.APPROACH, ProcedureType.APPROACH, Arrays.asList(l2_1, l2_2));
    return ResolvedToken.standardApproach(newProcedureGraph(newProcedure(List.of(bc))));
  }

  private static Leg createLeg(String name, double lat, double lon, PathTerminator type) {
    return Leg.builder(type, 0).associatedFix(createFix(name, lat, lon)).build();
  }

  private static Fix createFix(String name, double lat, double lon) {
    return Fix.builder().fixIdentifier(name).latLong(LatLong.of(lat, lon)).magneticVariation(MagneticVariation.ZERO).build();
  }

  private static final class LinkingStrategy {

    private final TokenMapper mapper;

    private LinkingStrategy(TokenMapper mapper) {
      this.mapper = requireNonNull(mapper);
    }

    private List<LinkedLegs> links(ResolvedToken left, ResolvedToken right) {
      return new ArrayList<>(mapper.map(left).accept(mapper.map(right)).links());
    }
  }
}
