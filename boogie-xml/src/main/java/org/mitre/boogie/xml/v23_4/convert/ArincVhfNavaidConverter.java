package org.mitre.boogie.xml.v23_4.convert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.FigureOfMerit;
import org.mitre.boogie.xml.model.fields.IlsDmeLocation;
import org.mitre.boogie.xml.model.fields.NavaidSynchronization;
import org.mitre.boogie.xml.model.fields.NavaidVoice;
import org.mitre.boogie.xml.model.fields.VorRangePower;
import org.mitre.boogie.xml.v23_4.generated.DmeTacan;
import org.mitre.boogie.xml.v23_4.generated.MilitaryTacan;
import org.mitre.boogie.xml.v23_4.generated.Navaid;
import org.mitre.boogie.xml.v23_4.generated.Tacan;
import org.mitre.boogie.xml.v23_4.generated.Vor;
import org.mitre.tdp.boogie.MagneticVariation;

public final class ArincVhfNavaidConverter implements Function<Navaid, Optional<ArincVhfNavaid>> {
  public static final ArincVhfNavaidConverter INSTANCE = new ArincVhfNavaidConverter();

  private static final ArincVhfNavaidValidator VALIDATOR = ArincVhfNavaidValidator.INSTANCE;
  private static final ArincPointConverter POINT_CONVERTER = ArincPointConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final FlatFrequencyConverter FREQ_CONVERTER = FlatFrequencyConverter.INSTANCE;
  private static final FlatStationDeclinationConverter SD_CONVERTER = FlatStationDeclinationConverter.INSTANCE;
  private static final FlatVhfNavaidClassConverter NC_CONVERTER = FlatVhfNavaidClassConverter.INSTANCE;

  private ArincVhfNavaidConverter() {
  }

  @Override
  public Optional<ArincVhfNavaid> apply(Navaid navaid) {
    return Optional.ofNullable(navaid)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincVhfNavaid convert(Navaid navaid) {
    if (navaid instanceof Vor vor) {
      return convertVor(vor);
    } else if (navaid instanceof DmeTacan dmeTacan) {
      return convertDmeTacan(dmeTacan);
    }
    throw new IllegalStateException("Unhandled navaid subtype for ArincVhfNavaidConverter: " + navaid.getClass().getSimpleName());
  }

  private ArincVhfNavaid convertVor(Vor vor) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(vor);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(vor);
    ArincPointInfo pointInfo = POINT_CONVERTER.apply(vor);

    Optional<FlatFrequency> freq = FREQ_CONVERTER.apply(vor.getVorFrequency());
    MagneticVariation sd = SD_CONVERTER.apply(vor.getStationDeclination());
    Optional<FlatVhfNavaidClass> nc = NC_CONVERTER.apply(vor.getNavaidClass());

    return ArincVhfNavaid.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .navaidSubType("VOR")
        .portRef(Optional.ofNullable(vor.getPortRef()).map(Object::toString).orElse(null))
        .dmeTacanRef(Optional.ofNullable(vor.getDmeTacanRef()).map(Object::toString).orElse(null))
        .elevation(vor.getElevation())
        .isVFRCheckpoint(vor.isIsVFRCheckpoint())
        .frequencyValue(freq.map(FlatFrequency::value).orElse(null))
        .freqUnitOfMeasure(freq.map(FlatFrequency::unitOfMeasure).orElse(null))
        .stationDeclination(sd)
        .figureOfMerit(Optional.ofNullable(vor.getFigureOfMerit()).map(Enum::name).filter(FigureOfMerit.VALID::contains).orElse(null))
        .frequencyProtection(vor.getFrequencyProtection())
        .navaidSynchronization(Optional.ofNullable(vor.getNavaidSynchronization()).map(Enum::name).filter(NavaidSynchronization.VALID::contains).orElse(null))
        .vorVoice(Optional.ofNullable(vor.getVorVoice()).map(Enum::name).filter(NavaidVoice.VALID::contains).orElse(null))
        .vorRangePower(Optional.ofNullable(vor.getVorRangePower()).map(Enum::name).filter(VorRangePower.VALID::contains).orElse(null))
        .vhfNavaidCoverage(nc.map(FlatVhfNavaidClass::coverage).orElse(null))
        .vhfNavaidWeatherInfo(nc.map(FlatVhfNavaidClass::weatherInfo).orElse(null))
        .isNotCollocated(nc.map(FlatVhfNavaidClass::isNotCollocated).orElse(null))
        .isBiased(nc.map(FlatVhfNavaidClass::isBiased).orElse(null))
        .isNoVoice(nc.map(FlatVhfNavaidClass::isNoVoice).orElse(null))
        .build();
  }

