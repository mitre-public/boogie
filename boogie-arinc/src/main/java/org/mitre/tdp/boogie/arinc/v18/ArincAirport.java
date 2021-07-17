package org.mitre.tdp.boogie.arinc.v18;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.LongestRunwaySurfaceCode;
import org.mitre.tdp.boogie.arinc.v18.field.MagneticTrueIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimit;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimitAltitude;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;

/**
 * Data class for representing structured/parsed content from within an ARINC format airport record.
 * <br>
 * Section/Subsection = PA
 */
public final class ArincAirport {

  /**
   * See {@link RecordType}.
   */
  private final RecordType recordType;
  /**
   * See {@link CustomerAreaCode}.
   */
  private final CustomerAreaCode customerAreaCode;
  /**
   * See {@link SectionCode}.
   */
  private final SectionCode sectionCode;
  /**
   * See {@link AirportHeliportIdentifier}.
   */
  private final String airportIdentifier;
  /**
   * See {@link IcaoRegion}.
   */
  private final String airportIcaoRegion;
  /**
   * See {@link SubSectionCode}.
   */
  private final String subSectionCode;
  private final String iataDesignator;
  /**
   * See {@link ContinuationRecordNumber}.
   */
  private final String continuationRecordNumber;
  /**
   * See {@link SpeedLimitAltitude}.
   */
  private final Double speedLimitAltitude;
  private final Integer longestRunway;
  private final Boolean ifrCapability;
  private final LongestRunwaySurfaceCode longestRunwaySurfaceCode;
  private final Double latitude;
  private final Double longitude;
  private final Double magneticVariation;
  private final Double airportElevation;
  /**
   * See {@link SpeedLimit}.
   */
  private final Integer speedLimit;
  private final String recommendedNavaid;
  private final String recommendedNavaidIcaoRegion;
  private final Double transitionAltitude;
  private final Double transitionLevel;
  private final PublicMilitaryIndicator publicMilitaryIndicator;
  private final Boolean daylightTimeIndicator;
  private final MagneticTrueIndicator magneticTrueIndicator;
  private final String datumCode;
  private final String airportFullName;
  private final Integer fileRecordNumber;
  private final String lastUpdateCycle;

  private ArincAirport(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.airportIdentifier = builder.airportIdentifier;
    this.airportIcaoRegion = builder.airportIcaoRegion;
    this.subSectionCode = builder.subSectionCode;
    this.iataDesignator = builder.iataDesignator;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.speedLimitAltitude = builder.speedLimitAltitude;
    this.longestRunway = builder.longestRunway;
    this.ifrCapability = builder.ifrCapability;
    this.longestRunwaySurfaceCode = builder.longestRunwaySurfaceCode;
    this.latitude = builder.latitude;
    this.longitude = builder.longitude;
    this.magneticVariation = builder.magneticVariation;
    this.airportElevation = builder.airportElevation;
    this.speedLimit = builder.speedLimit;
    this.recommendedNavaid = builder.recommendedNavaid;
    this.recommendedNavaidIcaoRegion = builder.recommendedNavaidIcaoRegion;
    this.transitionAltitude = builder.transitionAltitude;
    this.transitionLevel = builder.transitionLevel;
    this.publicMilitaryIndicator = builder.publicMilitaryIndicator;
    this.daylightTimeIndicator = builder.daylightTimeIndicator;
    this.magneticTrueIndicator = builder.magneticTrueIndicator;
    this.datumCode = builder.datumCode;
    this.airportFullName = builder.airportFullName;
    this.fileRecordNumber = builder.fileRecordNumber;
    this.lastUpdateCycle = builder.lastUpdateCycle;
  }

  public RecordType recordType() {
    return recordType;
  }

  public Optional<CustomerAreaCode> customerAreaCode() {
    return Optional.ofNullable(customerAreaCode);
  }

  public SectionCode sectionCode() {
    return sectionCode;
  }

  public String airportIdentifier() {
    return airportIdentifier;
  }

  public String airportIcaoRegion() {
    return airportIcaoRegion;
  }

