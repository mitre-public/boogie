package org.mitre.tdp.boogie.dafif.v81.converter;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class DafifAtsConverter implements Function<DafifRecord, Optional<DafifAirTrafficSegment>> {

  @Override
  public Optional<DafifAirTrafficSegment> apply(DafifRecord dafifRecord) {
    requireNonNull(dafifRecord, "Cannot convert null DafifRecord.");

    return Optional.of(new DafifAirTrafficSegment.Builder()
        .atsIdentifier(dafifRecord.requiredField("atsIdentifier"))
        .atsRouteSequenceNumber(dafifRecord.requiredField("atsRouteSequenceNumber"))
        .atsRouteDirection(dafifRecord.requiredField("atsRouteDirection"))
        .atsRouteType(dafifRecord.requiredField("atsRouteType"))
        .icaoCode(dafifRecord.requiredField("icaoCode"))
        .biDirectional(dafifRecord.<String>optionalField("biDirectional").orElse(null))
        .frequencyClass(dafifRecord.requiredField("frequencyClass"))
        .level(dafifRecord.requiredField("level"))
        .atsRouteStatus(dafifRecord.requiredField("atsRouteStatus"))
        .waypoint1IcaoCode(dafifRecord.requiredField("waypoint1IcaoCode"))
        .waypoint1NavaidType(dafifRecord.<Integer>optionalField("waypoint1NavaidType").orElse(null))
        .waypoint1WaypointIdentifierWptIdent(dafifRecord.requiredField("waypoint1WaypointIdentifierWptIdent"))
        .waypoint1CountryCode(dafifRecord.requiredField("waypoint1CountryCode"))
        .waypoint1AtsWaypointDescriptionCode1(dafifRecord.requiredField("waypoint1AtsWaypointDescriptionCode1"))
        .waypoint1AtsWaypointDescriptionCode2(dafifRecord.<String>optionalField("waypoint1AtsWaypointDescriptionCode2").orElse(null))
        .waypoint1AtsWaypointDescriptionCode3(dafifRecord.<String>optionalField("waypoint1AtsWaypointDescriptionCode3").orElse(null))
        .waypoint1AtsWaypointDescriptionCode4(dafifRecord.<String>optionalField("waypoint1AtsWaypointDescriptionCode4").orElse(null))
        .waypoint1GeodeticLatitude(dafifRecord.<String>optionalField("waypoint1GeodeticLatitude").orElse(null))
        .waypoint1DegreesLatitude(dafifRecord.<Double>optionalField("waypoint1DegreesLatitude").orElse(null))
        .waypoint1GeodeticLongitude(dafifRecord.<String>optionalField("waypoint1GeodeticLongitude").orElse(null))
        .waypoint1DegreesLongitude(dafifRecord.<Double>optionalField("waypoint1DegreesLongitude").orElse(null))
        .waypoint2IcaoCode(dafifRecord.requiredField("waypoint2IcaoCode"))
        .waypoint2NavaidType(dafifRecord.<Integer>optionalField("waypoint2NavaidType").orElse(null))
        .waypoint2WaypointIdentifierWptIdent(dafifRecord.requiredField("waypoint2WaypointIdentifierWptIdent"))
        .waypoint2CountryCode(dafifRecord.requiredField("waypoint2CountryCode"))
        .waypoint2AtsWaypointDescriptionCode1(dafifRecord.requiredField("waypoint2AtsWaypointDescriptionCode1"))
        .waypoint2AtsWaypointDescriptionCode2(dafifRecord.<String>optionalField("waypoint2AtsWaypointDescriptionCode2").orElse(null))
        .waypoint2AtsWaypointDescriptionCode3(dafifRecord.<String>optionalField("waypoint2AtsWaypointDescriptionCode3").orElse(null))
        .waypoint2AtsWaypointDescriptionCode4(dafifRecord.<String>optionalField("waypoint2AtsWaypointDescriptionCode4").orElse(null))
        .waypoint2GeodeticLatitude(dafifRecord.<String>optionalField("waypoint2GeodeticLatitude").orElse(null))
        .waypoint2DegreesLatitude(dafifRecord.<Double>optionalField("waypoint2DegreesLatitude").orElse(null))
        .waypoint2GeodeticLongitude(dafifRecord.<String>optionalField("waypoint2GeodeticLongitude").orElse(null))
        .waypoint2DegreesLongitude(dafifRecord.<Double>optionalField("waypoint2DegreesLongitude").orElse(null))
        .atsRouteOutboundMagneticCourse(dafifRecord.<String>optionalField("atsRouteOutboundMagneticCourse").orElse(null))
        .atsRouteDistance(dafifRecord.<Double>optionalField("atsRouteDistance").orElse(null))
        .atsRouteInboundMagneticCourse(dafifRecord.<String>optionalField("atsRouteInboundMagneticCourse").orElse(null))
        .minimumAltitude(dafifRecord.<String>optionalField("minimumAltitude").orElse(null))
        .upperLimit(dafifRecord.<String>optionalField("upperLimit").orElse(null))
        .lowerLimit(dafifRecord.<String>optionalField("lowerLimit").orElse(null))
        .maxAuthorizedAltitude(dafifRecord.<String>optionalField("maxAuthorizedAltitude").orElse(null))
        .cruiseLevelIndicator(dafifRecord.<String>optionalField("cruiseLevelIndicator").orElse(null))
        .requiredNavPerformance(dafifRecord.<Integer>optionalField("requiredNavPerformance").orElse(null))
        .cycleDate(dafifRecord.requiredField("cycleDate"))
        .atsDesignator(dafifRecord.<String>optionalField("atsDesignator").orElse(null))
        .build());
  }
}
