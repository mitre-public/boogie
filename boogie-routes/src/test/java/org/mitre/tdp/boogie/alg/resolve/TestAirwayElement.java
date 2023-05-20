package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.util.Iterators;

class TestAirwayElement {

  // no hard exception
  @Test
  void testAirwayElementSafeWithAirwaysOfLessThanTwoLegs() {
    Leg leg = mock(Leg.class);

    Airway airway = mock(Airway.class);
    when(airway.legs()).thenReturn((List) Collections.singletonList(leg));

    AirwayToken element = new AirwayToken(airway);
    assertEquals(0, element.toLinkedLegs().size());
  }

  @Test
  void testAirwayElement() {
    Airway airway = singleAirway();

    AirwayToken element = new AirwayToken(airway);

    assertAll(
        () -> assertEquals(element.toLinkedLegs().size(), 6),
        () -> assertTrue(element.toLinkedLegs().stream().allMatch(l -> l.source().pathTerminator().equals(PathTerminator.TF) && l.target().pathTerminator().equals(PathTerminator.TF)))
    );

    List<LinkedLegs> linked = element.toLinkedLegs();

    LinkedLegs ll1_1 = linked.get(0);
    LinkedLegs ll1_2 = linked.get(1);

    assertAll(
        () -> assertEquals(ll1_1.source(), ll1_2.target()),
        () -> assertEquals(ll1_1.target(), ll1_2.source()),
        () -> assertEquals(ll1_1.source().associatedFix().map(Fix::fixIdentifier).orElse(null), "YYT"),
        () -> assertEquals(ll1_1.target().associatedFix().map(Fix::fixIdentifier).orElse(null), "YYZ")
    );

    LinkedLegs ll2_1 = linked.get(2);
    LinkedLegs ll2_2 = linked.get(3);

    assertAll(
        () -> assertEquals(ll2_1.source(), ll2_2.target()),
        () -> assertEquals(ll2_1.target(), ll2_2.source()),
        () -> assertEquals(ll2_1.source().associatedFix().map(Fix::fixIdentifier).orElse(null), "YYZ"),
        () -> assertEquals(ll2_1.target().associatedFix().map(Fix::fixIdentifier).orElse(null), "ZZT")
    );

    LinkedLegs ll3_1 = linked.get(4);
    LinkedLegs ll3_2 = linked.get(5);

    assertAll(
        () -> assertEquals(ll3_1.source(), ll3_2.target()),
        () -> assertEquals(ll3_1.target(), ll3_2.source()),
        () -> assertEquals(ll3_1.source().associatedFix().map(Fix::fixIdentifier).orElse(null), "ZZT"),
        () -> assertEquals(ll3_1.target().associatedFix().map(Fix::fixIdentifier).orElse(null), "ZZY")
    );
  }

  @Test
  void testAirwayElementSubsequentLegLinkReferences() {
    Airway airway = singleAirway();

    AirwayToken element = new AirwayToken(airway);

    List<LinkedLegs> linked = element.toLinkedLegs();

    List<LinkedLegs> forward = IntStream.range(0, linked.size() / 2)
        .map(i -> 2 * i)
        .mapToObj(linked::get)
        .collect(Collectors.toList());

    Iterators.pairwise(forward, (ll1, ll2) -> {
      assertEquals(ll1.target(), ll2.source(), "The target of the previous leg should be identical to the source of the next.");
      assertEquals(ll1.target().hashCode(), ll2.source().hashCode(), "The target of the previous leg should have the same hashCode as the source of the next");
    });

    List<LinkedLegs> backwards = IntStream.range(0, linked.size() / 2)
        .map(i -> (2 * i) + 1)
        .mapToObj(linked::get)
        .collect(Collectors.toList());

    Iterators.pairwise(backwards, (ll1, ll2) -> {
      assertEquals(ll1.source(), ll2.target(), "The target of the previous leg should be identical to the source of the next.");
      assertEquals(ll1.source().hashCode(), ll2.target().hashCode(), "The target of the previous leg should have the same hashCode as the source of the next");
    });
  }

  private static Airway singleAirway() {
    Leg l1 = TF("YYT", 0.0, 0.0);
    Leg l2 = TF("YYZ", 0.0, 1.0);
    Leg l3 = TF("ZZT", 0.0, 2.0);
    Leg l4 = TF("ZZY", 0.0, 3.0);

    Airway airway = mock(Airway.class);

    String airwayId = "J121";

    when(airway.legs()).thenReturn((List) Arrays.asList(l1, l2, l3, l4));
    when(airway.airwayIdentifier()).thenReturn(airwayId);
    return airway;
  }
}
