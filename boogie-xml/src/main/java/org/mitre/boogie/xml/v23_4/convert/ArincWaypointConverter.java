package org.mitre.boogie.xml.v23_4.convert;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.fields.*;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.util.Coordinate;
import org.mitre.boogie.xml.util.DecimalDegrees;
import org.mitre.boogie.xml.util.MagVar;
import org.mitre.boogie.xml.util.MagneticVariationConverter;
import org.mitre.boogie.xml.v23_4.generated.Waypoint;
import org.mitre.boogie.xml.v23_4.util.FraConverter;
import org.mitre.boogie.xml.v23_4.util.WaypointTypeConverter;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.MagneticVariation;

public final class ArincWaypointConverter implements Function<Waypoint, Optional<ArincWaypoint>>, Serializable {
  public static final ArincWaypointConverter INSTANCE = new ArincWaypointConverter();

  private static final DecimalDegrees DECIMAL_DEGREES = DecimalDegrees.INSTANCE;
  private static final MagneticVariationConverter MAGNETIC_VARIATION_CONVERTER = MagneticVariationConverter.INSTANCE;
  private static final WaypointTypeConverter WAYPOINT_TYPE_CONVERTER = WaypointTypeConverter.INSTANCE;
  private static final FraConverter FRA_CONVERTER = FraConverter.INSTANCE;

  private  ArincWaypointConverter() {
  }

  @Override
  public Optional<ArincWaypoint> apply(Waypoint waypoint) {
    return Optional.ofNullable(waypoint)
        .filter(ArincWaypointValidator.INSTANCE)
        .map(this::convert);
  }

  private ArincWaypoint convert(Waypoint waypoint) {
    ArincBaseInfo baseInfo = Optional.ofNullable(waypoint.getSupplementalData())
        .map(SupplementalData::record)
        .map(ArincBaseInfo::from)
        .orElse(null);

    ArincRecordInfo recordInfo = ArincRecordInfo.builder()
        .recordType(Optional.of(waypoint.getRecordType()).map(Enum::name).map(ArincRecordType::valueOf).orElseThrow(() -> new IllegalStateException("Record Type was required: " + waypoint)))
        .areaCode(Optional.ofNullable(waypoint.getAreaCode()).map(Enum::name).orElse(null))
        .customerCode(waypoint.getCustomerCode())
        .notes(waypoint.getNotes())
        .build();

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

    ArincPointInfo pointInfo = ArincPointInfo.builder()
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

    ArincNameFormatIndicator nameFormatIndicator = Optional.ofNullable(waypoint.getNameFormatIndicator())
        .map(Enum::name)
        .filter(ArincNameFormatIndicator.VALID::contains)
        .map(ArincNameFormatIndicator::valueOf)
        .orElse(null);

    ArincWaypointType type = Optional.ofNullable(waypoint.getWaypointType())
        .map(WAYPOINT_TYPE_CONVERTER)
        .orElse(null);

    ArincWaypointUsage use = Optional.ofNullable(waypoint.getWaypointUsage())
        .map(u -> ArincWaypointUsage.of(u.isIsHi(), u.isIsLo(), u.isIsTerminal()))
        .orElse(null);

    ArincFraInfo fra = Optional.ofNullable(waypoint.getFraInfo())
        .map(FRA_CONVERTER)
        .orElse(null);

    Boolean vfrCheckpoint = Optional.ofNullable(waypoint.isIsVFRCheckpoint()).orElse(false);

    return ArincWaypoint.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .nameFormatIndicator(nameFormatIndicator)
        .waypointType(type)
        .waypointUsage(use)
        .fraInfo(fra)
        .vfrCheckPoint(vfrCheckpoint)
        .build();
  }
}
