package org.mitre.tdp.boogie.alg.resolve;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airways;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.model.BoogieFix;
import org.mitre.tdp.boogie.model.BoogieLeg;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mitre.tdp.boogie.MockObjects.transition;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedure;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedureGraph;

public class TestApproachElement {

  /**
   * Test case to ensure that when an approach element starts with a non fix originating leg, an exception is thrown.
   */
  @Test
  void testNonFixOriginatingApproach() {
    StarElement star = fixTerminatingStar(0.5);
    ApproachElement approach = nonFixOriginatingApproach(0.5);

    assertThrows(IllegalArgumentException.class, () -> approach.visit(star));
  }

  /**
   * Tests case where distance between final fix terminating leg from the star and fix originating leg of the approach is zero.
   * The intended adjustment to the LinkedLegs in this instance is to do nothing.
   */
  @Test
  void testFixTerminatingStarAndZeroDistanceInBetween() {
    StarElement star = fixTerminatingStar(0.5);
    ApproachElement approach = fixOriginatingApproach(0.5);
    List<LinkedLegs> starApproachLinkedLegs = approach.visit(star);

    assertAll(
        () -> assertEquals(1, starApproachLinkedLegs.size()),

        () -> assertEquals(PathTerminator.TF, starApproachLinkedLegs.get(0).source().pathTerminator()),
        () -> assertEquals("fix terminating star leg", starApproachLinkedLegs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.FD, starApproachLinkedLegs.get(0).target().pathTerminator()),
        () -> assertEquals("fix originating approach leg", starApproachLinkedLegs.get(0).target().associatedFix().get().fixIdentifier())
    );
  }

