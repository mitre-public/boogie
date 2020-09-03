package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class TestScoredStage {
  @Test
  void testInitialStage() {
    ScoredStage<Integer, Character> s = ScoredStage.initialStage(new LinkedHashSet<>(Arrays.asList('A', 'B')));
    assertEquals(Arrays.asList('A', 'B'), new ArrayList<>(s.states()));
    assertEquals(Arrays.asList(null, null), s.transitionScores());
    assertEquals(Arrays.asList(null, null), s.stateScores());
    assertEquals(Arrays.asList(null, null), s.likelihoods());
  }

  private List<Likelihood> likelihoods(Double... likelihoods) {
    return Stream.of(likelihoods).map(x -> Likelihood.valueOf(x)).collect(Collectors.toList());
  }
}