  public String subSectionCode() {
    return subSectionCode;
  }

  public Optional<String> iataDesignator() {
    return Optional.ofNullable(iataDesignator);
  }

  public Optional<String> continuationRecordNumber() {
    return Optional.ofNullable(continuationRecordNumber);
  }

  public Optional<Double> speedLimitAltitude() {
    return Optional.ofNullable(speedLimitAltitude);
  }

  public Optional<Integer> longestRunway() {
    return Optional.ofNullable(longestRunway);
  }

  public Optional<Boolean> ifrCapability() {
    return Optional.ofNullable(ifrCapability);
  }

  public Optional<LongestRunwaySurfaceCode> longestRunwaySurfaceCode() {
    return Optional.ofNullable(longestRunwaySurfaceCode);
  }

  public Double latitude() {
    return latitude;
  }

  public Double longitude() {
    return longitude;
  }

  public Optional<Double> magneticVariation() {
    return Optional.ofNullable(magneticVariation);
  }

  public Optional<Double> airportElevation() {
    return Optional.ofNullable(airportElevation);
  }

  public Optional<Integer> speedLimit() {
    return Optional.ofNullable(speedLimit);
  }

  public Optional<String> recommendedNavaid() {
    return Optional.ofNullable(recommendedNavaid);
  }

  public Optional<String> recommendedNavaidIcaoRegion() {
    return Optional.ofNullable(recommendedNavaidIcaoRegion);
  }

  public Optional<Double> transitionAltitude() {
    return Optional.ofNullable(transitionAltitude);
  }

  public Optional<Double> transitionLevel() {
    return Optional.ofNullable(transitionLevel);
  }

  public Optional<PublicMilitaryIndicator> publicMilitaryIndicator() {
    return Optional.ofNullable(publicMilitaryIndicator);
  }

  public Optional<Boolean> daylightTimeIndicator() {
    return Optional.ofNullable(daylightTimeIndicator);
  }

  public Optional<MagneticTrueIndicator> magneticTrueIndicator() {
    return Optional.ofNullable(magneticTrueIndicator);
  }

  public Optional<String> datumCode() {
    return Optional.ofNullable(datumCode);
  }

  public Optional<String> airportFullName() {
    return Optional.ofNullable(airportFullName);
  }

  public Integer fileRecordNumber() {
    return fileRecordNumber;
  }

  public String lastUpdateCycle() {
    return lastUpdateCycle;
  }

