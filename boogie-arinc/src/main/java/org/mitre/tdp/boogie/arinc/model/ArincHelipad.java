package org.mitre.tdp.boogie.arinc.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.ComponentElevation;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.Latitude;
import org.mitre.tdp.boogie.arinc.v18.field.LongestRunwaySurfaceCode;
import org.mitre.tdp.boogie.arinc.v18.field.Longitude;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v21.field.MaximumAllowableHelicopterWeight;
import org.mitre.tdp.boogie.arinc.v21.field.PadDimensions;
import org.mitre.tdp.boogie.arinc.v21.field.PadIdentifier;
import org.mitre.tdp.boogie.arinc.v21.field.PadShape;
import org.mitre.tdp.boogie.arinc.v21.field.SurfaceType;

/**
 * The Arinc helipad is a rectangular or circular place for helicopters to land.
 * These can be at heliports or airports.
 */
public final class ArincHelipad implements ArincModel {
  /**
   * See {@link RecordType}.
   */
  private final RecordType recordType;
  /**
   * See {@link CustomerAreaCode}.
   */
  private final CustomerAreaCode customerAreaCode;
  /**
   * See {@link SectionCode} - always "P" or "H" for airport/heliport pads.
   */
  private final SectionCode sectionCode;
  /**
   * See {@link SubSectionCode} - always "H" for airport helipads
   */
  private final String subSectionCode;
  /**
   * See {@link AirportHeliportIdentifier}
   */
  private final String airportHeliportIdentifier;
  /**
   * See {@link IcaoRegion}
   */
  private final String icaoCode;
  /**
   * See {@link PadIdentifier}
   */
  private final String helipadIdentifier;
  /**
   * See {@link ContinuationRecordNumber}
   */
  private final String continuationRecordNumber;

  /**
   * See {@link PadShape}
   */
  private final String padShape;
  /**
   * This field is parsed from {@link PadDimensions}
   * This field is the width in feet if the pad is a runway.
   */
  private final Double padXDimension;
  /**
   * This field is parsed from {@link PadDimensions}
   * This field is the length in feet if the pad is a runway.
   */
  private final Double padYDimension;
  /**
   * This field is parsed from {@link PadDimensions}
   * This field is the diameter in feet of the pad if it is circular.
   */
  private final Double padDiameter;
  /**
   * See {@link Latitude}
   */
  private final Double latitude;
  /**
   * See {@link Longitude}
   */
  private final Double longitude;
  /**
   * See {@link LongestRunwaySurfaceCode}
   */
  private final String helipadSurfaceCode;
  /**
   * See {@link SurfaceType}
   */
  private final String helipadSurfaceType;
  /**
   * See {@link MaximumAllowableHelicopterWeight}
   */
  private final Integer maximumHelicopterWeight;
  private final String helicopterPerformanceRequirement;
  /**
   * See {@link ComponentElevation}
   */
  private final Double padElevation;
  /**
   * See {@link FileRecordNumber}
   */
  private final Integer fileRecordNumber;
  /**
   * See {@link Cycle}
   */
  private final String cycle;

