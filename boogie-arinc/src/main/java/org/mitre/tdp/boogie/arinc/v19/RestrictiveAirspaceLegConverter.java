package org.mitre.tdp.boogie.arinc.v19;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincRestrictiveAirspaceLeg;
import org.mitre.tdp.boogie.arinc.v18.RestrictiveAirspaceLegBuilder;
import org.mitre.tdp.boogie.arinc.v18.RestrictiveAirspaceValidator;
import org.mitre.tdp.boogie.arinc.v19.field.RestrictiveType;

import static java.util.Objects.requireNonNull;

public final class RestrictiveAirspaceLegConverter implements Function<ArincRecord, Optional<ArincRestrictiveAirspaceLeg>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new RestrictiveAirspaceValidator().negate();

  @Override
  public Optional<ArincRestrictiveAirspaceLeg> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot convert null ArincRecord.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    RestrictiveType restrictiveType = arincRecord.requiredField("restrictiveType");

    ArincRestrictiveAirspaceLeg restrictiveAirspace = RestrictiveAirspaceLegBuilder.INSTANCE.apply(arincRecord)
        .restrictiveType(restrictiveType.name())
        .build();

    return Optional.of(restrictiveAirspace);
  }
}
