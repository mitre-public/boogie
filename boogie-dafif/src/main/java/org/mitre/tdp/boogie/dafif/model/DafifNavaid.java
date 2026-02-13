package org.mitre.tdp.boogie.dafif.model;

import org.mitre.tdp.boogie.dafif.v81.field.AssociatedIcaoFaaHostCtryCode;
import org.mitre.tdp.boogie.dafif.v81.field.CoordinatePrecision;
import org.mitre.tdp.boogie.dafif.v81.field.CountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DmeDegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DmeDegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DmeElevation;
import org.mitre.tdp.boogie.dafif.v81.field.DmeGeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DmeGeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.FrequencyProtection;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticDatum;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoRegion;
import org.mitre.tdp.boogie.dafif.v81.field.IlsNavaidElevation;
import org.mitre.tdp.boogie.dafif.v81.field.LocalHorizontalDatum;
import org.mitre.tdp.boogie.dafif.v81.field.MagneticVariation;
import org.mitre.tdp.boogie.dafif.v81.field.Name;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidChannel;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidFrequencyNav;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidKeyCode;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidPower;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidRadioClassCode;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidRange;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidSlavedVariation;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidStatus;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidType;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidUsageCode;
import org.mitre.tdp.boogie.dafif.v81.field.StateProvinceCode;
import org.mitre.tdp.boogie.dafif.v81.field.WAC;

import java.util.Objects;
import java.util.Optional;

public final class DafifNavaid implements DafifModel {

  /**
   * {@link NavaidIdentifier}
   */
  private final String navaidIdentifier;
  /**
   * {@link NavaidType}
   */
  private final Integer navaidType;
  /**
   * {@link CountryCode}
   */
  private final String countryCode;
  /**
   * {@link NavaidKeyCode}
   */
  private final Integer navaidKeyCode;
  /**
   * {@link StateProvinceCode}
   */
  private final Integer stateProvinceCode;
  /**
   * {@link Name}
   */
  private final String name;
  /**
   * {@link IcaoRegion}
   */
  private final String icaoRegion;
  /**
   * {@link WAC}
   */
  private final String wac;
  /**
   * {@link NavaidFrequencyNav}
   */
  private final String navaidFrequencyNav;
  /**
   * {@link NavaidUsageCode}
   */
  private final String navaidUsageCode;
  /**
   * {@link NavaidChannel}
   */
  private final String navaidChannel;
  /**
   * {@link NavaidRadioClassCode}
   */
  private final String navaidRadioClassCode;
  /**
   * {@link FrequencyProtection}
   */
  private final String frequencyProtection;
  /**
   * {@link NavaidPower}
   */
  private final String navaidPower;
  /**
   * {@link NavaidRange}
   */
  private final String navaidRange;
  /**
   * {@link LocalHorizontalDatum}
   */
  private final String localHorizontalDatum;
  /**
   * {@link GeodeticDatum}
   */
  private final String geodeticDatum;
  /**
   * {@link GeodeticLatitude}
   */
  private final String geodeticLatitude;
  /**
   * {@link DegreesLatitude}
   */
  private final Double degreesLatitude;
  /**
   * {@link GeodeticLongitude}
   */
  private final String geodeticLongitude;
  /**
   * {@link DegreesLongitude}
   */
  private final Double degreesLongitude;
  /**
   * {@link NavaidSlavedVariation}
   */
  private final String navaidSlavedVariation;
  /**
   * {@link MagneticVariation}
   */
  private final String magneticVariation;
  /**
   * {@link IlsNavaidElevation}
   */
  private final String ilsNavaidElevation;
  /**
   * {@link DmeGeodeticLatitude}
   */
  private final String dmeGeodeticLatitude;
  /**
   * {@link DmeDegreesLatitude}
   */
  private final Double dmeDegreesLatitude;
  /**
   * {@link DmeGeodeticLongitude}
   */
  private final String dmeGeodeticLongitude;
  /**
   * {@link DmeDegreesLongitude}
   */
  private final Double dmeDegreesLongitude;
  /**
   * {@link DmeElevation}
   */
  private final String dmeElevation;
  /**
   * {@link AssociatedIcaoFaaHostCtryCode}
   */
  private final String associatedIcaoFaaHostCtryCode;
  /**
   * {@link NavaidStatus}
   */
  private final String navaidStatus;
  /**
   * {@link CycleDate}
   */
  private final Integer cycleDate;
  /**
   * {@link CoordinatePrecision}
   */
  private final Integer coordinatePrecision;

