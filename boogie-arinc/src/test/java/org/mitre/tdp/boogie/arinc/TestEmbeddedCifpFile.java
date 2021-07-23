package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;

class TestEmbeddedCifpFile {

  @Test
  void testInstantiation() {
    EmbeddedCifpFile file = EmbeddedCifpFile.instance();
    assertEquals(file, EmbeddedCifpFile.instance(), "Singleton instance should be the same.");

    Collection<ArincRecord> records = EmbeddedCifpFile.instance().get();
    assertEquals(315936, records.size());

    ConvertingArincRecordConsumer converter = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V19);
    records.forEach(converter);

    assertAll(
        () -> assertEquals(13779, converter.arincAirports().size(), "Airports"),
        () -> assertEquals(14308, converter.arincRunways().size(), "Runways"),
        () -> assertEquals(1309, converter.arincLocalizerGlideSlopes().size(), "LocalizerGlideSlopes"),
        () -> assertEquals(62874, converter.arincWaypoints().size(), "Waypoints"),
        () -> assertEquals(969, converter.arincNdbNavaids().size(), "NDB Navaids"),
        () -> assertEquals(976, converter.arincVhfNavaids().size(), "VHF Navaids"),
        () -> assertEquals(18471, converter.arincAirwayLegs().size(), "Airway Legs"),
        () -> assertEquals(162028, converter.arincProcedureLegs().size(), "Procedure Legs")
    );
  }
}
