package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.Runway;

class ArincRunwayValidatorTest {
  private final ArincRunwayValidator validator = ArincRunwayValidator.INSTANCE;

  @Test
  void validRunwayPasses() {
    assertTrue(validator.test(TestGeneratedObjects.newValidRunway()));
  }

  @Test
  void nullRecordTypeFails() {
    Runway runway = TestGeneratedObjects.newValidRunway();
    runway.setRecordType(null);
    assertFalse(validator.test(runway));
  }

  @Test
  void nullIdentifierFails() {
    Runway runway = TestGeneratedObjects.newValidRunway();
    runway.setIdentifier(null);
    assertFalse(validator.test(runway));
  }

  @Test
  void nullIcaoCodeFails() {
    Runway runway = TestGeneratedObjects.newValidRunway();
    runway.setIcaoCode(null);
    assertFalse(validator.test(runway));
  }

  @Test
  void nullLocationFails() {
    Runway runway = TestGeneratedObjects.newValidRunway();
    runway.setLocation(null);
    assertFalse(validator.test(runway));
  }

  @Test
  void nullLatitudeFails() {
    Runway runway = TestGeneratedObjects.newValidRunway();
    runway.getLocation().setLatitude(null);
    assertFalse(validator.test(runway));
  }

  @Test
  void nullLongitudeFails() {
    Runway runway = TestGeneratedObjects.newValidRunway();
    runway.getLocation().setLongitude(null);
    assertFalse(validator.test(runway));
  }

  @Test
  void nullCycleDateFails() {
    Runway runway = TestGeneratedObjects.newValidRunway();
    runway.setCycleDate(null);
    assertFalse(validator.test(runway));
  }

  @Test
  void nullRunwayIdentifierFails() {
    Runway runway = TestGeneratedObjects.newValidRunway();
    runway.setRunwayIdentifier(null);
    assertFalse(validator.test(runway));
  }
}
