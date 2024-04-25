package org.mitre.tdp.boogie.dafif;

import org.mitre.caasd.commons.fileutil.FileLineIterator;
import org.mitre.caasd.commons.util.DemotedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class DafifFileParser implements Function<File, Collection<DafifRecord>> {

  private final DafifRecordParser recordParser;

  public DafifFileParser(DafifRecordSpec... recordSpecs) {
    this(DafifRecordParser.standard(recordSpecs));
  }

  public DafifFileParser(DafifRecordParser recordParser) {
    this.recordParser = requireNonNull(recordParser);
  }

  @Override
  public Collection<DafifRecord> apply(File file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      return apply(fis, file.getName());
    } catch (IOException e) {
      throw DemotedException.demote("Error opening dafif file: " + file, e);
    }
  }

  public Collection<DafifRecord> apply(InputStream inputStream, String filename) {
    requireNonNull(inputStream);
    requireNonNull(filename);

    try (FileLineIterator lineIterator = new FileLineIterator(new InputStreamReader(inputStream))) {
      LinkedHashSet<DafifRecord> records = new LinkedHashSet<>();

      // skip the first line of txt file because it is column headers
      if (lineIterator.hasNext()) {
        lineIterator.next();
      }
      lineIterator.forEachRemaining(line -> recordParser.parse(getRecordType(filename), line).ifPresent(records::add));

      return records;
    } catch (Exception e) {
      throw new IllegalArgumentException("Error during parsing of DAFIF record stream.", e);
    }
  }

  private DafifRecordType getRecordType(String filename) {
    return Optional.of(DafifRecordType.valueOf(filename.split("\\.")[0]))
        .orElseThrow(() -> new RuntimeException("Cannot find DAFIF record type from filename: " + filename));
  }
}
