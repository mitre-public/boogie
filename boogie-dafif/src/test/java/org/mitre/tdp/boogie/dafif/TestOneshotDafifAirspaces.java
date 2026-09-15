package org.mitre.tdp.boogie.dafif;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.dafif.assemble.AirportAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.assemble.AirwayAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.assemble.BoundaryAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.assemble.FixAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.assemble.ProcedureAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.assemble.SuasAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.model.DafifBoundaryParent;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;
import org.mitre.tdp.boogie.dafif.model.DafifSuasParent;
import org.mitre.tdp.boogie.dafif.model.DafifSuasSegment;
import org.mitre.tdp.boogie.dafif.model.enums.Shape;

import com.google.common.collect.Range;

class TestOneshotDafifAirspaces {

  @Test
  void assemblesAirspacesWhenSegmentsPrecedeParentsInZip() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try (ZipOutputStream zip = new ZipOutputStream(bytes)) {
      entry(zip, "BDRY/BDRY.TXT", boundaryCircle());
      entry(zip, "SUAS/SUAS.TXT", suasCircle("", "5.00"), suasCircle("A", "10.00"));
      entry(zip, "BDRY/BDRY_PAR.TXT", boundaryParent());
      entry(zip, "SUAS/SUAS_PAR.TXT", suasParent("", "010000AMSL"), suasParent("A", "FL200"));
    }

