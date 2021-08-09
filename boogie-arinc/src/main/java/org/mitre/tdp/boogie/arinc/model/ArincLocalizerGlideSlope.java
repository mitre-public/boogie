package org.mitre.tdp.boogie.arinc.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.GlideSlopePosition;
import org.mitre.tdp.boogie.arinc.v18.field.IlsMlsGlsCategory;
import org.mitre.tdp.boogie.arinc.v18.field.IlsMlsGlsIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.LocalizerBearing;
import org.mitre.tdp.boogie.arinc.v18.field.LocalizerFrequency;
import org.mitre.tdp.boogie.arinc.v18.field.LocalizerPosition;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

/**
 * Data class for representing structured/parsed content from within an ARINC format localizer/glidslope record.
 * <br>
 * Section/Subsection = PI
 */
public final class ArincLocalizerGlideSlope implements ArincModel {

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
   * The identifier of the airport containing the runway the localizer/glideslope services.
   */
  private final String airportIdentifier;
  private final String airportIcaoRegion;
  private final String subSectionCode;
  /**
   * See {@link IlsMlsGlsIdentifier}.
   */
  private final String localizerIdentifier;
  /**
   * See {@link IlsMlsGlsCategory}.
   */
  private final String ilsMlsGlsCategory;
  /**
   * See {@link ContinuationRecordNumber}.
   */
  private final String continuationRecordNumber;
  /**
   * The frequency of the localizer/glideslope in MHz.
   * <br>
   * See {@link LocalizerFrequency}.
   */
  private final Double localizerFrequency;
  /**
   * The identifier of the runway the facility services.
   */
  private final String runwayIdentifier;
  private final Double localizerLatitude;
  private final Double localizerLongitude;
  /**
   * See {@link LocalizerBearing}.
   */
  private final Double localizerBearing;
  private final Double glideSlopeLatitude;
  private final Double glideSlopeLongitude;
  /**
   * Distance from the localizer to the end of the runway in feet.
   * <br>
   * See {@link LocalizerPosition}.
   */
  private final Integer localizerPosition;
  private final String localizerPositionReference;
  /**
   * Distance from the glideSlope to the end of the runway in feet.
   * <br>
   * See {@link GlideSlopePosition}.
   */
  private final Integer glideSlopePosition;
  private final Double localizerWidth;
  private final Double glideSlopeAngle;
  private final Double stationDeclination;
  private final Integer glideSlopeHeightAtLandingThreshold;
  private final Double glideSlopeElevation;
  private final String supportingFacilityIdentifier;
  private final String supportingFacilityIcaoRegion;
  private final SectionCode supportingFacilitySectionCode;
  private final String supportingFacilitySubsectionCode;
  private final Integer fileRecordNumber;
  private final String lastUpdateCycle;

  private ArincLocalizerGlideSlope(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.airportIdentifier = builder.airportIdentifier;
    this.airportIcaoRegion = builder.airportIcaoRegion;
    this.subSectionCode = builder.subSectionCode;
    this.localizerIdentifier = builder.localizerIdentifier;
    this.ilsMlsGlsCategory = builder.ilsMlsGlsCategory;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.localizerFrequency = builder.localizerFrequency;
    this.runwayIdentifier = builder.runwayIdentifier;
    this.localizerLatitude = builder.localizerLatitude;
    this.localizerLongitude = builder.localizerLongitude;
    this.localizerBearing = builder.localizerBearing;
    this.glideSlopeLatitude = builder.glideSlopeLatitude;
    this.glideSlopeLongitude = builder.glideSlopeLongitude;
    this.localizerPosition = builder.localizerPosition;
    this.localizerPositionReference = builder.localizerPositionReference;
    this.glideSlopePosition = builder.glideSlopePosition;
    this.localizerWidth = builder.localizerWidth;
    this.glideSlopeAngle = builder.glideSlopeAngle;
    this.stationDeclination = builder.stationDeclination;
    this.glideSlopeHeightAtLandingThreshold = builder.glideSlopeHeightAtLandingThreshold;
    this.glideSlopeElevation = builder.glideSlopeElevation;
    this.supportingFacilityIdentifier = builder.supportingFacilityIdentifier;
    this.supportingFacilityIcaoRegion = builder.supportingFacilityIcaoRegion;
    this.supportingFacilitySectionCode = builder.supportingFacilitySectionCode;
    this.supportingFacilitySubsectionCode = builder.supportingFacilitySubsectionCode;
    this.fileRecordNumber = builder.fileRecordNumber;
    this.lastUpdateCycle = builder.lastUpdateCycle;
  }

  public RecordType recordType() {
    return recordType;
  }

  public Optional<CustomerAreaCode> customerAreaCode() {
    return Optional.ofNullable(customerAreaCode);
  }

