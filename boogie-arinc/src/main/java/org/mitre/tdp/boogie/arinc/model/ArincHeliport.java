package org.mitre.tdp.boogie.arinc.model;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.*;
import org.mitre.tdp.boogie.arinc.v21.field.HeliportType;
import org.mitre.tdp.boogie.arinc.v21.field.PadDimensions;
import org.mitre.tdp.boogie.arinc.v21.field.PadShape;

/**
 * The Arinc Heliport model supports both the old and new style Heliport Definitions.
 * Previously the heliport was really more like a single record for a landing surface. More like a single pad at a hospital.
 * Later models in arinc changed a heliport to be more like an airport, which can have children helipads, runways, ...etc.
 */
public final class ArincHeliport implements ArincModel {
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

  private final String heliportIdentifier;

  private final String heliportIcaoRegion;
  /**
   * See {@link SubSectionCode} - always "H" for airport helipads
   */
  private final String subSectionCode;
  /**
   * The “ATA/IATA” field contains the Airport/Heliport ATA/IATA designator code to which the data contained in the record
   * relates.
   */
  private final String iataDesignator;
  /**
   * See {@link PadIdentifier}
   */
  private final String padIdentifier;
  /**
   * See {@link ContinuationRecordNumber}
   */
  private final String continuationRecordNumber;
  /**
   * See {@link SpeedLimitAltitude}.
   */
  private final Double speedLimitAltitude;
  /**
   * See {@link DatumCode}.
   */
  private final String datumCode;
  /**
   * See {@link HeliportType}
   */
  private final HeliportType heliportType;
  /**
   * True if the raw field explicitly indicates 'Y' the airport has IFR capability - otherwise false.
   * <br>
   * See {@link IfrCapability}.
   */
  private final Boolean ifrCapability;
  /**
   * The latitude of the heliport.
   */
  private final double latitude;
  /**
   * The longitude of the heliport.
   */
  private final double longitude;
  /**
   * The published magnetic variation at the facility in degrees (+/-).
   * <br>
   * See {@link MagneticVariation}.
   */
  private final Double magneticVariation;
  /**
   * See {@link AirportHeliportElevation}.
   */
  private final Double heliportElevation;
  /**
   * See {@link SpeedLimit}.
   */
  private final Integer speedLimit;
  /**
   * The string identifier of the recommended navaid associated with the airport.
   */
  private final String recommendedVhfNavaid;
  /**
   * The icao region of the navaid.
   */
  private final String navaidIcaoRegion;
  /**
   * See {@link TransitionAltitude}.
   */
  private final Double transitionAltitude;
  /**
   * See {@link TransitionAltitude}.
   */
  private final Double transitionLevel;
  /**
   * See {@link PublicMilitaryIndicator}.
   */
  private final PublicMilitaryIndicator publicMilitaryIndicator;
  /**
   * See {@link PublicMilitaryIndicator}.
   */
  private final Boolean daylightTimeIndicator;

  /**
   * See {@link PadShape}
   * Note in Arinc 424 Version 21+ this will not be populated.
   */
  private final String padShape;
  /**
   * This field is parsed from {@link PadDimensions}
   * This field is the width in feet if the pad is a runway.
   *  Note in Arinc 424 Version 21+ this will not be populated.
   */
  private final Double padXDimension;
  /**
   * This field is parsed from {@link PadDimensions}
   * This field is the length in feet if the pad is a runway.
   *  Note in Arinc 424 Version 21+ this will not be populated.
   */
  private final Double padYDimension;
  /**
   * This field is parsed from {@link PadDimensions}
   * This field is the diameter in feet of the pad if it is circular.
   *  Note in Arinc 424 Version 21+ this will not be populated.
   */
  private final Double padDiameter;
  /**
   * See {@link MagneticTrueIndicator}.
   */
  private final MagneticTrueIndicator magneticTrueIndicator;
  /**
   * The full name of the facility
   */
  private final String heliportName;
  /**
   * See {@link FileRecordNumber}
   */
  private final Integer fileRecordNumber;
  /**
   * See {@link Cycle}.
   */
  private final String cycleDate;

