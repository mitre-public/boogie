package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mitre.tdp.boogie.conformance.alg.assign.dp.TestViterbiTagger.State.A;
import static org.mitre.tdp.boogie.conformance.alg.assign.dp.TestViterbiTagger.State.B;

class TestViterbiTagger {

  private static final NavigableSet<Long> stages = LongStream.range(0, 10).boxed()
      .collect(Collectors.toCollection(TreeSet::new));

  // this is basically an abs value function with a slope of 0.2 centered at 5
  private static final List<Integer> stages2 = Arrays.asList(0, 1, 2, 3, 4, 5);
  private static final List<IntersectionStates> states = Arrays.asList(IntersectionStates.values());

  @Test
  void testTagger() {
    ViterbiTagger<Integer, IntersectionStates> tagger = ViterbiTagger.forHmmStates(stages2, states);

    Map<Integer, IntersectionStates> path = tagger.optimalPath();

    assertEquals(Arrays.asList(
        IntersectionStates.B,
        IntersectionStates.B,
        IntersectionStates.B,
        IntersectionStates.B,
        IntersectionStates.C,
        IntersectionStates.C), new ArrayList<>(path.values()));

    assertEquals(-2.87, tagger.trellis().optimalPathScoreAt(5).logLikelihood(), 0.01);
  }

  @Test
  void testForwardMinMax() {
    ViterbiTagger<Long, State> tagger = ViterbiTagger.forHmmStates(stages, Arrays.asList(State.values()));

    assertFalse(tagger.trellis().optimalPath().isEmpty());
    assertEquals(Arrays.asList(A, B, A, B, B, B, B, B, B, B), new ArrayList<>(tagger.optimalPath().values()));
  }

  enum State implements HmmState<Long> {
    A(x -> Math.pow(0.8, x.doubleValue())),
    B(x -> Math.pow(0.9, x.doubleValue()));

    private final Function<Long, Double> scorer;

    State(Function<Long, Double> scorer) {
      this.scorer = scorer;
    }

    @Override
    public double getValue(Long stage) {
      return scorer.apply(stage);
    }

    @Override
    public List<? extends HmmTransition<?, ?>> getPossibleTransitions() {
      switch (this) {
        case A:
          return Arrays.asList(
              new Transition(A, 0.5),
              new Transition(B, 0.6));
        case B:
          return Arrays.asList(
              new Transition(A, 0.6),
              new Transition(B, 0.5));
        default:
          throw new RuntimeException();
      }
    }
  }

  static class Transition implements HmmTransition<Long, State> {

    private final State state;
    private final double score;

    Transition(State s, double c) {
      this.state = s;
      this.score = c;
    }

    @Override
    public State getTransition() {
      return state;
    }

    @Override
    public Double getTransitionProbability() {
      return score;
    }
  }
}