  @Override
  public SectionCode sectionCode() {
    return sectionCode;
  }

  public String airportIdentifier() {
    return airportIdentifier;
  }

  public String airportIcaoRegion() {
    return airportIcaoRegion;
  }

  @Override
  public Optional<String> subSectionCode() {
    return Optional.of(subSectionCode);
  }

  public String localizerIdentifier() {
    return localizerIdentifier;
  }

  public Optional<String> ilsMlsGlsCategory() {
    return Optional.ofNullable(ilsMlsGlsCategory);
  }

  public Optional<String> continuationRecordNumber() {
    return Optional.ofNullable(continuationRecordNumber);
  }

  public Optional<Double> localizerFrequency() {
    return Optional.ofNullable(localizerFrequency);
  }

  public String runwayIdentifier() {
    return runwayIdentifier;
  }

  public Optional<Double> localizerLatitude() {
    return Optional.ofNullable(localizerLatitude);
  }

  public Optional<Double> localizerLongitude() {
    return Optional.ofNullable(localizerLongitude);
  }

  public Optional<Double> localizerBearing() {
    return Optional.ofNullable(localizerBearing);
  }

  public Optional<Double> glideSlopeLatitude() {
    return Optional.ofNullable(glideSlopeLatitude);
  }

  public Optional<Double> glideSlopeLongitude() {
    return Optional.ofNullable(glideSlopeLongitude);
  }

  public Optional<Integer> localizerPosition() {
    return Optional.ofNullable(localizerPosition);
  }

  public Optional<String> localizerPositionReference() {
    return Optional.ofNullable(localizerPositionReference);
  }

  public Optional<Integer> glideSlopePosition() {
    return Optional.ofNullable(glideSlopePosition);
  }

  public Optional<Double> localizerWidth() {
    return Optional.ofNullable(localizerWidth);
  }

  public Optional<Double> glideSlopeAngle() {
    return Optional.ofNullable(glideSlopeAngle);
  }

  public Optional<Double> stationDeclination() {
    return Optional.ofNullable(stationDeclination);
  }

  public Optional<Integer> glideSlopeHeightAtLandingThreshold() {
    return Optional.ofNullable(glideSlopeHeightAtLandingThreshold);
  }

  public Optional<Double> glideSlopeElevation() {
    return Optional.ofNullable(glideSlopeElevation);
  }

  public Optional<String> supportingFacilityIdentifier() {
    return Optional.ofNullable(supportingFacilityIdentifier);
  }

  public Optional<String> supportingFacilityIcaoCode() {
    return Optional.ofNullable(supportingFacilityIcaoRegion);
  }

  public Optional<SectionCode> supportingFacilitySectionCode() {
    return Optional.ofNullable(supportingFacilitySectionCode);
  }

