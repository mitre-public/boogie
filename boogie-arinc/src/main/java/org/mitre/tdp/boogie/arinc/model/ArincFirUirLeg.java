package org.mitre.tdp.boogie.arinc.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.ArcBearing;
import org.mitre.tdp.boogie.arinc.v18.field.ArcDistance;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CruiseTableIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirAddress;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirEntryReport;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirName;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirReportingUnitsAltitude;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirReportingUnitsSpeed;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.Limit;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SequenceNumber;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;

/**
 * Section/Subsection = UF
 */
public final class ArincFirUirLeg implements ArincModel {
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
   * See {@link SubSectionCode}.
   */
  private final String subSectionCode;
  /**
   * See {@link FirUirIdentifier}.
   */
  private final String firUirIdentifier;
  /**
   * See {@link FirUirAddress}.
   */
  private final String firUirAddress;
  /**
   * See {@link FirUirIndicator}.
   */
  private final FirUirIndicator firUirIndicator;
  /**
   * See {@link SequenceNumber}.
   */
  private final Integer sequenceNumber;
  /**
   * See {@link ContinuationRecordNumber}.
   */
  private final String continuationRecordNumber;
  /**
   * See {@link FirUirIdentifier}.
   */
  private final String adjacentFirIdentifier;
  /**
   * See {@link FirUirIdentifier}.
   */
  private final String adjacentUirIdentifier;
  /**
   * See {@link FirUirReportingUnitsSpeed}.
   */
  private final String reportingUnitsSpeed;
  /**
   * See {@link FirUirReportingUnitsAltitude}.
   */
  private final String reportingUnitsAltitude;
  /**
   * See {@link FirUirEntryReport}.
   */
  private final Boolean firUirEntryReport;
  /**
   * See {@link BoundaryVia}.
   */
  private final BoundaryVia boundaryVia;
  /**
   * See {@link Latitude}.
   */
  private final Double firUirLatitude;
  /**
   * See {@link Longitude}.
   */
  private final Double firUirLongitude;
  /**
   * See {@link Latitude}.
   */
  private final Double arcOriginLatitude;
  /**
   * See {@link Longitude}.
   */
  private final Double arcOriginLongitude;
  /**
   * See {@link ArcDistance}.
   */
  private final Integer arcDistance;
  /**
   * See {@link ArcBearing}.
   */
  private final Integer arcBearing;
  /**
   * See {@link Limit}.
   */
  private final Double firUpperLimit;
  /**
   * See {@link Limit}.
   */
  private final Double uirLowerLimit;
  /**
   * See {@link Limit}.
   */
  private final Double uirUpperLimit;
  /**
   * See {@link CruiseTableIndicator}.
   */
  private final String cruiseTableIndicator;
  /**
   * See {@link FirUirName}.
   */
  private final String firUirName;
  /**
   * See {@link FileRecordNumber}.
   */
  private final Integer fileRecordNumber;
  /**
   * See {@link Cycle}.
   */
  private final String cycleDate;

