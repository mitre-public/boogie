package org.mitre.boogie.xml.v23_4.util;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.util.Coordinate;
import org.mitre.boogie.xml.util.DecimalDegrees;
import org.mitre.boogie.xml.v23_4.generated.Location;
import org.mitre.caasd.commons.LatLong;

public final class LocationConverter implements Function<Location, Optional<LatLong>> {
  public static final LocationConverter INSTANCE = new LocationConverter();

  private static final DecimalDegrees DECIMAL_DEGREES = DecimalDegrees.INSTANCE;

  private LocationConverter() {
  }

  @Override
  public Optional<LatLong> apply(Location location) {
    Double latitude = Optional.of(location.getLatitude())
        .map(c -> Coordinate.from(c.getNorthSouth().name(), c.getDeg(), c.getMin(), c.getSec(), c.getHSec()))
        .map(DECIMAL_DEGREES)
        .or(() -> Optional.ofNullable(location.getLatitude().getDecimalDegreesLatitude()).map(BigDecimal::doubleValue))
        .orElseThrow(() -> new IllegalStateException("Location's Latitude was required: " + location));

    Double longitude = Optional.of(location.getLongitude())
        .map(c -> Coordinate.from(c.getEastWest().name(), c.getDeg(), c.getMin(), c.getSec(), c.getHSec()))
        .map(DECIMAL_DEGREES)
        .or(() -> Optional.ofNullable(location.getLongitude().getDecimalDegreesLongitude()).map(BigDecimal::doubleValue))
        .orElseThrow(() -> new IllegalStateException("Location's Longitude was required: " + location));

    if (latitude < -90.0 || latitude > 90.0 || longitude < -180.0 || longitude > 180.0) {
      return Optional.empty();
    }

    return Optional.of(LatLong.of(latitude, longitude));
  }
}
