package org.mitre.boogie.xml.v23_5.convert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.fields.ArincDatumCode;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.v23_5.util.LocationConverter;
import org.mitre.boogie.xml.util.MagVar;
import org.mitre.boogie.xml.util.MagneticVariationConverter;
import org.mitre.boogie.xml.v23_5.generated.A424Point;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.MagneticVariation;

final class ArincPointConverter implements Function<A424Point, ArincPointInfo> {
  static final ArincPointConverter INSTANCE = new ArincPointConverter();

  private static final LocationConverter LOCATION_CONVERTER = LocationConverter.INSTANCE;
  private static final MagneticVariationConverter MAGNETIC_VARIATION_CONVERTER = MagneticVariationConverter.INSTANCE;

  private ArincPointConverter() {
  }

  @Override
  public ArincPointInfo apply(A424Point waypoint) {
    LatLong latLong = LOCATION_CONVERTER.apply(waypoint.getLocation()).orElse(null);

    MagneticVariation magvar = Optional.ofNullable(waypoint.getMagneticVariation())
        .map(m -> MagVar.from(m.getMagneticVariationEWT().name(), Optional.ofNullable(m.getMagneticVariationValue()).map(BigDecimal::doubleValue).orElse(0.0)))
        .flatMap(MAGNETIC_VARIATION_CONVERTER)
        .orElse(null);

    return ArincPointInfo.builder()
        .fir(Optional.ofNullable(waypoint.getFirRef()).map(Object::toString).orElse(null))
        .uir(Optional.ofNullable(waypoint.getUirRef()).map(Object::toString).orElse(null))
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
