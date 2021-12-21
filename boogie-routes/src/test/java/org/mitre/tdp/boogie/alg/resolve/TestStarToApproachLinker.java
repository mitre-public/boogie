package org.mitre.tdp.boogie.alg.resolve;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
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

public class TestStarToApproachLinker {

  private static StarToApproachLinker linker = StarToApproachLinker.INSTANCE;

  /**
   * Test case to ensure that when an approach element starts with a non fix originating leg, an exception is thrown.
   */
  @Test
  void testNonFixOriginatingApproach() {
    StarElement star = fixTerminatingStar(0, 1, 0.5);
    ApproachElement approach = nonFixOriginatingApproach(2, 3, 0.5);

    assertThrows(IllegalArgumentException.class, () -> linker.apply(star, approach));
  }

  /**
   * Tests case where distance between final fix terminating leg from the star and fix originating leg of the approach is zero.
   * The intended adjustment to the LinkedLegs in this instance is to do nothing.
   */
  @Test
  void testFixTerminatingStarAndZeroDistanceInBetween() {
    StarElement star = fixTerminatingStar(0, 1, 0.5);
    ApproachElement approach = fixOriginatingApproach(2, 3, 0.5);
    List<LinkedLegs> starApproachLinkedLegs = linker.apply(star, approach);

    assertAll(
        () -> assertEquals(1, starApproachLinkedLegs.size()),
        () -> assertEquals(PathTerminator.TF, starApproachLinkedLegs.get(0).source().pathTerminator()),
        () -> assertEquals(1, starApproachLinkedLegs.get(0).source().sequenceNumber()),
        () -> assertEquals(PathTerminator.FD, starApproachLinkedLegs.get(0).target().pathTerminator()),
        () -> assertEquals(2, starApproachLinkedLegs.get(0).target().sequenceNumber())
    );
  }

  /**
   * Tests case where distance between fix terminating leg in the star and fix originating leg in the approach is non zero.
   * The intended adjustment to the LinkedLegs in this instance is to clone the fix originating leg in the approach, convert it to a DF leg
   * and insert it in between the final star leg and initial approach leg.
   */
  @Test
  void testFixTerminatingStarAndNonZeroDistanceInBetween() {
    StarElement star = fixTerminatingStar(0, 1, 0.5);
    ApproachElement approach = fixOriginatingApproach(2, 3, 0.75);
    List<LinkedLegs> starApproachLinkedLegs = linker.apply(star, approach);

    assertAll(
        () -> assertEquals(2, starApproachLinkedLegs.size()),
        () -> assertEquals(PathTerminator.TF, starApproachLinkedLegs.get(0).source().pathTerminator()),
        () -> assertEquals(1, starApproachLinkedLegs.get(0).source().sequenceNumber()),
        () -> assertEquals(PathTerminator.DF, starApproachLinkedLegs.get(0).target().pathTerminator()),
        () -> assertEquals(2, starApproachLinkedLegs.get(0).target().sequenceNumber()),
        () -> assertEquals(approach.procedure().transitions().stream().collect(Collectors.toList()).get(0).legs().get(0).associatedFix().get().fixIdentifier(), starApproachLinkedLegs.get(0).target().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.DF, starApproachLinkedLegs.get(1).source().pathTerminator()),
        () -> assertEquals(2, starApproachLinkedLegs.get(1).source().sequenceNumber()),
        () -> assertEquals(PathTerminator.FD, starApproachLinkedLegs.get(1).target().pathTerminator()),
        () -> assertEquals(3, starApproachLinkedLegs.get(1).target().sequenceNumber())
    );
  }

  /**
   * Tests case where distance between the manual terminating leg of the star and the fix originating leg of the approach is non zero.
   * The intended adjustment to the LinkedLegs in this instance is to replace the manual terminating leg of the star with a
   * clone of the fix origiating leg in the approach. The cloned leg will also be converted to a DF leg.
   */
  @Test
  void testManualTerminatingStarAndNonZeroDistanceInBetween() {
    StarElement star = manualTerminatingStar(0, 1, 0.5);
    ApproachElement approach = fixOriginatingApproach(2, 3, 0.75);
    List<LinkedLegs> starApproachLinkedLegs = linker.apply(star, approach);

    assertAll(
        () -> assertEquals(1, starApproachLinkedLegs.size()),
        () -> assertEquals(PathTerminator.DF, starApproachLinkedLegs.get(0).source().pathTerminator()),
        () -> assertEquals(1, starApproachLinkedLegs.get(0).source().sequenceNumber()),
        () -> assertEquals(approach.procedure().transitions().stream().collect(Collectors.toList()).get(0).legs().get(0).associatedFix().get().fixIdentifier(), starApproachLinkedLegs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.FD, starApproachLinkedLegs.get(0).target().pathTerminator()),
        () -> assertEquals(2, starApproachLinkedLegs.get(0).target().sequenceNumber())
    );
  }

