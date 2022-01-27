package org.mitre.tdp.boogie.arinc.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

/**
 * Data class for representing structured/parsed content from within an ARINC format waypoint record.
 * <br>
 * Section/Subsection = EA/PC
 */
public final class ArincWaypoint implements ArincModel {

  private final RecordType recordType;
  private final CustomerAreaCode customerAreaCode;
  private final SectionCode sectionCode;
  private final String enrouteSubSectionCode;
  private final String airportIdentifier;
  private final String airportIcaoRegion;
  private final String terminalSubSectionCode;
  private final String waypointIdentifier;
  private final String waypointIcaoRegion;
  private final String continuationRecordNumber;
  private final String waypointType;
  private final String waypointUsage;
  private final double latitude;
  private final double longitude;
  private final Double magneticVariation;
  private final String datumCode;
  private final String nameFormat;
  private final String waypointNameDescription;
  private final int fileRecordNumber;
  private final String lastUpdateCycle;

  private ArincWaypoint(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.enrouteSubSectionCode = builder.enrouteSubSectionCode;
    this.airportIdentifier = builder.airportIdentifier;
    this.airportIcaoRegion = builder.airportIcaoRegion;
    this.terminalSubSectionCode = builder.terminalSubSectionCode;
    this.waypointIdentifier = builder.waypointIdentifier;
    this.waypointIcaoRegion = builder.waypointIcaoRegion;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.waypointType = builder.waypointType;
    this.waypointUsage = builder.waypointUsage;
    this.latitude = builder.latitude;
    this.longitude = builder.longitude;
    this.magneticVariation = builder.magneticVariation;
    this.datumCode = builder.datumCode;
    this.nameFormat = builder.nameFormat;
    this.waypointNameDescription = builder.waypointNameDescription;
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

  public Optional<String> enrouteSubSectionCode() {
    return Optional.ofNullable(enrouteSubSectionCode);
  }

  public Optional<String> airportIdentifier() {
    return Optional.ofNullable(airportIdentifier);
  }

  public Optional<String> airportIcaoRegion() {
    return Optional.ofNullable(airportIcaoRegion);
  }

  public Optional<String> terminalSubSectionCode() {
    return Optional.ofNullable(terminalSubSectionCode);
  }

  @Override
  public Optional<String> subSectionCode() {
    return Optional.of(enrouteSubSectionCode().orElseGet(() -> terminalSubSectionCode().orElseThrow(IllegalStateException::new)));
  }

  public String waypointIdentifier() {
    return waypointIdentifier;
  }

  public String waypointIcaoRegion() {
    return waypointIcaoRegion;
  }

  public Optional<String> continuationRecordNumber() {
    return Optional.ofNullable(continuationRecordNumber);
  }

  public Optional<String> waypointType() {
    return Optional.ofNullable(waypointType);
  }

  public Optional<String> waypointUsage() {
    return Optional.ofNullable(waypointUsage);
  }

  public double latitude() {
    return latitude;
  }

  public double longitude() {
    return longitude;
  }

  public Optional<Double> magneticVariation() {
    return Optional.ofNullable(magneticVariation);
  }

  public Optional<String> datumCode() {
    return Optional.ofNullable(datumCode);
  }

  public Optional<String> nameFormat() {
    return Optional.ofNullable(nameFormat);
  }

  public Optional<String> waypointNameDescription() {
    return Optional.ofNullable(waypointNameDescription);
  }

  public int fileRecordNumber() {
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
        .enrouteSubSectionCode(enrouteSubSectionCode().orElse(null))
        .airportIdentifier(airportIdentifier().orElse(null))
        .airportIcaoRegion(airportIcaoRegion().orElse(null))
        .terminalSubSectionCode(terminalSubSectionCode().orElse(null))
        .waypointIdentifier(waypointIdentifier())
        .waypointIcaoRegion(waypointIcaoRegion())
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .waypointType(waypointType().orElse(null))
        .waypointUsage(waypointUsage().orElse(null))
        .latitude(latitude())
        .longitude(longitude())
        .magneticVariation(magneticVariation().orElse(null))
        .datumCode(datumCode().orElse(null))
        .nameFormat(nameFormat().orElse(null))
        .waypointNameDescription(waypointNameDescription().orElse(null))
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
    ArincWaypoint that = (ArincWaypoint) o;
    return recordType == that.recordType &&
        customerAreaCode == that.customerAreaCode &&
        sectionCode == that.sectionCode &&
        Objects.equals(enrouteSubSectionCode, that.enrouteSubSectionCode) &&
        Objects.equals(airportIdentifier, that.airportIdentifier) &&
        Objects.equals(airportIcaoRegion, that.airportIcaoRegion) &&
        Objects.equals(terminalSubSectionCode, that.terminalSubSectionCode) &&
        Objects.equals(waypointIdentifier, that.waypointIdentifier) &&
        Objects.equals(waypointIcaoRegion, that.waypointIcaoRegion) &&
        Objects.equals(continuationRecordNumber, that.continuationRecordNumber) &&
        Objects.equals(waypointType, that.waypointType) &&
        Objects.equals(waypointUsage, that.waypointUsage) &&
        Objects.equals(latitude, that.latitude) &&
        Objects.equals(longitude, that.longitude) &&
        Objects.equals(magneticVariation, that.magneticVariation) &&
        Objects.equals(datumCode, that.datumCode) &&
        Objects.equals(nameFormat, that.nameFormat) &&
        Objects.equals(waypointNameDescription, that.waypointNameDescription) &&
        Objects.equals(fileRecordNumber, that.fileRecordNumber) &&
        Objects.equals(lastUpdateCycle, that.lastUpdateCycle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, enrouteSubSectionCode, airportIdentifier, airportIcaoRegion, terminalSubSectionCode, waypointIdentifier, waypointIcaoRegion, continuationRecordNumber, waypointType, waypointUsage, latitude, longitude, magneticVariation, datumCode, nameFormat, waypointNameDescription, fileRecordNumber, lastUpdateCycle);
  }

  @Override
  public String toString() {
    return "ArincWaypoint2{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode=" + sectionCode +
        ", enrouteSubSectionCode='" + enrouteSubSectionCode + '\'' +
        ", airportIdentifier='" + airportIdentifier + '\'' +
        ", airportIcaoRegion='" + airportIcaoRegion + '\'' +
        ", terminalSubSectionCode='" + terminalSubSectionCode + '\'' +
        ", waypointIdentifier='" + waypointIdentifier + '\'' +
        ", waypointIcaoRegion='" + waypointIcaoRegion + '\'' +
        ", continuationRecordNumber='" + continuationRecordNumber + '\'' +
        ", waypointType='" + waypointType + '\'' +
        ", waypointUsage='" + waypointUsage + '\'' +
        ", latitude=" + latitude +
        ", longitude=" + longitude +
        ", magneticVariation=" + magneticVariation +
        ", datumCode='" + datumCode + '\'' +
        ", nameFormat='" + nameFormat + '\'' +
        ", waypointNameDescription='" + waypointNameDescription + '\'' +
        ", fileRecordNumber=" + fileRecordNumber +
        ", lastUpdateCycle='" + lastUpdateCycle + '\'' +
        '}';
  }

  public static final class Builder {
    private RecordType recordType;
    private CustomerAreaCode customerAreaCode;
    private SectionCode sectionCode;
    private String enrouteSubSectionCode;
    private String airportIdentifier;
    private String airportIcaoRegion;
    private String terminalSubSectionCode;
    private String waypointIdentifier;
    private String waypointIcaoRegion;
    private String continuationRecordNumber;
    private String waypointType;
    private String waypointUsage;
    private Double latitude;
    private Double longitude;
    private Double magneticVariation;
    private String datumCode;
    private String nameFormat;
    private String waypointNameDescription;
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

    public Builder enrouteSubSectionCode(String enrouteSubSectionCode) {
      this.enrouteSubSectionCode = enrouteSubSectionCode;
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

    public Builder terminalSubSectionCode(String terminalSubSectionCode) {
      this.terminalSubSectionCode = terminalSubSectionCode;
      return this;
    }

    public Builder waypointIdentifier(String waypointIdentifier) {
      this.waypointIdentifier = waypointIdentifier;
      return this;
    }

    public Builder waypointIcaoRegion(String waypointIcaoRegion) {
      this.waypointIcaoRegion = waypointIcaoRegion;
      return this;
    }

    public Builder continuationRecordNumber(String continuationRecordNumber) {
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder waypointType(String waypointType) {
      this.waypointType = waypointType;
      return this;
    }

    public Builder waypointUsage(String waypointUsage) {
      this.waypointUsage = waypointUsage;
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

    public Builder nameFormat(String nameFormat) {
      this.nameFormat = nameFormat;
      return this;
    }

    public Builder waypointNameDescription(String waypointNameDescription) {
      this.waypointNameDescription = waypointNameDescription;
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

    public ArincWaypoint build() {
      return new ArincWaypoint(this);
    }
  }
}
