package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

import java.io.File;

import org.mitre.caasd.commons.fileutil.FileLineIterator;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.LinkedHashMultimap;

/**
 * Factory class for instantiating {@link ArincDatabase}(s) from a variety of sources.
 * <br>
 * The most common instantiation pattern involves loading the database from a provided ARINC-424 flat file.
 */
public final class ArincDatabaseFactory {

  private static final Logger LOG = LoggerFactory.getLogger(ArincDatabaseFactory.class);

  /**
   * Class used for converting parsed {@link ArincRecord}s into {@link ArincKey}s which can then be loaded into a database.
   */
  private static final ArincDatabaseIndexer indexer = new ArincDatabaseIndexer();

  private ArincDatabaseFactory() {
    throw new IllegalStateException("Cannot instantiate static factory class.");
  }

  /**
   * Reads, parses and indexes a flat file containing ARINC424 data into a new {@link ArincDatabase} record.
   * <br>
   */
  public static ArincDatabase newDatabaseFromFile(File file) {
    try (FileLineIterator lineIterator = new FileLineIterator(requireNonNull(file))) {

      LinkedHashMultimap<ArincKey, ArincRecord> recordMap = LinkedHashMultimap.create();
      LOG.debug("Beginning scan of file {} for ARINC records.", file);

      lineIterator.forEachRemaining(line -> ArincVersion.V18.parser().apply(line)
          .ifPresent(record -> recordMap.put(indexer.apply(record), record)));

      LOG.debug("Instantiating new in-memory ArincDatabase with {} total entries.", recordMap.size());
      return new ArincDatabase(recordMap);
    } catch (Exception e) {
      throw new DatabaseInstantiationException(e);
    }
  }

  private static final class ArincRecordCollector{

  }

  public static final class DatabaseInstantiationException extends RuntimeException {

    DatabaseInstantiationException(Exception e) {
      super("Fatal error encountered when attempting to instantiate an ArincDatabase.", e);
    }
  }
}
