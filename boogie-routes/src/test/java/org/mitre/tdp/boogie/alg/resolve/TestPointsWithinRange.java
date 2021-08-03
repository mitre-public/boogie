package org.mitre.tdp.boogie.alg.resolve;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;

class TestPointsWithinRange {

  @Test
  void testSinglePointWithinRange() {
    Leg l1 = newLeg(LatLong.of(0., 0.));
    Leg l2 = newLeg(LatLong.of(0., 1.));
    Leg l3 = newLeg(LatLong.of(1., 1.));

    ResolvedElement r1 = newResolvedElement(l1, l2);
    ResolvedElement r2 = newResolvedElement(l2, l3);

    List<LinkedLegs> linkedLegs = PointsWithinRange.INSTANCE.apply(r1, r2);

    assertAll(
        () -> assertEquals(1, linkedLegs.size(), "Should only be one shared linking between the provided elements."),
        () -> assertEquals(0., linkedLegs.get(0).linkWeight(), "Link weight should be zero as they are the same leg.")
    );
  }

  @Test
  void testMultiplePointsWithinRange() {
    Leg l1 = newLeg(LatLong.of(0., 0.));
    Leg l2 = newLeg(LatLong.of(0., 1.));
    Leg l3 = newLeg(LatLong.of(1., 1.));

    ResolvedElement r1 = newResolvedElement(l1, l2, l3);
    ResolvedElement r2 = newResolvedElement(l2, l3);

    List<LinkedLegs> linkedLegs = PointsWithinRange.INSTANCE.apply(r1, r2);

    List<LinkedLegs> expected = Arrays.asList(
        new LinkedLegs(l2, l2, 0.),
        new LinkedLegs(l3, l3, 0.)
    );

    assertAll(
        () -> assertEquals(2, linkedLegs.size(), "Should only be one shared linking between the provided elements."),
        () -> assertEquals(expected, linkedLegs, "l2->l2, l3->l3 should be linked between the elements.")
    );
  }

  private ResolvedElement newResolvedElement(Leg... legs) {
    List<LinkedLegs> linkedLegs = Stream.of(legs).map(leg -> new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT)).collect(toList());

    ResolvedElement resolvedElement = mock(ResolvedElement.class);
    when(resolvedElement.toLinkedLegs()).thenReturn(linkedLegs);

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
