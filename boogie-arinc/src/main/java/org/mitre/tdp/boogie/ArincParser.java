package org.mitre.tdp.boogie;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

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
    return tryParse(rawRecord).orElseThrow(() -> new UnknownRecordException(arincSpec(), rawRecord));
  }

  /**
   * Attempts to parse the input record against the known {@link #arincSpec()}, of the parser. If no matching spec
   * is found this method returns {@link Optional#empty()}.
   */
  default Optional<ArincRecord> tryParse(String rawRecord) {
    Optional<RecordSpec> recordSpec = arincSpec().recordSpecs().stream()
        .filter(rspec -> rspec.matchesRecord(rawRecord))
        .findFirst();

    return recordSpec.map(spec -> {
      checkArgument(spec.recordLength() == rawRecord.length(),
          "Matched spec length doesn't match record length. Actual length " + rawRecord.length() + ", Expected length " + spec.recordLength());
      return spec.createParsedRecord(rawRecord);
    });
  }

  /**
   * Returns a new anonymous instance of and {@link ArincParser} using the given specification.
   */
  static ArincParser withSpec(ArincSpec spec) {
    checkNotNull(spec);
    return () -> spec;
  }
}
