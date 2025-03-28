package org.mitre.tdp.boogie.arinc.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

/**
 * Model class representing a GLS or PT record.
 */
public final class ArincGnssLandingSystem implements ArincModel {

  private final RecordType recordType;
  private final CustomerAreaCode customerAreaCode;
  private final SectionCode sectionCode;
  private final String airportIdentifier;
  private final String airportIcaoRegion;
  private final String subSectionCode;
  private final String glsRefPathIdentifier;
  private final String glsCategory;
  private final String continuationRecordNumber;
  private final int glsChannel;
  private final String runwayIdentifier;
  private final Double glsApproachBearing;
  private final double latitude;
  private final double longitude;
  private final String glsStationIdent;
  private final Integer serviceVolumeRadius;
  private final String tdmaSlots;
  private final Double glsApproachSlope;
  private final Double magneticVariation;
  private final Double stationElevation;
  private final String datumCode;
  private final String stationType;
  private final Double stationElevationWgs84;
  private final Integer glidePathTCH;
  private final int fileRecordNumber;
  private final String lastUpdatedCycle;

  public ArincGnssLandingSystem(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.airportIdentifier = builder.airportIdentifier;
    this.airportIcaoRegion = builder.airportIcaoRegion;
    this.subSectionCode = builder.subSectionCode;
    this.glsRefPathIdentifier = builder.glsRefPathIdentifier;
    this.glsCategory = builder.glsCategory;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.glsChannel = builder.glsChannel;
    this.runwayIdentifier = builder.runwayIdentifier;
    this.glsApproachBearing = builder.glsApproachBearing;
    this.latitude = builder.latitude;
    this.longitude = builder.longitude;
    this.glsStationIdent = builder.glsStationIdent;
    this.serviceVolumeRadius = builder.serviceVolumeRadius;
    this.tdmaSlots = builder.tdmaSlots;
    this.glsApproachSlope = builder.glsApproachSlope;
    this.magneticVariation = builder.magneticVariation;
    this.stationElevation = builder.stationElevation;
    this.datumCode = builder.datumCode;
    this.stationType = builder.stationType;
    this.stationElevationWgs84 = builder.stationElevationWgs84;
    this.glidePathTCH = builder.glidePathTCH;
    this.fileRecordNumber = builder.fileRecordNumber;
    this.lastUpdatedCycle = builder.lastUpdatedCycle;
  }

  public RecordType recordType() {
    return recordType;
  }

  public CustomerAreaCode customerAreaCode() {
    return customerAreaCode;
  }

  public String airportIdentifier() {
    return airportIdentifier;
  }

  public String airportIcaoRegion() {
    return airportIcaoRegion;
  }

  public String glsRefPathIdentifier() {
    return glsRefPathIdentifier;
  }

  public Optional<String> glsCategory() {
    return Optional.ofNullable(glsCategory);
  }

  public Optional<String> continuationRecordNumber() {
    return Optional.ofNullable(continuationRecordNumber);
  }

  public int glsChannel() {
    return glsChannel;
  }

  public String runwayIdentifier() {
    return runwayIdentifier;
  }

  public Optional<Double> glsApproachBearing() {
    return Optional.ofNullable(glsApproachBearing);
  }

  public double stationLatitude() {
    return latitude;
  }

  public double stationLongitude() {
    return longitude;
  }

  public Optional<String> glsStationIdent() {
    return Optional.ofNullable(glsStationIdent);
  }

  public Optional<Integer> serviceVolumeRadius() {
    return Optional.ofNullable(serviceVolumeRadius);
  }

  public Optional<String> tdmaSlots() {
    return Optional.ofNullable(tdmaSlots);
  }

  public Optional<Double> glsApproachSlope() {
    return Optional.ofNullable(glsApproachSlope);
  }

  public Double magneticVariation() {
    return magneticVariation;
  }

  public Optional<Double> stationElevation() {
    return Optional.ofNullable(stationElevation);
  }

