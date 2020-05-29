package org.mitre.tdp.boogie;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Interface for any object which can function as a parser for ARINC format navigational data.
 */
@FunctionalInterface
public interface ArincParser {

  /**
   * The spec to use when parsing the input ARINC records.
   */
  ArincSpec arincSpec();

  /**
   * Returns true if the given record matches a known {@link RecordSpec} within the provided {@link #arincSpec()}.
   */
  default boolean matchesKnownRecordSpec(String arincRecord) {
    return arincSpec().recordSpecs().stream().anyMatch(rspec -> rspec.matchesRecord(arincRecord));
  }

  /**
   * Parses the input raw record against the given {@link #arincSpec()}.
   */
  default ArincRecord parse(String rawRecord) {
    RecordSpec recordSpec = arincSpec().recordSpecs().stream()
        .filter(rspec -> rspec.matchesRecord(rawRecord))
        .findFirst()
        .orElseThrow(() -> new UnknownRecordException(arincSpec(), rawRecord));

    checkArgument(recordSpec.recordLength() == rawRecord.length(),
        "Matched spec length doesn't match record length. Actual length " + rawRecord.length() + ", Expected length " + recordSpec.recordLength());
    return recordSpec.createParsedRecord(rawRecord);
  }

  /**
   * Returns a new anonymous instance of and {@link ArincParser} using the given specification.
   */
  static ArincParser withSpec(ArincSpec spec) {
    checkNotNull(spec);
    return () -> spec;
  }
}
