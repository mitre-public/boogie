package org.mitre.tdp.boogie.dafif;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.mitre.tdp.boogie.dafif.model.ConvertingDafifRecordConsumer;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;

/**
 * This class represents the parsed output of a DAFIF 8.1 cycle as statically loaded from the application resources. This class
 * is a singleton and will parse the contents of the file only once - and will lazily load the data - that is to say if no calls
 * are made to {@link #instance()} the file will never be loaded.
 */
public final class EmbeddedDafifFile {

  private static final Logger LOG = LoggerFactory.getLogger(EmbeddedDafifFile.class);

  private static final String EMBEDDED_FILE_NAME = "DAFIF8_1_2601.zip";

  private static final Set<String> PARSEABLE_FILES = Set.of(
      "ARPT.TXT", "RWY.TXT", "ADD_RWY.TXT", "ILS.TXT", "NAV.TXT", "WPT.TXT", "TRM_PAR.TXT", "TRM_SEG.TXT", "ATS.TXT"
  );

  private final ConvertingDafifRecordConsumer records;

  private EmbeddedDafifFile() {
    this.records = DafifRecordConverterFactory.consumerForVersion(DafifVersion.V81);
    loadRecords();
  }

  /**
   * Returns the singleton instance of the contents of the embedded DAFIF file.
   */
  public static EmbeddedDafifFile instance() {
    return SingletonHolder.INSTANCE;
  }

  public Collection<DafifAirport> dafifAirports() {
    return records.dafifAirports();
  }

  public Collection<DafifRunway> dafifRunways() {
    return records.dafifRunways();
  }

  public Collection<DafifAddRunway> dafifAddRunways() {
    return records.dafifAddRunways();
  }

  public Collection<DafifIls> dafifIls() {
    return records.dafifIls();
  }

  public Collection<DafifNavaid> dafifNavaids() {
    return records.dafifNavaids();
  }

  public Collection<DafifWaypoint> dafifWaypoints() {
    return records.dafifWaypoints();
  }

  public Collection<DafifTerminalParent> dafifTerminalParents() {
    return records.dafifTerminalParents();
  }

  public Collection<DafifTerminalSegment> dafifTerminalSegments() {
    return records.dafifTerminalSegments();
  }

  public Collection<DafifAirTrafficSegment> dafifAts() {
    return records.dafifAts();
  }

  private void loadRecords() {
    DafifFileParser parser = new DafifFileParser(DafifVersion.V81);
    LOG.info("Loading records from embedded DAFIF file: {}", EMBEDDED_FILE_NAME);

    try (InputStream is = Resources.getResource(EMBEDDED_FILE_NAME).openStream();
         ZipInputStream zis = new ZipInputStream(is)) {

      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        String entryName = entry.getName();
        String filename = entryName.contains("/") ? entryName.substring(entryName.lastIndexOf('/') + 1) : entryName;

        if (!entry.isDirectory() && entryName.contains("DAFIFT/") && !entryName.contains("TRMH/") && !entryName.contains("SUPPH/") && PARSEABLE_FILES.contains(filename)) {
          LOG.info("Parsing zip entry: {}", entryName);
          Collection<DafifRecord> parsed = parser.apply(new NonClosingInputStream(zis), filename);
          parsed.forEach(records);
          LOG.info("Parsed {} records from {}", parsed.size(), filename);
        }

        zis.closeEntry();
      }

    } catch (IOException e) {
      throw new IllegalArgumentException("Error opening embedded DAFIF resource file.", e);
    }

    LOG.info("Finished loading DAFIF data: {} airports, {} runways, {} navaids, {} waypoints, {} terminal parents, {} terminal segments",
        dafifAirports().size(), dafifRunways().size(), dafifNavaids().size(), dafifWaypoints().size(),
        dafifTerminalParents().size(), dafifTerminalSegments().size());
  }

  /**
   * Wrapper that prevents the underlying stream from being closed, so that {@link DafifFileParser} doesn't
   * close the {@link ZipInputStream} when it finishes reading an entry.
   */
  private static final class NonClosingInputStream extends FilterInputStream {
    NonClosingInputStream(InputStream in) {
      super(in);
    }

    @Override
    public void close() {
      // intentionally do not close the underlying stream
    }
  }

  /**
   * "Initialization on demand idiom" -- lazy & threadsafe; only referenced once {@link #instance()} is called.
   */
  private static final class SingletonHolder {
    private static final EmbeddedDafifFile INSTANCE = new EmbeddedDafifFile();
  }
}
