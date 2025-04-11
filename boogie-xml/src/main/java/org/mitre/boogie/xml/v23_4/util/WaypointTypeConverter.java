package org.mitre.boogie.xml.v23_4.util;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.fields.ArincWaypointType;
import org.mitre.boogie.xml.v23_4.generated.WaypointType;

/**
 * This class converts between the v23_4 waypoint type and the internal model
 */
public final class WaypointTypeConverter implements Function<WaypointType, ArincWaypointType> {
  public static final WaypointTypeConverter INSTANCE = new WaypointTypeConverter();
  private WaypointTypeConverter() {}
  @Override
  public ArincWaypointType apply(WaypointType waypointType) {
    return ArincWaypointType.builder()
        .airwayIntersection(Optional.ofNullable(waypointType.isIsAirwayIntersection()).orElse(false))
        .arcCenter(Optional.ofNullable(waypointType.isIsArcCenter()).orElse(false))
        .backmarker(Optional.ofNullable(waypointType.isIsBackMarker()).orElse(false))
        .controlledAirspaceIntersection(Optional.ofNullable(waypointType.isIsControlledAirspaceIntersection()).orElse(false))
        .enroute(Optional.ofNullable(waypointType.isIsEnroute()).orElse(false))
        .facf(Optional.ofNullable(waypointType.isIsFacf()).orElse(false))
        .faf(Optional.ofNullable(waypointType.isIsFaf()).orElse(false))
        .firOrFraEntryPoint(Optional.ofNullable(waypointType.isIsFirOrFraEntryPoint()).orElse(false))
        .firOrFraExitPoint(Optional.ofNullable(waypointType.isIsFirOrFraExitPoint()).orElse(false))
        .firUirFix(Optional.ofNullable(waypointType.isIsFirUirFix()).orElse(false))
        .forApproach(Optional.ofNullable(waypointType.isIsForApproach()).orElse(false))
        .forSid(Optional.ofNullable(waypointType.isIsForSid()).orElse(false))
        .forStar(Optional.ofNullable(waypointType.isIsForStar()).orElse(false))
        .forMultipleProcedures(Optional.ofNullable(waypointType.isIsForMultipleProcedures()).orElse(false))
        .fullDegreeLatFix(Optional.ofNullable(waypointType.isIsFullDegreeLatFix()).orElse(false))
        .halfDegreeLatFix(Optional.ofNullable(waypointType.isIsHalfDegreeLatFix()).orElse(false))
        .helicopterOnlyAirwayFix(Optional.ofNullable(waypointType.isIsHelicopterOnlyAirwayFix()).orElse(false))
        .initialApproachFix(Optional.ofNullable(waypointType.isIsInitialApproachFix()).orElse(false))
        .intersectionDmeFix(Optional.ofNullable(waypointType.isIsIntersectionDmeFix()).orElse(false))
        .middleMarker(Optional.ofNullable(waypointType.isIsMiddleMarker()).orElse(false))
        .missedApproachPoint(Optional.ofNullable(waypointType.isIsMissedApproachPoint()).orElse(false))
        .ndb(Optional.ofNullable(waypointType.isIsNdb()).orElse(false))
        .oceanicGateway(Optional.ofNullable(waypointType.isIsOceanicGateway()).orElse(false))
        .offRoute(Optional.ofNullable(waypointType.isIsOffRoute()).orElse(false))
        .outerMarker(Optional.ofNullable(waypointType.isIsOuterMarker()).orElse(false))
        .requiredOffRoute(Optional.ofNullable(waypointType.isIsRequiredOffRoute()).orElse(false))
        .rfLegNotProcedureFix(Optional.ofNullable(waypointType.isIsRfLegNotProcedureFix()).orElse(false))
        .sourceProvidedEnroute(Optional.ofNullable(waypointType.isIsSourceProvidedEnroute()).orElse(false))
        .stepDownFix(Optional.ofNullable(waypointType.isIsStepDownFix()).orElse(false))
        .uncharted(Optional.ofNullable(waypointType.isIsUncharted()).orElse(false))
        .unnamed(Optional.ofNullable(waypointType.isIsUnnamed()).orElse(false))
        .vfr(Optional.ofNullable(waypointType.isIsVfr()).orElse(false))
        .build();
  }
}
