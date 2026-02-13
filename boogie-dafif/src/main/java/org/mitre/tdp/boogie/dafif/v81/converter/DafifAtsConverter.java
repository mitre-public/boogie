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

    String atsIdentifier = dafifRecord.requiredField("atsIdentifier");
    Integer atsRouteSequenceNumber = dafifRecord.requiredField("atsRouteSequenceNumber");
    String atsRouteDirection = dafifRecord.requiredField("atsRouteDirection");
    String atsRouteType = dafifRecord.requiredField("atsRouteType");
    String icaoCode = dafifRecord.requiredField("icaoCode");
    Optional<String> biDirectional = dafifRecord.optionalField("biDirectional");
    String frequencyClass = dafifRecord.requiredField("frequencyClass");
    String level = dafifRecord.requiredField("level");
    String atsRouteStatus = dafifRecord.requiredField("atsRouteStatus");
    String waypoint1IcaoCode = dafifRecord.requiredField("waypoint1IcaoCode");
    Optional<Integer> waypoint1NavaidType = dafifRecord.optionalField("waypoint1NavaidType");
    String waypoint1WaypointIdentifierWptIdent = dafifRecord.requiredField("waypoint1WaypointIdentifierWptIdent");
    String waypoint1CountryCode = dafifRecord.requiredField("waypoint1CountryCode");
    String waypoint1AtsWaypointDescriptionCode1 = dafifRecord.requiredField("waypoint1AtsWaypointDescriptionCode1");
    Optional<String> waypoint1AtsWaypointDescriptionCode2 = dafifRecord.optionalField("waypoint1AtsWaypointDescriptionCode2");
    Optional<String> waypoint1AtsWaypointDescriptionCode3 = dafifRecord.optionalField("waypoint1AtsWaypointDescriptionCode3");
    Optional<String> waypoint1AtsWaypointDescriptionCode4 = dafifRecord.optionalField("waypoint1AtsWaypointDescriptionCode4");
    Optional<String> waypoint1GeodeticLatitude = dafifRecord.optionalField("waypoint1GeodeticLatitude");
    Optional<Double> waypoint1DegreesLatitude = dafifRecord.optionalField("waypoint1DegreesLatitude");
    Optional<String> waypoint1GeodeticLongitude = dafifRecord.optionalField("waypoint1GeodeticLongitude");
    Optional<Double> waypoint1DegreesLongitude = dafifRecord.optionalField("waypoint1DegreesLongitude");
    String waypoint2IcaoCode = dafifRecord.requiredField("waypoint2IcaoCode");
    Optional<Integer> waypoint2NavaidType = dafifRecord.optionalField("waypoint2NavaidType");
    String waypoint2WaypointIdentifierWptIdent = dafifRecord.requiredField("waypoint2WaypointIdentifierWptIdent");
    String waypoint2CountryCode = dafifRecord.requiredField("waypoint2CountryCode");
    String waypoint2AtsWaypointDescriptionCode1 = dafifRecord.requiredField("waypoint2AtsWaypointDescriptionCode1");
    Optional<String> waypoint2AtsWaypointDescriptionCode2 = dafifRecord.optionalField("waypoint2AtsWaypointDescriptionCode2");
    Optional<String> waypoint2AtsWaypointDescriptionCode3 = dafifRecord.optionalField("waypoint2AtsWaypointDescriptionCode3");
    Optional<String> waypoint2AtsWaypointDescriptionCode4 = dafifRecord.optionalField("waypoint2AtsWaypointDescriptionCode4");
    Optional<String> waypoint2GeodeticLatitude = dafifRecord.optionalField("waypoint2GeodeticLatitude");
    Optional<Double> waypoint2DegreesLatitude = dafifRecord.optionalField("waypoint2DegreesLatitude");
    Optional<String> waypoint2GeodeticLongitude = dafifRecord.optionalField("waypoint2GeodeticLongitude");
    Optional<Double> waypoint2DegreesLongitude = dafifRecord.optionalField("waypoint2DegreesLongitude");
    Optional<String> atsRouteOutboundMagneticCourse = dafifRecord.optionalField("atsRouteOutboundMagneticCourse");
    Optional<Double> atsRouteDistance = dafifRecord.optionalField("atsRouteDistance");
    Optional<String> atsRouteInboundMagneticCourse = dafifRecord.optionalField("atsRouteInboundMagneticCourse");
    Optional<String> minimumAltitude = dafifRecord.optionalField("minimumAltitude");
    Optional<String> upperLimit = dafifRecord.optionalField("upperLimit");
    Optional<String> lowerLimit = dafifRecord.optionalField("lowerLimit");
    Optional<String> maxAuthorizedAltitude = dafifRecord.optionalField("maxAuthorizedAltitude");
    Optional<String> cruiseLevelIndicator = dafifRecord.optionalField("cruiseLevelIndicator");
    Optional<Integer> requiredNavPerformance = dafifRecord.optionalField("requiredNavPerformance");
    Integer cycleDate = dafifRecord.requiredField("cycleDate");
    Optional<String> atsDesignator = dafifRecord.optionalField("atsDesignator");

    DafifAirTrafficSegment airTrafficService = new DafifAirTrafficSegment.Builder()
        .atsIdentifier(atsIdentifier)
        .atsRouteSequenceNumber(atsRouteSequenceNumber)
        .atsRouteDirection(atsRouteDirection)
        .atsRouteType(atsRouteType)
        .icaoCode(icaoCode)
        .biDirectional(biDirectional.orElse(null))
        .frequencyClass(frequencyClass)
        .level(level)
        .atsRouteStatus(atsRouteStatus)
        .waypoint1IcaoCode(waypoint1IcaoCode)
        .waypoint1NavaidType(waypoint1NavaidType.orElse(null))
        .waypoint1WaypointIdentifierWptIdent(waypoint1WaypointIdentifierWptIdent)
        .waypoint1CountryCode(waypoint1CountryCode)
        .waypoint1AtsWaypointDescriptionCode1(waypoint1AtsWaypointDescriptionCode1)
        .waypoint1AtsWaypointDescriptionCode2(waypoint1AtsWaypointDescriptionCode2.orElse(null))
        .waypoint1AtsWaypointDescriptionCode3(waypoint1AtsWaypointDescriptionCode3.orElse(null))
        .waypoint1AtsWaypointDescriptionCode4(waypoint1AtsWaypointDescriptionCode4.orElse(null))
        .waypoint1GeodeticLatitude(waypoint1GeodeticLatitude.orElse(null))
        .waypoint1DegreesLatitude(waypoint1DegreesLatitude.orElse(null))
        .waypoint1GeodeticLongitude(waypoint1GeodeticLongitude.orElse(null))
        .waypoint1DegreesLongitude(waypoint1DegreesLongitude.orElse(null))
        .waypoint2IcaoCode(waypoint2IcaoCode)
        .waypoint2NavaidType(waypoint2NavaidType.orElse(null))
        .waypoint2WaypointIdentifierWptIdent(waypoint2WaypointIdentifierWptIdent)
        .waypoint2CountryCode(waypoint2CountryCode)
        .waypoint2AtsWaypointDescriptionCode1(waypoint2AtsWaypointDescriptionCode1)
        .waypoint2AtsWaypointDescriptionCode2(waypoint2AtsWaypointDescriptionCode2.orElse(null))
        .waypoint2AtsWaypointDescriptionCode3(waypoint2AtsWaypointDescriptionCode3.orElse(null))
        .waypoint2AtsWaypointDescriptionCode4(waypoint2AtsWaypointDescriptionCode4.orElse(null))
        .waypoint2GeodeticLatitude(waypoint2GeodeticLatitude.orElse(null))
        .waypoint2DegreesLatitude(waypoint2DegreesLatitude.orElse(null))
        .waypoint2GeodeticLongitude(waypoint2GeodeticLongitude.orElse(null))
        .waypoint2DegreesLongitude(waypoint2DegreesLongitude.orElse(null))
        .atsRouteOutboundMagneticCourse(atsRouteOutboundMagneticCourse.orElse(null))
        .atsRouteDistance(atsRouteDistance.orElse(null))
        .atsRouteInboundMagneticCourse(atsRouteInboundMagneticCourse.orElse(null))
        .minimumAltitude(minimumAltitude.orElse(null))
        .upperLimit(upperLimit.orElse(null))
        .lowerLimit(lowerLimit.orElse(null))
        .maxAuthorizedAltitude(maxAuthorizedAltitude.orElse(null))
        .cruiseLevelIndicator(cruiseLevelIndicator.orElse(null))
        .requiredNavPerformance(requiredNavPerformance.orElse(null))
        .cycleDate(cycleDate)
        .atsDesignator(atsDesignator.orElse(null))
        .build();

    return Optional.of(airTrafficService);
  }
}
