package org.mitre.boogie.xml.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ElevationType;
import org.mitre.boogie.xml.model.fields.HelicopterPerformanceReq;
import org.mitre.boogie.xml.model.fields.LandingThresholdElevationAccuracyCompliance;
import org.mitre.boogie.xml.model.fields.RunwayAccuracyCompliance;
import org.mitre.boogie.xml.model.fields.RunwayLeftRightCenterType;
import org.mitre.boogie.xml.model.fields.RunwaySuffix;
import org.mitre.boogie.xml.model.fields.RunwaySurfaceCode;
import org.mitre.boogie.xml.model.fields.RunwayUsageIndicator;
import org.mitre.boogie.xml.model.fields.SurfaceType;
import org.mitre.boogie.xml.model.fields.TchValueIndicator;
import org.mitre.boogie.xml.model.fields.TrueBearingSource;
import org.mitre.caasd.commons.LatLong;

public final class ArincRunway implements ArincFixRecord {
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;
  private final ArincPointInfo pointInfo;
  private final Boolean isWithoutLocation;
  private final Boolean isDerivedLocation;
  private final Long displacedThresholdDistance;
  private final Integer landingThresholdElevation;
  /**
   * See {@link ElevationType}
   */
  private final String landingThresholdElevationType;
  private final LatLong runwayEndLocation;
  private final Integer runwayEndElevation;
  private final Double ltpEllipsoidHeight;
  private final Double runwayBearing;
  private final Boolean runwayBearingIsTrueBearing;
  private final Double runwayTrueBearing;
  /**
   * See {@link TrueBearingSource}
   */
  private final String runwayTrueBearingSource;
  private final String runwayDescription;
  private final Double runwayGradient;
  private final Integer runwayNumber;
  /**
   * See {@link RunwayLeftRightCenterType}
   */
  private final String runwayLeftRightCenterType;
  /**
   * See {@link RunwaySuffix}
   */
  private final String runwaySuffix;
  private final Long runwayLength;
  private final Long runwayWidth;
  private final Long stopway;
  /**
   * See {@link TchValueIndicator}
   */
  private final String tchValueIndicator;
  private final Long thresholdCrossingHeight;
  private final Integer touchDownZoneElevation;
  private final Integer starterExtension;
  /**
   * See {@link RunwaySurfaceCode}
   */
  private final String surfaceCode;
  /**
   * See {@link SurfaceType}
   */
  private final String surfaceType;
  /**
   * See {@link HelicopterPerformanceReq}
   */
  private final String helicopterPerformanceReq;
  private final Long takeOffRunwayAvailable;
  private final Long takeOffDistanceAvailable;
  private final Long accelerateStopDistanceAvailable;
  private final Long landingDistanceAvailable;
  /**
   * See {@link RunwayUsageIndicator}
   */
  private final String runwayUsageIndicator;
  /**
   * See {@link RunwayAccuracyCompliance}
   */
  private final String runwayAccuracyCompliance;
  /**
   * See {@link LandingThresholdElevationAccuracyCompliance}
   */
  private final String landingThresholdElevationAccuracyCompliance;

