package org.mitre.tdp.boogie.viterbi;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestViterbiFeatureVector {

  private static final ViterbiFeatureVector vector = new ViterbiFeatureVector.Builder().addFeature("f1", 0.).addFeature("f2", 1.).build();

  @Test
  void testFeatureContainment() {
    assertAll(
        () -> assertTrue(vector.containsFeature("f1")),
        () -> assertTrue(vector.containsFeature("f2")),
        () -> assertFalse(vector.containsFeature("HI")),
        () -> assertThrows(NullPointerException.class, () -> vector.containsFeature(null))
    );
  }

  @Test
  void testFeatureValue() {
    assertAll(
        () -> assertEquals(0., vector.featureValue("f1")),
        () -> assertEquals(1., vector.featureValue("f2")),
        () -> assertThrows(NullPointerException.class, () -> vector.featureValue("HI"))
    );
  }

  @Test
  void testFailsOnDuplicateFeatureAddition() {
    ViterbiFeatureVector.Builder builder = new ViterbiFeatureVector.Builder();
    assertThrows(IllegalArgumentException.class, () -> builder.addFeature("K", 0.).addFeature("K", 1.));
  }
}
