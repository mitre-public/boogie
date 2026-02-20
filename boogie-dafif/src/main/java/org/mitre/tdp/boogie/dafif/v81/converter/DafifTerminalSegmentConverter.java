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

    return Optional.of(new DafifTerminalSegment.Builder()
        .airportIdentification(dafifRecord.requiredField("airportIdentification"))
        .terminalProcedureType(dafifRecord.requiredField("terminalProcedureType"))
        .terminalIdentifier(dafifRecord.requiredField("terminalIdentifier"))
        .terminalSequenceNumber(dafifRecord.requiredField("terminalSequenceNumber"))
        .terminalApproachType(dafifRecord.requiredField("terminalApproachType"))
        .transitionIdentifier(dafifRecord.<String>optionalField("transitionIdentifier").orElse(null))
        .icaoCode(dafifRecord.requiredField("icaoCode"))
        .trackDescriptionCode(dafifRecord.requiredField("trackDescriptionCode"))
        .termSegWaypointIdentifier(dafifRecord.<String>optionalField("termSegWaypointIdentifier").orElse(null))
        .waypointCountryCode(dafifRecord.<String>optionalField("waypointCountryCode").orElse(null))
        .terminalWaypointDescriptionCode1(dafifRecord.<String>optionalField("terminalWaypointDescriptionCode1Arpt").orElse(null))
        .terminalWaypointDescriptionCode2(dafifRecord.<String>optionalField("terminalWaypointDescriptionCode2").orElse(null))
        .terminalWaypointDescriptionCode3(dafifRecord.<String>optionalField("terminalWaypointDescriptionCode3").orElse(null))
        .terminalWaypointDescriptionCode4(dafifRecord.<String>optionalField("terminalWaypointDescriptionCode4").orElse(null))
        .terminalSegmentTurnDirection(dafifRecord.<String>optionalField("terminalSegmentTurnDirection").orElse(null))
        .navaid1Identifier(dafifRecord.<String>optionalField("navaid1Identifier").orElse(null))
        .navaid1Type(dafifRecord.<String>optionalField("navaid1Type").orElse(null))
        .navaid1CountryCode(dafifRecord.<String>optionalField("navaid1CountryCode").orElse(null))
        .navaid1KeyCode(dafifRecord.<Integer>optionalField("navaid1KeyCode").orElse(null))
        .fix1Bearing(dafifRecord.<Double>optionalField("fix1Bearing").orElse(null))
        .fix1Distance(dafifRecord.<Double>optionalField("fix1Distance").orElse(null))
        .navaid2Identifier(dafifRecord.<String>optionalField("navaid2Identifier").orElse(null))
        .navaid2Type(dafifRecord.<String>optionalField("navaid2Type").orElse(null))
        .navaid2CountryCode(dafifRecord.<String>optionalField("navai2CountryCode").orElse(null))
        .navaid2KeyCode(dafifRecord.<Integer>optionalField("navaid2KeyCode").orElse(null))
        .fix2Bearing(dafifRecord.<Double>optionalField("fix2Bearing").orElse(null))
        .fix2Distance(dafifRecord.<Double>optionalField("fix2Distance").orElse(null))
        .terminalMagneticCourse(dafifRecord.<String>optionalField("terminalMagneticCourse").orElse(null))
        .distance(dafifRecord.<Double>optionalField("distance").orElse(null))
        .altitudeDescription(dafifRecord.<String>optionalField("altitudeDescription").orElse(null))
        .altitude1(dafifRecord.<String>optionalField("altitude1").orElse(null))
        .altitude2(dafifRecord.<String>optionalField("altitude2").orElse(null))
        .requiredNavPerformance(dafifRecord.<Integer>optionalField("requiredNavPerformance").map(this::generateRequiredNavPerformance).orElse(null))
        .cycleDate(dafifRecord.requiredField("cycleDate"))
        .waypointGeodeticLatitude(dafifRecord.<String>optionalField("waypointGeodeticLatitude").orElse(null))
        .waypointDegreesLatitude(dafifRecord.<Double>optionalField("waypointDegreesLatitude").orElse(null))
        .waypointGeodeticLongitude(dafifRecord.<String>optionalField("waypointGeodeticLongitude").orElse(null))
        .waypointDegreesLongitude(dafifRecord.<Double>optionalField("waypointDegreesLongitude").orElse(null))
        .waypointMagneticVariation(dafifRecord.<Double>optionalField("waypointMagneticVariation").orElse(null))
        .navaid1GeodeticLatitude(dafifRecord.<String>optionalField("navaid1GeodeticLatitude").orElse(null))
        .navaid1DegreesLatitude(dafifRecord.<Double>optionalField("navaid1DegreesLatitude").orElse(null))
        .navaid1GeodeticLongitude(dafifRecord.<String>optionalField("navaid1GeodeticLongitude").orElse(null))
        .navaid1DegreesLongitude(dafifRecord.<Double>optionalField("navaid1DegreesLongitude").orElse(null))
        .navaid1MagneticVariation(dafifRecord.<Double>optionalField("navaid1MagneticVariation").orElse(null))
        .navaid1DmeGeodeticLatitude(dafifRecord.<String>optionalField("navaid1DmeGeodeticLatitude").orElse(null))
        .navaid1DmeDegreesLatitude(dafifRecord.<Double>optionalField("navaid1DmeDegreesLatitude").orElse(null))
        .navaid1DmeGeodeticLongitude(dafifRecord.<String>optionalField("navaid1DmeGeodeticLongitude").orElse(null))
        .navaid1DmeDegreesLongitude(dafifRecord.<Double>optionalField("navaid1DmeDegreesLongitude").orElse(null))
        .navaid2GeodeticLatitude(dafifRecord.<String>optionalField("navaid2GeodeticLatitude").orElse(null))
        .navaid2DegreesLatitude(dafifRecord.<Double>optionalField("navaid2DegreesLatitude").orElse(null))
        .navaid2GeodeticLongitude(dafifRecord.<String>optionalField("navaid2GeodeticLongitude").orElse(null))
        .navaid2DegreesLongitude(dafifRecord.<Double>optionalField("navaid2DegreesLongitude").orElse(null))
        .navaid2MagneticVariation(dafifRecord.<Double>optionalField("navaid2MagneticVariation").orElse(null))
        .navaid2DmeGeodeticLatitude(dafifRecord.<String>optionalField("navaid2DmeGeodeticLatitude").orElse(null))
        .navaid2DmeDegreesLatitude(dafifRecord.<Double>optionalField("navaid2DmeDegreesLatitude").orElse(null))
        .navaid2DmeGeodeticLongitude(dafifRecord.<String>optionalField("navaid2DmeGeodeticLongitude").orElse(null))
        .navaid2DmeDegreesLongitude(dafifRecord.<Double>optionalField("navaid2DmeDegreesLongitude").orElse(null))
        .speedLimit1(dafifRecord.<Double>optionalField("speedLimit1 ").orElse(null))
        .speedLimitAircraftType1(dafifRecord.<String>optionalField("speedLimitAircraftType1").orElse(null))
        .speedLimitAltitude1(dafifRecord.<String>optionalField("speedLimitAltitude1").orElse(null))
        .speedLimit2(dafifRecord.<Double>optionalField("speedLimit2").orElse(null))
        .speedLimitAircraftType2(dafifRecord.<String>optionalField("speedLimitAircraftType2").orElse(null))
        .speedLimitAltitude2(dafifRecord.<String>optionalField("speedLimitAltitude2").orElse(null))
        .verticalNavigationVnav(dafifRecord.<Double>optionalField("verticalNavigationVnav").orElse(null))
        .thresholdCrossingHeight(dafifRecord.<Integer>optionalField("thresholdCrossingHeight").orElse(null))
        .arcWaypointIdentifier(dafifRecord.<String>optionalField("arcWaypointIdentifier").orElse(null))
        .arcWaypointCountryCode(dafifRecord.<String>optionalField("arcWaypointCountryCode").orElse(null))
        .arcRadius(dafifRecord.<Double>optionalField("arcRadius").orElse(null))
        .build());
  }

  private Double generateRequiredNavPerformance(Integer dafifValue) {
    int decimals = dafifValue % 10;
    return dafifValue / (10.0 + decimals);
  }
}
