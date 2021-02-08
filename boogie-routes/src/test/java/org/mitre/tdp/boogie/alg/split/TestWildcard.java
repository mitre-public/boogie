package org.mitre.tdp.boogie.alg.split;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestWildcard {

  @Test
  void testTailored() {
    assertTrue(Wildcard.TAILORED.test("+*/"));
  }

  @Test
  void testSuppressed() {
    assertTrue(Wildcard.SUPPRESSED.test("+*/"));
  }

  @Test
  void testPlus() {
    assertTrue(Wildcard.PLUS.test("+*/"));
  }
}
