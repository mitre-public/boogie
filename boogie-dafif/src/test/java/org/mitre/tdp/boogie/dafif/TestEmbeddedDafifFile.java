package org.mitre.tdp.boogie.dafif;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("DAFIF")
@Tag("INTEGRATION")
class TestEmbeddedDafifFile {

  @Test
  void testInstantiation() {
    EmbeddedDafifFile file = EmbeddedDafifFile.instance();
    assertEquals(file, EmbeddedDafifFile.instance(), "Singleton instance should be the same.");

    assertAll(
        () -> assertEquals(10011, EmbeddedDafifFile.instance().dafifAirports().size(), "Airports"),
        () -> assertEquals(13652, EmbeddedDafifFile.instance().dafifRunways().size(), "Runways"),
        () -> assertEquals(5358, EmbeddedDafifFile.instance().dafifAddRunways().size(), "Additional Runways"),
        () -> assertEquals(13262, EmbeddedDafifFile.instance().dafifIls().size(), "ILS"),
        () -> assertEquals(8070, EmbeddedDafifFile.instance().dafifNavaids().size(), "Navaids"),
        () -> assertEquals(144997, EmbeddedDafifFile.instance().dafifWaypoints().size(), "Waypoints"),
        () -> assertEquals(31932, EmbeddedDafifFile.instance().dafifTerminalParents().size(), "Terminal Parents"),
        () -> assertEquals(349430, EmbeddedDafifFile.instance().dafifTerminalSegments().size(), "Terminal Segments")
    );
  }
}
