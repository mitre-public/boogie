package org.mitre.tdp.boogie.alg.split;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class RouteTokenTest {

  @Test
  void testStandard_EqualsAndHashCode() {
    EqualsVerifier.forClass(RouteToken.Standard.class).verify();
  }

  @Test
  void testFaaIfr_EqualsAndHashCode() {
    EqualsVerifier.forClass(RouteToken.FaaIfr.class).verify();
  }

  @Test
  void testIcao_EqualsAndHashCode() {
    EqualsVerifier.forClass(RouteToken.Icao.class).verify();
  }
}
