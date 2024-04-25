package org.mitre.tdp.boogie.dafif.model;

import org.mitre.tdp.boogie.dafif.v81.field.AirportIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.Collocation;
import org.mitre.tdp.boogie.dafif.v81.field.ComponentType;
import org.mitre.tdp.boogie.dafif.v81.field.CoordinatePrecision;
import org.mitre.tdp.boogie.dafif.v81.field.CountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticDatum;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.IlsBearingCourse;
import org.mitre.tdp.boogie.dafif.v81.field.IlsDmeBias;
import org.mitre.tdp.boogie.dafif.v81.field.IlsGlideSlopeAngle;
import org.mitre.tdp.boogie.dafif.v81.field.IlsMlsCategory;
import org.mitre.tdp.boogie.dafif.v81.field.IlsNavaidElevation;
import org.mitre.tdp.boogie.dafif.v81.field.IlsNavaidIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.IlsSlaveVariation;
import org.mitre.tdp.boogie.dafif.v81.field.LocalHorizontalDatum;
import org.mitre.tdp.boogie.dafif.v81.field.LocalizerOrGlideSlopeLocation;
import org.mitre.tdp.boogie.dafif.v81.field.LocalizerWidth;
import org.mitre.tdp.boogie.dafif.v81.field.LocatorOrMarkerLocation;
import org.mitre.tdp.boogie.dafif.v81.field.MagneticVariation;
import org.mitre.tdp.boogie.dafif.v81.field.MlsDmePrecision;
import org.mitre.tdp.boogie.dafif.v81.field.Name;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidChannel;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidFrequency;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidKeyCode;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidType;
import org.mitre.tdp.boogie.dafif.v81.field.RunwayIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.ThresholdCrossingHeight;

import java.util.Objects;

public final class DafifIls {

  /**
   * {@link AirportIdentification}
   */
  private final String airportIdentification;

  /**
   * {@link RunwayIdentifier}
   */
  private final String runwayIdentifier;

  /**
   * {@link ComponentType}
   */
  private final String componentType;

  /**
   * {@link Collocation}
   */
  private final String collocation;

  /**
   * {@link Name}
   */
  private final String name;

  /**
   * {@link NavaidFrequency}
   */
  private final String navaidFrequency;

  /**
   * {@link NavaidChannel}
   */
  private final String navaidChannel;

  /**
   * {@link IlsGlideSlopeAngle}
   */
  private final Double ilsGlideSlopeAngle;

  /**
   * {@link LocalizerOrGlideSlopeLocation}
   */
  private final String localizerOrGlideSlopeLocation;

  /**
   * {@link LocatorOrMarkerLocation}
   */
  private final String locatorOrMarkerLocation;

  /**
   * {@link IlsNavaidElevation}
   */
  private final String ilsNavaidElevation;

  /**
   * {@link LocalHorizontalDatum}
   */
  private final String localHorizontalDatum;

  /**
   * {@link GeodeticDatum}
   */
  private final String geodeticDatum;

  /**
   * {@link IlsMlsCategory}
   */
  private final String ilsMlsCategory;

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
   * {@link IlsNavaidIdentifier}
   */
  private final String ilsNavaidIdentifier;

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
   * {@link MagneticVariation}
   */
  private final String magneticVariation;

  /**
   * {@link IlsSlaveVariation}
   */
  private final String ilsSlaveVariation;

  /**
   * {@link IlsBearingCourse}
   */
  private final String ilsBearingCourse;

  /**
   * {@link LocalizerWidth}
   */
  private final Double localizerWidth;

  /**
   * {@link ThresholdCrossingHeight}
   */
  private final Integer thresholdCrossingHeight;

  /**
   * {@link IlsDmeBias}
   */
  private final Double ilsDmeBias;

  /**
   * {@link CycleDate}
   */
  private final Integer cycleDate;

  /**
   * {@link MlsDmePrecision}
   */
  private final String mlsDmePrecision;

