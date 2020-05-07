package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;


/**
 * An implementation of a DynamicProgrammer for min/max optimization.
 */
public class DynamicProgrammer<Stage extends Comparable<? super Stage>, State extends DynamicProgrammerState<Stage>> {

  private final Optimization optimization;
  // we want a consistent ordering so this is deterministic when the end states have ties
  private final LinkedHashMap<State, OptimizedState> states;
  private final NavigableSet<Stage> stages;

  /**
   * Creates a new dynamic programmer with the given stages, states, and optimization function.
   */
  public DynamicProgrammer(Collection<Stage> stages, Collection<State> states, Optimization optimize) {
    this.optimization = optimize;
    this.states = states.stream()
        .collect(Collectors.toMap(
            Function.identity(),
            OptimizedState::new,
            (a, b) -> {
              throw new RuntimeException();
            },
            LinkedHashMap::new));
    this.stages = new TreeSet<>(stages);
  }

  private void initialize() {
    states.forEach((state, optimals) -> {
      Stage stage = stages.first();
      optimals.put(stage, new OptimalTransition(state.getValue(stage), state, null, stage, null));
    });
  }

  public Map<State, OptimizedState> optimizedStates() {
    checkComputeOptimals();
    return states;
  }

  public NavigableMap<Stage, ScoredState<State>> optimalPath() {
    return optimalPath(stages.first(), stages.last());
  }

  /**
   * Return the optimal path between the start and end stages.
   */
  private NavigableMap<Stage, ScoredState<State>> optimalPath(Stage start, Stage end) {
    checkComputeOptimals();
    Comparator<Double> comp = optimization.comparator();

    // grab the optimal state at the target stage
    // note - there can be ties here, hard to say what the best option is
    // but using a linked hash map above at least makes it deterministic
    Map.Entry<State, OptimizedState> endState = states.entrySet().stream()
        .min((e1, e2) -> comp.compare(
            e1.getValue().get(end).score(),
            e2.getValue().get(end).score()))
        .orElseThrow(RuntimeException::new);

    NavigableMap<Stage, ScoredState<State>> path = new TreeMap<>();
    path.put(end, new ScoredState<>(endState.getValue().get(end).score, endState.getKey()));

    // walk backwards through the elements to the start stage saving the
    // optimal state at each
    Stage cstage = end;
    State cstate = endState.getKey();
    while (!cstage.equals(start)) {
      OptimizedState copt = states.get(cstate);
      OptimalTransition transition = copt.get(cstage);
      cstage = transition.fromStage();
      cstate = transition.fromState();
      path.put(cstage, new ScoredState<>(states.get(cstate).get(cstage).score(), cstate));
    }

    return path;
  }

  private void checkComputeOptimals() {
    if (states.entrySet().iterator().next().getValue().empty()) {
      computeOptimalStates();
    }
  }

  DynamicProgrammer<Stage, State> computeOptimalStates() {
    initialize();

    stages.tailSet(stages.first(), false)
        .forEach(stage -> states.keySet().forEach(state -> optimalTransition(stage, state)));
    return this;
  }

  /**
   * Takes the stage we want to transition to and the current state, and
   * generates the collection of possible valid transitions from that state
   * to other available states at the provided stage.
   */
  private void optimalTransition(Stage toStage, State fromState) {
    Stage fromStage = stages.lower(toStage);
    List<OptimalTransition> transitions;
    if (null == fromStage) {
      transitions = Collections.singletonList(new OptimalTransition(
          fromState.getValue(toStage),
          fromState,
          null,
          toStage,
          null));
    } else {
      List<? extends DynamicProgrammerTransition> ts = fromState.getPossibleTransitions(fromStage);

      // stream over the possible transitions and find the optimal one
      transitions = ts.stream()
          .map(t -> {
            DynamicProgrammerTransition<Stage, State> trans = (DynamicProgrammerTransition<Stage, State>) t;
            return BigDecimal.valueOf(trans.getTransitionProbability()).equals(BigDecimal.valueOf(0.0d))
                ? null
                : candidateTransition(trans, fromStage, toStage, fromState);
          })
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }
    // insert the optimal transitions
    transitions.forEach(transition -> states.get(transition.toState).putIfOptimal(toStage, transition));
  }

