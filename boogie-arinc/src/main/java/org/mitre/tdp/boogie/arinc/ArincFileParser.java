package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Function;

import org.mitre.caasd.commons.fileutil.FileLineIterator;
import org.mitre.caasd.commons.util.DemotedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper class for applying a supplied {@link ArincRecordParser} to the contents of an input file - assumed to be data on the
 * ARINC 424 format.
 * <br>
 * With the standard parser implementations for the subset of currently supported record types parsing and then converting an
 * entire cycle of data should only take a few seconds.
 */
public final class ArincFileParser implements Function<InputStream, Collection<ArincRecord>> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincFileParser.class);

  /**
   * The function to be applied to the input raw record string to generate a semi-structured {@link ArincRecord} object.
   * <br>
   * The standard implementation of this is the {@link ArincRecordParser}.
   */
  private final ArincRecordParser recordParser;

  public ArincFileParser(RecordSpec... recordSpecs) {
    this(ArincRecordParser.standard(recordSpecs));
  }

  public ArincFileParser(ArincRecordParser recordParser) {
    this.recordParser = requireNonNull(recordParser);
  }

  public Collection<ArincRecord> apply(File file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      return apply(fis);
    } catch (IOException e) {
      throw DemotedException.demote("Error opening input 424 file: " + file, e);
    }
  }

  @Override
  public Collection<ArincRecord> apply(InputStream file) {
    requireNonNull(file);
    try (FileLineIterator lineIterator = new FileLineIterator(new InputStreamReader(file))) {

      LinkedHashSet<ArincRecord> records = new LinkedHashSet<>();
      lineIterator.forEachRemaining(line -> recordParser.parse(line).ifPresent(records::add));

      return records;
    } catch (Exception e) {
      throw new IllegalArgumentException("Error during parse of ARINC 424 record stream.", e);
    }
  }
}
