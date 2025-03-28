package org.mitre.tdp.boogie.dafif.model;

import org.mitre.tdp.boogie.dafif.v81.field.AirportHeliportElevation;
import org.mitre.tdp.boogie.dafif.v81.field.AirportHeliportName;
import org.mitre.tdp.boogie.dafif.v81.field.AirportIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.AirportType;
import org.mitre.tdp.boogie.dafif.v81.field.Beacon;
import org.mitre.tdp.boogie.dafif.v81.field.CoordinatePrecision;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.FaaHostCountryIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticDatum;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.LocalHorizontalDatum;
import org.mitre.tdp.boogie.dafif.v81.field.MagneticVariation;
import org.mitre.tdp.boogie.dafif.v81.field.MagneticVariationOfRecord;
import org.mitre.tdp.boogie.dafif.v81.field.PrimaryOperatingAgency;
import org.mitre.tdp.boogie.dafif.v81.field.SecondaryAirport;
import org.mitre.tdp.boogie.dafif.v81.field.SecondaryFaaHost;
import org.mitre.tdp.boogie.dafif.v81.field.SecondaryIcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.SecondaryName;
import org.mitre.tdp.boogie.dafif.v81.field.SecondaryOperatingAgency;
import org.mitre.tdp.boogie.dafif.v81.field.ShorelineHydrography;
import org.mitre.tdp.boogie.dafif.v81.field.StateProvinceCode;
import org.mitre.tdp.boogie.dafif.v81.field.TerrainImpacted;
import org.mitre.tdp.boogie.dafif.v81.field.WAC;

import java.util.Objects;
import java.util.Optional;

public final class DafifAirport {

  /**
   * {@link AirportIdentification}
   */
  private final String airportIdentification;

  /**
   * {@link AirportHeliportName}
   */
  private final String name;

  /**
   * {@link StateProvinceCode}
   */
  private final Integer stateProvinceCode;

  /**
   * {@link IcaoCode}
   */
  private final String icaoCode;

  /**
   * {@link FaaHostCountryIdentifier}
   */
  private final String faaHostCountryIdentifier;

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
   * {@link AirportHeliportElevation}
   */
  private final Integer elevation;

  /**
   * {@link AirportType}
   */
  private final String airportType;

  /**
   * {@link MagneticVariation}
   */
  private final String magneticVariation;

  /**
   * {@link WAC}
   */
  private final String wac;

  /**
   * {@link Beacon}
   */
  private final String beacon;

  /**
   * {@link SecondaryAirport}
   */
  private final String secondaryAirport;

  /**
   * {@link PrimaryOperatingAgency}
   */
  private final String primaryOperatingAgency;

  /**
   * {@link SecondaryName}
   */
  private final String secondaryName;

  /**
   * {@link SecondaryIcaoCode}
   */
  private final String secondaryIcaoCode;

  /**
   * {@link SecondaryFaaHost}
   */
  private final String secondaryFaaHost;

  /**
   * {@link SecondaryOperatingAgency}
   */
  private final String secondaryOperatingAgency;

  /**
   * {@link CycleDate}
   */
  private final Integer cycleDate;

  /**
   * {@link TerrainImpacted}
   */
  private final String terrainImpacted;

  /**
   * {@link ShorelineHydrography}
   */
  private final String shoreline;

  /**
   * {@link CoordinatePrecision}
   */
  private final Integer coordinatePrecision;

  /**
   * {@link MagneticVariationOfRecord}
   */
  private final String magVarOfRecord;

