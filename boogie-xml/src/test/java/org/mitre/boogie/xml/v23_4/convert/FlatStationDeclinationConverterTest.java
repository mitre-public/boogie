package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.StationDeclination;
import org.mitre.tdp.boogie.MagneticVariation;

class FlatStationDeclinationConverterTest {
  private final FlatStationDeclinationConverter converter = FlatStationDeclinationConverter.INSTANCE;

  @Test
  void nullReturnsNull() {
    assertNull(converter.apply(null));
  }

  @Test
  void convertsWestStationDeclination() {
    StationDeclination sd = new StationDeclination();
    sd.setStationDeclinationEWT(org.mitre.boogie.xml.v23_4.generated.StationDeclinationEWT.WEST);
    sd.setStationDeclinationValue(BigDecimal.valueOf(3.5));

    MagneticVariation result = converter.apply(sd);
    assertEquals(MagneticVariation.ofDegrees(3.5), result);
  }

  @Test
  void convertsEastStationDeclination() {
    StationDeclination sd = new StationDeclination();
    sd.setStationDeclinationEWT(org.mitre.boogie.xml.v23_4.generated.StationDeclinationEWT.EAST);
    sd.setStationDeclinationValue(BigDecimal.valueOf(5.0));

    MagneticVariation result = converter.apply(sd);
    assertEquals(MagneticVariation.ofDegrees(-5.0), result);
  }

  @Test
  void trueStationDeclinationReturnsNull() {
    StationDeclination sd = new StationDeclination();
    sd.setStationDeclinationEWT(org.mitre.boogie.xml.v23_4.generated.StationDeclinationEWT.TRUE);
    sd.setStationDeclinationValue(BigDecimal.valueOf(0.0));

    assertNull(converter.apply(sd));
  }
}
