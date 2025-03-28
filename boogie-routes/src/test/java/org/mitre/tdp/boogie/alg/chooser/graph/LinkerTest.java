package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;

class LinkerTest {

  @Test
  void testClosestPointBetween() {

    Leg l1 = newLeg(LatLong.of(0., 0.));
    Leg l2 = newLeg(LatLong.of(.5, .5));
    Leg l3 = newLeg(LatLong.of(1., 1.));

    LinkableToken r1 = newResolvedElement(l1, l2);
    LinkableToken r2 = newResolvedElement(l3);

    Collection<LinkedLegs> linkedLegs = Linker.closestPointBetween(r1, r2).links();

    List<LinkedLegs> expected = singletonList(new LinkedLegs(l2, l3, 42.42937759769261)); // eh decimal equals

    assertEquals(expected, linkedLegs);
  }

  private LinkableToken newResolvedElement(Leg... legs) {
    List<LinkedLegs> linkedLegs = Stream.of(legs).map(leg -> new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT)).collect(toList());

    LinkableToken resolvedElement = mock(LinkableToken.class);
    when(resolvedElement.graphRepresentation()).thenReturn(linkedLegs);

    return resolvedElement;
  }

  private Leg newLeg(LatLong location) {
    return Leg.dfBuilder(Fix.builder().fixIdentifier("MOCK").latLong(location).build(), 0).build();
  }
}
