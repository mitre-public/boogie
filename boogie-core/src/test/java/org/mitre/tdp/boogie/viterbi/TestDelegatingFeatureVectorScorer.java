package org.mitre.tdp.boogie.viterbi;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TestDelegatingFeatureVectorScorer {

  @Test
  void testDelegatedFeatureScoring() {
    ViterbiFeatureVector featureVector = new ViterbiFeatureVector.Builder().build();

    assertAll(
        () -> assertEquals(.1, scorer.apply(0, 1).apply(featureVector)),
        () -> assertEquals(.1, scorer.apply(0, 1).apply(featureVector)),
        () -> assertEquals(.2, scorer.apply(2, 0).apply(featureVector)),
        () -> assertEquals(.2, scorer.apply(2, 0).apply(featureVector)),
        () -> assertThrows(IllegalStateException.class, () -> scorer.apply(0, 3).apply(featureVector))
    );
  }

  private static final DelegatingFeatureVectorScorer<Integer, Integer> scorer = DelegatingFeatureVectorScorer.<Integer, Integer>newBuilder()
      .addStateDelegatedFeatureScorer(i -> i.equals(1), viterbiFeatureVector -> .1)
      .addStageDelegatedFeatureScorer(i -> i.equals(2), viterbiFeatureVector -> .2)
      .build();
}