  public Optional<String> supportingFacilitySubSectionCode() {
    return Optional.ofNullable(supportingFacilitySubsectionCode);
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
        .subSectionCode(subSectionCode().orElseThrow(IllegalStateException::new))
        .localizerIdentifier(localizerIdentifier())
        .ilsMlsGlsCategory(ilsMlsGlsCategory().orElse(null))
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .localizerFrequency(localizerFrequency().orElse(null))
        .runwayIdentifier(runwayIdentifier())
        .localizerLatitude(localizerLatitude().orElse(null))
        .localizerLongitude(localizerLongitude().orElse(null))
        .localizerBearing(localizerBearing().orElse(null))
        .glideSlopeLatitude(glideSlopeLatitude().orElse(null))
        .glideSlopeLongitude(glideSlopeLongitude().orElse(null))
        .localizerPosition(localizerPosition().orElse(null))
        .localizerPositionReference(localizerPositionReference().orElse(null))
        .glideSlopePosition(glideSlopePosition().orElse(null))
        .localizerWidth(localizerWidth().orElse(null))
        .glideSlopeAngle(glideSlopeAngle().orElse(null))
        .stationDeclination(stationDeclination().orElse(null))
        .glideSlopeHeightAtLandingThreshold(glideSlopeHeightAtLandingThreshold().orElse(null))
        .glideSlopeElevation(glideSlopeElevation().orElse(null))
        .supportingFacilityIdentifier(supportingFacilityIdentifier().orElse(null))
        .supportingFacilityIcaoRegion(supportingFacilityIcaoCode().orElse(null))
        .supportingFacilitySectionCode(supportingFacilitySectionCode().orElse(null))
        .supportingFacilitySubSectionCode(supportingFacilitySubSectionCode().orElse(null))
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
    ArincLocalizerGlideSlope that = (ArincLocalizerGlideSlope) o;
    return recordType == that.recordType &&
        customerAreaCode == that.customerAreaCode &&
        sectionCode == that.sectionCode &&
        Objects.equals(airportIdentifier, that.airportIdentifier) &&
        Objects.equals(airportIcaoRegion, that.airportIcaoRegion) &&
        Objects.equals(subSectionCode, that.subSectionCode) &&
        Objects.equals(localizerIdentifier, that.localizerIdentifier) &&
        Objects.equals(ilsMlsGlsCategory, that.ilsMlsGlsCategory) &&
        Objects.equals(continuationRecordNumber, that.continuationRecordNumber) &&
        Objects.equals(localizerFrequency, that.localizerFrequency) &&
        Objects.equals(runwayIdentifier, that.runwayIdentifier) &&
        Objects.equals(localizerLatitude, that.localizerLatitude) &&
        Objects.equals(localizerLongitude, that.localizerLongitude) &&
        Objects.equals(localizerBearing, that.localizerBearing) &&
        Objects.equals(glideSlopeLatitude, that.glideSlopeLatitude) &&
        Objects.equals(glideSlopeLongitude, that.glideSlopeLongitude) &&
        Objects.equals(localizerPosition, that.localizerPosition) &&
        Objects.equals(localizerPositionReference, that.localizerPositionReference) &&
        Objects.equals(glideSlopePosition, that.glideSlopePosition) &&
        Objects.equals(localizerWidth, that.localizerWidth) &&
        Objects.equals(glideSlopeAngle, that.glideSlopeAngle) &&
        Objects.equals(stationDeclination, that.stationDeclination) &&
        Objects.equals(glideSlopeHeightAtLandingThreshold, that.glideSlopeHeightAtLandingThreshold) &&
        Objects.equals(glideSlopeElevation, that.glideSlopeElevation) &&
        Objects.equals(supportingFacilityIdentifier, that.supportingFacilityIdentifier) &&
        Objects.equals(supportingFacilityIcaoRegion, that.supportingFacilityIcaoRegion) &&
        supportingFacilitySectionCode == that.supportingFacilitySectionCode &&
        Objects.equals(supportingFacilitySubsectionCode, that.supportingFacilitySubsectionCode) &&
        Objects.equals(fileRecordNumber, that.fileRecordNumber) &&
        Objects.equals(lastUpdateCycle, that.lastUpdateCycle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, airportIdentifier, airportIcaoRegion, subSectionCode, localizerIdentifier, ilsMlsGlsCategory, continuationRecordNumber, localizerFrequency, runwayIdentifier, localizerLatitude, localizerLongitude, localizerBearing, glideSlopeLatitude, glideSlopeLongitude, localizerPosition, localizerPositionReference, glideSlopePosition, localizerWidth, glideSlopeAngle, stationDeclination, glideSlopeHeightAtLandingThreshold, glideSlopeElevation, supportingFacilityIdentifier, supportingFacilityIcaoRegion, supportingFacilitySectionCode, supportingFacilitySubsectionCode, fileRecordNumber, lastUpdateCycle);
  }

