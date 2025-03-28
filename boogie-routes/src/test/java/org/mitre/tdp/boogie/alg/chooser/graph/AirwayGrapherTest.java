package org.mitre.tdp.boogie.alg.chooser.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

class AirwayGrapherTest {

  private static final TokenGrapher GRAPHER = TokenGrapher.standard();

  @Test
  void test_StandardAirway_SafeWithLessThanTwoLegs() {

    ResolvedToken.StandardAirway airway = ResolvedToken.standardAirway(emptyAirway());

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(airway);
    assertEquals(0, representation.size(), "Should be zero generated links.");
  }

  @Test
  void test_StandardAirway() {

    ResolvedToken.StandardAirway airway = ResolvedToken.standardAirway(mockAirway());

    List<LinkedLegs> representation = new ArrayList<>(GRAPHER.graphRepresentationOf(airway));
    assertEquals(6, representation.size(), "Expect 2*3 linked legs for bidirectional flying.");

    // check the forward progression of legs
    for (int i = 2; i < representation.size(); i += 2) {

      LinkedLegs previous = representation.get(i - 2);
      LinkedLegs next = representation.get(i);

      assertSame(previous.target(), next.source());
    }

    // check consecutive legs are inverted (so the airway is bidirectional)
    for (int i = 1; i < representation.size(); i += 2) {

      LinkedLegs forward = representation.get(i - 1);
      LinkedLegs backward = representation.get(i);

      assertSame(forward.source(), backward.target());
      assertSame(forward.target(), backward.source());
    }
  }

  private Airway emptyAirway() {
    Leg leg = mock(Leg.class);

    Airway airway = mock(Airway.class);
    when(airway.legs()).thenReturn((List) Collections.singletonList(leg));

    return airway;
  }

  private Airway mockAirway() {

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
