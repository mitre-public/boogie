package org.mitre.boogie.xml.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.RunwaySurfaceCode;

public final class ArincAirport {
  private final ArincPortInfo portInfo;
  private final Long longestRunway;
  private final String longestRunwaySurfaceCode;
  private final List<ArincRunway> runways;
  private final List<ArincAirportGate> airportGates;

  public ArincAirport(Builder builder) {
    this.portInfo = builder.portInfo;
    this.longestRunway = builder.longestRunway;
    this.longestRunwaySurfaceCode = builder.longestRunwaySurfaceCode;
    this.runways = builder.runways;
    this.airportGates = builder.airportGates;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .portInfo(portInfo)
        .longestRunway(longestRunway)
        .longestRunwaySurfaceCode(longestRunwaySurfaceCode().map(Enum::name).orElse(null))
        .runways(runways)
        .airportGates(airportGates);
  }

  public ArincPortInfo portInfo() {
    return portInfo;
  }

  public Optional<Long> longestRunway() {
    return Optional.ofNullable(longestRunway);
  }

  public Optional<RunwaySurfaceCode> longestRunwaySurfaceCode() {
    return Optional.ofNullable(longestRunwaySurfaceCode)
        .filter(RunwaySurfaceCode.VALID::contains)
        .map(RunwaySurfaceCode::valueOf);
  }

  public List<ArincRunway> runways() {
    return Optional.ofNullable(runways).orElse(List.of());
  }

  public List<ArincAirportGate> airportGates() {
    return Optional.ofNullable(airportGates).orElse(List.of());
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    ArincAirport that = (ArincAirport) o;
    return Objects.equals(portInfo, that.portInfo) && Objects.equals(longestRunway, that.longestRunway) && Objects.equals(longestRunwaySurfaceCode, that.longestRunwaySurfaceCode) && Objects.equals(runways, that.runways) && Objects.equals(airportGates, that.airportGates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(portInfo, longestRunway, longestRunwaySurfaceCode, runways, airportGates);
  }

  @Override
  public String toString() {
    return "ArincAirport{" +
        "portInfo=" + portInfo +
        ", longestRunway=" + longestRunway +
        ", longestRunwaySurfaceCode='" + longestRunwaySurfaceCode + '\'' +
        ", runways=" + runways +
        ", airportGates=" + airportGates +
        '}';
  }

  public static class Builder {
    private ArincPortInfo portInfo;
    private Long longestRunway;
    private String longestRunwaySurfaceCode;
    private List<ArincRunway> runways;
    private List<ArincAirportGate> airportGates;

    private Builder() {
    }

    public Builder portInfo(ArincPortInfo portInfo) {
      this.portInfo = portInfo;
      return this;
    }

    public Builder longestRunway(Long longestRunway) {
      this.longestRunway = longestRunway;
      return this;
    }

    public Builder longestRunwaySurfaceCode(String longestRunwaySurfaceCode) {
      this.longestRunwaySurfaceCode = longestRunwaySurfaceCode;
      return this;
    }

    public Builder runways(List<ArincRunway> runways) {
      this.runways = runways;
      return this;
    }

    public Builder airportGates(List<ArincAirportGate> airportGates) {
      this.airportGates = airportGates;
      return this;
    }

    public ArincAirport build() {
      return new ArincAirport(this);
    }
  }
}
