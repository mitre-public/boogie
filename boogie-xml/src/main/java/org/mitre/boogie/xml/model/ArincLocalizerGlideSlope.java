package org.mitre.boogie.xml.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.FreqUnitOfMeasure;
import org.mitre.boogie.xml.model.fields.IlsBackCourse;
import org.mitre.boogie.xml.model.fields.IlsDmeLocation;
import org.mitre.boogie.xml.model.fields.LocalizerPositionReference;
import org.mitre.boogie.xml.model.fields.NavaidWeatherInfo;
import org.mitre.boogie.xml.model.fields.PrecisionApproachCategory;
import org.mitre.boogie.xml.model.fields.RunwayLeftRightCenterType;
import org.mitre.boogie.xml.model.fields.RunwaySuffix;
import org.mitre.boogie.xml.model.fields.TrueBearingSource;
import org.mitre.boogie.xml.model.fields.VhfNavaidCoverage;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.MagneticVariation;

public final class ArincLocalizerGlideSlope {
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;
  private final ArincPointInfo pointInfo;
  private final String supportingFacilityRef;
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
  // LocalizerGlideslope fields
  private final List<String> approachRouteIdent;
  private final Double glideslopeBeamWidth;
  private final Long glideslopeHeightAtLandingThreshold;
  private final LatLong glideslopeLocation;
  private final Long glideslopePosition;
  private final Long localizerPosition;
  /**
   * See {@link LocalizerPositionReference}
   */
  private final String localizerPositionReference;
  private final Double localizerTrueBearing;
  /**
   * See {@link TrueBearingSource}
   */
  private final String localizerTrueBearingSource;
  private final Double localizerWidth;
  private final MagneticVariation stationDeclination;
  // Frequency (flattened)
  private final Double frequencyValue;
  /**
   * See {@link FreqUnitOfMeasure}
   */
  private final String freqUnitOfMeasure;
  /**
   * See {@link IlsBackCourse}
   */
  private final String ilsBackCourse;
  /**
   * See {@link IlsDmeLocation}
   */
  private final String ilsDmeLocation;
  // VhfNavaidClass (flattened)
  /**
   * See {@link VhfNavaidCoverage}
   */
  private final String vhfNavaidCoverage;
  /**
   * See {@link NavaidWeatherInfo}
   */
  private final String vhfNavaidWeatherInfo;
  private final Boolean isNotCollocated;
  private final Boolean isBiased;
  private final Boolean isNoVoice;

