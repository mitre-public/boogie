package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.caasd.commons.Distance.ofNauticalMiles;
import static org.mockito.Mockito.mock;
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
}
