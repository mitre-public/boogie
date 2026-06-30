package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_5.generated.AirportGate;

class ArincAirportGateValidatorTest {
  private final ArincAirportGateValidator validator = ArincAirportGateValidator.INSTANCE;

  @Test
  void validGatePasses() {
    assertTrue(validator.test(TestGeneratedObjects.newValidAirportGate()));
  }

  @Test
  void nullRecordTypeFails() {
    AirportGate gate = TestGeneratedObjects.newValidAirportGate();
    gate.setRecordType(null);
    assertFalse(validator.test(gate));
  }

  @Test
  void nullIdentifierFails() {
    AirportGate gate = TestGeneratedObjects.newValidAirportGate();
    gate.setIdentifier(null);
    assertFalse(validator.test(gate));
  }

  @Test
  void nullIcaoCodeFails() {
    AirportGate gate = TestGeneratedObjects.newValidAirportGate();
    gate.setIcaoCode(null);
    assertFalse(validator.test(gate));
  }

  @Test
  void nullLocationFails() {
    AirportGate gate = TestGeneratedObjects.newValidAirportGate();
    gate.setLocation(null);
    assertFalse(validator.test(gate));
  }

  @Test
  void nullLatitudeFails() {
    AirportGate gate = TestGeneratedObjects.newValidAirportGate();
    gate.getLocation().setLatitude(null);
    assertFalse(validator.test(gate));
  }

  @Test
  void nullLongitudeFails() {
    AirportGate gate = TestGeneratedObjects.newValidAirportGate();
    gate.getLocation().setLongitude(null);
    assertFalse(validator.test(gate));
  }

  @Test
  void nullCycleDateFails() {
    AirportGate gate = TestGeneratedObjects.newValidAirportGate();
    gate.setCycleDate(null);
    assertFalse(validator.test(gate));
  }
}
