package org.mitre.boogie.xml.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.FreqUnitOfMeasure;
import org.mitre.boogie.xml.model.fields.NavaidNdbEmissionType;
import org.mitre.boogie.xml.model.fields.NavaidVoice;
import org.mitre.boogie.xml.model.fields.NavaidWeatherInfo;
import org.mitre.boogie.xml.model.fields.NdbNavaidCoverage;
import org.mitre.boogie.xml.model.fields.NdbNavaidIfMarkerInfo;
import org.mitre.boogie.xml.model.fields.NdbNavaidType;

public final class ArincNdbNavaid {
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;
  private final ArincPointInfo pointInfo;
  private final String dmeTacanRef;
  // Navaid fields
  private final Integer elevation;
  private final Boolean isVFRCheckpoint;
  // Ndb frequency (flattened from Frequency)
  private final Double frequencyValue;
  /**
   * See {@link FreqUnitOfMeasure}
   */
  private final String freqUnitOfMeasure;
  // Ndb fields
  private final Long typeOfEmission;
  private final Long repetitionRate;
  /**
   * See {@link NavaidNdbEmissionType}
   */
  private final String navaidNdbEmissionType;
  /**
   * See {@link NavaidVoice}
   */
  private final String ndbVoice;
  // NdbNavaidClass (flattened)
  private final Boolean isBfoRequired;
  /**
   * See {@link NdbNavaidCoverage}
   */
  private final String ndbNavaidCoverage;
  /**
   * See {@link NdbNavaidIfMarkerInfo}
   */
  private final String ndbNavaidIfMarker;
  /**
   * See {@link NdbNavaidType}
   */
  private final String ndbNavaidType;
  /**
   * See {@link NavaidWeatherInfo}
   */
  private final String ndbNavaidWeatherInfo;
  private final Boolean isNoVoice;

