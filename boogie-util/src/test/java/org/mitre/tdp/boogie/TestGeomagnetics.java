package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class TestGeomagnetics {

  @ParameterizedTest
  @EnumSource(GeomagneticCoefficients.class)
  void CoefficientConstructor_AllPossibleValues_CanInstantiate(GeomagneticCoefficients coeff) {
    assertDoesNotThrow(() -> new Geomagnetics(coeff), "All coefficient instantiations should work.");
  }
}
