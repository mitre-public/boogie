package org.mitre.boogie.xml.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.FigureOfMerit;
import org.mitre.boogie.xml.model.fields.FreqUnitOfMeasure;
import org.mitre.boogie.xml.model.fields.IlsDmeLocation;
import org.mitre.boogie.xml.model.fields.NavaidSynchronization;
import org.mitre.boogie.xml.model.fields.NavaidVoice;
import org.mitre.boogie.xml.model.fields.NavaidWeatherInfo;
import org.mitre.boogie.xml.model.fields.VhfNavaidCoverage;
import org.mitre.boogie.xml.model.fields.VorRangePower;
import org.mitre.tdp.boogie.MagneticVariation;

public final class ArincVhfNavaid {
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;
  private final ArincPointInfo pointInfo;
  private final String portRef;
  private final String dmeTacanRef;
  // Discriminator: VOR, DME, TACAN, MILITARY_TACAN
  private final String navaidSubType;
  // Navaid fields
  private final Integer elevation;
  private final Boolean isVFRCheckpoint;
  // Frequency (flattened from Frequency)
  private final Double frequencyValue;
  /**
   * See {@link FreqUnitOfMeasure}
   */
  private final String freqUnitOfMeasure;
  private final MagneticVariation stationDeclination;
  // Vor fields
  /**
   * See {@link FigureOfMerit}
   */
  private final String figureOfMerit;
  private final Long frequencyProtection;
  /**
   * See {@link NavaidSynchronization}
   */
  private final String navaidSynchronization;
  /**
   * See {@link NavaidVoice}
   */
  private final String vorVoice;
  /**
   * See {@link VorRangePower}
   */
  private final String vorRangePower;
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
  // DmeTacan-specific fields
  private final Double ilsDmeBias;
  private final Boolean isIlsComponent;
  /**
   * See {@link IlsDmeLocation}
   */
  private final String ilsDmeLocation;
  private final String dmeExpandedServiceVolume;
  private final String dmeOperationalServiceVolume;
  private final Boolean isRouteInappropriateDme;
  private final Boolean isPaired;
  private final Boolean isMlsP;