  @Override
  public String toString() {
    return "ArincLocalizerGlideSlope2{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode=" + sectionCode +
        ", airportIdentifier='" + airportIdentifier + '\'' +
        ", airportIcaoRegion='" + airportIcaoRegion + '\'' +
        ", subsectionCode='" + subSectionCode + '\'' +
        ", localizerIdentifier='" + localizerIdentifier + '\'' +
        ", ilsMlsGlsCategory='" + ilsMlsGlsCategory + '\'' +
        ", continuationRecordNumber='" + continuationRecordNumber + '\'' +
        ", localizerFrequency=" + localizerFrequency +
        ", runwayIdentifier='" + runwayIdentifier + '\'' +
        ", localizerLatitude=" + localizerLatitude +
        ", localizerLongitude=" + localizerLongitude +
        ", localizerBearing=" + localizerBearing +
        ", glideSlopeLatitude=" + glideSlopeLatitude +
        ", glideSlopeLongitude=" + glideSlopeLongitude +
        ", localizerPosition=" + localizerPosition +
        ", localizerPositionReference='" + localizerPositionReference + '\'' +
        ", glideSlopePosition=" + glideSlopePosition +
        ", localizerWidth=" + localizerWidth +
        ", glideSlopeAngle=" + glideSlopeAngle +
        ", stationDeclination=" + stationDeclination +
        ", glideSlopeHeightAtLandingThreshold=" + glideSlopeHeightAtLandingThreshold +
        ", glideSlopeElevation=" + glideSlopeElevation +
        ", supportingFacilityIdentifier='" + supportingFacilityIdentifier + '\'' +
        ", supportingFacilityIcaoRegion='" + supportingFacilityIcaoRegion + '\'' +
        ", supportingFacilitySectionCode=" + supportingFacilitySectionCode +
        ", supportingFacilitySubsectionCode='" + supportingFacilitySubsectionCode + '\'' +
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
    private String localizerIdentifier;
    private String ilsMlsGlsCategory;
    private String continuationRecordNumber;
    private Double localizerFrequency;
    private String runwayIdentifier;
    private Double localizerLatitude;
    private Double localizerLongitude;
    private Double localizerBearing;
    private Double glideSlopeLatitude;
    private Double glideSlopeLongitude;
    private Integer localizerPosition;
    private String localizerPositionReference;
    private Integer glideSlopePosition;
    private Double localizerWidth;
    private Double glideSlopeAngle;
    private Double stationDeclination;
    private Integer glideSlopeHeightAtLandingThreshold;
    private Double glideSlopeElevation;
    private String supportingFacilityIdentifier;
    private String supportingFacilityIcaoRegion;
    private SectionCode supportingFacilitySectionCode;
    private String supportingFacilitySubsectionCode;
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

    public Builder subSectionCode(String subsectionCode) {
      this.subSectionCode = subsectionCode;
      return this;
    }

    public Builder localizerIdentifier(String localizerIdentifier) {
      this.localizerIdentifier = localizerIdentifier;
      return this;
    }

    public Builder ilsMlsGlsCategory(String ilsMlsGlsCategory) {
      this.ilsMlsGlsCategory = ilsMlsGlsCategory;
      return this;
    }

    public Builder continuationRecordNumber(String continuationRecordNumber) {
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder localizerFrequency(Double localizerFrequency) {
      this.localizerFrequency = localizerFrequency;
      return this;
    }

    public Builder runwayIdentifier(String runwayIdentifier) {
      this.runwayIdentifier = runwayIdentifier;
      return this;
    }

    public Builder localizerLatitude(Double localizerLatitude) {
      this.localizerLatitude = localizerLatitude;
      return this;
    }

    public Builder localizerLongitude(Double localizerLongitude) {
      this.localizerLongitude = localizerLongitude;
      return this;
    }

    public Builder localizerBearing(Double localizerBearing) {
      this.localizerBearing = localizerBearing;
      return this;
    }

    public Builder glideSlopeLatitude(Double glideSlopeLatitude) {
      this.glideSlopeLatitude = glideSlopeLatitude;
      return this;
    }

    public Builder glideSlopeLongitude(Double glideSlopeLongitude) {
      this.glideSlopeLongitude = glideSlopeLongitude;
      return this;
    }

    public Builder localizerPosition(Integer localizerPosition) {
      this.localizerPosition = localizerPosition;
      return this;
    }

    public Builder localizerPositionReference(String localizerPositionReference) {
      this.localizerPositionReference = localizerPositionReference;
      return this;
    }

    public Builder glideSlopePosition(Integer glideSlopePosition) {
      this.glideSlopePosition = glideSlopePosition;
      return this;
    }

    public Builder localizerWidth(Double localizerWidth) {
      this.localizerWidth = localizerWidth;
      return this;
    }

    public Builder glideSlopeAngle(Double glideSlopeAngle) {
      this.glideSlopeAngle = glideSlopeAngle;
      return this;
    }

    public Builder stationDeclination(Double stationDeclination) {
      this.stationDeclination = stationDeclination;
      return this;
    }

    public Builder glideSlopeHeightAtLandingThreshold(Integer glideSlopeHeightAtLandingThreshold) {
      this.glideSlopeHeightAtLandingThreshold = glideSlopeHeightAtLandingThreshold;
      return this;
    }

    public Builder glideSlopeElevation(Double glideSlopeElevation) {
      this.glideSlopeElevation = glideSlopeElevation;
      return this;
    }

    public Builder supportingFacilityIdentifier(String supportingFacilityIdentifier) {
      this.supportingFacilityIdentifier = supportingFacilityIdentifier;
      return this;
    }

    public Builder supportingFacilityIcaoRegion(String supportingFacilityIcaoRegion) {
      this.supportingFacilityIcaoRegion = supportingFacilityIcaoRegion;
      return this;
    }

    public Builder supportingFacilitySectionCode(SectionCode supportingFacilitySectionCode) {
      this.supportingFacilitySectionCode = supportingFacilitySectionCode;
      return this;
    }

    public Builder supportingFacilitySubSectionCode(String supportingFacilitySubsectionCode) {
      this.supportingFacilitySubsectionCode = supportingFacilitySubsectionCode;
      return this;
    }

    public Builder fileRecordNumber(Integer fileRecordNumber) {
      this.fileRecordNumber = fileRecordNumber;
      return this;
    }

    public Builder lastUpdateCycle(String cycle) {
      this.lastUpdateCycle = cycle;
      return this;
    }

    public ArincLocalizerGlideSlope build() {
      return new ArincLocalizerGlideSlope(this);
    }
  }
}
