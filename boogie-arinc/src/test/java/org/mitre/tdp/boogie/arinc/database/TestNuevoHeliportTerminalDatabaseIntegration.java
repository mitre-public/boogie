package org.mitre.tdp.boogie.arinc.database;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.EmbeddedCifpFile;
import org.mitre.tdp.boogie.arinc.EmbeddedLidoFile;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;

@Tag("LIDO")
@Tag("INTEGRATION")
public class TestNuevoHeliportTerminalDatabaseIntegration {
  private static ArincTerminalAreaDatabase arincTerminalAreaDatabase;

  @BeforeAll
  static void setup() {
    arincTerminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        EmbeddedLidoFile.instance().arincAirports(),
        EmbeddedLidoFile.instance().arincRunways(),
        EmbeddedLidoFile.instance().arincLocalizerGlideSlopes(),
        EmbeddedLidoFile.instance().arincNdbNavaids(),
        EmbeddedLidoFile.instance().arincVhfNavaids(),
        EmbeddedLidoFile.instance().arincWaypoints(),
        EmbeddedLidoFile.instance().arincProcedureLegs(),
        EmbeddedLidoFile.instance().arincGnssLandingSystems(),
        EmbeddedLidoFile.instance().arincHelipads(),
        EmbeddedLidoFile.instance().arincHeliports()
    );
  }

  @Test
  void testLocalizersExistForRunwayReferences() {

    List<ArincRunway> runwaysWithPrimaryIlsMlsGlsReference = EmbeddedCifpFile.instance().arincRunways().stream()
        .filter(arincRunway -> arincRunway.ilsMlsGlsIdentifier().isPresent())
        .toList();

    List<ArincRunway> runwaysWithMatchingPrimaryIlsMlsGlsReference = runwaysWithPrimaryIlsMlsGlsReference.stream()
        .filter(arincRunway -> arincTerminalAreaDatabase.primaryLocalizerGlideSlopeOf(arincRunway.airportIdentifier(), arincRunway.runwayIdentifier()).isPresent())
        .toList();

    int missingPrimaryReferences = runwaysWithPrimaryIlsMlsGlsReference.size() - runwaysWithMatchingPrimaryIlsMlsGlsReference.size();

    List<ArincRunway> runwaysWithSecondaryIlsMlsGlsReference = EmbeddedCifpFile.instance().arincRunways().stream()
        .filter(arincRunway -> arincRunway.secondaryIlsMlsGlsIdentifier().isPresent())
        .toList();

    List<ArincRunway> runwaysWithMatchingSecondaryIlsMlsGlsReference = runwaysWithSecondaryIlsMlsGlsReference.stream()
        .filter(arincRunway -> arincTerminalAreaDatabase.secondaryLocalizerGlideSlopeOf(arincRunway.airportIdentifier(), arincRunway.runwayIdentifier()).isPresent())
        .toList();

    int missingSecondaryReferences = runwaysWithSecondaryIlsMlsGlsReference.size() - runwaysWithMatchingSecondaryIlsMlsGlsReference.size();

    assertAll(
        "Check all ILS/MLS/GLS's referenced by runways exist in the database.",
        () -> assertEquals(0, missingPrimaryReferences, "All runways referencing an ILS as their primary should have a matching ILS record."),
        () -> assertEquals(0, missingSecondaryReferences, "All runways referencing an ILS as their secondary should have a matching ILS record.")
    );
  }
}
