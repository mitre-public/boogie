package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_5.generated.Helipad;

class ArincHelipadValidatorTest {
  private final ArincHelipadValidator validator = ArincHelipadValidator.INSTANCE;

  @Test
  void validHelipadPasses() {
    assertTrue(validator.test(TestGeneratedObjects.newValidHelipad()));
  }

  @Test
  void nullRecordTypeFails() {
    Helipad helipad = TestGeneratedObjects.newValidHelipad();
    helipad.setRecordType(null);
    assertFalse(validator.test(helipad));
  }

  @Test
  void nullIdentifierFails() {
    Helipad helipad = TestGeneratedObjects.newValidHelipad();
    helipad.setIdentifier(null);
    assertFalse(validator.test(helipad));
  }

  @Test
  void nullIcaoCodeFails() {
    Helipad helipad = TestGeneratedObjects.newValidHelipad();
    helipad.setIcaoCode(null);
    assertFalse(validator.test(helipad));
  }

  @Test
  void nullLocationFails() {
    Helipad helipad = TestGeneratedObjects.newValidHelipad();
    helipad.setLocation(null);
    assertFalse(validator.test(helipad));
  }

  @Test
  void nullLatitudeFails() {
    Helipad helipad = TestGeneratedObjects.newValidHelipad();
    helipad.getLocation().setLatitude(null);
    assertFalse(validator.test(helipad));
  }

  @Test
  void nullLongitudeFails() {
    Helipad helipad = TestGeneratedObjects.newValidHelipad();
    helipad.getLocation().setLongitude(null);
    assertFalse(validator.test(helipad));
  }

  @Test
  void nullCycleDateFails() {
    Helipad helipad = TestGeneratedObjects.newValidHelipad();
    helipad.setCycleDate(null);
    assertFalse(validator.test(helipad));
  }
}