  private ArincLocalizerGlideSlope(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.pointInfo = builder.pointInfo;
    this.supportingFacilityRef = builder.supportingFacilityRef;
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
    this.approachRouteIdent = builder.approachRouteIdent;
    this.glideslopeBeamWidth = builder.glideslopeBeamWidth;
    this.glideslopeHeightAtLandingThreshold = builder.glideslopeHeightAtLandingThreshold;
    this.glideslopeLocation = builder.glideslopeLocation;
    this.glideslopePosition = builder.glideslopePosition;
    this.localizerPosition = builder.localizerPosition;
    this.localizerPositionReference = builder.localizerPositionReference;
    this.localizerTrueBearing = builder.localizerTrueBearing;
    this.localizerTrueBearingSource = builder.localizerTrueBearingSource;
    this.localizerWidth = builder.localizerWidth;
    this.stationDeclination = builder.stationDeclination;
    this.frequencyValue = builder.frequencyValue;
    this.freqUnitOfMeasure = builder.freqUnitOfMeasure;
    this.ilsBackCourse = builder.ilsBackCourse;
    this.ilsDmeLocation = builder.ilsDmeLocation;
    this.vhfNavaidCoverage = builder.vhfNavaidCoverage;
    this.vhfNavaidWeatherInfo = builder.vhfNavaidWeatherInfo;
    this.isNotCollocated = builder.isNotCollocated;
    this.isBiased = builder.isBiased;
    this.isNoVoice = builder.isNoVoice;
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

  public Optional<String> supportingFacilityRef() {
    return Optional.ofNullable(supportingFacilityRef);
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

  public List<String> approachRouteIdent() {
    return approachRouteIdent == null ? List.of() : approachRouteIdent;
  }

  public Optional<Double> glideslopeBeamWidth() {
    return Optional.ofNullable(glideslopeBeamWidth);
  }

  public Optional<Long> glideslopeHeightAtLandingThreshold() {
    return Optional.ofNullable(glideslopeHeightAtLandingThreshold);
  }

  public Optional<LatLong> glideslopeLocation() {
    return Optional.ofNullable(glideslopeLocation);
  }

  public Optional<Long> glideslopePosition() {
    return Optional.ofNullable(glideslopePosition);
  }

  public Optional<Long> localizerPosition() {
    return Optional.ofNullable(localizerPosition);
  }

  public Optional<LocalizerPositionReference> localizerPositionReference() {
    return Optional.ofNullable(localizerPositionReference).filter(LocalizerPositionReference.VALID::contains).map(LocalizerPositionReference::valueOf);
  }

  public Optional<Double> localizerTrueBearing() {
    return Optional.ofNullable(localizerTrueBearing);
  }

  public Optional<TrueBearingSource> localizerTrueBearingSource() {
    return Optional.ofNullable(localizerTrueBearingSource).filter(TrueBearingSource.VALID::contains).map(TrueBearingSource::valueOf);
  }

  public Optional<Double> localizerWidth() {
    return Optional.ofNullable(localizerWidth);
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

  public Optional<IlsBackCourse> ilsBackCourse() {
    return Optional.ofNullable(ilsBackCourse).filter(IlsBackCourse.VALID::contains).map(IlsBackCourse::valueOf);
  }

  public Optional<IlsDmeLocation> ilsDmeLocation() {
    return Optional.ofNullable(ilsDmeLocation).filter(IlsDmeLocation.VALID::contains).map(IlsDmeLocation::valueOf);
  }

  public Optional<VhfNavaidCoverage> vhfNavaidCoverage() {
    return Optional.ofNullable(vhfNavaidCoverage).filter(VhfNavaidCoverage.VALID::contains).map(VhfNavaidCoverage::valueOf);
  }

  public Optional<NavaidWeatherInfo> vhfNavaidWeatherInfo() {
    return Optional.ofNullable(vhfNavaidWeatherInfo).filter(NavaidWeatherInfo.VALID::contains).map(NavaidWeatherInfo::valueOf);
  }

  public Optional<Boolean> isNotCollocated() {
    return Optional.ofNullable(isNotCollocated);
  }

  public Optional<Boolean> isBiased() {
    return Optional.ofNullable(isBiased);
  }

  public Optional<Boolean> isNoVoice() {
    return Optional.ofNullable(isNoVoice);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincLocalizerGlideSlope that = (ArincLocalizerGlideSlope) o;
    return Objects.equals(baseInfo, that.baseInfo) && Objects.equals(recordInfo, that.recordInfo) && Objects.equals(pointInfo, that.pointInfo) && Objects.equals(elevation, that.elevation) && Objects.equals(isVFRCheckpoint, that.isVFRCheckpoint) && Objects.equals(approachAngle, that.approachAngle) && Objects.equals(approachCourseBearingValue, that.approachCourseBearingValue) && Objects.equals(approachCourseBearingIsTrueBearing, that.approachCourseBearingIsTrueBearing) && Objects.equals(category, that.category) && Objects.equals(runwayNumber, that.runwayNumber) && Objects.equals(runwayLeftRightCenterType, that.runwayLeftRightCenterType) && Objects.equals(runwaySuffix, that.runwaySuffix) && Objects.equals(approachRouteIdent, that.approachRouteIdent) && Objects.equals(glideslopeBeamWidth, that.glideslopeBeamWidth) && Objects.equals(glideslopeHeightAtLandingThreshold, that.glideslopeHeightAtLandingThreshold) && Objects.equals(glideslopeLocation, that.glideslopeLocation) && Objects.equals(glideslopePosition, that.glideslopePosition) && Objects.equals(localizerPosition, that.localizerPosition) && Objects.equals(localizerPositionReference, that.localizerPositionReference) && Objects.equals(localizerTrueBearing, that.localizerTrueBearing) && Objects.equals(localizerTrueBearingSource, that.localizerTrueBearingSource) && Objects.equals(localizerWidth, that.localizerWidth) && Objects.equals(stationDeclination, that.stationDeclination) && Objects.equals(frequencyValue, that.frequencyValue) && Objects.equals(freqUnitOfMeasure, that.freqUnitOfMeasure) && Objects.equals(ilsBackCourse, that.ilsBackCourse) && Objects.equals(ilsDmeLocation, that.ilsDmeLocation) && Objects.equals(vhfNavaidCoverage, that.vhfNavaidCoverage) && Objects.equals(vhfNavaidWeatherInfo, that.vhfNavaidWeatherInfo) && Objects.equals(isNotCollocated, that.isNotCollocated) && Objects.equals(isBiased, that.isBiased) && Objects.equals(isNoVoice, that.isNoVoice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, recordInfo, pointInfo, elevation, isVFRCheckpoint, approachAngle, approachCourseBearingValue, category, runwayNumber, frequencyValue, freqUnitOfMeasure, ilsBackCourse, ilsDmeLocation);
  }

  @Override
  public String toString() {
    return "ArincLocalizerGlideSlope{" +
        "pointInfo=" + pointInfo +
        ", frequencyValue=" + frequencyValue +
        ", category='" + category + '\'' +
        '}';
  }

  public static final class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private ArincPointInfo pointInfo;
    private String supportingFacilityRef;
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
    private List<String> approachRouteIdent;
    private Double glideslopeBeamWidth;
    private Long glideslopeHeightAtLandingThreshold;
    private LatLong glideslopeLocation;
    private Long glideslopePosition;
    private Long localizerPosition;
    private String localizerPositionReference;
    private Double localizerTrueBearing;
    private String localizerTrueBearingSource;
    private Double localizerWidth;
    private MagneticVariation stationDeclination;
    private Double frequencyValue;
    private String freqUnitOfMeasure;
    private String ilsBackCourse;
    private String ilsDmeLocation;
    private String vhfNavaidCoverage;
    private String vhfNavaidWeatherInfo;
    private Boolean isNotCollocated;
    private Boolean isBiased;
    private Boolean isNoVoice;

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

    public Builder supportingFacilityRef(String supportingFacilityRef) {
      this.supportingFacilityRef = supportingFacilityRef;
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

    public Builder approachRouteIdent(List<String> approachRouteIdent) {
      this.approachRouteIdent = approachRouteIdent;
      return this;
    }

    public Builder glideslopeBeamWidth(Double glideslopeBeamWidth) {
      this.glideslopeBeamWidth = glideslopeBeamWidth;
      return this;
    }

    public Builder glideslopeHeightAtLandingThreshold(Long glideslopeHeightAtLandingThreshold) {
      this.glideslopeHeightAtLandingThreshold = glideslopeHeightAtLandingThreshold;
      return this;
    }

    public Builder glideslopeLocation(LatLong glideslopeLocation) {
      this.glideslopeLocation = glideslopeLocation;
      return this;
    }

    public Builder glideslopePosition(Long glideslopePosition) {
      this.glideslopePosition = glideslopePosition;
      return this;
    }

    public Builder localizerPosition(Long localizerPosition) {
      this.localizerPosition = localizerPosition;
      return this;
    }

    public Builder localizerPositionReference(String localizerPositionReference) {
      this.localizerPositionReference = localizerPositionReference;
      return this;
    }

    public Builder localizerTrueBearing(Double localizerTrueBearing) {
      this.localizerTrueBearing = localizerTrueBearing;
      return this;
    }

    public Builder localizerTrueBearingSource(String localizerTrueBearingSource) {
      this.localizerTrueBearingSource = localizerTrueBearingSource;
      return this;
    }

    public Builder localizerWidth(Double localizerWidth) {
      this.localizerWidth = localizerWidth;
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

    public Builder ilsBackCourse(String ilsBackCourse) {
      this.ilsBackCourse = ilsBackCourse;
      return this;
    }

    public Builder ilsDmeLocation(String ilsDmeLocation) {
      this.ilsDmeLocation = ilsDmeLocation;
      return this;
    }

    public Builder vhfNavaidCoverage(String vhfNavaidCoverage) {
      this.vhfNavaidCoverage = vhfNavaidCoverage;
      return this;
    }

    public Builder vhfNavaidWeatherInfo(String vhfNavaidWeatherInfo) {
      this.vhfNavaidWeatherInfo = vhfNavaidWeatherInfo;
      return this;
    }

    public Builder isNotCollocated(Boolean isNotCollocated) {
      this.isNotCollocated = isNotCollocated;
      return this;
    }

    public Builder isBiased(Boolean isBiased) {
      this.isBiased = isBiased;
      return this;
    }

    public Builder isNoVoice(Boolean isNoVoice) {
      this.isNoVoice = isNoVoice;
      return this;
    }

    public ArincLocalizerGlideSlope build() {
      return new ArincLocalizerGlideSlope(this);
    }
  }
}
