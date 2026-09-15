package org.mitre.tdp.boogie.dafif.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.dafif.model.enums.SpecialUseAirspaceType;

/**
 * Immutable contents of a DAFIF 8.1 SUAS_PAR.TXT record.
 */
public final class DafifSuasParent implements DafifModel {

  /**
   * SUAS_IDENT: {@link org.mitre.tdp.boogie.dafif.v81.field.SuasIdentification}.
   */
  private final String suasIdentification;

  /**
   * SECTOR: {@link org.mitre.tdp.boogie.dafif.v81.field.Sector}.
   */
  private final String sector;

  /**
   * TYPE: {@link org.mitre.tdp.boogie.dafif.v81.field.SpecialUseAirspaceType}.
   */
  private final SpecialUseAirspaceType specialUseAirspaceType;

  /**
   * NAME: {@link org.mitre.tdp.boogie.dafif.v81.field.Name}.
   */
  private final String name;

  /**
   * ICAO: {@link org.mitre.tdp.boogie.dafif.v81.field.IcaoCode}.
   */
  private final String icaoCode;

  /**
   * CON_AGCY: {@link org.mitre.tdp.boogie.dafif.v81.field.ControllingAuthority}.
   */
  private final String controllingAuthority;

  /**
   * LOC_HDATUM: {@link org.mitre.tdp.boogie.dafif.v81.field.LocalHorizontalDatum}.
   */
  private final String localHorizontalDatum;

  /**
   * WGS_DATUM: {@link org.mitre.tdp.boogie.dafif.v81.field.GeodeticDatum}.
   */
  private final String geodeticDatum;

  /**
   * COMM_NAME: {@link org.mitre.tdp.boogie.dafif.v81.field.CommunicationName}.
   */
  private final String communicationName;

  /**
   * FREQ1: {@link org.mitre.tdp.boogie.dafif.v81.field.CommunicationsFrequency}.
   */
  private final String communicationsFrequency1;

  /**
   * FREQ2: {@link org.mitre.tdp.boogie.dafif.v81.field.CommunicationsFrequency}.
   */
  private final String communicationsFrequency2;

  /**
   * LEVEL: {@link org.mitre.tdp.boogie.dafif.v81.field.Level}.
   */
  private final String level;

  /**
   * UPPER_ALT: {@link org.mitre.tdp.boogie.dafif.v81.field.UpperAltitude}.
   */
  private final String upperAltitude;

  /**
   * LOWER_ALT: {@link org.mitre.tdp.boogie.dafif.v81.field.LowerAltitude}.
   */
  private final String lowerAltitude;

  /**
   * EFF_TIMES: {@link org.mitre.tdp.boogie.dafif.v81.field.SuasEffectiveTimes}.
   */
  private final String suasEffectiveTimes;

  /**
   * WX: {@link org.mitre.tdp.boogie.dafif.v81.field.SuasWeather}.
   */
  private final String suasWeather;

  /**
   * CYCLE_DATE: {@link org.mitre.tdp.boogie.dafif.v81.field.CycleDate}.
   */
  private final Integer cycleDate;

  /**
   * EFF_DATE: {@link org.mitre.tdp.boogie.dafif.v81.field.EffectiveDate}.
   */
  private final String effectiveDate;

  public DafifSuasParent(Builder builder) {
    this.suasIdentification = builder.suasIdentification;
    this.sector = builder.sector;
    this.specialUseAirspaceType = builder.specialUseAirspaceType;
    this.name = builder.name;
    this.icaoCode = builder.icaoCode;
    this.controllingAuthority = builder.controllingAuthority;
    this.localHorizontalDatum = builder.localHorizontalDatum;
    this.geodeticDatum = builder.geodeticDatum;
    this.communicationName = builder.communicationName;
    this.communicationsFrequency1 = builder.communicationsFrequency1;
    this.communicationsFrequency2 = builder.communicationsFrequency2;
    this.level = builder.level;
    this.upperAltitude = builder.upperAltitude;
    this.lowerAltitude = builder.lowerAltitude;
    this.suasEffectiveTimes = builder.suasEffectiveTimes;
    this.suasWeather = builder.suasWeather;
    this.cycleDate = builder.cycleDate;
    this.effectiveDate = builder.effectiveDate;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .suasIdentification(suasIdentification)
        .sector(sector)
        .specialUseAirspaceType(specialUseAirspaceType)
        .name(name)
        .icaoCode(icaoCode)
        .controllingAuthority(controllingAuthority)
        .localHorizontalDatum(localHorizontalDatum)
        .geodeticDatum(geodeticDatum)
        .communicationName(communicationName)
        .communicationsFrequency1(communicationsFrequency1)
        .communicationsFrequency2(communicationsFrequency2)
        .level(level)
        .upperAltitude(upperAltitude)
        .lowerAltitude(lowerAltitude)
        .suasEffectiveTimes(suasEffectiveTimes)
        .suasWeather(suasWeather)
        .cycleDate(cycleDate)
        .effectiveDate(effectiveDate);
  }

  public String suasIdentification() {
    return suasIdentification;
  }

  public Optional<String> sector() {
    return Optional.ofNullable(sector);
  }

  public SpecialUseAirspaceType specialUseAirspaceType() {
    return specialUseAirspaceType;
  }

  public Optional<String> name() {
    return Optional.ofNullable(name);
  }

  public String icaoCode() {
    return icaoCode;
  }

  public Optional<String> controllingAuthority() {
    return Optional.ofNullable(controllingAuthority);
  }

  public Optional<String> localHorizontalDatum() {
    return Optional.ofNullable(localHorizontalDatum);
  }

  public String geodeticDatum() {
    return geodeticDatum;
  }

  public Optional<String> communicationName() {
    return Optional.ofNullable(communicationName);
  }