  public Builder toBuilder() {
    return new Builder()
        .recordType(recordType())
        .customerAreaCode(customerAreaCode().orElse(null))
        .sectionCode(sectionCode())
        .airportIdentifier(airportIdentifier())
        .airportIcaoRegion(airportIcaoRegion())
        .subSectionCode(subSectionCode())
        .iataDesignator(iataDesignator().orElse(null))
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .speedLimitAltitude(speedLimitAltitude().orElse(null))
        .longestRunway(longestRunway().orElse(null))
        .ifrCapability(ifrCapability().orElse(null))
        .longestRunwaySurfaceCode(longestRunwaySurfaceCode().orElse(null))
        .latitude(latitude())
        .longitude(longitude())
        .magneticVariation(magneticVariation().orElse(null))
        .airportElevation(airportElevation().orElse(null))
        .speedLimit(speedLimit().orElse(null))
        .recommendedNavaid(recommendedNavaid().orElse(null))
        .recommendedNavaidIcaoRegion(recommendedNavaidIcaoRegion().orElse(null))
        .transitionAltitude(transitionAltitude().orElse(null))
        .transitionLevel(transitionLevel().orElse(null))
        .publicMilitaryIndicator(publicMilitaryIndicator().orElse(null))
        .daylightTimeIndicator(daylightTimeIndicator().orElse(null))
        .magneticTrueIndicator(magneticTrueIndicator().orElse(null))
        .datumCode(datumCode().orElse(null))
        .airportFullName(airportFullName().orElse(null))
        .fileRecordNumber(fileRecordNumber())
        .lastUpdateCycle(lastUpdateCycle());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincAirport that = (ArincAirport) o;
    return recordType == that.recordType &&
        customerAreaCode == that.customerAreaCode &&
        sectionCode == that.sectionCode &&
        Objects.equals(airportIdentifier, that.airportIdentifier) &&
        Objects.equals(airportIcaoRegion, that.airportIcaoRegion) &&
        Objects.equals(subSectionCode, that.subSectionCode) &&
        Objects.equals(iataDesignator, that.iataDesignator) &&
        Objects.equals(continuationRecordNumber, that.continuationRecordNumber) &&
        Objects.equals(speedLimitAltitude, that.speedLimitAltitude) &&
        Objects.equals(longestRunway, that.longestRunway) &&
        Objects.equals(ifrCapability, that.ifrCapability) &&
        longestRunwaySurfaceCode == that.longestRunwaySurfaceCode &&
        Objects.equals(latitude, that.latitude) &&
        Objects.equals(longitude, that.longitude) &&
        Objects.equals(magneticVariation, that.magneticVariation) &&
        Objects.equals(airportElevation, that.airportElevation) &&
        Objects.equals(speedLimit, that.speedLimit) &&
        Objects.equals(recommendedNavaid, that.recommendedNavaid) &&
        Objects.equals(recommendedNavaidIcaoRegion, that.recommendedNavaidIcaoRegion) &&
        Objects.equals(transitionAltitude, that.transitionAltitude) &&
        Objects.equals(transitionLevel, that.transitionLevel) &&
        publicMilitaryIndicator == that.publicMilitaryIndicator &&
        Objects.equals(daylightTimeIndicator, that.daylightTimeIndicator) &&
        magneticTrueIndicator == that.magneticTrueIndicator &&
        Objects.equals(datumCode, that.datumCode) &&
        Objects.equals(airportFullName, that.airportFullName) &&
        Objects.equals(fileRecordNumber, that.fileRecordNumber) &&
        Objects.equals(lastUpdateCycle, that.lastUpdateCycle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, airportIdentifier, airportIcaoRegion, subSectionCode, iataDesignator, continuationRecordNumber, speedLimitAltitude, longestRunway, ifrCapability, longestRunwaySurfaceCode, latitude, longitude, magneticVariation, airportElevation, speedLimit, recommendedNavaid, recommendedNavaidIcaoRegion, transitionAltitude, transitionLevel, publicMilitaryIndicator, daylightTimeIndicator, magneticTrueIndicator, datumCode, airportFullName, fileRecordNumber, lastUpdateCycle);
  }

  @Override
  public String toString() {
    return "ArincAirport2{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode='" + sectionCode + '\'' +
        ", airportIdentifier='" + airportIdentifier + '\'' +
        ", airportIcaoRegion='" + airportIcaoRegion + '\'' +
        ", subSectionCode='" + subSectionCode + '\'' +
        ", iataDesignator='" + iataDesignator + '\'' +
        ", continuationRecordNumber='" + continuationRecordNumber + '\'' +
        ", speedLimitAltitude=" + speedLimitAltitude +
        ", longestRunway=" + longestRunway +
        ", ifrCapability=" + ifrCapability +
        ", longestRunwaySurfaceCode=" + longestRunwaySurfaceCode +
        ", latitude=" + latitude +
        ", longitude=" + longitude +
        ", magneticVariation=" + magneticVariation +
        ", airportElevation=" + airportElevation +
        ", speedLimit=" + speedLimit +
        ", recommendedNavaid='" + recommendedNavaid + '\'' +
        ", recommendedNavaidIcaoRegion='" + recommendedNavaidIcaoRegion + '\'' +
        ", transitionAltitude=" + transitionAltitude +
        ", transitionLevel=" + transitionLevel +
        ", publicMilitaryIndicator=" + publicMilitaryIndicator +
        ", daylightTimeIndicator=" + daylightTimeIndicator +
        ", magneticTrueIndicator=" + magneticTrueIndicator +
        ", datumCode='" + datumCode + '\'' +
        ", airportFullName='" + airportFullName + '\'' +
        ", fileRecordNumber=" + fileRecordNumber +
        ", lastUpdateCycle='" + lastUpdateCycle + '\'' +
        '}';
  }

