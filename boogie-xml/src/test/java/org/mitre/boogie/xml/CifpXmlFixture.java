package org.mitre.boogie.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.tdp.boogie.AiracCycle;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.IsThisAHeader;
import org.mitre.tdp.boogie.arinc.IsThisAPrimaryRecord;
import org.mitre.tdp.boogie.arinc.model.ArincHeaderOne;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertedArincRecords;

/** Builds a larger v23_4 JAXB test publication from the supported CIFP V19 primary records. */
final class CifpXmlFixture {

  private CifpXmlFixture() {}

  static Loaded load() throws IOException, DatatypeConfigurationException {
    ArincRecordParser parser = ArincRecordParser.standard(ArincVersion.V19.specs());
    var consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V19);
    var primary = new IsThisAPrimaryRecord();
    var header = new IsThisAHeader();
    var refs = new CifpXmlReferences();
    Map<String, Integer> unparsed = new TreeMap<>();
    int continuations = 0;
    try (var input = new GZIPInputStream(Objects.requireNonNull(
        CifpXmlFixture.class.getResourceAsStream("/cifp-2101.dat.gz"), "Missing CIFP fixture"));
         var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.US_ASCII))) {
      String line;
      while ((line = reader.readLine()) != null) {
        ArincRecord record = parser.parse(line).orElse(null);
        if (record == null) {
          unparsed.merge(family(line), 1, Integer::sum);
        } else if (header.test(record)) {
          consumer.accept(record);
        } else if (primary.test(record)) {
          consumer.accept(record);
          String recordFamily = family(line);
          if (line.charAt(4) == 'U') {
            refs.addAirspaceRecord(recordFamily, record.requiredField("fileRecordNumber"), line);
          }
          switch (recordFamily) {
            case "PD", "PE", "PF", "HD", "HE", "HF", "ER", "EP" -> refs.addRouteRecord(recordFamily, record);
            default -> { }
          }
        } else {
          continuations++;
        }
      }
    }
    ConvertedArincRecords records = consumer.snapshot();
    AeroPublication publication = publication(records.arincHeaderOne().orElseThrow());
    CifpXmlPoints.populate(records, publication, refs);
    CifpXmlRoutes.populate(records, publication, refs);
    CifpXmlAirspaces.populate(records, publication, refs);
    return new Loaded(records, publication, Map.copyOf(unparsed), continuations);
  }

  private static AeroPublication publication(ArincHeaderOne header) throws DatatypeConfigurationException {
    var factory = DatatypeFactory.newInstance();
    String cycle = header.cycle().orElseThrow();
    var publication = new AeroPublication();
    publication.setCreationDateTime(dateTime(factory, header.creationTime().orElseThrow()));
    publication.setStartOfValidity(dateTime(factory, AiracCycle.startDate(cycle)));
    publication.setEndOfValidity(dateTime(factory, AiracCycle.endDate(cycle)));
    publication.setCycleDate(Integer.valueOf(cycle));
    publication.setArinc424SchemaVersion("4.0.0");
    publication.setArinc424Supplement("23");
    publication.setIsTestDataset(true);
    publication.setFileName("cifp-2101.xml");
    publication.setSupplierTextField("Converted from CIFP V19 primary records for XML/EXI testing");
    header.dataSupplierIdentifier().ifPresent(publication::setDataSupplierIdent);
    header.targetCustomerIdent().ifPresent(publication::setTargetCustomerIdent);
    header.databasePartNumber().ifPresent(publication::setDatabasePartNumber);
    return publication;
  }

  private static XMLGregorianCalendar dateTime(DatatypeFactory factory, Instant instant) {
    return factory.newXMLGregorianCalendar(instant.toString());
  }

  private static String family(String line) {
    if (line.startsWith("HDR")) {
      return line.substring(0, 5);
    }
    char section = line.charAt(4);
    if (section == 'P' && line.charAt(5) == 'N') {
      return "PN";
    }
    if (section == 'P' || section == 'H') {
      return "" + section + line.charAt(12);
    }
    return "" + section + line.charAt(5);
  }

  record Loaded(ConvertedArincRecords source, AeroPublication publication,
                Map<String, Integer> unparsedFamilies, int skippedContinuations) {}
}
