package org.mitre.tdp.boogie.arinc.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.RunwayLength;
import org.mitre.tdp.boogie.arinc.v18.field.RunwayWidth;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.ThresholdDisplacementDistance;

/**
 * Data class for representing structured/parsed content from within an ARINC format runway record.
 * <br>
 * Section/Subsection = PG
 */
public final class ArincRunway implements ArincModel {

  private final RecordType recordType;
  private final CustomerAreaCode customerAreaCode;
  private final SectionCode sectionCode;
  private final String airportIdentifier;
  private final String airportIcaoRegion;
  private final String subSectionCode;
  private final String runwayIdentifier;
  private final String continuationRecordNumber;
  /**
   * The length of the runway (with no regard to displaced thresholds, so the full physical length) in feet with a resolution
   * of one foot.
   * <br>
   * See {@link RunwayLength}
   */
  private final Integer runwayLength;
  private final Double runwayMagneticBearing;
  private final double latitude;
  private final double longitude;
  private final Double runwayGradient;
  private final Integer landingThresholdElevation;
  /**
   * Distance from the displaced threshold of the runway to its physical extent in feet.
   * <br>
   * See {@link ThresholdDisplacementDistance}
   */
  private final Integer thresholdDisplacementDistance;
  private final Integer thresholdCrossingHeight;
  /**
   * Width of the runway (in feet), resolution is 1 foot.
   * <br>
   * See {@link RunwayWidth}
   */
  private final Integer runwayWidth;
  private final String ilsMlsGlsIdentifier;
  private final String ilsMlsGlsCategory;
  private final Integer stopway;
  private final String secondaryIlsMlsGlsIdentifier;
  private final String secondaryIlsMlsGlsCategory;
  private final String runwayAccuracyComplianceFlag;
  private final String landingThresholdAccuracyComplianceFlag;
  private final String runwayDescription;
  private final int fileRecordNumber;
  private final String lastUpdateCycle;
  private final

