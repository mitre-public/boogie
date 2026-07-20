package org.mitre.boogie.xml.v23_5.convert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincLocalizerGlideslopeMarker;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.LocatorAddInfo;
import org.mitre.boogie.xml.model.fields.LocatorCollocation;
import org.mitre.boogie.xml.model.fields.LocatorCoverage;
import org.mitre.boogie.xml.model.fields.LocatorFacility1;
import org.mitre.boogie.xml.model.fields.LocatorFacility2;
import org.mitre.boogie.xml.model.fields.MarkerType;
import org.mitre.boogie.xml.model.fields.NavaidWeatherInfo;
import org.mitre.boogie.xml.v23_5.generated.AirportHeliportLocalizerMarker;
import org.mitre.boogie.xml.v23_5.generated.LocatorClass;

public final class ArincLocalizerGlideslopeMarkerConverter implements Function<AirportHeliportLocalizerMarker, Optional<ArincLocalizerGlideslopeMarker>> {
  public static final ArincLocalizerGlideslopeMarkerConverter INSTANCE = new ArincLocalizerGlideslopeMarkerConverter();

  private static final ArincLocalizerGlideslopeMarkerValidator VALIDATOR = ArincLocalizerGlideslopeMarkerValidator.INSTANCE;
  private static final ArincPointConverter POINT_CONVERTER = ArincPointConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final FlatFrequencyConverter FREQ_CONVERTER = FlatFrequencyConverter.INSTANCE;
  private static final FlatRunwayIdentifierConverter RWY_CONVERTER = FlatRunwayIdentifierConverter.INSTANCE;

  private ArincLocalizerGlideslopeMarkerConverter() {
  }

  @Override
  public Optional<ArincLocalizerGlideslopeMarker> apply(AirportHeliportLocalizerMarker marker) {
    return Optional.ofNullable(marker)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincLocalizerGlideslopeMarker convert(AirportHeliportLocalizerMarker marker) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(marker);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(marker);
    ArincPointInfo pointInfo = POINT_CONVERTER.apply(marker);

    Optional<FlatFrequency> freq = FREQ_CONVERTER.apply(marker.getLocatorFrequency());
    Optional<FlatRunwayIdentifier> rwy = RWY_CONVERTER.apply(marker.getRunwayIdentifier());

    String locatorFacility1 = Optional.ofNullable(marker.getLocatorClass())
        .map(LocatorClass::getAhLocalizerMarkerLocatorFacility1)
        .map(Enum::name)
        .filter(LocatorFacility1.VALID::contains)
        .orElse(null);

    String locatorFacility2 = Optional.ofNullable(marker.getLocatorClass())
        .map(LocatorClass::getAhLocalizerMarkerLocatorFacility2)
        .map(Enum::name)
        .filter(LocatorFacility2.VALID::contains)
        .orElse(null);

    String locatorCoverage = Optional.ofNullable(marker.getLocatorClass())
        .map(LocatorClass::getAhLocalizerMarkerLocatorCoverage)
        .map(Enum::name)
        .filter(LocatorCoverage.VALID::contains)
        .orElse(null);

    String locatorAddInfo = Optional.ofNullable(marker.getLocatorClass())
        .map(LocatorClass::getAhLocalizerMarkerLocatorAddInfo)
        .map(Enum::name)
        .filter(LocatorAddInfo.VALID::contains)
        .orElse(null);

    String locatorWeatherInfo = Optional.ofNullable(marker.getLocatorClass())
        .map(LocatorClass::getAhLocalizerMarkerWeatherInfo)
        .map(Enum::name)
        .filter(NavaidWeatherInfo.VALID::contains)
        .orElse(null);

    String locatorCollocation = Optional.ofNullable(marker.getLocatorClass())
        .map(LocatorClass::getAhLocalizerMarkerLocatorCollocation)
        .map(Enum::name)
        .filter(LocatorCollocation.VALID::contains)
        .orElse(null);

    return ArincLocalizerGlideslopeMarker.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .localizerRef(Optional.ofNullable(marker.getLocalizerRef()).map(Object::toString).orElse(null))
        .elevation(marker.getElevation())
        .locatorFacilityCharacteristics(marker.getLocatorFacilityCharacteristics())
        .locatorFrequencyValue(freq.map(FlatFrequency::value).orElse(null))
        .locatorFreqUnitOfMeasure(freq.map(FlatFrequency::unitOfMeasure).orElse(null))
        .markerType(Optional.ofNullable(marker.getMarkerType()).map(Enum::name).filter(MarkerType.VALID::contains).orElse(null))
        .minorAxisBearing(Optional.ofNullable(marker.getMinorAxisBearing()).map(BigDecimal::doubleValue).orElse(null))
        .runwayNumber(rwy.map(FlatRunwayIdentifier::number).orElse(null))
        .runwayLeftRightCenterType(rwy.map(FlatRunwayIdentifier::leftRightCenterType).orElse(null))
        .runwaySuffix(rwy.map(FlatRunwayIdentifier::suffix).orElse(null))
        .locatorFacility1(locatorFacility1)
        .locatorFacility2(locatorFacility2)
        .locatorCoverage(locatorCoverage)
        .locatorAddInfo(locatorAddInfo)
        .locatorWeatherInfo(locatorWeatherInfo)
        .locatorCollocation(locatorCollocation)
        .build();
  }
}
