package org.mitre.tdp.boogie.dafif.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.dafif.model.enums.Derivation;
import org.mitre.tdp.boogie.dafif.model.enums.Shape;
import org.mitre.tdp.boogie.dafif.model.enums.SpecialUseAirspaceType;

/**
 * Immutable contents of a DAFIF 8.1 SUAS.TXT record.
 */
public final class DafifSuasSegment implements DafifModel {

  /**
   * SUAS_IDENT: {@link org.mitre.tdp.boogie.dafif.v81.field.SuasIdentification}.
   */
  private final String suasIdentification;

  /**
   * SECTOR: {@link org.mitre.tdp.boogie.dafif.v81.field.Sector}.
   */
  private final String sector;

  /**
   * SEG_NBR: {@link org.mitre.tdp.boogie.dafif.v81.field.SegmentNumber}.
   */
  private final Integer segmentNumber;

  /**
   * NAME: {@link org.mitre.tdp.boogie.dafif.v81.field.Name}.
   */
  private final String name;

  /**
   * TYPE: {@link org.mitre.tdp.boogie.dafif.v81.field.SpecialUseAirspaceType}.
   */
  private final SpecialUseAirspaceType specialUseAirspaceType;

  /**
   * ICAO: {@link org.mitre.tdp.boogie.dafif.v81.field.IcaoCode}.
   */
  private final String icaoCode;

  /**
   * SHAP: {@link org.mitre.tdp.boogie.dafif.v81.field.Shape}.
   */
  private final Shape shape;

  /**
   * DERIVATION: {@link org.mitre.tdp.boogie.dafif.v81.field.Derivation}.
   */
  private final Derivation derivation;

  /**
   * WGS_LAT1: {@link org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude}.
   */
  private final String geodeticLatitude1;

  /**
   * WGS_DLAT1: {@link org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude}.
   */
  private final Double latitude1;

  /**
   * WGS_LONG1: {@link org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude}.
   */
  private final String geodeticLongitude1;

  /**
   * WGS_DLONG1: {@link org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude}.
   */
  private final Double longitude1;

  /**
   * WGS_LAT2: {@link org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude}.
   */
  private final String geodeticLatitude2;

  /**
   * WGS_DLAT2: {@link org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude}.
   */
  private final Double latitude2;

  /**
   * WGS_LONG2: {@link org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude}.
   */
  private final String geodeticLongitude2;

  /**
   * WGS_DLONG2: {@link org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude}.
   */
  private final Double longitude2;

  /**
   * WGS_LAT0: {@link org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude}.
   */
  private final String geodeticLatitude0;

  /**
   * WGS_DLAT0: {@link org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude}.
   */
  private final Double latitude0;

  /**
   * WGS_LONG0: {@link org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude}.
   */
  private final String geodeticLongitude0;

  /**
   * WGS_DLONG0: {@link org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude}.
   */
  private final Double longitude0;

  /**
   * RADIUS1: {@link org.mitre.tdp.boogie.dafif.v81.field.Radius}.
   */
  private final Double radius1;

  /**
   * RADIUS2: {@link org.mitre.tdp.boogie.dafif.v81.field.Radius}.
   */
  private final Double radius2;

  /**
   * BEARING1: {@link org.mitre.tdp.boogie.dafif.v81.field.Bearing}.
   */
  private final Double bearing1;

  /**
   * BEARING2: {@link org.mitre.tdp.boogie.dafif.v81.field.Bearing}.
   */
  private final Double bearing2;

  /**
   * NAV_IDENT: {@link org.mitre.tdp.boogie.dafif.v81.field.NavaidIdentifier}.
   */
  private final String navaidIdentifier;

  /**
   * NAV_TYPE: {@link org.mitre.tdp.boogie.dafif.v81.field.NavaidType}.
   */
  private final Integer navaidType;

  /**
   * NAV_CTRY: {@link org.mitre.tdp.boogie.dafif.v81.field.CountryCode}.
   */
  private final String navaidCountryCode;

  /**
   * NAV_KEY_CD: {@link org.mitre.tdp.boogie.dafif.v81.field.NavaidKeyCode}.
   */
  private final Integer navaidKeyCode;

  /**
   * CYCLE_DATE: {@link org.mitre.tdp.boogie.dafif.v81.field.CycleDate}.
   */
  private final Integer cycleDate;

