package org.mitre.tdp.boogie.dafif.v81.converter;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class DafifTerminalSegmentConverter implements Function<DafifRecord, Optional<DafifTerminalSegment>> {

  @Override
  public Optional<DafifTerminalSegment> apply(DafifRecord dafifRecord) {
    requireNonNull(dafifRecord, "Cannot convert null DafifRecord.");

    String airportIdentification = dafifRecord.requiredField("airportIdentification");
    Integer terminalProcedureType = dafifRecord.requiredField("terminalProcedureType");
    String terminalIdentifier = dafifRecord.requiredField("terminalIdentifier");
    Integer terminalSequenceNumber = dafifRecord.requiredField("terminalSequenceNumber");
    String terminalApproachType = dafifRecord.requiredField("terminalApproachType");
    String transitionIdentifier = dafifRecord.<String>optionalField("transitionIdentifier").orElse(null);
    String icaoCode = dafifRecord.requiredField("icaoCode");
    String trackDescriptionCode = dafifRecord.requiredField("trackDescriptionCode");
    Optional<String> termSegWaypointIdentifier = dafifRecord.optionalField("termSegWaypointIdentifier");
    Optional<String> waypointCountryCode = dafifRecord.optionalField("waypointCountryCode");
    Optional<String> terminalWaypointDescriptionCode1Arpt = dafifRecord.optionalField("terminalWaypointDescriptionCode1Arpt");
    Optional<String> terminalWaypointDescriptionCode2 = dafifRecord.optionalField("terminalWaypointDescriptionCode2");
    Optional<String> terminalWaypointDescriptionCode3 = dafifRecord.optionalField("terminalWaypointDescriptionCode3");
    Optional<String> terminalWaypointDescriptionCode4 = dafifRecord.optionalField("terminalWaypointDescriptionCode4");
    Optional<String> terminalSegmentTurnDirection = dafifRecord.optionalField("terminalSegmentTurnDirection");
    Optional<String> navaid1Identifier = dafifRecord.optionalField("navaid1Identifier");
    Optional<String> navaid1Type = dafifRecord.optionalField("navaid1Type");
    Optional<String> navaid1CountryCode = dafifRecord.optionalField("navaid1CountryCode");
    Optional<Integer> navaid1KeyCode = dafifRecord.optionalField("navaid1KeyCode");
    Optional<Double> fix1Bearing = dafifRecord.optionalField("fix1Bearing");
    Optional<Double> fix1Distance = dafifRecord.optionalField("fix1Distance");
    Optional<String> navaid2Identifier = dafifRecord.optionalField("navaid2Identifier");
    Optional<String> navaid2Type = dafifRecord.optionalField("navaid2Type");
    Optional<String> navaid2CountryCode = dafifRecord.optionalField("navai2CountryCode");
    Optional<Integer> navaid2KeyCode = dafifRecord.optionalField("navaid2KeyCode");
    Optional<Double> fix2Bearing = dafifRecord.optionalField("fix2Bearing");
    Optional<Double> fix2Distance = dafifRecord.optionalField("fix2Distance");
    Optional<String> terminalMagneticCourse = dafifRecord.optionalField("terminalMagneticCourse");
    Optional<Double> distance = dafifRecord.optionalField("distance");
    Optional<String> altitudeDescription = dafifRecord.optionalField("altitudeDescription");
    Optional<String> altitude1 = dafifRecord.optionalField("altitude1");
    Optional<String> altitude2 = dafifRecord.optionalField("altitude2");
    Optional<Double> requiredNavPerformance = dafifRecord.optionalField("requiredNavPerformance").map(x -> generateRequiredNavPerformance((Integer) x));
    Integer cycleDate = dafifRecord.requiredField("cycleDate");
    Optional<String> waypointGeodeticLatitude = dafifRecord.optionalField("waypointGeodeticLatitude");
    Optional<Double> waypointDegreesLatitude = dafifRecord.optionalField("waypointDegreesLatitude");
    Optional<String> waypointGeodeticLongitude = dafifRecord.optionalField("waypointGeodeticLongitude");
    Optional<Double> waypointDegreesLongitude = dafifRecord.optionalField("waypointDegreesLongitude");
    Optional<Double> waypointMagneticVariation = dafifRecord.optionalField("waypointMagneticVariation");
    Optional<String> navaid1GeodeticLatitude = dafifRecord.optionalField("navaid1GeodeticLatitude");
    Optional<Double> navaid1DegreesLatitude = dafifRecord.optionalField("navaid1DegreesLatitude");
    Optional<String> navaid1GeodeticLongitude = dafifRecord.optionalField("navaid1GeodeticLongitude");
    Optional<Double> navaid1DegreesLongitude = dafifRecord.optionalField("navaid1DegreesLongitude");
    Optional<Double> navaid1MagneticVariation = dafifRecord.optionalField("navaid1MagneticVariation");
    Optional<String> navaid1DmeGeodeticLatitude = dafifRecord.optionalField("navaid1DmeGeodeticLatitude");
    Optional<Double> navaid1DmeDegreesLatitude = dafifRecord.optionalField("navaid1DmeDegreesLatitude");
    Optional<String> navaid1DmeGeodeticLongitude = dafifRecord.optionalField("navaid1DmeGeodeticLongitude");
    Optional<Double> navaid1DmeDegreesLongitude = dafifRecord.optionalField("navaid1DmeDegreesLongitude");
    Optional<String> navaid2GeodeticLatitude = dafifRecord.optionalField("navaid2GeodeticLatitude");
    Optional<Double> navaid2DegreesLatitude = dafifRecord.optionalField("navaid2DegreesLatitude");
    Optional<String> navaid2GeodeticLongitude = dafifRecord.optionalField("navaid2GeodeticLongitude");
    Optional<Double> navaid2DegreesLongitude = dafifRecord.optionalField("navaid2DegreesLongitude");
    Optional<Double> navaid2MagneticVariation = dafifRecord.optionalField("navaid2MagneticVariation");
    Optional<String> navaid2DmeGeodeticLatitude = dafifRecord.optionalField("navaid2DmeGeodeticLatitude");
    Optional<Double> navaid2DmeDegreesLatitude = dafifRecord.optionalField("navaid2DmeDegreesLatitude");
    Optional<String> navaid2DmeGeodeticLongitude = dafifRecord.optionalField("navaid2DmeGeodeticLongitude");
    Optional<Double> navaid2DmeDegreesLongitude = dafifRecord.optionalField("navaid2DmeDegreesLongitude");
    Optional<Double> speedLimit1 = dafifRecord.optionalField("speedLimit1 ");
    Optional<String> speedLimitAircraftType1 = dafifRecord.optionalField("speedLimitAircraftType1");
    Optional<String> speedLimitAltitude1 = dafifRecord.optionalField("speedLimitAltitude1");
    Optional<Double> speedLimit2 = dafifRecord.optionalField("speedLimit2");
    Optional<String> speedLimitAircraftType2 = dafifRecord.optionalField("speedLimitAircraftType2");
    Optional<String> speedLimitAltitude2 = dafifRecord.optionalField("speedLimitAltitude2");
    Optional<Double> verticalNavigationVnav = dafifRecord.optionalField("verticalNavigationVnav");
    Optional<Integer> thresholdCrossingHeight = dafifRecord.optionalField("thresholdCrossingHeight");
    Optional<String> arcWaypointIdentifier = dafifRecord.optionalField("arcWaypointIdentifier");
    Optional<String> arcWaypointCountryCode = dafifRecord.optionalField("arcWaypointCountryCode");
    Optional<Double> arcRadius = dafifRecord.optionalField("arcRadius");

    DafifTerminalSegment dafifTerminalSegment = new DafifTerminalSegment.Builder()
        .airportIdentification(airportIdentification)
        .terminalProcedureType(terminalProcedureType)
        .terminalIdentifier(terminalIdentifier)
        .terminalSequenceNumber(terminalSequenceNumber)
        .terminalApproachType(terminalApproachType)
        .transitionIdentifier(transitionIdentifier)
        .icaoCode(icaoCode)
        .trackDescriptionCode(trackDescriptionCode)
        .termSegWaypointIdentifier(termSegWaypointIdentifier.orElse(null))
        .waypointCountryCode(waypointCountryCode.orElse(null))
        .terminalWaypointDescriptionCode1(terminalWaypointDescriptionCode1Arpt.orElse(null))
        .terminalWaypointDescriptionCode2(terminalWaypointDescriptionCode2.orElse(null))
        .terminalWaypointDescriptionCode3(terminalWaypointDescriptionCode3.orElse(null))
        .terminalWaypointDescriptionCode4(terminalWaypointDescriptionCode4.orElse(null))
        .terminalSegmentTurnDirection(terminalSegmentTurnDirection.orElse(null))
        .navaid1Identifier(navaid1Identifier.orElse(null))
        .navaid1Type(navaid1Type.orElse(null))
        .navaid1CountryCode(navaid1CountryCode.orElse(null))
        .navaid1KeyCode(navaid1KeyCode.orElse(null))
        .fix1Bearing(fix1Bearing.orElse(null))
        .fix1Distance(fix1Distance.orElse(null))
        .navaid2Identifier(navaid2Identifier.orElse(null))
        .navaid2Type(navaid2Type.orElse(null))
        .navaid2CountryCode(navaid2CountryCode.orElse(null))
        .navaid2KeyCode(navaid2KeyCode.orElse(null))
        .fix2Bearing(fix2Bearing.orElse(null))
        .fix2Distance(fix2Distance.orElse(null))
        .terminalMagneticCourse(terminalMagneticCourse.orElse(null))
        .distance(distance.orElse(null))
        .altitudeDescription(altitudeDescription.orElse(null))
        .altitude1(altitude1.orElse(null))
        .altitude2(altitude2.orElse(null))
        .requiredNavPerformance(requiredNavPerformance.orElse(null))
        .cycleDate(cycleDate)
        .waypointGeodeticLatitude(waypointGeodeticLatitude.orElse(null))
        .waypointDegreesLatitude(waypointDegreesLatitude.orElse(null))
        .waypointGeodeticLongitude(waypointGeodeticLongitude.orElse(null))
        .waypointDegreesLongitude(waypointDegreesLongitude.orElse(null))
        .waypointMagneticVariation(waypointMagneticVariation.orElse(null))
        .navaid1GeodeticLatitude(navaid1GeodeticLatitude.orElse(null))
        .navaid1DegreesLatitude(navaid1DegreesLatitude.orElse(null))
        .navaid1GeodeticLongitude(navaid1GeodeticLongitude.orElse(null))
        .navaid1DegreesLongitude(navaid1DegreesLongitude.orElse(null))
        .navaid1MagneticVariation(navaid1MagneticVariation.orElse(null))
        .navaid1DmeGeodeticLatitude(navaid1DmeGeodeticLatitude.orElse(null))
        .navaid1DmeDegreesLatitude(navaid1DmeDegreesLatitude.orElse(null))
        .navaid1DmeGeodeticLongitude(navaid1DmeGeodeticLongitude.orElse(null))
        .navaid1DmeDegreesLongitude(navaid1DmeDegreesLongitude.orElse(null))
        .navaid2GeodeticLatitude(navaid2GeodeticLatitude.orElse(null))
        .navaid2DegreesLatitude(navaid2DegreesLatitude.orElse(null))
        .navaid2GeodeticLongitude(navaid2GeodeticLongitude.orElse(null))
        .navaid2DegreesLongitude(navaid2DegreesLongitude.orElse(null))
        .navaid2MagneticVariation(navaid2MagneticVariation.orElse(null))
        .navaid2DmeGeodeticLatitude(navaid2DmeGeodeticLatitude.orElse(null))
        .navaid2DmeDegreesLatitude(navaid2DmeDegreesLatitude.orElse(null))
        .navaid2DmeGeodeticLongitude(navaid2DmeGeodeticLongitude.orElse(null))
        .navaid2DmeDegreesLongitude(navaid2DmeDegreesLongitude.orElse(null))
        .speedLimit1(speedLimit1.orElse(null))
        .speedLimitAircraftType1(speedLimitAircraftType1.orElse(null))
        .speedLimitAltitude1(speedLimitAltitude1.orElse(null))
        .speedLimit2(speedLimit2.orElse(null))
        .speedLimitAircraftType2(speedLimitAircraftType2.orElse(null))
        .speedLimitAltitude2(speedLimitAltitude2.orElse(null))
        .verticalNavigationVnav(verticalNavigationVnav.orElse(null))
        .thresholdCrossingHeight(thresholdCrossingHeight.orElse(null))
        .arcWaypointIdentifier(arcWaypointIdentifier.orElse(null))
        .arcWaypointCountryCode(arcWaypointCountryCode.orElse(null))
        .arcRadius(arcRadius.orElse(null))
        .build();

    return Optional.of(dafifTerminalSegment);
  }

  private Double generateRequiredNavPerformance(Integer dafifValue) {
    int decimals = dafifValue % 10;
    return dafifValue / (10.0 + decimals);
  }
}