    var records = OneshotDafifParser.standard(DafifVersion.V81)
        .assembleFrom(new ByteArrayInputStream(bytes.toByteArray()));
    Airspace boundary = records.boundaries().iterator().next();
    List<Airspace> suas = List.copyOf(records.specialUseAirspaces());
    assertAll(
        () -> assertEquals(1, records.boundaries().size()),
        () -> assertEquals(2, suas.size()),
        () -> assertEquals(3, records.airspaces().size()),
        () -> assertEquals("US00001", boundary.identifier()),
        () -> assertEquals(AirspaceType.FIR, boundary.airspaceType()),
        () -> assertEquals(Range.closed(18000.0, 60000.0), boundary.altitudeLimit()),
        () -> assertEquals(Geometry.CIRCLE, boundary.sequences().get(0).geometry()),
        () -> assertEquals(LatLong.of(40.0, -75.0), boundary.sequences().get(0).centerFix().orElseThrow()),
        () -> assertEquals(2, suas.stream().map(Airspace::identifier).distinct().count()),
        () -> assertTrue(suas.stream().allMatch(a -> a.airspaceType() == AirspaceType.RESTRICTIVE)),
        () -> assertEquals(List.of(10000.0, 20000.0), suas.stream().map(a -> a.altitudeLimit().upperEndpoint()).sorted().toList()),
        () -> assertTrue(records.airports().isEmpty()),
        () -> assertTrue(records.procedures().isEmpty())
    );
  }

  @Test
  void customAirspaceAndSequenceTypesRetainOrderedSourceGeometryAndSectors() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try (ZipOutputStream zip = new ZipOutputStream(bytes)) {
      entry(zip, "BDRY/BDRY.TXT", boundaryPoint("20"), boundaryPoint("10"));
      entry(zip, "SUAS/SUAS.TXT", suasPoint("A", "30"), suasCircle("", "5.00", "2.00"), suasPoint("A", "10"));
      entry(zip, "BDRY/BDRY_PAR.TXT", boundaryParent());
      entry(zip, "SUAS/SUAS_PAR.TXT", suasParent("", "010000AMSL"), suasParent("A", "FL200"));
    }

    var strategy = new ClientAirspaceStrategy();
    OneshotDafifParser<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, ClientAirspace, ClientSequence> parser =
        OneshotDafifParser.<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, ClientAirspace, ClientSequence>builder(DafifVersion.V81)
            .airportStrategy(AirportAssemblyStrategy.standard())
            .fixStrategy(FixAssemblyStrategy.standard())
            .airwayStrategy(AirwayAssemblyStrategy.standard())
            .procedureStrategy(ProcedureAssemblyStrategy.standard())
            .boundaryStrategy(strategy)
            .suasStrategy(strategy)
            .build();
    OneshotDafifParser.ClientRecords<Airport, Fix, Airway, Procedure, ClientAirspace> records =
        parser.assembleFrom(new ByteArrayInputStream(bytes.toByteArray()));

    List<ClientSequence> boundarySequences = List.of(
        new ClientSequence(10, Shape.POINT, Optional.empty()),
        new ClientSequence(20, Shape.POINT, Optional.empty()));
    ClientAirspace boundary = new ClientAirspace("boundary", "US00001", "", boundarySequences);
    ClientAirspace annulus = new ClientAirspace("suas", "R100", "",
        List.of(new ClientSequence(10, Shape.CIRCLE, Optional.of(2.0))));
    ClientAirspace sector = new ClientAirspace("suas", "R100", "A", List.of(
        new ClientSequence(10, Shape.POINT, Optional.empty()),
        new ClientSequence(30, Shape.POINT, Optional.empty())));
    List<ClientAirspace> boundaries = List.copyOf(records.boundaries());
    List<ClientAirspace> suas = List.copyOf(records.specialUseAirspaces());
    List<ClientAirspace> airspaces = List.copyOf(records.airspaces());

    var standardRecords = OneshotDafifParser.standard(DafifVersion.V81)
        .assembleFrom(new ByteArrayInputStream(bytes.toByteArray()));
    assertAll(
        () -> assertEquals(List.of(boundary), boundaries),
        () -> assertEquals(List.of(annulus, sector), suas),
        () -> assertEquals(List.of(boundary, annulus, sector), airspaces),
        () -> assertTrue(standardRecords.airspaces().isEmpty())
    );
  }

  @Test
  void zipWithoutAirspaceTablesReturnsEmptyAirspaceCollections() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try (ZipOutputStream zip = new ZipOutputStream(bytes)) {
      entry(zip, "DOCS/IGNORED.TXT", "ignored");
    }
    var records = OneshotDafifParser.standard(DafifVersion.V81)
        .assembleFrom(new ByteArrayInputStream(bytes.toByteArray()));
    assertAll(
        () -> assertTrue(records.boundaries().isEmpty()),
        () -> assertTrue(records.specialUseAirspaces().isEmpty()),
        () -> assertTrue(records.airspaces().isEmpty())
    );
  }

  private static String boundaryCircle() {
    return row("US00001", "10", "TEST FIR", "08", "KZNY", "C", "", "", "", "", "", "", "", "", "",
        "N40000000", "40.000000", "W075000000", "-75.000000", "20.00", "", "", "", "", "", "", "", "202601");
  }

  private static String boundaryPoint(String segmentNumber) {
    return row("US00001", segmentNumber, "TEST FIR", "08", "KZNY", "A", "", "N40000000", "40.000000",
        "W075000000", "-75.000000", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "202601");
  }

  private static String boundaryParent() {
    return row("US00001", "08", "TEST FIR", "KZNY", "", "WGE", "WGE",
        "", "", "", "", "", "", "B", "FL600", "FL180", "", "202601", "U", "U");
  }

  private static String suasCircle(String sector, String radius) {
    return suasCircle(sector, radius, "");
  }

  private static String suasCircle(String sector, String radius, String innerRadius) {
    return row("R100", sector, "10", "", "R", "KZNY", "C", "", "", "", "", "", "", "", "", "",
        "N40000000", "40.000000", "W075000000", "-75.000000", radius, innerRadius, "", "", "", "", "", "", "202601");
  }

  private static String suasPoint(String sector, String segmentNumber) {
    return row("R100", sector, segmentNumber, "", "R", "KZNY", "A", "", "N40000000", "40.000000",
        "W075000000", "-75.000000", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "202601");
  }

  private static String suasParent(String sector, String ceiling) {
    return row("R100", sector, "R", "", "KZNY", "", "WGE", "WGE", "", "", "", "B", ceiling,
        "SURFACE", "", "", "202601", "01 Jan 26");
  }

  private static String row(String... fields) {
    return String.join("\t", fields);
  }

  private static void entry(ZipOutputStream zip, String name, String... rows) throws IOException {
    zip.putNextEntry(new ZipEntry("DAFIF8_1/DAFIFT/" + name));
    zip.write(("header\n" + String.join("\n", rows) + "\n").getBytes(StandardCharsets.UTF_8));
    zip.closeEntry();
  }

  private record ClientAirspace(String source, String identifier, String sector, List<ClientSequence> sequences) {
  }

  private record ClientSequence(int number, Shape shape, Optional<Double> innerRadius) {
  }

  private static final class ClientAirspaceStrategy implements BoundaryAssemblyStrategy<ClientAirspace, ClientSequence>,
      SuasAssemblyStrategy<ClientAirspace, ClientSequence> {

    @Override
    public ClientAirspace convertBoundary(DafifBoundaryParent parent, List<ClientSequence> sequences) {
      return new ClientAirspace("boundary", parent.boundaryIdentification(), "", sequences);
    }

    @Override
    public List<ClientSequence> convertBoundarySequences(List<DafifBoundarySegment> segments) {
      return segments.stream()
          .map(segment -> new ClientSequence(segment.segmentNumber(), segment.shape(), segment.radius2()))
          .toList();
    }

    @Override
    public ClientAirspace convertSuas(DafifSuasParent parent, List<ClientSequence> sequences) {
      return new ClientAirspace("suas", parent.suasIdentification(), parent.sector().orElse(""), sequences);
    }

    @Override
    public List<ClientSequence> convertSuasSequences(List<DafifSuasSegment> segments) {
      return segments.stream()
          .map(segment -> new ClientSequence(segment.segmentNumber(), segment.shape(), segment.radius2()))
          .toList();
    }
  }
}
