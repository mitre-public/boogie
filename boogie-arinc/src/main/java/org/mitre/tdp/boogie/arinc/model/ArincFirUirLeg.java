package org.mitre.tdp.boogie.arinc.model;

import org.mitre.tdp.boogie.arinc.v18.field.*;

import java.util.Optional;

/**
 * Section/Subsection = UF
 */
public class ArincFirUirLeg implements ArincModel {
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
  private final String firUirIndicator;
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
   * See {@link ReportingUnitsSpeed}.
   */
  private final String reportingUnitsSpeed;
  /**
   * See {@link ReportingUnitsAltitude}.
   */
  private final String reportingUnitsAltitude;
  /**
   * See {@link EntryReport}.
   */
  private final String entryReport;
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
  private final String firUpperLimit;
  /**
   * See {@link Limit}.
   */
  private final String uirLowerLimit;
  /**
   * See {@link Limit}.
   */
  private final String uirUpperLimit;
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
    this.entryReport = builder.entryReport;
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

  public RecordType recordType() {
    return recordType;
  }

  public CustomerAreaCode customerAreaCode() {
    return customerAreaCode;
  }

  public String firUirIdentifier() {
    return firUirIdentifier;
  }

  public String firUirAddress() {
    return firUirAddress;
  }

  public String firUirIndicator() {
    return firUirIndicator;
  }

  public Integer sequenceNumber() {
    return sequenceNumber;
  }

  public String adjacentFirIdentifier() {
    return adjacentFirIdentifier;
  }

  public String adjacentUirIdentifier() {
    return adjacentUirIdentifier;
  }

  public String reportingUnitsSpeed() {
    return reportingUnitsSpeed;
  }

  public String reportingUnitsAltitude() {
    return reportingUnitsAltitude;
  }

  public String entryReport() {
    return entryReport;
  }

  public BoundaryVia boundaryVia() {
    return boundaryVia;
  }

  public Double firUirLatitude() {
    return firUirLatitude;
  }

  public Double firUirLongitude() {
    return firUirLongitude;
  }

  public Double arcOriginLatitude() {
    return arcOriginLatitude;
  }

  public Double arcOriginLongitude() {
    return arcOriginLongitude;
  }

  public Integer arcDistance() {
    return arcDistance;
  }

  public Integer arcBearing() {
    return arcBearing;
  }

  public String firUpperLimit() {
    return firUpperLimit;
  }

  public String uirLowerLimit() {
    return uirLowerLimit;
  }

  public String uirUpperLimit() {
    return uirUpperLimit;
  }

  public String cruiseTableIndicator() {
    return cruiseTableIndicator;
  }

  public String firUirName() {
    return firUirName;
  }

  public Integer fileRecordNumber() {
    return fileRecordNumber;
  }

  public String cycleDate() {
    return cycleDate;
  }

  public static final class Builder {
    private RecordType recordType;
    private CustomerAreaCode customerAreaCode;
    private SectionCode sectionCode;
    private String subSectionCode;
    private String firUirIdentifier;
    private String firUirAddress;
    private String firUirIndicator;
    private Integer sequenceNumber;
    private String continuationRecordNumber;
    private String adjacentFirIdentifier;
    private String adjacentUirIdentifier;
    private String reportingUnitsSpeed;
    private String reportingUnitsAltitude;
    private String entryReport;
    private BoundaryVia boundaryVia;
    private Double firUirLatitude;
    private Double firUirLongitude;
    private Double arcOriginLatitude;
    private Double arcOriginLongitude;
    private Integer arcDistance;
    private Integer arcBearing;
    private String firUpperLimit;
    private String uirLowerLimit;
    private String uirUpperLimit;
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

    public Builder firUirIndicator(String firUirIndicator) {
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

    public Builder entryReport(String entryReport) {
      this.entryReport = entryReport;
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

    public Builder firUpperLimit(String firUpperLimit) {
      this.firUpperLimit = firUpperLimit;
      return this;
    }

    public Builder uirLowerLimit(String uirLowerLimit) {
      this.uirLowerLimit = uirLowerLimit;
      return this;
    }

    public Builder uirUpperLimit(String uirUpperLimit) {
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
