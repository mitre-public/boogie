package org.mitre.boogie.xml.v23_5.convert;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.v23_5.generated.HoldRvsmMinimumMaximumAltitudeConstraint;

final class FlatMinMaxAltitudeConstraintConverter implements Function<HoldRvsmMinimumMaximumAltitudeConstraint, Optional<FlatMinMaxAltitudeConstraint>> {
  static final FlatMinMaxAltitudeConstraintConverter INSTANCE = new FlatMinMaxAltitudeConstraintConverter();

  private FlatMinMaxAltitudeConstraintConverter() {
  }

  @Override
  public Optional<FlatMinMaxAltitudeConstraint> apply(HoldRvsmMinimumMaximumAltitudeConstraint constraint) {
    return Optional.ofNullable(constraint)
        .map(c -> new FlatMinMaxAltitudeConstraint(
            Optional.ofNullable(c.getMinimumAltitude()).map(ConstraintAltitudeResolver::resolve).orElse(null),
            Optional.ofNullable(c.getMaximumAltitude()).map(ConstraintAltitudeResolver::resolve).orElse(null)));
  }

}
