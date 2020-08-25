package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.mitre.tdp.boogie.conformance.alg.assign.ScoreBasedRouteResolver;

/**
 * Returns the maximum-likelihood state path for a sequence of observations (aka "stages").
 */
public class ViterbiTagger<Stage extends Comparable<? super Stage>, State extends HmmState<Stage>> {

  // we want a consistent ordering so this is deterministic when the end states have ties
  private final LinkedHashMap<State, ViterbiState> states;
  private final NavigableSet<Stage> stages;

  /**
   * Indicates whether the process was interrupted while executing. If it was then the result of any optimal path calls will
   * return the result od the {@link #interruptedReturnSupplier}.
   */
  private boolean interrupted = false;
  /**
   * This function can take a long time to execute since its basically a m x n^2 computation where m is the number of stages
   * and n is the number of available states - assuming all transitions are valid.
   */
  private Supplier<NavigableMap<Stage, ScoredState<State>>> interruptedReturnSupplier = TreeMap::new;

  /**
   * Creates a new dynamic programmer with the given stages, states, and optimization function.
   */
  public ViterbiTagger(Collection<? extends Stage> stages, Collection<State> states) {
    this.states = states.stream()
        .collect(Collectors.toMap(
            Function.identity(),
            ViterbiState::new,
            (a, b) -> {
              throw new RuntimeException();
            },
            LinkedHashMap::new));
    this.stages = new TreeSet<>(stages);
  }

  /**
   * Configures an interruption return supplier. Given the potential long runtime of the algorithm it supports interruptions
   * where the return of {@link #optimalPath()} will be given by this supplier if it was interrupted during computation.
   */
  public ViterbiTagger<Stage, State> setInterruptedReturnSupplier(Supplier<NavigableMap<Stage, ScoredState<State>>> supplier) {
    this.interruptedReturnSupplier = supplier;
    return this;
  }

  private void initialize() {
    states.forEach((state, optimals) -> {
      Stage stage = stages.first();
      optimals.put(stage, new ViterbiTransition(state.getValue(stage), 0, 0, 0, state, null, stage, null));
    });
  }

  public Map<State, ViterbiState> optimizedStates() {
    checkComputeOptimals();
    return states;
  }

  public NavigableMap<Stage, ScoredState<State>> optimalScoredPath() {
    return interrupted ? interruptedReturnSupplier.get() : optimalPath(stages.first(), stages.last());
  }

