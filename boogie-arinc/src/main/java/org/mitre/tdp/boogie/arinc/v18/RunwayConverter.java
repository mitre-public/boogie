package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;

public final class RunwayConverter implements Function<ArincRecord, Optional<ArincRunway>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new RunwayValidator().negate();

  @Override
  public Optional<ArincRunway> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Input ArincRecord cannot be null.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    ArincRunway runway = RunwayBuilder.INSTANCE.apply(arincRecord).build();

    return Optional.of(runway);
  }
}
