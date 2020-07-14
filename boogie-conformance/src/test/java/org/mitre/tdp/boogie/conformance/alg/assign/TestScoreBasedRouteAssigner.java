package org.mitre.tdp.boogie.conformance.alg.assign;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
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
import org.mitre.tdp.boogie.conformance.Scorer;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;
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

    ConsecutiveLegs sourcea = legs(source, a, scorer(pointList.get(0)));
    ConsecutiveLegs ab = legs(a, b, scorer(pointList.get(1), pointList.get(2)));
    ConsecutiveLegs ac = legs(a, c, scorer(pointList.get(5)));
    ConsecutiveLegs bd = legs(b, d, scorer());
    ConsecutiveLegs be = legs(b, e, scorer(pointList.get(3), pointList.get(4)));
    ConsecutiveLegs cf = legs(c, f, scorer());
    ConsecutiveLegs cg = legs(c, g, scorer());
    ConsecutiveLegs eh = legs(e, h, scorer());

    ScoreBasedRouteResolver resolver = ScoreBasedRouteResolver.with(Arrays.asList(sourcea, ab, ac, bd, be, cf, cg));
    Map<ConformablePoint, ConsecutiveLegs> mapping = resolver.resolveRoute(pointList);

    Map<ConformablePoint, ConsecutiveLegs> expected = new HashMap<>();
    expected.put(pointList.get(0), sourcea);
    expected.put(pointList.get(1), ab);
    expected.put(pointList.get(2), ab);
    expected.put(pointList.get(3), be);
    expected.put(pointList.get(4), be);
    expected.put(pointList.get(5), ac);

    assertEquals(expected, mapping, "Since we hit the end of the connected state at {b->e} we allow transitions to disconnected high-score states {a->c}.");

    resolver = ScoreBasedRouteResolver.with(Arrays.asList(sourcea, ab, ac, bd, be, cf, cg, eh));
    mapping = resolver.resolveRoute(pointList);

    expected.put(pointList.get(5), be);

    assertEquals(expected, mapping, "With additional downstream leg of {b->e} ({e->h}) we need to finish the connected components before we can transition to a disconnected state.");
  }

  private ConformablePoint conformablePoint(Instant tau) {
    ConformablePoint pt = mock(ConformablePoint.class);
    when(pt.time()).thenReturn(tau);
    when(pt.compareTo(any())).thenCallRealMethod();
    when(pt.toString()).thenReturn(tau.toString());
    return pt;
  }

  private Scorer<ConformablePoint, ConsecutiveLegs> scorer(ConformablePoint... points) {
    Set<ConformablePoint> pointSet = Sets.newHashSet(points);
    Answer<Optional<Double>> answer = incoming -> {
      Object[] args = incoming.getArguments();
      return Optional.of(pointSet.contains(args[0]) ? 1.0 : 0.0);
    };

    Scorer<ConformablePoint, ConsecutiveLegs> scorer = mock(Scorer.class);
    when(scorer.score(any())).thenAnswer(answer);
    when(scorer.transitionScore(any())).thenReturn(1.0);
    return scorer;
  }

  private ConsecutiveLegs legs(Leg l1, Leg l2, Scorer<ConformablePoint, ConsecutiveLegs> scorer) {
    String toString = String.format("%s -> %s", l1.pathTerminator().identifier(), l2.pathTerminator().identifier());
    ConsecutiveLegs consecutiveLegs = mock(ConsecutiveLegs.class);
    when(consecutiveLegs.previous()).thenReturn(Optional.of(l1));
    when(consecutiveLegs.current()).thenReturn(l2);
    when(consecutiveLegs.scorer()).thenReturn(scorer);
    when(consecutiveLegs.toString()).thenReturn(toString);
    return consecutiveLegs;
  }

  private Leg leg(String identifier, PathTerm type) {
    Fix fix = fix(identifier);
    Leg leg = mock(Leg.class);
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
