package org.mitre.tdp.boogie.viterbi;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;

class TestViterbiFeatureVectorExtractor {

  @Test
  void testFeatureCollector() {
    ViterbiFeatureVectorExtractor.FeatureCollector featureCollector = new ViterbiFeatureVectorExtractor.FeatureCollector();

    ViterbiFeatureVector vector = Stream.of(Pair.of("1", 1.), Pair.of("2", 2.), Pair.of("3", 3.)).collect(featureCollector);
    assertAll(
        () -> assertEquals(1., vector.featureValue("1")),
        () -> assertEquals(2., vector.featureValue("2")),
        () -> assertEquals(3., vector.featureValue("3")),
        // fail on replicate features in the collection
        () -> assertThrows(IllegalArgumentException.class, () -> Stream.of(Pair.of("1", 1.), Pair.of("1", 1.)).collect(featureCollector))
    );
  }

  @Test
  void testFeatureExtractorAdditionThrowsExceptionOnDuplicate() {
    ViterbiFeatureVectorExtractor.Builder<Integer, Integer> extractor = ViterbiFeatureVectorExtractor.newBuilder();

    assertThrows(IllegalArgumentException.class, () -> extractor.addFeatureExtractor("1", (stage, state) -> 1.).addFeatureExtractor("1", (stage, state) -> 1.));

    ViterbiFeatureVectorExtractor.Builder<Integer, Integer> extractor2 = ViterbiFeatureVectorExtractor.<Integer, Integer>newBuilder()
        .addFeatureExtractor("1", (stage, state) -> 1.).addFeatureExtractor("2", (stage, state) -> 2.);

    ViterbiFeatureVector vector = extractor2.build().apply(1, 2);
    assertAll(
        () -> assertEquals(1., vector.featureValue("1")),
        () -> assertEquals(2., vector.featureValue("2"))
    );
  }

  @Test
  void testMultiFeatureExtractionAdditionThrowsExceptionOnDuplicate() {
    ViterbiFeatureVectorExtractor.Builder<Integer, Integer> extractor = ViterbiFeatureVectorExtractor.newBuilder();

    assertAll(
        () -> assertThrows(IllegalArgumentException.class, () -> extractor.addMultiFeatureExtractor((stage, state) -> Arrays.asList(Pair.of("1", 1.), Pair.of("1", 2.)), "1", "1")),
        () -> assertThrows(IllegalArgumentException.class, () -> extractor.addMultiFeatureExtractor((stage, state) -> Arrays.asList(Pair.of("1", 1.), Pair.of("2", 2.))))
    );

    ViterbiFeatureVectorExtractor.Builder<Integer, Integer> extractor2 = ViterbiFeatureVectorExtractor.<Integer, Integer>newBuilder()
        .addMultiFeatureExtractor((stage, state) -> Arrays.asList(Pair.of("1", 1.), Pair.of("2", 2.)), "1", "2");

    ViterbiFeatureVector vector = extractor2.build().apply(1, 2);
    assertAll(
        () -> assertEquals(1., vector.featureValue("1")),
        () -> assertEquals(2., vector.featureValue("2"))
    );
  }
}
