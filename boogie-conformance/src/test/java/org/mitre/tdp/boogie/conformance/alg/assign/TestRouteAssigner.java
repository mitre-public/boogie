package org.mitre.tdp.boogie.conformance.alg.assign;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScoringRule.newScoringRule;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.NoopCombinationStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.link.LinkingStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.link.SuppliedLinkStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.RuleBasedScoringStrategy;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

class TestRouteAssigner {

  @Test
  public void testScorerReturnsMaxValuePath() {
    Leg source = leg("source");
    Leg a = leg("a");
    Leg b = leg("b");
    Leg c = leg("c");
    Leg d = leg("d");
    Leg e = leg("e");
    Leg f = leg("f");
    Leg g = leg("g");
    Leg h = leg("h");
    Leg i = leg("i");

    List<ConformablePoint> points = LongStream.range(0, 10)
        .mapToObj(tau -> {
          Instant time = Instant.EPOCH.plus(Duration.ofMinutes(tau));
          return conformablePoint(time);
        })
        .collect(Collectors.toList());

    Route route0 = route(Arrays.asList(source, a));
    Route route1 = route(Arrays.asList(b, c, d, e));
    Route route2 = route(Arrays.asList(f, g, h, i));

    List<Route> routes = Arrays.asList(route0, route1, route2);

    LinkingStrategy linkingStrategy = new SuppliedLinkStrategy(
        Pair.of(new FlyableLeg(source, a, null, route0), new FlyableLeg(null, b, c, route1)),
        Pair.of(new FlyableLeg(b, c, d, route1), new FlyableLeg(f, g, h, route2))
    );

    RouteAssigner alg = new RouteAssigner(
        linkingStrategy,
        new NoopCombinationStrategy(),
        new RuleBasedScoringStrategy.Builder()
            .addOnLegScoringRules(
                newScoringRule(l -> l.current().equals(a), scorer(points.get(0), points.get(1))),
                newScoringRule(l -> l.current().equals(b), scorer(points.get(2), points.get(3))),
                newScoringRule(l -> l.current().equals(g), scorer(points.get(4), points.get(5), points.get(6))),
                newScoringRule(l -> l.current().equals(h), scorer(points.get(7), points.get(8), points.get(9))),
                newScoringRule(l -> true, scorer())
            )
            .setLegTransitionScorers((l1, l2) -> Optional.of(.1))
            .build()
    );

    Map<ConformablePoint, Leg> mapping = Maps.transformValues(
        alg.assignments(points, routes),
        FlyableLeg::current
    );

    Map<ConformablePoint, Leg> expected = new LinkedHashMap<>();
    expected.put(points.get(0), a);
    expected.put(points.get(1), a);
    expected.put(points.get(2), b);
    expected.put(points.get(3), b);
    expected.put(points.get(4), c);
    expected.put(points.get(5), g);
    expected.put(points.get(6), g);
    expected.put(points.get(7), h);
    expected.put(points.get(8), h);
    expected.put(points.get(9), h);

    assertEquals(expected, mapping, String.format("Mismatched transitional legs \n expected %s \n actual %s \n", format(expected), format(mapping)));
  }

  private String format(Map<ConformablePoint, Leg> legs) {
    return legs.entrySet().stream()
        .sorted(Comparator.comparing(entry -> entry.getKey().time()))
        .map(entry -> {
          Instant time = entry.getKey().time();
          Leg leg = entry.getValue();

          String ids = String.format("%s", leg.pathTerminator().identifier());
          return String.format("%s=%s", time.toString(), ids);
        })
        .collect(Collectors.joining(","));
  }

  private OnLegScorer scorer(ConformablePoint... points) {
    Set<ConformablePoint> pointSet = Sets.newHashSet(points);
    Answer<Optional<Double>> answer = incoming -> {
      Object[] args = incoming.getArguments();
      return Optional.of(pointSet.contains(args[0]) ? 0.99 / points.length : 0.01);
    };

    OnLegScorer scorer = mock(OnLegScorer.class);
    when(scorer.score(any(), any())).thenAnswer(answer);
    return scorer;
  }

  private ConformablePoint conformablePoint(Instant tau) {
    ConformablePoint pt = mock(ConformablePoint.class);
    when(pt.time()).thenReturn(tau);
    when(pt.compareTo(any())).thenCallRealMethod();
    when(pt.toString()).thenReturn(tau.toString());
    return pt;
  }

  private Route route(List<Leg> legs) {
    Route route = mock(Route.class);
    when(route.legs()).thenReturn((List) legs);
    return route;
  }

  private Leg leg(String identifier) {
    PathTerm term = mock(PathTerm.class);
    when(term.hasRequiredFields(any())).thenReturn(true);
    Fix fix = fix(identifier);
    Leg leg = mock(Leg.class, identifier);
    when(leg.pathTerminator()).thenReturn(fix);
    when(leg.type()).thenReturn(term);
    return leg;
  }

  private Fix fix(String identifier) {
    Fix fix = mock(Fix.class);
    when(fix.identifier()).thenReturn(identifier);
    return fix;
  }
}
