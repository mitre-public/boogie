package org.mitre.tdp.boogie.arinc.v21;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.v18.HeliportBuilder;
import org.mitre.tdp.boogie.arinc.v18.HeliportValidator;
import org.mitre.tdp.boogie.arinc.v21.field.HeliportType;

public final class HeliportConverter implements Function<ArincRecord, Optional<ArincHeliport>> {
  private static final HeliportBuilder BUILDER = HeliportBuilder.INSTANCE;
  private static final HeliportValidator VALIDATOR = new HeliportValidator();

  public HeliportConverter() {}

  @Override
  public Optional<ArincHeliport> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Can't convert a null arinc record");
    Optional<HeliportType> type = arincRecord.optionalField("heliportType");
    return Optional.of(arincRecord)
        .filter(VALIDATOR)
        .map(i -> BUILDER.apply(i)
            .heliportType(type.filter(t -> HeliportType.VALID.contains(t.name())).orElse(HeliportType.U))
            .build()
        );
  }
}
