package org.mitre.boogie.xml.v23_4.convert;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincAirportCommunications;
import org.mitre.boogie.xml.model.ArincGnssLandingSystem;
import org.mitre.boogie.xml.model.ArincHelipad;
import org.mitre.boogie.xml.model.ArincLocalizerGlideSlope;
import org.mitre.boogie.xml.model.ArincLocalizerGlideslopeMarker;
import org.mitre.boogie.xml.model.ArincMsa;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincProcedure;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincTaa;
import org.mitre.boogie.xml.model.fields.ControlledAirspaceIndicator;
import org.mitre.boogie.xml.model.fields.MagneticTrueIndicator;
import org.mitre.boogie.xml.model.fields.PublicMilitaryIndicator;
import org.mitre.boogie.xml.model.fields.UtcOffset;
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
  private static final ArincMsaConverter MSA_CONVERTER = ArincMsaConverter.INSTANCE;
  private static final ArincAirportCommunicationsConverter COMM_CONVERTER = ArincAirportCommunicationsConverter.INSTANCE;
  private static final ArincHelipadConverter HELIPAD_CONVERTER = ArincHelipadConverter.INSTANCE;
  private static final ArincLocalizerGlideslopeMarkerConverter MARKER_CONVERTER = ArincLocalizerGlideslopeMarkerConverter.INSTANCE;
  private static final ArincLocalizerGlideSlopeConverter ILS_CONVERTER = ArincLocalizerGlideSlopeConverter.INSTANCE;
  private static final ArincNdbNavaidConverter NDB_CONVERTER = ArincNdbNavaidConverter.INSTANCE;
  private static final ArincTaaConverter TAA_CONVERTER = ArincTaaConverter.INSTANCE;
  private static final ArincGnssLandingSystemConverter GLS_CONVERTER = ArincGnssLandingSystemConverter.INSTANCE;

  private ArincPortConverter() {
  }

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
    String recommendedNavaidRef = Optional.ofNullable(port.getRecommendedNavaidRef())
        .map(Object::toString)
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

    List<ArincNdbNavaid> ndbs = port.getTerminalNdb().stream()
        .map(NDB_CONVERTER)
        .flatMap(Optional::stream)
        .toList();

    List<ArincProcedure> procs = List.of(); //todo do this

    List<ArincWaypoint> waypoints = port.getTerminalWaypoint().stream()
        .map(WAYPOINT_CONVERTER)
        .flatMap(Optional::stream)
        .toList();

    List<ArincTaa> taas = port.getTaa().stream()
        .map(TAA_CONVERTER)
        .flatMap(Optional::stream)
        .toList();

    List<ArincGnssLandingSystem> gnssLandingSystems = port.getGls().stream()
        .map(GLS_CONVERTER)
        .flatMap(Optional::stream)
        .toList();

    List<ArincAirportCommunications> coms = port.getPortCommunication().stream()
        .map(COMM_CONVERTER)
        .flatMap(Optional::stream)
        .toList();

    List<ArincHelipad> pads = port.getHelipad().stream()
        .map(HELIPAD_CONVERTER)
        .flatMap(Optional::stream)
        .toList();

    List<ArincLocalizerGlideslopeMarker> markers = port.getLocalizerMarker().stream()
        .map(MARKER_CONVERTER)
        .flatMap(Optional::stream)
        .toList();

    List<ArincLocalizerGlideSlope> ils = port.getLocalizerGlideslope().stream()
        .map(ILS_CONVERTER)
        .flatMap(Optional::stream)
        .toList();

    List<ArincMsa> msas = port.getMsa().stream()
        .map(MSA_CONVERTER)
        .flatMap(Optional::stream)
        .toList();

    String controlledAsArptIndicatorRef = Optional.ofNullable(port.getControlledAsArptIndicator())
        .map(Object::toString)
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
        .recommendedNavaidRef(recommendedNavaidRef)
        .speedLimit(speedLimit)
        .speedLimitAltitude(Optional.ofNullable(port.getSpeedLimitAltitude()).map(Constraint::getAltitude).orElse(null))
        .utcOffset(offset)
        .transitionAltitude(port.getTransitionAltitude())
        .ndbNavaid(ndbs)
        .procedures(procs)
        .terminalWaypoints(waypoints)
        .taas(taas)
        .communications(coms)
        .helipads(pads)
        .markers(markers)
        .localizerGlideSlopes(ils)
        .gnssLandingSystems(gnssLandingSystems)
        .msas(msas)
        .vfrCheckpoint(port.isIsVFRCheckpoint())
        .controlledAsArptIndicatorRef(controlledAsArptIndicatorRef)
        .controlledAirspaceIndicator(indicator)
        .build();
  }
}
