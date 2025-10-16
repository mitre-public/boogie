package org.mitre.boogie.xml.v23_4.convert;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.*;
import org.mitre.boogie.xml.model.fields.*;
import org.mitre.boogie.xml.v23_4.generated.A424Point;
import org.mitre.boogie.xml.v23_4.generated.Constraint;
import org.mitre.boogie.xml.v23_4.generated.Port;

import com.google.common.collect.Range;

final class ArincPortConverter implements Function<Port, ArincPortInfo> {
  static final ArincPortConverter INSTANCE = new ArincPortConverter();

  private static final ArincWaypointConverter WAYPOINT_CONVERTER = ArincWaypointConverter.INSTANCE;
  private static final ArincSpeedLimitConverter SPEED_LIMIT_CONVERTER = ArincSpeedLimitConverter.INSTANCE;
  private static final ArincPointConverter POINT_CONVERTER = ArincPointConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;

  private ArincPortConverter() {}

  @Override
  public ArincPortInfo apply(Port port) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(port);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(port);
    ArincPointInfo pointInfo = POINT_CONVERTER.apply(port);
    String magneticTrueIndicator = Optional.ofNullable(port.getMagneticTrueIndicator())
        .map(Enum::name)
        .filter(MagneticTrueIndicator.VALID::contains)
        .map(MagneticTrueIndicator::valueOf)
        .map(Enum::name)
        .orElse(null);
    String publicMilitaryIndicator = Optional.ofNullable(port.getPublicMilitaryIndicator())
        .map(Enum::name)
        .filter(PublicMilitaryIndicator.VALID::contains)
        .map(PublicMilitaryIndicator::valueOf)
        .map(Enum::name)
        .orElse(null);
    ArincPointInfo recNavId = Optional.ofNullable(port.getRecommendedNavaidRef())
        .filter(o -> o.getClass().isAssignableFrom(A424Point.class))
        .map(A424Point.class::cast)
        .map(POINT_CONVERTER)
        .orElse(null);

    Range<Long> speedLimit = Optional.ofNullable(port.getSpeedLimit())
        .map(SPEED_LIMIT_CONVERTER)
        .orElse(Range.all());


    UtcOffset offset = Optional.ofNullable(port.getUtcOffset())
        .filter(org.mitre.boogie.xml.v23_4.generated.UtcOffset::isIsDaylightObserving)
        .map(org.mitre.boogie.xml.v23_4.generated.UtcOffset::getTimeZone)
        .map(t -> UtcOffset.from(t.getHourOffset(), t.getMinuteOffset(), true))
        .or(() -> Optional.ofNullable(port.getUtcOffset())
            .map(org.mitre.boogie.xml.v23_4.generated.UtcOffset::getTimeZone)
            .map(t -> UtcOffset.from(t.getHourOffset(), t.getMinuteOffset(), false)))
        .orElse(null);

    List<ArincNdbNavaid> ndbs = List.of(); //todo do this
    List<ArincProcedure> procs = List.of(); //todo do this

    List<ArincWaypoint> waypoints = port.getTerminalWaypoint().stream()
        .map(WAYPOINT_CONVERTER)
        .flatMap(Optional::stream)
        .toList();

    //todo all these things
    List<ArincTaa>  tas = List.of();
    List<ArincAirportCommunications> coms = List.of();
    List<ArincHelipad> pads = List.of();
    List<ArincLocalizerGlideslopeMarker> markers = List.of();
    List<ArincLocalizerGlideSlope> ils = List.of();
    List<ArincGnssLandingSystem> gnssLandingSystems = List.of();
    List<ArincMsa> msas = List.of();

    ArincPointInfo controlledAirspace = Optional.ofNullable(port.getControlledAsArptIndicator())
        .filter(o -> o.getClass().isAssignableFrom(A424Point.class))
        .map(A424Point.class::cast)
        .map(POINT_CONVERTER)
        .orElse(null);

    String indicator = Optional.ofNullable(port.getControlledAsIndicator())
        .map(Enum::name)
        .filter(ControlledAirspaceIndicator.VALID::contains)
        .map(ControlledAirspaceIndicator::valueOf)
        .map(Enum::name)
        .orElse(null);

    return ArincPortInfo.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .elevation(port.getElevation())
        .ataIataDesignator(port.getAtaIataDesignator())
        .daylightIndicator(port.isDaylightIndicator())
        .ifrCapable(port.isIsIfrCapable())
        .magneticTrueIndicator(magneticTrueIndicator)
        .publicMilitaryIndicator(publicMilitaryIndicator)
        .recommendedNavaidId(recNavId)
        .speedLimit(speedLimit)
        .speedLimitAltitude(Optional.ofNullable(port.getSpeedLimitAltitude()).map(Constraint::getAltitude).orElse(null))
        .utcOffset(offset)
        .transitionAltitude(port.getTransitionAltitude())
        .terminalWaypoints(waypoints)
        .taas(tas)
        .communications(coms)
        .helipads(pads)
        .markers(markers)
        .localizerGlideSlopes(ils)
        .gnssLandingSystems(gnssLandingSystems)
        .msas(msas)
        .vfrCheckpoint(port.isIsVFRCheckpoint())
        .controlledAirspaceId(controlledAirspace)
        .controlledAirspaceIndicator(indicator)
        .build();
  }
}