  public Optional<String> datumCode() {
    return Optional.ofNullable(datumCode);
  }

  public Optional<String> stationType() {
    return Optional.ofNullable(stationType);
  }

  public Optional<Double> stationElevationWgs84() {
    return Optional.ofNullable(stationElevationWgs84);
  }

  public Optional<Integer> glidePathTCH() {
    return Optional.ofNullable(glidePathTCH);
  }

  public int fileRecordNumber() {
    return fileRecordNumber;
  }

  public String lastUpdatedCycle() {
    return lastUpdatedCycle;
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincGnssLandingSystem that = (ArincGnssLandingSystem) o;
    return glsChannel == that.glsChannel
        && Double.compare(latitude, that.latitude) == 0
        && Double.compare(longitude, that.longitude) == 0
        && fileRecordNumber == that.fileRecordNumber
        && recordType == that.recordType
        && customerAreaCode == that.customerAreaCode
        && sectionCode == that.sectionCode
        && Objects.equals(airportIdentifier, that.airportIdentifier)
        && Objects.equals(airportIcaoRegion, that.airportIcaoRegion)
        && Objects.equals(subSectionCode, that.subSectionCode)
        && Objects.equals(glsRefPathIdentifier, that.glsRefPathIdentifier)
        && Objects.equals(glsCategory, that.glsCategory)
        && Objects.equals(continuationRecordNumber, that.continuationRecordNumber)
        && Objects.equals(runwayIdentifier, that.runwayIdentifier)
        && Objects.equals(glsApproachBearing, that.glsApproachBearing)
        && Objects.equals(glsStationIdent, that.glsStationIdent)
        && Objects.equals(serviceVolumeRadius, that.serviceVolumeRadius)
        && Objects.equals(tdmaSlots, that.tdmaSlots)
        && Objects.equals(glsApproachSlope, that.glsApproachSlope)
        && Objects.equals(magneticVariation, that.magneticVariation)
        && Objects.equals(stationElevation, that.stationElevation)
        && Objects.equals(datumCode, that.datumCode)
        && Objects.equals(stationType, that.stationType)
        && Objects.equals(stationElevationWgs84, that.stationElevationWgs84)
        && Objects.equals(glidePathTCH, that.glidePathTCH)
        && Objects.equals(lastUpdatedCycle, that.lastUpdatedCycle);
  }

