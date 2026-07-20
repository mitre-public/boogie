package org.mitre.boogie.xml.v23_5.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincLocalizerGlideSlope;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.IlsBackCourse;
import org.mitre.boogie.xml.model.fields.IlsDmeLocation;
import org.mitre.boogie.xml.model.fields.LocalizerPositionReference;
import org.mitre.boogie.xml.model.fields.PrecisionApproachCategory;
import org.mitre.boogie.xml.model.fields.TrueBearingSource;
import org.mitre.boogie.xml.v23_5.util.LocationConverter;
import org.mitre.boogie.xml.v23_5.generated.Bearing;
import org.mitre.boogie.xml.v23_5.generated.LocalizerGlideslope;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.MagneticVariation;

public final class ArincLocalizerGlideSlopeConverter implements Function<LocalizerGlideslope, Optional<ArincLocalizerGlideSlope>> {
  public static final ArincLocalizerGlideSlopeConverter INSTANCE = new ArincLocalizerGlideSlopeConverter();

  private static final ArincLocalizerGlideSlopeValidator VALIDATOR = ArincLocalizerGlideSlopeValidator.INSTANCE;
  private static final ArincPointConverter POINT_CONVERTER = ArincPointConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final LocationConverter LOCATION_CONVERTER = LocationConverter.INSTANCE;
  private static final FlatRunwayIdentifierConverter RWY_CONVERTER = FlatRunwayIdentifierConverter.INSTANCE;
  private static final FlatStationDeclinationConverter SD_CONVERTER = FlatStationDeclinationConverter.INSTANCE;
  private static final FlatFrequencyConverter FREQ_CONVERTER = FlatFrequencyConverter.INSTANCE;
  private static final FlatVhfNavaidClassConverter NC_CONVERTER = FlatVhfNavaidClassConverter.INSTANCE;

  private ArincLocalizerGlideSlopeConverter() {
  }

  @Override
  public Optional<ArincLocalizerGlideSlope> apply(LocalizerGlideslope lg) {
    return Optional.ofNullable(lg)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincLocalizerGlideSlope convert(LocalizerGlideslope lg) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(lg);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(lg);
    ArincPointInfo pointInfo = POINT_CONVERTER.apply(lg);

    Double approachCourseBearingValue = Optional.ofNullable(lg.getApproachCourseBearing())
        .map(Bearing::getBearingValue)
        .map(BigDecimal::doubleValue)
        .orElse(null);

    Boolean approachCourseBearingIsTrueBearing = Optional.ofNullable(lg.getApproachCourseBearing())
        .map(Bearing::isIsTrueBearing)
        .orElse(null);

    Optional<FlatRunwayIdentifier> rwy = RWY_CONVERTER.apply(lg.getRunwayIdentifier());

    LatLong glideslopeLocation = Optional.ofNullable(lg.getGlideslopeLocation())
        .flatMap(LOCATION_CONVERTER)
        .orElse(null);

    MagneticVariation sd = SD_CONVERTER.apply(lg.getStationDeclination());
    Optional<FlatFrequency> freq = FREQ_CONVERTER.apply(lg.getLocalizerGlideslopeFrequency());
    Optional<FlatVhfNavaidClass> nc = NC_CONVERTER.apply(lg.getNavaidClass());

    List<String> approachRouteIdent = new ArrayList<>(lg.getApproachRouteIdent());

    return ArincLocalizerGlideSlope.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .supportingFacilityRef(Optional.ofNullable(lg.getSupportingFacilityReference()).map(Object::toString).orElse(null))
        .elevation(lg.getElevation())
        .isVFRCheckpoint(lg.isIsVFRCheckpoint())
        .runwayRef(Optional.ofNullable(lg.getRunwayRef()).map(Object::toString).orElse(null))
        .approachAngle(Optional.ofNullable(lg.getApproachAngle()).map(BigDecimal::doubleValue).orElse(null))
        .approachCourseBearingValue(approachCourseBearingValue)
        .approachCourseBearingIsTrueBearing(approachCourseBearingIsTrueBearing)
        .category(Optional.ofNullable(lg.getCategory()).map(Enum::name).filter(PrecisionApproachCategory.VALID::contains).orElse(null))
        .runwayNumber(rwy.map(FlatRunwayIdentifier::number).orElse(null))
        .runwayLeftRightCenterType(rwy.map(FlatRunwayIdentifier::leftRightCenterType).orElse(null))
        .runwaySuffix(rwy.map(FlatRunwayIdentifier::suffix).orElse(null))
        .approachRouteIdent(approachRouteIdent)
        .glideslopeBeamWidth(Optional.ofNullable(lg.getGlideslopeBeamWidth()).map(BigDecimal::doubleValue).orElse(null))
        .glideslopeHeightAtLandingThreshold(lg.getGlideslopeHeightAtLandingThreshold())
        .glideslopeLocation(glideslopeLocation)
        .glideslopePosition(lg.getGlideslopePosition())
        .localizerPosition(lg.getLocalizerPosition())
        .localizerPositionReference(Optional.ofNullable(lg.getLocalizerPositionReference()).map(Enum::name).filter(LocalizerPositionReference.VALID::contains).orElse(null))
        .localizerTrueBearing(Optional.ofNullable(lg.getLocalizerTrueBearing()).map(BigDecimal::doubleValue).orElse(null))
        .localizerTrueBearingSource(Optional.ofNullable(lg.getLocalizerTrueBearingSource()).map(Enum::name).filter(TrueBearingSource.VALID::contains).orElse(null))
        .localizerWidth(Optional.ofNullable(lg.getLocalizerWidth()).map(BigDecimal::doubleValue).orElse(null))
        .stationDeclination(sd)
        .frequencyValue(freq.map(FlatFrequency::value).orElse(null))
        .freqUnitOfMeasure(freq.map(FlatFrequency::unitOfMeasure).orElse(null))
        .ilsBackCourse(Optional.ofNullable(lg.getIlsBackCourse()).map(Enum::name).filter(IlsBackCourse.VALID::contains).orElse(null))
        .ilsDmeLocation(Optional.ofNullable(lg.getIlsDmeLocation()).map(Enum::name).filter(IlsDmeLocation.VALID::contains).orElse(null))
        .vhfNavaidCoverage(nc.map(FlatVhfNavaidClass::coverage).orElse(null))
        .vhfNavaidWeatherInfo(nc.map(FlatVhfNavaidClass::weatherInfo).orElse(null))
        .isNotCollocated(nc.map(FlatVhfNavaidClass::isNotCollocated).orElse(null))
        .isBiased(nc.map(FlatVhfNavaidClass::isBiased).orElse(null))
        .isNoVoice(nc.map(FlatVhfNavaidClass::isNoVoice).orElse(null))
        .build();
  }
}
