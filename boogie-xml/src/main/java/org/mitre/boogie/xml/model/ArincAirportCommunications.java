package org.mitre.boogie.xml.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.CommunicationClass;
import org.mitre.boogie.xml.model.fields.CommunicationType;
import org.mitre.boogie.xml.model.fields.DistanceDescription;
import org.mitre.boogie.xml.model.fields.FreqUnitOfMeasure;
import org.mitre.boogie.xml.model.fields.FrequencyUnits;
import org.mitre.boogie.xml.model.fields.H24Indicator;
import org.mitre.boogie.xml.model.fields.Modulation;
import org.mitre.boogie.xml.model.fields.RadarService;
import org.mitre.boogie.xml.model.fields.SignalEmission;
import org.mitre.caasd.commons.LatLong;

public final class ArincAirportCommunications {
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;
  // Communication fields
  private final String remoteFacilityRef;
  private final LatLong location;
  private final String callSign;
  /**
   * See {@link CommunicationClass}
   */
  private final String communicationClass;
  /**
   * See {@link CommunicationType}
   */
  private final String communicationType;
  /**
   * See {@link FrequencyUnits}
   */
  private final String frequencyUnits;
  /**
   * See {@link RadarService}
   */
  private final String radarService;
  /**
   * See {@link H24Indicator}
   */
  private final String h24Indicator;
  /**
   * See {@link Modulation}
   */
  private final String modulation;
  private final int sequenceNumber;
  /**
   * See {@link SignalEmission}
   */
  private final String signalEmission;
  // Transmit frequency (flattened from Frequency)
  private final Double transmitFrequencyValue;
  /**
   * See {@link FreqUnitOfMeasure}
   */
  private final String transmitFreqUnitOfMeasure;
  // Receive frequency (flattened from Frequency)
  private final Double receiveFrequencyValue;
  /**
   * See {@link FreqUnitOfMeasure}
   */
  private final String receiveFreqUnitOfMeasure;
  private final Integer transmitterSiteElevation;
  // PortCommunication fields
  private final Long communicationDistance;
  /**
   * See {@link DistanceDescription}
   */
  private final String distanceDescription;

  private ArincAirportCommunications(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.remoteFacilityRef = builder.remoteFacilityRef;
    this.location = builder.location;
    this.callSign = builder.callSign;
    this.communicationClass = builder.communicationClass;
    this.communicationType = builder.communicationType;
    this.frequencyUnits = builder.frequencyUnits;
    this.radarService = builder.radarService;
    this.h24Indicator = builder.h24Indicator;
    this.modulation = builder.modulation;
    this.sequenceNumber = builder.sequenceNumber;
    this.signalEmission = builder.signalEmission;
    this.transmitFrequencyValue = builder.transmitFrequencyValue;
    this.transmitFreqUnitOfMeasure = builder.transmitFreqUnitOfMeasure;
    this.receiveFrequencyValue = builder.receiveFrequencyValue;
    this.receiveFreqUnitOfMeasure = builder.receiveFreqUnitOfMeasure;
    this.transmitterSiteElevation = builder.transmitterSiteElevation;
    this.communicationDistance = builder.communicationDistance;
    this.distanceDescription = builder.distanceDescription;
  }

  public static Builder builder() {
    return new Builder();
  }

  public ArincBaseInfo baseInfo() {
    return baseInfo;
  }

  public ArincRecordInfo recordInfo() {
    return recordInfo;
  }

  public Optional<String> remoteFacilityRef() {
    return Optional.ofNullable(remoteFacilityRef);
  }

  public Optional<LatLong> location() {
    return Optional.ofNullable(location);
  }

  public Optional<String> callSign() {
    return Optional.ofNullable(callSign);
  }

  public Optional<CommunicationClass> communicationClass() {
    return Optional.ofNullable(communicationClass).filter(CommunicationClass.VALID::contains).map(CommunicationClass::valueOf);
  }

  public Optional<CommunicationType> communicationType() {
    return Optional.ofNullable(communicationType).filter(CommunicationType.VALID::contains).map(CommunicationType::valueOf);
  }

  public Optional<FrequencyUnits> frequencyUnits() {
    return Optional.ofNullable(frequencyUnits).filter(FrequencyUnits.VALID::contains).map(FrequencyUnits::valueOf);
  }

  public Optional<RadarService> radarService() {
    return Optional.ofNullable(radarService).filter(RadarService.VALID::contains).map(RadarService::valueOf);
  }

  public Optional<H24Indicator> h24Indicator() {
    return Optional.ofNullable(h24Indicator).filter(H24Indicator.VALID::contains).map(H24Indicator::valueOf);
  }

  public Optional<Modulation> modulation() {
    return Optional.ofNullable(modulation).filter(Modulation.VALID::contains).map(Modulation::valueOf);
  }

  public int sequenceNumber() {
    return sequenceNumber;
  }

  public Optional<SignalEmission> signalEmission() {
    return Optional.ofNullable(signalEmission).filter(SignalEmission.VALID::contains).map(SignalEmission::valueOf);
  }

  public Optional<Double> transmitFrequencyValue() {
    return Optional.ofNullable(transmitFrequencyValue);
  }

  public Optional<FreqUnitOfMeasure> transmitFreqUnitOfMeasure() {
    return Optional.ofNullable(transmitFreqUnitOfMeasure).filter(FreqUnitOfMeasure.VALID::contains).map(FreqUnitOfMeasure::valueOf);
  }

  public Optional<Double> receiveFrequencyValue() {
    return Optional.ofNullable(receiveFrequencyValue);
  }

  public Optional<FreqUnitOfMeasure> receiveFreqUnitOfMeasure() {
    return Optional.ofNullable(receiveFreqUnitOfMeasure).filter(FreqUnitOfMeasure.VALID::contains).map(FreqUnitOfMeasure::valueOf);
  }