  private ArincNdbNavaid(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.pointInfo = builder.pointInfo;
    this.dmeTacanRef = builder.dmeTacanRef;
    this.elevation = builder.elevation;
    this.isVFRCheckpoint = builder.isVFRCheckpoint;
    this.frequencyValue = builder.frequencyValue;
    this.freqUnitOfMeasure = builder.freqUnitOfMeasure;
    this.typeOfEmission = builder.typeOfEmission;
    this.repetitionRate = builder.repetitionRate;
    this.navaidNdbEmissionType = builder.navaidNdbEmissionType;
    this.ndbVoice = builder.ndbVoice;
    this.isBfoRequired = builder.isBfoRequired;
    this.ndbNavaidCoverage = builder.ndbNavaidCoverage;
    this.ndbNavaidIfMarker = builder.ndbNavaidIfMarker;
    this.ndbNavaidType = builder.ndbNavaidType;
    this.ndbNavaidWeatherInfo = builder.ndbNavaidWeatherInfo;
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

  public Optional<String> dmeTacanRef() {
    return Optional.ofNullable(dmeTacanRef);
  }

  public Optional<Integer> elevation() {
    return Optional.ofNullable(elevation);
  }

  public Optional<Boolean> isVFRCheckpoint() {
    return Optional.ofNullable(isVFRCheckpoint);
  }

  public Optional<Double> frequencyValue() {
    return Optional.ofNullable(frequencyValue);
  }

  public Optional<FreqUnitOfMeasure> freqUnitOfMeasure() {
    return Optional.ofNullable(freqUnitOfMeasure).filter(FreqUnitOfMeasure.VALID::contains).map(FreqUnitOfMeasure::valueOf);
  }

  public Optional<Long> typeOfEmission() {
    return Optional.ofNullable(typeOfEmission);
  }

  public Optional<Long> repetitionRate() {
    return Optional.ofNullable(repetitionRate);
  }

  public Optional<NavaidNdbEmissionType> navaidNdbEmissionType() {
    return Optional.ofNullable(navaidNdbEmissionType).filter(NavaidNdbEmissionType.VALID::contains).map(NavaidNdbEmissionType::valueOf);
  }

  public Optional<NavaidVoice> ndbVoice() {
    return Optional.ofNullable(ndbVoice).filter(NavaidVoice.VALID::contains).map(NavaidVoice::valueOf);
  }

  public Optional<Boolean> isBfoRequired() {
    return Optional.ofNullable(isBfoRequired);
  }

  public Optional<NdbNavaidCoverage> ndbNavaidCoverage() {
    return Optional.ofNullable(ndbNavaidCoverage).filter(NdbNavaidCoverage.VALID::contains).map(NdbNavaidCoverage::valueOf);
  }

  public Optional<NdbNavaidIfMarkerInfo> ndbNavaidIfMarker() {
    return Optional.ofNullable(ndbNavaidIfMarker).filter(NdbNavaidIfMarkerInfo.VALID::contains).map(NdbNavaidIfMarkerInfo::valueOf);
  }

  public Optional<NdbNavaidType> ndbNavaidType() {
    return Optional.ofNullable(ndbNavaidType).filter(NdbNavaidType.VALID::contains).map(NdbNavaidType::valueOf);
  }

  public Optional<NavaidWeatherInfo> ndbNavaidWeatherInfo() {
    return Optional.ofNullable(ndbNavaidWeatherInfo).filter(NavaidWeatherInfo.VALID::contains).map(NavaidWeatherInfo::valueOf);
  }

  public Optional<Boolean> isNoVoice() {
    return Optional.ofNullable(isNoVoice);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincNdbNavaid that = (ArincNdbNavaid) o;
    return Objects.equals(baseInfo, that.baseInfo) && Objects.equals(recordInfo, that.recordInfo) && Objects.equals(pointInfo, that.pointInfo) && Objects.equals(elevation, that.elevation) && Objects.equals(isVFRCheckpoint, that.isVFRCheckpoint) && Objects.equals(frequencyValue, that.frequencyValue) && Objects.equals(freqUnitOfMeasure, that.freqUnitOfMeasure) && Objects.equals(typeOfEmission, that.typeOfEmission) && Objects.equals(repetitionRate, that.repetitionRate) && Objects.equals(navaidNdbEmissionType, that.navaidNdbEmissionType) && Objects.equals(ndbVoice, that.ndbVoice) && Objects.equals(isBfoRequired, that.isBfoRequired) && Objects.equals(ndbNavaidCoverage, that.ndbNavaidCoverage) && Objects.equals(ndbNavaidIfMarker, that.ndbNavaidIfMarker) && Objects.equals(ndbNavaidType, that.ndbNavaidType) && Objects.equals(ndbNavaidWeatherInfo, that.ndbNavaidWeatherInfo) && Objects.equals(isNoVoice, that.isNoVoice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, recordInfo, pointInfo, elevation, isVFRCheckpoint, frequencyValue, freqUnitOfMeasure, typeOfEmission, repetitionRate, navaidNdbEmissionType, ndbVoice, isBfoRequired, ndbNavaidCoverage, ndbNavaidIfMarker, ndbNavaidType, ndbNavaidWeatherInfo, isNoVoice);
  }

  @Override
  public String toString() {
    return "ArincNdbNavaid{" +
        "pointInfo=" + pointInfo +
        ", elevation=" + elevation +
        ", frequencyValue=" + frequencyValue +
        ", freqUnitOfMeasure='" + freqUnitOfMeasure + '\'' +
        '}';
  }

  public static final class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private ArincPointInfo pointInfo;
    private String dmeTacanRef;
    private Integer elevation;
    private Boolean isVFRCheckpoint;
    private Double frequencyValue;
    private String freqUnitOfMeasure;
    private Long typeOfEmission;
    private Long repetitionRate;
    private String navaidNdbEmissionType;
    private String ndbVoice;
    private Boolean isBfoRequired;
    private String ndbNavaidCoverage;
    private String ndbNavaidIfMarker;
    private String ndbNavaidType;
    private String ndbNavaidWeatherInfo;
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

    public Builder dmeTacanRef(String dmeTacanRef) {
      this.dmeTacanRef = dmeTacanRef;
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

    public Builder frequencyValue(Double frequencyValue) {
      this.frequencyValue = frequencyValue;
      return this;
    }

    public Builder freqUnitOfMeasure(String freqUnitOfMeasure) {
      this.freqUnitOfMeasure = freqUnitOfMeasure;
      return this;
    }

    public Builder typeOfEmission(Long typeOfEmission) {
      this.typeOfEmission = typeOfEmission;
      return this;
    }

    public Builder repetitionRate(Long repetitionRate) {
      this.repetitionRate = repetitionRate;
      return this;
    }

    public Builder navaidNdbEmissionType(String navaidNdbEmissionType) {
      this.navaidNdbEmissionType = navaidNdbEmissionType;
      return this;
    }

    public Builder ndbVoice(String ndbVoice) {
      this.ndbVoice = ndbVoice;
      return this;
    }

    public Builder isBfoRequired(Boolean isBfoRequired) {
      this.isBfoRequired = isBfoRequired;
      return this;
    }

    public Builder ndbNavaidCoverage(String ndbNavaidCoverage) {
      this.ndbNavaidCoverage = ndbNavaidCoverage;
      return this;
    }

    public Builder ndbNavaidIfMarker(String ndbNavaidIfMarker) {
      this.ndbNavaidIfMarker = ndbNavaidIfMarker;
      return this;
    }

    public Builder ndbNavaidType(String ndbNavaidType) {
      this.ndbNavaidType = ndbNavaidType;
      return this;
    }

    public Builder ndbNavaidWeatherInfo(String ndbNavaidWeatherInfo) {
      this.ndbNavaidWeatherInfo = ndbNavaidWeatherInfo;
      return this;
    }

    public Builder isNoVoice(Boolean isNoVoice) {
      this.isNoVoice = isNoVoice;
      return this;
    }

    public ArincNdbNavaid build() {
      return new ArincNdbNavaid(this);
    }
  }
}