  private DafifAirport(Builder builder) {
    this.airportIdentification = builder.airportIdentification;
    this.name = builder.name;
    this.stateProvinceCode = builder.stateProvinceCode;
    this.icaoCode = builder.icaoCode;
    this.faaHostCountryIdentifier = builder.faaHostCountryIdentifier;
    this.localHorizontalDatum = builder.localHorizontalDatum;
    this.geodeticDatum = builder.geodeticDatum;
    this.geodeticLatitude = builder.geodeticLatitude;
    this.degreesLatitude = builder.degreesLatitude;
    this.geodeticLongitude = builder.geodeticLongitude;
    this.degreesLongitude = builder.degreesLongitude;
    this.elevation = builder.elevation;
    this.airportType = builder.airportType;
    this.magneticVariation = builder.mageneticVariation;
    this.wac = builder.wac;
    this.beacon = builder.beacon;
    this.secondaryAirport = builder.secondaryAirport;
    this.primaryOperatingAgency = builder.primaryOperatingAgency;
    this.secondaryName = builder.secondaryName;
    this.secondaryIcaoCode = builder.secondaryIcaoCode;
    this.secondaryFaaHost = builder.secondaryFaaHost;
    this.secondaryOperatingAgency = builder.secondaryOperatingAgency;
    this.cycleDate = builder.cycleDate;
    this.terrainImpacted = builder.terrainImpacted;
    this.shoreline = builder.shoreline;
    this.coordinatePrecision = builder.coordinatePrecision;
    this.magVarOfRecord = builder.magVarOfRecord;
  }

  public String airportIdentification() {
    return airportIdentification;
  }

  public String name() {
    return name;
  }

  public String icaoCode() {
    return icaoCode;
  }

  public String localHorizontalDatum() {
    return localHorizontalDatum;
  }

  public Integer elevation() {
    return elevation;
  }

  public String airportType() {
    return airportType;
  }

  public String magneticVariation() {
    return magneticVariation;
  }

  public String wac() {
    return wac;
  }

  public String primaryOperatingAgency() {
    return primaryOperatingAgency;
  }

  public Integer cycleDate() {
    return cycleDate;
  }

  public Optional<Integer> stateProvinceCode() {
    return Optional.ofNullable(stateProvinceCode);
  }

  public Optional<String> faaHostCountryIdentifier() {
    return Optional.ofNullable(faaHostCountryIdentifier);
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

  public Optional<String> beacon() {
    return Optional.ofNullable(beacon);
  }

  public Optional<String> secondaryAirport() {
    return Optional.ofNullable(secondaryAirport);
  }

  public Optional<String> secondaryName() {
    return Optional.ofNullable(secondaryName);
  }

  public Optional<String> secondaryIcaoCode() {
    return Optional.ofNullable(secondaryIcaoCode);
  }

  public Optional<String> secondaryFaaHost() {
    return Optional.ofNullable(secondaryFaaHost);
  }

  public Optional<String> secondaryOperatingAgency() {
    return Optional.ofNullable(secondaryOperatingAgency);
  }

  public Optional<String> terrainImpacted() {
    return Optional.ofNullable(terrainImpacted);
  }

  public Optional<String> shoreline() {
    return Optional.ofNullable(shoreline);
  }

  public Optional<Integer> coordinatePrecision() {
    return Optional.ofNullable(coordinatePrecision);
  }

  public Optional<String> magVarOfRecord() {
    return Optional.ofNullable(magVarOfRecord);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    DafifAirport that = (DafifAirport) o;
    return Objects.equals(airportIdentification, that.airportIdentification) && Objects.equals(name, that.name) && Objects.equals(stateProvinceCode, that.stateProvinceCode) && Objects.equals(icaoCode, that.icaoCode) && Objects.equals(faaHostCountryIdentifier, that.faaHostCountryIdentifier) && Objects.equals(localHorizontalDatum, that.localHorizontalDatum) && Objects.equals(geodeticDatum, that.geodeticDatum) && Objects.equals(geodeticLatitude, that.geodeticLatitude) && Objects.equals(degreesLatitude, that.degreesLatitude) && Objects.equals(geodeticLongitude, that.geodeticLongitude) && Objects.equals(degreesLongitude, that.degreesLongitude) && Objects.equals(elevation, that.elevation) && Objects.equals(airportType, that.airportType) && Objects.equals(magneticVariation, that.magneticVariation) && Objects.equals(wac, that.wac) && Objects.equals(beacon, that.beacon) && Objects.equals(secondaryAirport, that.secondaryAirport) && Objects.equals(primaryOperatingAgency, that.primaryOperatingAgency) && Objects.equals(secondaryName, that.secondaryName) && Objects.equals(secondaryIcaoCode, that.secondaryIcaoCode) && Objects.equals(secondaryFaaHost, that.secondaryFaaHost) && Objects.equals(secondaryOperatingAgency, that.secondaryOperatingAgency) && Objects.equals(cycleDate, that.cycleDate) && Objects.equals(terrainImpacted, that.terrainImpacted) && Objects.equals(shoreline, that.shoreline) && Objects.equals(coordinatePrecision, that.coordinatePrecision) && Objects.equals(magVarOfRecord, that.magVarOfRecord);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airportIdentification, name, stateProvinceCode, icaoCode, faaHostCountryIdentifier, localHorizontalDatum, geodeticDatum, geodeticLatitude, degreesLatitude, geodeticLongitude, degreesLongitude, elevation, airportType, magneticVariation, wac, beacon, secondaryAirport, primaryOperatingAgency, secondaryName, secondaryIcaoCode, secondaryFaaHost, secondaryOperatingAgency, cycleDate, terrainImpacted, shoreline, coordinatePrecision, magVarOfRecord);
  }

