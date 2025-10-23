package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.util.DemotedException;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;

@Tag("LIDO")
@Tag("INTEGRATION")
public class OneShotRecordParserLidoIntegrationTest {
  @Test
  void testParseLido() {
    OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace> records;

    try (InputStream is = EmbeddedLidoFile.getInputStream()) {
      records = OneshotRecordParser.standard(ArincVersion.V22).assembleFrom(is);
    } catch (IOException e) {
      throw DemotedException.demote("Exception parsing embedded LIDO file.", e);
    }

    assertAll(
        () -> assertEquals(26960, records.airports().size(), "Airports"),
        () -> assertEquals(269229, records.fixes().size(), "Fixes"),
        () -> assertEquals(14588, records.airways().size(), "Airways"),
        () -> assertEquals(100716, records.procedures().size(), "Procedures"),
        () -> assertEquals(357, records.firUirs().size(), "FIRs and UIRs"),
        () -> assertEquals(11761, records.conrolledAirspaces().size(), "Controlled Airspaces"),
        () -> assertEquals("A424-22std.dat", records.headerOne().get().fileName().get())
    );
  }
}
