package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;

public final class HoldingPatternConverter implements Function<ArincRecord, Optional<ArincHoldingPattern>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new HoldingPatternValidator().negate();

  @Override
  public Optional<ArincHoldingPattern> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot convert null arinc records.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    return Optional.of(HoldingPatternBuilder.INSTANCE.apply(arincRecord).build());
  }


}
