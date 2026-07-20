package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_5.generated.Sid;

class ArincProcedureValidatorTest {

  private static final ArincProcedureValidator VALIDATOR = ArincProcedureValidator.INSTANCE;

  @Test
  void validProcedure() {
    assertTrue(VALIDATOR.test(TestGeneratedObjects.newValidSid()));
  }

  @Test
  void nullIdentifier() {
    Sid sid = TestGeneratedObjects.newValidSid();
    sid.setIdentifier(null);
    assertFalse(VALIDATOR.test(sid));
  }

  @Test
  void nullReferenceId() {
    Sid sid = TestGeneratedObjects.newValidSid();
    sid.setReferenceId(null);
    assertFalse(VALIDATOR.test(sid));
  }
}
