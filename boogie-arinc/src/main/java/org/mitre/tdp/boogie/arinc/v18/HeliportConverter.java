package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.utils.Dimensions;
import org.mitre.tdp.boogie.arinc.v21.field.PadShape;

/**
 * Converter class for functionally transforming a {@link ArincRecord} into a {@link ArincHeliport} concrete data model.
 * <br>
 * If the input record does not pass the {@link HeliportValidator#test(ArincRecord)} call then this function returns {@link Optional#empty()}.
 */
public final class HeliportConverter implements Function<ArincRecord, Optional<ArincHeliport>> {
  private static final HeliportValidator VALIDATOR = new HeliportValidator();
  private static final HeliportBuilder BUILDER = HeliportBuilder.INSTANCE;

  @Override
  public Optional<ArincHeliport> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot convert null record");
    Optional<String> padIdentifier = arincRecord.optionalField("helipadIdentifier");
    Optional<String> rawDimensions = arincRecord.optionalField("padDimensions");
    Optional<PadShape> padShape = rawDimensions.map(PadShapeExtractor.INSTANCE);
    Optional<Dimensions> dimensions = rawDimensions.filter(d -> padShape.isPresent()).flatMap(raw -> DimensionsParser.INSTANCE.apply(raw, padShape.orElseThrow()));
    return Optional.of(arincRecord)
        .filter(VALIDATOR)
        .map(i -> BUILDER.apply(i)
            .padIdentifier(padIdentifier.orElse(null))
            .padShape(padShape.map(Enum::name).orElse(null))
            .padXDimension(dimensions.flatMap(Dimensions::xPossible).orElse(null))
            .padYDimension(dimensions.flatMap(Dimensions::yPossible).orElse(null))
            .padDiameter(dimensions.flatMap(Dimensions::diameterPossible).orElse(null))
            .build()
        );
  }
}
