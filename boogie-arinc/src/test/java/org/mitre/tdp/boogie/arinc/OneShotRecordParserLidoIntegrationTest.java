package org.mitre.tdp.boogie.arinc;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.util.DemotedException;
import org.mitre.tdp.boogie.*;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("LIDO")
@Tag("INTEGRATION")
public class OneShotRecordParserLidoIntegrationTest {
  @Test
  void testParseLido() {
    OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace, Heliport> records;

    try (InputStream is = EmbeddedLidoFile.getInputStream()) {
      records = OneshotRecordParser.standard(ArincVersion.V22).assembleFrom(is);
    } catch (IOException e) {
      throw DemotedException.demote("Exception parsing embedded LIDO file.", e);
    }

    int airportRunwayCount = records.airports().stream()
        .mapToInt(airport -> airport.runways().size())
        .sum();
    int heliportRunwayCount = records.heliports().stream()
        .mapToInt(heliport -> heliport.runways().size())
        .sum();
    int procedureLegCount = records.procedures().stream()
        .flatMap(procedure -> procedure.transitions().stream())
        .mapToInt(transition -> transition.legs().size())
        .sum();
    long procedureTransitionCount = records.procedures().stream()
        .mapToLong(procedure -> procedure.transitions().size())
        .sum();
    long oibbN13lMissedApproaches = records.procedures().stream()
        .filter(procedure -> "OIBB".equals(procedure.airportIdentifier()))
        .filter(procedure -> "N13L".equals(procedure.procedureIdentifier()))
        .flatMap(procedure -> procedure.transitions().stream())
        .filter(transition -> TransitionType.MISSED.equals(transition.transitionType()))
        .count();

    assertAll(
        () -> assertEquals(26960, records.airports().size(), "Airports"),
        () -> assertEquals(34092, airportRunwayCount, "Airport Runways"),
        () -> assertEquals(32, heliportRunwayCount, "Heliport Runways"),
        () -> assertEquals(34124, airportRunwayCount + heliportRunwayCount, "Runways"),
        () -> assertEquals(270393, records.fixes().size(), "Fixes"),
        () -> assertEquals(14588, records.airways().size(), "Airways"),
        () -> assertEquals(101085, records.procedures().size(), "Procedures"),
        () -> assertEquals(245853, procedureTransitionCount, "Procedure Transitions"),
        () -> assertEquals(861925, procedureLegCount, "Procedure Legs"),
        () -> assertEquals(2, oibbN13lMissedApproaches, "Multiple missed approaches"),
        () -> assertEquals(357, records.firUirs().size(), "FIRs and UIRs"),
        () -> assertEquals(13232, records.controlledAirspaces().size(), "Controlled Airspaces"),
        () -> assertEquals(20503, records.restrictiveAirspaces().size(), "Restrictive Airspaces"),
        () -> assertEquals("A424-22std.dat", records.headerOne().orElseThrow().fileName().orElseThrow()),
        () -> assertEquals(9646, records.heliports().size(), "heliports")
    );
  }
}