  /**
   * {@link CoordinatePrecision}
   */
  private final Integer coordinatePrecision;

  private DafifIls(Builder builder) {
    this.airportIdentification = builder.airportIdentification;
    this.runwayIdentifier = builder.runwayIdentifier;
    this.componentType = builder.componentType;
    this.collocation = builder.collocation;
    this.name = builder.name;
    this.navaidFrequency = builder.navaidFrequency;
    this.navaidChannel = builder.navaidChannel;
    this.ilsGlideSlopeAngle = builder.ilsGlideSlopeAngle;
    this.localizerOrGlideSlopeLocation = builder.localizerOrGlideSlopeLocation;
    this.locatorOrMarkerLocation = builder.locatorOrMarkerLocation;
    this.ilsNavaidElevation = builder.ilsNavaidElevation;
    this.localHorizontalDatum = builder.localHorizontalDatum;
    this.geodeticDatum = builder.geodeticDatum;
    this.ilsMlsCategory = builder.ilsMlsCategory;
    this.geodeticLatitude = builder.geodeticLatitude;
    this.degreesLatitude = builder.degreesLatitude;
    this.geodeticLongitude = builder.geodeticLongitude;
    this.degreesLongitude = builder.degreesLongitude;
    this.ilsNavaidIdentifier = builder.ilsNavaidIdentifier;
    this.navaidType = builder.navaidType;
    this.countryCode = builder.countryCode;
    this.navaidKeyCode = builder.navaidKeyCode;
    this.magneticVariation = builder.magneticVariation;
    this.ilsSlaveVariation = builder.ilsSlaveVariation;
    this.ilsBearingCourse = builder.ilsBearingCourse;
    this.localizerWidth = builder.localizerWidth;
    this.thresholdCrossingHeight = builder.thresholdCrossingHeight;
    this.ilsDmeBias = builder.ilsDmeBias;
    this.cycleDate = builder.cycleDate;
    this.mlsDmePrecision = builder.mlsDmePrecisionNonPrecision;
    this.coordinatePrecision = builder.coordinatePrecision;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    DafifIls dafifIls = (DafifIls) o;
    return Objects.equals(airportIdentification, dafifIls.airportIdentification) && Objects.equals(runwayIdentifier, dafifIls.runwayIdentifier) && Objects.equals(componentType, dafifIls.componentType) && Objects.equals(collocation, dafifIls.collocation) && Objects.equals(name, dafifIls.name) && Objects.equals(navaidFrequency, dafifIls.navaidFrequency) && Objects.equals(navaidChannel, dafifIls.navaidChannel) && Objects.equals(ilsGlideSlopeAngle, dafifIls.ilsGlideSlopeAngle) && Objects.equals(localizerOrGlideSlopeLocation, dafifIls.localizerOrGlideSlopeLocation) && Objects.equals(locatorOrMarkerLocation, dafifIls.locatorOrMarkerLocation) && Objects.equals(ilsNavaidElevation, dafifIls.ilsNavaidElevation) && Objects.equals(localHorizontalDatum, dafifIls.localHorizontalDatum) && Objects.equals(geodeticDatum, dafifIls.geodeticDatum) && Objects.equals(ilsMlsCategory, dafifIls.ilsMlsCategory) && Objects.equals(geodeticLatitude, dafifIls.geodeticLatitude) && Objects.equals(degreesLatitude, dafifIls.degreesLatitude) && Objects.equals(geodeticLongitude, dafifIls.geodeticLongitude) && Objects.equals(degreesLongitude, dafifIls.degreesLongitude) && Objects.equals(ilsNavaidIdentifier, dafifIls.ilsNavaidIdentifier) && Objects.equals(navaidType, dafifIls.navaidType) && Objects.equals(countryCode, dafifIls.countryCode) && Objects.equals(navaidKeyCode, dafifIls.navaidKeyCode) && Objects.equals(magneticVariation, dafifIls.magneticVariation) && Objects.equals(ilsSlaveVariation, dafifIls.ilsSlaveVariation) && Objects.equals(ilsBearingCourse, dafifIls.ilsBearingCourse) && Objects.equals(localizerWidth, dafifIls.localizerWidth) && Objects.equals(thresholdCrossingHeight, dafifIls.thresholdCrossingHeight) && Objects.equals(ilsDmeBias, dafifIls.ilsDmeBias) && Objects.equals(cycleDate, dafifIls.cycleDate) && Objects.equals(mlsDmePrecision, dafifIls.mlsDmePrecision) && Objects.equals(coordinatePrecision, dafifIls.coordinatePrecision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airportIdentification, runwayIdentifier, componentType, collocation, name, navaidFrequency, navaidChannel, ilsGlideSlopeAngle, localizerOrGlideSlopeLocation, locatorOrMarkerLocation, ilsNavaidElevation, localHorizontalDatum, geodeticDatum, ilsMlsCategory, geodeticLatitude, degreesLatitude, geodeticLongitude, degreesLongitude, ilsNavaidIdentifier, navaidType, countryCode, navaidKeyCode, magneticVariation, ilsSlaveVariation, ilsBearingCourse, localizerWidth, thresholdCrossingHeight, ilsDmeBias, cycleDate, mlsDmePrecision, coordinatePrecision);
  }

