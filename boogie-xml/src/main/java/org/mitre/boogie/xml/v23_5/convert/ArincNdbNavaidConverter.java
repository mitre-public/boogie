package org.mitre.boogie.xml.v23_5.convert;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.NavaidNdbEmissionType;
import org.mitre.boogie.xml.model.fields.NavaidVoice;
import org.mitre.boogie.xml.model.fields.NavaidWeatherInfo;
import org.mitre.boogie.xml.model.fields.NdbNavaidCoverage;
import org.mitre.boogie.xml.model.fields.NdbNavaidIfMarkerInfo;
import org.mitre.boogie.xml.model.fields.NdbNavaidType;
import org.mitre.boogie.xml.v23_5.generated.Ndb;
import org.mitre.boogie.xml.v23_5.generated.NdbNavaidClass;

public final class ArincNdbNavaidConverter implements Function<Ndb, Optional<ArincNdbNavaid>> {
  public static final ArincNdbNavaidConverter INSTANCE = new ArincNdbNavaidConverter();

  private static final ArincNdbNavaidValidator VALIDATOR = ArincNdbNavaidValidator.INSTANCE;
  private static final ArincPointConverter POINT_CONVERTER = ArincPointConverter.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final FlatFrequencyConverter FREQ_CONVERTER = FlatFrequencyConverter.INSTANCE;

  private ArincNdbNavaidConverter() {
  }

  @Override
  public Optional<ArincNdbNavaid> apply(Ndb ndb) {
    return Optional.ofNullable(ndb)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincNdbNavaid convert(Ndb ndb) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(ndb);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(ndb);
    ArincPointInfo pointInfo = POINT_CONVERTER.apply(ndb);

    Optional<FlatFrequency> freq = FREQ_CONVERTER.apply(ndb.getNdbFrequency());

    Boolean isBfoRequired = Optional.ofNullable(ndb.getNdbClass())
        .map(NdbNavaidClass::isIsBfoRequired)
        .orElse(null);

    String ndbNavaidCoverage = Optional.ofNullable(ndb.getNdbClass())
        .map(NdbNavaidClass::getNdbNavaidCoverage)
        .map(Enum::name)
        .filter(NdbNavaidCoverage.VALID::contains)
        .orElse(null);

    String ndbNavaidIfMarker = Optional.ofNullable(ndb.getNdbClass())
        .map(NdbNavaidClass::getNdbNavaidIfMarker)
        .map(Enum::name)
        .filter(NdbNavaidIfMarkerInfo.VALID::contains)
        .orElse(null);

    String ndbNavaidType = Optional.ofNullable(ndb.getNdbClass())
        .map(NdbNavaidClass::getNdbNavaidType)
        .map(Enum::name)
        .filter(NdbNavaidType.VALID::contains)
        .orElse(null);

    String ndbNavaidWeatherInfo = Optional.ofNullable(ndb.getNdbClass())
        .map(NdbNavaidClass::getNdbNavaidWeatherInfo)
        .map(Enum::name)
        .filter(NavaidWeatherInfo.VALID::contains)
        .orElse(null);

    Boolean isNoVoice = Optional.ofNullable(ndb.getNdbClass())
        .map(NdbNavaidClass::isIsNoVoice)
        .orElse(null);

    return ArincNdbNavaid.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .dmeTacanRef(Optional.ofNullable(ndb.getDmeTacanRef()).map(Object::toString).orElse(null))
        .elevation(ndb.getElevation())
        .isVFRCheckpoint(ndb.isIsVFRCheckpoint())
        .frequencyValue(freq.map(FlatFrequency::value).orElse(null))
        .freqUnitOfMeasure(freq.map(FlatFrequency::unitOfMeasure).orElse(null))
        .typeOfEmission(ndb.getTypeOfEmission())
        .repetitionRate(ndb.getRepetitionRate())
        .navaidNdbEmissionType(Optional.ofNullable(ndb.getNavaidNdbEmissionType()).map(Enum::name).filter(NavaidNdbEmissionType.VALID::contains).orElse(null))
        .ndbVoice(Optional.ofNullable(ndb.getNdbVoice()).map(Enum::name).filter(NavaidVoice.VALID::contains).orElse(null))
        .isBfoRequired(isBfoRequired)
        .ndbNavaidCoverage(ndbNavaidCoverage)
        .ndbNavaidIfMarker(ndbNavaidIfMarker)
        .ndbNavaidType(ndbNavaidType)
        .ndbNavaidWeatherInfo(ndbNavaidWeatherInfo)
        .isNoVoice(isNoVoice)
        .build();
  }
}