  public NavigableMap<Stage, State> optimalPath() {
    return optimalScoredPath().entrySet().stream()
        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue().state(), (a, b) -> {throw new RuntimeException();}, TreeMap::new));
  }

  /**
   * Return the optimal path between the start and end stages.
   */
  private NavigableMap<Stage, ScoredState<State>> optimalPath(Stage start, Stage end) {
    checkComputeOptimals();

    Comparator<Double> comp = Comparator.reverseOrder();

    // take the final state with the highest or lowest score depending on optimization type
    Comparator<Map.Entry<State, ViterbiState>> entryScoreComparator =
        (e1, e2) -> comp.compare(
            e1.getValue().get(end).score(),
            e2.getValue().get(end).score());

    // take the path with the fewest visited states if there are score ties
    Comparator<Map.Entry<State, ViterbiState>> entryTotalStateComparator = Comparator.comparingLong(
        entry -> resolvePath(start, end, entry).values().stream().map(ScoredState::state).distinct().count());

    // grab the optimal state at the target stage
    // note - there can be ties here, hard to say what the best option is
    // but using a linked hash map above at least makes it deterministic
    Map.Entry<State, ViterbiState> endState = states.entrySet().stream()
        .min(entryScoreComparator.thenComparing(entryTotalStateComparator))
        .orElseThrow(RuntimeException::new);

    return resolvePath(start, end, endState);
  }

  /**
   * Resolves the path from start to end stage which resulted in the given optimal end state.
   */
  private NavigableMap<Stage, ScoredState<State>> resolvePath(Stage start, Stage end, Map.Entry<State, ViterbiState> endState) {
    NavigableMap<Stage, ScoredState<State>> path = new TreeMap<>();
    path.put(end, new ScoredState<>(endState.getValue().get(end).score, endState.getKey()));

    // walk backwards through the elements to the start stage saving the
    // optimal state at each
    Stage cstage = end;
    State cstate = endState.getKey();
    while (!cstage.equals(start)) {
      ViterbiState copt = states.get(cstate);
      ViterbiTransition transition = copt.get(cstage);
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

  ViterbiTagger<Stage, State> computeOptimalStates() {
    initialize();
    NavigableSet<Stage> remainingStages = stages.tailSet(stages.first(), false);

    for (Stage stage : remainingStages) {
      if (Thread.interrupted()) {
        this.interrupted = true;
        break;
      }
      states.keySet().forEach(state -> optimalTransition(stage, state));
    }
    return this;
  }

  private Map<State, ViterbiTransition> scoresForStage(Stage stage) {
    return states.entrySet().stream().collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue().get(stage)));
  }

  /**
   * Takes the stage we want to transition to and the current state, and
   * generates the collection of possible valid transitions from that state
   * to other available states at the provided stage.
   */
  private void optimalTransition(Stage toStage, State fromState) {
    Stage fromStage = stages.lower(toStage);
    List<ViterbiTransition> transitions;
    if (null == fromStage) {
      transitions = Collections.singletonList(new ViterbiTransition(
          fromState.getValue(toStage),
          0.,
          0.,
          0.,
          fromState,
          null,
          toStage,
          null));
    } else {
      List<? extends HmmTransition> ts = fromState.getPossibleTransitions(fromStage);

      // stream over the possible transitions and find the optimal one
      transitions = ts.stream()
          .map(t -> {
            HmmTransition<Stage, State> trans = (HmmTransition<Stage, State>) t;
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
  private ViterbiTransition candidateTransition(HmmTransition<Stage, State> transitionTo, Stage fromStage, Stage toStage, State fromState) {
    // extract the transition score from the raw transition probability
    double transitionScore = transitionTo.getTransitionProbability();
    double toStateValue = transitionTo.getTransition().getValue(toStage);

    Preconditions.checkState(0.0 <= transitionScore && transitionScore <= 1.0, "Score must be in the interval [0,1]: " + transitionScore);

    // grab the cumulative score for the from state at the from stage
    ViterbiState optState = states.get(fromState);
    ViterbiTransition optTrans = optState.get(fromStage);
    double fromStateCumulative = optTrans.score();

    // combine the to state value at the to stage with the transition score and
    // add to the prior cumulative score, setting as the final transition score
    double cumScore = fromStateCumulative + Math.log(toStateValue * transitionScore);
    return new ViterbiTransition(cumScore, fromStateCumulative, toStateValue, transitionScore, transitionTo.getTransition(), fromState, toStage, fromStage);
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

  private class ViterbiState {
    private final State state;
    private final NavigableMap<Stage, ViterbiTransition> scores;

    ViterbiState(State state) {
      this.state = state;
      this.scores = new TreeMap<>();
    }

    public State state() {
      return state;
    }

    private boolean empty() {
      return this.scores.isEmpty();
    }

    public ViterbiTransition get(Stage stage) {
      return scores.get(stage);
    }

    private ViterbiState put(Stage stage, ViterbiTransition transition) {
      scores.put(stage, transition);
      return this;
    }

    private ViterbiState putIfOptimal(Stage stage, ViterbiTransition transition) {
      ViterbiTransition ctrans = scores.get(stage);
      if (null == ctrans) {
        scores.put(stage, transition);
      } else {
        // smart
        if (transition.compareTo(ctrans) > 0) {
          scores.put(stage, transition);
        } else if (transition.compareTo(ctrans) == 0) {
          System.out.println("FOUND AMBIGUITY!! " + ctrans.fromState + " " + ctrans.score() + " == " + transition.fromState + " " + transition.score());
        }
      }
      return this;
    }
  }

  /**
   * A Viterbi transition represents the transition that is part of the optimal path to a state at the listed stage.
   * It also contains a pointer to the preceding state in the optimal path.
   */
  private class ViterbiTransition implements Comparable<ViterbiTransition> {
    private final double score;
    private final double prevScore;
    private final double stateScore;
    private final double transitionScore;
    private final State toState;
    private final Stage toStage;
    private final State fromState;
    private final Stage fromStage;

    public ViterbiTransition(double cumScore, double fromStateCumulative, double stateScore, double transitionScore, State toState, State fromState, Stage toStage, Stage fromStage) {
      this.score = cumScore;
      this.prevScore = fromStateCumulative;
      this.stateScore = stateScore;
      this.transitionScore = transitionScore;
      this.toState = toState;
      this.fromState = fromState;
      this.toStage = toStage;
      this.fromStage = fromStage;
    }

    public double score() {
      return score;
    }

    public double getPrevScore() {
      return prevScore;
    }

    public double getStateScore() {
      return stateScore;
    }

    public double getTransitionScore() {
      return transitionScore;
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
    public int compareTo(ViterbiTransition transition) {
      return Double.compare(score, transition.score);
    }
  }
}