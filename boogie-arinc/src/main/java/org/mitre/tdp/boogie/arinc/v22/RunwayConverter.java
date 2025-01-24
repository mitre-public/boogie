package org.mitre.tdp.boogie.arinc.v22;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.v18.RunwayBuilder;
import org.mitre.tdp.boogie.arinc.v18.RunwayValidator;

public final class RunwayConverter implements Function<ArincRecord, Optional<ArincRunway>> {
  private static final Predicate<ArincRecord> isInvalidRecord = new RunwayValidator().negate();
  @Override
  public Optional<ArincRunway> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Input ArincRecord cannot be null.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<String> runwayAccuracyComplianceFlag = arincRecord.optionalField("runwayAccuracyComplianceFlag");
    Optional<String> landingThresholdAccuracyComplianceFlag = arincRecord.optionalField("landingThresholdElevationAccuracyComplianceFlag");

    ArincRunway builder = RunwayBuilder.INSTANCE.apply(arincRecord)
        .runwayAccuracyComplianceFlag(runwayAccuracyComplianceFlag.orElse(null))
        .landingThresholdElevationComplianceFlag(landingThresholdAccuracyComplianceFlag.orElse(null))
        .build();

    return Optional.of(builder);
  }
}
