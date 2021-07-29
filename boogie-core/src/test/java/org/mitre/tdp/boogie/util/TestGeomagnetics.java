package org.mitre.tdp.boogie.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class TestGeomagnetics {

  @ParameterizedTest
  @EnumSource(GeomagneticCoefficients.class)
  void CoefficientConstructor_AllPossibleValues_CanInstantiate(GeomagneticCoefficients coeff) {
    new Geomagnetics(coeff);
  }
}