  public ArincHelipad(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.subSectionCode = builder.subSectionCode;
    this.airportHeliportIdentifier = builder.airportHeliportIdentifier;
    this.icaoCode = builder.icaoCode;
    this.helipadIdentifier = builder.helipadIdentifier;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.padShape = builder.padShape;
    this.padXDimension = builder.padXDimension;
    this.padYDimension = builder.padYDimension;
    this.padDiameter = builder.padDiameter;
    this.latitude = builder.latitude;
    this.longitude = builder.longitude;
    this.helipadSurfaceCode = builder.helipadSurfaceCode;
    this.helipadSurfaceType = builder.helipadSurfaceType;
    this.maximumHelicopterWeight = builder.maximumHelicopterWeight;
    this.helicopterPerformanceRequirement = builder.helicopterPerformanceRequirement;
    this.padElevation = builder.padElevation;
    this.fileRecordNumber = builder.fileRecordNumber;
    this.cycle = builder.cycle;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .recordType(this.recordType)
        .customerAreaCode(this.customerAreaCode)
        .sectionCode(this.sectionCode)
        .subSectionCode(this.subSectionCode)
        .airportHeliportIdentifier(this.airportHeliportIdentifier)
        .icaoCode(this.icaoCode)
        .helipadIdentifier(this.helipadIdentifier)
        .continuationRecordNumber(this.continuationRecordNumber)
        .padShape(this.padShape)
        .padXDimension(this.padXDimension)
        .padYDimension(this.padYDimension)
        .padDiameter(this.padDiameter)
        .latitude(this.latitude)
        .longitude(this.longitude)
        .helipadSurfaceCode(this.helipadSurfaceCode)
        .helipadSurfaceType(this.helipadSurfaceType)
        .maximumHelicopterWeight(this.maximumHelicopterWeight)
        .helicopterPerformanceRequirement(this.helicopterPerformanceRequirement)
        .padElevation(this.padElevation)
        .fileRecordNumber(this.fileRecordNumber)
        .cycle(this.cycle);
  }

  public RecordType recordType() {
    return recordType;
  }

  public CustomerAreaCode customerAreaCode() {
    return customerAreaCode;
  }

  public String airportHeliportIdentifier() {
    return airportHeliportIdentifier;
  }

  public String icaoCode() {
    return icaoCode;
  }

  public String helipadIdentifier() {
    return helipadIdentifier;
  }

  public String padShape() {
    return padShape;
  }

  public Double latitude() {
    return latitude;
  }

  public Double longitude() {
    return longitude;
  }

  public Integer fileRecordNumber() {
    return fileRecordNumber;
  }

  public String cycle() {
    return cycle;
  }

  public Optional<Double> padXDimension() {
    return Optional.ofNullable(padXDimension);
  }

  public Optional<Double> padYDimension() {
    return Optional.ofNullable(padYDimension);
  }

  public Optional<Double> padDiameter() {
    return Optional.ofNullable(padDiameter);
  }

  public Optional<String> helipadSurfaceCode() {
    return Optional.ofNullable(helipadSurfaceCode);
  }

  public Optional<String> helipadSurfaceType() {
    return Optional.ofNullable(helipadSurfaceType);
  }

  public Optional<Integer> maximumHelicopterWeight() {
    return Optional.ofNullable(maximumHelicopterWeight);
  }

  public Optional<String> helicopterPerformanceRequirement() {
    return Optional.ofNullable(helicopterPerformanceRequirement);
  }

  public Optional<Double> padElevation() {
    return Optional.ofNullable(padElevation);
  }

  @Override
  public SectionCode sectionCode() {
    return sectionCode;
  }

  @Override
  public Optional<String> subSectionCode() {
    return Optional.ofNullable(subSectionCode);
  }

  @Override
  public Optional<String> continuationRecordNumber() {
    return Optional.ofNullable(continuationRecordNumber);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ArincHelipad that = (ArincHelipad) o;
    return recordType == that.recordType
        && customerAreaCode == that.customerAreaCode
        && sectionCode == that.sectionCode
        && Objects.equals(subSectionCode, that.subSectionCode)
        && Objects.equals(airportHeliportIdentifier, that.airportHeliportIdentifier)
        && Objects.equals(icaoCode, that.icaoCode)
        && Objects.equals(helipadIdentifier, that.helipadIdentifier)
        && Objects.equals(continuationRecordNumber, that.continuationRecordNumber)
        && Objects.equals(padShape, that.padShape)
        && Objects.equals(padXDimension, that.padXDimension)
        && Objects.equals(padYDimension, that.padYDimension)
        && Objects.equals(padDiameter, that.padDiameter)
        && Objects.equals(latitude, that.latitude)
        && Objects.equals(longitude, that.longitude)
        && Objects.equals(helipadSurfaceCode, that.helipadSurfaceCode)
        && Objects.equals(helipadSurfaceType, that.helipadSurfaceType)
        && Objects.equals(maximumHelicopterWeight, that.maximumHelicopterWeight)
        && Objects.equals(helicopterPerformanceRequirement, that.helicopterPerformanceRequirement)
        && Objects.equals(padElevation, that.padElevation)
        && Objects.equals(fileRecordNumber, that.fileRecordNumber)
        && Objects.equals(cycle, that.cycle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, subSectionCode, airportHeliportIdentifier, icaoCode, helipadIdentifier, continuationRecordNumber, padShape, padXDimension, padYDimension, padDiameter, latitude, longitude, helipadSurfaceCode, helipadSurfaceType, maximumHelicopterWeight, helicopterPerformanceRequirement, padElevation, fileRecordNumber, cycle);
  }

