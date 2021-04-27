package org.mitre.tdp.boogie.viterbi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intermediate representation of the Viterbi algorithm, containing all information
 * needed to obtain the maximum-likelihood path to arrive at each state in a stage.
 */
public final class ViterbiTrellis<Stage, State> extends LinkedHashMap<Stage, ScoredStage<Stage, State>> {

  private final List<Stage> stages;
  private final Set<State> states;

  public ViterbiTrellis(List<Stage> stages, Set<State> states) {
    this.stages = stages;
    this.states = states;
  }

  public Map<Stage, State> optimalPath() {
    return mapValues(optimalScoredPath(), (ScoredStage.ScoredState<State> x) -> x.state());
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

  public void updateTransitionLikelihood(Stage fromStage, Stage toStage, State fromState, State toState, Likelihood transitionScore) {
    ScoredStage<Stage, State> fromScoredStage = Objects.requireNonNull(this.get(fromStage), "ViterbiTrellis missing expected previous stage");
    ScoredStage<Stage, State> scoredStage = Objects.requireNonNull(this.get(toStage), "ViterbiTrellis missing expected stage");
    Likelihood cumulativeTransitionlikelihood = fromScoredStage.stateLikelihood(fromState).times(transitionScore);
    scoredStage.updateTransitionLikelihood(fromState, toState, cumulativeTransitionlikelihood);
  }

  public void updateStateLikelihood(Stage toStage, State toState, Likelihood stateScore) {
    this.get(toStage).updateStateLikelihood(toState, stateScore);
  }

  void setInitialStageLikelihoods() {
    this.put(stages.get(0), ScoredStage.initialStage(states));
  }

  private Map<Stage, ScoredStage.ScoredState<State>> optimalScoredPath() {
    checkOptimalPath();
    return mapValues(stages, (Stage x) -> this.get(x).viterbiPathScoredState());
  }

  private void checkOptimalPath() {
    State optimalState = null;
    ListIterator<Stage> iterator = stages.listIterator(stages.size());
    while (iterator.hasPrevious()) {
      Stage stage = iterator.previous();
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
  public static <K, V> Map<K, V> mapValues(List<K> s, Function<K, V> mapper) {
    return s.stream()
        .collect(Collectors.toMap(x -> x, mapper, (u, v) -> {throw new IllegalStateException("Collision: " + u + ", " + v);}, LinkedHashMap::new));
  }

  public static <K, V1, V2> Map<K, V2> mapValues(Map<K, V1> m, Function<V1, V2> mapper) {
    return m.entrySet().stream()
        .collect(Collectors.toMap(x -> x.getKey(), x -> mapper.apply(x.getValue()), (u, v) -> {throw new IllegalStateException();}, LinkedHashMap::new));
  }

  public static <S, T> ViterbiTrellis<S, T> empty() {
    return new ViterbiTrellis<>(new ArrayList<>(), new LinkedHashSet<>());
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
  public interface StateVisitor<State, T> {
    T visit(State state, Likelihood stateScore, Likelihood cumulativeStateLikelihood, State fromState);
  }

  @FunctionalInterface
  public interface StageVisitor<Stage, T> {
    void visit(Stage stage, List<T> stageResults);
  }

  private static final Logger LOG = LoggerFactory.getLogger(ViterbiTrellis.class);

  /**
   * Dump to string
   */
  public void dump() {
    LOG.info("Stages");
    int i = 0;
    for (Stage stage : stages) {
      LOG.info("  {}: {}", i++, stage);
    }
    LOG.info("States");
    int j = 0;
    for (State state : states) {
      LOG.info("  {}: {}", j++, state);
    }

    i = 0;
    for (Stage stage : stages) {
      ScoredStage<Stage, State> scoredStage = this.get(stage);
      j = 0;
      for (State state : states) {
        String likelihoods = Stream.of(scoredStage.transitionScores().get(j), scoredStage.stateScores().get(j), scoredStage.likelihoods().get(j)).map(x -> Optional.ofNullable(x).map(y -> Double.toString(y.value())).orElse("?")).collect(Collectors.joining(", "));
        LOG.info("  {} / {}: {}", i, j, likelihoods);
        j++;
      }
      i++;
    }
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