  private ArincFirUirLeg(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.subSectionCode = builder.subSectionCode;
    this.firUirIdentifier = builder.firUirIdentifier;
    this.firUirAddress = builder.firUirAddress;
    this.firUirIndicator = builder.firUirIndicator;
    this.sequenceNumber = builder.sequenceNumber;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.adjacentFirIdentifier = builder.adjacentFirIdentifier;
    this.adjacentUirIdentifier = builder.adjacentUirIdentifier;
    this.reportingUnitsSpeed = builder.reportingUnitsSpeed;
    this.reportingUnitsAltitude = builder.reportingUnitsAltitude;
    this.firUirEntryReport = builder.firUirEntryReport;
    this.boundaryVia = builder.boundaryVia;
    this.firUirLatitude = builder.firUirLatitude;
    this.firUirLongitude = builder.firUirLongitude;
    this.arcOriginLatitude = builder.arcOriginLatitude;
    this.arcOriginLongitude = builder.arcOriginLongitude;
    this.arcDistance = builder.arcDistance;
    this.arcBearing = builder.arcBearing;
    this.firUpperLimit = builder.firUpperLimit;
    this.uirLowerLimit = builder.uirLowerLimit;
    this.uirUpperLimit = builder.uirUpperLimit;
    this.cruiseTableIndicator = builder.cruiseTableIndicator;
    this.firUirName = builder.firUirName;
    this.fileRecordNumber = builder.fileRecordNumber;
    this.cycleDate = builder.cycleDate;
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

  public Builder toBuilder() {
    return new Builder()
        .recordType(recordType())
        .customerAreaCode(customerAreaCode())
        .sectionCode(sectionCode())
        .subSectionCode(subSectionCode().orElse(null))
        .firUirIdentifier(firUirIdentifier())
        .firUirAddress(firUirAddress().orElse(null))
        .firUirIndicator(firUirIndicator())
        .sequenceNumber(sequenceNumber())
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .adjacentFirIdentifier(adjacentFirIdentifier().orElse(null))
        .adjacentUirIdentifier(adjacentUirIdentifier().orElse(null))
        .reportingUnitsSpeed(reportingUnitsSpeed().orElse(null))
        .reportingUnitsAltitude(reportingUnitsAltitude().orElse(null))
        .firUirEntryReport(entryReport().orElse(null))
        .boundaryVia(boundaryVia())
        .firUirLatitude(firUirLatitude().orElse(null))
        .firUirLongitude(firUirLongitude().orElse(null))
        .arcOriginLatitude(arcOriginLatitude().orElse(null))
        .arcOriginLongitude(arcOriginLongitude().orElse(null))
        .arcDistance(arcDistance().orElse(null))
        .arcBearing(arcBearing().orElse(null))
        .firUpperLimit(firUpperLimit().orElse(null))
        .uirLowerLimit(uirLowerLimit().orElse(null))
        .uirUpperLimit(uirUpperLimit().orElse(null))
        .cruiseTableIndicator(cruiseTableIndicator().orElse(null))
        .firUirName(firUirName().orElse(null))
        .fileRecordNumber(fileRecordNumber())
        .cycleDate(cycleDate());
  }

  public RecordType recordType() {
    return recordType;
  }

  public CustomerAreaCode customerAreaCode() {
    return customerAreaCode;
  }

  public String firUirIdentifier() {
    return firUirIdentifier;
  }

  public Optional<String> firUirAddress() {
    return Optional.ofNullable(firUirAddress);
  }

  public FirUirIndicator firUirIndicator() {
    return firUirIndicator;
  }

  public Integer sequenceNumber() {
    return sequenceNumber;
  }

  public Optional<String> adjacentFirIdentifier() {
    return Optional.ofNullable(adjacentFirIdentifier);
  }

  public Optional<String> adjacentUirIdentifier() {
    return Optional.ofNullable(adjacentUirIdentifier);
  }

  public Optional<String> reportingUnitsSpeed() {
    return Optional.ofNullable(reportingUnitsSpeed);
  }

  public Optional<String> reportingUnitsAltitude() {
    return Optional.ofNullable(reportingUnitsAltitude);
  }

  public Optional<Boolean> entryReport() {
    return Optional.ofNullable(firUirEntryReport);
  }

  public BoundaryVia boundaryVia() {
    return boundaryVia;
  }

  public Optional<Double> firUirLatitude() {
    return Optional.ofNullable(firUirLatitude);
  }

  public Optional<Double> firUirLongitude() {
    return Optional.ofNullable(firUirLongitude);
  }

  public Optional<Double> arcOriginLatitude() {
    return Optional.ofNullable(arcOriginLatitude);
  }

  public Optional<Double> arcOriginLongitude() {
    return Optional.ofNullable(arcOriginLongitude);
  }

  public Optional<Integer> arcDistance() {
    return Optional.ofNullable(arcDistance);
  }

  public Optional<Integer> arcBearing() {
    return Optional.ofNullable(arcBearing);
  }

  public Optional<Double> firUpperLimit() {
    return Optional.ofNullable(firUpperLimit);
  }

  public Optional<Double> uirLowerLimit() {
    return Optional.ofNullable(uirLowerLimit);
  }

  public Optional<Double> uirUpperLimit() {
    return Optional.ofNullable(uirUpperLimit);
  }

  public Optional<String> cruiseTableIndicator() {
    return Optional.ofNullable(cruiseTableIndicator);
  }

  public Optional<String> firUirName() {
    return Optional.ofNullable(firUirName);
  }

  public Integer fileRecordNumber() {
    return fileRecordNumber;
  }

  public String cycleDate() {
    return cycleDate;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    ArincFirUirLeg that = (ArincFirUirLeg) o;
    return recordType == that.recordType && customerAreaCode == that.customerAreaCode && sectionCode == that.sectionCode && Objects.equals(subSectionCode, that.subSectionCode) && Objects.equals(firUirIdentifier, that.firUirIdentifier) && Objects.equals(firUirAddress, that.firUirAddress) && firUirIndicator == that.firUirIndicator && Objects.equals(sequenceNumber, that.sequenceNumber) && Objects.equals(continuationRecordNumber, that.continuationRecordNumber) && Objects.equals(adjacentFirIdentifier, that.adjacentFirIdentifier) && Objects.equals(adjacentUirIdentifier, that.adjacentUirIdentifier) && Objects.equals(reportingUnitsSpeed, that.reportingUnitsSpeed) && Objects.equals(reportingUnitsAltitude, that.reportingUnitsAltitude) && Objects.equals(firUirEntryReport, that.firUirEntryReport) && boundaryVia == that.boundaryVia && Objects.equals(firUirLatitude, that.firUirLatitude) && Objects.equals(firUirLongitude, that.firUirLongitude) && Objects.equals(arcOriginLatitude, that.arcOriginLatitude) && Objects.equals(arcOriginLongitude, that.arcOriginLongitude) && Objects.equals(arcDistance, that.arcDistance) && Objects.equals(arcBearing, that.arcBearing) && Objects.equals(firUpperLimit, that.firUpperLimit) && Objects.equals(uirLowerLimit, that.uirLowerLimit) && Objects.equals(uirUpperLimit, that.uirUpperLimit) && Objects.equals(cruiseTableIndicator, that.cruiseTableIndicator) && Objects.equals(firUirName, that.firUirName) && Objects.equals(fileRecordNumber, that.fileRecordNumber) && Objects.equals(cycleDate, that.cycleDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, subSectionCode, firUirIdentifier, firUirAddress, firUirIndicator, sequenceNumber, continuationRecordNumber, adjacentFirIdentifier, adjacentUirIdentifier, reportingUnitsSpeed, reportingUnitsAltitude, firUirEntryReport, boundaryVia, firUirLatitude, firUirLongitude, arcOriginLatitude, arcOriginLongitude, arcDistance, arcBearing, firUpperLimit, uirLowerLimit, uirUpperLimit, cruiseTableIndicator, firUirName, fileRecordNumber, cycleDate);
  }

  @Override
  public String toString() {
    return "ArincFirUirLeg{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode=" + sectionCode +
        ", subSectionCode='" + subSectionCode + '\'' +
        ", firUirIdentifier='" + firUirIdentifier + '\'' +
        ", firUirAddress='" + firUirAddress + '\'' +
        ", firUirIndicator=" + firUirIndicator +
        ", sequenceNumber=" + sequenceNumber +
        ", continuationRecordNumber='" + continuationRecordNumber + '\'' +
        ", adjacentFirIdentifier='" + adjacentFirIdentifier + '\'' +
        ", adjacentUirIdentifier='" + adjacentUirIdentifier + '\'' +
        ", reportingUnitsSpeed='" + reportingUnitsSpeed + '\'' +
        ", reportingUnitsAltitude='" + reportingUnitsAltitude + '\'' +
        ", firUirEntryReport=" + firUirEntryReport +
        ", boundaryVia=" + boundaryVia +
        ", firUirLatitude=" + firUirLatitude +
        ", firUirLongitude=" + firUirLongitude +
        ", arcOriginLatitude=" + arcOriginLatitude +
        ", arcOriginLongitude=" + arcOriginLongitude +
        ", arcDistance=" + arcDistance +
        ", arcBearing=" + arcBearing +
        ", firUpperLimit=" + firUpperLimit +
        ", uirLowerLimit=" + uirLowerLimit +
        ", uirUpperLimit=" + uirUpperLimit +
        ", cruiseTableIndicator='" + cruiseTableIndicator + '\'' +
        ", firUirName='" + firUirName + '\'' +
        ", fileRecordNumber=" + fileRecordNumber +
        ", cycleDate='" + cycleDate + '\'' +
        '}';
  }

  public static final class Builder {
    private RecordType recordType;
    private CustomerAreaCode customerAreaCode;
    private SectionCode sectionCode;
    private String subSectionCode;
    private String firUirIdentifier;
    private String firUirAddress;
    private FirUirIndicator firUirIndicator;
    private Integer sequenceNumber;
    private String continuationRecordNumber;
    private String adjacentFirIdentifier;
    private String adjacentUirIdentifier;
    private String reportingUnitsSpeed;
    private String reportingUnitsAltitude;
    private Boolean firUirEntryReport;
    private BoundaryVia boundaryVia;
    private Double firUirLatitude;
    private Double firUirLongitude;
    private Double arcOriginLatitude;
    private Double arcOriginLongitude;
    private Integer arcDistance;
    private Integer arcBearing;
    private Double firUpperLimit;
    private Double uirLowerLimit;
    private Double uirUpperLimit;
    private String cruiseTableIndicator;
    private String firUirName;
    private Integer fileRecordNumber;
    private String cycleDate;

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

    public Builder firUirIdentifier(String firUirIdentifier) {
      this.firUirIdentifier = firUirIdentifier;
      return this;
    }

    public Builder firUirAddress(String firUirAddress) {
      this.firUirAddress = firUirAddress;
      return this;
    }

    public Builder firUirIndicator(FirUirIndicator firUirIndicator) {
      this.firUirIndicator = firUirIndicator;
      return this;
    }

    public Builder sequenceNumber(Integer sequenceNumber) {
      this.sequenceNumber = sequenceNumber;
      return this;
    }

    public Builder continuationRecordNumber(String continuationRecordNumber) {
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder adjacentFirIdentifier(String adjacentFirIdentifier) {
      this.adjacentFirIdentifier = adjacentFirIdentifier;
      return this;
    }

    public Builder adjacentUirIdentifier(String adjacentUirIdentifier) {
      this.adjacentUirIdentifier = adjacentUirIdentifier;
      return this;
    }

    public Builder reportingUnitsSpeed(String reportingUnitsSpeed) {
      this.reportingUnitsSpeed = reportingUnitsSpeed;
      return this;
    }

    public Builder reportingUnitsAltitude(String reportingUnitsAltitude) {
      this.reportingUnitsAltitude = reportingUnitsAltitude;
      return this;
    }

    public Builder firUirEntryReport(Boolean entryReport) {
      this.firUirEntryReport = entryReport;
      return this;
    }

    public Builder boundaryVia(BoundaryVia boundaryVia) {
      this.boundaryVia = boundaryVia;
      return this;
    }

    public Builder firUirLatitude(Double firUirLatitude) {
      this.firUirLatitude = firUirLatitude;
      return this;
    }

    public Builder firUirLongitude(Double firUirLongitude) {
      this.firUirLongitude = firUirLongitude;
      return this;
    }

    public Builder arcOriginLatitude(Double arcOriginLatitude) {
      this.arcOriginLatitude = arcOriginLatitude;
      return this;
    }

    public Builder arcOriginLongitude(Double arcOriginLongitude) {
      this.arcOriginLongitude = arcOriginLongitude;
      return this;
    }

    public Builder arcDistance(Integer arcDistance) {
      this.arcDistance = arcDistance;
      return this;
    }

    public Builder arcBearing(Integer arcBearing) {
      this.arcBearing = arcBearing;
      return this;
    }

    public Builder firUpperLimit(Double firUpperLimit) {
      this.firUpperLimit = firUpperLimit;
      return this;
    }

    public Builder uirLowerLimit(Double uirLowerLimit) {
      this.uirLowerLimit = uirLowerLimit;
      return this;
    }

    public Builder uirUpperLimit(Double uirUpperLimit) {
      this.uirUpperLimit = uirUpperLimit;
      return this;
    }

    public Builder cruiseTableIndicator(String cruiseTableIndicator) {
      this.cruiseTableIndicator = cruiseTableIndicator;
      return this;
    }

    public Builder firUirName(String firUirName) {
      this.firUirName = firUirName;
      return this;
    }

    public Builder fileRecordNumber(Integer fileRecordNumber) {
      this.fileRecordNumber = fileRecordNumber;
      return this;
    }

    public Builder cycleDate(String cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }

    public ArincFirUirLeg build() {
      return new ArincFirUirLeg(this);
    }
  }
}
