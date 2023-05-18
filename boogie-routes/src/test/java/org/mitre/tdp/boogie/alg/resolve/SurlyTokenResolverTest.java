package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class SurlyTokenResolverTest {

  @Test
  void testThrowsExceptionOnEmpty() {

    RouteTokenResolver surly = RouteTokenResolver.surly(RouteTokenResolver.noop());

    RouteToken token = RouteToken.standard("KORD", 0.);

    Throwable throwable = assertThrows(IllegalStateException.class, () -> surly.resolve(null, token, null));
    assertTrue(throwable.getLocalizedMessage().contains("KORD"), "Should contain name of offending infrastructure element.");
  }
}
