package org.mitre.tdp.boogie.viterbi;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Internal representation of the stages and states within the {@link ViterbiTrellis}.
 * <p>
 * Values here are only meant to be exposed via the {@link ViterbiTrellis}, so all
 * classes and members are package-private.
 */
class ScoredStage<STAGE, STATE> {

  private final LinkedHashMap<STATE, ScoredState<STATE>> scoredStates;

  private STATE viterbiPathState;

  ScoredStage(LinkedHashMap<STATE, ScoredState<STATE>> scoredStates) {
    this.scoredStates = scoredStates;
  }

  Collection<ScoredState<STATE>> scoredStates() {
    return scoredStates.values();
  }

  Set<STATE> states() {
    return scoredStates.keySet();
  }

  List<Likelihood> transitionScores() {
    return scoredStates().stream().map(x -> x.cumulativeTransitionScore).collect(Collectors.toList());
  }

  List<Likelihood> stateScores() {
    return scoredStates().stream().map(x -> x.stateScore).collect(Collectors.toList());
  }

  List<Likelihood> likelihoods() {
    return scoredStates().stream().map(x -> x.likelihood()).collect(Collectors.toList());
  }

  void updateTransitionLikelihood(STATE fromState, STATE toState, Likelihood l) {
    ScoredState<STATE> s = scoredStates.get(toState);
    if (s != null) {
      if (s.cumulativeTransitionScore == null || l.compareTo(s.cumulativeTransitionLikelihood()) > 0) {
        this.scoredStates.put(toState, new ScoredState<>(toState, fromState, l, s.stateScore));
      }
    } else {
      this.scoredStates.put(toState, new ScoredState<>(toState, fromState, l, null));
    }
  }

  void updateStateLikelihood(STATE toState, Likelihood stateScore) {
    ScoredState<STATE> s = scoredStates.get(toState);
    this.scoredStates.put(toState, new ScoredState<>(toState, s.fromState(), s.cumulativeTransitionLikelihood(), stateScore));
  }

  Likelihood viterbiPathLikelihood() {
    return viterbiPathScoredState().likelihood();
  }

  ScoredState<STATE> viterbiPathScoredState() {
    return scoredStates.get(viterbiPathState);
  }

  static <STAGE, STATE> ScoredStage<STAGE, STATE> initialStage(Set<STATE> states) {
    LinkedHashMap<STATE, ScoredState<STATE>> scoredStates = states.stream()
        .collect(Collectors.toMap(s -> s, s -> ScoredState.initialState(s),
            (u, v) -> { throw new IllegalStateException();}, LinkedHashMap::new));
    return new ScoredStage<>(scoredStates);
  }

  static <STAGE, STATE> ScoredStage<STAGE, STATE> intermediateStage() {
    return new ScoredStage<>(new LinkedHashMap<>());
  }

  void setViterbiPathState(STATE state) {
    this.viterbiPathState = state;
  }

  Likelihood stateLikelihood(STATE fromState) {
    return scoredStates.get(fromState).likelihood();
  }

  static class ScoredState<STATE> {
    private final STATE state;
    private final STATE fromState;
    private final Likelihood cumulativeTransitionScore;
    private final Likelihood stateScore;

    ScoredState(STATE state, STATE fromState, Likelihood cumulativeTransitionScore, Likelihood stateScore) {
      this.state = state;
      this.fromState = fromState;
      this.cumulativeTransitionScore = cumulativeTransitionScore;
      this.stateScore = stateScore;
    }

    Likelihood likelihood() {
      return cumulativeTransitionScore == null ? stateScore : cumulativeTransitionScore.times(stateScore);
    }

    STATE state() {
      return state;
    }

    STATE fromState() {
      return fromState;
    }

    Likelihood cumulativeTransitionLikelihood() {
      return cumulativeTransitionScore;
    }

    Likelihood stateScore() {
      return stateScore;
    }

    static <STATE> ScoredState<STATE> initialState(STATE state) {
      return new ScoredState<>(state, null, null, null);
    }
  }
}
