package org.mitre.tdp.boogie.dafif.model;

import org.mitre.tdp.boogie.dafif.v81.field.WaypointIdentifierWptIdent;
import org.mitre.tdp.boogie.dafif.v81.field.CountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.StateProvinceCode;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointPointNavaidFlag;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointType;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointDescriptionName;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointUsageCode;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointBearing;
import org.mitre.tdp.boogie.dafif.v81.field.Distance;
import org.mitre.tdp.boogie.dafif.v81.field.WAC;
import org.mitre.tdp.boogie.dafif.v81.field.LocalHorizontalDatum;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticDatum;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.MagneticVariation;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidType;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidKeyCode;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointRunwayIdent;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointRwyIcao;
import org.mitre.tdp.boogie.dafif.v81.field.CoordinatePrecision;

import java.util.Objects;

public final class DafifWaypoint {
  /**
   * {@link WaypointIdentifierWptIdent}
   */
  private final String waypointIdentifierWptIdent;
  /**
   * {@link CountryCode}
   */
  private final String countryCode;
  /**
   * {@link StateProvinceCode}
   */
  private final Integer stateProvinceCode;
  /**
   * {@link WaypointPointNavaidFlag}
   */
  private final String waypointPointNavaidFlag;
  /**
   * {@link WaypointType}
   */
  private final String waypointType;
  /**
   * {@link WaypointDescriptionName}
   */
  private final String waypointDescriptionName;
  /**
   * {@link IcaoCode}
   */
  private final String icaoCode;
  /**
   * {@link WaypointUsageCode}
   */
  private final String waypointUsageCode;
  /**
   * {@link WaypointBearing}
   */
  private final Double waypointBearing;
  /**
   * {@link Distance}
   */
  private final Double distance;
  /**
   * {@link WAC}
   */
  private final String wac;
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
   * {@link MagneticVariation}
   */
  private final String magneticVariation;
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
  private final String navaidCountryCode;
  /**
   * {@link NavaidKeyCode}
   */
  private final Integer navaidKeyCode;
  /**
   * {@link CycleDate}
   */
  private final Integer cycleDate;
  /**
   * {@link WaypointRunwayIdent}
   */
  private final String waypointRunwayIdent;
  /**
   * {@link WaypointRwyIcao}
   */
  private final String waypointRwyIcao;
  /**
   * {@link CoordinatePrecision}
   */
  private final Integer coordinatePrecision;

