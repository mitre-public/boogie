package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("INTEGRATION")
public class TestEmbeddedLidoFile {
  @Test
  void testInstantiation() {
    EmbeddedLidoFile file = EmbeddedLidoFile.instance();
    assertEquals(file, EmbeddedLidoFile.instance(), "Singleton instance should be the same.");

    assertAll(
        () -> assertEquals(12927, EmbeddedLidoFile.instance().arincAirports().size(), "Airports"),
        () -> assertEquals(14352, EmbeddedLidoFile.instance().arincRunways().size(), "Runways"),
        () -> assertEquals(1520, EmbeddedLidoFile.instance().arincLocalizerGlideSlopes().size(), "LocalizerGlideSlopes"),
        () -> assertEquals(61913, EmbeddedLidoFile.instance().arincWaypoints().size(), "Waypoints"),
        () -> assertEquals(448, EmbeddedLidoFile.instance().arincNdbNavaids().size(), "NDB Navaids"),
        () -> assertEquals(1900, EmbeddedLidoFile.instance().arincVhfNavaids().size(), "VHF Navaids"),
        () -> assertEquals(17454, EmbeddedLidoFile.instance().arincAirwayLegs().size(), "Airway Legs"),
        () -> assertEquals(216436, EmbeddedLidoFile.instance().arincProcedureLegs().size(), "Procedure Legs"),
        () -> assertEquals(4445, EmbeddedLidoFile.instance().arincHoldingPatterns().size(), "Holding Patterns"),
        () -> assertEquals(23, EmbeddedLidoFile.instance().arincGnssLandingSystems().size(), "Gnss Landing Systems"),
        () -> assertEquals(2600, EmbeddedLidoFile.instance().arincFirUirLegs().size(), "FIR and UIRs")
    );
  }
}
