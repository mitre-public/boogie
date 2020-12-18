package org.mitre.tdp.boogie.conformance.alg.assign;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;

import com.google.common.collect.Sets;

class TestFlyableLegGraph {

  @Test
  public void testGrabsDownstreamLinks() {
    Leg source = leg("source", PathTerm.TF);
    Leg a = leg("a", PathTerm.TF);
    Leg b = leg("b", PathTerm.TF);
    Leg c = leg("c", PathTerm.TF);
    Leg d = leg("d", PathTerm.TF);
    Leg e = leg("e", PathTerm.TF);

    FlyableLeg p1 = new FlyableLeg(source, a, b);
    FlyableLeg p2 = new FlyableLeg(source, b, c);
    FlyableLeg p3 = new FlyableLeg(source, c, d);
    FlyableLeg p4 = new FlyableLeg(null, source, e);

    FlyableLegGraph legsGraph = new FlyableLegGraph();

    Stream.of(p1, p2, p3, p4).forEach(legsGraph::addVertex);
    legsGraph.addEdge(p1, p2);
    legsGraph.addEdge(p4, p1);
    legsGraph.addEdge(p4, p2);
    legsGraph.addEdge(p4, p3);

    List<FlyableLeg> downstream = legsGraph.downstreamLegsOf(p4);

    Set<String> downstreamIds = downstream.stream()
        .map(FlyableLeg::current)
        .map(Leg::pathTerminator)
        .map(Fix::identifier)
        .collect(Collectors.toSet());

    assertEquals(Sets.newHashSet("a", "b", "c"), downstreamIds);
  }

  @Test
  public void testIgnoresUpstreamLinks() {
    Leg source = leg("source", PathTerm.TF);
    Leg a = leg("a", PathTerm.TF);
    Leg b = leg("b", PathTerm.TF);
    Leg c = leg("c", PathTerm.TF);
    Leg d = leg("d", PathTerm.TF);
    Leg e = leg("e", PathTerm.TF);

    FlyableLeg p1 = new FlyableLeg(source, a, b);
    FlyableLeg p2 = new FlyableLeg(source, b, c);
    FlyableLeg p3 = new FlyableLeg(source, c, d);
    FlyableLeg p4 = new FlyableLeg(null, source, e);

    FlyableLegGraph legsGraph = new FlyableLegGraph();

    Stream.of(p1, p2, p3, p4).forEach(legsGraph::addVertex);
    legsGraph.addEdge(p1, p2);
    legsGraph.addEdge(p4, p1);
    legsGraph.addEdge(p4, p2);
    legsGraph.addEdge(p4, p3);

    List<FlyableLeg> downstream;

    downstream = legsGraph.downstreamLegsOf(p2);
    assertEquals(0, downstream.size());

    downstream = legsGraph.downstreamLegsOf(p2);
    assertEquals(0, downstream.size());
  }

  private Fix fix(String identifier) {
    Fix fix = mock(Fix.class);
    when(fix.identifier()).thenReturn(identifier);
    return fix;
  }

  private Leg leg(String identifier, PathTerm type) {
    Fix fix = fix(identifier);
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(fix);
    when(leg.type()).thenReturn(type);
    return leg;
  }
}
