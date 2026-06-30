package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_5.generated.Airport;

class ArincAirportValidatorTest {
  private final ArincAirportValidator validator = new ArincAirportValidator();

  @Test
  void validAirportPasses() {
    assertTrue(validator.test(TestGeneratedObjects.newValidAirport()));
  }

  @Test
  void nullNameFails() {
    Airport airport = TestGeneratedObjects.newValidAirport();
    airport.setName(null);
    assertFalse(validator.test(airport));
  }

  @Test
  void nullLocationFails() {
    Airport airport = TestGeneratedObjects.newValidAirport();
    airport.setLocation(null);
    assertFalse(validator.test(airport));
  }
}