  public DafifNavaid(Builder builder) {
    this.navaidIdentifier = builder.navaidIdentifier;
    this.navaidType = builder.navaidType;
    this.countryCode = builder.countryCode;
    this.navaidKeyCode = builder.navaidKeyCode;
    this.stateProvinceCode = builder.stateProvinceCode;
    this.name = builder.name;
    this.icaoRegion = builder.icaoRegion;
    this.wac = builder.wac;
    this.navaidFrequencyNav = builder.navaidFrequencyNav;
    this.navaidUsageCode = builder.navaidUsageCode;
    this.navaidChannel = builder.navaidChannel;
    this.navaidRadioClassCode = builder.navaidRadioClassCode;
    this.frequencyProtection = builder.frequencyProtection;
    this.navaidPower = builder.navaidPower;
    this.navaidRange = builder.navaidRange;
    this.localHorizontalDatum = builder.localHorizontalDatum;
    this.geodeticDatum = builder.geodeticDatum;
    this.geodeticLatitude = builder.geodeticLatitude;
    this.degreesLatitude = builder.degreesLatitude;
    this.geodeticLongitude = builder.geodeticLongitude;
    this.degreesLongitude = builder.degreesLongitude;
    this.navaidSlavedVariation = builder.navaidSlavedVariation;
    this.magneticVariation = builder.magneticVariation;
    this.ilsNavaidElevation = builder.ilsNavaidElevation;
    this.dmeGeodeticLatitude = builder.dmeGeodeticLatitude;
    this.dmeDegreesLatitude = builder.dmeDegreesLatitude;
    this.dmeGeodeticLongitude = builder.dmeGeodeticLongitude;
    this.dmeDegreesLongitude = builder.dmeDegreesLongitude;
    this.dmeElevation = builder.dmeElevation;
    this.associatedIcaoFaaHostCtryCode = builder.associatedIcaoFaaHostCtryCode;
    this.navaidStatus = builder.navaidStatus;
    this.cycleDate = builder.cycleDate;
    this.coordinatePrecision = builder.coordinatePrecision;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    DafifNavaid that = (DafifNavaid) o;
    return Objects.equals(navaidIdentifier, that.navaidIdentifier) && Objects.equals(navaidType, that.navaidType) && Objects.equals(countryCode, that.countryCode) && Objects.equals(navaidKeyCode, that.navaidKeyCode) && Objects.equals(stateProvinceCode, that.stateProvinceCode) && Objects.equals(name, that.name) && Objects.equals(icaoRegion, that.icaoRegion) && Objects.equals(wac, that.wac) && Objects.equals(navaidFrequencyNav, that.navaidFrequencyNav) && Objects.equals(navaidUsageCode, that.navaidUsageCode) && Objects.equals(navaidChannel, that.navaidChannel) && Objects.equals(navaidRadioClassCode, that.navaidRadioClassCode) && Objects.equals(frequencyProtection, that.frequencyProtection) && Objects.equals(navaidPower, that.navaidPower) && Objects.equals(navaidRange, that.navaidRange) && Objects.equals(localHorizontalDatum, that.localHorizontalDatum) && Objects.equals(geodeticDatum, that.geodeticDatum) && Objects.equals(geodeticLatitude, that.geodeticLatitude) && Objects.equals(degreesLatitude, that.degreesLatitude) && Objects.equals(geodeticLongitude, that.geodeticLongitude) && Objects.equals(degreesLongitude, that.degreesLongitude) && Objects.equals(navaidSlavedVariation, that.navaidSlavedVariation) && Objects.equals(magneticVariation, that.magneticVariation) && Objects.equals(ilsNavaidElevation, that.ilsNavaidElevation) && Objects.equals(dmeGeodeticLatitude, that.dmeGeodeticLatitude) && Objects.equals(dmeDegreesLatitude, that.dmeDegreesLatitude) && Objects.equals(dmeGeodeticLongitude, that.dmeGeodeticLongitude) && Objects.equals(dmeDegreesLongitude, that.dmeDegreesLongitude) && Objects.equals(dmeElevation, that.dmeElevation) && Objects.equals(associatedIcaoFaaHostCtryCode, that.associatedIcaoFaaHostCtryCode) && Objects.equals(navaidStatus, that.navaidStatus) && Objects.equals(cycleDate, that.cycleDate) && Objects.equals(coordinatePrecision, that.coordinatePrecision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(navaidIdentifier, navaidType, countryCode, navaidKeyCode, stateProvinceCode, name, icaoRegion, wac, navaidFrequencyNav, navaidUsageCode, navaidChannel, navaidRadioClassCode, frequencyProtection, navaidPower, navaidRange, localHorizontalDatum, geodeticDatum, geodeticLatitude, degreesLatitude, geodeticLongitude, degreesLongitude, navaidSlavedVariation, magneticVariation, ilsNavaidElevation, dmeGeodeticLatitude, dmeDegreesLatitude, dmeGeodeticLongitude, dmeDegreesLongitude, dmeElevation, associatedIcaoFaaHostCtryCode, navaidStatus, cycleDate, coordinatePrecision);
  }

  public String navaidIdentifier() {
    return navaidIdentifier;
  }

  public Integer navaidType() {
    return navaidType;
  }

  public String countryCode() {
    return countryCode;
  }

  public Integer navaidKeyCode() {
    return navaidKeyCode;
  }

  public String icaoRegion() {
    return icaoRegion;
  }

  public String wac() {
    return wac;
  }

  public String navaidUsageCode() {
    return navaidUsageCode;
  }

  public String navaidRadioClassCode() {
    return navaidRadioClassCode;
  }

  public String navaidPower() {
    return navaidPower;
  }

  public String navaidRange() {
    return navaidRange;
  }

  public String magneticVariation() {
    return magneticVariation;
  }

  public String ilsNavaidElevation() {
    return ilsNavaidElevation;
  }

  public String dmeElevation() {
    return dmeElevation;
  }

  public Integer cycleDate() {
    return cycleDate;
  }

  public String navaidStatus() {
    return navaidStatus;
  }

  public Optional<Integer> stateProvinceCode() {
    return Optional.ofNullable(stateProvinceCode);
  }

  public Optional<String> name() {
    return Optional.ofNullable(name);
  }

  public Optional<String> navaidFrequencyNav() {
    return Optional.ofNullable(navaidFrequencyNav);
  }

  public Optional<String> navaidChannel() {
    return Optional.ofNullable(navaidChannel);
  }

  public Optional<String> frequencyProtection() {
    return Optional.ofNullable(frequencyProtection);
  }

  public Optional<String> localHorizontalDatum() {
    return Optional.ofNullable(localHorizontalDatum);
  }

  public Optional<String> geodeticDatum() {
    return Optional.ofNullable(geodeticDatum);
  }

  public Optional<String> geodeticLatitude() {
    return Optional.ofNullable(geodeticLatitude);
  }

  public Optional<Double> degreesLatitude() {
    return Optional.ofNullable(degreesLatitude);
  }

  public Optional<String> geodeticLongitude() {
    return Optional.ofNullable(geodeticLongitude);
  }

  public Optional<Double> degreesLongitude() {
    return Optional.ofNullable(degreesLongitude);
  }

  public Optional<String> navaidSlavedVariation() {
    return Optional.ofNullable(navaidSlavedVariation);
  }

  public Optional<String> dmeGeodeticLatitude() {
    return Optional.ofNullable(dmeGeodeticLatitude);
  }

  public Optional<Double> dmeDegreesLatitude() {
    return Optional.ofNullable(dmeDegreesLatitude);
  }

  public Optional<String> dmeGeodeticLongitude() {
    return Optional.ofNullable(dmeGeodeticLongitude);
  }

  public Optional<Double> dmeDegreesLongitude() {
    return Optional.ofNullable(dmeDegreesLongitude);
  }

  public Optional<String> associatedIcaoFaaHostCtryCode() {
    return Optional.ofNullable(associatedIcaoFaaHostCtryCode);
  }

  public Optional<Integer> coordinatePrecision() {
    return Optional.ofNullable(coordinatePrecision);
  }

  @Override
  public DafifFileType getFileType() {
    return DafifFileType.NAVAID;
  }

  public static final class Builder {
    private String navaidIdentifier;
    private Integer navaidType;
    private String countryCode;
    private Integer navaidKeyCode;
    private Integer stateProvinceCode;
    private String name;
    private String icaoRegion;
    private String wac;
    private String navaidFrequencyNav;
    private String navaidUsageCode;
    private String navaidChannel;
    private String navaidRadioClassCode;
    private String frequencyProtection;
    private String navaidPower;
    private String navaidRange;
    private String localHorizontalDatum;
    private String geodeticDatum;
    private String geodeticLatitude;
    private Double degreesLatitude;
    private String geodeticLongitude;
    private Double degreesLongitude;
    private String navaidSlavedVariation;
    private String magneticVariation;
    private String ilsNavaidElevation;
    private String dmeGeodeticLatitude;
    private Double dmeDegreesLatitude;
    private String dmeGeodeticLongitude;
    private Double dmeDegreesLongitude;
    private String dmeElevation;
    private String associatedIcaoFaaHostCtryCode;
    private String navaidStatus;
    private Integer cycleDate;
    private Integer coordinatePrecision;

    public Builder navaidIdentifier(String navaidIdentifier) {
      this.navaidIdentifier = navaidIdentifier;
      return this;
    }

    public Builder navaidType(Integer navaidType) {
      this.navaidType = navaidType;
      return this;
    }

    public Builder countryCode(String countryCode) {
      this.countryCode = countryCode;
      return this;
    }

    public Builder navaidKeyCode(Integer navaidKeyCode) {
      this.navaidKeyCode = navaidKeyCode;
      return this;
    }

    public Builder stateProvinceCode(Integer stateProvinceCode) {
      this.stateProvinceCode = stateProvinceCode;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder icaoRegion(String icaoRegion) {
      this.icaoRegion = icaoRegion;
      return this;
    }

    public Builder wac(String wac) {
      this.wac = wac;
      return this;
    }

    public Builder navaidFrequencyNav(String navaidFrequencyNav) {
      this.navaidFrequencyNav = navaidFrequencyNav;
      return this;
    }

    public Builder navaidUsageCode(String navaidUsageCode) {
      this.navaidUsageCode = navaidUsageCode;
      return this;
    }

    public Builder navaidChannel(String navaidChannel) {
      this.navaidChannel = navaidChannel;
      return this;
    }

    public Builder navaidRadioClassCode(String navaidRadioClassCode) {
      this.navaidRadioClassCode = navaidRadioClassCode;
      return this;
    }

    public Builder frequencyProtection(String frequencyProtection) {
      this.frequencyProtection = frequencyProtection;
      return this;
    }

    public Builder navaidPower(String navaidPower) {
      this.navaidPower = navaidPower;
      return this;
    }

    public Builder navaidRange(String navaidRange) {
      this.navaidRange = navaidRange;
      return this;
    }

    public Builder localHorizontalDatum(String localHorizontalDatum) {
      this.localHorizontalDatum = localHorizontalDatum;
      return this;
    }

    public Builder geodeticDatum(String geodeticDatum) {
      this.geodeticDatum = geodeticDatum;
      return this;
    }

    public Builder geodeticLatitude(String geodeticLatitude) {
      this.geodeticLatitude = geodeticLatitude;
      return this;
    }

    public Builder degreesLatitude(Double degreesLatitude) {
      this.degreesLatitude = degreesLatitude;
      return this;
    }

    public Builder geodeticLongitude(String geodeticLongitude) {
      this.geodeticLongitude = geodeticLongitude;
      return this;
    }

    public Builder degreesLongitude(Double degreesLongitude) {
      this.degreesLongitude = degreesLongitude;
      return this;
    }

    public Builder navaidSlavedVariation(String navaidSlavedVariation) {
      this.navaidSlavedVariation = navaidSlavedVariation;
      return this;
    }

    public Builder magneticVariation(String magneticVariation) {
      this.magneticVariation = magneticVariation;
      return this;
    }

    public Builder ilsNavaidElevation(String ilsNavaidElevation) {
      this.ilsNavaidElevation = ilsNavaidElevation;
      return this;
    }

    public Builder dmeGeodeticLatitude(String dmeGeodeticLatitude) {
      this.dmeGeodeticLatitude = dmeGeodeticLatitude;
      return this;
    }

    public Builder dmeDegreesLatitude(Double dmeDegreesLatitude) {
      this.dmeDegreesLatitude = dmeDegreesLatitude;
      return this;
    }

    public Builder dmeGeodeticLongitude(String dmeGeodeticLongitude) {
      this.dmeGeodeticLongitude = dmeGeodeticLongitude;
      return this;
    }

    public Builder dmeDegreesLongitude(Double dmeDegreesLongitude) {
      this.dmeDegreesLongitude = dmeDegreesLongitude;
      return this;
    }

    public Builder dmeElevation(String dmeElevation) {
      this.dmeElevation = dmeElevation;
      return this;
    }

    public Builder associatedIcaoFaaHostCtryCode(String associatedIcaoFaaHostCtryCode) {
      this.associatedIcaoFaaHostCtryCode = associatedIcaoFaaHostCtryCode;
      return this;
    }

    public Builder navaidStatus(String navaidStatus) {
      this.navaidStatus = navaidStatus;
      return this;
    }

    public Builder cycleDate(Integer cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }

    public Builder coordinatePrecision(Integer coordinatePrecision) {
      this.coordinatePrecision = coordinatePrecision;
      return this;
    }

    public DafifNavaid build() {
      return new DafifNavaid(this);
    }
  }
}
