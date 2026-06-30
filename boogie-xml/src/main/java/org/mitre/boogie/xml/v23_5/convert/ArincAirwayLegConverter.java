package org.mitre.boogie.xml.v23_5.convert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincAirwayLeg;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.v23_5.generated.AirwayLeg;

final class ArincAirwayLegConverter implements Function<AirwayLeg, Optional<ArincAirwayLeg>> {
  static final ArincAirwayLegConverter INSTANCE = new ArincAirwayLegConverter();

  private static final ArincAirwayLegValidator VALIDATOR = ArincAirwayLegValidator.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final FlatCourseConverter COURSE_CONVERTER = FlatCourseConverter.INSTANCE;
  private static final FlatMinMaxAltitudeConstraintConverter MIN_MAX_ALT_CONVERTER = FlatMinMaxAltitudeConstraintConverter.INSTANCE;

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

    Optional<FlatCourse> inboundCourse = Optional.ofNullable(leg.getInboundCourse()).flatMap(COURSE_CONVERTER);
    Optional<FlatCourse> outboundCourse = Optional.ofNullable(leg.getOutboundCourse()).flatMap(COURSE_CONVERTER);

    Optional<FlatMinMaxAltitudeConstraint> rvsmMinMax = MIN_MAX_ALT_CONVERTER.apply(leg.getRvsmMinMaxLevels());

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
        .inboundCourseValue(inboundCourse.map(FlatCourse::value).orElse(null))
        .inboundCourseIsTrue(inboundCourse.map(FlatCourse::isTrue).orElse(null))
        .outboundCourseValue(outboundCourse.map(FlatCourse::value).orElse(null))
        .outboundCourseIsTrue(outboundCourse.map(FlatCourse::isTrue).orElse(null))
        .level(Optional.ofNullable(leg.getLevel()).map(Enum::name).orElse(null))
        .rho(Optional.ofNullable(leg.getRho()).map(BigDecimal::doubleValue).orElse(null))
        .rnp(Optional.ofNullable(leg.getRnp()).map(BigDecimal::doubleValue).orElse(null))
        .theta(Optional.ofNullable(leg.getTheta()).map(BigDecimal::doubleValue).orElse(null))
        .verticalScaleFactor(leg.getVerticalScaleFactor())
        .rvsmMinAltitude(rvsmMinMax.map(FlatMinMaxAltitudeConstraint::minimumAltitude).orElse(null))
        .rvsmMaxAltitude(rvsmMinMax.map(FlatMinMaxAltitudeConstraint::maximumAltitude).orElse(null))
        .build();
  }
}
