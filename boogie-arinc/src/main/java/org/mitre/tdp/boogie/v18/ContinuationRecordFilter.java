package org.mitre.tdp.boogie.v18;

import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.mitre.tdp.boogie.ArincRecordDecorator;

/**
 * Filter for dropping ARINC continuation records - most of the baseline ARINC record specs will have additional continuation records which are
 * used to specify non-standard fields or any supplementary information about the main record. Most of these continuation records aren't overly
 * useful in TDP - as such many of the ARINC converter classes wish to skip them for parsing.
 */
public interface ContinuationRecordFilter<T extends ArincRecordDecorator> extends Predicate<T> {

  @Override
  default boolean test(T decoratedRecord) {
    Optional<String> continuationRecordNumber = decoratedRecord.arincRecord().getOptionalField("continuationRecordNumber");
    return continuationRecordNumber.map(record -> !isContinuationRecord(record)).orElse(false);
  }

  /**
   * Continuation record numbers are [1-9a-z]. For any record with a continuation
   */
  default Boolean isContinuationRecord(String recordNumber) {
    return StringUtils.isAlpha(recordNumber) || 1 < Integer.parseInt(recordNumber);
  }

  /**
   * Returns a new anonymous instance of a {@link ContinuationRecordFilter}.
   */
  static <T extends ArincRecordDecorator> ContinuationRecordFilter<T> newInstance() {
    return new ContinuationRecordFilter<T>() {};
  }
}