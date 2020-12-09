package org.mitre.tdp.boogie.conformance.alg.assign;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assemble.GraphicalLegReducer;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegHashers;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPair;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPairImpl;
import org.mitre.tdp.boogie.conformance.alg.assign.score.LegScoringStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.score.LegTransitionScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

import com.google.common.collect.Sets;

public class TestFlyableLegGraph {

  @Test
  public void testGrabsDownstreamLinks() {
    Leg source = leg("source", PathTerm.TF);
    Leg a = leg("a", PathTerm.TF);
    Leg b = leg("b", PathTerm.TF);
    Leg c = leg("c", PathTerm.TF);
    Leg d = leg("d", PathTerm.TF);

    LegPair sourcePair = new LegPairImpl(source, a);
    LegPair legPair1 = new LegPairImpl(a, b);
    LegPair legPair2 = new LegPairImpl(a, c);
    LegPair legPair3 = new LegPairImpl(a, d);

    List<FlyableLeg> flyableLegs = reducer(Arrays.asList(legPair1, legPair2, legPair3, sourcePair)).flyableLegStream()
        .peek(dummyStrategy()::apply).collect(Collectors.toList());

    FlyableLegGraph legsGraph = FlyableLegGraph.withFlyableLegs(flyableLegs);

    FlyableLeg query = flyableLegs.stream().filter(leg -> leg.current().equals(source)).findFirst().orElseThrow(RuntimeException::new);

    List<FlyableLeg> downstream = legsGraph.downstreamLegsOf(query);
    assertEquals(3, downstream.size());

    Set<String> downstreamIds = downstream.stream()
        .map(FlyableLeg::next)
        .map(Optional::get)
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

    LegPair sourcePair = new LegPairImpl(source, a);
    LegPair legPair1 = new LegPairImpl(b, a);
    LegPair legPair2 = new LegPairImpl(c, a);
    LegPair legPair3 = new LegPairImpl(d, a);

    List<FlyableLeg> flyableLegs = reducer(Arrays.asList(legPair1, legPair2, legPair3, sourcePair)).flyableLegStream()
        .peek(dummyStrategy()::apply).collect(Collectors.toList());

    FlyableLegGraph legsGraph = FlyableLegGraph.withFlyableLegs(flyableLegs);

    FlyableLeg query = flyableLegs.stream().filter(leg -> leg.current().equals(a)).findFirst().orElseThrow(RuntimeException::new);

    List<FlyableLeg> downstream = legsGraph.downstreamLegsOf(query);
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

    LegPair sourcePair = new LegPairImpl(source, a);
    LegPair legPair1 = new LegPairImpl(a, b);
    LegPair legPair2 = new LegPairImpl(a, c);
    LegPair legPair3 = new LegPairImpl(a, d);
    LegPair legPair4 = new LegPairImpl(b, e);

    List<FlyableLeg> flyableLegs = reducer(Arrays.asList(legPair1, legPair2, legPair3, legPair4, sourcePair)).flyableLegStream()
        .peek(dummyStrategy()::apply).collect(Collectors.toList());

    FlyableLegGraph legsGraph = FlyableLegGraph.withFlyableLegs(flyableLegs);

    FlyableLeg query = flyableLegs.stream().filter(leg -> leg.current().equals(source)).findFirst().orElseThrow(RuntimeException::new);

    List<FlyableLeg> downstream = legsGraph.downstreamLegsOf(query);
    assertEquals(3, downstream.size());
  }

  private LegScoringStrategy dummyStrategy(){
    OnLegScorer onLegScorer = mock(OnLegScorer.class);
    LegTransitionScorer legTransitionScorer = mock(LegTransitionScorer.class);
    return new LegScoringStrategy() {
      @Override
      public OnLegScorer onLegScorerFor(FlyableLeg flyableLeg) {
        return onLegScorer;
      }

      @Override
      public LegTransitionScorer legTransitionScorer(FlyableLeg flyableLeg) {
        return legTransitionScorer;
      }
    };
  }

  private GraphicalLegReducer reducer(List<LegPair> legPairs) {
    GraphicalLegReducer reducer = new GraphicalLegReducer(
        LegHashers.byIdentifier()
            .andThenBy(LegHashers.byLocation())
            .andThenBy(LegHashers.byType())
            .orElseBy(LegHashers.byHashCode())
    );

    legPairs.forEach(reducer::addLegPair);
    return reducer;
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
