package org.mitre.boogie.xml.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ElevationType;
import org.mitre.boogie.xml.model.fields.HelicopterPerformanceReq;
import org.mitre.boogie.xml.model.fields.HelipadShape;
import org.mitre.boogie.xml.model.fields.RunwaySurfaceCode;
import org.mitre.boogie.xml.model.fields.SurfaceType;

public final class ArincHelipad {
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;
  private final ArincPointInfo pointInfo;
  private final Boolean isWithoutLocation;
  private final Boolean isDerivedLocation;
  private final Integer elevation;
  /**
   * See {@link ElevationType}
   */
  private final String elevationType;
  private final Integer tlofPadLengthLongSide;
  private final Integer tlofPadLengthShortSide;
  private final Integer tlofPadDiameter;
  private final Integer fatoPadLengthLongSide;
  private final Integer fatoPadLengthShortSide;
  private final Integer fatoPadDiameter;
  private final Integer safetyPadLengthLongSide;
  private final Integer safetyPadLengthShortSide;
  private final Integer safetyPadDiameter;
  /**
   * See {@link HelipadShape}
   */
  private final String helipadShape;
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
  private final Boolean isElevated;
  private final Integer helipadMaximumRotorDiameter;
  private final Double helipadOrientationBearing;
  private final Boolean helipadOrientationIsTrueBearing;
  private final Double helipadIdentifierOrientationBearing;
  private final Boolean helipadIdentifierOrientationIsTrueBearing;
  private final List<Double> preferredApproachBearings;

