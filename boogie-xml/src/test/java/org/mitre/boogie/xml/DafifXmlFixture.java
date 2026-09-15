package org.mitre.boogie.xml;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipFile;

import javax.xml.datatype.DatatypeFactory;

import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.tdp.boogie.AiracCycle;
import org.mitre.tdp.boogie.dafif.DafifFileParser;
import org.mitre.tdp.boogie.dafif.DafifVersion;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifRecordConverterFactory;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalParent;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

/** Converts the nine supported DAFIF 8.1 tables into a larger v23_4 JAXB test publication. */
final class DafifXmlFixture {
  private static final Set<String> TABLES = Set.of(
      "ARPT.TXT", "RWY.TXT", "ADD_RWY.TXT", "ILS.TXT", "NAV.TXT", "WPT.TXT",
      "TRM_PAR.TXT", "TRM_SEG.TXT", "ATS.TXT");

  private DafifXmlFixture() {}

  static Loaded load() throws Exception {
    Path archive = Path.of(Objects.requireNonNull(DafifXmlFixture.class
        .getResource("/DAFIF8_1_2601.zip"), "Missing DAFIF fixture").toURI());
    Records records = loadRecords(archive);
    var dates = DatatypeFactory.newInstance();
    var publication = new AeroPublication();
    publication.setCreationDateTime(dates.newXMLGregorianCalendar(Instant.now().toString()));
    publication.setCycleDate(2601);
    publication.setStartOfValidity(dates.newXMLGregorianCalendar(AiracCycle.startDate("2601").toString()));
    publication.setEndOfValidity(dates.newXMLGregorianCalendar(AiracCycle.endDate("2601").toString()));
    publication.setArinc424SchemaVersion("4.0.0");
    publication.setArinc424Supplement("23");
    publication.setIsTestDataset(true);
    publication.setFileName("dafif-2601.xml");
    publication.setSupplierTextField("Converted from the supported DAFIF 8.1 tables for XML/EXI testing");
    var refs = new DafifXmlReferences();
    DafifXmlPoints.populate(records, publication, refs);
    DafifXmlProcedures.populate(records, publication, refs);
    DafifXmlAirways.populate(records, publication, refs);
    return new Loaded(records, publication);
  }

  private static Records loadRecords(Path archive) throws IOException {
    var parser = new DafifFileParser(DafifVersion.V81);
    var consumer = DafifRecordConverterFactory.consumerForVersion(DafifVersion.V81);
    Set<String> loaded = new HashSet<>();
    try (var zip = new ZipFile(archive.toFile())) {
      var entries = zip.entries();
      while (entries.hasMoreElements()) {
        var entry = entries.nextElement();
        String name = entry.getName();
        String file = name.substring(name.lastIndexOf('/') + 1);
        if (entry.isDirectory() || !name.contains("DAFIFT/") || name.contains("TRMH/")
            || name.contains("SUPPH/") || !TABLES.contains(file)) {
          continue;
        }
        if (!loaded.add(file)) {
          throw new IOException("Duplicate DAFIF table: " + name);
        }
        try (var input = zip.getInputStream(entry); var rows = parser.stream(input, file)) {
          rows.forEach(consumer);
        }
      }
    }
    if (!loaded.equals(TABLES)) {
      Set<String> missing = new HashSet<>(TABLES);
      missing.removeAll(loaded);
      throw new IOException("Missing DAFIF tables: " + missing);
    }
    return new Records(List.copyOf(consumer.dafifAirports()), List.copyOf(consumer.dafifRunways()),
        List.copyOf(consumer.dafifAddRunways()), List.copyOf(consumer.dafifIls()),
        List.copyOf(consumer.dafifNavaids()), List.copyOf(consumer.dafifWaypoints()),
        List.copyOf(consumer.dafifTerminalParents()), List.copyOf(consumer.dafifTerminalSegments()),
        List.copyOf(consumer.dafifAts()));
  }

  record Records(List<DafifAirport> airports, List<DafifRunway> runways, List<DafifAddRunway> addRunways,
                 List<DafifIls> ils, List<DafifNavaid> navaids, List<DafifWaypoint> waypoints,
                 List<DafifTerminalParent> terminalParents, List<DafifTerminalSegment> terminalSegments,
                 List<DafifAirTrafficSegment> ats) {}

  record Loaded(Records source, AeroPublication publication) {}
}
