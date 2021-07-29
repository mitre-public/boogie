package org.mitre.tdp.boogie.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

import org.mitre.caasd.commons.fileutil.FileLineIterator;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;

/**
 * This class represents the parsed output of CIFP cycle 2101 as statically loaded from the application resources. This class is
 * a singleton and will parse the contents of the file only once - and will lazily load the data - that is to say if no calls are
 * made to {@link #instance()} the file will never be loaded.
 */
public final class EmbeddedCifpFile implements Supplier<Collection<ArincRecord>> {

  private static final Logger LOG = LoggerFactory.getLogger(EmbeddedCifpFile.class);

  private static final String embeddedFileName = "cifp-2101.dat.gz";

  private final Collection<ArincRecord> records;

  private EmbeddedCifpFile() {
    this.records = loadRecords();
  }

  @Override
  public Collection<ArincRecord> get() {
    return records;
  }

  /**
   * Loads all of the {@link ArincRecord}s from the embedded local CIFP file using the V19 record schemas.
   */
  private Collection<ArincRecord> loadRecords() {
    ArincRecordParser parser = ArincVersion.V19.parser();
    LOG.info("Loading records from embedded CIFP file.");

    URL resourceUrl = Resources.getResource(embeddedFileName);
    try (InputStreamReader reader = new InputStreamReader(new GZIPInputStream(resourceUrl.openStream()))) {
      FileLineIterator iterator = new FileLineIterator(reader);

      LinkedHashSet<ArincRecord> records = new LinkedHashSet<>();

      while (iterator.hasNext()) {
        parser.apply(iterator.next()).ifPresent(records::add);
      }

      LOG.info("Finished loading {} records from embedded file.", records.size());
      return records;
    } catch (IOException e) {
      throw new RuntimeException("Error opening embedded resource file.", e);
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
