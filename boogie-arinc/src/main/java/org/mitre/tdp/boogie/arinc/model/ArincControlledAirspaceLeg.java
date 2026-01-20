package org.mitre.tdp.boogie.arinc.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.AirspaceCenter;
import org.mitre.tdp.boogie.arinc.v18.field.AirspaceClassification;
import org.mitre.tdp.boogie.arinc.v18.field.AirspaceType;
import org.mitre.tdp.boogie.arinc.v18.field.ArcBearing;
import org.mitre.tdp.boogie.arinc.v18.field.ArcDistance;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.ControlledAirspaceName;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.Limit;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.MultipleCode;
import org.mitre.tdp.boogie.arinc.v18.field.Notam;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.Rnp;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SequenceNumber;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.TimeCode;
import org.mitre.tdp.boogie.arinc.v18.field.UnitIndicator;

/**
 * Section/Subsection = UC
 */
public final class ArincControlledAirspaceLeg implements ArincModel {
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
   * See {@link IcaoRegion}
   */
  private final String icaoRegion;
  /**
   * See {@link AirspaceType}
   */
  private final AirspaceType airspaceType;
  /**
   * See {@link AirspaceCenter}
   */
  private final String airspaceCenter;
  /**
   * See {@link SectionCode}.
   */
  private final String supplierSectionCode;
  /**
   * See {@link SubSectionCode}.
   */
  private final String supplierSubSectionCode;
  /**
   * See {@link AirspaceClassification}
   */
  private final String airspaceClassification;
  /**
   * See {@link MultipleCode}
   */
  private final String multipleCode;
  /**
   * See {@link SequenceNumber}
   */
  private final Integer sequenceNumber;
  /**
   * See {@link ContinuationRecordNumber}
   */
  private final String continuationRecordNumber;
  /**
   * See {@link Level}
   */
  private final Level level;
  /**
   * See {@link TimeCode}
   */
  private final String timeCode;
  /**
   * See {@link Notam}
   */
  private final String notam;
  /**
   * See {@link BoundaryVia}
   */
  private final BoundaryVia boundaryVia;
  /**
   * See {@link Latitude}
   */
  private final Double latitude;
  /**
   * See {@link Longitude}
   */
  private final Double longitude;
  /**
   * See {@link Latitude}
   */
  private final Double arcOriginLatitude;
  /**
   * See {@link Longitude}
   */
  private final Double arcOriginLongitude;
  /**
   * See {@link ArcDistance}
   */
  private final Integer arcDistance;
  /**
   * See {@link ArcBearing}
   */
  private final Integer arcBearing;
  /**
   * See {@link Rnp}
   */
  private final Double rnp;
  /**
   * See {@link Limit}
   */
  private final Double lowerLimit;
  /**
   * See {@link UnitIndicator}
   */
  private final String lowerUnitIndicator;
  /**
   * See {@link Limit}
   */
  private final Double upperLimit;
  /**
   * See {@link UnitIndicator}
   */
  private final String upperUnitIndicator;
  /**
   * See {@link ControlledAirspaceName}
   */
  private final String controlledAirspaceName;
  /**
   * See {@link FileRecordNumber}
   */
  private final Integer fileRecordNumber;
  /**
   * See {@link Cycle}
   */
  private final String cycleDate;

  private ArincControlledAirspaceLeg(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.subSectionCode = builder.subSectionCode;
    this.icaoRegion = builder.icaoCode;
    this.airspaceType = builder.airspaceType;
    this.airspaceCenter = builder.airspaceCenter;
    this.supplierSectionCode = builder.supplierSectionCode;
    this.supplierSubSectionCode = builder.supplierSubSectionCode;
    this.airspaceClassification = builder.airspaceClassification;
    this.multipleCode = builder.multipleCode;
    this.sequenceNumber = builder.sequenceNumber;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.level = builder.level;
    this.timeCode = builder.timeCode;
    this.notam = builder.notam;
    this.boundaryVia = builder.boundaryVia;
    this.latitude = builder.latitude;
    this.longitude = builder.longitude;
    this.arcOriginLatitude = builder.arcOriginLatitude;
    this.arcOriginLongitude = builder.arcOriginLongitude;
    this.arcDistance = builder.arcDistance;
    this.arcBearing = builder.arcBearing;
    this.rnp = builder.rnp;
    this.lowerLimit = builder.lowerLimit;
    this.lowerUnitIndicator = builder.lowerUnitIndicator;
    this.upperLimit = builder.upperLimit;
    this.upperUnitIndicator = builder.upperUnitIndicator;
    this.controlledAirspaceName = builder.controlledAirspaceName;
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
        .icaoCode(icaoRegion())
        .airspaceType(airspaceType())
        .airspaceCenter(airspaceCenter())
        .suppliedSectionCode(supplierSectionCode().map(Enum::name).orElse(null))
        .supplierSubSectionCode(supplierSubSectionCode().orElse(null))
        .airspaceClassification(airspaceClassification().orElse(null))
        .multipleCode(multipleCode().orElse(null))
        .sequenceNumber(sequenceNumber())
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .level(level().orElse(null))
        .timeCode(timeCode().orElse(null))
        .notam(notam().orElse(null))
        .boundaryVia(boundaryVia())
        .latitude(latitude().orElse(null))
        .longitude(longitude().orElse(null))
        .arcOriginLatitude(arcOriginLatitude().orElse(null))
        .arcOriginLongitude(arcOriginLongitude().orElse(null))
        .arcDistance(arcDistance().orElse(null))
        .arcBearing(arcBearing().orElse(null))
        .rnp(rnp().orElse(null))
        .lowerLimit(lowerLimit().orElse(null))
        .lowerUnitIndicator(lowerUnitIndicator().orElse(null))
        .upperLimit(upperLimit().orElse(null))
        .upperUnitIndicator(upperUnitIndicator().orElse(null))
        .controlledAirspaceName(controlledAirspaceName().orElse(null))
        .fileRecordNumber(fileRecordNumber())
        .cycleDate(cycleDate());
  }

