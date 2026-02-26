package org.mitre.boogie.xml.v23_4.convert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincAirwayLeg;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.v23_4.generated.AirwayLeg;
import org.mitre.boogie.xml.v23_4.generated.Course;
import org.mitre.boogie.xml.v23_4.generated.HoldRvsmMinimumMaximumAltitudeConstraint;

final class ArincAirwayLegConverter implements Function<AirwayLeg, Optional<ArincAirwayLeg>> {
  static final ArincAirwayLegConverter INSTANCE = new ArincAirwayLegConverter();

  private static final ArincAirwayLegValidator VALIDATOR = ArincAirwayLegValidator.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;

  private ArincAirwayLegConverter() {
  }

  @Override
  public Optional<ArincAirwayLeg> apply(AirwayLeg leg) {
    return Optional.ofNullable(leg)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincAirwayLeg convert(AirwayLeg leg) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(leg);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(leg);

    Double inboundCourseValue = Optional.ofNullable(leg.getInboundCourse())
        .map(Course::getCourseValue)
        .map(BigDecimal::doubleValue)
        .orElse(null);

    Boolean inboundCourseIsTrue = Optional.ofNullable(leg.getInboundCourse())
        .map(Course::isIsTrue)
        .orElse(null);

    Double outboundCourseValue = Optional.ofNullable(leg.getOutboundCourse())
        .map(Course::getCourseValue)
        .map(BigDecimal::doubleValue)
        .orElse(null);

    Boolean outboundCourseIsTrue = Optional.ofNullable(leg.getOutboundCourse())
        .map(Course::isIsTrue)
        .orElse(null);

    Integer rvsmMin = Optional.ofNullable(leg.getRvsmMinMaxLevels())
        .map(HoldRvsmMinimumMaximumAltitudeConstraint::getMinimumAltitude)
        .map(a -> a.getAltitude())
        .orElse(null);

    Integer rvsmMax = Optional.ofNullable(leg.getRvsmMinMaxLevels())
        .map(HoldRvsmMinimumMaximumAltitudeConstraint::getMaximumAltitude)
        .map(a -> a.getAltitude())
        .orElse(null);

    return ArincAirwayLeg.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .sequenceNumber(leg.getSequenceNumber())
        .fixRef(Optional.ofNullable(leg.getFixRef()).map(Object::toString).orElse(null))
        .fixIdent(leg.getFixIdent())
        .recNavaidIdent(leg.getRecNavaidIdent())
        .recNavaidRef(Optional.ofNullable(leg.getRecNavaidRef()).map(Object::toString).orElse(null))
        .cruiseTableRef(Optional.ofNullable(leg.getCruiseTableRef()).map(Object::toString).orElse(null))
        .airwayRouteType(Optional.ofNullable(leg.getAirwayRouteType()).map(Enum::name).orElse(null))
        .qualifier1(Optional.ofNullable(leg.getQualifier1()).map(Enum::name).orElse(null))
        .qualifier2(Optional.ofNullable(leg.getQualifier2()).map(Enum::name).orElse(null))
        .rnavPbnNavSpec(Optional.ofNullable(leg.getRnavPbnNavSpec()).map(Enum::name).orElse(null))
        .rnpPbnNavSpec(Optional.ofNullable(leg.getRnpPbnNavSpec()).map(Enum::name).orElse(null))
        .legDirectionRestriction(Optional.ofNullable(leg.getLegDirectionRestriction()).map(Enum::name).orElse(null))
        .routeDistanceFrom(Optional.ofNullable(leg.getRouteDistanceFrom()).map(BigDecimal::doubleValue).orElse(null))
        .euIndicator(Optional.ofNullable(leg.getEuIndicator()).map(Enum::name).orElse(null))
        .fixRadiusTransitionIndicator(Optional.ofNullable(leg.getFixRadiusTransitionIndicator()).map(BigDecimal::doubleValue).orElse(null))
        .inboundCourseValue(inboundCourseValue)
        .inboundCourseIsTrue(inboundCourseIsTrue)
        .outboundCourseValue(outboundCourseValue)
        .outboundCourseIsTrue(outboundCourseIsTrue)
        .level(Optional.ofNullable(leg.getLevel()).map(Enum::name).orElse(null))
        .rho(Optional.ofNullable(leg.getRho()).map(BigDecimal::doubleValue).orElse(null))
        .rnp(Optional.ofNullable(leg.getRnp()).map(BigDecimal::doubleValue).orElse(null))
        .theta(Optional.ofNullable(leg.getTheta()).map(BigDecimal::doubleValue).orElse(null))
        .verticalScaleFactor(leg.getVerticalScaleFactor())
        .rvsmMinAltitude(rvsmMin)
        .rvsmMaxAltitude(rvsmMax)
        .build();
  }
}
