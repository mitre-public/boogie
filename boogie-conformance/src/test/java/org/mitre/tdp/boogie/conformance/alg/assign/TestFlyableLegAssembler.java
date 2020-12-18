package org.mitre.tdp.boogie.conformance.alg.assign;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Route;

class TestFlyableLegAssembler {

  @Test
  public void testRouteAssemblyNoLegs() {
    Route route = Collections::emptyList;
    assertEquals(Collections.emptyList(), FlyableLegAssembler.assemble(route));
  }

  @Test
  public void testRouteAssembly1Leg() {
    Route route = () -> lotsOLegs().subList(0, 1);
    assertEquals(1, FlyableLegAssembler.assemble(route).size());
  }

  @Test
  public void testRouteAssembly2Legs() {
    Route route = () -> lotsOLegs().subList(0, 2);
    assertEquals(2, FlyableLegAssembler.assemble(route).size());
  }

  @Test
  public void testRouteAssembly3Legs() {
    Route route = () -> lotsOLegs().subList(0, 3);
    assertEquals(3, FlyableLegAssembler.assemble(route).size());
  }

  @Test
  public void testLinksLessThan2Legs() {
    Route route = Collections::emptyList;
    assertEquals(0, FlyableLegAssembler.assemble(route).size());
  }

  @Test
  public void testLinksMoreThan2Legs() {
    Route route = () -> lotsOLegs().subList(0, 4);
    assertEquals(3, FlyableLegAssembler.assembleWithLinks(Collections.singletonList(route)).second().size());
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
