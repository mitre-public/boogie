package org.mitre.boogie.xml.v23_4.convert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincGnssLandingSystem;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.PrecisionApproachCategory;
import org.mitre.boogie.xml.v23_4.generated.Bearing;
import org.mitre.boogie.xml.v23_4.generated.Gls;

final class ArincGnssLandingSystemConverter implements Function<Gls, Optional<ArincGnssLandingSystem>> {
  static final ArincGnssLandingSystemConverter INSTANCE = new ArincGnssLandingSystemConverter();

  private static final ArincGnssLandingSystemValidator VALIDATOR = ArincGnssLandingSystemValidator.INSTANCE;
  private static final ArincPointConverter POINT_CONVERTER = ArincPointConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final FlatRunwayIdentifierConverter RWY_CONVERTER = FlatRunwayIdentifierConverter.INSTANCE;

  private ArincGnssLandingSystemConverter() {
  }

  @Override
  public Optional<ArincGnssLandingSystem> apply(Gls gls) {
    return Optional.ofNullable(gls)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincGnssLandingSystem convert(Gls gls) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(gls);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(gls);
    ArincPointInfo pointInfo = POINT_CONVERTER.apply(gls);

    Double approachCourseBearingValue = Optional.ofNullable(gls.getApproachCourseBearing())
        .map(Bearing::getBearingValue)
        .map(BigDecimal::doubleValue)
        .orElse(null);

    Boolean approachCourseBearingIsTrueBearing = Optional.ofNullable(gls.getApproachCourseBearing())
        .map(Bearing::isIsTrueBearing)
        .orElse(null);

    Optional<FlatRunwayIdentifier> rwy = RWY_CONVERTER.apply(gls.getRunwayIdentifier());

    return ArincGnssLandingSystem.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .elevation(gls.getElevation())
        .isVFRCheckpoint(gls.isIsVFRCheckpoint())
        .runwayRef(Optional.ofNullable(gls.getRunwayRef()).map(Object::toString).orElse(null))
        .approachAngle(Optional.ofNullable(gls.getApproachAngle()).map(BigDecimal::doubleValue).orElse(null))
        .approachCourseBearingValue(approachCourseBearingValue)
        .approachCourseBearingIsTrueBearing(approachCourseBearingIsTrueBearing)
        .category(Optional.ofNullable(gls.getCategory()).map(Enum::name).filter(PrecisionApproachCategory.VALID::contains).orElse(null))
        .runwayNumber(rwy.map(FlatRunwayIdentifier::number).orElse(null))
        .runwayLeftRightCenterType(rwy.map(FlatRunwayIdentifier::leftRightCenterType).orElse(null))
        .runwaySuffix(rwy.map(FlatRunwayIdentifier::suffix).orElse(null))
        .serviceVolumeRadius(gls.getServiceVolumeRadius())
        .stationElevationWgs84(gls.getStationElevationWgs84())
        .stationType(gls.getStationType())
        .tdmaSlots(gls.getTdmaSlots())
        .glsChannel(gls.getGlsChannel())
        .thresholdCrossingHeight(gls.getThresholdCrossingHeight())
        .build();
  }
}
