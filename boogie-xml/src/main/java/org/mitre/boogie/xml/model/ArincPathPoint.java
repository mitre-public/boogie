package org.mitre.boogie.xml.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincHighPrecisionLocation;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;

/**
 * Version-neutral representation of an ARINC 424 GBAS or SBAS path point.
 *
 * <p>This class flattens the XML class hierarchy ({@code PathPoint -> GbasPathPoint/SbasPathPoint})
 * into one model. SBAS-specific fields are empty for a GBAS path point. Committee-controlled
 * values are retained as raw strings so a newer schema value is not discarded by an older model.
 */
public final class ArincPathPoint {

  public enum Type {
    GBAS,
    SBAS
  }

  // A424Record fields
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;

  // Concrete XML path point type
  private final Type pathPointType;

  // PathPoint fields
  private final String runwayNumber;
  private final String approachPerformanceDesignator;
  private final String approachRouteIdentifier;
  private final String approachTypeIdentifier;
  private final BigDecimal courseWidthAtThreshold;
  private final String fasDataCrcRemainder;
  private final ArincHighPrecisionLocation flightPathAlignmentPoint;
  private final BigDecimal fpapEllipsoidHeight;
  private final BigDecimal fpapOrthometricHeight;
  private final BigDecimal glidePathAngle;
  private final Long gnssChannelNumber;
  private final BigDecimal helicopterProcedureCourse;
  private final ArincHighPrecisionLocation landingThresholdPoint;
  private final Long lengthOffset;
  private final BigDecimal ltpEllipsoidHeight;
  private final BigDecimal ltpOrthometricHeight;
  private final BigDecimal pathPointTch;
  private final long referencePathDataSelector;
  private final String referencePathIdentifier;
  private final String routeIndicator;
  private final String tchUnitsIndicator;

  // GbasPathPoint / SbasPathPoint shared field (with type-specific code sets)
  private final String operationType;

  // SbasPathPoint fields
  private final BigDecimal horizontalAlertLimit;
  private final BigDecimal verticalAlertLimit;
  private final String sbasServiceProviderIdentifier;
  private final BigDecimal finalApproachCourseValue;
  private final Boolean finalApproachCourseIsTrue;

