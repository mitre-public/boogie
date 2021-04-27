package org.mitre.tdp.boogie.viterbi;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TestDelegatingFeatureVectorExtractor {

  @Test
  void testDelegatedFeatureExtraction() {
    ViterbiFeatureVector vector1 = extractor.apply(0, 1).apply(0, 1);
    ViterbiFeatureVector vector2 = extractor.apply(2, 0).apply(2, 0);

    assertAll(
        () -> assertEquals(1., vector1.featureValue("hi")),
        () -> assertEquals(2., vector1.featureValue("mom")),
        () -> assertEquals(3., vector2.featureValue("sup")),
        () -> assertEquals(4., vector2.featureValue("dad")),
        () -> assertThrows(IllegalStateException.class, () -> extractor.apply(0, 3).apply(0, 3))
    );
  }

  private static final ViterbiFeatureVectorExtractor<Integer, Integer> extractor1 = ViterbiFeatureVectorExtractor.<Integer, Integer>newBuilder()
      .addFeatureExtractor("hi", (j, k) -> 1.)
      .addFeatureExtractor("mom", (j, k) -> 2.)
      .build();

  private static final ViterbiFeatureVectorExtractor<Integer, Integer> extractor2 = ViterbiFeatureVectorExtractor.<Integer, Integer>newBuilder()
      .addFeatureExtractor("sup", (j, k) -> 3.)
      .addFeatureExtractor("dad", (j, k) -> 4.)
      .build();

  private static final DelegatingFeatureVectorExtractor<Integer, Integer> extractor = DelegatingFeatureVectorExtractor.<Integer, Integer>newBuilder()
      .addStateDelegatedFeatureExtractor(i -> i.equals(1), extractor1)
      .addStageDelegatedFeatureExtractor(i -> i.equals(2), extractor2)
      .build();
}
