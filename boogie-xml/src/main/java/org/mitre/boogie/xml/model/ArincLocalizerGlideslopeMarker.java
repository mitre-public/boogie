package org.mitre.boogie.xml.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.FreqUnitOfMeasure;
import org.mitre.boogie.xml.model.fields.LocatorAddInfo;
import org.mitre.boogie.xml.model.fields.LocatorCollocation;
import org.mitre.boogie.xml.model.fields.LocatorCoverage;
import org.mitre.boogie.xml.model.fields.LocatorFacility1;
import org.mitre.boogie.xml.model.fields.LocatorFacility2;
import org.mitre.boogie.xml.model.fields.MarkerType;
import org.mitre.boogie.xml.model.fields.NavaidWeatherInfo;
import org.mitre.boogie.xml.model.fields.RunwayLeftRightCenterType;
import org.mitre.boogie.xml.model.fields.RunwaySuffix;

public final class ArincLocalizerGlideslopeMarker implements ArincFixRecord {
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;
  private final ArincPointInfo pointInfo;
  // AirportHeliportLocalizerMarker fields
  private final String localizerRef;
  private final int elevation;
  private final String locatorFacilityCharacteristics;
  // Locator frequency (flattened from Frequency)
  private final Double locatorFrequencyValue;
  /**
   * See {@link FreqUnitOfMeasure}
   */
  private final String locatorFreqUnitOfMeasure;
  /**
   * See {@link MarkerType}
   */
  private final String markerType;
  private final Double minorAxisBearing;
  // RunwayIdentifier (flattened)
  private final Integer runwayNumber;
  /**
   * See {@link RunwayLeftRightCenterType}
   */
  private final String runwayLeftRightCenterType;
  /**
   * See {@link RunwaySuffix}
   */
  private final String runwaySuffix;
  // LocatorClass (flattened)
  /**
   * See {@link LocatorFacility1}
   */
  private final String locatorFacility1;
  /**
   * See {@link LocatorFacility2}
   */
  private final String locatorFacility2;
  /**
   * See {@link LocatorCoverage}
   */
  private final String locatorCoverage;
  /**
   * See {@link LocatorAddInfo}
   */
  private final String locatorAddInfo;
  /**
   * See {@link NavaidWeatherInfo}
   */
  private final String locatorWeatherInfo;
  /**
   * See {@link LocatorCollocation}
   */
  private final String locatorCollocation;

