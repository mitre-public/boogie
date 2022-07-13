package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestIsApproachForRunway {

  @Test
  void testMatch() {
    assertAll(
        () -> assertTrue(IsApproachForRunway.test("R04L", "04", "L"), "normal"),
        () -> assertTrue(IsApproachForRunway.test("R04L", "04", null), "Missing parallel designator, we still want to land at least at that number"),
        () -> assertFalse(IsApproachForRunway.test("L04", "04", "L"), "Make sure that the first letter gets skipped to avoid false match"),
        () -> assertTrue(IsApproachForRunway.test("L04LZ", "04", "L"), "This should pass because it has the L and not for the Localizer title")
    );
  }
}