  public DafifSuasSegment(Builder builder) {
    this.suasIdentification = builder.suasIdentification;
    this.sector = builder.sector;
    this.segmentNumber = builder.segmentNumber;
    this.name = builder.name;
    this.specialUseAirspaceType = builder.specialUseAirspaceType;
    this.icaoCode = builder.icaoCode;
    this.shape = builder.shape;
    this.derivation = builder.derivation;
    this.geodeticLatitude1 = builder.geodeticLatitude1;
    this.latitude1 = builder.latitude1;
    this.geodeticLongitude1 = builder.geodeticLongitude1;
    this.longitude1 = builder.longitude1;
    this.geodeticLatitude2 = builder.geodeticLatitude2;
    this.latitude2 = builder.latitude2;
    this.geodeticLongitude2 = builder.geodeticLongitude2;
    this.longitude2 = builder.longitude2;
    this.geodeticLatitude0 = builder.geodeticLatitude0;
    this.latitude0 = builder.latitude0;
    this.geodeticLongitude0 = builder.geodeticLongitude0;
    this.longitude0 = builder.longitude0;
    this.radius1 = builder.radius1;
    this.radius2 = builder.radius2;
    this.bearing1 = builder.bearing1;
    this.bearing2 = builder.bearing2;
    this.navaidIdentifier = builder.navaidIdentifier;
    this.navaidType = builder.navaidType;
    this.navaidCountryCode = builder.navaidCountryCode;
    this.navaidKeyCode = builder.navaidKeyCode;
    this.cycleDate = builder.cycleDate;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .suasIdentification(suasIdentification)
        .sector(sector)
        .segmentNumber(segmentNumber)
        .name(name)
        .specialUseAirspaceType(specialUseAirspaceType)
        .icaoCode(icaoCode)
        .shape(shape)
        .derivation(derivation)
        .geodeticLatitude1(geodeticLatitude1)
        .latitude1(latitude1)
        .geodeticLongitude1(geodeticLongitude1)
        .longitude1(longitude1)
        .geodeticLatitude2(geodeticLatitude2)
        .latitude2(latitude2)
        .geodeticLongitude2(geodeticLongitude2)
        .longitude2(longitude2)
        .geodeticLatitude0(geodeticLatitude0)
        .latitude0(latitude0)
        .geodeticLongitude0(geodeticLongitude0)
        .longitude0(longitude0)
        .radius1(radius1)
        .radius2(radius2)
        .bearing1(bearing1)
        .bearing2(bearing2)
        .navaidIdentifier(navaidIdentifier)
        .navaidType(navaidType)
        .navaidCountryCode(navaidCountryCode)
        .navaidKeyCode(navaidKeyCode)
        .cycleDate(cycleDate);
  }

  public String suasIdentification() {
    return suasIdentification;
  }

  public Optional<String> sector() {
    return Optional.ofNullable(sector);
  }

  public Integer segmentNumber() {
    return segmentNumber;
  }

  public Optional<String> name() {
    return Optional.ofNullable(name);
  }

  public SpecialUseAirspaceType specialUseAirspaceType() {
    return specialUseAirspaceType;
  }

  public String icaoCode() {
    return icaoCode;
  }

  public Shape shape() {
    return shape;
  }

  public Optional<Derivation> derivation() {
    return Optional.ofNullable(derivation);
  }

  public Optional<String> geodeticLatitude1() {
    return Optional.ofNullable(geodeticLatitude1);
  }

  public Optional<Double> latitude1() {
    return Optional.ofNullable(latitude1);
  }

  public Optional<String> geodeticLongitude1() {
    return Optional.ofNullable(geodeticLongitude1);
  }

  public Optional<Double> longitude1() {
    return Optional.ofNullable(longitude1);
  }

  public Optional<String> geodeticLatitude2() {
    return Optional.ofNullable(geodeticLatitude2);
  }

  public Optional<Double> latitude2() {
    return Optional.ofNullable(latitude2);
  }

  public Optional<String> geodeticLongitude2() {
    return Optional.ofNullable(geodeticLongitude2);
  }

  public Optional<Double> longitude2() {
    return Optional.ofNullable(longitude2);
  }

  public Optional<String> geodeticLatitude0() {
    return Optional.ofNullable(geodeticLatitude0);
  }

  public Optional<Double> latitude0() {
    return Optional.ofNullable(latitude0);
  }

  public Optional<String> geodeticLongitude0() {
    return Optional.ofNullable(geodeticLongitude0);
  }

  public Optional<Double> longitude0() {
    return Optional.ofNullable(longitude0);
  }

  public Optional<Double> radius1() {
    return Optional.ofNullable(radius1);
  }

