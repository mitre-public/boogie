package org.mitre.tdp.boogie.conformance.alg.assign;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;

class TestFlyableLeg {

  @Test
  public void testFlyableLegHashByContents() {
    Route dummyRoute = Route.newRoute(new ArrayList<>(), new Object());

    Leg l1 = mock(Leg.class);
    Leg l2 = mock(Leg.class);
    Leg l3 = mock(Leg.class);

    FlyableLeg f1 = new FlyableLeg(l1, l2, l3, dummyRoute);
    FlyableLeg f2 = new FlyableLeg(l1, l2, l3, dummyRoute);
    FlyableLeg f3 = new FlyableLeg(l1, l2, null, dummyRoute);

    assertAll(
        () -> assertEquals(f1, f2),
        () -> assertNotEquals(f1, f3)
    );
  }
}
