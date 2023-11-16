package org.mitre.tdp.boogie.arinc.model;

import static java.util.Objects.requireNonNull;

import java.lang.String;
import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.*;

public final class ArincAirportPrimaryExtension implements ArincModel {
  /**
   * See {@link RecordType}.
   */
  private final RecordType recordType;
  /**
   * See {@link CustomerAreaCode}.
   */
  private final CustomerAreaCode customerAreaCode;
  /**
   * See {@link SectionCode}.
   */
  private final SectionCode sectionCode;
  /**
   * See {@link AirportHeliportIdentifier}.
   */
  private final String airportIdentifier;
  /**
   * See {@link IcaoRegion}.
   */
  private final String airportIcaoRegion;
  /**
   * See {@link SubSectionCode}.
   */
  private final String subSectionCode;
  /**
   * The “ATA/IATA” field contains the Airport/Heliport ATA/IATA designator code to which the data contained in the record
   * relates.
   */
  private final String iataDesignator;
  /**
   * See {@link ContinuationRecordNumber}.
   */
  private final String continuationRecordNumber;

  /**
   * See {@link Notes}.
   */
  private final String notes;

  /**
   * See {@link ApplicationType}
   */
  private final ApplicationType applicationType;

  private final int fileRecordNumber;
  /**
   * See {@link Cycle}.
   */
  private final String lastUpdateCycle;

  private ArincAirportPrimaryExtension(Builder builder) {
    this.recordType = requireNonNull(builder.recordType);
    this.customerAreaCode = requireNonNull(builder.customerAreaCode);
    this.sectionCode = requireNonNull(builder.sectionCode);
    this.airportIdentifier = requireNonNull(builder.airportIdentifier);
    this.airportIcaoRegion = requireNonNull(builder.airportIcaoRegion);
    this.subSectionCode = requireNonNull(builder.subSectionCode);
    this.iataDesignator = builder.iataDesignator;
    this.continuationRecordNumber = requireNonNull(builder.continuationRecordNumber);
    this.notes = builder.notes;
    this.applicationType = requireNonNull(builder.applicationType);
    this.fileRecordNumber = builder.fileRecordNumber;
    this.lastUpdateCycle = requireNonNull(builder.lastUpdatedCycle);
  }

  public static Builder builder() {
    return new ArincAirportPrimaryExtension.Builder();
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

  public String airportIdentifier() {
    return airportIdentifier;
  }

  public String airportIcaoRegion() {
    return airportIcaoRegion;
  }

  public Optional<String> subSectionCode() {
    return Optional.ofNullable(subSectionCode);
  }

  public Optional<String> iataDesignator() {
    return Optional.ofNullable(iataDesignator);
  }

  public Optional<String> continuationRecordNumber() {
    return Optional.ofNullable(continuationRecordNumber);
  }

  public String notes() {
    return notes;
  }

  @Override
  public Optional<ApplicationType> applicationType() {
    return Optional.ofNullable(applicationType);
  }

  public int fileRecordNumber() {
    return fileRecordNumber;
  }

  public String lastUpdateCycle() {
    return lastUpdateCycle;
  }

  public Builder toBuilder() {
    return builder()
        .recordType(recordType())
        .customerAreaCode(customerAreaCode().orElse(null))
        .sectionCode(sectionCode())
        .airportIdentifier(airportIdentifier())
        .airportIcaoRegion(airportIcaoRegion())
        .subSectionCode(subSectionCode().orElse(null))
        .iataDesignator(iataDesignator().orElse(null))
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .applicationType(applicationType().orElse(null))
        .fileRecordNumber(fileRecordNumber())
        .lastUpdatedCycle(lastUpdateCycle())
        .notes(notes());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArincAirportPrimaryExtension that = (ArincAirportPrimaryExtension) o;
    return fileRecordNumber == that.fileRecordNumber && recordType == that.recordType && customerAreaCode == that.customerAreaCode && sectionCode == that.sectionCode && Objects.equals(airportIdentifier, that.airportIdentifier) && Objects.equals(airportIcaoRegion, that.airportIcaoRegion) && Objects.equals(subSectionCode, that.subSectionCode) && Objects.equals(iataDesignator, that.iataDesignator) && Objects.equals(continuationRecordNumber, that.continuationRecordNumber) && Objects.equals(notes, that.notes) && Objects.equals(applicationType, that.applicationType) && Objects.equals(lastUpdateCycle, that.lastUpdateCycle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, airportIdentifier, airportIcaoRegion, subSectionCode, iataDesignator, continuationRecordNumber, notes, applicationType, fileRecordNumber, lastUpdateCycle);
  }

  @Override
  public String toString() {
    return "LidoArincAirportPrimaryExtension{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode=" + sectionCode +
        ", airportIdentifier='" + airportIdentifier + '\'' +
        ", airportIcaoRegion='" + airportIcaoRegion + '\'' +
        ", subSectionCode='" + subSectionCode + '\'' +
        ", iataDesignator='" + iataDesignator + '\'' +
        ", continuationRecordNumber='" + continuationRecordNumber + '\'' +
        ", notes='" + notes + '\'' +
        ", applicationType='" + applicationType + '\'' +
        ", fileRecordNumber=" + fileRecordNumber +
        ", lastUpdateCycle='" + lastUpdateCycle + '\'' +
        '}';
  }

  public static final class Builder {
    private RecordType recordType;

    private CustomerAreaCode customerAreaCode;

    private SectionCode sectionCode;

    private String airportIdentifier;

    private String airportIcaoRegion;

    private String subSectionCode;

    private String iataDesignator;

    private String continuationRecordNumber;

    private String notes;

    private ApplicationType applicationType;

    private int fileRecordNumber;

    private String lastUpdatedCycle;

    private Builder() {
    }

    public ArincAirportPrimaryExtension build() {
      return new ArincAirportPrimaryExtension(this);
    }

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

    public Builder airportIdentifier(String airportIdentifier) {
      this.airportIdentifier = airportIdentifier;
      return this;
    }

    public Builder airportIcaoRegion(String airportIcaoRegion) {
      this.airportIcaoRegion = airportIcaoRegion;
      return this;
    }

    public Builder subSectionCode(String subSectionCode) {
      this.subSectionCode = subSectionCode;
      return this;
    }

    public Builder iataDesignator(String iataDesignator) {
      this.iataDesignator = iataDesignator;
      return this;
    }

    public Builder continuationRecordNumber(String continuationRecordNumber) {
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder notes(String notes) {
      this.notes = notes;
      return this;
    }

    public Builder applicationType(ApplicationType applicationType) {
      this.applicationType = applicationType;
      return this;
    }

    public Builder fileRecordNumber(int fileRecordNumber) {
      this.fileRecordNumber = fileRecordNumber;
      return this;
    }

    public Builder lastUpdatedCycle(String lastUpdatedCycle) {
      this.lastUpdatedCycle = lastUpdatedCycle;
      return this;
    }
  }
}