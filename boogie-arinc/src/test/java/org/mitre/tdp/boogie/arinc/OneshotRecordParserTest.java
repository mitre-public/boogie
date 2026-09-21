package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static java.util.stream.Collectors.toMap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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

  @Test
  void testControlledAirspaceCenterIdentificationWithoutResolvedFixes() {
    // Standalone circle boundaries with UF, PA, HA, and unspecified supplier references.
    // No referenced records are supplied: identification must survive independently of fix resolution.
    String input = String.join("\n",
        "SAFRUCDACDAAA UFD  B00010BU   CE                   N35381700W00034440002500360   00984AFL045MALGIERS CTA 2 NORTH WEST      906722605",
        "SAFRUCDAZDAAD PAD  A00010LU   CE                   N35195785E00412211500800360   GND  A02952ABOU SAADA CTR                 923992605",
        "SCANUCCYZCYBN HAE  A00010LU   CE                   N44162000W07954420000500360   GND  A03700MBORDEN CTR                    393492605",
        "SAFRUCFVZFVTL   D  A00010BU   CE                   S19261200E02951390001800360   GND  A06500MGWERU/THORNHILL CTR           720322210"
    );
    var records = OneshotRecordParser.standard(ArincVersion.V19)
        .assembleFrom(new ByteArrayInputStream(input.getBytes(StandardCharsets.US_ASCII)));

    Map<String, CenterIdentification> identifications = records.controlledAirspaces().stream()
        .map(airspace -> airspace.centerIdentification().orElseThrow())
        .collect(toMap(CenterIdentification::identifier, identification -> identification));

    assertAll(
        () -> assertEquals(4, records.controlledAirspaces().size()),
        () -> assertEquals(Map.of(
            "DAAA", centerIdentification("DAAA", "AFR", "DA", BoogieType.AIRSPACE),
            "DAAD", centerIdentification("DAAD", "AFR", "DA", BoogieType.AIRPORT),
            "CYBN", centerIdentification("CYBN", "CAN", "CY", BoogieType.HELIPORT),
            "FVTL", centerIdentification("FVTL", "AFR", "FV", null)
        ), identifications),
        () -> assertTrue(records.controlledAirspaces().stream().allMatch(airspace -> airspace.center().isEmpty())),
        () -> assertTrue(records.controlledAirspaces().stream()
            .allMatch(airspace -> airspace.sequences().size() == 1 && airspace.sequences().get(0).geometry() == Geometry.CIRCLE))
    );
  }

  private CenterIdentification centerIdentification(String identifier, String area, String icaoRegion, BoogieType type) {
    return CenterIdentification.builder(identifier)
        .area(area)
        .icaoRegion(icaoRegion)
        .type(type)
        .build();
  }
}
