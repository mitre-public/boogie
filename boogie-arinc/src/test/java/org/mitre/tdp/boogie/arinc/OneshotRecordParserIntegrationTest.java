package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.util.DemotedException;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.arinc.assemble.AirportAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.AirwayAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.FixAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.ProcedureAssemblyStrategy;

@Tag("INTEGRATION")
class OneshotRecordParserIntegrationTest {

  @Test
  void testParse() {

    OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace> records;

    try (InputStream is = EmbeddedCifpFile.getInputStream()) {
      records = OneshotRecordParser.standard(ArincVersion.V19).assembleFrom(is);
    } catch (IOException e) {
      throw DemotedException.demote("Exception parsing embedded CIFP file.", e);
    }

    assertAll(
        () -> assertEquals(13779, records.airports().size(), "Airports"),
        () -> assertEquals(67910, records.fixes().size(), "Fixes"),
        () -> assertEquals(1550, records.airways().size(), "Airways"),
        () -> assertEquals(14258, records.procedures().size(), "Procedures"),
        () -> assertEquals(0, records.firUirs().size(), "FIR-UIRs")
    );
  }
}