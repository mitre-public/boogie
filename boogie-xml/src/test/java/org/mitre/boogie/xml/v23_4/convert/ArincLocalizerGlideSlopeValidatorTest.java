package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.LocalizerGlideslope;

class ArincLocalizerGlideSlopeValidatorTest {
  private final ArincLocalizerGlideSlopeValidator validator = ArincLocalizerGlideSlopeValidator.INSTANCE;

  @Test
  void validLgPasses() {
    assertTrue(validator.test(TestGeneratedObjects.newValidLocalizerGlideslope()));
  }

  @Test
  void nullRecordTypeFails() {
    LocalizerGlideslope lg = TestGeneratedObjects.newValidLocalizerGlideslope();
    lg.setRecordType(null);
    assertFalse(validator.test(lg));
  }

  @Test
  void nullIdentifierFails() {
    LocalizerGlideslope lg = TestGeneratedObjects.newValidLocalizerGlideslope();
    lg.setIdentifier(null);
    assertFalse(validator.test(lg));
  }

  @Test
  void nullIcaoCodeFails() {
    LocalizerGlideslope lg = TestGeneratedObjects.newValidLocalizerGlideslope();
    lg.setIcaoCode(null);
    assertFalse(validator.test(lg));
  }

  @Test
  void nullLocationFails() {
    LocalizerGlideslope lg = TestGeneratedObjects.newValidLocalizerGlideslope();
    lg.setLocation(null);
    assertFalse(validator.test(lg));
  }

  @Test
  void nullLatitudeFails() {
    LocalizerGlideslope lg = TestGeneratedObjects.newValidLocalizerGlideslope();
    lg.getLocation().setLatitude(null);
    assertFalse(validator.test(lg));
  }

  @Test
  void nullLongitudeFails() {
    LocalizerGlideslope lg = TestGeneratedObjects.newValidLocalizerGlideslope();
    lg.getLocation().setLongitude(null);
    assertFalse(validator.test(lg));
  }

  @Test
  void nullCycleDateFails() {
    LocalizerGlideslope lg = TestGeneratedObjects.newValidLocalizerGlideslope();
    lg.setCycleDate(null);
    assertFalse(validator.test(lg));
  }

  @Test
  void nullFrequencyFails() {
    LocalizerGlideslope lg = TestGeneratedObjects.newValidLocalizerGlideslope();
    lg.setLocalizerGlideslopeFrequency(null);
    assertFalse(validator.test(lg));
  }
}
