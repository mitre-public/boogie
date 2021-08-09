package org.mitre.tdp.boogie.arinc.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

/**
 * Data class for representing structured/parsed content from within an ARINC format NDB navaid record.
 * <br>
 * Section/Subsection = DB or PN
 */
public final class ArincNdbNavaid implements ArincModel {

  private final RecordType recordType;
  private final CustomerAreaCode customerAreaCode;
  private final SectionCode sectionCode;
  private final String subSectionCode;
  private final String airportIdentifier;
  private final String airportIcaoRegion;
  private final String ndbIdentifier;
  private final String ndbIcaoRegion;
  private final String continuationRecordNumber;
  private final Double ndbFrequency;
  private final String navaidClass;
  private final Double latitude;
  private final Double longitude;
  private final Double magneticVariation;
  private final String datumCode;
  private final String ndbNavaidName;
  private final Integer fileRecordNumber;
  private final String lastUpdateCycle;

  private ArincNdbNavaid(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.subSectionCode = builder.subSectionCode;
    this.airportIdentifier = builder.airportIdentifier;
    this.airportIcaoRegion = builder.airportIcaoRegion;
    this.ndbIdentifier = builder.ndbIdentifier;
    this.ndbIcaoRegion = builder.ndbIcaoRegion;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.ndbFrequency = builder.ndbFrequency;
    this.navaidClass = builder.navaidClass;
    this.latitude = builder.latitude;
    this.longitude = builder.longitude;
    this.magneticVariation = builder.magneticVariation;
    this.datumCode = builder.datumCode;
    this.ndbNavaidName = builder.ndbNavaidName;
    this.fileRecordNumber = builder.fileRecordNumber;
    this.lastUpdateCycle = builder.lastUpdateCycle;
  }

  public RecordType recordType() {
    return recordType;
  }

  public Optional<CustomerAreaCode> customerAreaCode() {
    return Optional.ofNullable(customerAreaCode);
  }

  @Override
  public SectionCode sectionCode() {
    return sectionCode;
  }

  @Override
  public Optional<String> subSectionCode() {
    return Optional.of(subSectionCode);
  }

  public Optional<String> airportIdentifier() {
    return Optional.ofNullable(airportIdentifier);
  }

  public Optional<String> airportIcaoRegion() {
    return Optional.ofNullable(airportIcaoRegion);
  }

  public String ndbIdentifier() {
    return ndbIdentifier;
  }

  public String ndbIcaoRegion() {
    return ndbIcaoRegion;
  }

  public Optional<String> continuationRecordNumber() {
    return Optional.ofNullable(continuationRecordNumber);
  }

  public Optional<Double> ndbFrequency() {
    return Optional.ofNullable(ndbFrequency);
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

  public Optional<Double> magneticVariation() {
    return Optional.ofNullable(magneticVariation);
  }

  public Optional<String> datumCode() {
    return Optional.ofNullable(datumCode);
  }

  public Optional<String> ndbNavaidName() {
    return Optional.ofNullable(ndbNavaidName);
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
        .subSectionCode(subSectionCode().orElseThrow(IllegalStateException::new))
        .airportIdentifier(airportIdentifier().orElse(null))
        .airportIcaoRegion(airportIcaoRegion().orElse(null))
        .ndbIdentifier(ndbIdentifier())
        .ndbIcaoRegion(ndbIcaoRegion())
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .ndbFrequency(ndbFrequency().orElse(null))
        .navaidClass(navaidClass().orElse(null))
        .latitude(latitude())
        .longitude(longitude())
        .magneticVariation(magneticVariation().orElse(null))
        .datumCode(datumCode().orElse(null))
        .ndbNavaidName(ndbNavaidName().orElse(null))
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
    ArincNdbNavaid that = (ArincNdbNavaid) o;
    return recordType == that.recordType &&
        customerAreaCode == that.customerAreaCode &&
        sectionCode == that.sectionCode &&
        Objects.equals(subSectionCode, that.subSectionCode) &&
        Objects.equals(airportIdentifier, that.airportIdentifier) &&
        Objects.equals(airportIcaoRegion, that.airportIcaoRegion) &&
        Objects.equals(ndbIdentifier, that.ndbIdentifier) &&
        Objects.equals(ndbIcaoRegion, that.ndbIcaoRegion) &&
        Objects.equals(continuationRecordNumber, that.continuationRecordNumber) &&
        Objects.equals(ndbFrequency, that.ndbFrequency) &&
        Objects.equals(navaidClass, that.navaidClass) &&
        Objects.equals(latitude, that.latitude) &&
        Objects.equals(longitude, that.longitude) &&
        Objects.equals(magneticVariation, that.magneticVariation) &&
        Objects.equals(datumCode, that.datumCode) &&
        Objects.equals(ndbNavaidName, that.ndbNavaidName) &&
        Objects.equals(fileRecordNumber, that.fileRecordNumber) &&
        Objects.equals(lastUpdateCycle, that.lastUpdateCycle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, subSectionCode, airportIdentifier, airportIcaoRegion, ndbIdentifier, ndbIcaoRegion, continuationRecordNumber, ndbFrequency, navaidClass, latitude, longitude, magneticVariation, datumCode, ndbNavaidName, fileRecordNumber, lastUpdateCycle);
  }

  @Override
  public String toString() {
    return "ArincNdbNavaid2{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode=" + sectionCode +
        ", subSectionCode='" + subSectionCode + '\'' +
        ", airportIdentifier='" + airportIdentifier + '\'' +
        ", airportIcaoRegion='" + airportIcaoRegion + '\'' +
        ", ndbIdentifier='" + ndbIdentifier + '\'' +
        ", ndbIcaoRegion='" + ndbIcaoRegion + '\'' +
        ", continuationRecordNumber='" + continuationRecordNumber + '\'' +
        ", ndbFrequency=" + ndbFrequency +
        ", navaidClass='" + navaidClass + '\'' +
        ", latitude=" + latitude +
        ", longitude=" + longitude +
        ", magneticVariation=" + magneticVariation +
        ", datumCode='" + datumCode + '\'' +
        ", ndbNavaidName='" + ndbNavaidName + '\'' +
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
    private String ndbIdentifier;
    private String ndbIcaoRegion;
    private String continuationRecordNumber;
    private Double ndbFrequency;
    private String navaidClass;
    private Double latitude;
    private Double longitude;
    private Double magneticVariation;
    private String datumCode;
    private String ndbNavaidName;
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

    public Builder ndbIdentifier(String ndbIdentifier) {
      this.ndbIdentifier = ndbIdentifier;
      return this;
    }

    public Builder ndbIcaoRegion(String ndbIcaoRegion) {
      this.ndbIcaoRegion = ndbIcaoRegion;
      return this;
    }

    public Builder continuationRecordNumber(String continuationRecordNumber) {
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder ndbFrequency(Double ndbFrequency) {
      this.ndbFrequency = ndbFrequency;
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

    public Builder magneticVariation(Double magneticVariation) {
      this.magneticVariation = magneticVariation;
      return this;
    }

    public Builder datumCode(String datumCode) {
      this.datumCode = datumCode;
      return this;
    }

    public Builder ndbNavaidName(String ndbNavaidName) {
      this.ndbNavaidName = ndbNavaidName;
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

    public ArincNdbNavaid build() {
      return new ArincNdbNavaid(this);
    }
  }
}