  public Optional<String> communicationsFrequency1() {
    return Optional.ofNullable(communicationsFrequency1);
  }

  public Optional<String> communicationsFrequency2() {
    return Optional.ofNullable(communicationsFrequency2);
  }

  public String level() {
    return level;
  }

  public String upperAltitude() {
    return upperAltitude;
  }

  public String lowerAltitude() {
    return lowerAltitude;
  }

  public Optional<String> suasEffectiveTimes() {
    return Optional.ofNullable(suasEffectiveTimes);
  }

  public Optional<String> suasWeather() {
    return Optional.ofNullable(suasWeather);
  }

  public Integer cycleDate() {
    return cycleDate;
  }

  public String effectiveDate() {
    return effectiveDate;
  }

  @Override
  public DafifFileType getFileType() {
    return DafifFileType.SUAS_PARENT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DafifSuasParent that = (DafifSuasParent) o;
    return Objects.equals(suasIdentification, that.suasIdentification)
        && Objects.equals(sector, that.sector)
        && Objects.equals(specialUseAirspaceType, that.specialUseAirspaceType)
        && Objects.equals(name, that.name)
        && Objects.equals(icaoCode, that.icaoCode)
        && Objects.equals(controllingAuthority, that.controllingAuthority)
        && Objects.equals(localHorizontalDatum, that.localHorizontalDatum)
        && Objects.equals(geodeticDatum, that.geodeticDatum)
        && Objects.equals(communicationName, that.communicationName)
        && Objects.equals(communicationsFrequency1, that.communicationsFrequency1)
        && Objects.equals(communicationsFrequency2, that.communicationsFrequency2)
        && Objects.equals(level, that.level)
        && Objects.equals(upperAltitude, that.upperAltitude)
        && Objects.equals(lowerAltitude, that.lowerAltitude)
        && Objects.equals(suasEffectiveTimes, that.suasEffectiveTimes)
        && Objects.equals(suasWeather, that.suasWeather)
        && Objects.equals(cycleDate, that.cycleDate)
        && Objects.equals(effectiveDate, that.effectiveDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        suasIdentification,
        sector,
        specialUseAirspaceType,
        name,
        icaoCode,
        controllingAuthority,
        localHorizontalDatum,
        geodeticDatum,
        communicationName,
        communicationsFrequency1,
        communicationsFrequency2,
        level,
        upperAltitude,
        lowerAltitude,
        suasEffectiveTimes,
        suasWeather,
        cycleDate,
        effectiveDate);
  }

  @Override
  public String toString() {
    return "DafifSuasParent{"
        + "suasIdentification=" + suasIdentification
        + ", sector=" + sector
        + ", specialUseAirspaceType=" + specialUseAirspaceType
        + ", name=" + name
        + ", icaoCode=" + icaoCode
        + ", controllingAuthority=" + controllingAuthority
        + ", localHorizontalDatum=" + localHorizontalDatum
        + ", geodeticDatum=" + geodeticDatum
        + ", communicationName=" + communicationName
        + ", communicationsFrequency1=" + communicationsFrequency1
        + ", communicationsFrequency2=" + communicationsFrequency2
        + ", level=" + level
        + ", upperAltitude=" + upperAltitude
        + ", lowerAltitude=" + lowerAltitude
        + ", suasEffectiveTimes=" + suasEffectiveTimes
        + ", suasWeather=" + suasWeather
        + ", cycleDate=" + cycleDate
        + ", effectiveDate=" + effectiveDate
        + "}";
  }

  public static final class Builder {
    private String suasIdentification;
    private String sector;
    private SpecialUseAirspaceType specialUseAirspaceType;
    private String name;
    private String icaoCode;
    private String controllingAuthority;
    private String localHorizontalDatum;
    private String geodeticDatum;
    private String communicationName;
    private String communicationsFrequency1;
    private String communicationsFrequency2;
    private String level;
    private String upperAltitude;
    private String lowerAltitude;
    private String suasEffectiveTimes;
    private String suasWeather;
    private Integer cycleDate;
    private String effectiveDate;

    public Builder suasIdentification(String suasIdentification) {
      this.suasIdentification = suasIdentification;
      return this;
    }

    public Builder sector(String sector) {
      this.sector = sector;
      return this;
    }

    public Builder specialUseAirspaceType(SpecialUseAirspaceType specialUseAirspaceType) {
      this.specialUseAirspaceType = specialUseAirspaceType;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder controllingAuthority(String controllingAuthority) {
      this.controllingAuthority = controllingAuthority;
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

    public Builder communicationName(String communicationName) {
      this.communicationName = communicationName;
      return this;
    }

    public Builder communicationsFrequency1(String communicationsFrequency1) {
      this.communicationsFrequency1 = communicationsFrequency1;
      return this;
    }

    public Builder communicationsFrequency2(String communicationsFrequency2) {
      this.communicationsFrequency2 = communicationsFrequency2;
      return this;
    }

    public Builder level(String level) {
      this.level = level;
      return this;
    }

    public Builder upperAltitude(String upperAltitude) {
      this.upperAltitude = upperAltitude;
      return this;
    }

    public Builder lowerAltitude(String lowerAltitude) {
      this.lowerAltitude = lowerAltitude;
      return this;
    }

    public Builder suasEffectiveTimes(String suasEffectiveTimes) {
      this.suasEffectiveTimes = suasEffectiveTimes;
      return this;
    }

    public Builder suasWeather(String suasWeather) {
      this.suasWeather = suasWeather;
      return this;
    }

    public Builder cycleDate(Integer cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }

    public Builder effectiveDate(String effectiveDate) {
      this.effectiveDate = effectiveDate;
      return this;
    }

    public DafifSuasParent build() {
      return new DafifSuasParent(this);
    }
  }
}
