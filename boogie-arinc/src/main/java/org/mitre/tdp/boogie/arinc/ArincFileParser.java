package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Function;

import org.mitre.caasd.commons.fileutil.FileLineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper class for applying a supplied {@link ArincRecordParser} to the contents of an input file - assumed to be data on the
 * ARINC 424 format.
 */
public final class ArincFileParser implements Function<File, Collection<ArincRecord>> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincFileParser.class);

  private final ArincRecordParser recordParser;

  public ArincFileParser(RecordSpec... recordSpecs) {
    this(new ArincRecordParser(recordSpecs));
  }

  public ArincFileParser(ArincRecordParser recordParser) {
    this.recordParser = requireNonNull(recordParser);
  }

  @Override
  public Collection<ArincRecord> apply(File file) {
    try (FileLineIterator lineIterator = new FileLineIterator(requireNonNull(file))) {

      LinkedHashSet<ArincRecord> records = new LinkedHashSet<>();
      LOG.debug("Beginning scan of file {} for ARINC records.", file);

      lineIterator.forEachRemaining(line -> recordParser.apply(line).ifPresent(records::add));

      LOG.debug("Instantiating new in-memory ArincDatabase with {} total entries.", records.size());
      return records;
    } catch (Exception e) {
      throw new RuntimeException("Error during parse of ARINC file at: ".concat(file.getAbsolutePath()), e);
    }
  }
}