  private ArincLocalizerGlideslopeMarker(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.pointInfo = builder.pointInfo;
    this.localizerRef = builder.localizerRef;
    this.elevation = builder.elevation;
    this.locatorFacilityCharacteristics = builder.locatorFacilityCharacteristics;
    this.locatorFrequencyValue = builder.locatorFrequencyValue;
    this.locatorFreqUnitOfMeasure = builder.locatorFreqUnitOfMeasure;
    this.markerType = builder.markerType;
    this.minorAxisBearing = builder.minorAxisBearing;
    this.runwayNumber = builder.runwayNumber;
    this.runwayLeftRightCenterType = builder.runwayLeftRightCenterType;
    this.runwaySuffix = builder.runwaySuffix;
    this.locatorFacility1 = builder.locatorFacility1;
    this.locatorFacility2 = builder.locatorFacility2;
    this.locatorCoverage = builder.locatorCoverage;
    this.locatorAddInfo = builder.locatorAddInfo;
    this.locatorWeatherInfo = builder.locatorWeatherInfo;
    this.locatorCollocation = builder.locatorCollocation;
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

  public Optional<String> localizerRef() {
    return Optional.ofNullable(localizerRef);
  }

  public int elevation() {
    return elevation;
  }

  public Optional<String> locatorFacilityCharacteristics() {
    return Optional.ofNullable(locatorFacilityCharacteristics);
  }

  public Optional<Double> locatorFrequencyValue() {
    return Optional.ofNullable(locatorFrequencyValue);
  }

  public Optional<FreqUnitOfMeasure> locatorFreqUnitOfMeasure() {
    return Optional.ofNullable(locatorFreqUnitOfMeasure).filter(FreqUnitOfMeasure.VALID::contains).map(FreqUnitOfMeasure::valueOf);
  }

  public Optional<MarkerType> markerType() {
    return Optional.ofNullable(markerType).filter(MarkerType.VALID::contains).map(MarkerType::valueOf);
  }

  public Optional<Double> minorAxisBearing() {
    return Optional.ofNullable(minorAxisBearing);
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

  public Optional<LocatorFacility1> locatorFacility1() {
    return Optional.ofNullable(locatorFacility1).filter(LocatorFacility1.VALID::contains).map(LocatorFacility1::valueOf);
  }

  public Optional<LocatorFacility2> locatorFacility2() {
    return Optional.ofNullable(locatorFacility2).filter(LocatorFacility2.VALID::contains).map(LocatorFacility2::valueOf);
  }

  public Optional<LocatorCoverage> locatorCoverage() {
    return Optional.ofNullable(locatorCoverage).filter(LocatorCoverage.VALID::contains).map(LocatorCoverage::valueOf);
  }

  public Optional<LocatorAddInfo> locatorAddInfo() {
    return Optional.ofNullable(locatorAddInfo).filter(LocatorAddInfo.VALID::contains).map(LocatorAddInfo::valueOf);
  }

  public Optional<NavaidWeatherInfo> locatorWeatherInfo() {
    return Optional.ofNullable(locatorWeatherInfo).filter(NavaidWeatherInfo.VALID::contains).map(NavaidWeatherInfo::valueOf);
  }

  public Optional<LocatorCollocation> locatorCollocation() {
    return Optional.ofNullable(locatorCollocation).filter(LocatorCollocation.VALID::contains).map(LocatorCollocation::valueOf);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    ArincLocalizerGlideslopeMarker that = (ArincLocalizerGlideslopeMarker) o;
    return elevation == that.elevation && Objects.equals(baseInfo, that.baseInfo) && Objects.equals(recordInfo, that.recordInfo) && Objects.equals(pointInfo, that.pointInfo) && Objects.equals(localizerRef, that.localizerRef) && Objects.equals(locatorFacilityCharacteristics, that.locatorFacilityCharacteristics) && Objects.equals(locatorFrequencyValue, that.locatorFrequencyValue) && Objects.equals(locatorFreqUnitOfMeasure, that.locatorFreqUnitOfMeasure) && Objects.equals(markerType, that.markerType) && Objects.equals(minorAxisBearing, that.minorAxisBearing) && Objects.equals(runwayNumber, that.runwayNumber) && Objects.equals(runwayLeftRightCenterType, that.runwayLeftRightCenterType) && Objects.equals(runwaySuffix, that.runwaySuffix) && Objects.equals(locatorFacility1, that.locatorFacility1) && Objects.equals(locatorFacility2, that.locatorFacility2) && Objects.equals(locatorCoverage, that.locatorCoverage) && Objects.equals(locatorAddInfo, that.locatorAddInfo) && Objects.equals(locatorWeatherInfo, that.locatorWeatherInfo) && Objects.equals(locatorCollocation, that.locatorCollocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, recordInfo, pointInfo, localizerRef, elevation, locatorFacilityCharacteristics, locatorFrequencyValue, locatorFreqUnitOfMeasure, markerType, minorAxisBearing, runwayNumber, runwayLeftRightCenterType, runwaySuffix, locatorFacility1, locatorFacility2, locatorCoverage, locatorAddInfo, locatorWeatherInfo, locatorCollocation);
  }

  @Override
  public String toString() {
    return "ArincLocalizerGlideslopeMarker{" +
        "baseInfo=" + baseInfo +
        ", recordInfo=" + recordInfo +
        ", pointInfo=" + pointInfo +
        ", localizerRef='" + localizerRef + '\'' +
        ", elevation=" + elevation +
        ", locatorFacilityCharacteristics='" + locatorFacilityCharacteristics + '\'' +
        ", locatorFrequencyValue=" + locatorFrequencyValue +
        ", locatorFreqUnitOfMeasure='" + locatorFreqUnitOfMeasure + '\'' +
        ", markerType='" + markerType + '\'' +
        ", minorAxisBearing=" + minorAxisBearing +
        ", runwayNumber=" + runwayNumber +
        ", runwayLeftRightCenterType='" + runwayLeftRightCenterType + '\'' +
        ", runwaySuffix='" + runwaySuffix + '\'' +
        ", locatorFacility1='" + locatorFacility1 + '\'' +
        ", locatorFacility2='" + locatorFacility2 + '\'' +
        ", locatorCoverage='" + locatorCoverage + '\'' +
        ", locatorAddInfo='" + locatorAddInfo + '\'' +
        ", locatorWeatherInfo='" + locatorWeatherInfo + '\'' +
        ", locatorCollocation='" + locatorCollocation + '\'' +
        '}';
  }

  public static final class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private ArincPointInfo pointInfo;
    private String localizerRef;
    private int elevation;
    private String locatorFacilityCharacteristics;
    private Double locatorFrequencyValue;
    private String locatorFreqUnitOfMeasure;
    private String markerType;
    private Double minorAxisBearing;
    private Integer runwayNumber;
    private String runwayLeftRightCenterType;
    private String runwaySuffix;
    private String locatorFacility1;
    private String locatorFacility2;
    private String locatorCoverage;
    private String locatorAddInfo;
    private String locatorWeatherInfo;
    private String locatorCollocation;

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

    public Builder localizerRef(String localizerRef) {
      this.localizerRef = localizerRef;
      return this;
    }

    public Builder elevation(int elevation) {
      this.elevation = elevation;
      return this;
    }

    public Builder locatorFacilityCharacteristics(String locatorFacilityCharacteristics) {
      this.locatorFacilityCharacteristics = locatorFacilityCharacteristics;
      return this;
    }

    public Builder locatorFrequencyValue(Double locatorFrequencyValue) {
      this.locatorFrequencyValue = locatorFrequencyValue;
      return this;
    }

    public Builder locatorFreqUnitOfMeasure(String locatorFreqUnitOfMeasure) {
      this.locatorFreqUnitOfMeasure = locatorFreqUnitOfMeasure;
      return this;
    }

    public Builder markerType(String markerType) {
      this.markerType = markerType;
      return this;
    }

    public Builder minorAxisBearing(Double minorAxisBearing) {
      this.minorAxisBearing = minorAxisBearing;
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

    public Builder locatorFacility1(String locatorFacility1) {
      this.locatorFacility1 = locatorFacility1;
      return this;
    }

    public Builder locatorFacility2(String locatorFacility2) {
      this.locatorFacility2 = locatorFacility2;
      return this;
    }

    public Builder locatorCoverage(String locatorCoverage) {
      this.locatorCoverage = locatorCoverage;
      return this;
    }

    public Builder locatorAddInfo(String locatorAddInfo) {
      this.locatorAddInfo = locatorAddInfo;
      return this;
    }

    public Builder locatorWeatherInfo(String locatorWeatherInfo) {
      this.locatorWeatherInfo = locatorWeatherInfo;
      return this;
    }

    public Builder locatorCollocation(String locatorCollocation) {
      this.locatorCollocation = locatorCollocation;
      return this;
    }

    public ArincLocalizerGlideslopeMarker build() {
      return new ArincLocalizerGlideslopeMarker(this);
    }
  }
}
