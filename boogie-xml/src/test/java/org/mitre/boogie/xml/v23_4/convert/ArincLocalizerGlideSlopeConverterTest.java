package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincLocalizerGlideSlope;
import org.mitre.boogie.xml.model.fields.FreqUnitOfMeasure;
import org.mitre.boogie.xml.model.fields.IlsBackCourse;
import org.mitre.boogie.xml.model.fields.IlsDmeLocation;
import org.mitre.boogie.xml.model.fields.LocalizerPositionReference;
import org.mitre.boogie.xml.model.fields.NavaidWeatherInfo;
import org.mitre.boogie.xml.model.fields.PrecisionApproachCategory;
import org.mitre.boogie.xml.model.fields.RunwayLeftRightCenterType;
import org.mitre.boogie.xml.model.fields.TrueBearingSource;
import org.mitre.boogie.xml.model.fields.VhfNavaidCoverage;
import org.mitre.boogie.xml.v23_4.generated.LocalizerGlideslope;
import org.mitre.tdp.boogie.MagneticVariation;

class ArincLocalizerGlideSlopeConverterTest {
  private final ArincLocalizerGlideSlopeConverter converter = ArincLocalizerGlideSlopeConverter.INSTANCE;

  @Test
  void nullLgReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(null));
  }

  @Test
  void invalidLgReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(new LocalizerGlideslope()));
  }

  @Test
  void validLgConverts() {
    Optional<ArincLocalizerGlideSlope> result = converter.apply(TestGeneratedObjects.newValidLocalizerGlideslope());
    assertTrue(result.isPresent());

    ArincLocalizerGlideSlope lg = result.get();

    assertAll(
        () -> assertNotNull(lg.recordInfo()),
        () -> assertNotNull(lg.pointInfo()),
        () -> assertEquals("K6", lg.pointInfo().icaoCode()),
        () -> assertEquals("IDCA", lg.pointInfo().identifier()),

        () -> assertEquals(110.3, lg.frequencyValue().orElse(null), 0.001),
        () -> assertEquals(Optional.of(FreqUnitOfMeasure.MEGA_HERTZ), lg.freqUnitOfMeasure()),

        () -> assertEquals(3.0, lg.approachAngle().orElse(null), 0.001),
        () -> assertEquals(9.5, lg.approachCourseBearingValue().orElse(null), 0.001),
        () -> assertEquals(Optional.of(true), lg.approachCourseBearingIsTrueBearing()),
        () -> assertEquals(Optional.of(PrecisionApproachCategory.ILS_MLS_GLS_CAT_1), lg.category()),

        () -> assertEquals(Integer.valueOf(1), lg.runwayNumber()),
        () -> assertEquals(Optional.of(RunwayLeftRightCenterType.CENTER), lg.runwayLeftRightCenterType()),

        () -> assertEquals(1, lg.approachRouteIdent().size()),
        () -> assertEquals("I01C", lg.approachRouteIdent().get(0)),

        () -> assertEquals(1.4, lg.glideslopeBeamWidth().orElse(null), 0.001),
        () -> assertEquals(Optional.of(50L), lg.glideslopeHeightAtLandingThreshold()),
        () -> assertNotNull(lg.glideslopeLocation().orElse(null)),
        () -> assertEquals(Optional.of(300L), lg.glideslopePosition()),
        () -> assertEquals(Optional.of(1000L), lg.localizerPosition()),
        () -> assertEquals(Optional.of(LocalizerPositionReference.BEYOND_STOP_END), lg.localizerPositionReference()),
        () -> assertEquals(9.5, lg.localizerTrueBearing().orElse(null), 0.001),
        () -> assertEquals(Optional.of(TrueBearingSource.OFFICIAL), lg.localizerTrueBearingSource()),
        () -> assertEquals(5.0, lg.localizerWidth().orElse(null), 0.001),

        () -> assertEquals(Optional.of(MagneticVariation.ofDegrees(3.5)), lg.stationDeclination()),

        () -> assertEquals(Optional.of(IlsBackCourse.UNUSABLE), lg.ilsBackCourse()),
        () -> assertEquals(Optional.of(IlsDmeLocation.COLLOCATED_LOCALIZER), lg.ilsDmeLocation()),

        () -> assertEquals(Optional.of(VhfNavaidCoverage.TERMINAL), lg.vhfNavaidCoverage()),
        () -> assertEquals(Optional.of(NavaidWeatherInfo.AUTOMATED), lg.vhfNavaidWeatherInfo()),
        () -> assertEquals(Optional.of(false), lg.isNotCollocated()),
        () -> assertEquals(Optional.of(false), lg.isBiased()),
        () -> assertEquals(Optional.of(true), lg.isNoVoice()),

        () -> assertEquals(Optional.of("SUPP-FAC-IDCA"), lg.supportingFacilityRef()),
        () -> assertEquals(Optional.of("RWY-01C"), lg.runwayRef()),
        () -> assertEquals(Optional.of(313), lg.elevation()),
        () -> assertEquals(Optional.of(false), lg.isVFRCheckpoint())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    LocalizerGlideslope lg = TestGeneratedObjects.newValidLocalizerGlideslope();
    lg.setApproachCourseBearing(null);
    lg.setRunwayIdentifier(null);
    lg.setStationDeclination(null);
    lg.setGlideslopeLocation(null);
    lg.setNavaidClass(null);
    lg.setIlsBackCourse(null);
    lg.setIlsDmeLocation(null);
    lg.setCategory(null);

    Optional<ArincLocalizerGlideSlope> result = converter.apply(lg);
    assertTrue(result.isPresent());

    ArincLocalizerGlideSlope ils = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), ils.approachCourseBearingValue()),
        () -> assertEquals(Optional.empty(), ils.approachCourseBearingIsTrueBearing()),
        () -> assertNull(ils.runwayNumber()),
        () -> assertEquals(Optional.empty(), ils.runwayLeftRightCenterType()),
        () -> assertEquals(Optional.empty(), ils.runwaySuffix()),
        () -> assertEquals(Optional.empty(), ils.stationDeclination()),
        () -> assertEquals(Optional.empty(), ils.glideslopeLocation()),
        () -> assertEquals(Optional.empty(), ils.vhfNavaidCoverage()),
        () -> assertEquals(Optional.empty(), ils.vhfNavaidWeatherInfo()),
        () -> assertEquals(Optional.empty(), ils.isNotCollocated()),
        () -> assertEquals(Optional.empty(), ils.isBiased()),
        () -> assertEquals(Optional.empty(), ils.isNoVoice()),
        () -> assertEquals(Optional.empty(), ils.ilsBackCourse()),
        () -> assertEquals(Optional.empty(), ils.ilsDmeLocation()),
        () -> assertEquals(Optional.empty(), ils.category())
    );
  }
}
