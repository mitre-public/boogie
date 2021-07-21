package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestRecommendedNavaid {

  private static final RecommendedNavaid parser = new RecommendedNavaid();

  @Test
  void testFiltersTrimmableInput() {
    assertEquals(Optional.empty(), parser.apply("   "));
  }
}
