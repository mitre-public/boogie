package org.mitre.tdp.boogie.arinc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;
import org.mitre.tdp.boogie.arinc.model.ArincFirUirLeg;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;

/**
 * This class represents the parsed output of CIFP cycle 2101 as statically loaded from the application resources. This class is
 * a singleton and will parse the contents of the file only once - and will lazily load the data - that is to say if no calls are
 * made to {@link #instance()} the file will never be loaded.
 */
public final class EmbeddedLidoFile {

  private static final Logger LOG = LoggerFactory.getLogger(EmbeddedLidoFile.class);

  private static final String EMBEDDED_FILE_NAME = "A424-22std.dat.gz";

  private final ConvertingArincRecordConsumer records;

  private EmbeddedLidoFile() {
    this.records = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V22);
    ArincRecordParser parser = ArincVersion.V22.parser();
    IsThisAPrimaryRecord isThisAPrimaryRecord = new IsThisAPrimaryRecord();
    IsThisAHeader isThisAHeader = new IsThisAHeader();
    LOG.info("Loading records from embedded LIDO file.");
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream()))) {
      reader.lines().map(parser::parse).flatMap(Optional::stream).filter(isThisAHeader.negate()).filter(isThisAPrimaryRecord).forEach(records);
    } catch (IOException e) {
      throw new IllegalArgumentException("Error opening embedded resource file.", e);
    }
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
        + arincHeliports().size()
        + arincHelipads().size();
  }

  /**
   * "Initialization on demand idiom" -- lazy & threadsafe; only referenced once @link #instance()} is called.
   * <br>
   * See https://sourcemaking.com/design_patterns/singleton/java/1 for details.
   */
  private static final class SingletonHolder {
    private static final EmbeddedLidoFile INSTANCE = new EmbeddedLidoFile();
  }
}