  public String airspaceCenter() {
    return airspaceCenter;
  }

  public RecordType recordType() {
    return recordType;
  }

  public CustomerAreaCode customerAreaCode() {
    return customerAreaCode;
  }

  public String icaoRegion() {
    return icaoRegion;
  }

  public AirspaceType airspaceType() {
    return airspaceType;
  }

  public Optional<SectionCode> supplierSectionCode() {
    return Optional.ofNullable(supplierSectionCode).map(SectionCode::valueOf);
  }

  public Optional<String> supplierSubSectionCode() {
    return Optional.ofNullable(supplierSubSectionCode);
  }

  public Optional<String> airspaceClassification() {
    return Optional.ofNullable(airspaceClassification);
  }

  public Optional<String> multipleCode() {
    return Optional.ofNullable(multipleCode);
  }

  public Integer sequenceNumber() {
    return sequenceNumber;
  }

  public Optional<Level> level() {
    return Optional.ofNullable(level);
  }

  public Optional<String> timeCode() {
    return Optional.ofNullable(timeCode);
  }

  public Optional<String> notam() {
    return Optional.ofNullable(notam);
  }

  public BoundaryVia boundaryVia() {
    return boundaryVia;
  }

  public Optional<Double> latitude() {
    return Optional.ofNullable(latitude);
  }

  public Optional<Double> longitude() {
    return Optional.ofNullable(longitude);
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

  public Optional<Double> rnp() {
    return Optional.ofNullable(rnp);
  }

  public Optional<Double> lowerLimit() {
    return Optional.ofNullable(lowerLimit);
  }

  public Optional<String> lowerUnitIndicator() {
    return Optional.ofNullable(lowerUnitIndicator);
  }

  public Optional<Double> upperLimit() {
    return Optional.ofNullable(upperLimit);
  }

  public Optional<String> upperUnitIndicator() {
    return Optional.ofNullable(upperUnitIndicator);
  }

  public Optional<String> controlledAirspaceName() {
    return Optional.ofNullable(controlledAirspaceName);
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
    ArincControlledAirspaceLeg that = (ArincControlledAirspaceLeg) o;
    return recordType == that.recordType && customerAreaCode == that.customerAreaCode && sectionCode == that.sectionCode && Objects.equals(subSectionCode, that.subSectionCode) && Objects.equals(icaoRegion, that.icaoRegion) && airspaceType == that.airspaceType && Objects.equals(airspaceCenter, that.airspaceCenter) && Objects.equals(supplierSectionCode, that.supplierSectionCode) && Objects.equals(supplierSubSectionCode, that.supplierSubSectionCode) && Objects.equals(airspaceClassification, that.airspaceClassification) && Objects.equals(multipleCode, that.multipleCode) && Objects.equals(sequenceNumber, that.sequenceNumber) && Objects.equals(continuationRecordNumber, that.continuationRecordNumber) && level == that.level && Objects.equals(timeCode, that.timeCode) && Objects.equals(notam, that.notam) && boundaryVia == that.boundaryVia && Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude) && Objects.equals(arcOriginLatitude, that.arcOriginLatitude) && Objects.equals(arcOriginLongitude, that.arcOriginLongitude) && Objects.equals(arcDistance, that.arcDistance) && Objects.equals(arcBearing, that.arcBearing) && Objects.equals(rnp, that.rnp) && Objects.equals(lowerLimit, that.lowerLimit) && Objects.equals(lowerUnitIndicator, that.lowerUnitIndicator) && Objects.equals(upperLimit, that.upperLimit) && Objects.equals(upperUnitIndicator, that.upperUnitIndicator) && Objects.equals(controlledAirspaceName, that.controlledAirspaceName) && Objects.equals(fileRecordNumber, that.fileRecordNumber) && Objects.equals(cycleDate, that.cycleDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, subSectionCode, icaoRegion, airspaceType, airspaceCenter, supplierSectionCode, supplierSubSectionCode, airspaceClassification, multipleCode, sequenceNumber, continuationRecordNumber, level, timeCode, notam, boundaryVia, latitude, longitude, arcOriginLatitude, arcOriginLongitude, arcDistance, arcBearing, rnp, lowerLimit, lowerUnitIndicator, upperLimit, upperUnitIndicator, controlledAirspaceName, fileRecordNumber, cycleDate);
  }