  private ArincHeliport(Builder builder) {
    this.recordType = requireNonNull(builder.recordType);
    this.customerAreaCode = requireNonNull(builder.customerAreaCode);
    this.sectionCode = requireNonNull(builder.sectionCode);
    this.heliportIdentifier = requireNonNull(builder.heliportIdentifier);
    this.heliportIcaoRegion = requireNonNull(builder.heliportIcaoRegion);
    this.subSectionCode = builder.subSectionCode;
    this.iataDesignator = builder.iataDesignator;
    this.padIdentifier = builder.padIdentifier;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.speedLimitAltitude = builder.speedLimitAltitude;
    this.datumCode = builder.datumCode;
    this.heliportType = builder.heliportType;
    this.ifrCapability = builder.ifrCapability;
    this.latitude = builder.latitude;
    this.longitude = builder.longitude;
    this.magneticVariation = builder.magneticVariation;
    this.heliportElevation = builder.heliportElevation;
    this.speedLimit = builder.speedLimit;
    this.recommendedVhfNavaid = builder.recommendedVhfNavaid;
    this.navaidIcaoRegion = builder.navaidIcaoRegion;
    this.transitionAltitude = builder.transitionAltitude;
    this.transitionLevel = builder.transitionLevel;
    this.publicMilitaryIndicator = builder.publicMilitaryIndicator;
    this.daylightTimeIndicator = builder.daylightTimeIndicator;
    this.padShape = builder.padShape;
    this.padXDimension = builder.padXDimension;
    this.padYDimension = builder.padYDimension;
    this.padDiameter = builder.padDiameter;
    this.magneticTrueIndicator = builder.magneticTrueIndicator;
    this.heliportName = builder.heliportName;
    this.fileRecordNumber = builder.fileRecordNumber;
    this.cycleDate = builder.cycleDate;
  }

  public static Builder builder() {
    return new Builder();
  }

  public RecordType recordType() {
    return recordType;
  }

  public CustomerAreaCode customerAreaCode() {
    return customerAreaCode;
  }

  public SectionCode sectionCode() {
    return sectionCode;
  }

  public String heliportIdentifier() {
    return heliportIdentifier;
  }

  public String heliportIcaoRegion() {
    return heliportIcaoRegion;
  }

  public Optional<String> subSectionCode() {
    return ofNullable(subSectionCode);
  }

  public Optional<String> iataDesignator() {
    return ofNullable(iataDesignator);
  }

  public Optional<String> padIdentifier() {
    return ofNullable(padIdentifier);
  }

  public Optional<String> continuationRecordNumber() {
    return ofNullable(continuationRecordNumber);
  }

  public Optional<Double> speedLimitAltitude() {
    return ofNullable(speedLimitAltitude);
  }

  public Optional<String> datumCode() {
    return ofNullable(datumCode);
  }

  public HeliportType heliportType() {
    return heliportType;
  }

  public Optional<Boolean> ifrCapability() {
    return ofNullable(ifrCapability);
  }

  public double latitude() {
    return latitude;
  }

  public double longitude() {
    return longitude;
  }

  public Optional<Double> magneticVariation() {
    return ofNullable(magneticVariation);
  }

  public Optional<Double> heliportElevation() {
    return ofNullable(heliportElevation);
  }

  public Optional<Integer> speedLimit() {
    return ofNullable(speedLimit);
  }

  public Optional<String> recommendedVhfNavaid() {
    return ofNullable(recommendedVhfNavaid);
  }

  public Optional<String> navaidIcaoRegion() {
    return ofNullable(navaidIcaoRegion);
  }

  public Optional<Double> transitionAltitude() {
    return ofNullable(transitionAltitude);
  }

  public Optional<Double> transitionLevel() {
    return ofNullable(transitionLevel);
  }

  public Optional<PublicMilitaryIndicator> publicMilitaryIndicator() {
    return ofNullable(publicMilitaryIndicator);
  }

  public Optional<Boolean> daylightTimeIndicator() {
    return ofNullable(daylightTimeIndicator);
  }

  public Optional<PadShape> padShape() {
    return ofNullable(padShape).map(PadShape::valueOf);
  }

  public Optional<Double> padXDimension() {
    return ofNullable(padXDimension);
  }

  public Optional<Double> padYDimension() {
    return ofNullable(padYDimension);
  }

  public Optional<Double> padDiameter() {
    return ofNullable(padDiameter);
  }

  public Optional<MagneticTrueIndicator> magneticTrueIndicator() {
    return ofNullable(magneticTrueIndicator);
  }

  public Optional<String> heliportName() {
    return ofNullable(heliportName);
  }

  public Optional<Integer> fileRecordNumber() {
    return ofNullable(fileRecordNumber);
  }

  public Optional<String> cycleDate() {
    return ofNullable(cycleDate);
  }

