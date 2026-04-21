package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincHelipad;
import org.mitre.boogie.xml.model.fields.ElevationType;
import org.mitre.boogie.xml.model.fields.HelipadShape;
import org.mitre.boogie.xml.model.fields.HelicopterPerformanceReq;
import org.mitre.boogie.xml.model.fields.RunwaySurfaceCode;
import org.mitre.boogie.xml.v23_4.generated.Helipad;

class ArincHelipadConverterTest {
  private final ArincHelipadConverter converter = ArincHelipadConverter.INSTANCE;

  @Test
  void nullHelipadReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(null));
  }

  @Test
  void invalidHelipadReturnsEmpty() {
    Helipad helipad = new Helipad();
    assertEquals(Optional.empty(), converter.apply(helipad));
  }

  @Test
  void validHelipadConverts() {
    Optional<ArincHelipad> result = converter.apply(TestGeneratedObjects.newValidHelipad());
    assertTrue(result.isPresent());

    ArincHelipad hp = result.get();

    assertAll(
        () -> assertNotNull(hp.recordInfo()),
        () -> assertNotNull(hp.pointInfo()),
        () -> assertEquals("K6", hp.pointInfo().icaoCode()),
        () -> assertEquals("H1", hp.pointInfo().identifier()),

        () -> assertEquals(Optional.of(false), hp.isWithoutLocation()),
        () -> assertEquals(Optional.of(false), hp.isDerivedLocation()),
        () -> assertEquals(Optional.of(false), hp.isElevated()),

        () -> assertEquals(Optional.of(300), hp.elevation()),
        () -> assertEquals(Optional.of(ElevationType.LANDING_THRESHOLD), hp.elevationType()),

        () -> assertEquals(Optional.of(HelipadShape.CIRCLE), hp.helipadShape()),
        () -> assertEquals(Optional.of(RunwaySurfaceCode.HARD), hp.surfaceCode()),
        () -> assertEquals(Optional.of(HelicopterPerformanceReq.MULTI_ENGINE), hp.helicopterPerformanceReq()),
        () -> assertEquals(Optional.of(50), hp.helipadMaximumRotorDiameter()),

        () -> assertEquals(180.0, hp.helipadOrientationBearing().orElse(null), 0.001),
        () -> assertEquals(Optional.of(true), hp.helipadOrientationIsTrueBearing()),
        () -> assertEquals(90.0, hp.helipadIdentifierOrientationBearing().orElse(null), 0.001),
        () -> assertEquals(Optional.of(false), hp.helipadIdentifierOrientationIsTrueBearing()),

        () -> assertEquals(List.of(270.0, 90.0), hp.preferredApproachBearings()),

        () -> assertEquals(Optional.of(30), hp.tlofPadLengthLongSide()),
        () -> assertEquals(Optional.of(20), hp.tlofPadLengthShortSide()),
        () -> assertEquals(Optional.of(25), hp.tlofPadDiameter()),
        () -> assertEquals(Optional.of(40), hp.fatoPadLengthLongSide()),
        () -> assertEquals(Optional.of(30), hp.fatoPadLengthShortSide()),
        () -> assertEquals(Optional.of(35), hp.fatoPadDiameter()),
        () -> assertEquals(Optional.of(50), hp.safetyPadLengthLongSide()),
        () -> assertEquals(Optional.of(40), hp.safetyPadLengthShortSide()),
        () -> assertEquals(Optional.of(45), hp.safetyPadDiameter())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    Helipad helipad = TestGeneratedObjects.newValidHelipad();
    helipad.setHelipadOrientation(null);
    helipad.setHelipadIdentifierOrientation(null);
    helipad.setHelipadTlofDimensions(null);
    helipad.setHelipadFatoDimensions(null);
    helipad.setSafetyDimensions(null);
    helipad.setSurfaceCode(null);
    helipad.setElevation(null);

    Optional<ArincHelipad> result = converter.apply(helipad);
    assertTrue(result.isPresent());

    ArincHelipad hp = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), hp.helipadOrientationBearing()),
        () -> assertEquals(Optional.empty(), hp.helipadOrientationIsTrueBearing()),
        () -> assertEquals(Optional.empty(), hp.helipadIdentifierOrientationBearing()),
        () -> assertEquals(Optional.empty(), hp.helipadIdentifierOrientationIsTrueBearing()),
        () -> assertEquals(Optional.empty(), hp.tlofPadLengthLongSide()),
        () -> assertEquals(Optional.empty(), hp.fatoPadDiameter()),
        () -> assertEquals(Optional.empty(), hp.safetyPadLengthShortSide()),
        () -> assertEquals(Optional.empty(), hp.elevation())
    );
  }
}
