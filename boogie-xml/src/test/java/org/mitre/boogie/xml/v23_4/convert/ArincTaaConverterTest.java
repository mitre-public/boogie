package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.fields.ArincTaa;
import org.mitre.boogie.xml.model.fields.ArincTaaSectorDetails;
import org.mitre.boogie.xml.model.fields.MagneticTrueIndicator;
import org.mitre.boogie.xml.model.fields.TaaSectorIdentifier;
import org.mitre.boogie.xml.v23_4.generated.Taa;

class ArincTaaConverterTest {

  private static final ArincTaaConverter CONVERTER = ArincTaaConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    assertEquals(Optional.empty(), CONVERTER.apply(null));
  }

  @Test
  void invalidReturnsEmpty() {
    Taa taa = new Taa();
    assertEquals(Optional.empty(), CONVERTER.apply(taa));
  }

  @Test
  void validTaaConverts() {
    Optional<ArincTaa> result = CONVERTER.apply(TestGeneratedObjects.newValidTaa());
    assertTrue(result.isPresent());

    ArincTaa taa = result.get();
    assertAll(
        () -> assertNotNull(taa.recordInfo()),
        () -> assertEquals(Optional.of(TaaSectorIdentifier.STRAIGHT_IN_OR_CENTER_FIX), taa.taaFixPositionIndicator()),
        () -> assertEquals(Optional.of(MagneticTrueIndicator.MAGNETIC), taa.magneticTrueIndicator()),
        () -> assertEquals(Optional.of("WPT-IAF-1"), taa.taaIafWaypointRef()),
        () -> assertEquals(Optional.of("WPT-SECTOR-1"), taa.sectorBearingWaypointRef()),
        () -> assertEquals(1, taa.approachTypeIdentifier().size()),
        () -> assertEquals("I01C", taa.approachTypeIdentifier().get(0)),
        () -> assertEquals(2, taa.sectorDetails().size())
    );

    ArincTaaSectorDetails sd1 = taa.sectorDetails().get(0);
    assertAll(
        () -> assertEquals(2500, sd1.sectorAltitude()),
        () -> assertEquals(0, sd1.sectorBearingBegin()),
        () -> assertEquals(180, sd1.sectorBearingEnd()),
        () -> assertEquals(25, sd1.sectorRadius()),
        () -> assertEquals(0, sd1.sectorRadiusStart()),
        () -> assertEquals(25, sd1.sectorRadiusEnd()),
        () -> assertFalse(sd1.procedureTurn())
    );

    ArincTaaSectorDetails sd2 = taa.sectorDetails().get(1);
    assertAll(
        () -> assertEquals(3000, sd2.sectorAltitude()),
        () -> assertTrue(sd2.procedureTurn())
    );
  }
}