  public Builder toBuilder() {
    return builder()
        .recordType(recordType())
        .customerAreaCode(customerAreaCode())
        .sectionCode(sectionCode())
        .heliportIdentifier(heliportIdentifier())
        .heliportIcaoRegion(heliportIcaoRegion())
        .subSectionCode(subSectionCode().orElse(null))
        .iataDesignator(iataDesignator().orElse(null))
        .padIdentifier(padIdentifier().orElse(null))
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .speedLimitAltitude(speedLimitAltitude().orElse(null))
        .datumCode(datumCode().orElse(null))
        .heliportType(heliportType())
        .ifrCapability(ifrCapability().orElse(null))
        .latitude(latitude())
        .longitude(longitude())
        .magneticVariation(magneticVariation().orElse(null))
        .heliportElevation(heliportElevation().orElse(null))
        .speedLimit(speedLimit().orElse(null))
        .recommendedVhfNavaid(recommendedVhfNavaid().orElse(null))
        .navaidIcaoRegion(navaidIcaoRegion().orElse(null))
        .transitionAltitude(transitionAltitude().orElse(null))
        .transitionLevel(transitionLevel().orElse(null))
        .publicMilitaryIndicator(publicMilitaryIndicator().orElse(null))
        .daylightTimeIndicator(daylightTimeIndicator().orElse(null))
        .padShape(padShape().map(Enum::name).orElse(null))
        .padXDimension(padXDimension().orElse(null))
        .padYDimension(padYDimension().orElse(null))
        .padDiameter(padDiameter().orElse(null))
        .magneticTrueIndicator(magneticTrueIndicator().orElse(null))
        .heliportName(heliportName().orElse(null))
        .fileRecordNumber(fileRecordNumber().orElse(null))
        .cycleDate(cycleDate().orElse(null));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ArincHeliport that = (ArincHeliport) o;
    return Double.compare(latitude, that.latitude) == 0 && Double.compare(longitude, that.longitude) == 0 && recordType == that.recordType && customerAreaCode == that.customerAreaCode && sectionCode == that.sectionCode && Objects.equals(heliportIdentifier, that.heliportIdentifier) && Objects.equals(heliportIcaoRegion, that.heliportIcaoRegion) && Objects.equals(subSectionCode, that.subSectionCode) && Objects.equals(iataDesignator, that.iataDesignator) && Objects.equals(padIdentifier, that.padIdentifier) && Objects.equals(continuationRecordNumber, that.continuationRecordNumber) && Objects.equals(speedLimitAltitude, that.speedLimitAltitude) && Objects.equals(datumCode, that.datumCode) && Objects.equals(heliportType, that.heliportType) && Objects.equals(ifrCapability, that.ifrCapability) && Objects.equals(magneticVariation, that.magneticVariation) && Objects.equals(heliportElevation, that.heliportElevation) && Objects.equals(speedLimit, that.speedLimit) && Objects.equals(recommendedVhfNavaid, that.recommendedVhfNavaid) && Objects.equals(navaidIcaoRegion, that.navaidIcaoRegion) && Objects.equals(transitionAltitude, that.transitionAltitude) && Objects.equals(transitionLevel, that.transitionLevel) && publicMilitaryIndicator == that.publicMilitaryIndicator && Objects.equals(daylightTimeIndicator, that.daylightTimeIndicator) && Objects.equals(padShape, that.padShape) && Objects.equals(padXDimension, that.padXDimension) && Objects.equals(padYDimension, that.padYDimension) && Objects.equals(padDiameter, that.padDiameter) && magneticTrueIndicator == that.magneticTrueIndicator && Objects.equals(heliportName, that.heliportName) && Objects.equals(fileRecordNumber, that.fileRecordNumber) && Objects.equals(cycleDate, that.cycleDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, heliportIdentifier, heliportIcaoRegion, subSectionCode, iataDesignator, padIdentifier, continuationRecordNumber, speedLimitAltitude, datumCode, heliportType, ifrCapability, latitude, longitude, magneticVariation, heliportElevation, speedLimit, recommendedVhfNavaid, navaidIcaoRegion, transitionAltitude, transitionLevel, publicMilitaryIndicator, daylightTimeIndicator, padShape, padXDimension, padYDimension, padDiameter, magneticTrueIndicator, heliportName, fileRecordNumber, cycleDate);
  }

