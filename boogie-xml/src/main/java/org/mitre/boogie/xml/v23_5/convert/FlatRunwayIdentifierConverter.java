package org.mitre.boogie.xml.v23_5.convert;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.fields.RunwayLeftRightCenterType;
import org.mitre.boogie.xml.model.fields.RunwaySuffix;
import org.mitre.boogie.xml.v23_5.generated.RunwayIdentifier;

final class FlatRunwayIdentifierConverter implements Function<RunwayIdentifier, Optional<FlatRunwayIdentifier>> {
  static final FlatRunwayIdentifierConverter INSTANCE = new FlatRunwayIdentifierConverter();

  private FlatRunwayIdentifierConverter() {
  }

  @Override
  public Optional<FlatRunwayIdentifier> apply(RunwayIdentifier ri) {
    return Optional.ofNullable(ri)
        .map(r -> new FlatRunwayIdentifier(
            (int) r.getRunwayNumber(),
            Optional.ofNullable(r.getRunwayLeftRightCenterType()).map(Enum::name).filter(RunwayLeftRightCenterType.VALID::contains).orElse(null),
            Optional.ofNullable(r.getRunwaySuffix()).map(Enum::name).filter(RunwaySuffix.VALID::contains).orElse(null)));
  }
}
