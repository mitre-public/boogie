package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.model.ArincAirportPrimaryExtension;
import org.mitre.tdp.boogie.arinc.v18.field.ApplicationType;

class TestAirportPrimaryExtensionConverterTest {
  private static final String RAW = "SUSAP K1A0K7A        3E1A0                                                                                                 007132003";
  private static final AirportPrimaryExtensionConverter converter = new AirportPrimaryExtensionConverter();
  @Test
  void testConvert() {
    ArincRecord record = ArincVersion.V19.parser().parse(RAW).orElseThrow(AssertionError::new);
    ArincAirportPrimaryExtension converted = converter.apply(record).get();
    ArincAirportPrimaryExtension rebuilt = converted.toBuilder().build();
    assertAll(
        () -> assertEquals("2003", converted.lastUpdateCycle()),
        () -> assertEquals("K1A0", converted.airportIdentifier()),
        () -> assertEquals("K7", converted.airportIcaoRegion()),
        () -> assertEquals(ApplicationType.E, converted.applicationType().get()),
        () -> assertEquals("1A0", converted.notes()),
        () -> assertEquals(converted, rebuilt)
    );
  }
}