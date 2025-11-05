package org.mitre.tdp.boogie.arinc;

import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;

/**
 * Filter for dropping ARINC continuation records.
 * <br>
 * Most of the baseline ARINC record specs will have additional continuation records which are used to specify non-standard fields
 * or any supplementary information about the primary record.
 * <br>
 * This filter is supplied as <i>most</i> of these continuation records aren't overly useful in TDP - as such many of the ARINC
 * converter classes wish to skip them for parsing.
 */
public final class IsThisAPrimaryRecord implements Predicate<ArincRecord> {
  private static final PrimaryRecord PRIMARY = PrimaryRecord.INSTANCE;

  @Override
  public boolean test(ArincRecord arincRecord) {
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    return continuationRecordNumber.filter(PRIMARY).isPresent();
  }
}