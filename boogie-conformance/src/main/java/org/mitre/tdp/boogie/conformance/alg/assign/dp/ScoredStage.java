package org.mitre.tdp.boogie.conformance.alg.assign.dp;

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
class ScoredStage<Stage, State> {

  private final LinkedHashMap<State, ScoredState<State>> scoredStates;

  private State viterbiPathState;

  ScoredStage(LinkedHashMap<State, ScoredState<State>> scoredStates) {
    this.scoredStates = scoredStates;
  }

  Collection<ScoredState<State>> scoredStates() {
    return scoredStates.values();
  }

  Set<State> states() {
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

  void updateTransitionLikelihood(State fromState, State toState, Likelihood l) {
    ScoredState<State> s = scoredStates.get(toState);
    if (s != null) {
      if (s.cumulativeTransitionScore == null || l.compareTo(s.cumulativeTransitionLikelihood()) > 0) {
        this.scoredStates.put(toState, new ScoredState<>(toState, fromState, l, s.stateScore));
      }
    } else {
      this.scoredStates.put(toState, new ScoredState<>(toState, fromState, l, null));
    }
  }

  void updateStateLikelihood(State toState, Likelihood stateScore) {
    ScoredState<State> s = scoredStates.get(toState);
    this.scoredStates.put(toState, new ScoredState<>(toState, s.fromState(), s.cumulativeTransitionLikelihood(), stateScore));
  }

  Likelihood viterbiPathLikelihood() {
    return viterbiPathScoredState().likelihood();
  }

  ScoredState<State> viterbiPathScoredState() {
    return scoredStates.get(viterbiPathState);
  }

  static <Stage, State> ScoredStage<Stage, State> initialStage(Set<State> states) {
    LinkedHashMap<State, ScoredState<State>> scoredStates = states.stream()
        .collect(Collectors.toMap(s -> s, s -> ScoredState.initialState(s),
            (u, v) -> { throw new IllegalStateException();}, LinkedHashMap::new));
    return new ScoredStage<>(scoredStates);
  }

  static <Stage, State> ScoredStage<Stage, State> intermediateStage() {
    return new ScoredStage<>(new LinkedHashMap<>());
  }

  void setViterbiPathState(State state) {
    this.viterbiPathState = state;
  }

  Likelihood stateLikelihood(State fromState) {
    return scoredStates.get(fromState).likelihood();
  }

  static class ScoredState<State> {
    private final State state;
    private final State fromState;
    private final Likelihood cumulativeTransitionScore;
    private final Likelihood stateScore;

    ScoredState(State state, State fromState, Likelihood cumulativeTransitionScore, Likelihood stateScore) {
      this.state = state;
      this.fromState = fromState;
      this.cumulativeTransitionScore = cumulativeTransitionScore;
      this.stateScore = stateScore;
    }

    Likelihood likelihood() {
      return cumulativeTransitionScore == null ? stateScore : cumulativeTransitionScore.times(stateScore);
    }

    State state() {
      return state;
    }

    State fromState() {
      return fromState;
    }

    Likelihood cumulativeTransitionLikelihood() {
      return cumulativeTransitionScore;
    }

    Likelihood stateScore() {
      return stateScore;
    }

    static <State> ScoredState<State> initialState(State state) {
      return new ScoredState<>(state, null, null, null);
    }
  }
}
