package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Intermediate representation of the Viterbi algorithm, containing all information
 * needed to obtain the maximum-likelihood path to arrive at each state in a stage.
 */
public class ViterbiTrellis<Stage extends Comparable<? super Stage>, State> extends TreeMap<Stage, ScoredStage<Stage, State>> {

  private final NavigableSet<Stage> stages;
  private final LinkedHashSet<State> states;

  public ViterbiTrellis(NavigableSet<Stage> stages, LinkedHashSet<State> states) {
    this.stages = stages;
    this.states = states;
  }

  public NavigableMap<Stage, State> optimalPath() {
    return mapValues(optimalScoredPath(), x -> x.state());
  }

  public Likelihood optimalPathScoreAt(Stage stage) {
    return optimalScoredPath().get(stage).likelihood();
  }

  public void initializeStage(Stage toStage) {
    this.put(toStage, ScoredStage.intermediateStage());
  }

  public Set<State> statesInStage(Stage fromStage) {
    ScoredStage<Stage, State> scoredStage = Objects.requireNonNull(this.get(fromStage), "ViterbiTrellis missing stage");
    return scoredStage.states();
  }

  public void updateTransitionLikelihood(Stage toStage, State fromState, State toState, Likelihood transitionScore) {
    ScoredStage<Stage, State> fromScoredStage = Objects.requireNonNull(this.get(stages.lower(toStage)), "ViterbiTrellis missing expected previous stage");
    ScoredStage<Stage, State> scoredStage = Objects.requireNonNull(this.get(toStage), "ViterbiTrellis missing expected stage");
    Likelihood cumulativeTransitionlikelihood = fromScoredStage.stateLikelihood(fromState).times(transitionScore);
    scoredStage.updateTransitionLikelihood(fromState, toState, cumulativeTransitionlikelihood);
  }

  public void updateStateLikelihood(Stage toStage, State toState, Likelihood stateScore) {
    this.get(toStage).updateStateLikelihood(toState, stateScore);
  }

  void setInitialStageLikelihoods() {
    this.put(stages.first(), ScoredStage.initialStage(states));
  }

  private NavigableMap<Stage, ScoredStage.ScoredState<State>> optimalScoredPath() {
    checkOptimalPath();
    return mapValues(stages, x -> this.get(x).viterbiPathScoredState());
  }

  private void checkOptimalPath() {
    State optimalState = null;
    for (Stage stage : stages.descendingSet()) {
      ScoredStage<Stage, State> scoredStage = Objects.requireNonNull(this.get(stage), "ViterbiTrellis missing stage");
      if (optimalState == null) {
        ScoredStage.ScoredState<State> optimalScoredState = scoredStage.scoredStates().stream().max(Comparator.comparing(x -> x.likelihood())).orElseThrow(() -> new IllegalStateException());
        scoredStage.setViterbiPathState(optimalScoredState.state());
        optimalState = optimalScoredState.fromState();
      } else {
        scoredStage.setViterbiPathState(optimalState);
        optimalState = scoredStage.viterbiPathScoredState().fromState();
      }
    }
  }

  // Helpers to create NavigableMaps
  public static <K, V> NavigableMap<K, V> mapValues(NavigableSet<K> s, Function<K, V> mapper) {
    return s.stream()
        .collect(Collectors.toMap(x -> x, mapper, (u, v) -> {throw new IllegalStateException();}, TreeMap::new));
  }

  public static <K, V1, V2> NavigableMap<K, V2> mapValues(NavigableMap<K, V1> m, Function<V1, V2> mapper) {
    return m.entrySet().stream()
        .collect(Collectors.toMap(x -> x.getKey(), x -> mapper.apply(x.getValue()), (u, v) -> {throw new IllegalStateException();}, TreeMap::new));
  }

  public static <S extends Comparable<? super S>, T> ViterbiTrellis<S, T> empty() {
    return new ViterbiTrellis<S, T>(new TreeSet<>(), new LinkedHashSet<>());
  }

  /**
   * Visitor methods allow extracting information about the trellis without granting direct access to the internal data structures.
   */
  public <T> void visit(StateVisitor<State, T> stateVisitor, StageVisitor<Stage, T> stageVisitor) {
    for (Map.Entry<Stage, ScoredStage<Stage, State>> e : this.entrySet()) {
      List<T> stageResults = e.getValue().scoredStates().stream()
          .map(x -> stateVisitor.visit(x.state(), x.stateScore(), x.likelihood(), x.fromState()))
          .collect(Collectors.toList());
      stageVisitor.visit(e.getKey(), stageResults);
    }
  }

  @FunctionalInterface
  public static interface StateVisitor<State, T> {
    T visit(State state, Likelihood stateScore, Likelihood cumulativeStateLikelihood, State fromState);
  }

  @FunctionalInterface
  public static interface StageVisitor<Stage, T> {
    void visit(Stage stage, List<T> stageResults);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ViterbiTrellis<?, ?> that = (ViterbiTrellis<?, ?>) o;
    return stages.equals(that.stages) && states.equals(that.states);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), stages, states);
  }
}
