package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_5.generated.Vor;

class ArincVhfNavaidValidatorTest {
  private final ArincVhfNavaidValidator validator = ArincVhfNavaidValidator.INSTANCE;

  @Test
  void validVorPasses() {
    assertTrue(validator.test(TestGeneratedObjects.newValidVor()));
  }

  @Test
  void nullRecordTypeFails() {
    Vor vor = TestGeneratedObjects.newValidVor();
    vor.setRecordType(null);
    assertFalse(validator.test(vor));
  }

  @Test
  void nullIdentifierFails() {
    Vor vor = TestGeneratedObjects.newValidVor();
    vor.setIdentifier(null);
    assertFalse(validator.test(vor));
  }

  @Test
  void nullIcaoCodeFails() {
    Vor vor = TestGeneratedObjects.newValidVor();
    vor.setIcaoCode(null);
    assertFalse(validator.test(vor));
  }

  @Test
  void nullLocationFails() {
    Vor vor = TestGeneratedObjects.newValidVor();
    vor.setLocation(null);
    assertFalse(validator.test(vor));
  }

  @Test
  void nullLatitudeFails() {
    Vor vor = TestGeneratedObjects.newValidVor();
    vor.getLocation().setLatitude(null);
    assertFalse(validator.test(vor));
  }

  @Test
  void nullLongitudeFails() {
    Vor vor = TestGeneratedObjects.newValidVor();
    vor.getLocation().setLongitude(null);
    assertFalse(validator.test(vor));
  }

  @Test
  void nullCycleDateFails() {
    Vor vor = TestGeneratedObjects.newValidVor();
    vor.setCycleDate(null);
    assertFalse(validator.test(vor));
  }

  @Test
  void nullVorFrequencyFails() {
    Vor vor = TestGeneratedObjects.newValidVor();
    vor.setVorFrequency(null);
    assertFalse(validator.test(vor));
  }

  @Test
  void nullNavaidClassFails() {
    Vor vor = TestGeneratedObjects.newValidVor();
    vor.setNavaidClass(null);
    assertFalse(validator.test(vor));
  }
}
