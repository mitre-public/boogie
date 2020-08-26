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
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Sets;

public class TestScoreBasedRouteAssigner {

  @Test
  public void testScorerReturnsMaxValuePath() {
    Leg source = leg("source", PathTerm.TF);
    Leg a = leg("a", PathTerm.TF);
    Leg b = leg("b", PathTerm.TF);
    Leg c = leg("c", PathTerm.TF);
    Leg d = leg("d", PathTerm.TF);
    Leg e = leg("e", PathTerm.TF);
    Leg f = leg("f", PathTerm.TF);
    Leg g = leg("g", PathTerm.TF);
    Leg h = leg("h", PathTerm.TF);

    List<ConformablePoint> pointList = LongStream.range(0, 6)
        .mapToObj(tau -> {
          Instant time = Instant.EPOCH.plus(Duration.ofMinutes(tau));
          ConformablePoint pt = conformablePoint(time);
          return pt;
        })
        .collect(Collectors.toList());

    FlyableLeg sourceab = legs(source, a, b, scorer(pointList.get(0)));
    FlyableLeg sourceac = legs(source, a, c, scorer(pointList.get(0)));
    FlyableLeg abd = legs(a, b, d, scorer(pointList.get(1), pointList.get(2), pointList.get(3)));
    FlyableLeg abe = legs(a, b, e, scorer(pointList.get(1), pointList.get(2)));
    FlyableLeg acf = legs(a, c, f, scorer(pointList.get(5)));
    FlyableLeg acg = legs(a, c, g, scorer(pointList.get(5)));
    FlyableLeg bdn = legs(b, d, null, scorer());
    FlyableLeg beh = legs(b, e, h, scorer(pointList.get(3), pointList.get(4), pointList.get(5)));
    FlyableLeg cfn = legs(c, f, null, scorer());
    FlyableLeg cgn = legs(c, g, null, scorer(pointList.get(3)));
    FlyableLeg ehn = legs(e, h, null, scorer());

    ScoreBasedRouteResolver resolver = ScoreBasedRouteResolver.withConformableLegs(Arrays.asList(sourceab, sourceac, abd, abe, acf, acg, bdn, beh, cfn, cgn, ehn));
    Map<ConformablePoint, FlyableLeg> mapping = resolver.resolveRoute(pointList);

    Map<ConformablePoint, FlyableLeg> expected = new LinkedHashMap<>();
    expected.put(pointList.get(0), sourceab);
    expected.put(pointList.get(1), abe);
    expected.put(pointList.get(2), abe);
    expected.put(pointList.get(3), beh);
    expected.put(pointList.get(4), beh);
    expected.put(pointList.get(5), beh);

    assertEquals(expected, mapping, String.format("Mismatched transitional legs: \n %s \n %s \n", format(expected), format(mapping)));
  }

  private String format(Map<ConformablePoint, FlyableLeg> legs) {
    return legs.entrySet().stream()
        .sorted(Comparator.comparing(entry -> entry.getKey().time()))
        .map(entry -> {
          Instant time = entry.getKey().time();
          FlyableLeg leg = entry.getValue();

          String ids = String.format("%s -> %s",
              leg.previous().get().pathTerminator().identifier(),
              leg.current().pathTerminator().identifier());

          return String.format("%s=%s", time.toString(), ids);
        })
        .collect(Collectors.joining(","));
  }

  private ConformablePoint conformablePoint(Instant tau) {
    ConformablePoint pt = mock(ConformablePoint.class);
    when(pt.time()).thenReturn(tau);
    when(pt.compareTo(any())).thenCallRealMethod();
    when(pt.toString()).thenReturn(tau.toString());
    return pt;
  }

  private OnLegScorer scorer(ConformablePoint... points) {
    Set<ConformablePoint> pointSet = Sets.newHashSet(points);
    Answer<Optional<Double>> answer = incoming -> {
      Object[] args = incoming.getArguments();
      return Optional.of(pointSet.contains(args[0]) ? 0.99 : 0.01);
    };

    OnLegScorer scorer = mock(OnLegScorer.class);
    when(scorer.score(any(), any())).thenAnswer(answer);
    return scorer;
  }

  private FlyableLeg legs(Leg l1, Leg l2, Leg l3, OnLegScorer scorer) {
    return new FlyableLeg(l1, l2, l3).setOnLegScorer(scorer);
  }

  private Leg leg(String identifier, PathTerm type) {
    Fix fix = fix(identifier);
    Leg leg = mock(Leg.class, identifier);
    when(leg.pathTerminator()).thenReturn(fix);
    when(leg.type()).thenReturn(type);
    return leg;
  }

  private Fix fix(String identifier) {
    Fix fix = mock(Fix.class);
    when(fix.identifier()).thenReturn(identifier);
    return fix;
  }
}