  /**
   * Tests case where distance between the manual terminating leg of the star and the fix originating leg of the approach is zero.
   * This is a unique case where the manual terminating leg is starting at the same spot as the fix originating leg. So we drop the final manual
   * terminating leg of the star, take the closest leg prior, and insert a DF to the approach in between the new manual termianting leg
   * and the approach leg.
   */
  @Test
  void testManualTerminatingStarAndZeroDistanceInBetween() {
    StarElement star = manualTerminatingStar(0, 1, 0.5);
    ApproachElement approach =  fixOriginatingApproach(2, 3, 0.5);
    List<LinkedLegs> starApproachLinkedLegs = linker.apply(star, approach);

    assertAll(
        () -> assertEquals(2, starApproachLinkedLegs.size()),
        () -> assertEquals(PathTerminator.IF, starApproachLinkedLegs.get(0).source().pathTerminator()),
        () -> assertEquals(0, starApproachLinkedLegs.get(0).source().sequenceNumber()),
        () -> assertEquals(star.procedure().transitions().stream().collect(Collectors.toList()).get(0).legs().get(0).associatedFix().get().fixIdentifier(), starApproachLinkedLegs.get(0).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.DF, starApproachLinkedLegs.get(0).target().pathTerminator()),
        () -> assertEquals(1, starApproachLinkedLegs.get(0).target().sequenceNumber()),
        () -> assertEquals(approach.procedure().transitions().stream().collect(Collectors.toList()).get(0).legs().get(0).associatedFix().get().fixIdentifier(), starApproachLinkedLegs.get(0).target().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.DF, starApproachLinkedLegs.get(1).source().pathTerminator()),
        () -> assertEquals(1, starApproachLinkedLegs.get(1).source().sequenceNumber()),
        () -> assertEquals(approach.procedure().transitions().stream().collect(Collectors.toList()).get(0).legs().get(0).associatedFix().get().fixIdentifier(), starApproachLinkedLegs.get(1).source().associatedFix().get().fixIdentifier()),
        () -> assertEquals(PathTerminator.FD, starApproachLinkedLegs.get(1).target().pathTerminator()),
        () -> assertEquals(2, starApproachLinkedLegs.get(1).target().sequenceNumber())
    );
  }

  private StarElement fixTerminatingStar(int firstSequenceNumber, int secondSequenceNumber, double endingLongitude) {
    Leg l1_1 = createLeg("first star leg", 0.0, 0.0, PathTerminator.IF, firstSequenceNumber);
    Leg l1_2 = createLeg("fix terminating star leg", 0.0, endingLongitude, PathTerminator.TF, secondSequenceNumber);
    Transition ab = transition("fixTerminatingStar", "ALPHA1", "APT", TransitionType.ENROUTE, ProcedureType.STAR, Arrays.asList(l1_1, l1_2));

    return new StarElement(newProcedureGraph(newProcedure(Arrays.asList(ab))));
  }

  private StarElement manualTerminatingStar(int firstSequenceNumber, int secondSequenceNumber, double endingLongitude) {
    Leg l1_1 = createLeg("first star leg", 0.0, 0.0, PathTerminator.IF, firstSequenceNumber);
    Leg l1_2 = createLeg("manual terminating star leg", 0.0, endingLongitude, PathTerminator.FM, secondSequenceNumber);
    Transition ab = transition("manualTerminatingStar", "ALPHA1", "APT", TransitionType.ENROUTE, ProcedureType.STAR, Arrays.asList(l1_1, l1_2));

    return new StarElement(newProcedureGraph(newProcedure(Arrays.asList(ab))));
  }

  private ApproachElement fixOriginatingApproach(int firstSequenceNumber, int secondSequenceNumber, double startingLongitude) {
    Leg l2_1 = createLeg("fix originating approach leg", 0.0, startingLongitude, PathTerminator.FD, firstSequenceNumber);
    Leg l2_2 = createLeg("next approach leg", 0.0, startingLongitude + 0.5, PathTerminator.TF, secondSequenceNumber);
    Transition bc = transition("fixOriginatingApproach", "ALPHA2", "APT", TransitionType.APPROACH, ProcedureType.APPROACH, Arrays.asList(l2_1, l2_2));

    return new ApproachElement(newProcedureGraph(newProcedure(Arrays.asList(bc))));
  }

  private ApproachElement nonFixOriginatingApproach(int firstSequenceNumber, int secondSequenceNumber, double startingLongitude) {
    Leg l2_1 = createLeg("non fix originating approach leg", 0.0, startingLongitude, PathTerminator.TF, firstSequenceNumber);
    Leg l2_2 = createLeg("next approach leg", 0.0, startingLongitude + 0.5, PathTerminator.TF, secondSequenceNumber);
    Transition bc = transition("nonFixOriginatingApproach", "ALPHA2", "APT", TransitionType.APPROACH, ProcedureType.APPROACH, Arrays.asList(l2_1, l2_2));

    return new ApproachElement(newProcedureGraph(newProcedure(Arrays.asList(bc))));
  }

  private static BoogieLeg createLeg(String name, double lat, double lon, PathTerminator type, int sequenceNumber) {
    return new BoogieLeg.Builder()
        .pathTerminator(type)
        .associatedFix(createFix(name, lat, lon))
        .sequenceNumber(sequenceNumber)
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