  private ArincHelipad(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.pointInfo = builder.pointInfo;
    this.isWithoutLocation = builder.isWithoutLocation;
    this.isDerivedLocation = builder.isDerivedLocation;
    this.elevation = builder.elevation;
    this.elevationType = builder.elevationType;
    this.tlofPadLengthLongSide = builder.tlofPadLengthLongSide;
    this.tlofPadLengthShortSide = builder.tlofPadLengthShortSide;
    this.tlofPadDiameter = builder.tlofPadDiameter;
    this.fatoPadLengthLongSide = builder.fatoPadLengthLongSide;
    this.fatoPadLengthShortSide = builder.fatoPadLengthShortSide;
    this.fatoPadDiameter = builder.fatoPadDiameter;
    this.safetyPadLengthLongSide = builder.safetyPadLengthLongSide;
    this.safetyPadLengthShortSide = builder.safetyPadLengthShortSide;
    this.safetyPadDiameter = builder.safetyPadDiameter;
    this.helipadShape = builder.helipadShape;
    this.surfaceCode = builder.surfaceCode;
    this.surfaceType = builder.surfaceType;
    this.helicopterPerformanceReq = builder.helicopterPerformanceReq;
    this.isElevated = builder.isElevated;
    this.helipadMaximumRotorDiameter = builder.helipadMaximumRotorDiameter;
    this.helipadOrientationBearing = builder.helipadOrientationBearing;
    this.helipadOrientationIsTrueBearing = builder.helipadOrientationIsTrueBearing;
    this.helipadIdentifierOrientationBearing = builder.helipadIdentifierOrientationBearing;
    this.helipadIdentifierOrientationIsTrueBearing = builder.helipadIdentifierOrientationIsTrueBearing;
    this.preferredApproachBearings = builder.preferredApproachBearings;
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
        .elevation(elevation)
        .elevationType(elevationType)
        .tlofPadLengthLongSide(tlofPadLengthLongSide)
        .tlofPadLengthShortSide(tlofPadLengthShortSide)
        .tlofPadDiameter(tlofPadDiameter)
        .fatoPadLengthLongSide(fatoPadLengthLongSide)
        .fatoPadLengthShortSide(fatoPadLengthShortSide)
        .fatoPadDiameter(fatoPadDiameter)
        .safetyPadLengthLongSide(safetyPadLengthLongSide)
        .safetyPadLengthShortSide(safetyPadLengthShortSide)
        .safetyPadDiameter(safetyPadDiameter)
        .helipadShape(helipadShape)
        .surfaceCode(surfaceCode)
        .surfaceType(surfaceType)
        .helicopterPerformanceReq(helicopterPerformanceReq)
        .isElevated(isElevated)
        .helipadMaximumRotorDiameter(helipadMaximumRotorDiameter)
        .helipadOrientationBearing(helipadOrientationBearing)
        .helipadOrientationIsTrueBearing(helipadOrientationIsTrueBearing)
        .helipadIdentifierOrientationBearing(helipadIdentifierOrientationBearing)
        .helipadIdentifierOrientationIsTrueBearing(helipadIdentifierOrientationIsTrueBearing)
        .preferredApproachBearings(preferredApproachBearings);
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

  public Optional<Integer> elevation() {
    return Optional.ofNullable(elevation);
  }

  public Optional<ElevationType> elevationType() {
    return Optional.ofNullable(elevationType).filter(ElevationType.VALID::contains).map(ElevationType::valueOf);
  }

  public Optional<Integer> tlofPadLengthLongSide() {
    return Optional.ofNullable(tlofPadLengthLongSide);
  }

  public Optional<Integer> tlofPadLengthShortSide() {
    return Optional.ofNullable(tlofPadLengthShortSide);
  }

  public Optional<Integer> tlofPadDiameter() {
    return Optional.ofNullable(tlofPadDiameter);
  }

  public Optional<Integer> fatoPadLengthLongSide() {
    return Optional.ofNullable(fatoPadLengthLongSide);
  }

  public Optional<Integer> fatoPadLengthShortSide() {
    return Optional.ofNullable(fatoPadLengthShortSide);
  }

  public Optional<Integer> fatoPadDiameter() {
    return Optional.ofNullable(fatoPadDiameter);
  }

  public Optional<Integer> safetyPadLengthLongSide() {
    return Optional.ofNullable(safetyPadLengthLongSide);
  }

  public Optional<Integer> safetyPadLengthShortSide() {
    return Optional.ofNullable(safetyPadLengthShortSide);
  }

  public Optional<Integer> safetyPadDiameter() {
    return Optional.ofNullable(safetyPadDiameter);
  }

  public Optional<HelipadShape> helipadShape() {
    return Optional.ofNullable(helipadShape).filter(HelipadShape.VALID::contains).map(HelipadShape::valueOf);
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

  public Optional<Boolean> isElevated() {
    return Optional.ofNullable(isElevated);
  }

  public Optional<Integer> helipadMaximumRotorDiameter() {
    return Optional.ofNullable(helipadMaximumRotorDiameter);
  }

  public Optional<Double> helipadOrientationBearing() {
    return Optional.ofNullable(helipadOrientationBearing);
  }

  public Optional<Boolean> helipadOrientationIsTrueBearing() {
    return Optional.ofNullable(helipadOrientationIsTrueBearing);
  }

  public Optional<Double> helipadIdentifierOrientationBearing() {
    return Optional.ofNullable(helipadIdentifierOrientationBearing);
  }

  public Optional<Boolean> helipadIdentifierOrientationIsTrueBearing() {
    return Optional.ofNullable(helipadIdentifierOrientationIsTrueBearing);
  }

  public List<Double> preferredApproachBearings() {
    return Optional.ofNullable(preferredApproachBearings).orElse(List.of());
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    ArincHelipad that = (ArincHelipad) o;
    return Objects.equals(baseInfo, that.baseInfo) && Objects.equals(recordInfo, that.recordInfo) && Objects.equals(pointInfo, that.pointInfo) && Objects.equals(isWithoutLocation, that.isWithoutLocation) && Objects.equals(isDerivedLocation, that.isDerivedLocation) && Objects.equals(elevation, that.elevation) && Objects.equals(elevationType, that.elevationType) && Objects.equals(tlofPadLengthLongSide, that.tlofPadLengthLongSide) && Objects.equals(tlofPadLengthShortSide, that.tlofPadLengthShortSide) && Objects.equals(tlofPadDiameter, that.tlofPadDiameter) && Objects.equals(fatoPadLengthLongSide, that.fatoPadLengthLongSide) && Objects.equals(fatoPadLengthShortSide, that.fatoPadLengthShortSide) && Objects.equals(fatoPadDiameter, that.fatoPadDiameter) && Objects.equals(safetyPadLengthLongSide, that.safetyPadLengthLongSide) && Objects.equals(safetyPadLengthShortSide, that.safetyPadLengthShortSide) && Objects.equals(safetyPadDiameter, that.safetyPadDiameter) && Objects.equals(helipadShape, that.helipadShape) && Objects.equals(surfaceCode, that.surfaceCode) && Objects.equals(surfaceType, that.surfaceType) && Objects.equals(helicopterPerformanceReq, that.helicopterPerformanceReq) && Objects.equals(isElevated, that.isElevated) && Objects.equals(helipadMaximumRotorDiameter, that.helipadMaximumRotorDiameter) && Objects.equals(helipadOrientationBearing, that.helipadOrientationBearing) && Objects.equals(helipadOrientationIsTrueBearing, that.helipadOrientationIsTrueBearing) && Objects.equals(helipadIdentifierOrientationBearing, that.helipadIdentifierOrientationBearing) && Objects.equals(helipadIdentifierOrientationIsTrueBearing, that.helipadIdentifierOrientationIsTrueBearing) && Objects.equals(preferredApproachBearings, that.preferredApproachBearings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, recordInfo, pointInfo, isWithoutLocation, isDerivedLocation, elevation, elevationType, tlofPadLengthLongSide, tlofPadLengthShortSide, tlofPadDiameter, fatoPadLengthLongSide, fatoPadLengthShortSide, fatoPadDiameter, safetyPadLengthLongSide, safetyPadLengthShortSide, safetyPadDiameter, helipadShape, surfaceCode, surfaceType, helicopterPerformanceReq, isElevated, helipadMaximumRotorDiameter, helipadOrientationBearing, helipadOrientationIsTrueBearing, helipadIdentifierOrientationBearing, helipadIdentifierOrientationIsTrueBearing, preferredApproachBearings);
  }

  @Override
  public String toString() {
    return "ArincHelipad{" +
        "baseInfo=" + baseInfo +
        ", recordInfo=" + recordInfo +
        ", pointInfo=" + pointInfo +
        ", isWithoutLocation=" + isWithoutLocation +
        ", isDerivedLocation=" + isDerivedLocation +
        ", elevation=" + elevation +
        ", elevationType='" + elevationType + '\'' +
        ", tlofPadLengthLongSide=" + tlofPadLengthLongSide +
        ", tlofPadLengthShortSide=" + tlofPadLengthShortSide +
        ", tlofPadDiameter=" + tlofPadDiameter +
        ", fatoPadLengthLongSide=" + fatoPadLengthLongSide +
        ", fatoPadLengthShortSide=" + fatoPadLengthShortSide +
        ", fatoPadDiameter=" + fatoPadDiameter +
        ", safetyPadLengthLongSide=" + safetyPadLengthLongSide +
        ", safetyPadLengthShortSide=" + safetyPadLengthShortSide +
        ", safetyPadDiameter=" + safetyPadDiameter +
        ", helipadShape='" + helipadShape + '\'' +
        ", surfaceCode='" + surfaceCode + '\'' +
        ", surfaceType='" + surfaceType + '\'' +
        ", helicopterPerformanceReq='" + helicopterPerformanceReq + '\'' +
        ", isElevated=" + isElevated +
        ", helipadMaximumRotorDiameter=" + helipadMaximumRotorDiameter +
        ", helipadOrientationBearing=" + helipadOrientationBearing +
        ", helipadOrientationIsTrueBearing=" + helipadOrientationIsTrueBearing +
        ", helipadIdentifierOrientationBearing=" + helipadIdentifierOrientationBearing +
        ", helipadIdentifierOrientationIsTrueBearing=" + helipadIdentifierOrientationIsTrueBearing +
        ", preferredApproachBearings=" + preferredApproachBearings +
        '}';
  }

  public static class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private ArincPointInfo pointInfo;
    private Boolean isWithoutLocation;
    private Boolean isDerivedLocation;
    private Integer elevation;
    private String elevationType;
    private Integer tlofPadLengthLongSide;
    private Integer tlofPadLengthShortSide;
    private Integer tlofPadDiameter;
    private Integer fatoPadLengthLongSide;
    private Integer fatoPadLengthShortSide;
    private Integer fatoPadDiameter;
    private Integer safetyPadLengthLongSide;
    private Integer safetyPadLengthShortSide;
    private Integer safetyPadDiameter;
    private String helipadShape;
    private String surfaceCode;
    private String surfaceType;
    private String helicopterPerformanceReq;
    private Boolean isElevated;
    private Integer helipadMaximumRotorDiameter;
    private Double helipadOrientationBearing;
    private Boolean helipadOrientationIsTrueBearing;
    private Double helipadIdentifierOrientationBearing;
    private Boolean helipadIdentifierOrientationIsTrueBearing;
    private List<Double> preferredApproachBearings;

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

    public Builder elevation(Integer elevation) {
      this.elevation = elevation;
      return this;
    }

    public Builder elevationType(String elevationType) {
      this.elevationType = elevationType;
      return this;
    }

    public Builder tlofPadLengthLongSide(Integer tlofPadLengthLongSide) {
      this.tlofPadLengthLongSide = tlofPadLengthLongSide;
      return this;
    }

    public Builder tlofPadLengthShortSide(Integer tlofPadLengthShortSide) {
      this.tlofPadLengthShortSide = tlofPadLengthShortSide;
      return this;
    }

    public Builder tlofPadDiameter(Integer tlofPadDiameter) {
      this.tlofPadDiameter = tlofPadDiameter;
      return this;
    }

    public Builder fatoPadLengthLongSide(Integer fatoPadLengthLongSide) {
      this.fatoPadLengthLongSide = fatoPadLengthLongSide;
      return this;
    }

    public Builder fatoPadLengthShortSide(Integer fatoPadLengthShortSide) {
      this.fatoPadLengthShortSide = fatoPadLengthShortSide;
      return this;
    }

    public Builder fatoPadDiameter(Integer fatoPadDiameter) {
      this.fatoPadDiameter = fatoPadDiameter;
      return this;
    }

    public Builder safetyPadLengthLongSide(Integer safetyPadLengthLongSide) {
      this.safetyPadLengthLongSide = safetyPadLengthLongSide;
      return this;
    }

    public Builder safetyPadLengthShortSide(Integer safetyPadLengthShortSide) {
      this.safetyPadLengthShortSide = safetyPadLengthShortSide;
      return this;
    }

    public Builder safetyPadDiameter(Integer safetyPadDiameter) {
      this.safetyPadDiameter = safetyPadDiameter;
      return this;
    }

    public Builder helipadShape(String helipadShape) {
      this.helipadShape = helipadShape;
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

    public Builder isElevated(Boolean isElevated) {
      this.isElevated = isElevated;
      return this;
    }

    public Builder helipadMaximumRotorDiameter(Integer helipadMaximumRotorDiameter) {
      this.helipadMaximumRotorDiameter = helipadMaximumRotorDiameter;
      return this;
    }

    public Builder helipadOrientationBearing(Double helipadOrientationBearing) {
      this.helipadOrientationBearing = helipadOrientationBearing;
      return this;
    }

    public Builder helipadOrientationIsTrueBearing(Boolean helipadOrientationIsTrueBearing) {
      this.helipadOrientationIsTrueBearing = helipadOrientationIsTrueBearing;
      return this;
    }

    public Builder helipadIdentifierOrientationBearing(Double helipadIdentifierOrientationBearing) {
      this.helipadIdentifierOrientationBearing = helipadIdentifierOrientationBearing;
      return this;
    }

    public Builder helipadIdentifierOrientationIsTrueBearing(Boolean helipadIdentifierOrientationIsTrueBearing) {
      this.helipadIdentifierOrientationIsTrueBearing = helipadIdentifierOrientationIsTrueBearing;
      return this;
    }

    public Builder preferredApproachBearings(List<Double> preferredApproachBearings) {
      this.preferredApproachBearings = preferredApproachBearings;
      return this;
    }

    public ArincHelipad build() {
      return new ArincHelipad(this);
    }
  }
}
