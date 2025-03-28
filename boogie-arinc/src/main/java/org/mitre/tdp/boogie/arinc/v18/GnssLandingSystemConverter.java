package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;

public final class GnssLandingSystemConverter implements Function<ArincRecord, Optional<ArincGnssLandingSystem>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new GnssLandingSystemValidator().negate();

  @Override
  public Optional<ArincGnssLandingSystem> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot convert null ArincRecord.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    ArincGnssLandingSystem gnssLandingSystem = GnssLandingSystemBuilder.INSTANCE.apply(arincRecord).build();

    return Optional.of(gnssLandingSystem);
  }
}
