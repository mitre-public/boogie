package org.mitre.boogie.xml.v23_4.convert;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.fields.*;
import org.mitre.boogie.xml.v23_4.generated.Waypoint;
import org.mitre.boogie.xml.v23_4.util.FraConverter;
import org.mitre.boogie.xml.v23_4.util.WaypointTypeConverter;

public final class ArincWaypointConverter implements Function<Waypoint, Optional<ArincWaypoint>>, Serializable {
  public static final ArincWaypointConverter INSTANCE = new ArincWaypointConverter();

  private static final ArincPointConverter POINT_CONVERTER = ArincPointConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;

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
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(waypoint);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(waypoint);
    ArincPointInfo pointInfo = POINT_CONVERTER.apply(waypoint);

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