  private ArincRunway(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.airportIdentifier = builder.airportIdentifier;
    this.airportIcaoRegion = builder.airportIcaoRegion;
    this.subSectionCode = builder.subSectionCode;
    this.runwayIdentifier = builder.runwayIdentifier;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.runwayLength = builder.runwayLength;
    this.runwayMagneticBearing = builder.runwayMagneticBearing;
    this.latitude = builder.latitude;
    this.longitude = builder.longitude;
    this.runwayGradient = builder.runwayGradient;
    this.landingThresholdElevation = builder.landingThresholdElevation;
    this.thresholdDisplacementDistance = builder.thresholdDisplacementDistance;
    this.thresholdCrossingHeight = builder.thresholdCrossingHeight;
    this.runwayWidth = builder.runwayWidth;
    this.ilsMlsGlsIdentifier = builder.ilsMlsGlsIdentifier;
    this.ilsMlsGlsCategory = builder.ilsMlsGlsCategory;
    this.stopway = builder.stopway;
    this.secondaryIlsMlsGlsIdentifier = builder.secondaryIlsMlsGlsIdentifier;
    this.secondaryIlsMlsGlsCategory = builder.secondaryIlsMlsGlsCategory;
    this.runwayAccuracyComplianceFlag = builder.runwayAccuracyComplianceFlag;
    this.landingThresholdAccuracyComplianceFlag = builder.landingThresholdElevationComplianceFlag;
    this.runwayDescription = builder.runwayDescription;
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

  public String airportIdentifier() {
    return airportIdentifier;
  }

  public String airportIcaoRegion() {
    return airportIcaoRegion;
  }

  @Override
  public Optional<String> subSectionCode() {
    return Optional.of(subSectionCode);
  }

  public String runwayIdentifier() {
    return runwayIdentifier;
  }

  public Optional<String> continuationRecordNumber() {
    return Optional.ofNullable(continuationRecordNumber);
  }

  public Optional<Integer> runwayLength() {
    return Optional.ofNullable(runwayLength);
  }

  public Optional<Double> runwayMagneticBearing() {
    return Optional.ofNullable(runwayMagneticBearing);
  }

  public double latitude() {
    return latitude;
  }

  public double longitude() {
    return longitude;
  }

  public Optional<Double> runwayGradient() {
    return Optional.ofNullable(runwayGradient);
  }

  public Optional<Integer> landingThresholdElevation() {
    return Optional.ofNullable(landingThresholdElevation);
  }

  public Optional<Integer> thresholdDisplacementDistance() {
    return Optional.ofNullable(thresholdDisplacementDistance);
  }

  public Optional<Integer> thresholdCrossingHeight() {
    return Optional.ofNullable(thresholdCrossingHeight);
  }

  public Optional<Integer> runwayWidth() {
    return Optional.ofNullable(runwayWidth);
  }

  public Optional<String> ilsMlsGlsIdentifier() {
    return Optional.ofNullable(ilsMlsGlsIdentifier);
  }

  public Optional<String> ilsMlsGlsCategory() {
    return Optional.ofNullable(ilsMlsGlsCategory);
  }

  public Optional<Integer> stopway() {
    return Optional.ofNullable(stopway);
  }

  public Optional<String> secondaryIlsMlsGlsIdentifier() {
    return Optional.ofNullable(secondaryIlsMlsGlsIdentifier);
  }

  public Optional<String> secondaryIlsMlsGlsCategory() {
    return Optional.ofNullable(secondaryIlsMlsGlsCategory);
  }

  public Optional<String> runwayAccuracyComplianceFlag() {
    return Optional.ofNullable(runwayAccuracyComplianceFlag);
  }

  public Optional<String> landingThresholdAccuracyComplianceFlag() {
    return Optional.ofNullable(landingThresholdAccuracyComplianceFlag);
  }

  public Optional<String> runwayDescription() {
    return Optional.ofNullable(runwayDescription);
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
        .airportIdentifier(airportIdentifier())
        .airportIcaoRegion(airportIcaoRegion())
        .subSectionCode(subSectionCode().orElseThrow(IllegalStateException::new))
        .runwayIdentifier(runwayIdentifier())
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .runwayLength(runwayLength().orElse(null))
        .runwayMagneticBearing(runwayMagneticBearing().orElse(null))
        .latitude(latitude())
        .longitude(longitude())
        .runwayGradient(runwayGradient().orElse(null))
        .landingThresholdElevation(landingThresholdElevation().orElse(null))
        .thresholdDisplacementDistance(thresholdDisplacementDistance().orElse(null))
        .thresholdCrossingHeight(thresholdCrossingHeight().orElse(null))
        .runwayWidth(runwayWidth().orElse(null))
        .ilsMlsGlsIdentifier(ilsMlsGlsIdentifier().orElse(null))
        .ilsMlsGlsCategory(ilsMlsGlsCategory().orElse(null))
        .stopway(stopway().orElse(null))
        .secondaryIlsMlsGlsIdentifier(secondaryIlsMlsGlsIdentifier().orElse(null))
        .secondaryIlsMlsGlsCategory(secondaryIlsMlsGlsCategory().orElse(null))
        .runwayAccuracyComplianceFlag(runwayAccuracyComplianceFlag().orElse(null))
        .landingThresholdElevationComplianceFlag(runwayAccuracyComplianceFlag().orElse(null))
        .runwayDescription(runwayDescription().orElse(null))
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
    ArincRunway that = (ArincRunway) o;
    return recordType == that.recordType &&
        customerAreaCode == that.customerAreaCode &&
        sectionCode == that.sectionCode &&
        Objects.equals(airportIdentifier, that.airportIdentifier) &&
        Objects.equals(airportIcaoRegion, that.airportIcaoRegion) &&
        Objects.equals(subSectionCode, that.subSectionCode) &&
        Objects.equals(runwayIdentifier, that.runwayIdentifier) &&
        Objects.equals(continuationRecordNumber, that.continuationRecordNumber) &&
        Objects.equals(runwayLength, that.runwayLength) &&
        Objects.equals(runwayMagneticBearing, that.runwayMagneticBearing) &&
        Objects.equals(latitude, that.latitude) &&
        Objects.equals(longitude, that.longitude) &&
        Objects.equals(runwayGradient, that.runwayGradient) &&
        Objects.equals(landingThresholdElevation, that.landingThresholdElevation) &&
        Objects.equals(thresholdDisplacementDistance, that.thresholdDisplacementDistance) &&
        Objects.equals(thresholdCrossingHeight, that.thresholdCrossingHeight) &&
        Objects.equals(runwayWidth, that.runwayWidth) &&
        Objects.equals(ilsMlsGlsIdentifier, that.ilsMlsGlsIdentifier) &&
        Objects.equals(ilsMlsGlsCategory, that.ilsMlsGlsCategory) &&
        Objects.equals(stopway, that.stopway) &&
        Objects.equals(secondaryIlsMlsGlsIdentifier, that.secondaryIlsMlsGlsIdentifier) &&
        Objects.equals(secondaryIlsMlsGlsCategory, that.secondaryIlsMlsGlsCategory) &&
        Objects.equals(runwayAccuracyComplianceFlag, that.runwayAccuracyComplianceFlag) &&
        Objects.equals(landingThresholdAccuracyComplianceFlag, that.landingThresholdAccuracyComplianceFlag) &&
        Objects.equals(runwayDescription, that.runwayDescription) &&
        Objects.equals(fileRecordNumber, that.fileRecordNumber) &&
        Objects.equals(lastUpdateCycle, that.lastUpdateCycle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, airportIdentifier, airportIcaoRegion, subSectionCode, runwayIdentifier, continuationRecordNumber, runwayLength, runwayMagneticBearing, latitude, longitude, runwayGradient, landingThresholdElevation, thresholdDisplacementDistance, thresholdCrossingHeight, runwayWidth, ilsMlsGlsIdentifier, ilsMlsGlsCategory, stopway, secondaryIlsMlsGlsIdentifier, secondaryIlsMlsGlsCategory, runwayAccuracyComplianceFlag, landingThresholdAccuracyComplianceFlag, runwayDescription, fileRecordNumber, lastUpdateCycle);
  }

  @Override
  public String toString() {
    return "ArincRunway{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode=" + sectionCode +
        ", airportIdentifier='" + airportIdentifier + '\'' +
        ", airportIcaoRegion='" + airportIcaoRegion + '\'' +
        ", subSectionCode='" + subSectionCode + '\'' +
        ", runwayIdentifier='" + runwayIdentifier + '\'' +
        ", continuationRecordNumber='" + continuationRecordNumber + '\'' +
        ", runwayLength=" + runwayLength +
        ", runwayMagneticBearing=" + runwayMagneticBearing +
        ", latitude=" + latitude +
        ", longitude=" + longitude +
        ", runwayGradient=" + runwayGradient +
        ", landingThresholdElevation=" + landingThresholdElevation +
        ", thresholdDisplacementDistance=" + thresholdDisplacementDistance +
        ", thresholdCrossingHeight=" + thresholdCrossingHeight +
        ", runwayWidth=" + runwayWidth +
        ", ilsMlsGlsIdentifier='" + ilsMlsGlsIdentifier + '\'' +
        ", ilsMlsGlsCategory='" + ilsMlsGlsCategory + '\'' +
        ", stopway=" + stopway +
        ", secondaryIlsMlsGlsIdentifier='" + secondaryIlsMlsGlsIdentifier + '\'' +
        ", secondaryIlsMlsGlsCategory='" + secondaryIlsMlsGlsCategory + '\'' +
        ", runwayAccuracyComplianceFlag='" + runwayAccuracyComplianceFlag + '\'' +
        ", landingThresholdAccuracyComplianceFlag='" + landingThresholdAccuracyComplianceFlag + '\'' +
        ", runwayDescription='" + runwayDescription + '\'' +
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
    private String runwayIdentifier;
    private String continuationRecordNumber;
    private Integer runwayLength;
    private Double runwayMagneticBearing;
    private Double latitude;
    private Double longitude;
    private Double runwayGradient;
    private Integer landingThresholdElevation;
    private Integer thresholdDisplacementDistance;
    private Integer thresholdCrossingHeight;
    private Integer runwayWidth;
    private String ilsMlsGlsIdentifier;
    private String ilsMlsGlsCategory;
    private Integer stopway;
    private String secondaryIlsMlsGlsIdentifier;
    private String secondaryIlsMlsGlsCategory;
    private String runwayAccuracyComplianceFlag;
    private String landingThresholdElevationComplianceFlag;
    private String runwayDescription;
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

    public Builder runwayIdentifier(String runwayIdentifier) {
      this.runwayIdentifier = runwayIdentifier;
      return this;
    }

    public Builder continuationRecordNumber(String continuationRecordNumber) {
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder runwayLength(Integer runwayLength) {
      this.runwayLength = runwayLength;
      return this;
    }

    public Builder runwayMagneticBearing(Double runwayMagneticBearing) {
      this.runwayMagneticBearing = runwayMagneticBearing;
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

    public Builder runwayGradient(Double runwayGradient) {
      this.runwayGradient = runwayGradient;
      return this;
    }

    public Builder landingThresholdElevation(Integer landingThresholdElevation) {
      this.landingThresholdElevation = landingThresholdElevation;
      return this;
    }

    public Builder thresholdDisplacementDistance(Integer thresholdDisplacementDistance) {
      this.thresholdDisplacementDistance = thresholdDisplacementDistance;
      return this;
    }

    public Builder thresholdCrossingHeight(Integer thresholdCrossingHeight) {
      this.thresholdCrossingHeight = thresholdCrossingHeight;
      return this;
    }

    public Builder runwayWidth(Integer runwayWidth) {
      this.runwayWidth = runwayWidth;
      return this;
    }

    public Builder ilsMlsGlsIdentifier(String ilsMlsGlsIdentifier) {
      this.ilsMlsGlsIdentifier = ilsMlsGlsIdentifier;
      return this;
    }

    public Builder ilsMlsGlsCategory(String ilsMlsGlsCategory) {
      this.ilsMlsGlsCategory = ilsMlsGlsCategory;
      return this;
    }

    public Builder stopway(Integer stopway) {
      this.stopway = stopway;
      return this;
    }

    public Builder secondaryIlsMlsGlsIdentifier(String secondaryIlsMlsGlsIdentifier) {
      this.secondaryIlsMlsGlsIdentifier = secondaryIlsMlsGlsIdentifier;
      return this;
    }

    public Builder secondaryIlsMlsGlsCategory(String secondaryIlsMlsGlsCategory) {
      this.secondaryIlsMlsGlsCategory = secondaryIlsMlsGlsCategory;
      return this;
    }


    public Builder runwayAccuracyComplianceFlag(String runwayAccuracyComplianceFlag) {
      this.runwayAccuracyComplianceFlag = runwayAccuracyComplianceFlag;
      return this;
    }

    public Builder landingThresholdElevationComplianceFlag(String landingThresholdElevationComplianceFlag) {
      this.landingThresholdElevationComplianceFlag = landingThresholdElevationComplianceFlag;
      return this;
    }

    public Builder runwayDescription(String runwayDescription) {
      this.runwayDescription = runwayDescription;
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

    public ArincRunway build() {
      return new ArincRunway(this);
    }
  }
}
