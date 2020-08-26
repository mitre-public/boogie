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

  // this is basically an abs value function with a slope of 2 centered at 5
  private static final Function<Long, Double> linear = t -> FastMath.abs((-2.0 * t) + 10.0) / 10.0;
  private static final List<Integer> stages2 = Arrays.asList(0, 1, 2, 3, 4, 5);
  private static final List<IntersectionStates> states = Arrays.asList(IntersectionStates.values());

  @Test
  void testTagger() {
    ViterbiTagger<Integer, IntersectionStates> tagger = new ViterbiTagger<>(stages2, states);

    NavigableMap<Integer, IntersectionStates> path = tagger.optimalPath();

    assertEquals(Arrays.asList(
        IntersectionStates.C,
        IntersectionStates.C,
        IntersectionStates.B,
        IntersectionStates.B,
        IntersectionStates.B,
        IntersectionStates.C), new ArrayList<>(path.values()));

    assertEquals(-1.66, tagger.optimalScoredPath().get(5).score(), 0.01);
  }

  @Test
  void testForwardMinMax() {
    ViterbiTagger<Long, State> tagger = new ViterbiTagger<>(stages, Arrays.asList(State.values()));

    assertFalse(tagger.optimizedStates().isEmpty());
    assertEquals(Arrays.asList(B, A, B, B, B, B, B, B, B, A), new ArrayList<>(tagger.optimalPath().values()));
  }

  enum State implements HmmState<Long> {
    A(x -> x.doubleValue() + 1.0),
    B(x -> x.doubleValue() + 2.0);

    private final Function<Long, Double> scorer;

    State(Function<Long, Double> scorer) {
      this.scorer = scorer;
    }

    @Override
    public double getValue(Long stage) {
      return scorer.apply(stage);
    }

    @Override
    public List<? extends HmmTransition<?, ?>> getPossibleTransitions(Long stage) {
      switch (this) {
        case A:
          return Arrays.asList(
              new Transition(A, 0.5),
              new Transition(B, linear.apply(stage)));
        case B:
          return Arrays.asList(
              new Transition(A, linear.apply(stage)),
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