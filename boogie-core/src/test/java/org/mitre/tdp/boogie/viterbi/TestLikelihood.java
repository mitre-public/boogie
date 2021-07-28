package org.mitre.tdp.boogie.viterbi;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    Likelihood result = Likelihood.copyOf(b);
    assertAll(
        () -> assertEquals(-6931, result.logLikelihood(), 1.0), // log-likelihood is extremely low
        () -> assertEquals(0, Math.exp(result.logLikelihood())) // this is why we need log-likelihood -- the standard likelihood value underflows to 0
    );
  }
}