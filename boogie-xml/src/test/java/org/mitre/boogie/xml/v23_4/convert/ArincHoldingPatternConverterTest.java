package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincHoldingPattern;

class ArincHoldingPatternConverterTest {

  private static final ArincHoldingPatternConverter CONVERTER = ArincHoldingPatternConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    assertEquals(Optional.empty(), CONVERTER.apply(null));
  }

  @Test
  void invalidReturnsEmpty() {
    org.mitre.boogie.xml.v23_4.generated.HoldingPattern hp = new org.mitre.boogie.xml.v23_4.generated.HoldingPattern();
    assertEquals(Optional.empty(), CONVERTER.apply(hp));
  }

  @Test
  void validHoldingPatternConverts() {
    Optional<ArincHoldingPattern> result = CONVERTER.apply(TestGeneratedObjects.newValidHoldingPattern());
    assertTrue(result.isPresent());

    ArincHoldingPattern hp = result.get();
    assertAll(
        () -> assertNotNull(hp.recordInfo()),
        () -> assertEquals(Optional.of(true), hp.isEnroute()),
        () -> assertEquals(Optional.of(4.0), hp.arcRadius()),
        () -> assertEquals(Optional.of("HOLD01"), hp.holdingPatternName()),
        () -> assertEquals(Optional.of(230L), hp.holdingSpeed()),
        () -> assertEquals("PT4M", hp.holdingTime().orElseThrow().toString()),
        () -> assertEquals(Optional.of(5.0), hp.holdingDistance()),
        () -> assertEquals(Optional.of(270.0), hp.inboundHoldingCourseValue()),
        () -> assertEquals(Optional.of(false), hp.inboundHoldingCourseIsTrue()),
        () -> assertEquals(Optional.of("INBOUND"), hp.legInboundOutboundIndicator()),
        () -> assertEquals(Optional.of(5000), hp.holdMinAltitude()),
        () -> assertEquals(Optional.of(8000), hp.holdMaxAltitude()),
        () -> assertEquals(Optional.of(10000), hp.rvsmMinAltitude()),
        () -> assertEquals(Optional.of(18000), hp.rvsmMaxAltitude()),
        () -> assertEquals(Optional.of("RIGHT"), hp.turnDirection()),
        () -> assertEquals(Optional.of(100L), hp.verticalScaleFactor()),
        () -> assertEquals(Optional.of("FIX01"), hp.fixIdentifier()),
        () -> assertEquals(Optional.of("FIX-FIX01"), hp.fixRef()),
        () -> assertEquals(Optional.of("PORT-K6"), hp.portRef()),
        () -> assertEquals(Optional.of(true), hp.isOnHigh()),
        () -> assertEquals(Optional.of(false), hp.isOnLow()),
        () -> assertEquals(Optional.of(true), hp.isOnSid()),
        () -> assertEquals(Optional.of(false), hp.isOnStar()),
        () -> assertEquals(Optional.of(true), hp.isOnApproach()),
        () -> assertEquals(Optional.of(false), hp.isOnMissedApproach()),
        () -> assertEquals(Optional.of(1L), hp.multipleIndicator()),
        () -> assertEquals(Optional.of("NAVAID-DCA"), hp.inboundCourseNavaid()),
        () -> assertEquals(Optional.of(180.0), hp.inboundCourseTheta())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    org.mitre.boogie.xml.v23_4.generated.HoldingPattern hp = TestGeneratedObjects.newValidHoldingPattern();
    hp.setArcRadius(null);
    hp.setHoldingPatternName(null);
    hp.setHoldingSpeed(null);
    hp.setHoldingTime(null);
    hp.setHoldingDistance(null);
    hp.setInboundHoldingCourse(null);
    hp.setLegInboundOutboundIndicator(null);
    hp.setTurnDirection(null);
    hp.setVerticalScaleFactor(null);
    hp.setFixIdentifier(null);
    hp.setFixRef(null);
    hp.setPortRef(null);
    hp.setHoldingUses(null);
    hp.setMultipleIndicator(null);
    hp.setInboundCourseNavaid(null);
    hp.setInboundCourseTheta(null);
    hp.setHoldMinMaxAltitudes(null);
    hp.setRvsmMinMaxLevels(null);

    Optional<ArincHoldingPattern> result = CONVERTER.apply(hp);
    assertTrue(result.isPresent());

    ArincHoldingPattern converted = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), converted.arcRadius()),
        () -> assertEquals(Optional.empty(), converted.holdingPatternName()),
        () -> assertEquals(Optional.empty(), converted.holdingSpeed()),
        () -> assertEquals(Optional.empty(), converted.holdingTime()),
        () -> assertEquals(Optional.empty(), converted.holdingDistance()),
        () -> assertEquals(Optional.empty(), converted.inboundHoldingCourseValue()),
        () -> assertEquals(Optional.empty(), converted.inboundHoldingCourseIsTrue()),
        () -> assertEquals(Optional.empty(), converted.legInboundOutboundIndicator()),
        () -> assertEquals(Optional.empty(), converted.turnDirection()),
        () -> assertEquals(Optional.empty(), converted.verticalScaleFactor()),
        () -> assertEquals(Optional.empty(), converted.fixIdentifier()),
        () -> assertEquals(Optional.empty(), converted.fixRef()),
        () -> assertEquals(Optional.empty(), converted.portRef()),
        () -> assertEquals(Optional.empty(), converted.isOnHigh()),
        () -> assertEquals(Optional.empty(), converted.isOnLow()),
        () -> assertEquals(Optional.empty(), converted.isOnSid()),
        () -> assertEquals(Optional.empty(), converted.isOnStar()),
        () -> assertEquals(Optional.empty(), converted.isOnApproach()),
        () -> assertEquals(Optional.empty(), converted.isOnMissedApproach()),
        () -> assertEquals(Optional.empty(), converted.multipleIndicator()),
        () -> assertEquals(Optional.empty(), converted.inboundCourseNavaid()),
        () -> assertEquals(Optional.empty(), converted.inboundCourseTheta()),
        () -> assertEquals(Optional.empty(), converted.holdMinAltitude()),
        () -> assertEquals(Optional.empty(), converted.holdMaxAltitude()),
        () -> assertEquals(Optional.empty(), converted.rvsmMinAltitude()),
        () -> assertEquals(Optional.empty(), converted.rvsmMaxAltitude())
    );
  }
}
