package org.mitre.tdp.boogie.arinc.v19;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;
import org.mitre.tdp.boogie.arinc.v18.HoldingPatternBuilder;
import org.mitre.tdp.boogie.arinc.v18.HoldingPatternValidator;

public final class HoldingPatternConverter implements Function<ArincRecord, Optional<ArincHoldingPattern>> {
  private static final Predicate<ArincRecord> isInvalidRecord = new HoldingPatternValidator().negate();

  @Override
  public Optional<ArincHoldingPattern> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot convert null arinc records.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<Integer> verticalScale = arincRecord.optionalField("verticalScaleFactor");
    Optional<Integer> rvsmMin = arincRecord.optionalField("rvsmMinimumLevel");
    Optional<Integer> rvsmMax = arincRecord.optionalField("rvsmMaximumLevel");

    ArincHoldingPattern arincHoldingPattern = HoldingPatternBuilder.INSTANCE.apply(arincRecord)
        .verticalScaleFactor(verticalScale.orElse(null))
        .rvsmMin(rvsmMin.orElse(null))
        .rvsmMax(rvsmMax.orElse(null))
        .build();

    return Optional.of(arincHoldingPattern);
  }
}
