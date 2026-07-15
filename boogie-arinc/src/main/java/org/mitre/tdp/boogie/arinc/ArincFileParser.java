package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Consumer;
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
    LinkedHashSet<ArincRecord> records = new LinkedHashSet<>();
    parse(file, records::add);
    return records;
  }

  /**
   * Parses a file incrementally and sends each supported record to {@code sink}. Unlike
   * {@link #apply(File)}, this method does not retain or de-duplicate the parsed records.
   */
  public void parse(File file, Consumer<? super ArincRecord> sink) {
    requireNonNull(file);
    try (FileInputStream input = new FileInputStream(file)) {
      parse(input, sink);
    } catch (IOException e) {
      throw DemotedException.demote("Error opening input 424 file: " + file, e);
    }
  }

  /**
   * Parses an ARINC 424 stream incrementally and sends each supported record to
   * {@code sink}. The stream is consumed and closed, matching the existing
   * {@link #apply(InputStream)} ownership contract.
   *
   * <p>The callback may convert, persist, or otherwise discard each record immediately,
   * avoiding the full-file {@link LinkedHashSet} used by the collection-returning API.
   */
  public void parse(InputStream file, Consumer<? super ArincRecord> sink) {
    requireNonNull(file);
    requireNonNull(sink);
    try (FileLineIterator lineIterator = new FileLineIterator(new InputStreamReader(file))) {
      lineIterator.forEachRemaining(line -> recordParser.parse(line).ifPresent(sink));
    } catch (Exception e) {
      throw new IllegalArgumentException("Error during parse of ARINC 424 record stream.", e);
    }
  }
}
