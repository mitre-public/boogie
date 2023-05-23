package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;

class LinkerTest {

  @Test
  void testClosestPointBetween() {

    Leg l1 = newLeg(LatLong.of(0., 0.));
    Leg l2 = newLeg(LatLong.of(.5, .5));
    Leg l3 = newLeg(LatLong.of(1., 1.));

    GraphableToken r1 = newResolvedElement(l1, l2);
    GraphableToken r2 = newResolvedElement(l3);

    Collection<LinkedLegs> linkedLegs = Linker.closestPointBetween(r1, r2).links();

    List<LinkedLegs> expected = singletonList(new LinkedLegs(l2, l3, 42.42937759769261)); // eh decimal equals

    assertEquals(expected, linkedLegs);
  }

  private GraphableToken newResolvedElement(Leg... legs) {
    List<LinkedLegs> linkedLegs = Stream.of(legs).map(leg -> new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT)).collect(toList());

    GraphableToken resolvedElement = mock(GraphableToken.class);
    when(resolvedElement.linkedLegs()).thenReturn(linkedLegs);

    return resolvedElement;
  }

  private Leg newLeg(LatLong location) {
    Fix fix = spy(Fix.class);
    when(fix.fixIdentifier()).thenReturn("MOCK");
    when(fix.fixRegion()).thenReturn("MOCK");
    when(fix.latLong()).thenReturn(location);

    Leg leg = mock(Leg.class);
    when(leg.associatedFix()).thenReturn((Optional) Optional.of(fix));
    when(leg.pathTerminator()).thenReturn(PathTerminator.DF);
    when(leg.toString()).thenReturn(location.toString());

    return leg;
  }
}
