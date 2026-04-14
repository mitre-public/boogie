package org.mitre.boogie.xml.v23_4.convert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincProcedureLeg;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.v23_4.generated.AltitudeConstraint;
import org.mitre.boogie.xml.v23_4.generated.AltitudeTermination;
import org.mitre.boogie.xml.v23_4.generated.ApproachLeg;
import org.mitre.boogie.xml.v23_4.generated.ApproachWaypointDescription;
import org.mitre.boogie.xml.v23_4.generated.MagneticVariation;
import org.mitre.boogie.xml.v23_4.generated.ProcedureLeg;
import org.mitre.boogie.xml.v23_4.generated.ProcedureWaypointDescription;
import org.mitre.boogie.xml.v23_4.generated.SidLeg;
import org.mitre.boogie.xml.v23_4.generated.SpeedLimit;
import org.mitre.boogie.xml.v23_4.generated.StarLeg;

final class ArincProcedureLegConverter implements Function<ProcedureLeg, Optional<ArincProcedureLeg>> {
  static final ArincProcedureLegConverter INSTANCE = new ArincProcedureLegConverter();

  private static final ArincProcedureLegValidator VALIDATOR = ArincProcedureLegValidator.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final FlatCourseConverter COURSE_CONVERTER = FlatCourseConverter.INSTANCE;

  private ArincProcedureLegConverter() {
  }

  @Override
  public Optional<ArincProcedureLeg> apply(ProcedureLeg leg) {
    return Optional.ofNullable(leg)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincProcedureLeg convert(ProcedureLeg leg) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(leg);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(leg);

    Optional<FlatCourse> course = Optional.ofNullable(leg.getCourse()).flatMap(COURSE_CONVERTER);
    Optional<MagneticVariation> magVar = Optional.ofNullable(leg.getProcedureDesignMagVar());
    Optional<SpeedLimit> speedLimit = Optional.ofNullable(leg.getSpeedLimit());
    Optional<AltitudeConstraint> altConstraint = Optional.ofNullable(leg.getAltitudeConstraint());
    Optional<AltitudeTermination> altTerm = Optional.ofNullable(leg.getAltitudeTermination());
    Optional<ProcedureLeg.RaceTrackAltitude> raceTrack = Optional.ofNullable(leg.getRaceTrackAltitude());
    Optional<ProcedureWaypointDescription> wpDesc = Optional.ofNullable(leg.getWaypointDescriptor());

    ArincProcedureLeg.Builder builder = ArincProcedureLeg.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        // Leg fields
        .sequenceNumber(leg.getSequenceNumber())
        .fixRef(Optional.ofNullable(leg.getFixRef()).map(Object::toString).orElse(null))
        .fixIdent(leg.getFixIdent())
        .recNavaidIdent(leg.getRecNavaidIdent())
        .recNavaidRef(Optional.ofNullable(leg.getRecNavaidRef()).map(Object::toString).orElse(null))
        // ProcedureLeg fields
        .pathAndTermination(leg.getPathAndTermination().name())
        .turnDirection(Optional.ofNullable(leg.getTurnDirection()).map(Enum::name).orElse(null))
        .isTurnDirectionValid(leg.isIsTurnDirectionValid())
        .arcRadius(Optional.ofNullable(leg.getArcRadius()).map(BigDecimal::doubleValue).orElse(null))
        .atcIndicator(Optional.ofNullable(leg.getAtcIndicator()).map(Enum::name).orElse(null))
        .distance(Optional.ofNullable(leg.getDistance()).map(BigDecimal::doubleValue).orElse(null))
        .legDistance(Optional.ofNullable(leg.getLegDistance()).map(BigDecimal::doubleValue).orElse(null))
        .legInboundIndicator(leg.getLegInboundIndicator())
        .legInboundOutboundIndicator(Optional.ofNullable(leg.getLegInboundOutboundIndicator()).map(Enum::name).orElse(null))
        .rho(Optional.ofNullable(leg.getRho()).map(BigDecimal::doubleValue).orElse(null))
        .rnp(Optional.ofNullable(leg.getRnp()).map(BigDecimal::doubleValue).orElse(null))
        .theta(Optional.ofNullable(leg.getTheta()).map(BigDecimal::doubleValue).orElse(null))
        .courseValue(course.map(FlatCourse::value).orElse(null))
        .courseIsTrue(course.map(FlatCourse::isTrue).orElse(null))
        .centerFix(leg.getCenterFix())
        .centerFixRef(Optional.ofNullable(leg.getCenterFixRef()).map(Object::toString).orElse(null))
        .transitionsAltitudeLevel(leg.getTransitionsAltitudeLevel())
        .verticalScaleFactor(leg.getVerticalScaleFactor())
        .holdTime(Optional.ofNullable(leg.getHoldTime()).map(Object::toString).orElse(null))
        // ProcedureDesignMagVar
        .procedureDesignMagVarDirection(magVar.map(MagneticVariation::getMagneticVariationEWT).map(Enum::name).orElse(null))
        .procedureDesignMagVarValue(magVar.map(MagneticVariation::getMagneticVariationValue).map(BigDecimal::doubleValue).orElse(null))
        // SpeedLimit
        .speedLimitAt(speedLimit.map(SpeedLimit::getAt).orElse(null))
        .speedLimitAtOrAbove(speedLimit.map(SpeedLimit::getAtOrAbove).orElse(null))
        .speedLimitAtOrBelow(speedLimit.map(SpeedLimit::getAtOrBelow).orElse(null))
        // AltitudeConstraint
        .altitudeConstraintAt(altConstraint.map(AltitudeConstraint::getAt).map(ConstraintAltitudeResolver::resolve).orElse(null))
        .altitudeConstraintAtOrAbove(altConstraint.map(AltitudeConstraint::getAtOrAbove).map(ConstraintAltitudeResolver::resolve).orElse(null))
        .altitudeConstraintAtOrBelow(altConstraint.map(AltitudeConstraint::getAtOrBelow).map(ConstraintAltitudeResolver::resolve).orElse(null))
        // AltitudeTermination
        .altitudeTerminationType(altTerm.map(AltitudeTermination::getTerminationType).map(Enum::name).orElse(null))
        .altitudeTerminationValue(altTerm.map(AltitudeTermination::getAltitude).map(ConstraintAltitudeResolver::resolve).orElse(null))
        // RaceTrackAltitude
        .raceTrackAltitude(raceTrack.map(ProcedureLeg.RaceTrackAltitude::getAtOrAbove).map(ConstraintAltitudeResolver::resolve).orElse(null))
        // ProcedureWaypointDescription
        .isEssential(wpDesc.map(ProcedureWaypointDescription::isIsEssential).orElse(null))
        .isFlyOver(wpDesc.map(ProcedureWaypointDescription::isIsFlyOver).orElse(null))
        .isHolding(wpDesc.map(ProcedureWaypointDescription::isIsHolding).orElse(null))
        .isNoProcedureTurn(wpDesc.map(ProcedureWaypointDescription::isIsNoProcedureTurn).orElse(null))
        .isPhantomFix(wpDesc.map(ProcedureWaypointDescription::isIsPhantomFix).orElse(null))
        .isSourceProvidedEnrouteWaypoint(wpDesc.map(ProcedureWaypointDescription::isIsSourceProvidedEnrouteWaypoint).orElse(null))
        .isTaaProcedureTurn(wpDesc.map(ProcedureWaypointDescription::isIsTaaProcedureTurn).orElse(null))
        .isAtcCompulsoryReportingPoint(wpDesc.map(ProcedureWaypointDescription::isIsAtcCompulsoryReportingPoint).orElse(null));

    if (leg instanceof ApproachLeg approachLeg) {
      convertApproachLeg(builder, approachLeg);
    } else if (leg instanceof SidLeg sidLeg) {
      convertSidLeg(builder, sidLeg);
    } else if (leg instanceof StarLeg starLeg) {
      convertStarLeg(builder, starLeg);
    }

    return builder.build();
  }

