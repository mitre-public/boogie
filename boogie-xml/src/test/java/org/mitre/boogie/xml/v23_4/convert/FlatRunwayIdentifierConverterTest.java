package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.RunwayIdentifier;

class FlatRunwayIdentifierConverterTest {
  private final FlatRunwayIdentifierConverter converter = FlatRunwayIdentifierConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    Optional<FlatRunwayIdentifier> result = converter.apply(null);
    assertTrue(result.isEmpty());
  }

  @Test
  void convertsValidRunwayIdentifier() {
    RunwayIdentifier ri = new RunwayIdentifier();
    ri.setRunwayNumber(9);
    ri.setRunwayLeftRightCenterType(org.mitre.boogie.xml.v23_4.generated.RunwayLeftRightCenterType.LEFT);
    ri.setRunwaySuffix(org.mitre.boogie.xml.v23_4.generated.RunwaySuffix.WATER_SEALANE_OR_WATERWAY);

    FlatRunwayIdentifier result = converter.apply(ri).orElseThrow();
    assertAll(
        () -> assertEquals(9, result.number()),
        () -> assertEquals("LEFT", result.leftRightCenterType()),
        () -> assertEquals("WATER_SEALANE_OR_WATERWAY", result.suffix())
    );
  }

  @Test
  void nullOptionalFieldsInsideRunwayIdentifier() {
    RunwayIdentifier ri = new RunwayIdentifier();
    ri.setRunwayNumber(27);

    FlatRunwayIdentifier result = converter.apply(ri).orElseThrow();
    assertAll(
        () -> assertEquals(27, result.number()),
        () -> assertNull(result.leftRightCenterType()),
        () -> assertNull(result.suffix())
    );
  }
}
