package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincLocalizerGlideslopeMarker;
import org.mitre.boogie.xml.model.fields.FreqUnitOfMeasure;
import org.mitre.boogie.xml.model.fields.LocatorAddInfo;
import org.mitre.boogie.xml.model.fields.LocatorCollocation;
import org.mitre.boogie.xml.model.fields.LocatorCoverage;
import org.mitre.boogie.xml.model.fields.LocatorFacility1;
import org.mitre.boogie.xml.model.fields.LocatorFacility2;
import org.mitre.boogie.xml.model.fields.MarkerType;
import org.mitre.boogie.xml.model.fields.NavaidWeatherInfo;
import org.mitre.boogie.xml.model.fields.RunwayLeftRightCenterType;
import org.mitre.boogie.xml.v23_4.generated.AirportHeliportLocalizerMarker;

class ArincLocalizerGlideslopeMarkerConverterTest {
  private final ArincLocalizerGlideslopeMarkerConverter converter = ArincLocalizerGlideslopeMarkerConverter.INSTANCE;

  @Test
  void nullMarkerReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(null));
  }

  @Test
  void invalidMarkerReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(new AirportHeliportLocalizerMarker()));
  }

  @Test
  void validMarkerConverts() {
    Optional<ArincLocalizerGlideslopeMarker> result = converter.apply(TestGeneratedObjects.newValidLocalizerMarker());
    assertTrue(result.isPresent());

    ArincLocalizerGlideslopeMarker m = result.get();

    assertAll(
        () -> assertNotNull(m.recordInfo()),
        () -> assertNotNull(m.pointInfo()),
        () -> assertEquals("K6", m.pointInfo().icaoCode()),
        () -> assertEquals("OM01C", m.pointInfo().identifier()),

        () -> assertEquals(Optional.of("LOC-IDCA"), m.localizerRef()),
        () -> assertEquals(280, m.elevation()),
        () -> assertEquals(Optional.of("LOM"), m.locatorFacilityCharacteristics()),

        () -> assertEquals(350.0, m.locatorFrequencyValue().orElse(null), 0.001),
        () -> assertEquals(Optional.of(FreqUnitOfMeasure.KILO_HERTZ), m.locatorFreqUnitOfMeasure()),

        () -> assertEquals(Optional.of(MarkerType.OM), m.markerType()),
        () -> assertEquals(180.0, m.minorAxisBearing().orElse(null), 0.001),

        () -> assertEquals(Integer.valueOf(1), m.runwayNumber()),
        () -> assertEquals(Optional.of(RunwayLeftRightCenterType.CENTER), m.runwayLeftRightCenterType()),

        () -> assertEquals(Optional.of(LocatorFacility1.NDB), m.locatorFacility1()),
        () -> assertEquals(Optional.of(LocatorFacility2.OUTER_MARKER), m.locatorFacility2()),
        () -> assertEquals(Optional.of(LocatorCoverage.HIGH_POWER_NDB), m.locatorCoverage()),
        () -> assertEquals(Optional.of(LocatorAddInfo.NO_VOICE_ON_FREQUENCY), m.locatorAddInfo()),
        () -> assertEquals(Optional.of(NavaidWeatherInfo.AUTOMATED), m.locatorWeatherInfo()),
        () -> assertEquals(Optional.of(LocatorCollocation.LOCATOR_MARKER_COLLOCATED), m.locatorCollocation())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    AirportHeliportLocalizerMarker marker = TestGeneratedObjects.newValidLocalizerMarker();
    marker.setLocatorFrequency(null);
    marker.setRunwayIdentifier(null);
    marker.setLocatorClass(null);
    marker.setMarkerType(null);
    marker.setMinorAxisBearing(null);

    Optional<ArincLocalizerGlideslopeMarker> result = converter.apply(marker);
    assertTrue(result.isPresent());

    ArincLocalizerGlideslopeMarker m = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), m.locatorFrequencyValue()),
        () -> assertEquals(Optional.empty(), m.locatorFreqUnitOfMeasure()),
        () -> assertNull(m.runwayNumber()),
        () -> assertEquals(Optional.empty(), m.runwayLeftRightCenterType()),
        () -> assertEquals(Optional.empty(), m.runwaySuffix()),
        () -> assertEquals(Optional.empty(), m.locatorFacility1()),
        () -> assertEquals(Optional.empty(), m.locatorFacility2()),
        () -> assertEquals(Optional.empty(), m.locatorCoverage()),
        () -> assertEquals(Optional.empty(), m.locatorAddInfo()),
        () -> assertEquals(Optional.empty(), m.locatorWeatherInfo()),
        () -> assertEquals(Optional.empty(), m.locatorCollocation()),
        () -> assertEquals(Optional.empty(), m.markerType()),
        () -> assertEquals(Optional.empty(), m.minorAxisBearing())
    );
  }
}
