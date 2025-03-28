package org.mitre.tdp.boogie.arinc.v22;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegBuilder;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegValidator;
import org.mitre.tdp.boogie.arinc.v22.field.ProcedureDesignAircraftCategoryOrType;

/**
 * This one is mostly the same as the -21 but the enum changed which means we need new processing
 */
public final class ProcedureLegConverter implements Function<ArincRecord, Optional<ArincProcedureLeg>> {
  private static final Predicate<ArincRecord> isInvalidRecord = new ProcedureLegValidator().negate();
  @Override
  public Optional<ArincProcedureLeg> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Input ArincRecord cannot be null.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<ProcedureDesignAircraftCategoryOrType> categoryOrType = arincRecord.optionalField("procedureDesignAircraftCategoryOrType");
    Optional<String> legInboundOutboundIndicator = arincRecord.optionalField("legInboundOutboundIndicator");
    Optional<String> gnssFmsIndicator = arincRecord.optionalField("gnssFmsIndicator");
    Optional<String> routeTypeQualifier3 = arincRecord.optionalField("routeTypeQualifier3");
    Optional<String> preferredMultipleApproachIndicator = arincRecord.optionalField("preferredMultipleApproachIndicator");

    ArincProcedureLeg.Builder builder = ProcedureLegBuilder.INSTANCE.apply(arincRecord)
        .categoryOrType(categoryOrType.map(Enum::name).orElse(null))
        .legInboundOutboundIndicator(legInboundOutboundIndicator.orElse(null))
        .gnssFmsIndicator(gnssFmsIndicator.orElse(null))
        .routeTypeQualifier3(routeTypeQualifier3.orElse(null))
        .preferredMultipleApproachIndicator(preferredMultipleApproachIndicator.orElse(null));

    return Optional.of(builder.build());
  }
}
