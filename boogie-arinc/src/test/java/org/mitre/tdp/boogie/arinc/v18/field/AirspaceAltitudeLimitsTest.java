package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.AltitudeLimits;
import org.mitre.tdp.boogie.AltitudeReference;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.assemble.ControlledAirspaceAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.RestrictiveAirspaceAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;
import org.mitre.tdp.boogie.arinc.model.ArincFirUirLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRestrictiveAirspaceLeg;
import org.mitre.tdp.boogie.arinc.v18.ControlledAirspaceLegConverter;
import org.mitre.tdp.boogie.arinc.v18.ControlledAirspaceLegSpec;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegConverter;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegSpec;
import org.mitre.tdp.boogie.arinc.v19.RestrictiveAirspaceLegConverter;
import org.mitre.tdp.boogie.arinc.v19.RestrictiveAirspaceLegSpec;

class AirspaceAltitudeLimitsTest {

  @Test
  void readsEachLimitInItsReference() {
    assertAll(
        () -> assertEquals(AltitudeLimit.ground(), AirspaceAltitudeLimits.parse("GND  ", AltitudeReference.AGL)),
        () -> assertEquals(AltitudeLimit.msl(3_700.0), AirspaceAltitudeLimits.parse("03700", AltitudeReference.MSL)),
        () -> assertEquals(AltitudeLimit.agl(984.0), AirspaceAltitudeLimits.parse("00984", AltitudeReference.AGL)),
        () -> assertEquals(AltitudeLimit.flightLevel(45), AirspaceAltitudeLimits.parse("FL045", AltitudeReference.MSL), "a flight level is pressure altitude"),
        () -> assertEquals(AltitudeLimit.msl(0.0), AirspaceAltitudeLimits.parse("MSL  ", AltitudeReference.MSL)),
        () -> assertEquals(AltitudeLimit.msl(-200.0), AirspaceAltitudeLimits.parse("-0200", AltitudeReference.MSL)),
        () -> assertEquals(AltitudeLimit.none(), AirspaceAltitudeLimits.parse("UNLTD")),
        () -> assertEquals(AltitudeLimit.byNotam(), AirspaceAltitudeLimits.parse("NOTAM")));
  }

  @Test
  void limitsTheSourceDoesNotPlaceAreNotSpecified() {
    assertAll(
        () -> assertEquals(AltitudeLimit.notSpecified(), AirspaceAltitudeLimits.parse("NOTSP")),
        () -> assertEquals(AltitudeLimit.notSpecified(), AirspaceAltitudeLimits.parse("05000"), "a number without a unit"),
        () -> assertEquals(AltitudeLimit.notSpecified(), AirspaceAltitudeLimits.parse("5X000", AltitudeReference.MSL), "unreadable"));
  }

  @Test
  void unitIndicatorsNameTheirReference() {
    assertAll(
        () -> assertEquals(Optional.of(AltitudeReference.AGL), AirspaceAltitudeLimits.reference("A")),
        () -> assertEquals(Optional.of(AltitudeReference.MSL), AirspaceAltitudeLimits.reference("M")),
        () -> assertEquals(Optional.empty(), AirspaceAltitudeLimits.reference(" ")));
  }

  @Test
  void convertedLegsCarryTheirLimits() {
    assertAll(
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.ground(), AltitudeLimit.msl(3_700.0)),
            controlled("SCANUCCYZCYBN HAE  A00010LU   CE                   N44162000W07954420000500360   GND  A03700MBORDEN CTR                    393492605").altitudeLimits()),
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.agl(984.0), AltitudeLimit.flightLevel(45)),
            controlled("SAFRUCDACDAAA UFD  B00010BU   CE                   N35381700W00034440002500360   00984AFL045MALGIERS CTA 2 NORTH WEST      906722605").altitudeLimits()),
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.flightLevel(245), AltitudeLimit.flightLevel(660)),
            controlled("SSPAUCAGCAGGG UFA  A00010H    H S04500000E159000000                              FL245MFL660MHONIARA CTA                   794722210").altitudeLimits()),
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.msl(4_000.0), AltitudeLimit.msl(17_999.0)),
            restrictive("SUSAURK1NBOARDMAN  A00101L    G N45525900W119310400                              04000M17999MBOARDMAN MOA                  714171703").altitudeLimits()));

    ArincFirUirLeg firUir = firUir("SAFRUFDRRRZRZXB10950DNKK    12N G N13342821E006270449                           FL245FL245UNLTDRR NIAMEY FIR/UIR           923192313");
    assertAll(
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.none(), AltitudeLimit.flightLevel(245)), firUir.firAltitudeLimits()),
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.flightLevel(245), AltitudeLimit.none()), firUir.uirAltitudeLimits()),
        () -> assertTrue(firUir.firAltitudeLimits().known()));
  }

  @Test
  void assembledAirspacesCarryTheirLimits() {
    ArincControlledAirspaceLeg borden = controlled("SCANUCCYZCYBN HAE  A00010LU   CE                   N44162000W07954420000500360   GND  A03700MBORDEN CTR                    393492605");
    ArincRestrictiveAirspaceLeg boardman = restrictive("SUSAURK1NBOARDMAN  A00101L    G N45525900W119310400                              04000M17999MBOARDMAN MOA                  714171703");
    Airspace controlledAirspace = ControlledAirspaceAssemblyStrategy.standard().convertControlledAirspace(borden, null, List.of());
    Airspace restrictiveAirspace = RestrictiveAirspaceAssemblyStrategy.standard().convertRestrictiveAirspace(boardman, List.of());
    assertAll(
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.ground(), AltitudeLimit.msl(3_700.0)), controlledAirspace.altitudeLimits()),
        () -> assertEquals(AltitudeLimits.of(AltitudeLimit.msl(4_000.0), AltitudeLimit.msl(17_999.0)), restrictiveAirspace.altitudeLimits()));
  }

  private static ArincControlledAirspaceLeg controlled(String line) {
    return convert(new ControlledAirspaceLegSpec(), new ControlledAirspaceLegConverter(), line);
  }

  private static ArincRestrictiveAirspaceLeg restrictive(String line) {
    return convert(new RestrictiveAirspaceLegSpec(), new RestrictiveAirspaceLegConverter(), line);
  }

  private static ArincFirUirLeg firUir(String line) {
    return convert(new FirUirLegSpec(), new FirUirLegConverter(), line);
  }

  private static <T> T convert(org.mitre.tdp.boogie.arinc.RecordSpec spec, Function<ArincRecord, Optional<T>> converter, String line) {
    ArincRecord record = ArincRecordParser.standard(spec).parse(line).orElseThrow(() -> new AssertionError("Unparsed: " + line));
    return converter.apply(record).orElseThrow(() -> new AssertionError("Unconverted: " + line));
  }
}
