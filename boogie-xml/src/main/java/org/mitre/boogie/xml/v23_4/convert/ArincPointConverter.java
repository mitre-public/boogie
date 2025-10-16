package org.mitre.boogie.xml.v23_4.convert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.fields.ArincDatumCode;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.util.Coordinate;
import org.mitre.boogie.xml.util.DecimalDegrees;
import org.mitre.boogie.xml.util.MagVar;
import org.mitre.boogie.xml.util.MagneticVariationConverter;
import org.mitre.boogie.xml.v23_4.generated.A424Point;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.MagneticVariation;

final class ArincPointConverter implements Function<A424Point, ArincPointInfo> {
  static final ArincPointConverter INSTANCE = new ArincPointConverter();

  private static final DecimalDegrees DECIMAL_DEGREES = DecimalDegrees.INSTANCE;
  private static final MagneticVariationConverter MAGNETIC_VARIATION_CONVERTER = MagneticVariationConverter.INSTANCE;

  private ArincPointConverter() {}

  @Override
  public ArincPointInfo apply(A424Point waypoint) {
    Double latitude = Optional.of(waypoint.getLocation().getLatitude())
        .map(c -> Coordinate.from(c.getNorthSouth().name(), c.getDeg(), c.getMin(), c.getSec(), c.getHSec()))
        .map(DECIMAL_DEGREES)
        .or(() -> Optional.ofNullable(waypoint.getLocation().getLatitude().getDecimalDegreesLatitude()).map(BigDecimal::doubleValue))
        .orElseThrow(() -> new IllegalStateException("Location's Latitude was required: " + waypoint.getLocation()));

    Double longitude = Optional.of(waypoint.getLocation().getLongitude())
        .map(c -> Coordinate.from(c.getEastWest().name(), c.getDeg(), c.getMin(), c.getSec(), c.getHSec()))
        .map(DECIMAL_DEGREES)
        .or(() -> Optional.ofNullable(waypoint.getLocation().getLongitude().getDecimalDegreesLongitude()).map(BigDecimal::doubleValue))
        .orElseThrow(() -> new IllegalStateException("Location's Longitude was required: " + waypoint.getLocation()));

    LatLong latLong = LatLong.of(latitude, longitude);

    MagneticVariation magvar = Optional.ofNullable(waypoint.getMagneticVariation())
        .map(m -> MagVar.from(m.getMagneticVariationEWT().name(), Optional.ofNullable(m.getMagneticVariationValue()).map(BigDecimal::doubleValue).orElse(0.0)))
        .flatMap(MAGNETIC_VARIATION_CONVERTER)
        .orElse(null);

    return ArincPointInfo.builder()
        .fir(null) //todo get bindings working
        .uir(null) //todo get bindings working
        .datumCode(Optional.ofNullable(waypoint.getDatumCode()).map(Enum::name).filter(ArincDatumCode.VALID::contains).orElse(null))
        .icaoCode(waypoint.getIcaoCode())
        .identifier(waypoint.getIdentifier())
        .magneticVariation(magvar)
        .latLong(latLong)
        .name(waypoint.getName())
        .referenceId(waypoint.getReferenceId())
        .build();
  }
}
