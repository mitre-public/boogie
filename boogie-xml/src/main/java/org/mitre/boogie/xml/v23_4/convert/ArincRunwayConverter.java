package org.mitre.boogie.xml.v23_4.convert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincRunway;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ElevationType;
import org.mitre.boogie.xml.model.fields.HelicopterPerformanceReq;
import org.mitre.boogie.xml.model.fields.LandingThresholdElevationAccuracyCompliance;
import org.mitre.boogie.xml.model.fields.RunwayAccuracyCompliance;
import org.mitre.boogie.xml.model.fields.RunwayLeftRightCenterType;
import org.mitre.boogie.xml.model.fields.RunwaySuffix;
import org.mitre.boogie.xml.model.fields.RunwaySurfaceCode;
import org.mitre.boogie.xml.model.fields.RunwayUsageIndicator;
import org.mitre.boogie.xml.model.fields.SurfaceType;
import org.mitre.boogie.xml.model.fields.TchValueIndicator;
import org.mitre.boogie.xml.model.fields.TrueBearingSource;
import org.mitre.boogie.xml.v23_4.util.LocationConverter;
import org.mitre.boogie.xml.v23_4.generated.Bearing;
import org.mitre.boogie.xml.v23_4.generated.Runway;
import org.mitre.boogie.xml.v23_4.generated.RunwayIdentifier;
import org.mitre.caasd.commons.LatLong;

public final class ArincRunwayConverter implements Function<Runway, Optional<ArincRunway>> {
  public static final ArincRunwayConverter INSTANCE = new ArincRunwayConverter();

  private static final ArincRunwayValidator VALIDATOR = ArincRunwayValidator.INSTANCE;
  private static final ArincPointConverter POINT_CONVERTER = ArincPointConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final LocationConverter LOCATION_CONVERTER = LocationConverter.INSTANCE;

  private ArincRunwayConverter() {
  }

  @Override
  public Optional<ArincRunway> apply(Runway runway) {
    return Optional.ofNullable(runway)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincRunway convert(Runway runway) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(runway);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(runway);
    ArincPointInfo pointInfo = POINT_CONVERTER.apply(runway);

    LatLong runwayEndLocation = Optional.ofNullable(runway.getRunwayEndLocation())
        .flatMap(LOCATION_CONVERTER)
        .orElse(null);

    Double runwayBearing = Optional.ofNullable(runway.getRunwayBearing())
        .map(Bearing::getBearingValue)
        .map(BigDecimal::doubleValue)
        .orElse(null);

    Boolean runwayBearingIsTrueBearing = Optional.ofNullable(runway.getRunwayBearing())
        .map(Bearing::isIsTrueBearing)
        .orElse(null);

    String surfaceCode = Optional.ofNullable(runway.getSurfaceCode())
        .map(Enum::name)
        .filter(RunwaySurfaceCode.VALID::contains)
        .orElse(null);

    Integer runwayNumber = Optional.ofNullable(runway.getRunwayIdentifier())
        .map(id -> (int) id.getRunwayNumber())
        .orElse(null);

    String runwayLeftRightCenterType = Optional.ofNullable(runway.getRunwayIdentifier())
        .map(RunwayIdentifier::getRunwayLeftRightCenterType)
        .map(Enum::name)
        .filter(RunwayLeftRightCenterType.VALID::contains)
        .orElse(null);

    String runwaySuffix = Optional.ofNullable(runway.getRunwayIdentifier())
        .map(RunwayIdentifier::getRunwaySuffix)
        .map(Enum::name)
        .filter(RunwaySuffix.VALID::contains)
        .orElse(null);

    String runwayAccuracyCompliance = Optional.ofNullable(runway.getRunwayAccuracy())
        .map(Runway.RunwayAccuracy::getRunwayAccuracyCompliance)
        .map(Enum::name)
        .filter(RunwayAccuracyCompliance.VALID::contains)
        .orElse(null);

    String landingThresholdElevationAccuracyCompliance = Optional.ofNullable(runway.getRunwayAccuracy())
        .map(Runway.RunwayAccuracy::getLandingThresholdElevationAccuracyCompliance)
        .map(Enum::name)
        .filter(LandingThresholdElevationAccuracyCompliance.VALID::contains)
        .orElse(null);

    return ArincRunway.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .isWithoutLocation(runway.isIsWithoutLocation())
        .isDerivedLocation(runway.isIsDerivedLocation())
        .displacedThresholdDistance(runway.getDisplacedThresholdDistance())
        .landingThresholdElevation(runway.getLandingThresholdElevation())
        .landingThresholdElevationType(Optional.ofNullable(runway.getLandingThresholdElevationType()).map(Enum::name).filter(ElevationType.VALID::contains).orElse(null))
        .runwayEndLocation(runwayEndLocation)
        .runwayEndElevation(runway.getRunwayEndElevation())
        .ltpEllipsoidHeight(Optional.ofNullable(runway.getLtpEllipsoidHeight()).map(BigDecimal::doubleValue).orElse(null))
        .runwayBearing(runwayBearing)
        .runwayBearingIsTrueBearing(runwayBearingIsTrueBearing)
        .runwayTrueBearing(Optional.ofNullable(runway.getRunwayTrueBearing()).map(BigDecimal::doubleValue).orElse(null))
        .runwayTrueBearingSource(Optional.ofNullable(runway.getRunwayTrueBearingSource()).map(Enum::name).filter(TrueBearingSource.VALID::contains).orElse(null))
        .runwayDescription(runway.getRunwayDescription())
        .runwayGradient(Optional.ofNullable(runway.getRunwayGradient()).map(BigDecimal::doubleValue).orElse(null))
        .runwayNumber(runwayNumber)
        .runwayLeftRightCenterType(runwayLeftRightCenterType)
        .runwaySuffix(runwaySuffix)
        .runwayLength(runway.getRunwayLength())
        .runwayWidth(runway.getRunwayWidth())
        .stopway(runway.getStopway())
        .tchValueIndicator(Optional.ofNullable(runway.getTchValueIndicator()).map(Enum::name).filter(TchValueIndicator.VALID::contains).orElse(null))
        .thresholdCrossingHeight(runway.getThresholdCrossingHeight())
        .touchDownZoneElevation(runway.getTouchDownZoneElevation())
        .starterExtension(runway.getStarterExtension())
        .surfaceCode(surfaceCode)
        .surfaceType(Optional.ofNullable(runway.getSurfaceType()).map(Enum::name).filter(SurfaceType.VALID::contains).orElse(null))
        .helicopterPerformanceReq(Optional.ofNullable(runway.getHelicopterPerformanceReq()).map(Enum::name).filter(HelicopterPerformanceReq.VALID::contains).orElse(null))
        .takeOffRunwayAvailable(runway.getTakeOffRunwayAvailable())
        .takeOffDistanceAvailable(runway.getTakeOffDistanceAvailable())
        .accelerateStopDistanceAvailable(runway.getAccelerateStopDistanceAvailable())
        .landingDistanceAvailable(runway.getLandingDistanceAvailable())
        .runwayUsageIndicator(Optional.ofNullable(runway.getRunwayUsageIndicator()).map(Enum::name).filter(RunwayUsageIndicator.VALID::contains).orElse(null))
        .runwayAccuracyCompliance(runwayAccuracyCompliance)
        .landingThresholdElevationAccuracyCompliance(landingThresholdElevationAccuracyCompliance)
        .build();
  }

}
