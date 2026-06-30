package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincAirportGate;
import org.mitre.boogie.xml.v23_4.generated.AirportGate;

class ArincAirportGateConverterTest {
  private final ArincAirportGateConverter converter = ArincAirportGateConverter.INSTANCE;

  @Test
  void nullGateReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(null));
  }

  @Test
  void invalidGateReturnsEmpty() {
    AirportGate gate = new AirportGate();
    assertEquals(Optional.empty(), converter.apply(gate));
  }

  @Test
  void validGateConverts() {
    Optional<ArincAirportGate> result = converter.apply(TestGeneratedObjects.newValidAirportGate());
    assertTrue(result.isPresent());

    ArincAirportGate gate = result.get();
    assertAll(
        () -> assertNotNull(gate.recordInfo()),
        () -> assertNotNull(gate.pointInfo()),
        () -> assertEquals("K6", gate.pointInfo().icaoCode()),
        () -> assertEquals("A12", gate.pointInfo().identifier()),
        () -> assertEquals(Optional.of("Gate A12"), gate.pointInfo().name())
    );
  }
}