  private ArincVhfNavaid(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.pointInfo = builder.pointInfo;
    this.portRef = builder.portRef;
    this.dmeTacanRef = builder.dmeTacanRef;
    this.navaidSubType = builder.navaidSubType;
    this.elevation = builder.elevation;
    this.isVFRCheckpoint = builder.isVFRCheckpoint;
    this.frequencyValue = builder.frequencyValue;
    this.freqUnitOfMeasure = builder.freqUnitOfMeasure;
    this.stationDeclination = builder.stationDeclination;
    this.figureOfMerit = builder.figureOfMerit;
    this.frequencyProtection = builder.frequencyProtection;
    this.navaidSynchronization = builder.navaidSynchronization;
    this.vorVoice = builder.vorVoice;
    this.vorRangePower = builder.vorRangePower;
    this.vhfNavaidCoverage = builder.vhfNavaidCoverage;
    this.vhfNavaidWeatherInfo = builder.vhfNavaidWeatherInfo;
    this.isNotCollocated = builder.isNotCollocated;
    this.isBiased = builder.isBiased;
    this.isNoVoice = builder.isNoVoice;
    this.ilsDmeBias = builder.ilsDmeBias;
    this.isIlsComponent = builder.isIlsComponent;
    this.ilsDmeLocation = builder.ilsDmeLocation;
    this.dmeExpandedServiceVolume = builder.dmeExpandedServiceVolume;
    this.dmeOperationalServiceVolume = builder.dmeOperationalServiceVolume;
    this.isRouteInappropriateDme = builder.isRouteInappropriateDme;
    this.isPaired = builder.isPaired;
    this.isMlsP = builder.isMlsP;
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

  public Optional<String> portRef() {
    return Optional.ofNullable(portRef);
  }

  public Optional<String> dmeTacanRef() {
    return Optional.ofNullable(dmeTacanRef);
  }

  public Optional<String> navaidSubType() {
    return Optional.ofNullable(navaidSubType);
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

  public Optional<MagneticVariation> stationDeclination() {
    return Optional.ofNullable(stationDeclination);
  }

  public Optional<FigureOfMerit> figureOfMerit() {
    return Optional.ofNullable(figureOfMerit).filter(FigureOfMerit.VALID::contains).map(FigureOfMerit::valueOf);
  }

  public Optional<Long> frequencyProtection() {
    return Optional.ofNullable(frequencyProtection);
  }

  public Optional<NavaidSynchronization> navaidSynchronization() {
    return Optional.ofNullable(navaidSynchronization).filter(NavaidSynchronization.VALID::contains).map(NavaidSynchronization::valueOf);
  }

  public Optional<NavaidVoice> vorVoice() {
    return Optional.ofNullable(vorVoice).filter(NavaidVoice.VALID::contains).map(NavaidVoice::valueOf);
  }

  public Optional<VorRangePower> vorRangePower() {
    return Optional.ofNullable(vorRangePower).filter(VorRangePower.VALID::contains).map(VorRangePower::valueOf);
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

  public Optional<Double> ilsDmeBias() {
    return Optional.ofNullable(ilsDmeBias);
  }

  public Optional<Boolean> isIlsComponent() {
    return Optional.ofNullable(isIlsComponent);
  }

  public Optional<IlsDmeLocation> ilsDmeLocation() {
    return Optional.ofNullable(ilsDmeLocation).filter(IlsDmeLocation.VALID::contains).map(IlsDmeLocation::valueOf);
  }

  public Optional<String> dmeExpandedServiceVolume() {
    return Optional.ofNullable(dmeExpandedServiceVolume);
  }

  public Optional<String> dmeOperationalServiceVolume() {
    return Optional.ofNullable(dmeOperationalServiceVolume);
  }

  public Optional<Boolean> isRouteInappropriateDme() {
    return Optional.ofNullable(isRouteInappropriateDme);
  }

  public Optional<Boolean> isPaired() {
    return Optional.ofNullable(isPaired);
  }

  public Optional<Boolean> isMlsP() {
    return Optional.ofNullable(isMlsP);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincVhfNavaid that = (ArincVhfNavaid) o;
    return Objects.equals(baseInfo, that.baseInfo) && Objects.equals(recordInfo, that.recordInfo) && Objects.equals(pointInfo, that.pointInfo) && Objects.equals(elevation, that.elevation) && Objects.equals(isVFRCheckpoint, that.isVFRCheckpoint) && Objects.equals(frequencyValue, that.frequencyValue) && Objects.equals(freqUnitOfMeasure, that.freqUnitOfMeasure) && Objects.equals(stationDeclination, that.stationDeclination) && Objects.equals(figureOfMerit, that.figureOfMerit) && Objects.equals(frequencyProtection, that.frequencyProtection) && Objects.equals(navaidSynchronization, that.navaidSynchronization) && Objects.equals(vorVoice, that.vorVoice) && Objects.equals(vorRangePower, that.vorRangePower) && Objects.equals(vhfNavaidCoverage, that.vhfNavaidCoverage) && Objects.equals(vhfNavaidWeatherInfo, that.vhfNavaidWeatherInfo) && Objects.equals(isNotCollocated, that.isNotCollocated) && Objects.equals(isBiased, that.isBiased) && Objects.equals(isNoVoice, that.isNoVoice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, recordInfo, pointInfo, elevation, isVFRCheckpoint, frequencyValue, freqUnitOfMeasure, stationDeclination, figureOfMerit, frequencyProtection, navaidSynchronization, vorVoice, vorRangePower, vhfNavaidCoverage, vhfNavaidWeatherInfo, isNotCollocated, isBiased, isNoVoice);
  }

  @Override
  public String toString() {
    return "ArincVhfNavaid{" +
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
    private String portRef;
    private String dmeTacanRef;
    private String navaidSubType;
    private Integer elevation;
    private Boolean isVFRCheckpoint;
    private Double frequencyValue;
    private String freqUnitOfMeasure;
    private MagneticVariation stationDeclination;
    private String figureOfMerit;
    private Long frequencyProtection;
    private String navaidSynchronization;
    private String vorVoice;
    private String vorRangePower;
    private String vhfNavaidCoverage;
    private String vhfNavaidWeatherInfo;
    private Boolean isNotCollocated;
    private Boolean isBiased;
    private Boolean isNoVoice;
    private Double ilsDmeBias;
    private Boolean isIlsComponent;
    private String ilsDmeLocation;
    private String dmeExpandedServiceVolume;
    private String dmeOperationalServiceVolume;
    private Boolean isRouteInappropriateDme;
    private Boolean isPaired;
    private Boolean isMlsP;

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

    public Builder portRef(String portRef) {
      this.portRef = portRef;
      return this;
    }

    public Builder dmeTacanRef(String dmeTacanRef) {
      this.dmeTacanRef = dmeTacanRef;
      return this;
    }

    public Builder navaidSubType(String navaidSubType) {
      this.navaidSubType = navaidSubType;
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

    public Builder stationDeclination(MagneticVariation stationDeclination) {
      this.stationDeclination = stationDeclination;
      return this;
    }

    public Builder figureOfMerit(String figureOfMerit) {
      this.figureOfMerit = figureOfMerit;
      return this;
    }

    public Builder frequencyProtection(Long frequencyProtection) {
      this.frequencyProtection = frequencyProtection;
      return this;
    }

    public Builder navaidSynchronization(String navaidSynchronization) {
      this.navaidSynchronization = navaidSynchronization;
      return this;
    }

    public Builder vorVoice(String vorVoice) {
      this.vorVoice = vorVoice;
      return this;
    }

    public Builder vorRangePower(String vorRangePower) {
      this.vorRangePower = vorRangePower;
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

    public Builder ilsDmeBias(Double ilsDmeBias) {
      this.ilsDmeBias = ilsDmeBias;
      return this;
    }

    public Builder isIlsComponent(Boolean isIlsComponent) {
      this.isIlsComponent = isIlsComponent;
      return this;
    }

    public Builder ilsDmeLocation(String ilsDmeLocation) {
      this.ilsDmeLocation = ilsDmeLocation;
      return this;
    }

    public Builder dmeExpandedServiceVolume(String dmeExpandedServiceVolume) {
      this.dmeExpandedServiceVolume = dmeExpandedServiceVolume;
      return this;
    }

    public Builder dmeOperationalServiceVolume(String dmeOperationalServiceVolume) {
      this.dmeOperationalServiceVolume = dmeOperationalServiceVolume;
      return this;
    }

    public Builder isRouteInappropriateDme(Boolean isRouteInappropriateDme) {
      this.isRouteInappropriateDme = isRouteInappropriateDme;
      return this;
    }

    public Builder isPaired(Boolean isPaired) {
      this.isPaired = isPaired;
      return this;
    }

    public Builder isMlsP(Boolean isMlsP) {
      this.isMlsP = isMlsP;
      return this;
    }

    public ArincVhfNavaid build() {
      return new ArincVhfNavaid(this);
    }
  }
}
