package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_5.generated.Heliport;

class ArincHeliportValidatorTest {
  private final ArincHeliportValidator validator = new ArincHeliportValidator();

  @Test
  void validHeliportPasses() {
    assertTrue(validator.test(TestGeneratedObjects.newValidHeliport()));
  }

  @Test
  void nullNameFails() {
    Heliport heliport = TestGeneratedObjects.newValidHeliport();
    heliport.setName(null);
    assertFalse(validator.test(heliport));
  }

  @Test
  void nullLocationFails() {
    Heliport heliport = TestGeneratedObjects.newValidHeliport();
    heliport.setLocation(null);
    assertFalse(validator.test(heliport));
  }
}