  private DafifWaypoint(Builder builder) {
    this.waypointIdentifierWptIdent = builder.waypointIdentifierWptIdent;
    this.countryCode = builder.countryCode;
    this.stateProvinceCode = builder.stateProvinceCode;
    this.waypointPointNavaidFlag = builder.waypointPointNavaidFlag;
    this.waypointType = builder.waypointType;
    this.waypointDescriptionName = builder.waypointDescriptionName;
    this.icaoCode = builder.icaoCode;
    this.waypointUsageCode = builder.waypointUsageCode;
    this.waypointBearing = builder.waypointBearing;
    this.distance = builder.distance;
    this.wac = builder.wac;
    this.localHorizontalDatum = builder.localHorizontalDatum;
    this.geodeticDatum = builder.geodeticDatum;
    this.geodeticLatitude = builder.geodeticLatitude;
    this.degreesLatitude = builder.degreesLatitude;
    this.geodeticLongitude = builder.geodeticLongitude;
    this.degreesLongitude = builder.degreesLongitude;
    this.magneticVariation = builder.magneticVariation;
    this.navaidIdentifier = builder.navaidIdentifier;
    this.navaidType = builder.navaidType;
    this.navaidCountryCode = builder.navaidCountryCode;
    this.navaidKeyCode = builder.navaidKeyCode;
    this.cycleDate = builder.cycleDate;
    this.waypointRunwayIdent = builder.waypointRunwayIdent;
    this.waypointRwyIcao = builder.waypointRwyIcao;
    this.coordinatePrecision = builder.coordinatePrecision;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    DafifWaypoint that = (DafifWaypoint) o;
    return Objects.equals(waypointIdentifierWptIdent, that.waypointIdentifierWptIdent) && Objects.equals(countryCode, that.countryCode) && Objects.equals(stateProvinceCode, that.stateProvinceCode) && Objects.equals(waypointPointNavaidFlag, that.waypointPointNavaidFlag) && Objects.equals(waypointType, that.waypointType) && Objects.equals(waypointDescriptionName, that.waypointDescriptionName) && Objects.equals(icaoCode, that.icaoCode) && Objects.equals(waypointUsageCode, that.waypointUsageCode) && Objects.equals(waypointBearing, that.waypointBearing) && Objects.equals(distance, that.distance) && Objects.equals(wac, that.wac) && Objects.equals(localHorizontalDatum, that.localHorizontalDatum) && Objects.equals(geodeticDatum, that.geodeticDatum) && Objects.equals(geodeticLatitude, that.geodeticLatitude) && Objects.equals(degreesLatitude, that.degreesLatitude) && Objects.equals(geodeticLongitude, that.geodeticLongitude) && Objects.equals(degreesLongitude, that.degreesLongitude) && Objects.equals(magneticVariation, that.magneticVariation) && Objects.equals(navaidIdentifier, that.navaidIdentifier) && Objects.equals(navaidType, that.navaidType) && Objects.equals(navaidCountryCode, that.navaidCountryCode) && Objects.equals(navaidKeyCode, that.navaidKeyCode) && Objects.equals(cycleDate, that.cycleDate) && Objects.equals(waypointRunwayIdent, that.waypointRunwayIdent) && Objects.equals(waypointRwyIcao, that.waypointRwyIcao) && Objects.equals(coordinatePrecision, that.coordinatePrecision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(waypointIdentifierWptIdent, countryCode, stateProvinceCode, waypointPointNavaidFlag, waypointType, waypointDescriptionName, icaoCode, waypointUsageCode, waypointBearing, distance, wac, localHorizontalDatum, geodeticDatum, geodeticLatitude, degreesLatitude, geodeticLongitude, degreesLongitude, magneticVariation, navaidIdentifier, navaidType, navaidCountryCode, navaidKeyCode, cycleDate, waypointRunwayIdent, waypointRwyIcao, coordinatePrecision);
  }

  public String getWaypointIdentifierWptIdent() {
    return waypointIdentifierWptIdent;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public Integer getStateProvinceCode() {
    return stateProvinceCode;
  }

  public String getWaypointPointNavaidFlag() {
    return waypointPointNavaidFlag;
  }

  public String getWaypointType() {
    return waypointType;
  }

  public String getWaypointDescriptionName() {
    return waypointDescriptionName;
  }

  public String getIcaoCode() {
    return icaoCode;
  }

  public String getWaypointUsageCode() {
    return waypointUsageCode;
  }

  public Double getWaypointBearing() {
    return waypointBearing;
  }

  public Double getDistance() {
    return distance;
  }

  public String getWac() {
    return wac;
  }

  public String getLocalHorizontalDatum() {
    return localHorizontalDatum;
  }

  public String getGeodeticDatum() {
    return geodeticDatum;
  }

  public String getGeodeticLatitude() {
    return geodeticLatitude;
  }

  public Double getDegreesLatitude() {
    return degreesLatitude;
  }

  public String getGeodeticLongitude() {
    return geodeticLongitude;
  }

  public Double getDegreesLongitude() {
    return degreesLongitude;
  }

  public String getMagneticVariation() {
    return magneticVariation;
  }

  public String getNavaidIdentifier() {
    return navaidIdentifier;
  }

  public Integer getNavaidType() {
    return navaidType;
  }

  public String getNavaidCountryCode() {
    return navaidCountryCode;
  }

  public Integer getNavaidKeyCode() {
    return navaidKeyCode;
  }

  public Integer getCycleDate() {
    return cycleDate;
  }

  public String getWaypointRunwayIdent() {
    return waypointRunwayIdent;
  }

  public String getWaypointRwyIcao() {
    return waypointRwyIcao;
  }

  public Integer getCoordinatePrecision() {
    return coordinatePrecision;
  }

  public static final class Builder {
    private String waypointIdentifierWptIdent;
    private String countryCode;
    private Integer stateProvinceCode;
    private String waypointPointNavaidFlag;
    private String waypointType;
    private String waypointDescriptionName;
    private String icaoCode;
    private String waypointUsageCode;
    private Double waypointBearing;
    private Double distance;
    private String wac;
    private String localHorizontalDatum;
    private String geodeticDatum;
    private String geodeticLatitude;
    private Double degreesLatitude;
    private String geodeticLongitude;
    private Double degreesLongitude;
    private String magneticVariation;
    private String navaidIdentifier;
    private Integer navaidType;
    private String navaidCountryCode;
    private Integer navaidKeyCode;
    private Integer cycleDate;
    private String waypointRunwayIdent;
    private String waypointRwyIcao;
    private Integer coordinatePrecision;

    public Builder waypointIdentifierWptIdent(String waypointIdentifierWptIdent) {
      this.waypointIdentifierWptIdent = waypointIdentifierWptIdent;
      return this;
    }

    public Builder countryCode(String countryCode) {
      this.countryCode = countryCode;
      return this;
    }

    public Builder stateProvinceCode(Integer stateProvinceCode) {
      this.stateProvinceCode = stateProvinceCode;
      return this;
    }

    public Builder waypointPointNavaidFlag(String waypointPointNavaidFlag) {
      this.waypointPointNavaidFlag = waypointPointNavaidFlag;
      return this;
    }

    public Builder waypointType(String waypointType) {
      this.waypointType = waypointType;
      return this;
    }

    public Builder waypointDescriptionName(String waypointDescriptionName) {
      this.waypointDescriptionName = waypointDescriptionName;
      return this;
    }

    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder waypointUsageCode(String waypointUsageCode) {
      this.waypointUsageCode = waypointUsageCode;
      return this;
    }

    public Builder waypointBearing(Double waypointBearing) {
      this.waypointBearing = waypointBearing;
      return this;
    }

    public Builder distance(Double distance) {
      this.distance = distance;
      return this;
    }

    public Builder wac(String wac) {
      this.wac = wac;
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

    public Builder magneticVariation(String magneticVariation) {
      this.magneticVariation = magneticVariation;
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

    public Builder waypointRunwayIdent(String waypointRunwayIdent) {
      this.waypointRunwayIdent = waypointRunwayIdent;
      return this;
    }

    public Builder waypointRwyIcao(String waypointRwyIcao) {
      this.waypointRwyIcao = waypointRwyIcao;
      return this;
    }

    public Builder coordinatePrecision(Integer coordinatePrecision) {
      this.coordinatePrecision = coordinatePrecision;
      return this;
    }

    public DafifWaypoint build() {
      return new DafifWaypoint(this);
    }
  }
}
