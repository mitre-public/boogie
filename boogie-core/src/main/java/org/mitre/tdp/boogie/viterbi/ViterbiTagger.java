package org.mitre.tdp.boogie.viterbi;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Returns the maximum-likelihood state path for a sequence of observations (aka "stages").
 * <p>
 * Uses the specified scoring functions as likelihood values.
 * <p>
 * All output likelihood values are expressed in
 */
public final class ViterbiTagger<STAGE, STATE> {

  private static final Logger LOG = LoggerFactory.getLogger(ViterbiTagger.class);

  private final List<STAGE> stages;
  private final Set<STATE> states;
  private final BiFunction<STAGE, STATE, Double> stateScorer;
  private final Function<STATE, Collection<STATE>> validTransitions;
  private final BiFunction<STATE, STATE, Double> transitionScorer;

  /**
   * The output of the algorithm is the "trellis" of maximum-likelihood paths from the initial stage through each valid
   * state/stage combination
   */
  private final ViterbiTrellis<STAGE, STATE> trellis;

  /**
   * Indicates whether the process was interrupted while executing. If it was then the result of any optimal path calls will
   * return the result od the {@link #interruptedReturnSupplier}.
   */
  private boolean interrupted = false;

  /**
   * This function can take a long time to execute since it's basically a {@code m x n^2} computation where m is the number of stages
   * and n is the number of available states - assuming all transitions are valid.
   */
  private Supplier<ViterbiTrellis<STAGE, STATE>> interruptedReturnSupplier = ViterbiTrellis::empty;

  /**
   * Creates a new dynamic programmer with the given stages, states, and optimization function.
   */
  private ViterbiTagger(
      List<STAGE> stages,
      Set<STATE> states,
      BiFunction<STAGE, STATE, Double> stateScorer,
      Function<STATE, Collection<STATE>> validTransitions,
      BiFunction<STATE, STATE, Double> transitionScorer
  ) {
    this.stages = stages;
    this.states = states;
    this.stateScorer = requireNonNull(stateScorer);
    this.validTransitions = memoize(validTransitions);
    this.transitionScorer = memoize(transitionScorer);
    this.trellis = new ViterbiTrellis<>(this.stages, this.states);
  }

  /**
   * Instantiates a new {@link ViterbiTagger} but with stage/state scoring and state-to-state transition scoring taken from a
   * provided {@link ViterbiScoringStrategy}.
   */
  public static <STAGE, STATE> ViterbiTagger<STAGE, STATE> with(
      Collection<? extends STAGE> stages,
      Collection<? extends STATE> states,
      ViterbiScoringStrategy<STAGE, STATE> scoringStrategy,
      ViterbiTransitionStrategy<STAGE, STATE> transitionStrategy
  ) {
    requireNonNull(scoringStrategy);
    requireNonNull(transitionStrategy);
    return with(
        stages,
        states,
        scoringStrategy::scoreStateGivenStage,
        transitionStrategy::validTransitionsFrom,
        transitionStrategy::scoreTransitionFrom
    );
  }

  /**
   * Instantiate a ViterbiTagger with the specified stages, states, and scoring functions.
   */
  public static <STAGE, STATE> ViterbiTagger<STAGE, STATE> with(
      Collection<? extends STAGE> stages,
      Collection<? extends STATE> states,
      BiFunction<STAGE, STATE, Double> stateScorer,
      Function<STATE, Collection<STATE>> validTransitions,
      BiFunction<STATE, STATE, Double> transitionScorer
  ) {
    return new ViterbiTagger<>(new ArrayList<>(stages), new LinkedHashSet<>(states), stateScorer, validTransitions, transitionScorer);
  }

  /**
   * Configures an interruption return supplier. Given the potential long runtime of the algorithm it supports interruptions
   * where the return of {@link #optimalPath()} will be given by this supplier if it was interrupted during computation.
   */
  public ViterbiTagger<STAGE, STATE> setInterruptedReturnSupplier(Supplier<ViterbiTrellis<STAGE, STATE>> supplier) {
    this.interruptedReturnSupplier = supplier;
    return this;
  }

  public int totalStates() {
    return states.size();
  }

  public int totalStages() {
    return stages.size();
  }

  public int totalCells() {
    return totalStates() * totalStages();
  }

  public int totalTransitions() {
    return totalStates() * totalStates() * totalStages();
  }

  public ViterbiTrellis<STAGE, STATE> trellis() {
    checkComputeOptimals();
    return interrupted ? this.interruptedReturnSupplier.get() : trellis;
  }

  public Map<STAGE, STATE> optimalPath() {
    return trellis().optimalPath();
  }

  private void checkComputeOptimals() {
    if (trellis.isEmpty()) {
      computeOptimalStates();
    }
  }

  void computeOptimalStates() {
    if (LOG.isInfoEnabled()) {
      LOG.info("Computing maximum-likelihood path for trellis with:\n {} states,\n {} stages,\n {} cells,\n {} transitions",
          String.format("%,d", totalStates()),
          String.format("%,d", totalStages()),
          String.format("%,d", totalCells()),
          String.format("%,d", totalTransitions()));
    }
    for (int stageIndex = 0; stageIndex < stages.size(); stageIndex++) {
      if (Thread.interrupted()) {
        LOG.warn("Viterbi optimal path computation was interrupted.");
        this.interrupted = true;
        break;
      }
      updateTrellis(stageIndex);
    }
  }

  /**
   * Takes the stage we want to transition to and generates the collection of possible valid transitions from that state to
   * other available states at the provided stage.
   */
  private void updateTrellis(int toStageIndex) {
    STAGE toStage = stages.get(toStageIndex);
    trellis.initializeStage(toStage);
    if (toStageIndex == 0) {
      trellis.setInitialStageLikelihoods();
    } else {
      STAGE fromStage = stages.get(toStageIndex - 1);
      // identify best transition to each state in toStage
      for (STATE fromState : trellis.statesInStage(fromStage)) {
        // for each fromState, get all non-zero transition scores
        for (STATE toState : validTransitions.apply(fromState)) {
          Likelihood transitionScore = Likelihood.valueOf(transitionScorer.apply(fromState, toState));
          trellis.updateTransitionLikelihood(fromStage, toStage, fromState, toState, transitionScore);
        }
      }
    }
    for (STATE toState : trellis.statesInStage(toStage)) {
      try {
        Likelihood stateScore = Likelihood.valueOf(stateScorer.apply(toStage, toState));
        trellis.updateStateLikelihood(toStage, toState, stateScore);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid likelihood value for state: " + toState + ", stage: " + toStage, e);
      }
    }
  }

  public static <STAGE extends Comparable<? super STAGE>, STATE extends HmmState<STAGE>> ViterbiTagger<STAGE, STATE> forHmmStates(Collection<? extends STAGE> stages, Collection<? extends STATE> states) {
    BiFunction<STAGE, STATE, Double> scorer = (STAGE stage, STATE s) -> s.getValue(stage);
    Function<STATE, Collection<STATE>> validTransitions = (STATE s) -> s.getPossibleTransitions().stream().map(x -> (STATE) x.getTransition()).collect(Collectors.toList());
    BiFunction<STATE, STATE, Double> transitionScorer = (STATE s, STATE s2) -> s.getPossibleTransitions().stream().filter(x -> x.getTransition().equals(s2)).map(x -> x.getTransitionProbability()).findFirst().orElse(0.0);
    return ViterbiTagger.with(stages, states, scorer, validTransitions, transitionScorer);
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