  public String getAirportIdentification() {
    return airportIdentification;
  }

  public String getRunwayIdentifier() {
    return runwayIdentifier;
  }

  public String getComponentType() {
    return componentType;
  }

  public String getCollocation() {
    return collocation;
  }

  public String getName() {
    return name;
  }

  public String getNavaidFrequency() {
    return navaidFrequency;
  }

  public String getNavaidChannel() {
    return navaidChannel;
  }

  public Double getIlsGlideSlopeAngle() {
    return ilsGlideSlopeAngle;
  }

  public String getLocalizerOrGlideSlopeLocation() {
    return localizerOrGlideSlopeLocation;
  }

  public String getLocatorOrMarkerLocation() {
    return locatorOrMarkerLocation;
  }

  public String getIlsNavaidElevation() {
    return ilsNavaidElevation;
  }

  public String getLocalHorizontalDatum() {
    return localHorizontalDatum;
  }

  public String getGeodeticDatum() {
    return geodeticDatum;
  }

  public String getIlsMlsCategory() {
    return ilsMlsCategory;
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

  public String getIlsNavaidIdentifier() {
    return ilsNavaidIdentifier;
  }

  public Integer getNavaidType() {
    return navaidType;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public Integer getNavaidKeyCode() {
    return navaidKeyCode;
  }

  public String getMagneticVariation() {
    return magneticVariation;
  }

  public String getIlsSlaveVariation() {
    return ilsSlaveVariation;
  }

  public String getIlsBearingCourse() {
    return ilsBearingCourse;
  }

  public Double getLocalizerWidth() {
    return localizerWidth;
  }

  public Integer getThresholdCrossingHeight() {
    return thresholdCrossingHeight;
  }

  public Double getIlsDmeBias() {
    return ilsDmeBias;
  }

  public Integer getCycleDate() {
    return cycleDate;
  }

  public String getMlsDmePrecision() {
    return mlsDmePrecision;
  }

  public Integer getCoordinatePrecision() {
    return coordinatePrecision;
  }

  public static final class Builder {
    private String airportIdentification;
    private String runwayIdentifier;
    private String componentType;
    private String collocation;
    private String name;
    private String navaidFrequency;
    private String navaidChannel;
    private Double ilsGlideSlopeAngle;
    private String localizerOrGlideSlopeLocation;
    private String locatorOrMarkerLocation;
    private String ilsNavaidElevation;
    private String localHorizontalDatum;
    private String geodeticDatum;
    private String ilsMlsCategory;
    private String geodeticLatitude;
    private Double degreesLatitude;
    private String geodeticLongitude;
    private Double degreesLongitude;
    private String ilsNavaidIdentifier;
    private Integer navaidType;
    private String countryCode;
    private Integer navaidKeyCode;
    private String magneticVariation;
    private String ilsSlaveVariation;
    private String ilsBearingCourse;
    private Double localizerWidth;
    private Integer thresholdCrossingHeight;
    private Double ilsDmeBias;
    private Integer cycleDate;
    private String mlsDmePrecisionNonPrecision;
    private Integer coordinatePrecision;

    public Builder airportIdentification(String airportIdentification) {
      this.airportIdentification = airportIdentification;
      return this;
    }

    public Builder runwayIdentifier(String runwayIdentifier) {
      this.runwayIdentifier = runwayIdentifier;
      return this;
    }

    public Builder componentType(String componentType) {
      this.componentType = componentType;
      return this;
    }

    public Builder collocation(String collocation) {
      this.collocation = collocation;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder navaidFrequency(String navaidFrequency) {
      this.navaidFrequency = navaidFrequency;
      return this;
    }

    public Builder navaidChannel(String navaidChannel) {
      this.navaidChannel = navaidChannel;
      return this;
    }

    public Builder ilsGlideSlopeAngle(Double ilsGlideSlopeAngle) {
      this.ilsGlideSlopeAngle = ilsGlideSlopeAngle;
      return this;
    }

    public Builder localizerOrGlideSlopeLocation(String localizerOrGlideSlopeLocation) {
      this.localizerOrGlideSlopeLocation = localizerOrGlideSlopeLocation;
      return this;
    }

    public Builder locatorOrMarkerLocation(String locatorOrMarkerLocation) {
      this.locatorOrMarkerLocation = locatorOrMarkerLocation;
      return this;
    }

    public Builder ilsNavaidElevation(String ilsNavaidElevation) {
      this.ilsNavaidElevation = ilsNavaidElevation;
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

    public Builder ilsMlsCategory(String ilsMlsCategory) {
      this.ilsMlsCategory = ilsMlsCategory;
      return this;
    }

    public Builder geodeticLatitude(String geodeticLatitude) {
      this.geodeticLatitude = geodeticLatitude;
      return this;
    }

    public Builder degreesLatitude(Double degreeLatitude) {
      this.degreesLatitude = degreeLatitude;
      return this;
    }

    public Builder geodeticLongitude(String geodeticLongitude) {
      this.geodeticLongitude = geodeticLongitude;
      return this;
    }

    public Builder degreesLongitude(Double degreeLongitude) {
      this.degreesLongitude = degreeLongitude;
      return this;
    }

    public Builder ilsNavaidIdentifier(String ilsNavaidIdentifier) {
      this.ilsNavaidIdentifier = ilsNavaidIdentifier;
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

    public Builder magneticVariation(String magneticVariation) {
      this.magneticVariation = magneticVariation;
      return this;
    }

    public Builder ilsSlaveVariation(String ilsSlaveVariation) {
      this.ilsSlaveVariation = ilsSlaveVariation;
      return this;
    }

    public Builder ilsBearingCourse(String ilsBearingCourse) {
      this.ilsBearingCourse = ilsBearingCourse;
      return this;
    }

    public Builder localizerWidth(Double localizerWidth) {
      this.localizerWidth = localizerWidth;
      return this;
    }

    public Builder thresholdCrossingHeight(Integer thresholdCrossingHeight) {
      this.thresholdCrossingHeight = thresholdCrossingHeight;
      return this;
    }

    public Builder ilsDmeBias(Double ilsDmeBias) {
      this.ilsDmeBias = ilsDmeBias;
      return this;
    }

    public Builder cycleDate(Integer cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }

    public Builder mlsDmePrecisionNonPrecision(String mlsDmePrecisionNonPrecision) {
      this.mlsDmePrecisionNonPrecision = mlsDmePrecisionNonPrecision;
      return this;
    }

    public Builder coordinatePrecision(Integer coordinatePrecision) {
      this.coordinatePrecision = coordinatePrecision;
      return this;
    }

    public DafifIls build() {
      return new DafifIls(this);
    }
  }
}
