package org.mitre.boogie.xml.v23_4.convert;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.function.Function;

import org.mitre.boogie.xml.v23_4.generated.SpeedLimit;

import com.google.common.collect.Range;

final class ArincSpeedLimitConverter implements Function<SpeedLimit, Range<Long>> {
  static final ArincSpeedLimitConverter INSTANCE = new ArincSpeedLimitConverter();
  private ArincSpeedLimitConverter() {}
  @Override
  public Range<Long> apply(SpeedLimit speedLimit) {
    if (nonNull(speedLimit.getAt())) {
      return Range.closed(speedLimit.getAt(), speedLimit.getAt());
    }

    if (nonNull(speedLimit.getAtOrAbove()) && nonNull(speedLimit.getAtOrBelow())) {
      return Range.closed(speedLimit.getAtOrAbove(), speedLimit.getAtOrBelow());
    }

    if (nonNull(speedLimit.getAtOrAbove())) {
      return Range.atLeast(speedLimit.getAtOrAbove());
    }

    return Range.atMost(speedLimit.getAtOrBelow());
  }
}
