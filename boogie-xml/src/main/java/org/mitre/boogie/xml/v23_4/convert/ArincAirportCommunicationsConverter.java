package org.mitre.boogie.xml.v23_4.convert;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincAirportCommunications;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.CommunicationClass;
import org.mitre.boogie.xml.model.fields.CommunicationType;
import org.mitre.boogie.xml.model.fields.DistanceDescription;
import org.mitre.boogie.xml.model.fields.FrequencyUnits;
import org.mitre.boogie.xml.model.fields.H24Indicator;
import org.mitre.boogie.xml.model.fields.Modulation;
import org.mitre.boogie.xml.model.fields.RadarService;
import org.mitre.boogie.xml.model.fields.SignalEmission;
import org.mitre.boogie.xml.v23_4.util.LocationConverter;
import org.mitre.boogie.xml.v23_4.generated.PortCommunication;
import org.mitre.caasd.commons.LatLong;

public final class ArincAirportCommunicationsConverter implements Function<PortCommunication, Optional<ArincAirportCommunications>> {
  public static final ArincAirportCommunicationsConverter INSTANCE = new ArincAirportCommunicationsConverter();

  private static final ArincAirportCommunicationsValidator VALIDATOR = ArincAirportCommunicationsValidator.INSTANCE;
  private static final ArincRecordConverter RECORD_CONVERTER = ArincRecordConverter.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final LocationConverter LOCATION_CONVERTER = LocationConverter.INSTANCE;
  private static final FlatFrequencyConverter FREQ_CONVERTER = FlatFrequencyConverter.INSTANCE;

  private ArincAirportCommunicationsConverter() {
  }

  @Override
  public Optional<ArincAirportCommunications> apply(PortCommunication comm) {
    return Optional.ofNullable(comm)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincAirportCommunications convert(PortCommunication comm) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(comm);
    ArincRecordInfo recordInfo = RECORD_CONVERTER.apply(comm);

    LatLong location = Optional.ofNullable(comm.getLocation())
        .flatMap(LOCATION_CONVERTER)
        .orElse(null);

    Optional<FlatFrequency> transmitFreq = FREQ_CONVERTER.apply(comm.getTransmitFrequency());
    Optional<FlatFrequency> receiveFreq = FREQ_CONVERTER.apply(comm.getReceiveFrequency());

    return ArincAirportCommunications.builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .remoteFacilityRef(Optional.ofNullable(comm.getRemoteFacilityRef()).map(Object::toString).orElse(null))
        .location(location)
        .callSign(comm.getCallSign())
        .communicationClass(Optional.ofNullable(comm.getCommunicationClass()).map(Enum::name).filter(CommunicationClass.VALID::contains).orElse(null))
        .communicationType(Optional.ofNullable(comm.getCommunicationType()).map(Enum::name).filter(CommunicationType.VALID::contains).orElse(null))
        .frequencyUnits(Optional.ofNullable(comm.getFrequencyUnits()).map(Enum::name).filter(FrequencyUnits.VALID::contains).orElse(null))
        .radarService(Optional.ofNullable(comm.getRadarService()).map(Enum::name).filter(RadarService.VALID::contains).orElse(null))
        .h24Indicator(Optional.ofNullable(comm.getH24Indicator()).map(Enum::name).filter(H24Indicator.VALID::contains).orElse(null))
        .modulation(Optional.ofNullable(comm.getModulation()).map(Enum::name).filter(Modulation.VALID::contains).orElse(null))
        .sequenceNumber((int) comm.getSequenceNumber())
        .signalEmission(Optional.ofNullable(comm.getSignalEmission()).map(Enum::name).filter(SignalEmission.VALID::contains).orElse(null))
        .transmitFrequencyValue(transmitFreq.map(FlatFrequency::value).orElse(null))
        .transmitFreqUnitOfMeasure(transmitFreq.map(FlatFrequency::unitOfMeasure).orElse(null))
        .receiveFrequencyValue(receiveFreq.map(FlatFrequency::value).orElse(null))
        .receiveFreqUnitOfMeasure(receiveFreq.map(FlatFrequency::unitOfMeasure).orElse(null))
        .transmitterSiteElevation(comm.getTransmitterSiteElevation())
        .communicationDistance(comm.getCommunicationDistance())
        .distanceDescription(Optional.ofNullable(comm.getDistanceDescription()).map(Enum::name).filter(DistanceDescription.VALID::contains).orElse(null))
        .build();
  }
}
