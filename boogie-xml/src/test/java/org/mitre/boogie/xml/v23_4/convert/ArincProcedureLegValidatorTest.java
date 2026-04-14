package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.SidLeg;

class ArincProcedureLegValidatorTest {

  private static final ArincProcedureLegValidator VALIDATOR = ArincProcedureLegValidator.INSTANCE;

  @Test
  void validLeg() {
    assertTrue(VALIDATOR.test(TestGeneratedObjects.newValidSidLeg(10)));
  }

  @Test
  void nullRecordType() {
    SidLeg leg = TestGeneratedObjects.newValidSidLeg(10);
    leg.setRecordType(null);
    assertFalse(VALIDATOR.test(leg));
  }

  @Test
  void nullCycleDate() {
    SidLeg leg = TestGeneratedObjects.newValidSidLeg(10);
    leg.setCycleDate(null);
    assertFalse(VALIDATOR.test(leg));
  }

  @Test
  void nullPathAndTermination() {
    SidLeg leg = TestGeneratedObjects.newValidSidLeg(10);
    leg.setPathAndTermination(null);
    assertFalse(VALIDATOR.test(leg));
  }

  @Test
  void validApproachLeg() {
    assertTrue(VALIDATOR.test(TestGeneratedObjects.newValidApproachLeg(10)));
  }

  @Test
  void validStarLeg() {
    assertTrue(VALIDATOR.test(TestGeneratedObjects.newValidStarLeg(10)));
  }
}
