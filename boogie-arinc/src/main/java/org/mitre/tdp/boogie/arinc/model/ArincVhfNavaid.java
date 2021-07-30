package org.mitre.tdp.boogie.arinc.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

/**
 * Data class for representing structured/parsed content from within an ARINC format VHF navaid record.
 * <br>
 * Section/Subsection = D
 */
public final class ArincVhfNavaid {

  private final RecordType recordType;
  private final CustomerAreaCode customerAreaCode;
  private final SectionCode sectionCode;
  private final String subSectionCode;
  private final String airportIdentifier;
  private final String airportIcaoRegion;
  private final String vhfIdentifier;
  private final String vhfIcaoRegion;
  private final String continuationRecordNumber;
  private final Double vhfFrequency;
  private final String navaidClass;
  /**
   * For consistency of use - if these are not populated in the raw record but the {@link #dmeLatitude} is then this field will
   * reflect that value (and the {@link #dmeLatitude} value will be identical).
   */
  private final Double latitude;
  /**
   * For consistency of use - if these are not populated in the raw record but the {@link #dmeLongitude} is then this field will
   * reflect that value (and the {@link #dmeLongitude} value will be identical).
   */
  private final Double longitude;
  private final String dmeIdentifier;
  private final Double dmeLatitude;
  private final Double dmeLongitude;
  private final Double stationDeclination;
  private final Double dmeElevation;
  private final Integer figureOfMerit;
  private final Double ilsDmeBias;
  private final Double frequencyProtectionDistance;
  private final String datumCode;
  private final String vhfNavaidName;
  private final Integer fileRecordNumber;
  private final String lastUpdateCycle;

  private ArincVhfNavaid(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.subSectionCode = builder.subSectionCode;
    this.airportIdentifier = builder.airportIdentifier;
    this.airportIcaoRegion = builder.airportIcaoRegion;
    this.vhfIdentifier = builder.vhfIdentifier;
    this.vhfIcaoRegion = builder.vhfIcaoRegion;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.vhfFrequency = builder.vhfFrequency;
    this.navaidClass = builder.navaidClass;
    this.latitude = builder.latitude;
    this.longitude = builder.longitude;
    this.dmeIdentifier = builder.dmeIdentifier;
    this.dmeLatitude = builder.dmeLatitude;
    this.dmeLongitude = builder.dmeLongitude;
    this.stationDeclination = builder.stationDeclination;
    this.dmeElevation = builder.dmeElevation;
    this.figureOfMerit = builder.figureOfMerit;
    this.ilsDmeBias = builder.ilsDmeBias;
    this.frequencyProtectionDistance = builder.frequencyProtectionDistance;
    this.datumCode = builder.datumCode;
    this.vhfNavaidName = builder.vhfNavaidName;
    this.fileRecordNumber = builder.fileRecordNumber;
    this.lastUpdateCycle = builder.lastUpdateCycle;
  }

  public RecordType recordType() {
    return recordType;
  }

  public Optional<CustomerAreaCode> customerAreaCode() {
    return Optional.ofNullable(customerAreaCode);
  }

  public SectionCode sectionCode() {
    return sectionCode;
  }

  public Optional<String> subSectionCode() {
    return Optional.ofNullable(subSectionCode);
  }

  public Optional<String> airportIdentifier() {
    return Optional.ofNullable(airportIdentifier);
  }

  public Optional<String> airportIcaoRegion() {
    return Optional.ofNullable(airportIcaoRegion);
  }

  public String vhfIdentifier() {
    return vhfIdentifier;
  }

  public String vhfIcaoRegion() {
    return vhfIcaoRegion;
  }

  public Optional<String> continuationRecordNumber() {
    return Optional.ofNullable(continuationRecordNumber);
  }

  public Optional<Double> vhfFrequency() {
    return Optional.ofNullable(vhfFrequency);
  }

  public Optional<String> navaidClass() {
    return Optional.ofNullable(navaidClass);
  }

  public Double latitude() {
    return latitude;
  }

  public Double longitude() {
    return longitude;
  }

  public Optional<String> dmeIdentifier() {
    return Optional.ofNullable(dmeIdentifier);
  }

  public Optional<Double> dmeLatitude() {
    return Optional.ofNullable(dmeLatitude);
  }

  public Optional<Double> dmeLongitude() {
    return Optional.ofNullable(dmeLongitude);
  }

  public Optional<Double> stationDeclination() {
    return Optional.ofNullable(stationDeclination);
  }

  public Optional<Double> dmeElevation() {
    return Optional.ofNullable(dmeElevation);
  }

  public Optional<Integer> figureOfMerit() {
    return Optional.ofNullable(figureOfMerit);
  }

  public Optional<Double> ilsDmeBias() {
    return Optional.ofNullable(ilsDmeBias);
  }

  public Optional<Double> frequencyProtectionDistance() {
    return Optional.ofNullable(frequencyProtectionDistance);
  }

