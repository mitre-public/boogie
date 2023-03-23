package org.mitre.tdp.boogie.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Runway;

/**
 * Immutable, buildable implementation of the boogie {@link Airport} provided for convenience.
 */
public final class BoogieAirport implements Airport {

  private final String airportIdentifier;
  private final String airportRegion;
  private final LatLong latLong;
  private final Double publishedVariation;
  private final double modeledVariation;
  private final List<BoogieRunway> runways;
  private final Double elevation;

  private BoogieAirport(Builder builder) {
    this.airportIdentifier = builder.airportIdentifier;
    this.airportRegion = builder.airportRegion;
    this.latLong = LatLong.of(builder.latitude, builder.longitude);
    this.publishedVariation = builder.publishedVariation;
    this.modeledVariation = builder.modeledVariation;
    this.runways = builder.runways;
    this.elevation = builder.elevation;
  }

  @Override
  public String airportIdentifier() {
    return airportIdentifier;
  }

  @Override
  public String airportRegion() {
    return airportRegion;
  }

  @Override
  public LatLong latLong() {
    return latLong;
  }

  @Override
  public Optional<Double> publishedVariation() {
    return Optional.ofNullable(publishedVariation);
  }

  @Override
  public double modeledVariation() {
    return modeledVariation;
  }

  @Override
  public List<BoogieRunway> runways() {
    return runways;
  }

  @Override
  public Optional<Double> elevation() {
    return Optional.ofNullable(elevation);
  }

  public Builder toBuilder() {
    return new Builder()
        .airportIdentifier(airportIdentifier())
        .airportRegion(airportRegion())
        .latitude(latitude())
        .longitude(longitude())
        .publishedVariation(publishedVariation().orElse(null))
        .modeledVariation(modeledVariation())
        .runways(runways())
        .elevation(elevation().orElse(null));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoogieAirport that = (BoogieAirport) o;
    return Double.compare(that.modeledVariation, modeledVariation) == 0 &&
        Objects.equals(airportIdentifier, that.airportIdentifier) &&
        Objects.equals(airportRegion, that.airportRegion) &&
        Objects.equals(latLong, that.latLong) &&
        Objects.equals(publishedVariation, that.publishedVariation) &&
        Objects.equals(runways, that.runways) &&
        Objects.equals(elevation, that.elevation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airportIdentifier, airportRegion, latLong, publishedVariation, modeledVariation, runways, elevation);
  }

  @Override
  public String toString() {
    return "BoogieAirport{" +
        "airportIdentifier='" + airportIdentifier + '\'' +
        ", airportRegion='" + airportRegion + '\'' +
        ", latLong=" + latLong +
        ", publishedVariation=" + publishedVariation +
        ", modeledVariation=" + modeledVariation +
        ", runways=" + runways +
        ", elevation=" + elevation +
        '}';
  }

  public static final class Builder {
    private String airportIdentifier;
    private String airportRegion;
    private double latitude;
    private double longitude;
    private Double publishedVariation;
    private double modeledVariation;
    private List<BoogieRunway> runways;
    private Double elevation;

    public Builder airportIdentifier(String airportIdentifier) {
      this.airportIdentifier = requireNonNull(airportIdentifier);
      return this;
    }

    public Builder airportRegion(String airportRegion) {
      this.airportRegion = requireNonNull(airportRegion);
      return this;
    }

    public Builder latitude(double latitude) {
      this.latitude = latitude;
      return this;
    }

    public Builder longitude(double longitude) {
      this.longitude = longitude;
      return this;
    }

    public Builder publishedVariation(Double publishedVariation) {
      this.publishedVariation = publishedVariation;
      return this;
    }

    public Builder modeledVariation(double modeledVariation) {
      this.modeledVariation = modeledVariation;
      return this;
    }

    public Builder runways(List<BoogieRunway> runways) {
      this.runways = runways;
      return this;
    }

    public Builder elevation(Double elevation) {
      this.elevation = elevation;
      return this;
    }

    public BoogieAirport build() {
      return new BoogieAirport(this);
    }
  }
}
