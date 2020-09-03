package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestLikelihood {
  @Test
  void testLikelihoodProduct() {
    Likelihood a = Likelihood.valueOf(0.5);
    Likelihood b = Likelihood.valueOf(0.8);
    assertEquals(Likelihood.valueOf(0.4), a.times(b));
  }

  @Test
  void testVeryLowLikelihood() {
    Likelihood a = Likelihood.valueOf(0.5);
    Likelihood b = Likelihood.valueOf(1.0);
    for (int i = 0; i < 10000; i++) {
      b = b.times(a);
    }
    assertEquals(-6931, b.logLikelihood(), 1.0); // log-likelihood is extremely low
    assertEquals(0, Math.exp(b.logLikelihood())); // this is why we need log-likelihood -- the standard likelihood value underflows to 0
  }

}