  private void convertApproachLeg(ArincProcedureLeg.Builder builder, ApproachLeg leg) {
    builder.verticalAngle(Optional.ofNullable(leg.getVerticalAngle()).map(BigDecimal::doubleValue).orElse(null))
        .glideSlopeCrossingAltitude(leg.getGlideSlopeCrossingAltitude());

    Optional<ApproachWaypointDescription> awpDesc = Optional.ofNullable(leg.getApproachWaypointDescription());
    builder.isFacf(awpDesc.map(ApproachWaypointDescription::isIsFacf).orElse(null))
        .isMissedApproachPoint(awpDesc.map(ApproachWaypointDescription::isIsMissedApproachPoint).orElse(null))
        .isFaf(awpDesc.map(ApproachWaypointDescription::isIsFaf).orElse(null))
        .isFinalEndPoint(awpDesc.map(ApproachWaypointDescription::isIsFinalEndPoint).orElse(null))
        .isFixTurningFinalApproach(awpDesc.map(ApproachWaypointDescription::isIsFixTurningFinalApproach).orElse(null))
        .isInitialApproachFix(awpDesc.map(ApproachWaypointDescription::isIsInitialApproachFix).orElse(null))
        .isIntermediateApproachFix(awpDesc.map(ApproachWaypointDescription::isIsIntermediateApproachFix).orElse(null))
        .stepDownFix(awpDesc.map(ApproachWaypointDescription::getStepDownFix).map(Enum::name).orElse(null));
  }

  private void convertSidLeg(ArincProcedureLeg.Builder builder, SidLeg leg) {
    builder.isEngineOutDisarmPoint(leg.isIsEngineOutDisarmPoint())
        .isInitialDepartureFix(leg.isIsInitialDepartureFix())
        .isQuietClimbRestorePoint(leg.isIsQuietClimbRestorePoint());
  }

  private void convertStarLeg(ArincProcedureLeg.Builder builder, StarLeg leg) {
    builder.verticalAngle(Optional.ofNullable(leg.getVerticalAngle()).map(BigDecimal::doubleValue).orElse(null));
  }
}