  /**
   * Builds a candidate optimal transition between fromState and toState at
   * between the fromStage and toStage.
   */
  private OptimalTransition candidateTransition(DynamicProgrammerTransition<Stage, State> transitionTo, Stage fromStage, Stage toStage, State fromState) {
    // extract the transition score from the raw transition probability
    double transitionScore = optimization.transitionScore(transitionTo.getTransitionProbability());
    double toStateValue = transitionTo.getTransition().getValue(toStage);

    // grab the cumulative score for the from state at the from stage
    OptimizedState optState = states.get(fromState);
    OptimalTransition optTrans = optState.get(fromStage);
    double fromStateCumulative = optTrans.score();

    // combine the to state value at the to stage with the transition score and
    // add to the prior cumulative score, setting as the final transition score
    double cumScore = fromStateCumulative + (toStateValue * transitionScore);
    return new OptimalTransition(cumScore, transitionTo.getTransition(), fromState, toStage, fromStage);
  }

  public enum Direction {
    FORWARD,
    BACKWARD
  }

  public enum Optimization {
    MAXIMIZE,
    MINIMIZE;

    public double transitionScore(double raw) {
      Preconditions.checkArgument(raw <= 1.0 && raw >= 0.0, "Score must be in the interval [0,1]: " + raw);
      return raw;
    }

    public <T extends Comparable<? super T>> Comparator<T> comparator() {
      return this.equals(MAXIMIZE) ? Comparator.reverseOrder() : Comparator.naturalOrder();
    }
  }

  public static class ScoredState<STATE> implements Comparable<ScoredState> {
    private final double score;
    private final STATE state;

    ScoredState(double sc, STATE st) {
      this.score = sc;
      this.state = st;
    }

    public double score() {
      return score;
    }

    public STATE state() {
      return state;
    }

    @Override
    public int compareTo(ScoredState s) {
      return Doubles.compare(score, s.score());
    }
  }

  class OptimizedState {
    private final State state;
    private final HashMap<Stage, OptimalTransition> scores;

    OptimizedState(State state) {
      this.state = state;
      this.scores = new HashMap<>();
    }

    public State state() {
      return state;
    }

    private boolean empty() {
      return this.scores.isEmpty();
    }

    public OptimalTransition get(Stage stage) {
      return scores.get(stage);
    }

    public OptimizedState put(Stage stage, OptimalTransition transition) {
      scores.put(stage, transition);
      return this;
    }

    public OptimizedState putIfOptimal(Stage stage, OptimalTransition transition) {
      OptimalTransition ctrans = scores.get(stage);
      if (null == ctrans) {
        scores.put(stage, transition);
      } else {
        // smart
        Comparator<OptimalTransition> comp = optimization.comparator();
        if (comp.compare(ctrans, transition) > 0) {
          scores.put(stage, transition);
        }
      }
      return this;
    }
  }

  /**
   * Each optimal transition represents the optimal score for a given state at the listed stage.
   * It also contains a pointer to the source state that produced the optimal score.
   */
  class OptimalTransition implements Comparable<OptimalTransition> {
    private final double score;
    private final State toState;
    private final Stage toStage;
    private final State fromState;
    private final Stage fromStage;

    OptimalTransition(double s, State tstate, State fstate, Stage tostage, Stage fromstage) {
      this.score = s;
      this.toState = tstate;
      this.fromState = fstate;
      this.toStage = tostage;
      this.fromStage = fromstage;
    }

    public double score() {
      return score;
    }

    public State toState() {
      return toState;
    }

    public State fromState() {
      return fromState;
    }

    public Stage toStage() {
      return toStage;
    }

    public Stage fromStage() {
      return fromStage;
    }

    @Override
    public int compareTo(OptimalTransition transition) {
      return Double.compare(score, transition.score);
    }
  }
}