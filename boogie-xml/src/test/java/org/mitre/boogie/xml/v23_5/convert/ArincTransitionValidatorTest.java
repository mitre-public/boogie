package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_5.generated.SidRunwayTransition;

class ArincTransitionValidatorTest {

  private static final ArincTransitionValidator VALIDATOR = ArincTransitionValidator.INSTANCE;

  @Test
  void validRoute() {
    assertTrue(VALIDATOR.test(TestGeneratedObjects.newValidSidRunwayTransition()));
  }

  @Test
  void nullIdentifier() {
    SidRunwayTransition srt = TestGeneratedObjects.newValidSidRunwayTransition();
    srt.setIdentifier(null);
    assertFalse(VALIDATOR.test(srt));
  }

  @Test
  void emptyLegs() {
    SidRunwayTransition srt = TestGeneratedObjects.newValidSidRunwayTransition();
    srt.getProcedureLeg().clear();
    assertFalse(VALIDATOR.test(srt));
  }
}