  /**
   * Tests case where distance between fix terminating leg in the star and fix originating leg in the approach is non zero.
   * The intended adjustment to the LinkedLegs in this instance is to clone the fix originating leg in the approach, convert it to a DF leg
   * and insert it in between the final star leg and initial approach leg.
   */
  @Test
  void testFixTerminatingStarAndNonZeroDistanceInBetween() {
    StarElement star = fixTerminatingStar(0.5);
    ApproachElement approach = fixOriginatingApproach(0.75);
    List<LinkedLegs> starApproachLinkedLegs = approach.visit(star);

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
  void testManualTerminatingStarAndNonZeroDistanceInBetween() {
    StarElement star = manualTerminatingStar(0.5);
    ApproachElement approach = fixOriginatingApproach(0.75);
    List<LinkedLegs> starApproachLinkedLegs = approach.visit(star);

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
  void testManualTerminatingStarAndZeroDistanceInBetween() {
    StarElement star = manualTerminatingStar(0.5);
    ApproachElement approach = fixOriginatingApproach(0.5);
    List<LinkedLegs> starApproachLinkedLegs = approach.visit(star);

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
  void testManualTerminatingSidWithOnlyOneFixAndZeroDistanceInBetween() {
    SidElement sid = manualTerminatingSidWithOnlyOneFix(0.5);
    ApproachElement approach = fixOriginatingApproach(0.5);
    List<LinkedLegs> legs = approach.visit(sid);

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
  void testManualTerminatingSidWithOnlyOneFixAndNonZeroDistanceInBetween() {
    SidElement sid = manualTerminatingSidWithOnlyOneFix(0.5);
    ApproachElement approach = fixOriginatingApproach(0.75);
    List<LinkedLegs> legs = approach.visit(sid);

    assertAll(
        () -> assertEquals(2, legs.size()),

        () -> assertEquals(PathTerminator.VM, legs.get(0).source().pathTerminator()),
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
    FixElement fix = new FixElement(createFix("myFix", 0, 0), " ");
    ApproachElement approach = fixOriginatingApproach(0);
    List<LinkedLegs> legs = approach.visit(fix);

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
  void testFixToApproachWithNonZeroDistance() {
    FixElement fix = new FixElement(createFix("myFix", 0, 0), " ");
    ApproachElement approach = fixOriginatingApproach(0.5);
    List<LinkedLegs> legs = approach.visit(fix);

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
  void testAirwayToApproachWithNonZeroDistance() {
    AirwayElement airway = new AirwayElement(Airways.UM219());
    ApproachElement approach = fixOriginatingApproach(0.5);
    List<LinkedLegs> legs = approach.visit(airway);

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

  private StarElement fixTerminatingStar(double endingLongitude) {
    Leg l1_1 = createLeg("first star leg", 0.0, 0.0, PathTerminator.IF);
    Leg l1_2 = createLeg("fix terminating star leg", 0.0, endingLongitude, PathTerminator.TF);
    Transition ab = transition("fixTerminatingStar", "ALPHA1", "APT", TransitionType.ENROUTE, ProcedureType.STAR, Arrays.asList(l1_1, l1_2));

    return new StarElement(newProcedureGraph(newProcedure(Arrays.asList(ab))));
  }

  private StarElement manualTerminatingStar(double endingLongitude) {
    Leg l1_1 = createLeg("first star leg", 0.0, 0.0, PathTerminator.IF);
    Leg l1_2 = createLeg("manual terminating star leg", 0.0, endingLongitude, PathTerminator.FM);
    Transition ab = transition("manualTerminatingStar", "ALPHA1", "APT", TransitionType.ENROUTE, ProcedureType.STAR, Arrays.asList(l1_1, l1_2));

    return new StarElement(newProcedureGraph(newProcedure(Arrays.asList(ab))));
  }

  private SidElement manualTerminatingSidWithOnlyOneFix(double endingLongitude) {
    Leg l1_1 = new BoogieLeg.Builder()
        .pathTerminator(PathTerminator.VA)
        .associatedFix(null)
        .build();
    Leg l1_2 = createLeg("manual terminating sid leg", 0.0, endingLongitude, PathTerminator.VM);
    Transition ab = transition("manualTerminatingSid", "ALPHA1", "APT", TransitionType.ENROUTE, ProcedureType.SID, Arrays.asList(l1_1, l1_2));

    return new SidElement(newProcedureGraph(newProcedure(Arrays.asList(ab))));
  }

  private ApproachElement fixOriginatingApproach(double startingLongitude) {
    Leg l2_1 = createLeg("fix originating approach leg", 0.0, startingLongitude, PathTerminator.FD);
    Leg l2_2 = createLeg("next approach leg", 0.0, startingLongitude + 0.5, PathTerminator.TF);
    Transition bc = transition("fixOriginatingApproach", "ALPHA2", "APT", TransitionType.APPROACH, ProcedureType.APPROACH, Arrays.asList(l2_1, l2_2));

    return new ApproachElement(newProcedureGraph(newProcedure(Arrays.asList(bc))));
  }

  private ApproachElement nonFixOriginatingApproach(double startingLongitude) {
    Leg l2_1 = createLeg("non fix originating approach leg", 0.0, startingLongitude, PathTerminator.TF);
    Leg l2_2 = createLeg("next approach leg", 0.0, startingLongitude + 0.5, PathTerminator.TF);
    Transition bc = transition("nonFixOriginatingApproach", "ALPHA2", "APT", TransitionType.APPROACH, ProcedureType.APPROACH, Arrays.asList(l2_1, l2_2));

    return new ApproachElement(newProcedureGraph(newProcedure(Arrays.asList(bc))));
  }

  private static BoogieLeg createLeg(String name, double lat, double lon, PathTerminator type) {
    return new BoogieLeg.Builder()
        .pathTerminator(type)
        .associatedFix(createFix(name, lat, lon))
        .build();
  }

  private static BoogieFix createFix(String name, double lat, double lon) {
    return new BoogieFix.Builder()
        .fixIdentifier(name)
        .latitude(lat)
        .longitude(lon)
        .build();
  }
}
