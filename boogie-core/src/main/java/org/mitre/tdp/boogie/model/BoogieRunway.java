package org.mitre.tdp.boogie.model;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Runway;

/**
 * Immutable, buildable implementation of the boogie {@link Runway} interface provided for convenience.
 */
public final class BoogieRunway implements Serializable, Runway {

  private final String runwayIdentifier;
  private final String airportIdentifier;
  private final String airportRegion;
  private final Double width;
  private final Double length;
  private final Double trueCourse;
  private final LatLong arrivalRunwayEnd;
  private final Integer landingThresholdElevation;
  private final LatLong departureRunwayEnd;
  private final Integer departureRunwayEndElevation;
  /**
   * The identifier of the primary ILS/GLS/MLS of the runway (if it exists).
   */
  private final String ilsGlsMls1;
  /**
   * The identifier of the secondary ILS/GLS/MLS of the runway (if it exists).
   */
  private final String ilsGlsMls2;

  private BoogieRunway(Builder builder) {
    this.runwayIdentifier = builder.runwayIdentifier;
    this.airportIdentifier = builder.airportIdentifier;
    this.airportRegion = builder.airportRegion;
    this.width = builder.width;
    this.length = builder.length;
    this.trueCourse = builder.trueCourse;
    this.arrivalRunwayEnd = builder.arrivalRunwayEnd;
    this.departureRunwayEnd = builder.departureRunwayEnd;
    this.ilsGlsMls1 = builder.ilsGlsMls1;
    this.ilsGlsMls2 = builder.ilsGlsMls2;
    this.landingThresholdElevation = builder.landingThresholdElevation;
    this.departureRunwayEndElevation = builder.departureRunwayEndElevation;
  }

  @Override
  public String runwayIdentifier() {
    return runwayIdentifier;
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
  public Optional<Double> width() {
    return Optional.ofNullable(width);
  }

  @Override
  public Optional<Double> length() {
    return Optional.ofNullable(length);
  }

  @Override
  public Optional<Double> trueCourse() {
    return Optional.ofNullable(trueCourse);
  }

  @Override
  public LatLong arrivalRunwayEnd() {
    return arrivalRunwayEnd;
  }

  @Override
  public Optional<Integer> landingThresholdElevation() {
    return Optional.ofNullable(landingThresholdElevation);
  }

  @Override
  public Optional<LatLong> departureRunwayEnd() {
    return Optional.ofNullable(departureRunwayEnd);
  }

  @Override
  public Optional<Integer> departureRunwayEndElevation() {
    return Optional.ofNullable(departureRunwayEndElevation);
  }

  public Optional<String> ilsGlsMls1() {
    return Optional.ofNullable(ilsGlsMls1);
  }

  public Optional<String> ilsGlsMls2() {
    return Optional.ofNullable(ilsGlsMls2);
  }

  public Builder toBuilder() {
    return new Builder()
        .runwayIdentifier(runwayIdentifier())
        .airportIdentifier(airportIdentifier())
        .airportRegion(airportRegion())
        .width(width().orElse(null))
        .length(length().orElse(null))
        .trueCourse(trueCourse().orElse(null))
        .arrivalRunwayEnd(arrivalRunwayEnd())
        .departureRunwayEnd(departureRunwayEnd().orElse(null))
        .ilsGlsMls1(ilsGlsMls1().orElse(null))
        .ilsGlsMls2(ilsGlsMls2().orElse(null))
        .landingThresholdElevation(landingThresholdElevation().orElse(null))
        .departureRunwayEndElevation(departureRunwayEndElevation().orElse(null));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoogieRunway that = (BoogieRunway) o;
    return Objects.equals(runwayIdentifier, that.runwayIdentifier) &&
        Objects.equals(airportIdentifier, that.airportIdentifier) &&
        Objects.equals(airportRegion, that.airportRegion) &&
        Objects.equals(width, that.width) &&
        Objects.equals(length, that.length) &&
        Objects.equals(trueCourse, that.trueCourse) &&
        Objects.equals(arrivalRunwayEnd, that.arrivalRunwayEnd) &&
        Objects.equals(departureRunwayEnd, that.departureRunwayEnd) &&
        Objects.equals(ilsGlsMls1, that.ilsGlsMls1) &&
        Objects.equals(ilsGlsMls2, that.ilsGlsMls2) &&
        Objects.equals(landingThresholdElevation, that.landingThresholdElevation) &&
        Objects.equals(departureRunwayEndElevation, that.departureRunwayEndElevation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(runwayIdentifier, airportIdentifier, airportRegion, width, length, trueCourse, arrivalRunwayEnd, departureRunwayEnd, ilsGlsMls1, ilsGlsMls2, landingThresholdElevation, departureRunwayEndElevation);
  }

  @Override
  public String toString() {
    return "BoogieRunway{" +
        "runwayIdentifier='" + runwayIdentifier + '\'' +
        ", airportIdentifier='" + airportIdentifier + '\'' +
        ", airportRegion='" + airportRegion + '\'' +
        ", width=" + width +
        ", length=" + length +
        ", trueCourse=" + trueCourse +
        ", arrivalRunwayEnd=" + arrivalRunwayEnd +
        ", departureRunwayEnd=" + departureRunwayEnd +
        ", ilsGlsMls1='" + ilsGlsMls1 + '\'' +
        ", ilsGlsMls2='" + ilsGlsMls2 + '\'' +
        '}';
  }

  public static final class Builder {
    private String runwayIdentifier;
    private String airportIdentifier;
    private String airportRegion;
    private Double width;
    private Double length;
    private Double trueCourse;
    private LatLong arrivalRunwayEnd;
    private LatLong departureRunwayEnd;
    private String ilsGlsMls1;
    private String ilsGlsMls2;
    private Integer landingThresholdElevation;
    private Integer departureRunwayEndElevation;

    public Builder runwayIdentifier(String runwayIdentifier) {
      this.runwayIdentifier = requireNonNull(runwayIdentifier);
      return this;
    }

    public Builder airportIdentifier(String airportIdentifier) {
      this.airportIdentifier = requireNonNull(airportIdentifier);
      return this;
    }

    public Builder airportRegion(String airportRegion) {
      this.airportRegion = requireNonNull(airportRegion);
      return this;
    }

    public Builder width(Double width) {
      this.width = width;
      return this;
    }

    public Builder length(Double length) {
      this.length = length;
      return this;
    }

    public Builder trueCourse(Double trueCourse) {
      this.trueCourse = trueCourse;
      return this;
    }

    public Builder arrivalRunwayEnd(LatLong arrivalRunwayEnd) {
      this.arrivalRunwayEnd = requireNonNull(arrivalRunwayEnd);
      return this;
    }

    public Builder departureRunwayEnd(LatLong departureRunwayEnd) {
      this.departureRunwayEnd = departureRunwayEnd;
      return this;
    }

    public Builder ilsGlsMls1(String ilsGlsMls1) {
      this.ilsGlsMls1 = ilsGlsMls1;
      return this;
    }

    public Builder ilsGlsMls2(String ilsGlsMls2) {
      this.ilsGlsMls2 = ilsGlsMls2;
      return this;
    }

    public Builder landingThresholdElevation(Integer landingThresholdElevation) {
      this.landingThresholdElevation = landingThresholdElevation;
      return this;
    }

    public Builder departureRunwayEndElevation(Integer departureRunwayEndElevation) {
      this.departureRunwayEndElevation = departureRunwayEndElevation;
      return this;
    }

    public BoogieRunway build() {
      return new BoogieRunway(this);
    }
  }
}
