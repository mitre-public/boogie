package org.mitre.boogie.xml;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.exi.ExiOptions;
import org.mitre.boogie.xml.model.ArincRecords;

/** Reads the pre-generated CIFP 2101 publication; no source conversion or EXI encoding occurs here. */
@Tag("CIFP")
@Tag("INTEGRATION")
@Tag("XML")
class OneshotCifpExiIntegrationTest {

  @Test
  void loadsCifpModelRecordCounts() throws Exception {
    ArincRecords records;
    try (InputStream input = fixture()) {
      records = OneshotXmlModelParser.standard(ArincXmlVersion.V23_4, ExiOptions.schemaLess()).parseFrom(input);
    }

    assertAll(
        () -> assertEquals(13779, records.airports().size(), "Airports"),
        () -> assertEquals(5992, records.heliports().size(), "Heliports (legacy pads grouped by facility)"),
        () -> assertEquals(29005, records.waypoints().size(), "Enroute waypoints"),
        () -> assertEquals(707, records.ndbNavaids().size(), "Enroute NDB navaids"),
        () -> assertEquals(3006, records.vhfNavaids().size(), "VHF navaids, including separate DME/TACAN records"),
        () -> assertEquals(1678, records.arincAirways().size(), "Airway paths"),
        () -> assertEquals(0, records.holdingPatterns().size(), "No standalone holds in this export"),
        // The saved export has 64 runways without a structured runwayIdentifier, rejected by the validator.
        () -> assertEquals(14244, records.airports().stream().mapToInt(airport -> airport.runways().size()).sum(), "Validated runways"),
        () -> assertEquals(14258, records.airports().stream()
            .mapToInt(airport -> airport.portInfo().procedures().orElse(List.of()).size()).sum(), "Airport procedures"),
        () -> assertEquals(4, records.heliports().stream()
            .mapToInt(heliport -> heliport.portInfo().procedures().orElse(List.of()).size()).sum(), "Heliport procedures"),
        () -> assertEquals(19775, records.arincAirways().stream().mapToInt(airway -> airway.legs().size()).sum(), "Airway legs")
    );
  }

  @Test
  void loadsCifpAssembledRecordCounts() throws Exception {
    try (InputStream input = fixture()) {
      var records = OneshotXmlParser.standardBuilder(ArincXmlVersion.V23_4)
          .exiOptions(ExiOptions.schemaLess())
          .build()
          .assembleFrom(input);

      assertAll(
          () -> assertEquals(13779, records.airports().size(), "Airports"),
          () -> assertEquals(5992, records.heliports().size(), "Heliports"),
          () -> assertEquals(32718, records.fixes().size(), "Enroute fixes"),
          () -> assertEquals(1678, records.airways().size(), "Airway paths"),
          () -> assertEquals(14258, records.procedures().size(), "Airport procedures")
      );
    }
  }

  private static InputStream fixture() {
    InputStream input = OneshotCifpExiIntegrationTest.class.getResourceAsStream("/v23_4/cifp-2101-no-schema.exi");
    assertNotNull(input, "Missing CIFP EXI fixture; run git lfs pull");
    return input;
  }
}