  public static class Builder {
    private RecordType recordType;
    private CustomerAreaCode customerAreaCode;
    private SectionCode sectionCode;
    private String subSectionCode;
    private String airportHeliportIdentifier;
    private String icaoCode;
    private String helipadIdentifier;
    private String continuationRecordNumber;
    private String padShape;
    private Double padXDimension;
    private Double padYDimension;
    private Double padDiameter;
    private Double latitude;
    private Double longitude;
    private String helipadSurfaceCode;
    private String helipadSurfaceType;
    private Integer maximumHelicopterWeight;
    private String helicopterPerformanceRequirement;
    private Double padElevation;
    private Integer fileRecordNumber;
    private String cycle;

    public Builder recordType(RecordType recordType) {
      this.recordType = recordType;
      return this;
    }

    public Builder customerAreaCode(CustomerAreaCode customerAreaCode) {
      this.customerAreaCode = customerAreaCode;
      return this;
    }

    public Builder sectionCode(SectionCode sectionCode) {
      this.sectionCode = sectionCode;
      return this;
    }

    public Builder subSectionCode(String subSectionCode) {
      this.subSectionCode = subSectionCode;
      return this;
    }

    public Builder airportHeliportIdentifier(String airportHeliportIdentifier) {
      this.airportHeliportIdentifier = airportHeliportIdentifier;
      return this;
    }

    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder helipadIdentifier(String helipadIdentifier) {
      this.helipadIdentifier = helipadIdentifier;
      return this;
    }

    public Builder continuationRecordNumber(String continuationRecordNumber) {
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder padShape(String padShape) {
      this.padShape = padShape;
      return this;
    }

    public Builder padXDimension(Double padXDimension) {
      this.padXDimension = padXDimension;
      return this;
    }

    public Builder padYDimension(Double padYDimension) {
      this.padYDimension = padYDimension;
      return this;
    }

    public Builder padDiameter(Double padRadius) {
      this.padDiameter = padRadius;
      return this;
    }

    public Builder latitude(Double latitude) {
      this.latitude = latitude;
      return this;
    }

    public Builder longitude(Double longitude) {
      this.longitude = longitude;
      return this;
    }

    public Builder helipadSurfaceCode(String helipadSurfaceCode) {
      this.helipadSurfaceCode = helipadSurfaceCode;
      return this;
    }

    public Builder helipadSurfaceType(String helipadSurfaceType) {
      this.helipadSurfaceType = helipadSurfaceType;
      return this;
    }

    public Builder maximumHelicopterWeight(Integer maximumHelicopterWeight) {
      this.maximumHelicopterWeight = maximumHelicopterWeight;
      return this;
    }

    public Builder helicopterPerformanceRequirement(String helicopterPerformanceRequirement) {
      this.helicopterPerformanceRequirement = helicopterPerformanceRequirement;
      return this;
    }

    public Builder padElevation(Double padElevation) {
      this.padElevation = padElevation;
      return this;
    }

    public Builder fileRecordNumber(Integer fileRecordNumber) {
      this.fileRecordNumber = fileRecordNumber;
      return this;
    }

    public Builder cycle(String cycle) {
      this.cycle = cycle;
      return this;
    }

    public ArincHelipad build() {
      return new ArincHelipad(this);
    }
  }
}
