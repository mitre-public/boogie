package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_5.generated.PortCommunication;

class ArincAirportCommunicationsValidatorTest {
  private final ArincAirportCommunicationsValidator validator = ArincAirportCommunicationsValidator.INSTANCE;

  @Test
  void validCommPasses() {
    assertTrue(validator.test(TestGeneratedObjects.newValidPortCommunication()));
  }

  @Test
  void nullRecordTypeFails() {
    PortCommunication comm = TestGeneratedObjects.newValidPortCommunication();
    comm.setRecordType(null);
    assertFalse(validator.test(comm));
  }

  @Test
  void nullCycleDateFails() {
    PortCommunication comm = TestGeneratedObjects.newValidPortCommunication();
    comm.setCycleDate(null);
    assertFalse(validator.test(comm));
  }
}
