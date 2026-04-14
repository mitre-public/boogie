package org.mitre.boogie.xml.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.FreqUnitOfMeasure;
import org.mitre.boogie.xml.model.fields.PrecisionApproachCategory;
import org.mitre.boogie.xml.model.fields.RunwayLeftRightCenterType;
import org.mitre.boogie.xml.model.fields.RunwaySuffix;
import org.mitre.tdp.boogie.MagneticVariation;

public final class ArincGnssLandingSystem implements ArincFixRecord {
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;
  private final ArincPointInfo pointInfo;
  // Navaid fields
  private final Integer elevation;
  private final Boolean isVFRCheckpoint;
  // PrecisionApproachNavaid fields
  private final String runwayRef;
  private final Double approachAngle;
  private final Double approachCourseBearingValue;
  private final Boolean approachCourseBearingIsTrueBearing;
  /**
   * See {@link PrecisionApproachCategory}
   */
  private final String category;
  private final Integer runwayNumber;
  /**
   * See {@link RunwayLeftRightCenterType}
   */
  private final String runwayLeftRightCenterType;
  /**
   * See {@link RunwaySuffix}
   */
  private final String runwaySuffix;
  // Gls fields
  private final Long serviceVolumeRadius;
  private final Integer stationElevationWgs84;
  private final String stationType;
  private final String tdmaSlots;
  private final long glsChannel;
  private final Long thresholdCrossingHeight;
  // Navaid shared
  private final MagneticVariation stationDeclination;
  private final Double frequencyValue;
  /**
   * See {@link FreqUnitOfMeasure}
   */
  private final String freqUnitOfMeasure;

