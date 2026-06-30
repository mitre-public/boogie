package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincHeliport;
import org.mitre.boogie.xml.v23_4.generated.Heliport;
import org.mitre.boogie.xml.v23_4.generated.HeliportType;

class ArincHeliportConverterTest {
  private final ArincHeliportConverter converter = ArincHeliportConverter.INSTANCE;

  @Test
  void nullHeliportReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(null));
  }

  @Test
  void invalidHeliportReturnsEmpty() {
    Heliport heliport = new Heliport();
    assertEquals(Optional.empty(), converter.apply(heliport));
  }

  @Test
  void validHeliportConverts() {
    Optional<ArincHeliport> result = converter.apply(TestGeneratedObjects.newValidHeliport());
    assertTrue(result.isPresent());

    ArincHeliport hp = result.get();

    assertAll(
        () -> assertNotNull(hp.portInfo()),
        () -> assertNotNull(hp.portInfo().pointInfo()),
        () -> assertEquals("K6", hp.portInfo().pointInfo().icaoCode()),
        () -> assertEquals("6N5", hp.portInfo().pointInfo().identifier()),
        () -> assertEquals(Optional.of("HOSPITAL"), hp.heliportType()),
        () -> assertFalse(hp.portInfo().helipads().orElseThrow().isEmpty())
    );
  }

  @Test
  void convertsWithNullHeliportType() {
    Heliport heliport = TestGeneratedObjects.newValidHeliport();
    heliport.setHeliportType(null);

    Optional<ArincHeliport> result = converter.apply(heliport);
    assertTrue(result.isPresent());

    ArincHeliport hp = result.get();
    assertEquals(Optional.empty(), hp.heliportType());
  }

  @Test
  void convertsWithNoHelipads() {
    Heliport heliport = TestGeneratedObjects.newValidHeliport();
    heliport.getHelipad().clear();

    Optional<ArincHeliport> result = converter.apply(heliport);
    assertTrue(result.isPresent());

    ArincHeliport hp = result.get();
    assertTrue(hp.portInfo().helipads().orElseThrow().isEmpty());
  }

  @Test
  void convertsAllHeliportTypes() {
    for (HeliportType type : HeliportType.values()) {
      Heliport heliport = TestGeneratedObjects.newValidHeliport();
      heliport.setHeliportType(type);

      Optional<ArincHeliport> result = converter.apply(heliport);
      assertTrue(result.isPresent());
      assertEquals(Optional.of(type.name()), result.get().heliportType());
    }
  }
}
