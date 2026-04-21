package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincTransition;
import org.mitre.boogie.xml.v23_4.generated.FinalApproach;
import org.mitre.boogie.xml.v23_4.generated.SidRunwayTransition;
import org.mitre.boogie.xml.v23_4.generated.StarRunwayTransition;

class ArincTransitionConverterTest {

  private static final ArincTransitionConverter CONVERTER = ArincTransitionConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    assertEquals(Optional.empty(), CONVERTER.apply(null));
  }

  @Test
  void invalidReturnsEmpty() {
    SidRunwayTransition srt = new SidRunwayTransition();
    assertEquals(Optional.empty(), CONVERTER.apply(srt));
  }

  @Test
  void convertsSidRunwayTransition() {
    Optional<ArincTransition> result = CONVERTER.apply(TestGeneratedObjects.newValidSidRunwayTransition());
    assertTrue(result.isPresent());

    ArincTransition t = result.get();
    assertAll(
        () -> assertEquals("RW09L", t.identifier().get()),
        () -> assertEquals("SidRunwayTransition", t.transitionType()),
        () -> assertEquals(2, t.legs().size()),
        () -> assertEquals(Optional.of(true), t.isFromRunway()),
        () -> assertEquals(Optional.of("RNAV_1"), t.rnavPbnNavSpec()),
        () -> assertEquals(Optional.of("RNP_1"), t.rnpPbnNavSpec()),
        () -> assertEquals(Optional.of(true), t.isDmeReq()),
        () -> assertEquals(Optional.of(false), t.isGnssReq()),
        () -> assertEquals(Optional.of(true), t.isRadarReq())
    );
  }

  @Test
  void convertsStarRunwayTransition() {
    Optional<ArincTransition> result = CONVERTER.apply(TestGeneratedObjects.newValidStarRunwayTransition());
    assertTrue(result.isPresent());

    ArincTransition t = result.get();
    assertAll(
        () -> assertEquals("RW01C", t.identifier().get()),
        () -> assertEquals("StarRunwayTransition", t.transitionType()),
        () -> assertEquals(1, t.legs().size()),
        () -> assertEquals(Optional.of(true), t.isToRunway())
    );
  }

  @Test
  void convertsFinalApproach() {
    Optional<ArincTransition> result = CONVERTER.apply(TestGeneratedObjects.newValidFinalApproach());
    assertTrue(result.isPresent());

    ArincTransition t = result.get();
    assertAll(
        () -> assertEquals("I01C", t.identifier().get()),
        () -> assertEquals("FinalApproach", t.transitionType()),
        () -> assertEquals(2, t.legs().size()),
        () -> assertEquals(Optional.of("DME_REQUIRED"), t.qualifier1()),
        () -> assertEquals(Optional.of(false), t.isRemoteAltimeterRestricted()),
        () -> assertEquals(Optional.of(true), t.baroVnavNotAuthorized()),
        () -> assertEquals(Optional.of(50L), t.procedureTch()),
        () -> assertEquals(Optional.of(2000), t.glideSlopeInterceptAltitude())
    );
  }

  @Test
  void convertsApproachTransition() {
    Optional<ArincTransition> result = CONVERTER.apply(TestGeneratedObjects.newValidApproachTransition());
    assertTrue(result.isPresent());

    ArincTransition t = result.get();
    assertAll(
        () -> assertEquals("GNDLF", t.identifier().get()),
        () -> assertEquals("ApproachTransition", t.transitionType()),
        () -> assertEquals(1, t.legs().size()),
        () -> assertEquals(Optional.of("A"), t.multipleIndicator()),
        () -> assertEquals(Optional.of(false), t.isTfOverlay())
    );
  }

  @Test
  void convertsMissedApproach() {
    Optional<ArincTransition> result = CONVERTER.apply(TestGeneratedObjects.newValidMissedApproach());
    assertTrue(result.isPresent());

    ArincTransition t = result.get();
    assertAll(
        () -> assertEquals("MA01C", t.identifier().get()),
        () -> assertEquals("MissedApproach", t.transitionType()),
        () -> assertEquals(1, t.legs().size())
    );
  }
}
