package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Returns the maximum-likelihood state path for a sequence of observations (aka "stages").
 * <p>
 * Uses the specified scoring functions as likelihood values.
 * <p>
 * All output likelihood values are expressed in
 */
public class ViterbiTagger<Stage extends Comparable<? super Stage>, State> {

  private final NavigableSet<Stage> stages;
  private final LinkedHashSet<State> states; // uses a deterministic ordering
  private final BiFunction<Stage, State, Double> stateScorer;
  private final Function<State, Collection<State>> validTransitions;
  private final BiFunction<State, State, Double> transitionScorer;

  /**
   the output of the algorithm is the "trellis" of maximum-likelihood paths from the initial stage
   * through each valid state/stage combination
   */
  private final ViterbiTrellis<Stage, State> trellis;

  /**
   * Indicates whether the process was interrupted while executing. If it was then the result of any optimal path calls will
   * return the result od the {@link #interruptedReturnSupplier}.
   */
  private boolean interrupted = false;

  /**
   * This function can take a long time to execute since its basically a m x n^2 computation where m is the number of stages
   * and n is the number of available states - assuming all transitions are valid.
   */
  private Supplier<NavigableMap<Stage, State>> interruptedReturnSupplier = TreeMap::new;

  /**
   * Creates a new dynamic programmer with the given stages, states, and optimization function.
   */
  public ViterbiTagger(Collection<? extends Stage> stages, Collection<? extends State> states,
      BiFunction<Stage, State, Double> stateScorer, Function<State, Collection<State>> validTransitions, BiFunction<State, State, Double> transitionScorer) {
    this.stages = new TreeSet<>(stages);
    this.states = new LinkedHashSet<>(states);
    this.stateScorer = stateScorer;
    this.validTransitions = memoize(validTransitions);
    this.transitionScorer = memoize(transitionScorer);
    this.trellis = new ViterbiTrellis<>(this.stages, this.states);
  }

  /**
   * Configures an interruption return supplier. Given the potential long runtime of the algorithm it supports interruptions
   * where the return of {@link #optimalPath()} will be given by this supplier if it was interrupted during computation.
   */
  public ViterbiTagger<Stage, State> setInterruptedReturnSupplier(Supplier<NavigableMap<Stage, State>> supplier) {
    this.interruptedReturnSupplier = supplier;
    return this;
  }

  public ViterbiTrellis<Stage, State> trellis() {
    checkComputeOptimals();
    return trellis;
  }

  public NavigableMap<Stage, State> optimalPath() {
    return trellis().optimalPath();
  }

  private void checkComputeOptimals() {
    if (trellis.isEmpty()) {
      computeOptimalStates();
    }
  }

  void computeOptimalStates() {
    for (Stage stage : stages) {
      if (Thread.interrupted()) {
        this.interrupted = true;
        break;
      }
      updateTrellis(stage);
    }
  }

  /**
   * Takes the stage we want to transition to and
   * generates the collection of possible valid transitions from that state
   * to other available states at the provided stage.
   */
  private void updateTrellis(Stage toStage) {
    Stage fromStage = stages.lower(toStage);
    trellis.initializeStage(toStage);
    if (fromStage == null) {
      trellis.setInitialStageLikelihoods();
    } else {
      // identify best transition to each state in toStage
      for (State fromState : trellis.statesInStage(fromStage)) {
        // for each fromState, get all non-zero transition scores
        for (State toState : validTransitions.apply(fromState)) {
          Likelihood transitionScore = Likelihood.valueOf(transitionScorer.apply(fromState, toState));
          trellis.updateTransitionLikelihood(toStage, fromState, toState, transitionScore);
        }
      }
    }
    for (State toState : trellis.statesInStage(toStage)) {
      try {
        Likelihood stateScore = Likelihood.valueOf(stateScorer.apply(toStage, toState));
        trellis.updateStateLikelihood(toStage, toState, stateScore);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid likelihood value for state: " + toState + ", stage: " + toStage, e);
      }
    }

    trellis.assertComplete(toStage);
  }

  public static <Stage extends Comparable<? super Stage>, State extends HmmState<Stage>> ViterbiTagger<Stage, State> forHmmStates(Collection<? extends Stage> stages, Collection<? extends State> states) {
    BiFunction<Stage, State, Double> scorer = (Stage stage, State s) -> s.getValue(stage);
    Function<State, Collection<State>> validTransitions = (State s) -> s.getPossibleTransitions().stream().map(x -> (State) x.getTransition()).collect(Collectors.toList());
    BiFunction<State, State, Double> transitionScorer = (State s, State s2) -> s.getPossibleTransitions().stream().filter(x -> x.getTransition().equals(s2)).map(x -> x.getTransitionProbability()).findFirst().orElse(0.0);
    return new ViterbiTagger<>(stages, states, scorer, validTransitions, transitionScorer);
  }

  public static <T, R> Function<T, R> memoize(Function<T, R> fn) {
    Map<T, R> map = new ConcurrentHashMap<>();
    return (t) -> map.computeIfAbsent(t, fn);
  }

  public static <T1, T2, R> BiFunction<T1, T2, R> memoize(BiFunction<T1, T2, R> fn) {
    Function<T1, Function<T2, R>> f = memoize((T1 t1) -> memoize((T2 t2) -> fn.apply(t1, t2)));
    return (t1, t2) -> f.apply(t1).apply(t2);
  }

}
