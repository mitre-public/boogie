package org.mitre.tdp.boogie.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.zip.GZIPInputStream;

import org.mitre.caasd.commons.fileutil.FileLineIterator;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.ContinuationRecordFilter;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
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
public final class EmbeddedCifpFile {

  private static final Logger LOG = LoggerFactory.getLogger(EmbeddedCifpFile.class);

  private static final String EMBEDDED_FILE_NAME = "cifp-2101.dat.gz";

  private final ConvertingArincRecordConsumer records;

  private EmbeddedCifpFile() {
    this.records = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V19);
    loadRecords().forEach(records);
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

  public int totalRecords() {
    return arincAirports().size()
        + arincRunways().size()
        + arincLocalizerGlideSlopes().size()
        + arincNdbNavaids().size()
        + arincVhfNavaids().size()
        + arincWaypoints().size()
        + arincAirwayLegs().size()
        + arincProcedureLegs().size();
  }

  /**
   * Loads all of the {@link ArincRecord}s from the embedded local CIFP file using the V19 record schemas.
   */
  private Collection<ArincRecord> loadRecords() {
    ArincRecordParser parser = ArincVersion.V19.parser();
    LOG.info("Loading records from embedded CIFP file.");

    URL resourceUrl = Resources.getResource(EMBEDDED_FILE_NAME);
    try (InputStreamReader reader = new InputStreamReader(new GZIPInputStream(resourceUrl.openStream()))) {
      FileLineIterator iterator = new FileLineIterator(reader);

      LinkedHashSet<ArincRecord> parsedRecords = new LinkedHashSet<>();

      ContinuationRecordFilter continuationRecordFilter = new ContinuationRecordFilter();
      while (iterator.hasNext()) {
        parser.apply(iterator.next()).filter(continuationRecordFilter).ifPresent(parsedRecords::add);
      }

      LOG.info("Finished loading {} records from embedded file.", parsedRecords.size());
      return parsedRecords;
    } catch (IOException e) {
      throw new IllegalArgumentException("Error opening embedded resource file.", e);
    }
  }

  /**
   * Returns the singleton instance of the contents of the embedded ARINC 424 file.
   */
  public static EmbeddedCifpFile instance() {
    return SingletonHolder.INSTANCE;
  }

  /**
   * "Initialization on demand idiom" -- lazy & threadsafe; only referenced once @link #instance()} is called.
   * <br>
   * See https://sourcemaking.com/design_patterns/singleton/java/1 for details.
   */
  private static final class SingletonHolder {
    private static final EmbeddedCifpFile INSTANCE = new EmbeddedCifpFile();
  }
}
