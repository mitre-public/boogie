package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.util.DemotedException;
import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.arinc.assemble.*;

class OneshotRecordParserTest {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));
  private static final String UMGOS = "SSAMEAENRT   UMGOS SK1    R  RH N03190000W080465800                       W0023     WGE        P  UMGOS                    209142003";
  private static final String REDSX = "SUSAP KFMHK6CREDSX K61    RIF   N41552146W070140577                       W0154     NAR        P  REDSX                    520911902";

  @Test
  void testParse() {

    OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace, Heliport> records;

    try (FileInputStream fis = new FileInputStream(arincTestFile)) {
      records = OneshotRecordParser.standard(ArincVersion.V19).assembleFrom(fis);
    } catch (IOException e) {
      throw DemotedException.demote("Exception opening and parsing 424 file: " + arincTestFile, e);
    }

    assertAll(
        () -> assertEquals(358, records.airports().size(), "Airports"),
        () -> assertEquals(1301, records.fixes().size(), "Fixes"),
        () -> assertEquals(204, records.airways().size(), "Airways"),
        () -> assertEquals(1438, records.procedures().size(), "Procedures"),
        () -> assertEquals(1, records.firUirs().size(), "FIR-UIRs"),
        () -> assertEquals(14, records.restrictiveAirspaces().size(), "Restrictive Airspaces"),
        () -> assertEquals(273, records.heliports().size(), "Heliports")
    );
  }

  @Test
  void testKeepRecord() {
    String input = String.join("\n", UMGOS, REDSX);
    var parser = OneshotRecordParser
        .<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Airspace, AirspaceSequence, Helipad, Heliport>builder(
            ArincVersion.V19)
        .keepRecord(record -> record.<String>optionalField("waypointIdentifier").filter("UMGOS"::equals).isPresent())
        .airportStrategy(AirportAssemblyStrategy.standard())
        .fixStrategy(FixAssemblyStrategy.standard())
        .airwayStrategy(AirwayAssemblyStrategy.standard())
        .procedureStrategy(ProcedureAssemblyStrategy.standard())
        .firUirStrategy(FirUirAssemblyStrategy.standard())
        .controlledAirspaceStrategy(ControlledAirspaceAssemblyStrategy.standard())
        .restrictiveAirspaceStrategy(RestrictiveAirspaceAssemblyStrategy.standard())
        .heliportAssemblyStrategy(HeliportAssemblyStrategy.standard())
        .build();

    var records = parser.assembleFrom(new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII)));

    assertEquals(List.of("UMGOS"), records.fixes().stream().map(Fix::fixIdentifier).toList());
  }
}