  public Optional<Integer> transmitterSiteElevation() {
    return Optional.ofNullable(transmitterSiteElevation);
  }

  public Optional<Long> communicationDistance() {
    return Optional.ofNullable(communicationDistance);
  }

  public Optional<DistanceDescription> distanceDescription() {
    return Optional.ofNullable(distanceDescription).filter(DistanceDescription.VALID::contains).map(DistanceDescription::valueOf);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincAirportCommunications that = (ArincAirportCommunications) o;
    return sequenceNumber == that.sequenceNumber && Objects.equals(baseInfo, that.baseInfo) && Objects.equals(recordInfo, that.recordInfo) && Objects.equals(location, that.location) && Objects.equals(callSign, that.callSign) && Objects.equals(communicationClass, that.communicationClass) && Objects.equals(communicationType, that.communicationType) && Objects.equals(frequencyUnits, that.frequencyUnits) && Objects.equals(radarService, that.radarService) && Objects.equals(h24Indicator, that.h24Indicator) && Objects.equals(modulation, that.modulation) && Objects.equals(signalEmission, that.signalEmission) && Objects.equals(transmitFrequencyValue, that.transmitFrequencyValue) && Objects.equals(transmitFreqUnitOfMeasure, that.transmitFreqUnitOfMeasure) && Objects.equals(receiveFrequencyValue, that.receiveFrequencyValue) && Objects.equals(receiveFreqUnitOfMeasure, that.receiveFreqUnitOfMeasure) && Objects.equals(transmitterSiteElevation, that.transmitterSiteElevation) && Objects.equals(communicationDistance, that.communicationDistance) && Objects.equals(distanceDescription, that.distanceDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, recordInfo, location, callSign, communicationClass, communicationType, frequencyUnits, radarService, h24Indicator, modulation, sequenceNumber, signalEmission, transmitFrequencyValue, transmitFreqUnitOfMeasure, receiveFrequencyValue, receiveFreqUnitOfMeasure, transmitterSiteElevation, communicationDistance, distanceDescription);
  }

  @Override
  public String toString() {
    return "ArincAirportCommunications{" +
        "callSign='" + callSign + '\'' +
        ", communicationType='" + communicationType + '\'' +
        ", sequenceNumber=" + sequenceNumber +
        ", transmitFrequencyValue=" + transmitFrequencyValue +
        '}';
  }

  public static final class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private String remoteFacilityRef;
    private LatLong location;
    private String callSign;
    private String communicationClass;
    private String communicationType;
    private String frequencyUnits;
    private String radarService;
    private String h24Indicator;
    private String modulation;
    private int sequenceNumber;
    private String signalEmission;
    private Double transmitFrequencyValue;
    private String transmitFreqUnitOfMeasure;
    private Double receiveFrequencyValue;
    private String receiveFreqUnitOfMeasure;
    private Integer transmitterSiteElevation;
    private Long communicationDistance;
    private String distanceDescription;

    private Builder() {
    }

    public Builder baseInfo(ArincBaseInfo baseInfo) {
      this.baseInfo = baseInfo;
      return this;
    }

    public Builder recordInfo(ArincRecordInfo recordInfo) {
      this.recordInfo = recordInfo;
      return this;
    }

    public Builder remoteFacilityRef(String remoteFacilityRef) {
      this.remoteFacilityRef = remoteFacilityRef;
      return this;
    }

    public Builder location(LatLong location) {
      this.location = location;
      return this;
    }

    public Builder callSign(String callSign) {
      this.callSign = callSign;
      return this;
    }

    public Builder communicationClass(String communicationClass) {
      this.communicationClass = communicationClass;
      return this;
    }

    public Builder communicationType(String communicationType) {
      this.communicationType = communicationType;
      return this;
    }

    public Builder frequencyUnits(String frequencyUnits) {
      this.frequencyUnits = frequencyUnits;
      return this;
    }

    public Builder radarService(String radarService) {
      this.radarService = radarService;
      return this;
    }

    public Builder h24Indicator(String h24Indicator) {
      this.h24Indicator = h24Indicator;
      return this;
    }

    public Builder modulation(String modulation) {
      this.modulation = modulation;
      return this;
    }

    public Builder sequenceNumber(int sequenceNumber) {
      this.sequenceNumber = sequenceNumber;
      return this;
    }

    public Builder signalEmission(String signalEmission) {
      this.signalEmission = signalEmission;
      return this;
    }

    public Builder transmitFrequencyValue(Double transmitFrequencyValue) {
      this.transmitFrequencyValue = transmitFrequencyValue;
      return this;
    }

    public Builder transmitFreqUnitOfMeasure(String transmitFreqUnitOfMeasure) {
      this.transmitFreqUnitOfMeasure = transmitFreqUnitOfMeasure;
      return this;
    }

    public Builder receiveFrequencyValue(Double receiveFrequencyValue) {
      this.receiveFrequencyValue = receiveFrequencyValue;
      return this;
    }

    public Builder receiveFreqUnitOfMeasure(String receiveFreqUnitOfMeasure) {
      this.receiveFreqUnitOfMeasure = receiveFreqUnitOfMeasure;
      return this;
    }

    public Builder transmitterSiteElevation(Integer transmitterSiteElevation) {
      this.transmitterSiteElevation = transmitterSiteElevation;
      return this;
    }

    public Builder communicationDistance(Long communicationDistance) {
      this.communicationDistance = communicationDistance;
      return this;
    }

    public Builder distanceDescription(String distanceDescription) {
      this.distanceDescription = distanceDescription;
      return this;
    }

    public ArincAirportCommunications build() {
      return new ArincAirportCommunications(this);
    }
  }
}
