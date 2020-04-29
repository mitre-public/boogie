package org.mitre.tdp.boogie.conformance.alg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.apache.commons.math3.util.FastMath;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.conformance.alg.dp.DynamicProgrammer;
import org.mitre.tdp.boogie.conformance.alg.dp.DynamicProgrammerState;
import org.mitre.tdp.boogie.conformance.alg.dp.DynamicProgrammerTransition;

public class TestDynamicProgrammer {

  private static NavigableSet<Long> stages = LongStream.range(0, 10).boxed()
      .collect(Collectors.toCollection(TreeSet::new));

  // this is basically an abs value function with a slope of 2 centered at 5
  private static Function<Long, Double> linear = t -> FastMath.abs((-2.0 * t) + 10.0) / 10.0;
  private static List<Integer> stages2 = Arrays.asList(0, 1, 2, 3, 4, 5);
  private static List<IntersectionStates> states = Arrays.asList(
      IntersectionStates.A,
      IntersectionStates.B,
      IntersectionStates.C,
      IntersectionStates.D,
      IntersectionStates.E,
      IntersectionStates.F);

  @Test
  public void testForwardMinMax() {
    NavigableMap<Long, DynamicProgrammer.ScoredState<State>> path;
    DynamicProgrammer<Long, State> programmer;

    programmer = new DynamicProgrammer<>(stages, Arrays.asList(State.values()), DynamicProgrammer.Optimization.MINIMIZE);
    path = programmer.optimalPath();

    assertFalse(programmer.optimizedStates().isEmpty());

    assertEquals(path.get(0L).state(), State.A);
    assertEquals(path.get(1L).state(), State.A);
    assertEquals(path.get(2L).state(), State.A);
    assertEquals(path.get(3L).state(), State.A);
    assertEquals(path.get(4L).state(), State.B);
    assertEquals(path.get(5L).state(), State.A);
    assertEquals(path.get(6L).state(), State.A);
    assertEquals(path.get(7L).state(), State.B);
    assertEquals(path.get(8L).state(), State.A);
    assertEquals(path.get(9L).state(), State.A);

    programmer = new DynamicProgrammer<>(stages, Arrays.asList(State.values()), DynamicProgrammer.Optimization.MAXIMIZE);
    path = programmer.optimalPath();

    assertFalse(programmer.optimizedStates().isEmpty());

    assertEquals(path.get(0L).state(), State.B);
    assertEquals(path.get(1L).state(), State.A);
    assertEquals(path.get(2L).state(), State.B);
    assertEquals(path.get(3L).state(), State.B);
    assertEquals(path.get(4L).state(), State.B);
    assertEquals(path.get(5L).state(), State.B);
    assertEquals(path.get(6L).state(), State.B);
    assertEquals(path.get(7L).state(), State.B);
    assertEquals(path.get(8L).state(), State.B);
    assertEquals(path.get(9L).state(), State.A);
  }

  @Test
  public void testMax() {
    DynamicProgrammer<Integer, IntersectionStates> dp = new DynamicProgrammer<>(
        stages2, states, DynamicProgrammer.Optimization.MAXIMIZE);

    NavigableMap<Integer, DynamicProgrammer.ScoredState<IntersectionStates>> path = dp.optimalPath();

    assertEquals(path.get(0).state(), IntersectionStates.C);
    assertEquals(path.get(1).state(), IntersectionStates.C);
    assertEquals(path.get(2).state(), IntersectionStates.B);
    assertEquals(path.get(3).state(), IntersectionStates.B);
    assertEquals(path.get(4).state(), IntersectionStates.A);
    assertEquals(path.get(5).state(), IntersectionStates.A);

    assertEquals(path.get(5).score(), 39.0d, 0.1);
  }

  @Test
  public void testMin() {
    DynamicProgrammer<Integer, IntersectionStates> dp = new DynamicProgrammer<>(
        stages2, states, DynamicProgrammer.Optimization.MINIMIZE);

    NavigableMap<Integer, DynamicProgrammer.ScoredState<IntersectionStates>> path = dp.optimalPath();

    assertEquals(path.get(0).state(), IntersectionStates.E);
    assertEquals(path.get(1).state(), IntersectionStates.F);
    assertEquals(path.get(2).state(), IntersectionStates.E);
    assertEquals(path.get(3).state(), IntersectionStates.F);
    assertEquals(path.get(4).state(), IntersectionStates.E);
    assertEquals(path.get(5).state(), IntersectionStates.F);

    assertEquals(path.get(5).score(), 18.0d, 0.1);
  }

  enum State implements DynamicProgrammerState<Long> {
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
    public List<? extends DynamicProgrammerTransition> getPossibleTransitions(Long stage) {
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

  static class Transition implements DynamicProgrammerTransition<Long, State> {

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