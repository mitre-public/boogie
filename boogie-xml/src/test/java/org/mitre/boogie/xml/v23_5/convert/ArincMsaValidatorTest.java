package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_5.generated.Msa;

class ArincMsaValidatorTest {
  private final ArincMsaValidator validator = ArincMsaValidator.INSTANCE;

  @Test
  void validMsaPasses() {
    assertTrue(validator.test(TestGeneratedObjects.newValidMsa()));
  }

  @Test
  void nullRecordTypeFails() {
    Msa msa = TestGeneratedObjects.newValidMsa();
    msa.setRecordType(null);
    assertFalse(validator.test(msa));
  }

  @Test
  void nullCycleDateFails() {
    Msa msa = TestGeneratedObjects.newValidMsa();
    msa.setCycleDate(null);
    assertFalse(validator.test(msa));
  }

  @Test
  void emptySectorsFails() {
    Msa msa = TestGeneratedObjects.newValidMsa();
    msa.getSector().clear();
    assertFalse(validator.test(msa));
  }
}
