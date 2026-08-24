package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.AiracCycle;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidSpec;

class TestFixAssemblyStrategy {

  private static final FixAssemblyStrategy<Fix> STRATEGY = FixAssemblyStrategy.standard();
  private static final double LATITUDE = 42.2124;
  private static final double LONGITUDE = -83.3534;
  private static final String CYCLE = "2608";
  private static final String CYYH_VHF =
      "SCAND        YCB   CY011270VDHB N69070342W105102152YCB N69070342W105102152" +
          "T0000001113  551NARCAMBRIDGE BAY               ND483112605";

  @Test
  void nonzeroStationDeclinationTakesPrecedenceOverModeledVariation() {
    Fix fix = STRATEGY.convertVhfNavaid(vhfNavaid(13.0));

    assertEquals(MagneticVariation.ofDegrees(13.0), fix.magneticVariation().orElseThrow());
  }

  @Test
  void t0000StationDeclinationIsPreservedAsZero() {
    Fix fix = STRATEGY.convertVhfNavaid(vhfNavaid(0.0));

    assertEquals(MagneticVariation.ZERO, fix.magneticVariation().orElseThrow());
  }

  @Test
  void cyyhT0000StationDeclinationRemainsZeroThroughParsingAndAssembly() {
    ArincVhfNavaid navaid = ArincRecordParser.standard(new VhfNavaidSpec())
        .parse(CYYH_VHF)
        .flatMap(new VhfNavaidConverter())
        .orElseThrow();
    Fix fix = STRATEGY.convertVhfNavaid(navaid);

    assertAll(
        () -> assertEquals(0.0, navaid.stationDeclination().orElseThrow()),
        () -> assertEquals(MagneticVariation.ZERO, fix.magneticVariation().orElseThrow())
    );
  }

  @Test
  void modeledVariationIsUsedWhenStationDeclinationIsAbsent() {
    Fix fix = STRATEGY.convertVhfNavaid(vhfNavaid(null));
    MagneticVariation expected = MagneticVariation.from(
        LATITUDE,
        LONGITUDE,
        AiracCycle.startDate(CYCLE)
    );

    assertEquals(expected, fix.magneticVariation().orElseThrow());
  }

  private static ArincVhfNavaid vhfNavaid(Double stationDeclination) {
    return new ArincVhfNavaid.Builder()
        .vhfIdentifier("TEST")
        .vhfIcaoRegion("K6")
        .latitude(LATITUDE)
        .longitude(LONGITUDE)
        .stationDeclination(stationDeclination)
        .fileRecordNumber(1)
        .lastUpdateCycle(CYCLE)
        .build();
  }
}