  public static final class Builder {
    private String airportIdentification;
    private String name;
    private Integer stateProvinceCode;
    private String icaoCode;
    private String faaHostCountryIdentifier;
    private String localHorizontalDatum;
    private String geodeticDatum;
    private String geodeticLatitude;
    private Double degreesLatitude;
    private String geodeticLongitude;
    private Double degreesLongitude;
    private Integer elevation;
    private String airportType;
    private String mageneticVariation;
    private String wac;
    private String beacon;
    private String secondaryAirport;
    private String primaryOperatingAgency;
    private String secondaryName;
    private String secondaryIcaoCode;
    private String secondaryFaaHost;
    private String secondaryOperatingAgency;
    private Integer cycleDate;
    private String terrainImpacted;
    private String shoreline;

    private Integer coordinatePrecision;

    private String magVarOfRecord;

    public Builder airportIdentification(String airportIdentification) {
      this.airportIdentification = airportIdentification;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }
    public Builder stateProvinceCode(Integer stateProvinceCode) {
      this.stateProvinceCode = stateProvinceCode;
      return this;
    }
    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }
    public Builder faaHostCountryIdentifier(String faaHostCountryIdentifier) {
      this.faaHostCountryIdentifier = faaHostCountryIdentifier;
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
    public Builder elevation(Integer elevation) {
      this.elevation = elevation;
      return this;
    }
    public Builder airportType(String airportType) {
      this.airportType = airportType;
      return this;
    }
    public Builder mageneticVariation(String mageneticVariation) {
      this.mageneticVariation = mageneticVariation;
      return this;
    }
    public Builder wac(String wac) {
      this.wac = wac;
      return this;
    }
    public Builder beacon(String beacon) {
      this.beacon = beacon;
      return this;
    }
    public Builder secondaryAirport(String secondaryAirport) {
      this.secondaryAirport = secondaryAirport;
      return this;
    }
    public Builder primaryOperatingAgency(String primaryOperatingAgency) {
      this.primaryOperatingAgency = primaryOperatingAgency;
      return this;
    }
    public Builder secondaryName(String secondaryName) {
      this.secondaryName = secondaryName;
      return this;
    }
    public Builder secondaryIcaoCode(String secondaryIcaoCode) {
      this.secondaryIcaoCode = secondaryIcaoCode;
      return this;
    }
    public Builder secondaryFaaHost(String secondaryFaaHost) {
      this.secondaryFaaHost = secondaryFaaHost;
      return this;
    }
    public Builder secondaryOperatingAgency(String secondaryOperatingAgency) {
      this.secondaryOperatingAgency = secondaryOperatingAgency;
      return this;
    }
    public Builder cycleDate(Integer cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }
    public Builder terrainImpacted(String terrainImpacted) {
      this.terrainImpacted = terrainImpacted;
      return this;
    }
    public Builder shoreline(String shoreline) {
      this.shoreline = shoreline;
      return this;
    }

    public Builder coordinatePrecision(Integer coordinatePrecision) {
      this.coordinatePrecision = coordinatePrecision;
      return this;
    }

    public Builder magVarOfRecord(String magVarOfRecord) {
      this.magVarOfRecord = magVarOfRecord;
      return this;
    }

    public DafifAirport build() {
      return new DafifAirport(this);
    }
  }
}
