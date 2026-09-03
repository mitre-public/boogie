package org.mitre.tdp.boogie.arinc;

import com.google.common.io.Resources;
import org.mitre.tdp.boogie.arinc.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

/**
 * This class represents the parsed output of CIFP cycle 2101 as statically loaded from the application resources. This class is
 * a singleton and will parse the contents of the file only once - and will lazily load the data - that is to say if no calls are
 * made to {@link #instance()} the file will never be loaded.
 */
public final class EmbeddedLidoFile {

  private static final Logger LOG = LoggerFactory.getLogger(EmbeddedLidoFile.class);

  private static final String EMBEDDED_FILE_NAME = "A424-22std.dat.gz";

  private final ConvertedArincRecords records;

  private EmbeddedLidoFile() {
    ConvertingArincRecordConsumer consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V22);
    ArincRecordParser parser = ArincRecordParser.standard(ArincVersion.V22.specs());
    IsThisAPrimaryRecord isThisAPrimaryRecord = new IsThisAPrimaryRecord();
    IsThisAHeader isThisAHeader = new IsThisAHeader();
    LOG.info("Loading records from embedded LIDO file. {}", totalRecords());
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream()))) {
      reader.lines().map(parser::parse).flatMap(Optional::stream).filter(isThisAHeader.negate()).filter(isThisAPrimaryRecord).forEach(consumer);
    } catch (IOException e) {
      throw new IllegalArgumentException("Error opening embedded resource file.", e);
    }
    this.records = consumer.snapshot();
  }

  /**
   * Returns the singleton instance of the contents of the embedded ARINC 424 file.
   */
  public static EmbeddedLidoFile instance() {
    return SingletonHolder.INSTANCE;
  }

  public static InputStream getInputStream() throws IOException {
    return new GZIPInputStream(Resources.getResource(EMBEDDED_FILE_NAME).openStream());
  }

  public Collection<ArincAirport> arincAirports() {
    return records.arincAirports();
  }

  public Collection<ArincRunway> arincRunways() {
    return records.arincRunways();
  }

  public Collection<ArincLocalizerGlideSlope> arincLocalizerGlideSlopes() {
    return records.arincLocalizerGlideSlopes();
  }

  public Collection<ArincNdbNavaid> arincNdbNavaids() {
    return records.arincNdbNavaids();
  }

  public Collection<ArincVhfNavaid> arincVhfNavaids() {
    return records.arincVhfNavaids();
  }

  public Collection<ArincWaypoint> arincWaypoints() {
    return records.arincWaypoints();
  }

  public Collection<ArincAirwayLeg> arincAirwayLegs() {
    return records.arincAirwayLegs();
  }

  public Collection<ArincProcedureLeg> arincProcedureLegs() {
    return records.arincProcedureLegs();
  }

  public Collection<ArincGnssLandingSystem> arincGnssLandingSystems() {
    return records.arincGnssLandingSystems();
  }

  public Collection<ArincHoldingPattern> arincHoldingPatterns() {
    return records.arincHoldingPatterns();
  }

  public Collection<ArincFirUirLeg> arincFirUirLegs() {
    return records.arincFirUirLegs();
  }

  public Collection<ArincHelipad> arincHelipads() {
    return records.arincHelipads();
  }

  public Collection<ArincControlledAirspaceLeg> arincControlledAirspaceLegs() {
    return records.arincControlledAirspaceLegs();
  }

  public Collection<ArincRestrictiveAirspaceLeg> arincRestrictiveAirspaceLegs() {
    return records.arincRestrictiveAirspaceLegs();
  }

  public Collection<ArincHeliport> arincHeliports() {
    return records.arincHeliports();
  }

  public int totalRecords() {
    return arincAirports().size()
        + arincRunways().size()
        + arincLocalizerGlideSlopes().size()
        + arincNdbNavaids().size()
        + arincVhfNavaids().size()
        + arincWaypoints().size()
        + arincAirwayLegs().size()
        + arincProcedureLegs().size()
        + arincGnssLandingSystems().size()
        + arincHoldingPatterns().size()
        + arincFirUirLegs().size()
        + arincControlledAirspaceLegs().size()
        + arincRestrictiveAirspaceLegs().size()
        + arincHeliports().size()
        + arincHelipads().size();
  }

  /**
   * “Initialization-on-demand holder idiom” — lazy and thread-safe; referenced only when
   * {@link EmbeddedLidoFile#instance()} is called.
   * <br>
   * See <a href="https://sourcemaking.com/design_patterns/singleton/java/1">...</a> for details.
   */
  private static final class SingletonHolder {
    private static final EmbeddedLidoFile INSTANCE = new EmbeddedLidoFile();
  }
}
