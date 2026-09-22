package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

class TestGeomagnetics {

  // NOAA's reference table reports angles to 0.01 degrees and field strengths to 0.1 nT.
  private static final double ANGLE_TOLERANCE_DEGREES = 0.01;
  private static final double INTENSITY_TOLERANCE_NT = 0.1;

  @ParameterizedTest
  @EnumSource(GeomagneticCoefficients.class)
  void CoefficientConstructor_AllPossibleValues_CanInstantiate(GeomagneticCoefficients coeff) {
    assertDoesNotThrow(() -> new Geomagnetics(coeff), "All coefficient instantiations should work.");
  }

  @ParameterizedTest(name = "WMM2025: year={0}, height={1} km, latitude={2}, longitude={3}")
  @CsvFileSource(resources = "/geomagnetics/wmm2025-reference.csv", numLinesToSkip = 1)
  void wmm2025MatchesNoaaReferenceValues(double year, double heightKm, double latitude, double longitude, double north, double east, double vertical, double horizontal, double total, double inclination, double declination) {
    Geomagnetics model = new Geomagnetics(GeomagneticCoefficients.WMM2025);

    assertAll(
        () -> assertEquals(north, model.getNorthIntensity(latitude, longitude, year, heightKm), INTENSITY_TOLERANCE_NT, "North (X)"),
        () -> assertEquals(east, model.getEastIntensity(latitude, longitude, year, heightKm), INTENSITY_TOLERANCE_NT, "East (Y)"),
        () -> assertEquals(vertical, model.getVerticalIntensity(latitude, longitude, year, heightKm), INTENSITY_TOLERANCE_NT, "Vertical (Z)"),
        () -> assertEquals(horizontal, model.getHorizontalIntensity(latitude, longitude, year, heightKm), INTENSITY_TOLERANCE_NT, "Horizontal (H)"),
        () -> assertEquals(total, model.getIntensity(latitude, longitude, year, heightKm), INTENSITY_TOLERANCE_NT, "Total (F)"),
        () -> assertEquals(inclination, model.getDipAngle(latitude, longitude, year, heightKm), ANGLE_TOLERANCE_DEGREES, "Inclination (I)"),
        () -> assertEquals(declination, model.getDeclination(latitude, longitude, year, heightKm), ANGLE_TOLERANCE_DEGREES, "Declination (D)")
    );
  }

  // NOAA's 2027.5, zero-height cases also verify the default date derived from the model's epoch.
  @ParameterizedTest(name = "WMM2025 defaults: latitude={0}, longitude={1}")
  @CsvSource({
      " 80,   0, 55253.9,  83.24,  2.59",
      "  0, 120, 41036.9, -14.65, -0.24",
      "-80, 240, 54474.2, -71.92, 68.49"
  })
  void wmm2025DefaultsMatchNoaaReferenceValues(double latitude, double longitude, double total, double inclination, double declination) {
    Geomagnetics model = new Geomagnetics(GeomagneticCoefficients.WMM2025);

    assertAll(
        () -> assertEquals(total, model.getIntensity(latitude, longitude), INTENSITY_TOLERANCE_NT, "Total (F)"),
        () -> assertEquals(inclination, model.getDipAngle(latitude, longitude), ANGLE_TOLERANCE_DEGREES, "Inclination (I)"),
        () -> assertEquals(declination, model.getDeclination(latitude, longitude), ANGLE_TOLERANCE_DEGREES, "Declination (D)")
    );
  }
}