  public static final class Builder {
    private RecordType recordType;
    private CustomerAreaCode customerAreaCode;
    private SectionCode sectionCode;
    private String airportIdentifier;
    private String airportIcaoRegion;
    private String subSectionCode;
    private String iataDesignator;
    private String continuationRecordNumber;
    private Double speedLimitAltitude;
    private Integer longestRunway;
    private Boolean ifrCapability;
    private LongestRunwaySurfaceCode longestRunwaySurfaceCode;
    private Double latitude;
    private Double longitude;
    private Double magneticVariation;
    private Double airportElevation;
    private Integer speedLimit;
    private String recommendedNavaid;
    private String recommendedNavaidIcaoRegion;
    private Double transitionAltitude;
    private Double transitionLevel;
    private PublicMilitaryIndicator publicMilitaryIndicator;
    private Boolean daylightTimeIndicator;
    private MagneticTrueIndicator magneticTrueIndicator;
    private String datumCode;
    private String airportFullName;
    private Integer fileRecordNumber;
    private String lastUpdateCycle;

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

    public Builder iataDesignator(String iataDesignator) {
      this.iataDesignator = iataDesignator;
      return this;
    }

    public Builder continuationRecordNumber(String continuationRecordNumber) {
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder speedLimitAltitude(Double speedLimitAltitude) {
      this.speedLimitAltitude = speedLimitAltitude;
      return this;
    }

    public Builder longestRunway(Integer longestRunway) {
      this.longestRunway = longestRunway;
      return this;
    }

    public Builder ifrCapability(Boolean ifrCapability) {
      this.ifrCapability = ifrCapability;
      return this;
    }

    public Builder longestRunwaySurfaceCode(LongestRunwaySurfaceCode longestRunwaySurfaceCode) {
      this.longestRunwaySurfaceCode = longestRunwaySurfaceCode;
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

    public Builder magneticVariation(Double magneticVariation) {
      this.magneticVariation = magneticVariation;
      return this;
    }

    public Builder airportElevation(Double airportElevation) {
      this.airportElevation = airportElevation;
      return this;
    }

    public Builder speedLimit(Integer speedLimit) {
      this.speedLimit = speedLimit;
      return this;
    }

    public Builder recommendedNavaid(String recommendedNavaid) {
      this.recommendedNavaid = recommendedNavaid;
      return this;
    }

    public Builder recommendedNavaidIcaoRegion(String recommendedNavaidIcaoRegion) {
      this.recommendedNavaidIcaoRegion = recommendedNavaidIcaoRegion;
      return this;
    }

    public Builder transitionAltitude(Double transitionAltitude) {
      this.transitionAltitude = transitionAltitude;
      return this;
    }

    public Builder transitionLevel(Double transitionLevel) {
      this.transitionLevel = transitionLevel;
      return this;
    }

    public Builder publicMilitaryIndicator(PublicMilitaryIndicator publicMilitaryIndicator) {
      this.publicMilitaryIndicator = publicMilitaryIndicator;
      return this;
    }

    public Builder daylightTimeIndicator(Boolean daylightTimeIndicator) {
      this.daylightTimeIndicator = daylightTimeIndicator;
      return this;
    }

    public Builder magneticTrueIndicator(MagneticTrueIndicator magneticTrueIndicator) {
      this.magneticTrueIndicator = magneticTrueIndicator;
      return this;
    }

    public Builder datumCode(String datumCode) {
      this.datumCode = datumCode;
      return this;
    }

    public Builder airportFullName(String airportFullName) {
      this.airportFullName = airportFullName;
      return this;
    }

    public Builder fileRecordNumber(Integer fileRecordNumber) {
      this.fileRecordNumber = fileRecordNumber;
      return this;
    }

    public Builder lastUpdateCycle(String lastUpdateCycle) {
      this.lastUpdateCycle = lastUpdateCycle;
      return this;
    }

    public ArincAirport build() {
      return new ArincAirport(this);
    }
  }
}
