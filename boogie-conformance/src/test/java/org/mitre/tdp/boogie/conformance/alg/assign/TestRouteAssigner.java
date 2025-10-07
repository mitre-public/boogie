package org.mitre.tdp.boogie.conformance.alg.assign;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.NoopCombinationStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.link.LinkingStrategy;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;
import org.mitre.tdp.boogie.viterbi.RuleBasedViterbiScoringStrategy;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

class TestRouteAssigner {

  private static final PathTerminatorBasedLegValidator validator = new PathTerminatorBasedLegValidator();

  @Test
  void testScorerReturnsMaxValuePath() {
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

    LinkingStrategy linkingStrategy = LinkingStrategy.supplied(
        Pair.of(new FlyableLeg(source, a, null, route0), new FlyableLeg(null, b, c, route1)),
        Pair.of(new FlyableLeg(b, c, d, route1), new FlyableLeg(f, g, h, route2))
    );

    RouteAssigner alg = new RouteAssigner(
        linkingStrategy,
        new NoopCombinationStrategy(),
        RuleBasedViterbiScoringStrategy.<ConformablePoint, FlyableLeg>newBuilder()
            .addStateDelegatedScorer(l -> l.current().equals(a) && validator.test(l.current()), scorer(points.get(0), points.get(1)))
            .addStateDelegatedScorer(l -> l.current().equals(b) && validator.test(l.current()), scorer(points.get(2), points.get(3)))
            .addStateDelegatedScorer(l -> l.current().equals(g) && validator.test(l.current()), scorer(points.get(4), points.get(5), points.get(6)))
            .addStateDelegatedScorer(l -> l.current().equals(h) && validator.test(l.current()), scorer(points.get(7), points.get(8), points.get(9)))
            .addStateDelegatedScorer(l -> true, scorer())
            .build(),
        (l1, l2) -> .1
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

          String ids = String.format("%s", leg.associatedFix().map(Fix::fixIdentifier).orElse("NONE"));
          return String.format("%s=%s", time.toString(), ids);
        })
        .collect(Collectors.joining(","));
  }

  private BiFunction<ConformablePoint, FlyableLeg, Double> scorer(ConformablePoint... points) {
    Set<ConformablePoint> pointSet = Sets.newHashSet(points);
    return (conformablePoint, flyableLeg) -> pointSet.contains(conformablePoint) ? .99 / points.length : .01;
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
    when(route.legs()).thenReturn(legs);
    return route;
  }

  private Leg leg(String identifier) {
    Fix fix = fix(identifier);
    Leg leg = mock(Leg.class, identifier);
    when(leg.associatedFix()).thenReturn((Optional) Optional.of(fix));
    when(leg.pathTerminator()).thenReturn(PathTerminator.TF);
    return leg;
  }

  private Fix fix(String identifier) {
    Fix fix = mock(Fix.class);
    when(fix.fixIdentifier()).thenReturn(identifier);
    return fix;
  }
}
