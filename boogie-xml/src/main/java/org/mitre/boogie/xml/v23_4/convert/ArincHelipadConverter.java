package org.mitre.boogie.xml.v23_4.convert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.boogie.xml.model.ArincHelipad;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ElevationType;
import org.mitre.boogie.xml.model.fields.HelicopterPerformanceReq;
import org.mitre.boogie.xml.model.fields.HelipadShape;
import org.mitre.boogie.xml.model.fields.RunwaySurfaceCode;
import org.mitre.boogie.xml.model.fields.SurfaceType;
import org.mitre.boogie.xml.v23_4.generated.Bearing;
import org.mitre.boogie.xml.v23_4.generated.Helipad;
import org.mitre.boogie.xml.v23_4.generated.HelipadDimensions;

public final class ArincHelipadConverter implements Function<Helipad, Optional<ArincHelipad>> {
  public static final ArincHelipadConverter INSTANCE = new ArincHelipadConverter();

  private static final ArincHelipadValidator VALIDATOR = ArincHelipadValidator.INSTANCE;
  private static final ArincPointConverter POINT_CONVERTER = ArincPointConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;

  private ArincHelipadConverter() {
  }

  @Override
  public Optional<ArincHelipad> apply(Helipad helipad) {
    return Optional.ofNullable(helipad)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincHelipad convert(Helipad helipad) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(helipad);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(helipad);
    ArincPointInfo pointInfo = POINT_CONVERTER.apply(helipad);

    String surfaceCode = Optional.ofNullable(helipad.getSurfaceCode())
        .map(Enum::name)
        .filter(RunwaySurfaceCode.VALID::contains)
        .orElse(null);

    List<Double> preferredApproachBearings = helipad.getPreferredApproachBearing().stream()
        .map(Bearing::getBearingValue)
        .map(BigDecimal::doubleValue)
        .collect(Collectors.toList());

    return ArincHelipad.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .isWithoutLocation(helipad.isIsWithoutLocation())
        .isDerivedLocation(helipad.isIsDerivedLocation())
        .elevation(helipad.getElevation())
        .elevationType(Optional.ofNullable(helipad.getElevationType()).map(Enum::name).filter(ElevationType.VALID::contains).orElse(null))
        .tlofPadLengthLongSide(Optional.ofNullable(helipad.getHelipadTlofDimensions()).map(HelipadDimensions::getPadLengthLongSide).orElse(null))
        .tlofPadLengthShortSide(Optional.ofNullable(helipad.getHelipadTlofDimensions()).map(HelipadDimensions::getPadLengthShortSide).orElse(null))
        .tlofPadDiameter(Optional.ofNullable(helipad.getHelipadTlofDimensions()).map(HelipadDimensions::getPadDiameter).orElse(null))
        .fatoPadLengthLongSide(Optional.ofNullable(helipad.getHelipadFatoDimensions()).map(HelipadDimensions::getPadLengthLongSide).orElse(null))
        .fatoPadLengthShortSide(Optional.ofNullable(helipad.getHelipadFatoDimensions()).map(HelipadDimensions::getPadLengthShortSide).orElse(null))
        .fatoPadDiameter(Optional.ofNullable(helipad.getHelipadFatoDimensions()).map(HelipadDimensions::getPadDiameter).orElse(null))
        .safetyPadLengthLongSide(Optional.ofNullable(helipad.getSafetyDimensions()).map(HelipadDimensions::getPadLengthLongSide).orElse(null))
        .safetyPadLengthShortSide(Optional.ofNullable(helipad.getSafetyDimensions()).map(HelipadDimensions::getPadLengthShortSide).orElse(null))
        .safetyPadDiameter(Optional.ofNullable(helipad.getSafetyDimensions()).map(HelipadDimensions::getPadDiameter).orElse(null))
        .helipadShape(Optional.ofNullable(helipad.getHelipadShape()).map(Enum::name).filter(HelipadShape.VALID::contains).orElse(null))
        .surfaceCode(surfaceCode)
        .surfaceType(Optional.ofNullable(helipad.getSurfaceType()).map(Enum::name).filter(SurfaceType.VALID::contains).orElse(null))
        .helicopterPerformanceReq(Optional.ofNullable(helipad.getHelicopterPerformanceReq()).map(Enum::name).filter(HelicopterPerformanceReq.VALID::contains).orElse(null))
        .isElevated(helipad.isIsElevated())
        .helipadMaximumRotorDiameter(helipad.getHelipadMaximumRotorDiameter())
        .helipadOrientationBearing(Optional.ofNullable(helipad.getHelipadOrientation()).map(Bearing::getBearingValue).map(BigDecimal::doubleValue).orElse(null))
        .helipadOrientationIsTrueBearing(Optional.ofNullable(helipad.getHelipadOrientation()).map(Bearing::isIsTrueBearing).orElse(null))
        .helipadIdentifierOrientationBearing(Optional.ofNullable(helipad.getHelipadIdentifierOrientation()).map(Bearing::getBearingValue).map(BigDecimal::doubleValue).orElse(null))
        .helipadIdentifierOrientationIsTrueBearing(Optional.ofNullable(helipad.getHelipadIdentifierOrientation()).map(Bearing::isIsTrueBearing).orElse(null))
        .preferredApproachBearings(preferredApproachBearings)
        .build();
  }
}
