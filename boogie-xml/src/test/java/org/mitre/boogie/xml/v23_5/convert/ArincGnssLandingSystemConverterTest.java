package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincGnssLandingSystem;
import org.mitre.boogie.xml.model.fields.PrecisionApproachCategory;
import org.mitre.boogie.xml.v23_5.generated.Gls;

class ArincGnssLandingSystemConverterTest {

  private static final ArincGnssLandingSystemConverter CONVERTER = ArincGnssLandingSystemConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    assertEquals(Optional.empty(), CONVERTER.apply(null));
  }

  @Test
  void invalidReturnsEmpty() {
    Gls gls = new Gls();
    assertEquals(Optional.empty(), CONVERTER.apply(gls));
  }

  @Test
  void validGlsConverts() {
    Optional<ArincGnssLandingSystem> result = CONVERTER.apply(TestGeneratedObjects.newValidGls());
    assertTrue(result.isPresent());

    ArincGnssLandingSystem gls = result.get();
    assertAll(
        () -> assertNotNull(gls.recordInfo()),
        () -> assertNotNull(gls.pointInfo()),
        () -> assertEquals(21000, gls.glsChannel()),
        () -> assertEquals(Optional.of(3.0), gls.approachAngle()),
        () -> assertEquals(Optional.of(9.5), gls.approachCourseBearingValue()),
        () -> assertEquals(Optional.of(true), gls.approachCourseBearingIsTrueBearing()),
        () -> assertEquals(Optional.of(PrecisionApproachCategory.ILS_MLS_GLS_CAT_1), gls.category()),
        () -> assertEquals(1, gls.runwayNumber()),
        () -> assertEquals(Optional.of(20L), gls.serviceVolumeRadius()),
        () -> assertEquals(Optional.of(313), gls.stationElevationWgs84()),
        () -> assertEquals(Optional.of("LAAS"), gls.stationType()),
        () -> assertEquals(Optional.of("01"), gls.tdmaSlots()),
        () -> assertEquals(Optional.of(50L), gls.thresholdCrossingHeight()),
        () -> assertEquals(Optional.of("RWY-01C"), gls.runwayRef()),
        () -> assertEquals(Optional.of(313), gls.elevation()),
        () -> assertEquals(Optional.of(false), gls.isVFRCheckpoint())
    );
  }
}
