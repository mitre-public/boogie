package org.mitre.tdp.boogie.arinc;

import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

/**
 * Filter for dropping ARINC continuation records - most of the baseline ARINC record specs will have additional continuation records which are
 * used to specify non-standard fields or any supplementary information about the main record. Most of these continuation records aren't overly
 * useful in TDP - as such many of the ARINC converter classes wish to skip them for parsing.
 */
public final class ContinuationRecordFilter implements Predicate<ArincRecord> {

  @Override
  public boolean test(ArincRecord arincRecord) {
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    return continuationRecordNumber.map(record -> !isContinuationRecord(record)).orElse(false);
  }

  /**
   * Continuation record numbers are [1-9a-z]. For any record with a continuation
   */
  Boolean isContinuationRecord(String recordNumber) {
    return StringUtils.isAlpha(recordNumber) || 1 < Integer.parseInt(recordNumber);
  }
}