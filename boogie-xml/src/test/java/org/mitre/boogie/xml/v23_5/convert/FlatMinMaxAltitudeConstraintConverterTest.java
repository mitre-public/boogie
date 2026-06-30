package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_5.generated.HoldRvsmMinimumMaximumAltitudeConstraint;
import org.mitre.boogie.xml.v23_5.generated.RouteMaximumAltitude;
import org.mitre.boogie.xml.v23_5.generated.RouteMinimumAltitude;

class FlatMinMaxAltitudeConstraintConverterTest {
  private final FlatMinMaxAltitudeConstraintConverter converter = FlatMinMaxAltitudeConstraintConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    Optional<FlatMinMaxAltitudeConstraint> result = converter.apply(null);
    assertTrue(result.isEmpty());
  }

  @Test
  void convertsValidConstraint() {
    HoldRvsmMinimumMaximumAltitudeConstraint constraint = new HoldRvsmMinimumMaximumAltitudeConstraint();

    RouteMinimumAltitude min = new RouteMinimumAltitude();
    min.setAltitude(5000);
    constraint.setMinimumAltitude(min);

    RouteMaximumAltitude max = new RouteMaximumAltitude();
    max.setAltitude(8000);
    constraint.setMaximumAltitude(max);

    FlatMinMaxAltitudeConstraint result = converter.apply(constraint).orElseThrow();
    assertAll(
        () -> assertEquals(5000, result.minimumAltitude()),
        () -> assertEquals(8000, result.maximumAltitude())
    );
  }

  @Test
  void nullFieldsInsideConstraint() {
    HoldRvsmMinimumMaximumAltitudeConstraint constraint = new HoldRvsmMinimumMaximumAltitudeConstraint();

    FlatMinMaxAltitudeConstraint result = converter.apply(constraint).orElseThrow();
    assertAll(
        () -> assertNull(result.minimumAltitude()),
        () -> assertNull(result.maximumAltitude())
    );
  }

  @Test
  void onlyMinimumSet() {
    HoldRvsmMinimumMaximumAltitudeConstraint constraint = new HoldRvsmMinimumMaximumAltitudeConstraint();

    RouteMinimumAltitude min = new RouteMinimumAltitude();
    min.setAltitude(5000);
    constraint.setMinimumAltitude(min);

    FlatMinMaxAltitudeConstraint result = converter.apply(constraint).orElseThrow();
    assertAll(
        () -> assertEquals(5000, result.minimumAltitude()),
        () -> assertNull(result.maximumAltitude())
    );
  }

  @Test
  void onlyMaximumSet() {
    HoldRvsmMinimumMaximumAltitudeConstraint constraint = new HoldRvsmMinimumMaximumAltitudeConstraint();

    RouteMaximumAltitude max = new RouteMaximumAltitude();
    max.setAltitude(8000);
    constraint.setMaximumAltitude(max);

    FlatMinMaxAltitudeConstraint result = converter.apply(constraint).orElseThrow();
    assertAll(
        () -> assertNull(result.minimumAltitude()),
        () -> assertEquals(8000, result.maximumAltitude())
    );
  }
}
