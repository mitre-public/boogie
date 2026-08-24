package org.mitre.boogie.xml.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.MagneticVariation;

class MagneticVariationConverterTest {

  @Test
  void trueReferenceConvertsToZeroVariation() {
    assertEquals(MagneticVariation.ZERO, MagneticVariationConverter.INSTANCE.apply(MagVar.from("TRUE", 0.0)).orElseThrow());
  }
}
