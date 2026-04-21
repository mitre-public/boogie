package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.AirportHeliportLocalizerMarker;

class ArincLocalizerGlideslopeMarkerValidatorTest {
  private final ArincLocalizerGlideslopeMarkerValidator validator = ArincLocalizerGlideslopeMarkerValidator.INSTANCE;

  @Test
  void validMarkerPasses() {
    assertTrue(validator.test(TestGeneratedObjects.newValidLocalizerMarker()));
  }

  @Test
  void nullRecordTypeFails() {
    AirportHeliportLocalizerMarker marker = TestGeneratedObjects.newValidLocalizerMarker();
    marker.setRecordType(null);
    assertFalse(validator.test(marker));
  }

  @Test
  void nullIdentifierFails() {
    AirportHeliportLocalizerMarker marker = TestGeneratedObjects.newValidLocalizerMarker();
    marker.setIdentifier(null);
    assertFalse(validator.test(marker));
  }

  @Test
  void nullIcaoCodeFails() {
    AirportHeliportLocalizerMarker marker = TestGeneratedObjects.newValidLocalizerMarker();
    marker.setIcaoCode(null);
    assertFalse(validator.test(marker));
  }

  @Test
  void nullLocationFails() {
    AirportHeliportLocalizerMarker marker = TestGeneratedObjects.newValidLocalizerMarker();
    marker.setLocation(null);
    assertFalse(validator.test(marker));
  }

  @Test
  void nullLatitudeFails() {
    AirportHeliportLocalizerMarker marker = TestGeneratedObjects.newValidLocalizerMarker();
    marker.getLocation().setLatitude(null);
    assertFalse(validator.test(marker));
  }

  @Test
  void nullLongitudeFails() {
    AirportHeliportLocalizerMarker marker = TestGeneratedObjects.newValidLocalizerMarker();
    marker.getLocation().setLongitude(null);
    assertFalse(validator.test(marker));
  }

  @Test
  void nullCycleDateFails() {
    AirportHeliportLocalizerMarker marker = TestGeneratedObjects.newValidLocalizerMarker();
    marker.setCycleDate(null);
    assertFalse(validator.test(marker));
  }
}