  private ArincRunway(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.pointInfo = builder.pointInfo;
    this.isWithoutLocation = builder.isWithoutLocation;
    this.isDerivedLocation = builder.isDerivedLocation;
    this.displacedThresholdDistance = builder.displacedThresholdDistance;
    this.landingThresholdElevation = builder.landingThresholdElevation;
    this.landingThresholdElevationType = builder.landingThresholdElevationType;
    this.runwayEndLocation = builder.runwayEndLocation;
    this.runwayEndElevation = builder.runwayEndElevation;
    this.ltpEllipsoidHeight = builder.ltpEllipsoidHeight;
    this.runwayBearing = builder.runwayBearing;
    this.runwayBearingIsTrueBearing = builder.runwayBearingIsTrueBearing;
    this.runwayTrueBearing = builder.runwayTrueBearing;
    this.runwayTrueBearingSource = builder.runwayTrueBearingSource;
    this.runwayDescription = builder.runwayDescription;
    this.runwayGradient = builder.runwayGradient;
    this.runwayNumber = builder.runwayNumber;
    this.runwayLeftRightCenterType = builder.runwayLeftRightCenterType;
    this.runwaySuffix = builder.runwaySuffix;
    this.runwayLength = builder.runwayLength;
    this.runwayWidth = builder.runwayWidth;
    this.stopway = builder.stopway;
    this.tchValueIndicator = builder.tchValueIndicator;
    this.thresholdCrossingHeight = builder.thresholdCrossingHeight;
    this.touchDownZoneElevation = builder.touchDownZoneElevation;
    this.starterExtension = builder.starterExtension;
    this.surfaceCode = builder.surfaceCode;
    this.surfaceType = builder.surfaceType;
    this.helicopterPerformanceReq = builder.helicopterPerformanceReq;
    this.takeOffRunwayAvailable = builder.takeOffRunwayAvailable;
    this.takeOffDistanceAvailable = builder.takeOffDistanceAvailable;
    this.accelerateStopDistanceAvailable = builder.accelerateStopDistanceAvailable;
    this.landingDistanceAvailable = builder.landingDistanceAvailable;
    this.runwayUsageIndicator = builder.runwayUsageIndicator;
    this.runwayAccuracyCompliance = builder.runwayAccuracyCompliance;
    this.landingThresholdElevationAccuracyCompliance = builder.landingThresholdElevationAccuracyCompliance;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .isWithoutLocation(isWithoutLocation)
        .isDerivedLocation(isDerivedLocation)
        .displacedThresholdDistance(displacedThresholdDistance)
        .landingThresholdElevation(landingThresholdElevation)
        .landingThresholdElevationType(landingThresholdElevationType)
        .runwayEndLocation(runwayEndLocation)
        .runwayEndElevation(runwayEndElevation)
        .ltpEllipsoidHeight(ltpEllipsoidHeight)
        .runwayBearing(runwayBearing)
        .runwayBearingIsTrueBearing(runwayBearingIsTrueBearing)
        .runwayTrueBearing(runwayTrueBearing)
        .runwayTrueBearingSource(runwayTrueBearingSource)
        .runwayDescription(runwayDescription)
        .runwayGradient(runwayGradient)
        .runwayNumber(runwayNumber)
        .runwayLeftRightCenterType(runwayLeftRightCenterType)
        .runwaySuffix(runwaySuffix)
        .runwayLength(runwayLength)
        .runwayWidth(runwayWidth)
        .stopway(stopway)
        .tchValueIndicator(tchValueIndicator)
        .thresholdCrossingHeight(thresholdCrossingHeight)
        .touchDownZoneElevation(touchDownZoneElevation)
        .starterExtension(starterExtension)
        .surfaceCode(surfaceCode)
        .surfaceType(surfaceType)
        .helicopterPerformanceReq(helicopterPerformanceReq)
        .takeOffRunwayAvailable(takeOffRunwayAvailable)
        .takeOffDistanceAvailable(takeOffDistanceAvailable)
        .accelerateStopDistanceAvailable(accelerateStopDistanceAvailable)
        .landingDistanceAvailable(landingDistanceAvailable)
        .runwayUsageIndicator(runwayUsageIndicator)
        .runwayAccuracyCompliance(runwayAccuracyCompliance)
        .landingThresholdElevationAccuracyCompliance(landingThresholdElevationAccuracyCompliance);
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

  public Optional<Boolean> isWithoutLocation() {
    return Optional.ofNullable(isWithoutLocation);
  }

  public Optional<Boolean> isDerivedLocation() {
    return Optional.ofNullable(isDerivedLocation);
  }

  public Optional<Long> displacedThresholdDistance() {
    return Optional.ofNullable(displacedThresholdDistance);
  }

  public Optional<Integer> landingThresholdElevation() {
    return Optional.ofNullable(landingThresholdElevation);
  }

  public Optional<ElevationType> landingThresholdElevationType() {
    return Optional.ofNullable(landingThresholdElevationType).filter(ElevationType.VALID::contains).map(ElevationType::valueOf);
  }

  public Optional<LatLong> runwayEndLocation() {
    return Optional.ofNullable(runwayEndLocation);
  }

  public Optional<Integer> runwayEndElevation() {
    return Optional.ofNullable(runwayEndElevation);
  }

  public Optional<Double> ltpEllipsoidHeight() {
    return Optional.ofNullable(ltpEllipsoidHeight);
  }

  public Optional<Double> runwayBearing() {
    return Optional.ofNullable(runwayBearing);
  }

  public Optional<Boolean> runwayBearingIsTrueBearing() {
    return Optional.ofNullable(runwayBearingIsTrueBearing);
  }

  public Optional<Double> runwayTrueBearing() {
    return Optional.ofNullable(runwayTrueBearing);
  }

  public Optional<TrueBearingSource> runwayTrueBearingSource() {
    return Optional.ofNullable(runwayTrueBearingSource).filter(TrueBearingSource.VALID::contains).map(TrueBearingSource::valueOf);
  }

  public Optional<String> runwayDescription() {
    return Optional.ofNullable(runwayDescription);
  }

  public Optional<Double> runwayGradient() {
    return Optional.ofNullable(runwayGradient);
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

  public Optional<Long> runwayLength() {
    return Optional.ofNullable(runwayLength);
  }

  public Optional<Long> runwayWidth() {
    return Optional.ofNullable(runwayWidth);
  }

  public Optional<Long> stopway() {
    return Optional.ofNullable(stopway);
  }

  public Optional<TchValueIndicator> tchValueIndicator() {
    return Optional.ofNullable(tchValueIndicator).filter(TchValueIndicator.VALID::contains).map(TchValueIndicator::valueOf);
  }

  public Optional<Long> thresholdCrossingHeight() {
    return Optional.ofNullable(thresholdCrossingHeight);
  }

  public Optional<Integer> touchDownZoneElevation() {
    return Optional.ofNullable(touchDownZoneElevation);
  }

  public Optional<Integer> starterExtension() {
    return Optional.ofNullable(starterExtension);
  }

  public Optional<RunwaySurfaceCode> surfaceCode() {
    return Optional.ofNullable(surfaceCode)
        .filter(RunwaySurfaceCode.VALID::contains)
        .map(RunwaySurfaceCode::valueOf);
  }

  public Optional<SurfaceType> surfaceType() {
    return Optional.ofNullable(surfaceType).filter(SurfaceType.VALID::contains).map(SurfaceType::valueOf);
  }

  public Optional<HelicopterPerformanceReq> helicopterPerformanceReq() {
    return Optional.ofNullable(helicopterPerformanceReq).filter(HelicopterPerformanceReq.VALID::contains).map(HelicopterPerformanceReq::valueOf);
  }

  public Optional<Long> takeOffRunwayAvailable() {
    return Optional.ofNullable(takeOffRunwayAvailable);
  }

  public Optional<Long> takeOffDistanceAvailable() {
    return Optional.ofNullable(takeOffDistanceAvailable);
  }

  public Optional<Long> accelerateStopDistanceAvailable() {
    return Optional.ofNullable(accelerateStopDistanceAvailable);
  }

  public Optional<Long> landingDistanceAvailable() {
    return Optional.ofNullable(landingDistanceAvailable);
  }

  public Optional<RunwayUsageIndicator> runwayUsageIndicator() {
    return Optional.ofNullable(runwayUsageIndicator).filter(RunwayUsageIndicator.VALID::contains).map(RunwayUsageIndicator::valueOf);
  }

  public Optional<RunwayAccuracyCompliance> runwayAccuracyCompliance() {
    return Optional.ofNullable(runwayAccuracyCompliance).filter(RunwayAccuracyCompliance.VALID::contains).map(RunwayAccuracyCompliance::valueOf);
  }

  public Optional<LandingThresholdElevationAccuracyCompliance> landingThresholdElevationAccuracyCompliance() {
    return Optional.ofNullable(landingThresholdElevationAccuracyCompliance).filter(LandingThresholdElevationAccuracyCompliance.VALID::contains).map(LandingThresholdElevationAccuracyCompliance::valueOf);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincRunway that = (ArincRunway) o;
    return Objects.equals(baseInfo, that.baseInfo) && Objects.equals(recordInfo, that.recordInfo) && Objects.equals(pointInfo, that.pointInfo) && Objects.equals(isWithoutLocation, that.isWithoutLocation) && Objects.equals(isDerivedLocation, that.isDerivedLocation) && Objects.equals(displacedThresholdDistance, that.displacedThresholdDistance) && Objects.equals(landingThresholdElevation, that.landingThresholdElevation) && Objects.equals(landingThresholdElevationType, that.landingThresholdElevationType) && Objects.equals(runwayEndLocation, that.runwayEndLocation) && Objects.equals(runwayEndElevation, that.runwayEndElevation) && Objects.equals(ltpEllipsoidHeight, that.ltpEllipsoidHeight) && Objects.equals(runwayBearing, that.runwayBearing) && Objects.equals(runwayBearingIsTrueBearing, that.runwayBearingIsTrueBearing) && Objects.equals(runwayTrueBearing, that.runwayTrueBearing) && Objects.equals(runwayTrueBearingSource, that.runwayTrueBearingSource) && Objects.equals(runwayDescription, that.runwayDescription) && Objects.equals(runwayGradient, that.runwayGradient) && Objects.equals(runwayNumber, that.runwayNumber) && Objects.equals(runwayLeftRightCenterType, that.runwayLeftRightCenterType) && Objects.equals(runwaySuffix, that.runwaySuffix) && Objects.equals(runwayLength, that.runwayLength) && Objects.equals(runwayWidth, that.runwayWidth) && Objects.equals(stopway, that.stopway) && Objects.equals(tchValueIndicator, that.tchValueIndicator) && Objects.equals(thresholdCrossingHeight, that.thresholdCrossingHeight) && Objects.equals(touchDownZoneElevation, that.touchDownZoneElevation) && Objects.equals(starterExtension, that.starterExtension) && Objects.equals(surfaceCode, that.surfaceCode) && Objects.equals(surfaceType, that.surfaceType) && Objects.equals(helicopterPerformanceReq, that.helicopterPerformanceReq) && Objects.equals(takeOffRunwayAvailable, that.takeOffRunwayAvailable) && Objects.equals(takeOffDistanceAvailable, that.takeOffDistanceAvailable) && Objects.equals(accelerateStopDistanceAvailable, that.accelerateStopDistanceAvailable) && Objects.equals(landingDistanceAvailable, that.landingDistanceAvailable) && Objects.equals(runwayUsageIndicator, that.runwayUsageIndicator) && Objects.equals(runwayAccuracyCompliance, that.runwayAccuracyCompliance) && Objects.equals(landingThresholdElevationAccuracyCompliance, that.landingThresholdElevationAccuracyCompliance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, recordInfo, pointInfo, isWithoutLocation, isDerivedLocation, displacedThresholdDistance, landingThresholdElevation, landingThresholdElevationType, runwayEndLocation, runwayEndElevation, ltpEllipsoidHeight, runwayBearing, runwayBearingIsTrueBearing, runwayTrueBearing, runwayTrueBearingSource, runwayDescription, runwayGradient, runwayNumber, runwayLeftRightCenterType, runwaySuffix, runwayLength, runwayWidth, stopway, tchValueIndicator, thresholdCrossingHeight, touchDownZoneElevation, starterExtension, surfaceCode, surfaceType, helicopterPerformanceReq, takeOffRunwayAvailable, takeOffDistanceAvailable, accelerateStopDistanceAvailable, landingDistanceAvailable, runwayUsageIndicator, runwayAccuracyCompliance, landingThresholdElevationAccuracyCompliance);
  }

  @Override
  public String toString() {
    return "ArincRunway{" +
        "baseInfo=" + baseInfo +
        ", recordInfo=" + recordInfo +
        ", pointInfo=" + pointInfo +
        ", isWithoutLocation=" + isWithoutLocation +
        ", isDerivedLocation=" + isDerivedLocation +
        ", displacedThresholdDistance=" + displacedThresholdDistance +
        ", landingThresholdElevation=" + landingThresholdElevation +
        ", landingThresholdElevationType='" + landingThresholdElevationType + '\'' +
        ", runwayEndLocation=" + runwayEndLocation +
        ", runwayEndElevation=" + runwayEndElevation +
        ", ltpEllipsoidHeight=" + ltpEllipsoidHeight +
        ", runwayBearing=" + runwayBearing +
        ", runwayBearingIsTrueBearing=" + runwayBearingIsTrueBearing +
        ", runwayTrueBearing=" + runwayTrueBearing +
        ", runwayTrueBearingSource='" + runwayTrueBearingSource + '\'' +
        ", runwayDescription='" + runwayDescription + '\'' +
        ", runwayGradient=" + runwayGradient +
        ", runwayNumber=" + runwayNumber +
        ", runwayLeftRightCenterType='" + runwayLeftRightCenterType + '\'' +
        ", runwaySuffix='" + runwaySuffix + '\'' +
        ", runwayLength=" + runwayLength +
        ", runwayWidth=" + runwayWidth +
        ", stopway=" + stopway +
        ", tchValueIndicator='" + tchValueIndicator + '\'' +
        ", thresholdCrossingHeight=" + thresholdCrossingHeight +
        ", touchDownZoneElevation=" + touchDownZoneElevation +
        ", starterExtension=" + starterExtension +
        ", surfaceCode='" + surfaceCode + '\'' +
        ", surfaceType='" + surfaceType + '\'' +
        ", helicopterPerformanceReq='" + helicopterPerformanceReq + '\'' +
        ", takeOffRunwayAvailable=" + takeOffRunwayAvailable +
        ", takeOffDistanceAvailable=" + takeOffDistanceAvailable +
        ", accelerateStopDistanceAvailable=" + accelerateStopDistanceAvailable +
        ", landingDistanceAvailable=" + landingDistanceAvailable +
        ", runwayUsageIndicator='" + runwayUsageIndicator + '\'' +
        ", runwayAccuracyCompliance='" + runwayAccuracyCompliance + '\'' +
        ", landingThresholdElevationAccuracyCompliance='" + landingThresholdElevationAccuracyCompliance + '\'' +
        '}';
  }

  public static class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private ArincPointInfo pointInfo;
    private Boolean isWithoutLocation;
    private Boolean isDerivedLocation;
    private Long displacedThresholdDistance;
    private Integer landingThresholdElevation;
    private String landingThresholdElevationType;
    private LatLong runwayEndLocation;
    private Integer runwayEndElevation;
    private Double ltpEllipsoidHeight;
    private Double runwayBearing;
    private Boolean runwayBearingIsTrueBearing;
    private Double runwayTrueBearing;
    private String runwayTrueBearingSource;
    private String runwayDescription;
    private Double runwayGradient;
    private Integer runwayNumber;
    private String runwayLeftRightCenterType;
    private String runwaySuffix;
    private Long runwayLength;
    private Long runwayWidth;
    private Long stopway;
    private String tchValueIndicator;
    private Long thresholdCrossingHeight;
    private Integer touchDownZoneElevation;
    private Integer starterExtension;
    private String surfaceCode;
    private String surfaceType;
    private String helicopterPerformanceReq;
    private Long takeOffRunwayAvailable;
    private Long takeOffDistanceAvailable;
    private Long accelerateStopDistanceAvailable;
    private Long landingDistanceAvailable;
    private String runwayUsageIndicator;
    private String runwayAccuracyCompliance;
    private String landingThresholdElevationAccuracyCompliance;

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

    public Builder isWithoutLocation(Boolean isWithoutLocation) {
      this.isWithoutLocation = isWithoutLocation;
      return this;
    }

    public Builder isDerivedLocation(Boolean isDerivedLocation) {
      this.isDerivedLocation = isDerivedLocation;
      return this;
    }

    public Builder displacedThresholdDistance(Long displacedThresholdDistance) {
      this.displacedThresholdDistance = displacedThresholdDistance;
      return this;
    }

    public Builder landingThresholdElevation(Integer landingThresholdElevation) {
      this.landingThresholdElevation = landingThresholdElevation;
      return this;
    }

    public Builder landingThresholdElevationType(String landingThresholdElevationType) {
      this.landingThresholdElevationType = landingThresholdElevationType;
      return this;
    }

    public Builder runwayEndLocation(LatLong runwayEndLocation) {
      this.runwayEndLocation = runwayEndLocation;
      return this;
    }

    public Builder runwayEndElevation(Integer runwayEndElevation) {
      this.runwayEndElevation = runwayEndElevation;
      return this;
    }

    public Builder ltpEllipsoidHeight(Double ltpEllipsoidHeight) {
      this.ltpEllipsoidHeight = ltpEllipsoidHeight;
      return this;
    }

    public Builder runwayBearing(Double runwayBearing) {
      this.runwayBearing = runwayBearing;
      return this;
    }

    public Builder runwayBearingIsTrueBearing(Boolean runwayBearingIsTrueBearing) {
      this.runwayBearingIsTrueBearing = runwayBearingIsTrueBearing;
      return this;
    }

    public Builder runwayTrueBearing(Double runwayTrueBearing) {
      this.runwayTrueBearing = runwayTrueBearing;
      return this;
    }

    public Builder runwayTrueBearingSource(String runwayTrueBearingSource) {
      this.runwayTrueBearingSource = runwayTrueBearingSource;
      return this;
    }

    public Builder runwayDescription(String runwayDescription) {
      this.runwayDescription = runwayDescription;
      return this;
    }

    public Builder runwayGradient(Double runwayGradient) {
      this.runwayGradient = runwayGradient;
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

    public Builder runwayLength(Long runwayLength) {
      this.runwayLength = runwayLength;
      return this;
    }

    public Builder runwayWidth(Long runwayWidth) {
      this.runwayWidth = runwayWidth;
      return this;
    }

    public Builder stopway(Long stopway) {
      this.stopway = stopway;
      return this;
    }

    public Builder tchValueIndicator(String tchValueIndicator) {
      this.tchValueIndicator = tchValueIndicator;
      return this;
    }

    public Builder thresholdCrossingHeight(Long thresholdCrossingHeight) {
      this.thresholdCrossingHeight = thresholdCrossingHeight;
      return this;
    }

    public Builder touchDownZoneElevation(Integer touchDownZoneElevation) {
      this.touchDownZoneElevation = touchDownZoneElevation;
      return this;
    }

    public Builder starterExtension(Integer starterExtension) {
      this.starterExtension = starterExtension;
      return this;
    }

    public Builder surfaceCode(String surfaceCode) {
      this.surfaceCode = surfaceCode;
      return this;
    }

    public Builder surfaceType(String surfaceType) {
      this.surfaceType = surfaceType;
      return this;
    }

    public Builder helicopterPerformanceReq(String helicopterPerformanceReq) {
      this.helicopterPerformanceReq = helicopterPerformanceReq;
      return this;
    }

    public Builder takeOffRunwayAvailable(Long takeOffRunwayAvailable) {
      this.takeOffRunwayAvailable = takeOffRunwayAvailable;
      return this;
    }

    public Builder takeOffDistanceAvailable(Long takeOffDistanceAvailable) {
      this.takeOffDistanceAvailable = takeOffDistanceAvailable;
      return this;
    }

    public Builder accelerateStopDistanceAvailable(Long accelerateStopDistanceAvailable) {
      this.accelerateStopDistanceAvailable = accelerateStopDistanceAvailable;
      return this;
    }

    public Builder landingDistanceAvailable(Long landingDistanceAvailable) {
      this.landingDistanceAvailable = landingDistanceAvailable;
      return this;
    }

    public Builder runwayUsageIndicator(String runwayUsageIndicator) {
      this.runwayUsageIndicator = runwayUsageIndicator;
      return this;
    }

    public Builder runwayAccuracyCompliance(String runwayAccuracyCompliance) {
      this.runwayAccuracyCompliance = runwayAccuracyCompliance;
      return this;
    }

    public Builder landingThresholdElevationAccuracyCompliance(String landingThresholdElevationAccuracyCompliance) {
      this.landingThresholdElevationAccuracyCompliance = landingThresholdElevationAccuracyCompliance;
      return this;
    }

    public ArincRunway build() {
      return new ArincRunway(this);
    }
  }
}