  public Optional<String> datumCode() {
    return Optional.ofNullable(datumCode);
  }

  public Optional<String> vhfNavaidName() {
    return Optional.ofNullable(vhfNavaidName);
  }

  public Integer fileRecordNumber() {
    return fileRecordNumber;
  }

  public String lastUpdateCycle() {
    return lastUpdateCycle;
  }

  public Builder toBuilder() {
    return new Builder()
        .recordType(recordType())
        .customerAreaCode(customerAreaCode().orElse(null))
        .sectionCode(sectionCode())
        .subSectionCode(subSectionCode().orElse(null))
        .airportIdentifier(airportIdentifier().orElse(null))
        .airportIcaoRegion(airportIcaoRegion().orElse(null))
        .vhfIdentifier(vhfIdentifier())
        .vhfIcaoRegion(vhfIcaoRegion())
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .vhfFrequency(vhfFrequency().orElse(null))
        .navaidClass(navaidClass().orElse(null))
        .latitude(latitude())
        .longitude(longitude())
        .dmeIdentifier(dmeIdentifier().orElse(null))
        .dmeLatitude(dmeLatitude().orElse(null))
        .dmeLongitude(dmeLongitude().orElse(null))
        .stationDeclination(stationDeclination().orElse(null))
        .dmeElevation(dmeElevation().orElse(null))
        .figureOfMerit(figureOfMerit().orElse(null))
        .ilsDmeBias(ilsDmeBias().orElse(null))
        .frequencyProtectionDistance(frequencyProtectionDistance().orElse(null))
        .datumCode(datumCode().orElse(null))
        .vhfNavaidName(vhfNavaidName().orElse(null))
        .fileRecordNumber(fileRecordNumber())
        .lastUpdateCycle(lastUpdateCycle());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincVhfNavaid that = (ArincVhfNavaid) o;
    return recordType == that.recordType &&
        customerAreaCode == that.customerAreaCode &&
        sectionCode == that.sectionCode &&
        Objects.equals(subSectionCode, that.subSectionCode) &&
        Objects.equals(airportIdentifier, that.airportIdentifier) &&
        Objects.equals(airportIcaoRegion, that.airportIcaoRegion) &&
        Objects.equals(vhfIdentifier, that.vhfIdentifier) &&
        Objects.equals(vhfIcaoRegion, that.vhfIcaoRegion) &&
        Objects.equals(continuationRecordNumber, that.continuationRecordNumber) &&
        Objects.equals(vhfFrequency, that.vhfFrequency) &&
        Objects.equals(navaidClass, that.navaidClass) &&
        Objects.equals(latitude, that.latitude) &&
        Objects.equals(longitude, that.longitude) &&
        Objects.equals(dmeIdentifier, that.dmeIdentifier) &&
        Objects.equals(dmeLatitude, that.dmeLatitude) &&
        Objects.equals(dmeLongitude, that.dmeLongitude) &&
        Objects.equals(stationDeclination, that.stationDeclination) &&
        Objects.equals(dmeElevation, that.dmeElevation) &&
        Objects.equals(figureOfMerit, that.figureOfMerit) &&
        Objects.equals(ilsDmeBias, that.ilsDmeBias) &&
        Objects.equals(frequencyProtectionDistance, that.frequencyProtectionDistance) &&
        Objects.equals(datumCode, that.datumCode) &&
        Objects.equals(vhfNavaidName, that.vhfNavaidName) &&
        Objects.equals(fileRecordNumber, that.fileRecordNumber) &&
        Objects.equals(lastUpdateCycle, that.lastUpdateCycle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, subSectionCode, airportIdentifier, airportIcaoRegion, vhfIdentifier, vhfIcaoRegion, continuationRecordNumber, vhfFrequency, navaidClass, latitude, longitude, dmeIdentifier, dmeLatitude, dmeLongitude, stationDeclination, dmeElevation, figureOfMerit, ilsDmeBias, frequencyProtectionDistance, datumCode, vhfNavaidName, fileRecordNumber, lastUpdateCycle);
  }

  @Override
  public String toString() {
    return "ArincVhfNavaid2{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode=" + sectionCode +
        ", subSectionCode='" + subSectionCode + '\'' +
        ", airportIdentifier='" + airportIdentifier + '\'' +
        ", airportIcaoRegion='" + airportIcaoRegion + '\'' +
        ", vhfIdentifier='" + vhfIdentifier + '\'' +
        ", vhfIcaoRegion='" + vhfIcaoRegion + '\'' +
        ", continuationRecordNumber='" + continuationRecordNumber + '\'' +
        ", vhfFrequency=" + vhfFrequency +
        ", navaidClass='" + navaidClass + '\'' +
        ", latitude=" + latitude +
        ", longitude=" + longitude +
        ", dmeIdentifier='" + dmeIdentifier + '\'' +
        ", dmeLatitude=" + dmeLatitude +
        ", dmeLongitude=" + dmeLongitude +
        ", stationDeclination=" + stationDeclination +
        ", dmeElevation=" + dmeElevation +
        ", figureOfMerit=" + figureOfMerit +
        ", ilsDmeBias=" + ilsDmeBias +
        ", frequencyProtectionDistance=" + frequencyProtectionDistance +
        ", datumCode='" + datumCode + '\'' +
        ", vhfNavaidName='" + vhfNavaidName + '\'' +
        ", fileRecordNumber=" + fileRecordNumber +
        ", lastUpdateCycle='" + lastUpdateCycle + '\'' +
        '}';
  }

