package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_5.generated.Taa;

class ArincTaaValidatorTest {

  private static final ArincTaaValidator VALIDATOR = ArincTaaValidator.INSTANCE;

  @Test
  void validTaa() {
    assertTrue(VALIDATOR.test(TestGeneratedObjects.newValidTaa()));
  }

  @Test
  void nullRecordType() {
    Taa taa = TestGeneratedObjects.newValidTaa();
    taa.setRecordType(null);
    assertFalse(VALIDATOR.test(taa));
  }

  @Test
  void nullCycleDate() {
    Taa taa = TestGeneratedObjects.newValidTaa();
    taa.setCycleDate(null);
    assertFalse(VALIDATOR.test(taa));
  }

  @Test
  void nullTaaFixPositionIndicator() {
    Taa taa = TestGeneratedObjects.newValidTaa();
    taa.setTaaFixPositionIndicator(null);
    assertFalse(VALIDATOR.test(taa));
  }

  @Test
  void emptySectorDetails() {
    Taa taa = TestGeneratedObjects.newValidTaa();
    taa.getSectorTaaDetails().clear();
    assertFalse(VALIDATOR.test(taa));
  }
}
