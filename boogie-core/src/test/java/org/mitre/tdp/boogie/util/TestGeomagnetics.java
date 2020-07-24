package org.mitre.tdp.boogie.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class TestGeomagnetics {

  @ParameterizedTest
  @EnumSource(GeomagneticCoefficients.class)
  public void CoefficientConstructor_AllPossibleValues_CanInstantiate(GeomagneticCoefficients coeff) {
    new Geomagnetics(coeff);
  }

}
