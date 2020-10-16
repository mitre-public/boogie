package org.mitre.tdp.boogie.alg.approach.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

public class TestNoApproachPredictor {

  private SectionSplit newSplit(String name, int index) {
    return new SectionSplit.Builder().setValue(name).setIndex(index).build();
  }

  private ResolvedSection newSection(String name, int index) {
    return new ResolvedSection(newSplit(name, index));
  }

  @Test
  public void testNoApproachPredictor() {
    NoApproachPredictor predictor = new NoApproachPredictor();

    ResolvedSection s1 = newSection("KATL", 0);
    ResolvedSection s2 = newSection("KBOS", 0);

    Optional<ResolvedSection> approach = predictor.predictAndCheck(s1, s2);
    assertFalse(approach.isPresent(), "No approach predictor returned a section with resolved elements...");
  }
}