  private ArincGnssLandingSystem(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.pointInfo = builder.pointInfo;
    this.elevation = builder.elevation;
    this.isVFRCheckpoint = builder.isVFRCheckpoint;
    this.runwayRef = builder.runwayRef;
    this.approachAngle = builder.approachAngle;
    this.approachCourseBearingValue = builder.approachCourseBearingValue;
    this.approachCourseBearingIsTrueBearing = builder.approachCourseBearingIsTrueBearing;
    this.category = builder.category;
    this.runwayNumber = builder.runwayNumber;
    this.runwayLeftRightCenterType = builder.runwayLeftRightCenterType;
    this.runwaySuffix = builder.runwaySuffix;
    this.serviceVolumeRadius = builder.serviceVolumeRadius;
    this.stationElevationWgs84 = builder.stationElevationWgs84;
    this.stationType = builder.stationType;
    this.tdmaSlots = builder.tdmaSlots;
    this.glsChannel = builder.glsChannel;
    this.thresholdCrossingHeight = builder.thresholdCrossingHeight;
    this.stationDeclination = builder.stationDeclination;
    this.frequencyValue = builder.frequencyValue;
    this.freqUnitOfMeasure = builder.freqUnitOfMeasure;
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

  public ArincPointInfo pointInfo() {
    return pointInfo;
  }

  public Optional<Integer> elevation() {
    return Optional.ofNullable(elevation);
  }

  public Optional<Boolean> isVFRCheckpoint() {
    return Optional.ofNullable(isVFRCheckpoint);
  }

  public Optional<String> runwayRef() {
    return Optional.ofNullable(runwayRef);
  }

  public Optional<Double> approachAngle() {
    return Optional.ofNullable(approachAngle);
  }

  public Optional<Double> approachCourseBearingValue() {
    return Optional.ofNullable(approachCourseBearingValue);
  }

  public Optional<Boolean> approachCourseBearingIsTrueBearing() {
    return Optional.ofNullable(approachCourseBearingIsTrueBearing);
  }

  public Optional<PrecisionApproachCategory> category() {
    return Optional.ofNullable(category).filter(PrecisionApproachCategory.VALID::contains).map(PrecisionApproachCategory::valueOf);
  }

  public Integer runwayNumber() {
    return runwayNumber;
  }

  public Optional<RunwayLeftRightCenterType> runwayLeftRightCenterType() {
    return Optional.ofNullable(runwayLeftRightCenterType).filter(RunwayLeftRightCenterType.VALID::contains).map(RunwayLeftRightCenterType::valueOf);
  }

  public Optional<RunwaySuffix> runwaySuffix() {
    return Optional.ofNullable(runwaySuffix).filter(RunwaySuffix.VALID::contains).map(RunwaySuffix::valueOf);
  }

  public Optional<Long> serviceVolumeRadius() {
    return Optional.ofNullable(serviceVolumeRadius);
  }

  public Optional<Integer> stationElevationWgs84() {
    return Optional.ofNullable(stationElevationWgs84);
  }

  public Optional<String> stationType() {
    return Optional.ofNullable(stationType);
  }

  public Optional<String> tdmaSlots() {
    return Optional.ofNullable(tdmaSlots);
  }

  public long glsChannel() {
    return glsChannel;
  }

  public Optional<Long> thresholdCrossingHeight() {
    return Optional.ofNullable(thresholdCrossingHeight);
  }

  public Optional<MagneticVariation> stationDeclination() {
    return Optional.ofNullable(stationDeclination);
  }

  public Optional<Double> frequencyValue() {
    return Optional.ofNullable(frequencyValue);
  }

  public Optional<FreqUnitOfMeasure> freqUnitOfMeasure() {
    return Optional.ofNullable(freqUnitOfMeasure).filter(FreqUnitOfMeasure.VALID::contains).map(FreqUnitOfMeasure::valueOf);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    ArincGnssLandingSystem that = (ArincGnssLandingSystem) o;
    return glsChannel == that.glsChannel && Objects.equals(baseInfo, that.baseInfo) && Objects.equals(recordInfo, that.recordInfo) && Objects.equals(pointInfo, that.pointInfo) && Objects.equals(elevation, that.elevation) && Objects.equals(isVFRCheckpoint, that.isVFRCheckpoint) && Objects.equals(runwayRef, that.runwayRef) && Objects.equals(approachAngle, that.approachAngle) && Objects.equals(approachCourseBearingValue, that.approachCourseBearingValue) && Objects.equals(approachCourseBearingIsTrueBearing, that.approachCourseBearingIsTrueBearing) && Objects.equals(category, that.category) && Objects.equals(runwayNumber, that.runwayNumber) && Objects.equals(runwayLeftRightCenterType, that.runwayLeftRightCenterType) && Objects.equals(runwaySuffix, that.runwaySuffix) && Objects.equals(serviceVolumeRadius, that.serviceVolumeRadius) && Objects.equals(stationElevationWgs84, that.stationElevationWgs84) && Objects.equals(stationType, that.stationType) && Objects.equals(tdmaSlots, that.tdmaSlots) && Objects.equals(thresholdCrossingHeight, that.thresholdCrossingHeight) && Objects.equals(stationDeclination, that.stationDeclination) && Objects.equals(frequencyValue, that.frequencyValue) && Objects.equals(freqUnitOfMeasure, that.freqUnitOfMeasure);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, recordInfo, pointInfo, elevation, isVFRCheckpoint, runwayRef, approachAngle, approachCourseBearingValue, approachCourseBearingIsTrueBearing, category, runwayNumber, runwayLeftRightCenterType, runwaySuffix, serviceVolumeRadius, stationElevationWgs84, stationType, tdmaSlots, glsChannel, thresholdCrossingHeight, stationDeclination, frequencyValue, freqUnitOfMeasure);
  }

  @Override
  public String toString() {
    return "ArincGnssLandingSystem{" +
        "baseInfo=" + baseInfo +
        ", recordInfo=" + recordInfo +
        ", pointInfo=" + pointInfo +
        ", elevation=" + elevation +
        ", isVFRCheckpoint=" + isVFRCheckpoint +
        ", runwayRef='" + runwayRef + '\'' +
        ", approachAngle=" + approachAngle +
        ", approachCourseBearingValue=" + approachCourseBearingValue +
        ", approachCourseBearingIsTrueBearing=" + approachCourseBearingIsTrueBearing +
        ", category='" + category + '\'' +
        ", runwayNumber=" + runwayNumber +
        ", runwayLeftRightCenterType='" + runwayLeftRightCenterType + '\'' +
        ", runwaySuffix='" + runwaySuffix + '\'' +
        ", serviceVolumeRadius=" + serviceVolumeRadius +
        ", stationElevationWgs84=" + stationElevationWgs84 +
        ", stationType='" + stationType + '\'' +
        ", tdmaSlots='" + tdmaSlots + '\'' +
        ", glsChannel=" + glsChannel +
        ", thresholdCrossingHeight=" + thresholdCrossingHeight +
        ", stationDeclination=" + stationDeclination +
        ", frequencyValue=" + frequencyValue +
        ", freqUnitOfMeasure='" + freqUnitOfMeasure + '\'' +
        '}';
  }

