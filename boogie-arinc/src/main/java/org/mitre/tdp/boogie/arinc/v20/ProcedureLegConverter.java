package org.mitre.tdp.boogie.arinc.v20;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegBuilder;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegValidator;
import org.mitre.tdp.boogie.arinc.v20.field.ProcedureDesignAircraftCategoryOrType;

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

    ArincProcedureLeg.Builder builder = ProcedureLegBuilder.INSTANCE.apply(arincRecord)
        .categoryOrType(categoryOrType.map(Enum::name).orElse(null))
        .legInboundOutboundIndicator(legInboundOutboundIndicator.orElse(null));

    return Optional.of(builder.build());
  }
}
