package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("LIDO")
@Tag("INTEGRATION")
public class TestEmbeddedLidoFile {
  @Test
  void testInstantiation() {
    EmbeddedLidoFile file = EmbeddedLidoFile.instance();
    assertEquals(file, EmbeddedLidoFile.instance(), "Singleton instance should be the same.");
    assertAll(
        () -> assertEquals(26960, EmbeddedLidoFile.instance().arincAirports().size(), "Airports"),
        () -> assertEquals(34124, EmbeddedLidoFile.instance().arincRunways().size(), "Runways"), //it went down now that we want headings
        () -> assertEquals(4426, EmbeddedLidoFile.instance().arincLocalizerGlideSlopes().size(), "LocalizerGlideSlopes"),
        () -> assertEquals(258178, EmbeddedLidoFile.instance().arincWaypoints().size(), "Waypoints"),
        () -> assertEquals(3854, EmbeddedLidoFile.instance().arincNdbNavaids().size(), "NDB Navaids"),
        () -> assertEquals(8361, EmbeddedLidoFile.instance().arincVhfNavaids().size(), "VHF Navaids"),
        () -> assertEquals(109403, EmbeddedLidoFile.instance().arincAirwayLegs().size(), "Airway Legs"),
        () -> assertEquals(861925, EmbeddedLidoFile.instance().arincProcedureLegs().size(), "Procedure Legs"),
        () -> assertEquals(31656, EmbeddedLidoFile.instance().arincHoldingPatterns().size(), "Holding Patterns"),
        () -> assertEquals(315, EmbeddedLidoFile.instance().arincGnssLandingSystems().size(), "Gnss Landing Systems"),
        () -> assertEquals(308661, EmbeddedLidoFile.instance().arincFirUirLegs().size(), "FIR and UIR Legs"),
        () -> assertEquals(11908, EmbeddedLidoFile.instance().arincHelipads().size(), "Helipads, lots now that we do the heliports also"),
        () -> assertEquals(585050, EmbeddedLidoFile.instance().arincControlledAirspaceLegs().size(), "Controlled Airspace Legs"),
        () -> assertEquals(9646, EmbeddedLidoFile.instance().arincHeliports().size(), "Heliports")
    );
  }
}