  private ArincPathPoint(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.pathPointType = builder.pathPointType;
    this.runwayNumber = builder.runwayNumber;
    this.approachPerformanceDesignator = builder.approachPerformanceDesignator;
    this.approachRouteIdentifier = builder.approachRouteIdentifier;
    this.approachTypeIdentifier = builder.approachTypeIdentifier;
    this.courseWidthAtThreshold = builder.courseWidthAtThreshold;
    this.fasDataCrcRemainder = builder.fasDataCrcRemainder;
    this.flightPathAlignmentPoint = builder.flightPathAlignmentPoint;
    this.fpapEllipsoidHeight = builder.fpapEllipsoidHeight;
    this.fpapOrthometricHeight = builder.fpapOrthometricHeight;
    this.glidePathAngle = builder.glidePathAngle;
    this.gnssChannelNumber = builder.gnssChannelNumber;
    this.helicopterProcedureCourse = builder.helicopterProcedureCourse;
    this.landingThresholdPoint = builder.landingThresholdPoint;
    this.lengthOffset = builder.lengthOffset;
    this.ltpEllipsoidHeight = builder.ltpEllipsoidHeight;
    this.ltpOrthometricHeight = builder.ltpOrthometricHeight;
    this.pathPointTch = builder.pathPointTch;
    this.referencePathDataSelector = builder.referencePathDataSelector;
    this.referencePathIdentifier = builder.referencePathIdentifier;
    this.routeIndicator = builder.routeIndicator;
    this.tchUnitsIndicator = builder.tchUnitsIndicator;
    this.operationType = builder.operationType;
    this.horizontalAlertLimit = builder.horizontalAlertLimit;
    this.verticalAlertLimit = builder.verticalAlertLimit;
    this.sbasServiceProviderIdentifier = builder.sbasServiceProviderIdentifier;
    this.finalApproachCourseValue = builder.finalApproachCourseValue;
    this.finalApproachCourseIsTrue = builder.finalApproachCourseIsTrue;
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

  public Type pathPointType() {
    return pathPointType;
  }

  public String runwayNumber() {
    return runwayNumber;
  }

  public String approachPerformanceDesignator() {
    return approachPerformanceDesignator;
  }

  public Optional<String> approachRouteIdentifier() {
    return Optional.ofNullable(approachRouteIdentifier);
  }

  public Optional<String> approachTypeIdentifier() {
    return Optional.ofNullable(approachTypeIdentifier);
  }

  public BigDecimal courseWidthAtThreshold() {
    return courseWidthAtThreshold;
  }

  public String fasDataCrcRemainder() {
    return fasDataCrcRemainder;
  }

  public ArincHighPrecisionLocation flightPathAlignmentPoint() {
    return flightPathAlignmentPoint;
  }

  public Optional<BigDecimal> fpapEllipsoidHeight() {
    return Optional.ofNullable(fpapEllipsoidHeight);
  }

  public Optional<BigDecimal> fpapOrthometricHeight() {
    return Optional.ofNullable(fpapOrthometricHeight);
  }

  public BigDecimal glidePathAngle() {
    return glidePathAngle;
  }

  public Optional<Long> gnssChannelNumber() {
    return Optional.ofNullable(gnssChannelNumber);
  }

  public Optional<BigDecimal> helicopterProcedureCourse() {
    return Optional.ofNullable(helicopterProcedureCourse);
  }

  public ArincHighPrecisionLocation landingThresholdPoint() {
    return landingThresholdPoint;
  }

  public Optional<Long> lengthOffset() {
    return Optional.ofNullable(lengthOffset);
  }

  public BigDecimal ltpEllipsoidHeight() {
    return ltpEllipsoidHeight;
  }

  public Optional<BigDecimal> ltpOrthometricHeight() {
    return Optional.ofNullable(ltpOrthometricHeight);
  }

  public BigDecimal pathPointTch() {
    return pathPointTch;
  }

  public long referencePathDataSelector() {
    return referencePathDataSelector;
  }

  public String referencePathIdentifier() {
    return referencePathIdentifier;
  }

  public Optional<String> routeIndicator() {
    return Optional.ofNullable(routeIndicator);
  }

  public String tchUnitsIndicator() {
    return tchUnitsIndicator;
  }

  public String operationType() {
    return operationType;
  }

  public Optional<BigDecimal> horizontalAlertLimit() {
    return Optional.ofNullable(horizontalAlertLimit);
  }

  public Optional<BigDecimal> verticalAlertLimit() {
    return Optional.ofNullable(verticalAlertLimit);
  }

  public Optional<String> sbasServiceProviderIdentifier() {
    return Optional.ofNullable(sbasServiceProviderIdentifier);
  }

  public Optional<BigDecimal> finalApproachCourseValue() {
    return Optional.ofNullable(finalApproachCourseValue);
  }

  public Optional<Boolean> finalApproachCourseIsTrue() {
    return Optional.ofNullable(finalApproachCourseIsTrue);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincPathPoint that = (ArincPathPoint) o;
    return referencePathDataSelector == that.referencePathDataSelector
        && Objects.equals(baseInfo, that.baseInfo)
        && Objects.equals(recordInfo, that.recordInfo)
        && pathPointType == that.pathPointType
        && Objects.equals(runwayNumber, that.runwayNumber)
        && Objects.equals(approachPerformanceDesignator, that.approachPerformanceDesignator)
        && Objects.equals(approachRouteIdentifier, that.approachRouteIdentifier)
        && Objects.equals(approachTypeIdentifier, that.approachTypeIdentifier)
        && Objects.equals(courseWidthAtThreshold, that.courseWidthAtThreshold)
        && Objects.equals(fasDataCrcRemainder, that.fasDataCrcRemainder)
        && Objects.equals(flightPathAlignmentPoint, that.flightPathAlignmentPoint)
        && Objects.equals(fpapEllipsoidHeight, that.fpapEllipsoidHeight)
        && Objects.equals(fpapOrthometricHeight, that.fpapOrthometricHeight)
        && Objects.equals(glidePathAngle, that.glidePathAngle)
        && Objects.equals(gnssChannelNumber, that.gnssChannelNumber)
        && Objects.equals(helicopterProcedureCourse, that.helicopterProcedureCourse)
        && Objects.equals(landingThresholdPoint, that.landingThresholdPoint)
        && Objects.equals(lengthOffset, that.lengthOffset)
        && Objects.equals(ltpEllipsoidHeight, that.ltpEllipsoidHeight)
        && Objects.equals(ltpOrthometricHeight, that.ltpOrthometricHeight)
        && Objects.equals(pathPointTch, that.pathPointTch)
        && Objects.equals(referencePathIdentifier, that.referencePathIdentifier)
        && Objects.equals(routeIndicator, that.routeIndicator)
        && Objects.equals(tchUnitsIndicator, that.tchUnitsIndicator)
        && Objects.equals(operationType, that.operationType)
        && Objects.equals(horizontalAlertLimit, that.horizontalAlertLimit)
        && Objects.equals(verticalAlertLimit, that.verticalAlertLimit)
        && Objects.equals(sbasServiceProviderIdentifier, that.sbasServiceProviderIdentifier)
        && Objects.equals(finalApproachCourseValue, that.finalApproachCourseValue)
        && Objects.equals(finalApproachCourseIsTrue, that.finalApproachCourseIsTrue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        baseInfo,
        recordInfo,
        pathPointType,
        runwayNumber,
        approachPerformanceDesignator,
        approachRouteIdentifier,
        approachTypeIdentifier,
        courseWidthAtThreshold,
        fasDataCrcRemainder,
        flightPathAlignmentPoint,
        fpapEllipsoidHeight,
        fpapOrthometricHeight,
        glidePathAngle,
        gnssChannelNumber,
        helicopterProcedureCourse,
        landingThresholdPoint,
        lengthOffset,
        ltpEllipsoidHeight,
        ltpOrthometricHeight,
        pathPointTch,
        referencePathDataSelector,
        referencePathIdentifier,
        routeIndicator,
        tchUnitsIndicator,
        operationType,
        horizontalAlertLimit,
        verticalAlertLimit,
        sbasServiceProviderIdentifier,
        finalApproachCourseValue,
        finalApproachCourseIsTrue);
  }

  @Override
  public String toString() {
    return "ArincPathPoint{" +
        "baseInfo=" + baseInfo +
        ", recordInfo=" + recordInfo +
        ", pathPointType=" + pathPointType +
        ", runwayNumber='" + runwayNumber + '\'' +
        ", approachPerformanceDesignator='" + approachPerformanceDesignator + '\'' +
        ", approachRouteIdentifier='" + approachRouteIdentifier + '\'' +
        ", approachTypeIdentifier='" + approachTypeIdentifier + '\'' +
        ", courseWidthAtThreshold=" + courseWidthAtThreshold +
        ", fasDataCrcRemainder='" + fasDataCrcRemainder + '\'' +
        ", flightPathAlignmentPoint=" + flightPathAlignmentPoint +
        ", fpapEllipsoidHeight=" + fpapEllipsoidHeight +
        ", fpapOrthometricHeight=" + fpapOrthometricHeight +
        ", glidePathAngle=" + glidePathAngle +
        ", gnssChannelNumber=" + gnssChannelNumber +
        ", helicopterProcedureCourse=" + helicopterProcedureCourse +
        ", landingThresholdPoint=" + landingThresholdPoint +
        ", lengthOffset=" + lengthOffset +
        ", ltpEllipsoidHeight=" + ltpEllipsoidHeight +
        ", ltpOrthometricHeight=" + ltpOrthometricHeight +
        ", pathPointTch=" + pathPointTch +
        ", referencePathDataSelector=" + referencePathDataSelector +
        ", referencePathIdentifier='" + referencePathIdentifier + '\'' +
        ", routeIndicator='" + routeIndicator + '\'' +
        ", tchUnitsIndicator='" + tchUnitsIndicator + '\'' +
        ", operationType='" + operationType + '\'' +
        ", horizontalAlertLimit=" + horizontalAlertLimit +
        ", verticalAlertLimit=" + verticalAlertLimit +
        ", sbasServiceProviderIdentifier='" + sbasServiceProviderIdentifier + '\'' +
        ", finalApproachCourseValue=" + finalApproachCourseValue +
        ", finalApproachCourseIsTrue=" + finalApproachCourseIsTrue +
        '}';
  }

  public static final class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private Type pathPointType;
    private String runwayNumber;
    private String approachPerformanceDesignator;
    private String approachRouteIdentifier;
    private String approachTypeIdentifier;
    private BigDecimal courseWidthAtThreshold;
    private String fasDataCrcRemainder;
    private ArincHighPrecisionLocation flightPathAlignmentPoint;
    private BigDecimal fpapEllipsoidHeight;
    private BigDecimal fpapOrthometricHeight;
    private BigDecimal glidePathAngle;
    private Long gnssChannelNumber;
    private BigDecimal helicopterProcedureCourse;
    private ArincHighPrecisionLocation landingThresholdPoint;
    private Long lengthOffset;
    private BigDecimal ltpEllipsoidHeight;
    private BigDecimal ltpOrthometricHeight;
    private BigDecimal pathPointTch;
    private long referencePathDataSelector;
    private String referencePathIdentifier;
    private String routeIndicator;
    private String tchUnitsIndicator;
    private String operationType;
    private BigDecimal horizontalAlertLimit;
    private BigDecimal verticalAlertLimit;
    private String sbasServiceProviderIdentifier;
    private BigDecimal finalApproachCourseValue;
    private Boolean finalApproachCourseIsTrue;

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

    public Builder pathPointType(Type pathPointType) {
      this.pathPointType = pathPointType;
      return this;
    }

    public Builder runwayNumber(String runwayNumber) {
      this.runwayNumber = runwayNumber;
      return this;
    }

    public Builder approachPerformanceDesignator(String approachPerformanceDesignator) {
      this.approachPerformanceDesignator = approachPerformanceDesignator;
      return this;
    }

    public Builder approachRouteIdentifier(String approachRouteIdentifier) {
      this.approachRouteIdentifier = approachRouteIdentifier;
      return this;
    }

    public Builder approachTypeIdentifier(String approachTypeIdentifier) {
      this.approachTypeIdentifier = approachTypeIdentifier;
      return this;
    }

    public Builder courseWidthAtThreshold(BigDecimal courseWidthAtThreshold) {
      this.courseWidthAtThreshold = courseWidthAtThreshold;
      return this;
    }

    public Builder fasDataCrcRemainder(String fasDataCrcRemainder) {
      this.fasDataCrcRemainder = fasDataCrcRemainder;
      return this;
    }

    public Builder flightPathAlignmentPoint(ArincHighPrecisionLocation flightPathAlignmentPoint) {
      this.flightPathAlignmentPoint = flightPathAlignmentPoint;
      return this;
    }

    public Builder fpapEllipsoidHeight(BigDecimal fpapEllipsoidHeight) {
      this.fpapEllipsoidHeight = fpapEllipsoidHeight;
      return this;
    }

    public Builder fpapOrthometricHeight(BigDecimal fpapOrthometricHeight) {
      this.fpapOrthometricHeight = fpapOrthometricHeight;
      return this;
    }

    public Builder glidePathAngle(BigDecimal glidePathAngle) {
      this.glidePathAngle = glidePathAngle;
      return this;
    }

    public Builder gnssChannelNumber(Long gnssChannelNumber) {
      this.gnssChannelNumber = gnssChannelNumber;
      return this;
    }

    public Builder helicopterProcedureCourse(BigDecimal helicopterProcedureCourse) {
      this.helicopterProcedureCourse = helicopterProcedureCourse;
      return this;
    }

    public Builder landingThresholdPoint(ArincHighPrecisionLocation landingThresholdPoint) {
      this.landingThresholdPoint = landingThresholdPoint;
      return this;
    }

    public Builder lengthOffset(Long lengthOffset) {
      this.lengthOffset = lengthOffset;
      return this;
    }

    public Builder ltpEllipsoidHeight(BigDecimal ltpEllipsoidHeight) {
      this.ltpEllipsoidHeight = ltpEllipsoidHeight;
      return this;
    }

    public Builder ltpOrthometricHeight(BigDecimal ltpOrthometricHeight) {
      this.ltpOrthometricHeight = ltpOrthometricHeight;
      return this;
    }

    public Builder pathPointTch(BigDecimal pathPointTch) {
      this.pathPointTch = pathPointTch;
      return this;
    }

    public Builder referencePathDataSelector(long referencePathDataSelector) {
      this.referencePathDataSelector = referencePathDataSelector;
      return this;
    }

    public Builder referencePathIdentifier(String referencePathIdentifier) {
      this.referencePathIdentifier = referencePathIdentifier;
      return this;
    }

    public Builder routeIndicator(String routeIndicator) {
      this.routeIndicator = routeIndicator;
      return this;
    }

    public Builder tchUnitsIndicator(String tchUnitsIndicator) {
      this.tchUnitsIndicator = tchUnitsIndicator;
      return this;
    }

    public Builder operationType(String operationType) {
      this.operationType = operationType;
      return this;
    }

    public Builder horizontalAlertLimit(BigDecimal horizontalAlertLimit) {
      this.horizontalAlertLimit = horizontalAlertLimit;
      return this;
    }

    public Builder verticalAlertLimit(BigDecimal verticalAlertLimit) {
      this.verticalAlertLimit = verticalAlertLimit;
      return this;
    }

    public Builder sbasServiceProviderIdentifier(String sbasServiceProviderIdentifier) {
      this.sbasServiceProviderIdentifier = sbasServiceProviderIdentifier;
      return this;
    }

    public Builder finalApproachCourseValue(BigDecimal finalApproachCourseValue) {
      this.finalApproachCourseValue = finalApproachCourseValue;
      return this;
    }

    public Builder finalApproachCourseIsTrue(Boolean finalApproachCourseIsTrue) {
      this.finalApproachCourseIsTrue = finalApproachCourseIsTrue;
      return this;
    }

    public ArincPathPoint build() {
      return new ArincPathPoint(this);
    }
  }
}