  public static final class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private ArincPointInfo pointInfo;
    private Integer elevation;
    private Boolean isVFRCheckpoint;
    private String runwayRef;
    private Double approachAngle;
    private Double approachCourseBearingValue;
    private Boolean approachCourseBearingIsTrueBearing;
    private String category;
    private Integer runwayNumber;
    private String runwayLeftRightCenterType;
    private String runwaySuffix;
    private Long serviceVolumeRadius;
    private Integer stationElevationWgs84;
    private String stationType;
    private String tdmaSlots;
    private long glsChannel;
    private Long thresholdCrossingHeight;
    private MagneticVariation stationDeclination;
    private Double frequencyValue;
    private String freqUnitOfMeasure;

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

    public Builder pointInfo(ArincPointInfo pointInfo) {
      this.pointInfo = pointInfo;
      return this;
    }

    public Builder elevation(Integer elevation) {
      this.elevation = elevation;
      return this;
    }

    public Builder isVFRCheckpoint(Boolean isVFRCheckpoint) {
      this.isVFRCheckpoint = isVFRCheckpoint;
      return this;
    }

    public Builder runwayRef(String runwayRef) {
      this.runwayRef = runwayRef;
      return this;
    }

    public Builder approachAngle(Double approachAngle) {
      this.approachAngle = approachAngle;
      return this;
    }

    public Builder approachCourseBearingValue(Double approachCourseBearingValue) {
      this.approachCourseBearingValue = approachCourseBearingValue;
      return this;
    }

    public Builder approachCourseBearingIsTrueBearing(Boolean approachCourseBearingIsTrueBearing) {
      this.approachCourseBearingIsTrueBearing = approachCourseBearingIsTrueBearing;
      return this;
    }

    public Builder category(String category) {
      this.category = category;
      return this;
    }

    public Builder runwayNumber(Integer runwayNumber) {
      this.runwayNumber = runwayNumber;
      return this;
    }

    public Builder runwayLeftRightCenterType(String runwayLeftRightCenterType) {
      this.runwayLeftRightCenterType = runwayLeftRightCenterType;
      return this;
    }

    public Builder runwaySuffix(String runwaySuffix) {
      this.runwaySuffix = runwaySuffix;
      return this;
    }

    public Builder serviceVolumeRadius(Long serviceVolumeRadius) {
      this.serviceVolumeRadius = serviceVolumeRadius;
      return this;
    }

    public Builder stationElevationWgs84(Integer stationElevationWgs84) {
      this.stationElevationWgs84 = stationElevationWgs84;
      return this;
    }

    public Builder stationType(String stationType) {
      this.stationType = stationType;
      return this;
    }

    public Builder tdmaSlots(String tdmaSlots) {
      this.tdmaSlots = tdmaSlots;
      return this;
    }

    public Builder glsChannel(long glsChannel) {
      this.glsChannel = glsChannel;
      return this;
    }

    public Builder thresholdCrossingHeight(Long thresholdCrossingHeight) {
      this.thresholdCrossingHeight = thresholdCrossingHeight;
      return this;
    }

    public Builder stationDeclination(MagneticVariation stationDeclination) {
      this.stationDeclination = stationDeclination;
      return this;
    }

    public Builder frequencyValue(Double frequencyValue) {
      this.frequencyValue = frequencyValue;
      return this;
    }

    public Builder freqUnitOfMeasure(String freqUnitOfMeasure) {
      this.freqUnitOfMeasure = freqUnitOfMeasure;
      return this;
    }

    public ArincGnssLandingSystem build() {
      return new ArincGnssLandingSystem(this);
    }
  }
}
