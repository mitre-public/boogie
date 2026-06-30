package org.mitre.boogie.xml.v23_5.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincHoldingPattern;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.util.XmlDurationConverter;
import org.mitre.boogie.xml.v23_5.generated.HoldingPattern;
import org.mitre.boogie.xml.v23_5.generated.HoldingUses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArincHoldingPatternConverter implements Function<HoldingPattern, Optional<ArincHoldingPattern>> {
  public static final ArincHoldingPatternConverter INSTANCE = new ArincHoldingPatternConverter();

  public static final Logger LOG = LoggerFactory.getLogger(ArincHoldingPatternConverter.class);

  private static final ArincHoldingPatternValidator VALIDATOR = ArincHoldingPatternValidator.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final FlatCourseConverter COURSE_CONVERTER = FlatCourseConverter.INSTANCE;
  private static final FlatMinMaxAltitudeConstraintConverter MIN_MAX_ALT_CONVERTER = FlatMinMaxAltitudeConstraintConverter.INSTANCE;

  private ArincHoldingPatternConverter() {
  }

  @Override
  public Optional<ArincHoldingPattern> apply(HoldingPattern holdingPattern) {
    return Optional.ofNullable(holdingPattern)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincHoldingPattern convert(HoldingPattern holdingPattern) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(holdingPattern);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(holdingPattern);

    Optional<FlatCourse> inboundCourse = COURSE_CONVERTER.apply(holdingPattern.getInboundHoldingCourse());
    Optional<FlatMinMaxAltitudeConstraint> holdMinMax = MIN_MAX_ALT_CONVERTER.apply(holdingPattern.getHoldMinMaxAltitudes());
    Optional<FlatMinMaxAltitudeConstraint> rvsmMinMax = MIN_MAX_ALT_CONVERTER.apply(holdingPattern.getRvsmMinMaxLevels());

    HoldingUses holdingUses = holdingPattern.getHoldingUses();
    Boolean isOnUndefined = extractHoldingUsesValue(holdingUses, HoldingUses::isIsOnUndefined);
    Boolean isOnHigh = extractHoldingUsesValue(holdingUses, HoldingUses::isIsOnHigh);
    Boolean isOnLow = extractHoldingUsesValue(holdingUses, HoldingUses::isIsOnLow);
    Boolean isOnSid = extractHoldingUsesValue(holdingUses, HoldingUses::isIsOnSid);
    Boolean isOnStar = extractHoldingUsesValue(holdingUses, HoldingUses::isIsOnStar);
    Boolean isOnApproach = extractHoldingUsesValue(holdingUses, HoldingUses::isIsOnApproach);
    Boolean isOnMissed = extractHoldingUsesValue(holdingUses, HoldingUses::isIsOnMissedApproach);

    return ArincHoldingPattern.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .isEnroute(holdingPattern.isIsEnroute())
        .arcRadius(Optional.ofNullable(holdingPattern.getArcRadius()).map(BigDecimal::doubleValue).orElse(null))
        .holdingPatternName(holdingPattern.getHoldingPatternName())
        .holdingSpeed(holdingPattern.getHoldingSpeed())
        .holdingTime(Optional.ofNullable(holdingPattern.getHoldingTime()).flatMap(XmlDurationConverter.INSTANCE).orElse(null))
        .holdingDistance(Optional.ofNullable(holdingPattern.getHoldingDistance()).map(BigDecimal::doubleValue).orElse(null))
        .inboundHoldingCourseValue(inboundCourse.map(FlatCourse::value).orElse(null))
        .inboundHoldingCourseIsTrue(inboundCourse.map(FlatCourse::isTrue).orElse(null))
        .legInboundOutboundIndicator(Optional.ofNullable(holdingPattern.getLegInboundOutboundIndicator()).map(Enum::name).orElse(null))
        .holdMinAltitude(holdMinMax.map(FlatMinMaxAltitudeConstraint::minimumAltitude).orElse(null))
        .holdMaxAltitude(holdMinMax.map(FlatMinMaxAltitudeConstraint::maximumAltitude).orElse(null))
        .rnp(Optional.ofNullable(holdingPattern.getRnp()).map(BigDecimal::doubleValue).orElse(null))
        .rvsmMinAltitude(rvsmMinMax.map(FlatMinMaxAltitudeConstraint::minimumAltitude).orElse(null))
        .rvsmMaxAltitude(rvsmMinMax.map(FlatMinMaxAltitudeConstraint::maximumAltitude).orElse(null))
        .turnDirection(Optional.ofNullable(holdingPattern.getTurnDirection()).map(Enum::name).orElse(null))
        .verticalScaleFactor(holdingPattern.getVerticalScaleFactor())
        .fixIdentifier(holdingPattern.getFixIdentifier())
        .fixRef(Optional.ofNullable(holdingPattern.getFixRef()).map(Object::toString).orElse(null))
        .portRef(Optional.ofNullable(holdingPattern.getPortRef()).map(Object::toString).orElse(null))
        .isOnUndefined(isOnUndefined)
        .isOnHigh(isOnHigh)
        .isOnLow(isOnLow)
        .isOnSid(isOnSid)
        .isOnStar(isOnStar)
        .isOnApproach(isOnApproach)
        .isOnMissedApproach(isOnMissed)
        .multipleIndicator(Optional.ofNullable(holdingPattern.getMultipleIndicator()).map(BigInteger::longValue).orElse(null))
        .inboundCourseNavaid(Optional.ofNullable(holdingPattern.getInboundCourseNavaid()).map(Object::toString).orElse(null))
        .inboundCourseTheta(Optional.ofNullable(holdingPattern.getInboundCourseTheta()).map(BigDecimal::doubleValue).orElse(null))
        .build();
  }

  private static <T> T extractHoldingUsesValue(HoldingUses holdingUses, java.util.function.Function<HoldingUses, T> extractor) {
    return Optional.ofNullable(holdingUses).map(extractor).orElse(null);
  }
}
