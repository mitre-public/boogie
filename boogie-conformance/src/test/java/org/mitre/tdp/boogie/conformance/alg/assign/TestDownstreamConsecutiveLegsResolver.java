package org.mitre.tdp.boogie.conformance.alg.assign;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPair;

import com.google.common.collect.Sets;

public class TestDownstreamConsecutiveLegsResolver {

  @Test
  public void testGrabsDownstreamLinks() {
    Leg source = leg("source", PathTerm.TF);
    Leg a = leg("a", PathTerm.TF);
    Leg b = leg("b", PathTerm.TF);
    Leg c = leg("c", PathTerm.TF);
    Leg d = leg("d", PathTerm.TF);

    LegPair legPair1 = new LegPair(a, b, false);
    LegPair legPair2 = new LegPair(a, c, false);
    LegPair legPair3 = new LegPair(a, d, false);

    LegPair query = new LegPair(source, a, false);

    DownstreamConsecutiveLegsResolver legsGraph = DownstreamConsecutiveLegsResolver.from(Arrays.asList(legPair1, legPair2, legPair3, query));

    List<ConsecutiveLegs> downstream = legsGraph.downstreamLegsOf(query);
    assertEquals(3, downstream.size());

    Set<String> downstreamIds = downstream.stream()
        .map(ConsecutiveLegs::current)
        .map(Leg::pathTerminator)
        .map(Fix::identifier)
        .collect(Collectors.toSet());
    assertEquals(Sets.newHashSet("b", "c", "d"), downstreamIds);
  }

  @Test
  public void testIgnoresUpstreamLinks() {
    Leg source = leg("source", PathTerm.TF);
    Leg a = leg("a", PathTerm.TF);
    Leg b = leg("b", PathTerm.TF);
    Leg c = leg("c", PathTerm.TF);
    Leg d = leg("d", PathTerm.TF);

    LegPair legPair1 = new LegPair(b, a, false);
    LegPair legPair2 = new LegPair(c, a, false);
    LegPair legPair3 = new LegPair(d, a, false);

    LegPair query = new LegPair(source, a, false);

    DownstreamConsecutiveLegsResolver legsGraph = DownstreamConsecutiveLegsResolver.from(Arrays.asList(legPair1, legPair2, legPair3, query));

    List<ConsecutiveLegs> downstream = legsGraph.downstreamLegsOf(query);
    assertEquals(0, downstream.size());
  }

  @Test
  public void testIgnoresMultiHopDownstreamLinks() {
    Leg source = leg("source", PathTerm.TF);
    Leg a = leg("a", PathTerm.TF);
    Leg b = leg("b", PathTerm.TF);
    Leg c = leg("c", PathTerm.TF);
    Leg d = leg("d", PathTerm.TF);
    Leg e = leg("e", PathTerm.TF);

    LegPair legPair1 = new LegPair(a, b, false);
    LegPair legPair2 = new LegPair(a, c, false);
    LegPair legPair3 = new LegPair(a, d, false);
    LegPair legPair4 = new LegPair(b, e, false);

    LegPair query = new LegPair(source, a, false);

    DownstreamConsecutiveLegsResolver legsGraph = DownstreamConsecutiveLegsResolver.from(Arrays.asList(legPair1, legPair2, legPair3, legPair4, query));

    List<ConsecutiveLegs> downstream = legsGraph.downstreamLegsOf(query);
    assertEquals(3, downstream.size());
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