  @Override
  public String toString() {
    return "ArincHeliport{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode=" + sectionCode +
        ", heliportIdentifier='" + heliportIdentifier + '\'' +
        ", heliportIcaoRegion='" + heliportIcaoRegion + '\'' +
        ", subSectionCode='" + subSectionCode + '\'' +
        ", iataDesignator='" + iataDesignator + '\'' +
        ", padIdentifier='" + padIdentifier + '\'' +
        ", continuationRecordNumber='" + continuationRecordNumber + '\'' +
        ", speedLimitAltitude=" + speedLimitAltitude +
        ", datumCode='" + datumCode + '\'' +
        ", heliportType=" + heliportType +
        ", ifrCapability=" + ifrCapability +
        ", latitude=" + latitude +
        ", longitude=" + longitude +
        ", magneticVariation=" + magneticVariation +
        ", heliportElevation=" + heliportElevation +
        ", speedLimit=" + speedLimit +
        ", recommendedVhfNavaid='" + recommendedVhfNavaid + '\'' +
        ", navaidIcaoRegion='" + navaidIcaoRegion + '\'' +
        ", transitionAltitude=" + transitionAltitude +
        ", transitionLevel=" + transitionLevel +
        ", publicMilitaryIndicator=" + publicMilitaryIndicator +
        ", daylightTimeIndicator=" + daylightTimeIndicator +
        ", padShape='" + padShape + '\'' +
        ", padXDimension=" + padXDimension +
        ", padYDimension=" + padYDimension +
        ", padDiameter=" + padDiameter +
        ", magneticTrueIndicator=" + magneticTrueIndicator +
        ", heliportName='" + heliportName + '\'' +
        ", fileRecordNumber=" + fileRecordNumber +
        ", cycleDate='" + cycleDate + '\'' +
        '}';
  }

  public static final class Builder {
    private RecordType recordType;

    private CustomerAreaCode customerAreaCode;

    private SectionCode sectionCode;

    private String heliportIdentifier;

    private String heliportIcaoRegion;

    private String subSectionCode;

    private String iataDesignator;

    private String padIdentifier;

    private String continuationRecordNumber;

    private Double speedLimitAltitude;

    private String datumCode;

    private HeliportType heliportType;

    private Boolean ifrCapability;

    private double latitude;

    private double longitude;

    private Double magneticVariation;

    private Double heliportElevation;

    private Integer speedLimit;

    private String recommendedVhfNavaid;

    private String navaidIcaoRegion;

    private Double transitionAltitude;

    private Double transitionLevel;

    private PublicMilitaryIndicator publicMilitaryIndicator;

    private Boolean daylightTimeIndicator;

    private String padShape;

    private Double padXDimension;

    private Double padYDimension;

    private Double padDiameter;

    private MagneticTrueIndicator magneticTrueIndicator;

    private String heliportName;

    private Integer fileRecordNumber;

    private String cycleDate;

    private Builder() {
    }

    public ArincHeliport build() {
      return new ArincHeliport(this);
    }

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

    public Builder heliportIdentifier(String heliportIdentifier) {
      this.heliportIdentifier = heliportIdentifier;
      return this;
    }

    public Builder heliportIcaoRegion(String airportIcaoRegion) {
      this.heliportIcaoRegion = airportIcaoRegion;
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

    public Builder padIdentifier(String padIdentifier) {
      this.padIdentifier = padIdentifier;
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

    public Builder datumCode(String datumCode) {
      this.datumCode = datumCode;
      return this;
    }

    public Builder heliportType(HeliportType heliportType) {
      this.heliportType = heliportType;
      return this;
    }

    public Builder ifrCapability(Boolean ifrCapability) {
      this.ifrCapability = ifrCapability;
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

    public Builder magneticVariation(Double magneticVariation) {
      this.magneticVariation = magneticVariation;
      return this;
    }

    public Builder heliportElevation(Double heliportElevation) {
      this.heliportElevation = heliportElevation;
      return this;
    }

    public Builder speedLimit(Integer speedLimit) {
      this.speedLimit = speedLimit;
      return this;
    }

    public Builder recommendedVhfNavaid(String recommendedVhfNavaid) {
      this.recommendedVhfNavaid = recommendedVhfNavaid;
      return this;
    }

    public Builder navaidIcaoRegion(String navaidIcaoRegion) {
      this.navaidIcaoRegion = navaidIcaoRegion;
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

    public Builder padShape(String padShape) {
      this.padShape = padShape;
      return this;
    }

    public Builder padXDimension(Double padDimensionX) {
      this.padXDimension = padDimensionX;
      return this;
    }

    public Builder padYDimension(Double padDimensionY) {
      this.padYDimension = padDimensionY;
      return this;
    }

    public Builder padDiameter(Double padDiameter) {
      this.padDiameter = padDiameter;
      return this;
    }

    public Builder magneticTrueIndicator(MagneticTrueIndicator magneticTrueIndicator) {
      this.magneticTrueIndicator = magneticTrueIndicator;
      return this;
    }

    public Builder heliportName(String heliportName) {
      this.heliportName = heliportName;
      return this;
    }

    public Builder fileRecordNumber(Integer fileRecordNumber) {
      this.fileRecordNumber = fileRecordNumber;
      return this;
    }

    public Builder cycleDate(String cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }
  }
}