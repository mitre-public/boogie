package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestIsIntermediateOrInitialApproachFix {

  static final IsIntermediateOrInitialApproachFix UNDER_TEST = IsIntermediateOrInitialApproachFix.INSTANCE;

  @Test
  void test() {
    assertAll(
        () -> assertTrue(UNDER_TEST.isIntermediateOrInitialApproachFix("EA A")),  // A = IAF
        () -> assertTrue(UNDER_TEST.isIntermediateOrInitialApproachFix("EA B")),  // B = Intermediate fix
        () -> assertTrue(UNDER_TEST.isIntermediateOrInitialApproachFix("EA C")),  // C = Holding at IAF
        () -> assertTrue(UNDER_TEST.isIntermediateOrInitialApproachFix("EA D")),  // D = IAF at FACF
        () -> assertFalse(UNDER_TEST.isIntermediateOrInitialApproachFix("EA F")), // F = FAF
        () -> assertFalse(UNDER_TEST.isIntermediateOrInitialApproachFix("EA M")), // M = MAP
        () -> assertFalse(UNDER_TEST.isIntermediateOrInitialApproachFix("EA  "))  // blank = not set
    );
  }
}
