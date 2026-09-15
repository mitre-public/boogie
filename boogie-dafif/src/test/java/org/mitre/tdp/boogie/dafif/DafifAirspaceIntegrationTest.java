package org.mitre.tdp.boogie.dafif;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.dafif.assemble.BoundaryAssembler;
import org.mitre.tdp.boogie.dafif.assemble.SuasAssembler;
import org.mitre.tdp.boogie.dafif.model.ConvertingDafifRecordConsumer;
import org.mitre.tdp.boogie.dafif.model.DafifRecordConverterFactory;

import com.google.common.collect.Range;
import com.google.common.io.Resources;

@Tag("DAFIF")
@Tag("INTEGRATION")
class DafifAirspaceIntegrationTest {

  private static final Set<String> TABLES = Set.of(
      "DAFIF8_1/DAFIFT/BDRY/BDRY_PAR.TXT",
      "DAFIF8_1/DAFIFT/BDRY/BDRY.TXT",
      "DAFIF8_1/DAFIFT/SUAS/SUAS_PAR.TXT",
      "DAFIF8_1/DAFIFT/SUAS/SUAS.TXT");

  private static final Map<String, Long> PARSED_COUNTS = new HashMap<>();
  private static ConvertingDafifRecordConsumer records;
  private static List<Airspace> boundaries;
  private static List<Airspace> specialUseAirspaces;

  @BeforeAll
  static void setUp() throws IOException {
    DafifFileParser parser = new DafifFileParser(DafifVersion.V81);
    records = DafifRecordConverterFactory.consumerForVersion(DafifVersion.V81);
    try (InputStream input = Resources.getResource("DAFIF8_1_2601.zip").openStream();
         ZipInputStream zip = new ZipInputStream(input)) {
      ZipEntry entry;
      while ((entry = zip.getNextEntry()) != null) {
        if (!TABLES.contains(entry.getName())) {
          continue;
        }
        String filename = entry.getName().substring(entry.getName().lastIndexOf('/') + 1);
        InputStream table = new FilterInputStream(zip) {
          @Override
          public void close() {
            // Keep the zip open for the remaining tables.
          }
        };
        try (Stream<DafifRecord> parsed = parser.stream(table, filename)) {
          long count = parsed.mapToLong(record -> {
            records.accept(record);
            return 1L;
          }).sum();
          PARSED_COUNTS.put(filename, count);
        }
      }
    }
    boundaries = BoundaryAssembler.standard()
        .assemble(records.dafifBoundaryParents(), records.dafifBoundarySegments()).toList();
    specialUseAirspaces = SuasAssembler.standard()
        .assemble(records.dafifSuasParents(), records.dafifSuasSegments()).toList();
  }

  @Test
  void testAllFourTablesParseAndConvertWithoutRecordLoss() {
    assertAll(
        () -> assertEquals(Map.of(
            "BDRY_PAR.TXT", 17984L, "BDRY.TXT", 227101L,
            "SUAS_PAR.TXT", 18118L, "SUAS.TXT", 104791L), PARSED_COUNTS),
        () -> assertEquals(17984, records.dafifBoundaryParents().size()),
        () -> assertEquals(227101, records.dafifBoundarySegments().size()),
        () -> assertEquals(18118, records.dafifSuasParents().size()),
        () -> assertEquals(104791, records.dafifSuasSegments().size())
    );
  }

  @Test
  void testAssembledCounts() {
    assertAll(
        () -> assertEquals(17956, boundaries.size(), "Exclude 26 annuli and two open boundary lines"),
        () -> assertEquals(18111, specialUseAirspaces.size(), "Exclude four annuli and three point airspaces")
    );
  }

  @Test
  void testFirAndUirTypesAndAltitudeLimits() {
    Airspace fir = find(boundaries, "AG00004");
    Airspace uir = find(boundaries, "DI00002");
    assertAll(
        () -> assertEquals(AirspaceType.FIR, fir.airspaceType()),
        () -> assertEquals("AGGG", fir.area()),
        () -> assertEquals(Range.closed(24500.0, 60000.0), fir.altitudeLimit()),
        () -> assertEquals(14, fir.sequences().size(), "Starting anchor plus 13 source segments"),
        () -> assertEquals(AirspaceType.UIR, uir.airspaceType()),
        () -> assertEquals("DIII", uir.area()),
        () -> assertEquals(Range.atLeast(24500.0), uir.altitudeLimit()),
        () -> assertEquals(31, uir.sequences().size(), "Starting anchor plus 30 source segments")
    );
  }

  @Test
  void testUnnamedSpecialUseCircle() {
    Airspace airspace = find(specialUseAirspaces, "A211 (XA)");
    assertEquals(1, airspace.sequences().size());
    AirspaceSequence circle = airspace.sequences().get(0);
    assertAll(
        () -> assertEquals(AirspaceType.RESTRICTIVE, airspace.airspaceType()),
        () -> assertEquals("KOZR", airspace.area()),
        () -> assertEquals(Range.atMost(2800.0), airspace.altitudeLimit()),
        () -> assertEquals(Geometry.CIRCLE, circle.geometry()),
        () -> assertEquals(LatLong.of(31.308333, -85.705556), circle.centerFix().orElseThrow()),
        () -> assertEquals(5.0, circle.arcRadius().orElseThrow())
    );
  }

  @Test
  void testSpecialUseSectorsRemainSeparate() {
    Airspace first = find(specialUseAirspaces, "CYR107/1");
    Airspace second = find(specialUseAirspaces, "CYR107/2");
    assertAll(
        () -> assertEquals(Range.atMost(1000.0), first.altitudeLimit()),
        () -> assertEquals(Range.closed(1001.0, 13500.0), second.altitudeLimit()),
        () -> assertEquals(6, first.sequences().size(), "Starting anchor plus five source segments"),
        () -> assertEquals(6, second.sequences().size(), "Starting anchor plus five source segments")
    );
  }

  @Test
  void testUnsupportedGeometryIsRetainedInParsedRecordsAndOmittedFromAirspaces() {
    assertAll(
        () -> assertTrue(records.dafifBoundaryParents().stream()
            .anyMatch(parent -> parent.boundaryIdentification().equals("CZ00042"))),
        () -> assertTrue(records.dafifSuasParents().stream()
            .anyMatch(parent -> parent.suasIdentification().equals("R7201A"))),
        () -> assertTrue(records.dafifSuasParents().stream()
            .anyMatch(parent -> parent.suasIdentification().equals("LER12"))),
        () -> assertFalse(boundaries.stream().anyMatch(airspace ->
            Set.of("CZ00042", "ED01990", "PA09999").contains(airspace.identifier()))),
        () -> assertFalse(specialUseAirspaces.stream().anyMatch(airspace ->
            Set.of("R7201A", "LER12", "LFP91M", "LFP92M").contains(airspace.identifier())))
    );
  }

  private static Airspace find(List<Airspace> airspaces, String identifier) {
    return airspaces.stream().filter(airspace -> airspace.identifier().equals(identifier)).findFirst().orElseThrow();
  }
}
