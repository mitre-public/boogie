package org.mitre.boogie.xml.v23_4.convert;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.v23_4.generated.HoldRvsmMinimumMaximumAltitudeConstraint;

final class FlatMinMaxAltitudeConstraintConverter implements Function<HoldRvsmMinimumMaximumAltitudeConstraint, Optional<FlatMinMaxAltitudeConstraint>> {
  static final FlatMinMaxAltitudeConstraintConverter INSTANCE = new FlatMinMaxAltitudeConstraintConverter();

  private FlatMinMaxAltitudeConstraintConverter() {
  }

  @Override
  public Optional<FlatMinMaxAltitudeConstraint> apply(HoldRvsmMinimumMaximumAltitudeConstraint constraint) {
    return Optional.ofNullable(constraint)
        .map(c -> new FlatMinMaxAltitudeConstraint(
            Optional.ofNullable(c.getMinimumAltitude()).map(a -> a.getAltitude()).orElse(null),
            Optional.ofNullable(c.getMaximumAltitude()).map(a -> a.getAltitude()).orElse(null)));
  }
}
