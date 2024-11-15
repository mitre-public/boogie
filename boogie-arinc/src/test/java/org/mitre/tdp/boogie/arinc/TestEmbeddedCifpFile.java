package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("INTEGRATION")
class TestEmbeddedCifpFile {

  @Test
  void testInstantiation() {
    EmbeddedCifpFile file = EmbeddedCifpFile.instance();
    assertEquals(file, EmbeddedCifpFile.instance(), "Singleton instance should be the same.");

    assertAll(
        () -> assertEquals(13779, EmbeddedCifpFile.instance().arincAirports().size(), "Airports"),
        () -> assertEquals(14308, EmbeddedCifpFile.instance().arincRunways().size(), "Runways"),
        () -> assertEquals(1309, EmbeddedCifpFile.instance().arincLocalizerGlideSlopes().size(), "LocalizerGlideSlopes"),
        () -> assertEquals(64875, EmbeddedCifpFile.instance().arincWaypoints().size(), "Waypoints"),
        () -> assertEquals(969, EmbeddedCifpFile.instance().arincNdbNavaids().size(), "NDB Navaids"),
        () -> assertEquals(2066, EmbeddedCifpFile.instance().arincVhfNavaids().size(), "VHF Navaids"),
        () -> assertEquals(19775, EmbeddedCifpFile.instance().arincAirwayLegs().size(), "Airway Legs"),
        () -> assertEquals(192312, EmbeddedCifpFile.instance().arincProcedureLegs().size(), "Procedure Legs"),
        () -> assertEquals(0, EmbeddedCifpFile.instance().arincHoldingPatterns().size(), "Holding Patterns"),
        () -> assertEquals(0, EmbeddedCifpFile.instance().arincGnssLandingSystems().size(), "GNSS Landing Systems"),
        () -> assertEquals(0, EmbeddedCifpFile.instance().arincFirUirLegs().size(), "FIR/UIR")
    );
  }
}
