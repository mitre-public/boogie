package org.mitre.tdp.boogie.viterbi;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class TestGraphBackedViterbiScoringStrategy {

  private static final GraphBackedViterbiTransitionStrategy<?, Integer> strategy = new GraphBackedViterbiTransitionStrategy<>(new SampleGraph());

  @Test
  void testValidTransitionsInSampleGraph() {
    List<Integer> downstreamOf1 = Arrays.asList(2, 4);
    List<Integer> downstreamOf2 = Arrays.asList(1, 3);
    List<Integer> downstreamOf3 = Arrays.asList(1, 4);

    assertAll(
        () -> assertEquals(downstreamOf1, strategy.validTransitionsFrom(1)),
        () -> assertEquals(downstreamOf2, strategy.validTransitionsFrom(2)),
        () -> assertEquals(downstreamOf3, strategy.validTransitionsFrom(3))
    );
  }

  @Test
  void testScoreTransitionsInSampleGraph() {
    assertAll(
        () -> assertEquals(7., strategy.scoreTransitionFrom(1, 4)),
        () -> assertEquals(3., strategy.scoreTransitionFrom(1, 2)),
        () -> assertEquals(2., strategy.scoreTransitionFrom(2, 3)),
        () -> assertEquals(1., strategy.scoreTransitionFrom(3, 4))
    );
  }
}
