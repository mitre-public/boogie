package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.caasd.commons.Distance.ofNauticalMiles;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;

class PointsWithinRangeTest {

  @Test
  void testSinglePointWithinRange() {

    Leg l1 = newLeg(LatLong.of(0., 0.));
    Leg l2 = newLeg(LatLong.of(0., 1.));
    Leg l3 = newLeg(LatLong.of(1., 1.));

    LinkableToken r1 = newResolvedElement(l1, l2);
    LinkableToken r2 = newResolvedElement(l2, l3);

    Collection<LinkedLegs> linkedLegs = Linker.pointsWithinRange(ofNauticalMiles(.25), r1, r2).links();

    assertAll(
        () -> assertEquals(1, linkedLegs.size(), "Should only be one shared linking between the provided elements."),
        () -> assertEquals(LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT, linkedLegs.iterator().next().linkWeight(), "Link weight should be zero as they are the same leg.")
    );
  }

  @Test
  void testMultiplePointsWithinRange() {

    Leg l1 = newLeg(LatLong.of(0., 0.));
    Leg l2 = newLeg(LatLong.of(0., 1.));
    Leg l3 = newLeg(LatLong.of(1., 1.));

    LinkableToken r1 = newResolvedElement(l1, l2, l3);
    LinkableToken r2 = newResolvedElement(l2, l3);

    Collection<LinkedLegs> linkedLegs = Linker.pointsWithinRange(ofNauticalMiles(.25), r1, r2).links();

    List<LinkedLegs> expected = Arrays.asList(
        new LinkedLegs(l2, l2, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT),
        new LinkedLegs(l3, l3, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT)
    );

    assertAll(
        () -> assertEquals(2, linkedLegs.size(), "Should only be one shared linking between the provided elements."),
        () -> assertEquals(expected, linkedLegs, "l2->l2, l3->l3 should be linked between the elements.")
    );
  }

  @Test
  void testCandidateDistancesAreCalculatedOnceBeforeSorting() {

    Fix f1 = newMockFix(LatLong.of(0., 0.));
    Fix f2 = newMockFix(LatLong.of(.5, .5));
    Fix f3 = newMockFix(LatLong.of(1., 1.));
    Fix f4 = newMockFix(LatLong.of(1.5, 1.5));

    LinkableToken r1 = newResolvedElement(newLeg(f1), newLeg(f2));
    LinkableToken r2 = newResolvedElement(newLeg(f3), newLeg(f4));
    clearInvocations(f1, f2, f3, f4);

    Linker.pointsWithinRange(ofNauticalMiles(1_000.), r1, r2).links();

    Stream.of(f1, f2, f3, f4).forEach(fix -> verify(fix, times(2)).latLong());
  }

  private LinkableToken newResolvedElement(Leg... legs) {

    List<LinkedLegs> linkedLegs = Stream.of(legs)
        .map(leg -> new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT))
        .collect(toList());

    LinkableToken resolvedElement = mock(LinkableToken.class);
    when(resolvedElement.graphRepresentation()).thenReturn(linkedLegs);

    return resolvedElement;
  }

  private Leg newLeg(LatLong location) {
    return Leg.dfBuilder(Fix.builder().fixIdentifier("MOCK").latLong(location).build(), 0).build();
  }

  private Leg newLeg(Fix fix) {
    return Leg.dfBuilder(fix, 0).build();
  }

  private Fix newMockFix(LatLong location) {
    Fix fix = mock(Fix.class, CALLS_REAL_METHODS);
    when(fix.latLong()).thenReturn(location);
    return fix;
  }
}
