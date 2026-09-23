package org.mitre.tdp.boogie.alg.chooser.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.caasd.commons.Distance.ofNauticalMiles;
import static org.mitre.tdp.boogie.alg.chooser.graph.LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;

class PointsWithinRangeOrClosestTest {

  @Test
  void returnsAllRadiusLinksSortedByDistanceWithStableTies() {
    Leg leftFirst = newLeg("LEFT1", 0., 0.);
    Leg leftSecond = newLeg("LEFT2", 0., 0.);
    Leg far = newLeg("FAR", 0., .1);
    Leg near = newLeg("NEAR", 0., .01);
    Leg outside = newLeg("OUTSIDE", 0., 5.);

    List<LinkedLegs> expected = List.of(
        new LinkedLegs(leftFirst, near, distance(leftFirst, near)),
        new LinkedLegs(leftSecond, near, distance(leftSecond, near)),
        new LinkedLegs(leftFirst, far, distance(leftFirst, far)),
        new LinkedLegs(leftSecond, far, distance(leftSecond, far))
    );

    assertEquals(expected, Linker.pointsWithinRangeOrClosest(
        ofNauticalMiles(20.), newResolvedElement(leftFirst, leftSecond), newResolvedElement(far, near, outside)
    ).links());
  }

  @Test
  void fallsBackToNearestLinkAndKeepsFirstEncounterForTies() {
    Leg leftFirst = newLeg("LEFT1", 0., 0.);
    Leg leftSecond = newLeg("LEFT2", 0., 0.);
    Leg far = newLeg("FAR", 0., 2.);
    Leg nearFirst = newLeg("NEAR1", 0., 1.);
    Leg nearSecond = newLeg("NEAR2", 0., 1.);

    assertEquals(
        List.of(new LinkedLegs(leftFirst, nearFirst, distance(leftFirst, nearFirst))),
        Linker.pointsWithinRangeOrClosest(
            ofNauticalMiles(.25), newResolvedElement(leftFirst, leftSecond), newResolvedElement(far, nearFirst, nearSecond)
        ).links()
    );
  }

  @Test
  void excludesLinksExactlyAtRangeWhenAnInteriorLinkExists() {
    Distance range = ofNauticalMiles(10.);
    Fix leftFix = newMockFix(LatLong.of(0., 0.));
    Leg left = newLeg(leftFix);
    Leg boundary = newLeg("BOUNDARY", 0., 1.);
    Leg interior = newLeg("INTERIOR", 0., .01);
    when(leftFix.distanceInNmTo(boundary.associatedFix().orElseThrow())).thenReturn(range.inNauticalMiles());

    assertEquals(
        List.of(new LinkedLegs(left, interior, distance(left, interior))),
        Linker.pointsWithinRangeOrClosest(range, newResolvedElement(left), newResolvedElement(boundary, interior)).links()
    );
  }

  @Test
  void appliesWeightFloorToRadiusButUsesRawDistanceForNearestAndSorting() {
    Fix leftFix = newMockFix(LatLong.of(0., 0.));
    Leg left = newLeg(leftFix);
    Leg almost = newLeg("ALMOST", 0., .01);
    Leg coincident = newLeg("SAME", 0., 0.);
    when(leftFix.distanceInNmTo(almost.associatedFix().orElseThrow())).thenReturn(SAME_ELEMENT_MATCH_WEIGHT * .25);

    LinkableToken leftToken = newResolvedElement(left);
    LinkableToken rightToken = newResolvedElement(almost, coincident);
    LinkedLegs nearest = new LinkedLegs(left, coincident, SAME_ELEMENT_MATCH_WEIGHT);
    LinkedLegs second = new LinkedLegs(left, almost, SAME_ELEMENT_MATCH_WEIGHT);

    assertEquals(
        List.of(nearest),
        Linker.pointsWithinRangeOrClosest(ofNauticalMiles(SAME_ELEMENT_MATCH_WEIGHT * .5), leftToken, rightToken).links(),
        "Both clamped weights exceed the radius, so only the closest link should remain."
    );
    assertEquals(
        List.of(nearest, second),
        Linker.pointsWithinRangeOrClosest(ofNauticalMiles(SAME_ELEMENT_MATCH_WEIGHT * 2.), leftToken, rightToken).links(),
        "Equal clamped weights must retain their ordering by actual distance."
    );
  }

  @Test
  void missingPositionedLegsAllowTheNextFallback() {
    Leg noFix = Leg.builder(PathTerminator.VM, 0).outboundMagneticCourse(0.).build();
    Leg positioned = newLeg("FIX", 0., 0.);
    LinkableToken positionedToken = newResolvedElement(positioned);
    List<LinkedLegs> fallbackLinks = List.of(new LinkedLegs(noFix, positioned, 1.));

    for (LinkableToken missing : List.of(newResolvedElement(), newResolvedElement(noFix))) {
      Linker linker = Linker.pointsWithinRangeOrClosest(ofNauticalMiles(1.), missing, positionedToken);
      assertTrue(linker.links().isEmpty());
      assertEquals(fallbackLinks, linker.orElseTry(() -> fallbackLinks).links());
      assertTrue(Linker.pointsWithinRangeOrClosest(ofNauticalMiles(1.), positionedToken, missing).links().isEmpty());
    }
  }

  @ParameterizedTest
  @ValueSource(doubles = {.25, 1_000.})
  void calculatesEachUniquePairDistanceOnceOnRadiusHitOrMiss(double rangeNm) {
    Fix f1 = newMockFix(LatLong.of(0., 0.));
    Fix f2 = newMockFix(LatLong.of(.5, .5));
    Fix f3 = newMockFix(LatLong.of(1., 1.));
    Fix f4 = newMockFix(LatLong.of(1.5, 1.5));
    Leg l1 = newLeg(f1);
    Leg l2 = newLeg(f2);
    Leg l3 = newLeg(f3);
    Leg l4 = newLeg(f4);
    LinkableToken left = newResolvedElement(l1, l2, l1);
    LinkableToken right = newResolvedElement(l3, l4, l3);
    clearInvocations(f1, f2, f3, f4);

    Linker.pointsWithinRangeOrClosest(ofNauticalMiles(rangeNm), left, right).links();

    verify(f1).distanceInNmTo(f3);
    verify(f1).distanceInNmTo(f4);
    verify(f2).distanceInNmTo(f3);
    verify(f2).distanceInNmTo(f4);
    Stream.of(f1, f2, f3, f4).forEach(fix -> verify(fix, times(2)).latLong());
  }

  private static double distance(Leg source, Leg target) {
    return source.associatedFix().orElseThrow().distanceInNmTo(target.associatedFix().orElseThrow());
  }

  private static LinkableToken newResolvedElement(Leg... legs) {
    List<LinkedLegs> links = Stream.of(legs)
        .map(leg -> new LinkedLegs(leg, leg, SAME_ELEMENT_MATCH_WEIGHT))
        .toList();
    LinkableToken token = mock(LinkableToken.class);
    when(token.graphRepresentation()).thenReturn(links);
    return token;
  }

  private static Leg newLeg(String identifier, double latitude, double longitude) {
    return newLeg(Fix.builder().fixIdentifier(identifier).latLong(LatLong.of(latitude, longitude)).build());
  }

  private static Leg newLeg(Fix fix) {
    return Leg.dfBuilder(fix, 0).build();
  }

  private static Fix newMockFix(LatLong location) {
    Fix fix = mock(Fix.class, CALLS_REAL_METHODS);
    when(fix.latLong()).thenReturn(location);
    return fix;
  }
}
