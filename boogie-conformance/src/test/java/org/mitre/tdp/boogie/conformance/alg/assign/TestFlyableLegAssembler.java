package org.mitre.tdp.boogie.conformance.alg.assign;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;

class TestFlyableLegAssembler {

  @Test
  public void testRouteAssemblyNoLegs() {
    Route route = Route.newRoute(Collections.emptyList(), new Object());
    assertEquals(Collections.emptyList(), FlyableLegAssembler.assemble(route));
  }

  @Test
  public void testRouteAssembly1Leg() {
    Route route = Route.newRoute(lotsOLegs().subList(0, 1), new Object());
    assertEquals(1, FlyableLegAssembler.assemble(route).size());
  }

  @Test
  public void testRouteAssembly2Legs() {
    Route route = Route.newRoute(lotsOLegs().subList(0, 2), new Object());
    assertEquals(2, FlyableLegAssembler.assemble(route).size());
  }

  @Test
  public void testRouteAssembly3Legs() {
    Route route = Route.newRoute(lotsOLegs().subList(0, 3), new Object());
    assertEquals(3, FlyableLegAssembler.assemble(route).size());
  }

  @Test
  public void testLinksLessThan2Legs() {
    Route route = Route.newRoute(Collections.emptyList(), new Object());
    assertEquals(0, FlyableLegAssembler.assemble(route).size());
  }

  @Test
  public void testLinksMoreThan2Legs() {
    Route route = Route.newRoute(lotsOLegs().subList(0, 4), new Object());
    assertEquals(3, FlyableLegAssembler.assembleWithLinks(Collections.singletonList(route)).second().size());
  }

  @Test
  public void testOrderForPairs() {
    List<Leg> legs = lotsOLegs().subList(0, 2);
    Route route = Route.newRoute(legs, new Object());

    List<FlyableLeg> flyableLegs = new ArrayList<>(FlyableLegAssembler.assembleWithLinks(Collections.singletonList(route)).first());

    assertAll(
        () -> assertEquals(legs.get(0), flyableLegs.get(0).current()),
        () -> assertEquals(legs.get(1), flyableLegs.get(1).current())
    );
  }

  @Test
  public void testOrderForGreaterThan2() {
    List<Leg> legs = lotsOLegs().subList(0, 5);
    Route route = Route.newRoute(legs, new Object());

    List<FlyableLeg> flyableLegs = new ArrayList<>(FlyableLegAssembler.assembleWithLinks(Collections.singletonList(route)).first());

    assertAll(
        () -> assertEquals(legs.get(0), flyableLegs.get(0).current()),
        () -> assertEquals(legs.get(1), flyableLegs.get(1).current()),
        () -> assertEquals(legs.get(2), flyableLegs.get(2).current()),
        () -> assertEquals(legs.get(3), flyableLegs.get(3).current()),
        () -> assertEquals(legs.get(4), flyableLegs.get(4).current())
    );
  }

  private List<Leg> lotsOLegs() {
    Leg l1 = mock(Leg.class);
    Leg l2 = mock(Leg.class);
    Leg l3 = mock(Leg.class);
    Leg l4 = mock(Leg.class);
    Leg l5 = mock(Leg.class);
    Leg l6 = mock(Leg.class);
    return Arrays.asList(l1, l2, l3, l4, l5, l6);
  }
}