  public static final class Builder {
    private RecordType recordType;
    private CustomerAreaCode customerAreaCode;
    private SectionCode sectionCode;
    private String subSectionCode;
    private String airportIdentifier;
    private String airportIcaoRegion;
    private String vhfIdentifier;
    private String vhfIcaoRegion;
    private String continuationRecordNumber;
    private Double vhfFrequency;
    private String navaidClass;
    private Double latitude;
    private Double longitude;
    private String dmeIdentifier;
    private Double dmeLatitude;
    private Double dmeLongitude;
    private Double stationDeclination;
    private Double dmeElevation;
    private Integer figureOfMerit;
    private Double ilsDmeBias;
    private Double frequencyProtectionDistance;
    private String datumCode;
    private String vhfNavaidName;
    private Integer fileRecordNumber;
    private String lastUpdateCycle;

    public Builder recordType(RecordType recordType) {
      this.recordType = recordType;
      return this;
    }

    public Builder customerAreaCode(CustomerAreaCode customerAreaCode) {
      this.customerAreaCode = customerAreaCode;
      return this;
    }

    public Builder sectionCode(SectionCode sectionCode) {
      this.sectionCode = sectionCode;
      return this;
    }

    public Builder subSectionCode(String subSectionCode) {
      this.subSectionCode = subSectionCode;
      return this;
    }

    public Builder airportIdentifier(String airportIdentifier) {
      this.airportIdentifier = airportIdentifier;
      return this;
    }

    public Builder airportIcaoRegion(String airportIcaoRegion) {
      this.airportIcaoRegion = airportIcaoRegion;
      return this;
    }

    public Builder vhfIdentifier(String vhfIdentifier) {
      this.vhfIdentifier = vhfIdentifier;
      return this;
    }

    public Builder vhfIcaoRegion(String vhfIcaoRegion) {
      this.vhfIcaoRegion = vhfIcaoRegion;
      return this;
    }

    public Builder continuationRecordNumber(String continuationRecordNumber) {
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder vhfFrequency(Double vhfFrequency) {
      this.vhfFrequency = vhfFrequency;
      return this;
    }

    public Builder navaidClass(String navaidClass) {
      this.navaidClass = navaidClass;
      return this;
    }

    public Builder latitude(Double latitude) {
      this.latitude = latitude;
      return this;
    }

    public Builder longitude(Double longitude) {
      this.longitude = longitude;
      return this;
    }

    public Builder dmeIdentifier(String dmeIdentifier) {
      this.dmeIdentifier = dmeIdentifier;
      return this;
    }

    public Builder dmeLatitude(Double dmeLatitude) {
      this.dmeLatitude = dmeLatitude;
      return this;
    }

    public Builder dmeLongitude(Double dmeLongitude) {
      this.dmeLongitude = dmeLongitude;
      return this;
    }

    public Builder stationDeclination(Double stationDeclination) {
      this.stationDeclination = stationDeclination;
      return this;
    }

    public Builder dmeElevation(Double dmeElevation) {
      this.dmeElevation = dmeElevation;
      return this;
    }

    public Builder figureOfMerit(Integer figureOfMerit) {
      this.figureOfMerit = figureOfMerit;
      return this;
    }

    public Builder ilsDmeBias(Double ilsDmeBias) {
      this.ilsDmeBias = ilsDmeBias;
      return this;
    }

    public Builder frequencyProtectionDistance(Double frequencyProtectionDistance) {
      this.frequencyProtectionDistance = frequencyProtectionDistance;
      return this;
    }

    public Builder datumCode(String datumCode) {
      this.datumCode = datumCode;
      return this;
    }

    public Builder vhfNavaidName(String vhfNavaidName) {
      this.vhfNavaidName = vhfNavaidName;
      return this;
    }

    public Builder fileRecordNumber(Integer fileRecordNumber) {
      this.fileRecordNumber = fileRecordNumber;
      return this;
    }

    public Builder lastUpdateCycle(String lastUpdateCycle) {
      this.lastUpdateCycle = lastUpdateCycle;
      return this;
    }

    public ArincVhfNavaid build() {
      return new ArincVhfNavaid(this);
    }
  }
}