  @Override
  public String toString() {
    return "ArincControlledAirspaceLeg{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode=" + sectionCode +
        ", subSectionCode='" + subSectionCode + '\'' +
        ", icaoRegion='" + icaoRegion + '\'' +
        ", airspaceType=" + airspaceType +
        ", airspaceCenter='" + airspaceCenter + '\'' +
        ", supplierSectionCode='" + supplierSectionCode + '\'' +
        ", supplierSubSectionCode='" + supplierSubSectionCode + '\'' +
        ", airspaceClassification='" + airspaceClassification + '\'' +
        ", multipleCode='" + multipleCode + '\'' +
        ", sequenceNumber=" + sequenceNumber +
        ", continuationRecordNumber='" + continuationRecordNumber + '\'' +
        ", level=" + level +
        ", timeCode='" + timeCode + '\'' +
        ", notam='" + notam + '\'' +
        ", boundaryVia=" + boundaryVia +
        ", latitude=" + latitude +
        ", longitude=" + longitude +
        ", arcOriginLatitude=" + arcOriginLatitude +
        ", arcOriginLongitude=" + arcOriginLongitude +
        ", arcDistance=" + arcDistance +
        ", arcBearing=" + arcBearing +
        ", rnp=" + rnp +
        ", lowerLimit=" + lowerLimit +
        ", lowerUnitIndicator='" + lowerUnitIndicator + '\'' +
        ", upperLimit=" + upperLimit +
        ", upperUnitIndicator='" + upperUnitIndicator + '\'' +
        ", controlledAirspaceName='" + controlledAirspaceName + '\'' +
        ", fileRecordNumber=" + fileRecordNumber +
        ", cycleDate='" + cycleDate + '\'' +
        '}';
  }

  public static final class Builder {
    private RecordType recordType;
    private CustomerAreaCode customerAreaCode;
    private SectionCode sectionCode;
    private String subSectionCode;
    private String icaoCode;
    private AirspaceType airspaceType;
    private String airspaceCenter;
    private String supplierSectionCode;
    private String supplierSubSectionCode;
    private String airspaceClassification;
    private String multipleCode;
    private Integer sequenceNumber;
    private String continuationRecordNumber;
    private Level level;
    private String timeCode;
    private String notam;
    private BoundaryVia boundaryVia;
    private Double latitude;
    private Double longitude;
    private Double arcOriginLatitude;
    private Double arcOriginLongitude;
    private Integer arcDistance;
    private Integer arcBearing;
    private Double rnp;
    private Double lowerLimit;
    private String lowerUnitIndicator;
    private Double upperLimit;
    private String upperUnitIndicator;
    private String controlledAirspaceName;
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

    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder airspaceType(AirspaceType airspaceType) {
      this.airspaceType = airspaceType;
      return this;
    }

    public Builder airspaceCenter(String airspaceCenter) {
      this.airspaceCenter = airspaceCenter;
      return this;
    }

    public Builder suppliedSectionCode(String suppliedSectionCode) {
      this.supplierSectionCode = suppliedSectionCode;
      return this;
    }

    public Builder supplierSubSectionCode(String supplierSubSectionCode) {
      this.supplierSubSectionCode = supplierSubSectionCode;
      return this;
    }

    public Builder airspaceClassification(String airspaceClassification) {
      this.airspaceClassification = airspaceClassification;
      return this;
    }

    public Builder multipleCode(String multipleCode) {
      this.multipleCode = multipleCode;
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

    public Builder level(Level level) {
      this.level = level;
      return this;
    }

    public Builder timeCode(String timeCode) {
      this.timeCode = timeCode;
      return this;
    }

    public Builder notam(String notam) {
      this.notam = notam;
      return this;
    }

    public Builder boundaryVia(BoundaryVia boundaryVia) {
      this.boundaryVia = boundaryVia;
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

    public Builder rnp(Double rnp) {
      this.rnp = rnp;
      return this;
    }

    public Builder lowerLimit(Double lowerLimit) {
      this.lowerLimit = lowerLimit;
      return this;
    }

    public Builder lowerUnitIndicator(String lowerUnitIndicator) {
      this.lowerUnitIndicator = lowerUnitIndicator;
      return this;
    }

    public Builder upperLimit(Double upperLimit) {
      this.upperLimit = upperLimit;
      return this;
    }

    public Builder upperUnitIndicator(String upperUnitIndicator) {
      this.upperUnitIndicator = upperUnitIndicator;
      return this;
    }

    public Builder controlledAirspaceName(String controlledAirspaceName) {
      this.controlledAirspaceName = controlledAirspaceName;
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

    public ArincControlledAirspaceLeg build() {
      return new ArincControlledAirspaceLeg(this);
    }


  }
}