  public Optional<Double> radius2() {
    return Optional.ofNullable(radius2);
  }

  public Optional<Double> bearing1() {
    return Optional.ofNullable(bearing1);
  }

  public Optional<Double> bearing2() {
    return Optional.ofNullable(bearing2);
  }

  public Optional<String> navaidIdentifier() {
    return Optional.ofNullable(navaidIdentifier);
  }

  public Optional<Integer> navaidType() {
    return Optional.ofNullable(navaidType);
  }

  public Optional<String> navaidCountryCode() {
    return Optional.ofNullable(navaidCountryCode);
  }

  public Optional<Integer> navaidKeyCode() {
    return Optional.ofNullable(navaidKeyCode);
  }

  public Integer cycleDate() {
    return cycleDate;
  }

  @Override
  public DafifFileType getFileType() {
    return DafifFileType.SUAS_SEGMENT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DafifSuasSegment that = (DafifSuasSegment) o;
    return Objects.equals(suasIdentification, that.suasIdentification)
        && Objects.equals(sector, that.sector)
        && Objects.equals(segmentNumber, that.segmentNumber)
        && Objects.equals(name, that.name)
        && Objects.equals(specialUseAirspaceType, that.specialUseAirspaceType)
        && Objects.equals(icaoCode, that.icaoCode)
        && Objects.equals(shape, that.shape)
        && Objects.equals(derivation, that.derivation)
        && Objects.equals(geodeticLatitude1, that.geodeticLatitude1)
        && Objects.equals(latitude1, that.latitude1)
        && Objects.equals(geodeticLongitude1, that.geodeticLongitude1)
        && Objects.equals(longitude1, that.longitude1)
        && Objects.equals(geodeticLatitude2, that.geodeticLatitude2)
        && Objects.equals(latitude2, that.latitude2)
        && Objects.equals(geodeticLongitude2, that.geodeticLongitude2)
        && Objects.equals(longitude2, that.longitude2)
        && Objects.equals(geodeticLatitude0, that.geodeticLatitude0)
        && Objects.equals(latitude0, that.latitude0)
        && Objects.equals(geodeticLongitude0, that.geodeticLongitude0)
        && Objects.equals(longitude0, that.longitude0)
        && Objects.equals(radius1, that.radius1)
        && Objects.equals(radius2, that.radius2)
        && Objects.equals(bearing1, that.bearing1)
        && Objects.equals(bearing2, that.bearing2)
        && Objects.equals(navaidIdentifier, that.navaidIdentifier)
        && Objects.equals(navaidType, that.navaidType)
        && Objects.equals(navaidCountryCode, that.navaidCountryCode)
        && Objects.equals(navaidKeyCode, that.navaidKeyCode)
        && Objects.equals(cycleDate, that.cycleDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        suasIdentification,
        sector,
        segmentNumber,
        name,
        specialUseAirspaceType,
        icaoCode,
        shape,
        derivation,
        geodeticLatitude1,
        latitude1,
        geodeticLongitude1,
        longitude1,
        geodeticLatitude2,
        latitude2,
        geodeticLongitude2,
        longitude2,
        geodeticLatitude0,
        latitude0,
        geodeticLongitude0,
        longitude0,
        radius1,
        radius2,
        bearing1,
        bearing2,
        navaidIdentifier,
        navaidType,
        navaidCountryCode,
        navaidKeyCode,
        cycleDate);
  }

  @Override
  public String toString() {
    return "DafifSuasSegment{"
        + "suasIdentification=" + suasIdentification
        + ", sector=" + sector
        + ", segmentNumber=" + segmentNumber
        + ", name=" + name
        + ", specialUseAirspaceType=" + specialUseAirspaceType
        + ", icaoCode=" + icaoCode
        + ", shape=" + shape
        + ", derivation=" + derivation
        + ", geodeticLatitude1=" + geodeticLatitude1
        + ", latitude1=" + latitude1
        + ", geodeticLongitude1=" + geodeticLongitude1
        + ", longitude1=" + longitude1
        + ", geodeticLatitude2=" + geodeticLatitude2
        + ", latitude2=" + latitude2
        + ", geodeticLongitude2=" + geodeticLongitude2
        + ", longitude2=" + longitude2
        + ", geodeticLatitude0=" + geodeticLatitude0
        + ", latitude0=" + latitude0
        + ", geodeticLongitude0=" + geodeticLongitude0
        + ", longitude0=" + longitude0
        + ", radius1=" + radius1
        + ", radius2=" + radius2
        + ", bearing1=" + bearing1
        + ", bearing2=" + bearing2
        + ", navaidIdentifier=" + navaidIdentifier
        + ", navaidType=" + navaidType
        + ", navaidCountryCode=" + navaidCountryCode
        + ", navaidKeyCode=" + navaidKeyCode
        + ", cycleDate=" + cycleDate
        + "}";
  }