  public Builder toBuilder() {
    return new Builder()
        .recordType(recordType())
        .customerAreaCode(customerAreaCode())
        .sectionCode(sectionCode())
        .airportIdentifier(airportIdentifier())
        .airportIcaoRegion(airportIcaoRegion())
        .subSectionCode(subSectionCode().orElse(null))
        .glsRefPathIdentifier(glsRefPathIdentifier())
        .glsCategory(glsCategory().orElse(null))
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .glsChannel(glsChannel())
        .runwayIdentifier(runwayIdentifier())
        .glsApproachBearing(glsApproachBearing().orElse(null))
        .latitude(stationLatitude())
        .longitude(stationLongitude())
        .glsStationIdent(glsStationIdent().orElse(null))
        .serviceVolumeRadius(serviceVolumeRadius().orElse(null))
        .tdmaSlots(tdmaSlots().orElse(null))
        .glsApproachSlope(glsApproachSlope().orElse(null))
        .magneticVariation(magneticVariation())
        .stationElevation(stationElevation().orElse(null))
        .datumCode(datumCode().orElse(null))
        .stationType(stationType().orElse(null))
        .stationElevationWgs84(stationElevationWgs84().orElse(null))
        .glidePathTCH(glidePathTCH().orElse(null))
        .fileRecordNumber(fileRecordNumber())
        .lastUpdatedCycle(lastUpdatedCycle());
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, airportIdentifier, airportIcaoRegion, subSectionCode, glsRefPathIdentifier, glsCategory, continuationRecordNumber, glsChannel, runwayIdentifier, glsApproachBearing, latitude, longitude, glsStationIdent, serviceVolumeRadius, tdmaSlots, glsApproachSlope, magneticVariation, stationElevation, datumCode, stationType, stationElevationWgs84, glidePathTCH, fileRecordNumber, lastUpdatedCycle);
  }

  public static final class Builder {
    private RecordType recordType;
    private CustomerAreaCode customerAreaCode;
    private SectionCode sectionCode;
    private String airportIdentifier;
    private String airportIcaoRegion;
    private String subSectionCode;
    private String glsRefPathIdentifier;
    private String glsCategory;
    private String continuationRecordNumber;
    private int glsChannel;
    private String runwayIdentifier;
    private Double glsApproachBearing;
    private double latitude;
    private double longitude;
    private String glsStationIdent;
    private Integer serviceVolumeRadius;
    private String tdmaSlots;
    private Double glsApproachSlope;
    private Double magneticVariation;
    private Double stationElevation;
    private String datumCode;
    private String stationType;
    private Double stationElevationWgs84;
    private Integer glidePathTCH;
    private int fileRecordNumber;
    private String lastUpdatedCycle;

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

    public Builder airportIdentifier(String airportIdentifier) {
      this.airportIdentifier = airportIdentifier;
      return this;
    }

    public Builder airportIcaoRegion(String airportIcaoRegion) {
      this.airportIcaoRegion = airportIcaoRegion;
      return this;
    }

    public Builder subSectionCode(String subSectionCode) {
      this.subSectionCode = subSectionCode;
      return this;
    }

    public Builder glsRefPathIdentifier(String glsRefPathIdentifier) {
      this.glsRefPathIdentifier = glsRefPathIdentifier;
      return this;
    }

    public Builder glsCategory(String glsCategory) {
      this.glsCategory = glsCategory;
      return this;
    }

    public Builder continuationRecordNumber(String continuationRecordNumber) {
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder glsChannel(int glsChannel) {
      this.glsChannel = glsChannel;
      return this;
    }

    public Builder runwayIdentifier(String runwayIdentifier) {
      this.runwayIdentifier = runwayIdentifier;
      return this;
    }

    public Builder glsApproachBearing(Double glsApproachBearing) {
      this.glsApproachBearing = glsApproachBearing;
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

    public Builder glsStationIdent(String glsStationIdent) {
      this.glsStationIdent = glsStationIdent;
      return this;
    }

    public Builder serviceVolumeRadius(Integer serviceVolumeRadius) {
      this.serviceVolumeRadius = serviceVolumeRadius;
      return this;
    }

    public Builder tdmaSlots(String tdmaSlots) {
      this.tdmaSlots = tdmaSlots;
      return this;
    }

    public Builder glsApproachSlope(Double glsApproachSlope) {
      this.glsApproachSlope = glsApproachSlope;
      return this;
    }

    public Builder magneticVariation(Double magneticVariation) {
      this.magneticVariation = magneticVariation;
      return this;
    }

    public Builder stationElevation(Double stationElevation) {
      this.stationElevation = stationElevation;
      return this;
    }

    public Builder datumCode(String datumCode) {
      this.datumCode = datumCode;
      return this;
    }

    public Builder stationType(String stationType) {
      this.stationType = stationType;
      return this;
    }

    public Builder stationElevationWgs84(Double stationElevationWgs84) {
      this.stationElevationWgs84 = stationElevationWgs84;
      return this;
    }

    public Builder glidePathTCH(Integer glidePathTCH) {
      this.glidePathTCH = glidePathTCH;
      return this;
    }

    public Builder fileRecordNumber(int fileRecordNumber) {
      this.fileRecordNumber = fileRecordNumber;
      return this;
    }

    public Builder lastUpdatedCycle(String lastUpdatedCycle) {
      this.lastUpdatedCycle = lastUpdatedCycle;
      return this;
    }

    public ArincGnssLandingSystem build() {
      return new ArincGnssLandingSystem(this);
    }
  }
}
