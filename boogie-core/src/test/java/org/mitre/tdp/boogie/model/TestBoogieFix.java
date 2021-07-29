package org.mitre.tdp.boogie.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestBoogieFix {

  @Test
  void testEqualsAndHashCode() {
    EqualsVerifier.forClass(BoogieFix.class).verify();
  }

  @Test
  void testToFromBuilder() {
    BoogieFix fix = new BoogieFix.Builder()
        .fixIdentifier("TEST")
        .fixRegion("TEST")
        .latitude(0.)
        .longitude(0.)
        .publishedVariation(0.)
        .modeledVariation(0.)
        .elevation(0.)
        .build();

    assertEquals(fix, fix.toBuilder().build(), "toBuilder().build() should return equal fix.");
  }
}
