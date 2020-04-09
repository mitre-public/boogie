package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.mitre.caasd.commons.HasTime;
import org.mitre.tdp.boogie.conformance.Scorable;
import org.mitre.tdp.boogie.conformance.Scored;
import org.mitre.tdp.boogie.conformance.Scores;

import com.google.common.collect.Lists;

/**
 * The time-based score maximizer takes is built around a collection of {@link Scorable} objects
 * but leverages time as the index by which to move through the dynamic programming problem.
 * <p>
 * Since it is a maximizer it is looking to find the set of assignments of particular inputs
 * to the provided scorable objects such that over time the overall score is maximized.
 * <p>
 * While the {@link DynamicProgrammer} can perform both maximization and minimization problems in
 * either direction often one can be rephrased to look similar to one or the other, as such for
 * simplicity in this class we assume a maximization problem.
 * <p>
 * Given this is a maximization it's worth noting that this means the best assignments at any
 * given time should be rewarded with a higher score, while transitions with the most likelyhood
 * should be given a larger factor as well.
 * <p>
 * Note - When coming up with state and transition scores it's generally best practice to stick
 * to either using percentages everywhere or raw values everywhere.
 * <p>
 * E.g. for scoring logic based primarily on distances (e.g. point-leg conformance) either use
 * the raw distances everywhere or use the probability of the object being appropriately assigned
 * to the given leg given its distance away.
 */
public class TimeBasedScoreMaximizer<H extends HasTime, L extends Scorable<H, L>> {

  /**
   * The set of legs to evaluate conformance against.
   */
  private final List<L> legs;

  private TimeBasedScoreMaximizer(List<L> legs) {
    this.legs = legs;
  }

  public List<L> legs() {
    return legs;
  }

  /**
   * Returns the list of legs from the {@link TimeBasedScoreMaximizer#legs()} which together minimize the
   * total cross track error between the points and the available legs.
   * <p>
   * The returned {@link Scored}s should have both scores as well as start and end
   * times for conformance that have been populated by the output of the dynamic programmers
   * assignments.
   */
  public List<Scored<H, L>> score(List<H> pts) {
    ScoreSet distances = ScoreSet.between(legs(), pts);
    Conformer conformer = new Conformer(distances);
    return conformer.conformed();
  }

  /**
   * The conformer does the real meat of the work, running the dynamic programmer.
   * <p>
   * Here the problem is set up to the dynamic programmer. The stages are the increments
   * of time as done WRT the input points while the available states are determined by
   * the legs available for conformance.
   * <p>
   * I.e. each leg corresponds a potential state and the weight of the state is based on
   * the cross track distance between the track and the state at that point in time.
   */
  private class Conformer {

    private ScoreSet scores;
    private List<State> states;
    // transitions + weights are static and distance based
    private Map<State, List<DynamicProgrammerTransition>> transitions;

    private Conformer(ScoreSet dists) {
      this.scores = dists;
      this.states = IntStream.range(0, scores.scores().columns)
          .mapToObj(col -> new State(legs.get(col)))
          .collect(Collectors.toList());

      // pre-build the transition scores
      this.transitions = new HashMap<>();
      states.forEach(t -> {
        List<DynamicProgrammerTransition> ts = Lists.transform(states,
            s -> new Transition(s, transitionScore(t.leg(), s.leg())));
        transitions.put(t, ts);
      });
    }

    /**
     * Allow transitions between adjacent legs, otherwise scale the penalty based on the
     * distance from the end of the leg.
     */
    private double transitionScore(L l1, L l2) {
      return l1.scorer().transitionScore(l2);
    }

    private List<Scored<H, L>> conformed() {
      DynamicProgrammer<Long, State> algorithm = new DynamicProgrammer<>(
          Arrays.stream(scores.times()).boxed().collect(Collectors.toList()),
          states,
          DynamicProgrammer.Optimization.MAXIMIZE);

      NavigableMap<Long, DynamicProgrammer.ScoredState<State>> path = algorithm.optimalPath();

      Map<State, List<Map.Entry<Long, DynamicProgrammer.ScoredState<State>>>> scoredStates = path.entrySet().stream()
          .collect(Collectors.groupingBy(e -> e.getValue().state()));

      return scoredStates.entrySet().stream()
          .map(e -> {
            State s = e.getKey();
            List<Long> ts = new ArrayList<>();
            List<Double> cm = new ArrayList<>();

            e.getValue().forEach(p -> {
              ts.add(p.getKey());
              cm.add(p.getValue().score());
            });

            List<Double> ctd = new ArrayList<>();

            ts.forEach(t -> {
              int row = Arrays.binarySearch(scores.times(), t);
              int col = s.index;
              ctd.add(scores.scores().get(row, col));
            });

            Scores associatedScores = new Scores.Builder()
                .setCumulative(cm)
                .setScores(ctd)
                .setTimes(ts)
                .build();

            return ts.isEmpty() ? null : new Scored<H, L>(s.leg()).setAssociatedScores(associatedScores);
          })
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }

    /**
     * The dynamic programmer states map to available legs for conformance.
     * <p>
     * The state object itself carries functionality around the leg index
     * (i.e. hashCode).
     */
    private class State implements DynamicProgrammerState<Long> {

      private final L leg;
      private final int index;

      private State(L leg) {
        this(leg, legs.indexOf(leg));
      }

      private State(L leg, int index) {
        this.leg = leg;
        this.index = index;
      }

      L leg() {
        return leg;
      }

      @Override
      public double getValue(Long tau) {
        int row = Arrays.binarySearch(scores.times(), tau);
        return scores.scores().get(row, index);
      }

      @Override
      public List<DynamicProgrammerTransition> getPossibleTransitions(Long tau) {
        int row = Arrays.binarySearch(scores.times(), tau);
        int n = row + 1;
        return n > scores.times().length ? Collections.emptyList() : transitions.get(this);
      }

      @Override
      public boolean equals(Object other) {
        if (other == null) {
          return false;
        }
        if (this.getClass() == other.getClass()) {
          return leg().equals(((State) other).leg());
        }
        return false;
      }

      @Override
      public int hashCode() {
        return leg.hashCode();
      }

      @Override
      public String toString() {
        return leg.toString();
      }
    }

    private class Transition implements DynamicProgrammerTransition<Long, State> {

      private final State connected;
      private final double prob;

      private Transition(State conn, double prob) {
        this.connected = conn;
        this.prob = prob;
      }

      @Override
      public State getTransition() {
        return connected;
      }

      @Override
      public Double getTransitionProbability() {
        return prob;
      }
    }
  }

  public static <H extends HasTime, L extends Scorable<H, L>> TimeBasedScoreMaximizer<H, L> to(List<L> legs) {
    return new TimeBasedScoreMaximizer<>(legs);
  }
}
