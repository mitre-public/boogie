package org.mitre.tdp.boogie.arinc.model;

import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import java.util.Objects;
import java.util.Optional;

public final class ArincAirportGate implements ArincModel {
  private final RecordType recordType;
  private final CustomerAreaCode customerAreaCode;
  private final SectionCode sectionCode;
  private final String airportIdentifier;
  private final String airportIcaoRegion;
  private final String subSectionCode;
  private final String gateIdentifier;

  private final String continuationRecordNumber;
  private final Double latitude;
  private final Double longitude;
  private final String name;
  private final Integer fileRecordNumber;
  private final String lastUpdatedCycle;

  public ArincAirportGate() {
    this.recordType = null;
    this.customerAreaCode = null;
    this.sectionCode = null;
    this.airportIdentifier = null;
    this.airportIcaoRegion = null;
    this.subSectionCode = null;
    this.gateIdentifier = null;
    this.continuationRecordNumber = null;
    this.latitude = null;
    this.longitude = null;
    this.name = null;
    this.fileRecordNumber = null;
    this.lastUpdatedCycle = null;
  }

  public ArincAirportGate(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.airportIdentifier = builder.airportIdentifier;
    this.airportIcaoRegion = builder.airportIcaoRegion;
    this.subSectionCode = builder.subSectionCode;
    this.gateIdentifier = builder.gateIdentifier;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.latitude = builder.latitude;
    this.longitude = builder.longitude;
    this.name = builder.name;
    this.fileRecordNumber = builder.fileRecordNumber;
    this.lastUpdatedCycle = builder.lastUpdatedCycle;
  }

  public RecordType getRecordType() {
    return recordType;
  }

  public CustomerAreaCode getCustomerAreaCode() {
    return customerAreaCode;
  }

  public String getAirportIdentifier() {
    return airportIdentifier;
  }

  public String getAirportIcaoRegion() {
    return airportIcaoRegion;
  }

  public String getGateIdentifier() {
    return gateIdentifier;
  }

  public Double getLatitude() {
    return latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public String getName() {
    return name;
  }

  public Integer getFileRecordNumber() {
    return fileRecordNumber;
  }

  public String getLastUpdatedCycle() {
    return lastUpdatedCycle;
  }

  @Override
  public SectionCode sectionCode() {
    return sectionCode;
  }

  @Override
  public Optional<String> subSectionCode() {
    return Optional.of(subSectionCode);
  }

  @Override
  public Optional<String> continuationRecordNumber() {
    return Optional.of(continuationRecordNumber);
  }

  public boolean equals (Object object){
    if (this == object)
      return true;
    if (object == null || getClass() != object.getClass())
      return false;
    if (!super.equals(object))
      return false;
    ArincAirportGate that = (ArincAirportGate) object;
    return Objects.equals(recordType, that.recordType) && Objects.equals(customerAreaCode, that.customerAreaCode) && Objects.equals(sectionCode, that.sectionCode) && Objects.equals(airportIdentifier, that.airportIdentifier) && Objects.equals(airportIcaoRegion, that.airportIcaoRegion) && Objects.equals(subSectionCode, that.subSectionCode) && Objects.equals(gateIdentifier, that.gateIdentifier) && Objects.equals(continuationRecordNumber, that.continuationRecordNumber) && Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude) && Objects.equals(name, that.name) && Objects.equals(fileRecordNumber, that.fileRecordNumber) && Objects.equals(lastUpdatedCycle, that.lastUpdatedCycle);
  }

  public int hashCode () {
    return Objects.hash(super.hashCode(), recordType, customerAreaCode, sectionCode, airportIdentifier, airportIcaoRegion, subSectionCode, gateIdentifier, continuationRecordNumber, latitude, longitude, name, fileRecordNumber, lastUpdatedCycle);
  }

  public Builder toBuilder () {
    return new Builder()
        .recordType(getRecordType())
        .customerAreaCode(getCustomerAreaCode())
        .sectionCode(sectionCode())
        .airportIdentifier(getAirportIdentifier())
        .airportIcaoRegion(getAirportIcaoRegion())
        .subSectionCode(subSectionCode().orElse(null))
        .gateIdentifier(getGateIdentifier())
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .latitude(getLatitude())
        .longitude(getLongitude())
        .name(getName())
        .fileRecordNumber(getFileRecordNumber())
        .lastUpdatedCycle(getLastUpdatedCycle());
  }

  public static final class Builder {
    private RecordType recordType;
    private CustomerAreaCode customerAreaCode;
    private SectionCode sectionCode;
    private String airportIdentifier;
    private String airportIcaoRegion;
    private String subSectionCode;
    private String gateIdentifier;
    private String continuationRecordNumber;
    private Double latitude;
    private Double longitude;
    private String name;
    private Integer fileRecordNumber;
    private String lastUpdatedCycle;

    public Builder recordType (RecordType recordType){
      this.recordType = recordType;
      return this;
    }

    public Builder customerAreaCode (CustomerAreaCode customerAreaCode ){
      this.customerAreaCode = customerAreaCode;
      return this;
    }

    public Builder sectionCode (SectionCode sectionCode){
      this.sectionCode = sectionCode;
      return this;
    }

    public Builder airportIdentifier (String airportIdentifier){
      this.airportIdentifier = airportIdentifier;
      return this;
    }

    public Builder airportIcaoRegion (String airportIcaoRegion){
      this.airportIcaoRegion = airportIcaoRegion;
      return this;
    }
    public Builder subSectionCode (String subSectionCode){
      this.subSectionCode = subSectionCode;
      return this;
    }

    public Builder gateIdentifier (String gateIdentifier){
      this.gateIdentifier = gateIdentifier;
      return this;
    }

    public Builder continuationRecordNumber (String continuationRecordNumber){
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder latitude (Double latitude){
      this.latitude = latitude;
      return this;
    }

    public Builder longitude (Double longitude){
      this.longitude = longitude;
      return this;
    }

    public Builder name (String name){
      this.name = name;
      return this;
    }

    public Builder fileRecordNumber (Integer fileRecordNumber){
      this.fileRecordNumber = fileRecordNumber;
      return this;
    }

    public Builder lastUpdatedCycle (String lastUpdatedCycle){
      this.lastUpdatedCycle = lastUpdatedCycle;
      return this;
    }

    public ArincAirportGate build () {
      return new ArincAirportGate(this);
    }
  }
}
