package org.mitre.tdp.boogie.arinc.v21;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemBuilder;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemValidator;

public final class GnssLandingSystemConverter implements Function<ArincRecord, Optional<ArincGnssLandingSystem>> {
  private static final Predicate<ArincRecord> isInvalidRecord = new GnssLandingSystemValidator().negate();
  @Override
  public Optional<ArincGnssLandingSystem> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot convert null ArincRecord.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<Integer> glideSlopeTCH = arincRecord.optionalField("glidePathTch");

    ArincGnssLandingSystem gnssLandingSystem = GnssLandingSystemBuilder.INSTANCE.apply(arincRecord)
        .glidePathTCH(glideSlopeTCH.orElse(null))
        .build();

    return Optional.of(gnssLandingSystem);
  }
}
