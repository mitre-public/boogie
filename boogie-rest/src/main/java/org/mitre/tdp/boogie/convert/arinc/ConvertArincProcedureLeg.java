package org.mitre.tdp.boogie.convert.arinc;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.arinc.ArincProcedureLeg;
import org.mitre.tdp.boogie.contract.arinc.ImmutableArincProcedureLeg;

public final class ConvertArincProcedureLeg implements Function<org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg, ArincProcedureLeg> {
  public static final ConvertArincProcedureLeg INSTANCE = new ConvertArincProcedureLeg();

  private ConvertArincProcedureLeg() {

  }

  @Override
  public ArincProcedureLeg apply(org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg arincProcedureLeg) {
    requireNonNull(arincProcedureLeg);
    return ImmutableArincProcedureLeg.builder()
        .recordType(arincProcedureLeg.recordType())
        .customerAreaCode(arincProcedureLeg.customerAreaCode())
        .sectionCode(arincProcedureLeg.sectionCode())
        .airportIdentifier(arincProcedureLeg.airportIdentifier())
        .airportIcaoRegion(arincProcedureLeg.airportIcaoRegion())
        .subSectionCode(arincProcedureLeg.subSectionCode())
        .sidStarIdentifier(arincProcedureLeg.sidStarIdentifier())
        .routeType(arincProcedureLeg.routeType())
        .transitionIdentifier(arincProcedureLeg.transitionIdentifier())
        .sequenceNumber(arincProcedureLeg.sequenceNumber())
        .fixIdentifier(arincProcedureLeg.fixIdentifier())
        .fixIcaoRegion(arincProcedureLeg.fixIcaoRegion())
        .fixSectionCode(arincProcedureLeg.fixSectionCode())
        .fixSubSectionCode(arincProcedureLeg.fixSubSectionCode())
        .continuationRecordNumber(arincProcedureLeg.continuationRecordNumber())
        .waypointDescription(arincProcedureLeg.waypointDescription())
        .turnDirection(arincProcedureLeg.turnDirection())
        .rnp(arincProcedureLeg.rnp())
        .pathTerm(arincProcedureLeg.pathTerm())
        .turnDirectionValid(arincProcedureLeg.turnDirectionValid())
        .recommendedNavaidIdentifier(arincProcedureLeg.recommendedNavaidIdentifier())
        .recommendedNavaidIcaoRegion(arincProcedureLeg.recommendedNavaidIcaoRegion())
        .arcRadius(arincProcedureLeg.arcRadius())
        .theta(arincProcedureLeg.theta())
        .rho(arincProcedureLeg.rho())
        .outboundMagneticCourse(arincProcedureLeg.outboundMagneticCourse())
        .routeHoldDistanceTime(arincProcedureLeg.routeHoldDistanceTime())
        .holdTime(arincProcedureLeg.holdTime())
        .routeDistance(arincProcedureLeg.routeDistance())
        .recommendedNavaidSectionCode(arincProcedureLeg.recommendedNavaidSectionCode())
        .recommendedNavaidSubSectionCode(arincProcedureLeg.recommendedNavaidSubSectionCode())
        .altitudeDescription(arincProcedureLeg.altitudeDescription())
        .minAltitude1(arincProcedureLeg.minAltitude1())
        .minAltitude2(arincProcedureLeg.minAltitude2())
        .transitionAltitude(arincProcedureLeg.transitionAltitude())
        .speedLimit(arincProcedureLeg.speedLimit())
        .verticalAngle(arincProcedureLeg.verticalAngle())
        .centerFixIdentifier(arincProcedureLeg.centerFixIdentifier())
        .centerFixIcaoRegion(arincProcedureLeg.centerFixIcaoRegion())
        .centerFixSectionCode(arincProcedureLeg.centerFixSectionCode())
        .centerFixSubSectionCode(arincProcedureLeg.centerFixSubSectionCode())
        .routeTypeQualifier1(arincProcedureLeg.routeTypeQualifier1())
        .routeTypeQualifier2(arincProcedureLeg.routeTypeQualifier2())
        .fileRecordNumber(arincProcedureLeg.fileRecordNumber())
        .lastUpdateCycle(arincProcedureLeg.lastUpdateCycle())
        .build();
  }
}