  public static final class Builder {
    private String suasIdentification;
    private String sector;
    private Integer segmentNumber;
    private String name;
    private SpecialUseAirspaceType specialUseAirspaceType;
    private String icaoCode;
    private Shape shape;
    private Derivation derivation;
    private String geodeticLatitude1;
    private Double latitude1;
    private String geodeticLongitude1;
    private Double longitude1;
    private String geodeticLatitude2;
    private Double latitude2;
    private String geodeticLongitude2;
    private Double longitude2;
    private String geodeticLatitude0;
    private Double latitude0;
    private String geodeticLongitude0;
    private Double longitude0;
    private Double radius1;
    private Double radius2;
    private Double bearing1;
    private Double bearing2;
    private String navaidIdentifier;
    private Integer navaidType;
    private String navaidCountryCode;
    private Integer navaidKeyCode;
    private Integer cycleDate;

    public Builder suasIdentification(String suasIdentification) {
      this.suasIdentification = suasIdentification;
      return this;
    }

    public Builder sector(String sector) {
      this.sector = sector;
      return this;
    }

    public Builder segmentNumber(Integer segmentNumber) {
      this.segmentNumber = segmentNumber;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder specialUseAirspaceType(SpecialUseAirspaceType specialUseAirspaceType) {
      this.specialUseAirspaceType = specialUseAirspaceType;
      return this;
    }

    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder shape(Shape shape) {
      this.shape = shape;
      return this;
    }

    public Builder derivation(Derivation derivation) {
      this.derivation = derivation;
      return this;
    }

    public Builder geodeticLatitude1(String geodeticLatitude1) {
      this.geodeticLatitude1 = geodeticLatitude1;
      return this;
    }

    public Builder latitude1(Double latitude1) {
      this.latitude1 = latitude1;
      return this;
    }

    public Builder geodeticLongitude1(String geodeticLongitude1) {
      this.geodeticLongitude1 = geodeticLongitude1;
      return this;
    }

    public Builder longitude1(Double longitude1) {
      this.longitude1 = longitude1;
      return this;
    }

    public Builder geodeticLatitude2(String geodeticLatitude2) {
      this.geodeticLatitude2 = geodeticLatitude2;
      return this;
    }

    public Builder latitude2(Double latitude2) {
      this.latitude2 = latitude2;
      return this;
    }

    public Builder geodeticLongitude2(String geodeticLongitude2) {
      this.geodeticLongitude2 = geodeticLongitude2;
      return this;
    }

    public Builder longitude2(Double longitude2) {
      this.longitude2 = longitude2;
      return this;
    }

    public Builder geodeticLatitude0(String geodeticLatitude0) {
      this.geodeticLatitude0 = geodeticLatitude0;
      return this;
    }

    public Builder latitude0(Double latitude0) {
      this.latitude0 = latitude0;
      return this;
    }

    public Builder geodeticLongitude0(String geodeticLongitude0) {
      this.geodeticLongitude0 = geodeticLongitude0;
      return this;
    }

    public Builder longitude0(Double longitude0) {
      this.longitude0 = longitude0;
      return this;
    }

    public Builder radius1(Double radius1) {
      this.radius1 = radius1;
      return this;
    }

    public Builder radius2(Double radius2) {
      this.radius2 = radius2;
      return this;
    }

    public Builder bearing1(Double bearing1) {
      this.bearing1 = bearing1;
      return this;
    }

    public Builder bearing2(Double bearing2) {
      this.bearing2 = bearing2;
      return this;
    }

    public Builder navaidIdentifier(String navaidIdentifier) {
      this.navaidIdentifier = navaidIdentifier;
      return this;
    }

    public Builder navaidType(Integer navaidType) {
      this.navaidType = navaidType;
      return this;
    }

    public Builder navaidCountryCode(String navaidCountryCode) {
      this.navaidCountryCode = navaidCountryCode;
      return this;
    }

    public Builder navaidKeyCode(Integer navaidKeyCode) {
      this.navaidKeyCode = navaidKeyCode;
      return this;
    }

    public Builder cycleDate(Integer cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }

    public DafifSuasSegment build() {
      return new DafifSuasSegment(this);
    }
  }
}