  private ArincVhfNavaid convertDmeTacan(DmeTacan dmeTacan) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(dmeTacan);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(dmeTacan);
    ArincPointInfo pointInfo = POINT_CONVERTER.apply(dmeTacan);

    Optional<FlatFrequency> freq = FREQ_CONVERTER.apply(dmeTacan.getFrequency());
    Optional<FlatVhfNavaidClass> nc = NC_CONVERTER.apply(dmeTacan.getNavaidClass());

    MagneticVariation sd = null;
    if (dmeTacan instanceof Tacan tacan) {
      sd = SD_CONVERTER.apply(tacan.getStationDeclination());
    } else if (dmeTacan instanceof MilitaryTacan milTacan) {
      sd = SD_CONVERTER.apply(milTacan.getStationDeclination());
    }

    String subType;
    if (dmeTacan instanceof Tacan) {
      subType = "TACAN";
    } else if (dmeTacan instanceof MilitaryTacan) {
      subType = "MILITARY_TACAN";
    } else {
      subType = "DME";
    }

    return ArincVhfNavaid.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .navaidSubType(subType)
        .portRef(Optional.ofNullable(dmeTacan.getPortRef()).map(Object::toString).orElse(null))
        .elevation(dmeTacan.getElevation())
        .isVFRCheckpoint(dmeTacan.isIsVFRCheckpoint())
        .frequencyValue(freq.map(FlatFrequency::value).orElse(null))
        .freqUnitOfMeasure(freq.map(FlatFrequency::unitOfMeasure).orElse(null))
        .stationDeclination(sd)
        .figureOfMerit(Optional.ofNullable(dmeTacan.getFigureOfMerit()).map(Enum::name).filter(FigureOfMerit.VALID::contains).orElse(null))
        .frequencyProtection(dmeTacan.getFrequencyProtection())
        .vhfNavaidCoverage(nc.map(FlatVhfNavaidClass::coverage).orElse(null))
        .vhfNavaidWeatherInfo(nc.map(FlatVhfNavaidClass::weatherInfo).orElse(null))
        .isNotCollocated(nc.map(FlatVhfNavaidClass::isNotCollocated).orElse(null))
        .isBiased(nc.map(FlatVhfNavaidClass::isBiased).orElse(null))
        .isNoVoice(nc.map(FlatVhfNavaidClass::isNoVoice).orElse(null))
        .ilsDmeBias(Optional.ofNullable(dmeTacan.getIlsDmeBias()).map(BigDecimal::doubleValue).orElse(null))
        .isIlsComponent(dmeTacan.isIsIlsComponent())
        .ilsDmeLocation(Optional.ofNullable(dmeTacan.getIlsDmeLocation()).map(Enum::name).filter(IlsDmeLocation.VALID::contains).orElse(null))
        .dmeExpandedServiceVolume(Optional.ofNullable(dmeTacan.getDmeExpandedServiceVolume()).map(Enum::name).orElse(null))
        .dmeOperationalServiceVolume(dmeTacan.getDmeOperationalServiceVolume())
        .isRouteInappropriateDme(dmeTacan.isIsRouteInappropriateDme())
        .isPaired(dmeTacan.isIsPaired())
        .isMlsP(dmeTacan.isIsMlsP())
        .build();
  }
}
