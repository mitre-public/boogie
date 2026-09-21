package org.mitre.boogie.xml;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.util.DemotedException;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Runway;

class OneshotXmlParserTest {

  private static final File xmlTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/v23_4/parser-sample.xml"));

  @Test
  void testParse() {
    OneshotXmlParser.ClientRecords<Airport, Runway, Fix, Airway, Procedure, Helipad, Heliport> records;

    try (FileInputStream fis = new FileInputStream(xmlTestFile)) {
      records = OneshotXmlParser.standard(ArincXmlVersion.V23_4).assembleFrom(fis);
    } catch (IOException e) {
      throw DemotedException.demote("Exception opening and parsing XML file: " + xmlTestFile, e);
    }

    assertAll(
        () -> assertEquals(1, records.airports().size(), "Airports"),
        () -> assertEquals(4, records.fixes().size(), "Fixes"),
        () -> assertEquals(1, records.airways().size(), "Airways"),
        () -> assertEquals(1, records.procedures().size(), "Procedures"),
        () -> assertEquals(1, records.heliports().size(), "Heliports"),
        () -> assertNotNull(records.fixDatabase()),
        () -> assertNotNull(records.terminalAreaDatabase())
    );

    Airport airport = records.airports().iterator().next();
    Heliport heliport = records.heliports().iterator().next();
    Procedure procedure = records.procedures().iterator().next();
    var procedureLegs = procedure.transitions().iterator().next().legs();
    var airway = records.airways().iterator().next();
    assertAll(
        () -> assertEquals("KTST", airport.airportIdentifier()),
        () -> assertEquals(LatLong.of(38.9, -77.0), airport.latLong()),
        () -> assertEquals("RW09", airport.runways().iterator().next().runwayIdentifier()),
        () -> assertEquals("HTST", heliport.heliportIdentifier()),
        () -> assertEquals("H1", heliport.helipads().iterator().next().padIdentifier()),
        () -> assertEquals("TEST1", procedure.procedureIdentifier()),
        () -> assertEquals("KTST", procedure.airportIdentifier()),
        () -> assertEquals(List.of("ALPHA", "BRAVO"), procedureLegs.stream()
            .map(leg -> leg.associatedFix().orElseThrow().fixIdentifier()).toList()),
        () -> assertEquals("TST", procedureLegs.get(1).recommendedNavaid().orElseThrow().fixIdentifier()),
        () -> assertEquals("V1", airway.airwayIdentifier()),
        () -> assertEquals(List.of("ALPHA", "BRAVO"), airway.legs().stream()
            .map(leg -> leg.associatedFix().orElseThrow().fixIdentifier()).toList())
    );
  }
}
