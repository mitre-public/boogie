package org.mitre.tdp.boogie.arinc.model;

import static java.util.Optional.ofNullable;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.CategoryOrType;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.v18.field.AltitudeDescription;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimitDescription;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirection;
import org.mitre.tdp.boogie.arinc.v18.field.WaypointDescription;

/**
 * Data class for representing structured/parsed content from within an ARINC format airway leg record.
 * <br>
 * Section/Subsection = PD/PE/PF - SID/STAR/APPROACH
 * <br>
 * ARINC stores airways as contiguous records with each record representing a leg within the top level airway structure - this
 * class then mimics a single line of an ARINC airway record. These legs can then be sequenced to produce a full airway definition.
 */
public final class ArincProcedureLeg implements ArincModel {

  private final RecordType recordType;
  private final CustomerAreaCode customerAreaCode;
  private final SectionCode sectionCode;
  private final String airportIdentifier;
  private final String airportIcaoRegion;
  private final String subSectionCode;
  private final String sidStarIdentifier;
  private final String routeType;
  private final String transitionIdentifier;
  /**
   * See {@link CategoryOrType}.
   */
  private final String categoryOrType;
  private final int sequenceNumber;
  private final String fixIdentifier;
  private final String fixIcaoRegion;
  private final SectionCode fixSectionCode;
  private final String fixSubSectionCode;
  private final String continuationRecordNumber;
  /**
   * See {@link WaypointDescription}.
   */
  private final String waypointDescription;
  private final TurnDirection turnDirection;
  private final Float rnp;
  private final PathTerminator pathTerminator;
  private final Boolean turnDirectionValid;
  private final String recommendedNavaidIdentifier;
  private final String recommendedNavaidIcaoRegion;
  private final String legInboundOutboundIdentifier;
  private final Float arcRadius;
  private final Float theta;
  private final Float rho;
  private final Float outboundMagneticCourse;
  private final String routeHoldDistanceTime;
  private final Duration holdTime;
  private final Float routeDistance;
  private final SectionCode recommendedNavaidSectionCode;
  private final String recommendedNavaidSubSectionCode;
  /**
   * See {@link AltitudeDescription} for options - and note "blank"s are mapped to "@"s.
   */
  private final String altitudeDescription;
  private final Float minAltitude1;
  private final Float minAltitude2;
  private final Float transitionAltitude;
  private final Integer speedLimit;
  private final Float verticalAngle;
  private final String centerFixIdentifier;
  private final String centerFixIcaoRegion;
  private final SectionCode centerFixSectionCode;
  private final String centerFixSubSectionCode;
  private final String gnssFmsIndicator;
  /**
   * See {@link SpeedLimitDescription} for options - and note "blank"s are mapped to "@"s.
   */
  private final String speedLimitDescription;
  private final String routeTypeQualifier1;
  private final String routeTypeQualifier2;
  private final String routeTypeQualifier3;
  private final String preferredMultipleApproachIndicator;
  private final int fileRecordNumber;
  private final String lastUpdateCycle;

  private ArincProcedureLeg(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.airportIdentifier = builder.airportIdentifier;
    this.airportIcaoRegion = builder.airportIcaoRegion;
    this.subSectionCode = builder.subSectionCode;
    this.sidStarIdentifier = builder.sidStarIdentifier;
    this.routeType = builder.routeType;
    this.transitionIdentifier = builder.transitionIdentifier;
    this.categoryOrType = builder.categoryOrType;
    this.sequenceNumber = builder.sequenceNumber;
    this.fixIdentifier = builder.fixIdentifier;
    this.fixIcaoRegion = builder.fixIcaoRegion;
    this.fixSectionCode = builder.fixSectionCode;
    this.fixSubSectionCode = builder.fixSubSectionCode;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.waypointDescription = builder.waypointDescription;
    this.turnDirection = builder.turnDirection;
    this.rnp = ofNullable(builder.rnp).map(Double::floatValue).orElse(null);
    this.pathTerminator = builder.pathTerminator;
    this.turnDirectionValid = builder.turnDirectionValid;
    this.recommendedNavaidIdentifier = builder.recommendedNavaidIdentifier;
    this.recommendedNavaidIcaoRegion = builder.recommendedNavaidIcaoRegion;
    this.legInboundOutboundIdentifier = builder.legInboundOutboundIndicator;
    this.arcRadius = ofNullable(builder.arcRadius).map(Double::floatValue).orElse(null);
    this.theta = ofNullable(builder.theta).map(Double::floatValue).orElse(null);
    this.rho = ofNullable(builder.rho).map(Double::floatValue).orElse(null);
    this.outboundMagneticCourse = ofNullable(builder.outboundMagneticCourse).map(Double::floatValue).orElse(null);
    this.routeHoldDistanceTime = builder.routeHoldDistanceTime;
    this.holdTime = builder.holdTime;
    this.routeDistance = ofNullable(builder.routeDistance).map(Double::floatValue).orElse(null);
    this.recommendedNavaidSectionCode = builder.recommendedNavaidSectionCode;
    this.recommendedNavaidSubSectionCode = builder.recommendedNavaidSubSectionCode;
    this.altitudeDescription = builder.altitudeDescription;
    this.minAltitude1 = ofNullable(builder.minAltitude1).map(Double::floatValue).orElse(null);
    this.minAltitude2 = ofNullable(builder.minAltitude2).map(Double::floatValue).orElse(null);
    this.transitionAltitude = ofNullable(builder.transitionAltitude).map(Double::floatValue).orElse(null);
    this.speedLimit = builder.speedLimit;
    this.verticalAngle = ofNullable(builder.verticalAngle).map(Double::floatValue).orElse(null);
    this.centerFixIdentifier = builder.centerFixIdentifier;
    this.centerFixIcaoRegion = builder.centerFixIcaoRegion;
    this.centerFixSectionCode = builder.centerFixSectionCode;
    this.centerFixSubSectionCode = builder.centerFixSubSectionCode;
    this.gnssFmsIndicator = builder.gnssFmsIndicator;
    this.speedLimitDescription = builder.speedLimitDescription;
    this.routeTypeQualifier1 = builder.routeTypeQualifier1;
    this.routeTypeQualifier2 = builder.routeTypeQualifier2;
    this.routeTypeQualifier3 = builder.routeTypeQualifier3;
    this.preferredMultipleApproachIndicator = builder.preferredMultipleApproachIndicator;
    this.fileRecordNumber = builder.fileRecordNumber;
    this.lastUpdateCycle = builder.lastUpdateCycle;
  }

  public RecordType recordType() {
    return recordType;
  }

  public Optional<CustomerAreaCode> customerAreaCode() {
    return ofNullable(customerAreaCode);
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

  public String sidStarIdentifier() {
    return sidStarIdentifier;
  }

  public String routeType() {
    return routeType;
  }

  public Optional<String> transitionIdentifier() {
    return ofNullable(transitionIdentifier);
  }

  public Optional<String> categoryOrType() {
    return ofNullable(categoryOrType);
  }

  public int sequenceNumber() {
    return sequenceNumber;
  }

  public Optional<String> fixIdentifier() {
    return ofNullable(fixIdentifier);
  }

  public Optional<String> fixIcaoRegion() {
    return ofNullable(fixIcaoRegion);
  }

  public Optional<SectionCode> fixSectionCode() {
    return ofNullable(fixSectionCode);
  }

  public Optional<String> fixSubSectionCode() {
    return ofNullable(fixSubSectionCode);
  }

  public Optional<String> continuationRecordNumber() {
    return ofNullable(continuationRecordNumber);
  }

  public Optional<String> waypointDescription() {
    return ofNullable(waypointDescription);
  }

  public Optional<TurnDirection> turnDirection() {
    return ofNullable(turnDirection);
  }

  public Optional<Double> rnp() {
    return ofNullable(rnp).map(Float::doubleValue);
  }

  public PathTerminator pathTerm() {
    return pathTerminator;
  }

  public Optional<Boolean> turnDirectionValid() {
    return ofNullable(turnDirectionValid);
  }

  public Optional<String> recommendedNavaidIdentifier() {
    return ofNullable(recommendedNavaidIdentifier);
  }

  public Optional<String> recommendedNavaidIcaoRegion() {
    return ofNullable(recommendedNavaidIcaoRegion);
  }

  public Optional<Double> arcRadius() {
    return ofNullable(arcRadius).map(Float::doubleValue);
  }

  public Optional<Double> theta() {
    return ofNullable(theta).map(Float::doubleValue);
  }

  public Optional<Double> rho() {
    return ofNullable(rho).map(Float::doubleValue);
  }

  public Optional<Double> outboundMagneticCourse() {
    return ofNullable(outboundMagneticCourse).map(Float::doubleValue);
  }

  public Optional<String> routeHoldDistanceTime() {
    return ofNullable(routeHoldDistanceTime);
  }

  public Optional<Duration> holdTime() {
    return ofNullable(holdTime);
  }

  public Optional<Double> routeDistance() {
    return ofNullable(routeDistance).map(Float::doubleValue);
  }

  public Optional<SectionCode> recommendedNavaidSectionCode() {
    return ofNullable(recommendedNavaidSectionCode);
  }

  public Optional<String> recommendedNavaidSubSectionCode() {
    return ofNullable(recommendedNavaidSubSectionCode);
  }

  public Optional<String> legInboundOutboundIdentifier() {
    return ofNullable(legInboundOutboundIdentifier);
  }

  public Optional<String> altitudeDescription() {
    return ofNullable(altitudeDescription);
  }

  public Optional<Double> minAltitude1() {
    return ofNullable(minAltitude1).map(Float::doubleValue);
  }

  public Optional<Double> minAltitude2() {
    return ofNullable(minAltitude2).map(Float::doubleValue);
  }

  public Optional<Double> transitionAltitude() {
    return ofNullable(transitionAltitude).map(Float::doubleValue);
  }

  public Optional<Integer> speedLimit() {
    return ofNullable(speedLimit);
  }

  public Optional<Double> verticalAngle() {
    return ofNullable(verticalAngle).map(Float::doubleValue);
  }

  public Optional<String> centerFixIdentifier() {
    return ofNullable(centerFixIdentifier);
  }

  public Optional<String> centerFixIcaoRegion() {
    return ofNullable(centerFixIcaoRegion);
  }

  public Optional<SectionCode> centerFixSectionCode() {
    return ofNullable(centerFixSectionCode);
  }

  public Optional<String> centerFixSubSectionCode() {
    return ofNullable(centerFixSubSectionCode);
  }

  public Optional<String> gnssFmsIndicator() {
    return ofNullable(gnssFmsIndicator);
  }

  public Optional<String> speedLimitDescription() {
    return ofNullable(speedLimitDescription);
  }

  public Optional<String> routeTypeQualifier1() {
    return ofNullable(routeTypeQualifier1);
  }

  public Optional<String> routeTypeQualifier2() {
    return ofNullable(routeTypeQualifier2);
  }

  public Optional<String> routeTypeQualifier3() {
    return ofNullable(routeTypeQualifier3);
  }

  public Optional<String> preferredMultipleApproachIndicator() {
    return ofNullable(preferredMultipleApproachIndicator);
  }

  public int fileRecordNumber() {
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
        .sidStarIdentifier(sidStarIdentifier())
        .routeType(routeType())
        .transitionIdentifier(transitionIdentifier().orElse(null))
        .categoryOrType(categoryOrType().orElse(null))
        .sequenceNumber(sequenceNumber())
        .fixIdentifier(fixIdentifier().orElse(null))
        .fixIcaoRegion(fixIcaoRegion().orElse(null))
        .fixSectionCode(fixSectionCode().orElse(null))
        .fixSubSectionCode(fixSubSectionCode().orElse(null))
        .continuationRecordNumber(continuationRecordNumber().orElse(null))
        .waypointDescription(waypointDescription().orElse(null))
        .turnDirection(turnDirection().orElse(null))
        .rnp(rnp().orElse(null))
        .pathTerm(pathTerm())
        .turnDirectionValid(turnDirectionValid().orElse(null))
        .recommendedNavaidIdentifier(recommendedNavaidIdentifier().orElse(null))
        .recommendedNavaidIcaoRegion(recommendedNavaidIcaoRegion().orElse(null))
        .legInboundOutboundIndicator(legInboundOutboundIdentifier().orElse(null))
        .arcRadius(arcRadius().orElse(null))
        .theta(theta().orElse(null))
        .rho(rho().orElse(null))
        .outboundMagneticCourse(outboundMagneticCourse().orElse(null))
        .routeHoldDistanceTime(routeHoldDistanceTime().orElse(null))
        .holdTime(holdTime().orElse(null))
        .routeDistance(routeDistance().orElse(null))
        .recommendedNavaidSectionCode(recommendedNavaidSectionCode().orElse(null))
        .recommendedNavaidSubSectionCode(recommendedNavaidSubSectionCode().orElse(null))
        .altitudeDescription(altitudeDescription().orElse(null))
        .minAltitude1(minAltitude1().orElse(null))
        .minAltitude2(minAltitude2().orElse(null))
        .transitionAltitude(transitionAltitude().orElse(null))
        .speedLimit(speedLimit().orElse(null))
        .verticalAngle(verticalAngle().orElse(null))
        .centerFixIdentifier(centerFixIdentifier().orElse(null))
        .centerFixIcaoRegion(centerFixIcaoRegion().orElse(null))
        .centerFixSectionCode(centerFixSectionCode().orElse(null))
        .centerFixSubSectionCode(centerFixSubSectionCode().orElse(null))
        .gnssFmsIndicator(gnssFmsIndicator().orElse(null))
        .speedLimitDescription(speedLimitDescription().orElse(null))
        .routeTypeQualifier1(routeTypeQualifier1().orElse(null))
        .routeTypeQualifier2(routeTypeQualifier2().orElse(null))
        .routeTypeQualifier3(routeTypeQualifier3().orElse(null))
        .preferredMultipleApproachIndicator(preferredMultipleApproachIndicator().orElse(null))
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
    ArincProcedureLeg that = (ArincProcedureLeg) o;
    return recordType == that.recordType &&
        customerAreaCode == that.customerAreaCode &&
        sectionCode == that.sectionCode &&
        Objects.equals(airportIdentifier, that.airportIdentifier) &&
        Objects.equals(airportIcaoRegion, that.airportIcaoRegion) &&
        Objects.equals(subSectionCode, that.subSectionCode) &&
        Objects.equals(sidStarIdentifier, that.sidStarIdentifier) &&
        Objects.equals(routeType, that.routeType) &&
        Objects.equals(transitionIdentifier, that.transitionIdentifier) &&
        Objects.equals(categoryOrType, that.categoryOrType) &&
        Objects.equals(sequenceNumber, that.sequenceNumber) &&
        Objects.equals(fixIdentifier, that.fixIdentifier) &&
        Objects.equals(fixIcaoRegion, that.fixIcaoRegion) &&
        fixSectionCode == that.fixSectionCode &&
        Objects.equals(fixSubSectionCode, that.fixSubSectionCode) &&
        Objects.equals(continuationRecordNumber, that.continuationRecordNumber) &&
        Objects.equals(waypointDescription, that.waypointDescription) &&
        turnDirection == that.turnDirection &&
        Objects.equals(rnp, that.rnp) &&
        Objects.equals(pathTerminator, that.pathTerminator) &&
        Objects.equals(turnDirectionValid, that.turnDirectionValid) &&
        Objects.equals(recommendedNavaidIdentifier, that.recommendedNavaidIdentifier) &&
        Objects.equals(recommendedNavaidIcaoRegion, that.recommendedNavaidIcaoRegion) &&
        Objects.equals(legInboundOutboundIdentifier, that.legInboundOutboundIdentifier) &&
        Objects.equals(arcRadius, that.arcRadius) &&
        Objects.equals(theta, that.theta) &&
        Objects.equals(rho, that.rho) &&
        Objects.equals(outboundMagneticCourse, that.outboundMagneticCourse) &&
        Objects.equals(routeHoldDistanceTime, that.routeHoldDistanceTime) &&
        Objects.equals(holdTime, that.holdTime) &&
        Objects.equals(routeDistance, that.routeDistance) &&
        recommendedNavaidSectionCode == that.recommendedNavaidSectionCode &&
        Objects.equals(recommendedNavaidSubSectionCode, that.recommendedNavaidSubSectionCode) &&
        Objects.equals(altitudeDescription, that.altitudeDescription) &&
        Objects.equals(minAltitude1, that.minAltitude1) &&
        Objects.equals(minAltitude2, that.minAltitude2) &&
        Objects.equals(transitionAltitude, that.transitionAltitude) &&
        Objects.equals(speedLimit, that.speedLimit) &&
        Objects.equals(verticalAngle, that.verticalAngle) &&
        Objects.equals(centerFixIdentifier, that.centerFixIdentifier) &&
        Objects.equals(centerFixIcaoRegion, that.centerFixIcaoRegion) &&
        centerFixSectionCode == that.centerFixSectionCode &&
        Objects.equals(centerFixSubSectionCode, that.centerFixSubSectionCode) &&
        Objects.equals(gnssFmsIndicator, that.gnssFmsIndicator) &&
        Objects.equals(speedLimitDescription, that.speedLimitDescription) &&
        Objects.equals(routeTypeQualifier1, that.routeTypeQualifier1) &&
        Objects.equals(routeTypeQualifier2, that.routeTypeQualifier2) &&
        Objects.equals(routeTypeQualifier3, that.routeTypeQualifier3) &&
        Objects.equals(preferredMultipleApproachIndicator, that.preferredMultipleApproachIndicator) &&
        Objects.equals(fileRecordNumber, that.fileRecordNumber) &&
        Objects.equals(lastUpdateCycle, that.lastUpdateCycle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, airportIdentifier, airportIcaoRegion, subSectionCode, sidStarIdentifier, routeType, transitionIdentifier, categoryOrType, sequenceNumber, fixIdentifier, fixIcaoRegion, fixSectionCode, fixSubSectionCode, continuationRecordNumber, waypointDescription, turnDirection, rnp, pathTerminator, turnDirectionValid, recommendedNavaidIdentifier, recommendedNavaidIcaoRegion, legInboundOutboundIdentifier, arcRadius, theta, rho, outboundMagneticCourse, routeHoldDistanceTime, holdTime, routeDistance, recommendedNavaidSectionCode, recommendedNavaidSubSectionCode, altitudeDescription, minAltitude1, minAltitude2, transitionAltitude, speedLimit, verticalAngle, centerFixIdentifier, centerFixIcaoRegion, centerFixSectionCode, centerFixSubSectionCode, gnssFmsIndicator, speedLimitDescription, routeTypeQualifier1, routeTypeQualifier2, routeTypeQualifier3, preferredMultipleApproachIndicator, fileRecordNumber, lastUpdateCycle);
  }

  @Override
  public String toString() {
    return "ArincProcedureLeg{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode=" + sectionCode +
        ", airportIdentifier='" + airportIdentifier + '\'' +
        ", airportIcaoRegion='" + airportIcaoRegion + '\'' +
        ", subSectionCode='" + subSectionCode + '\'' +
        ", sidStarIdentifier='" + sidStarIdentifier + '\'' +
        ", routeType='" + routeType + '\'' +
        ", transitionIdentifier='" + transitionIdentifier + '\'' +
        ", categoryOrType='" + categoryOrType + '\'' +
        ", sequenceNumber=" + sequenceNumber +
        ", fixIdentifier='" + fixIdentifier + '\'' +
        ", fixIcaoRegion='" + fixIcaoRegion + '\'' +
        ", fixSectionCode=" + fixSectionCode +
        ", fixSubSectionCode='" + fixSubSectionCode + '\'' +
        ", continuationRecordNumber='" + continuationRecordNumber + '\'' +
        ", waypointDescription='" + waypointDescription + '\'' +
        ", turnDirection=" + turnDirection +
        ", rnp=" + rnp +
        ", pathTerm=" + pathTerminator +
        ", turnDirectionValid=" + turnDirectionValid +
        ", recommendedNavaidIdentifier='" + recommendedNavaidIdentifier + '\'' +
        ", recommendedNavaidIcaoRegion='" + recommendedNavaidIcaoRegion + '\'' +
        ", legInboundOutboundIdentifier='" + legInboundOutboundIdentifier + '\'' +
        ", arcRadius=" + arcRadius +
        ", theta=" + theta +
        ", rho=" + rho +
        ", outboundMagneticCourse=" + outboundMagneticCourse +
        ", routeHoldDistanceTime='" + routeHoldDistanceTime + '\'' +
        ", holdTime=" + holdTime +
        ", routeDistance=" + routeDistance +
        ", recommendedNavaidSectionCode=" + recommendedNavaidSectionCode +
        ", recommendedNavaidSubSectionCode='" + recommendedNavaidSubSectionCode + '\'' +
        ", altitudeDescription='" + altitudeDescription + '\'' +
        ", minAltitude1=" + minAltitude1 +
        ", minAltitude2=" + minAltitude2 +
        ", transitionAltitude=" + transitionAltitude +
        ", speedLimit=" + speedLimit +
        ", verticalAngle=" + verticalAngle +
        ", centerFixIdentifier='" + centerFixIdentifier + '\'' +
        ", centerFixIcaoRegion='" + centerFixIcaoRegion + '\'' +
        ", centerFixSectionCode=" + centerFixSectionCode +
        ", centerFixSubSectionCode='" + centerFixSubSectionCode + '\'' +
        ", gnssFmsIndicator='" + gnssFmsIndicator + '\'' +
        ", speedLimitDescription=" + speedLimitDescription +
        ", routeTypeQualifier1='" + routeTypeQualifier1 + '\'' +
        ", routeTypeQualifier2='" + routeTypeQualifier2 + '\'' +
        ", routeTypeQualifier3='" + routeTypeQualifier3 + '\'' +
        ", preferredMultipleApproachIndicator='" + preferredMultipleApproachIndicator + '\'' +
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
    private String sidStarIdentifier;
    private String routeType;
    private String transitionIdentifier;
    private String categoryOrType;
    private Integer sequenceNumber;
    private String fixIdentifier;
    private String fixIcaoRegion;
    private SectionCode fixSectionCode;
    private String fixSubSectionCode;
    private String continuationRecordNumber;
    private String waypointDescription;
    private TurnDirection turnDirection;
    private Double rnp;
    private PathTerminator pathTerminator;
    private Boolean turnDirectionValid;
    private String recommendedNavaidIdentifier;
    private String recommendedNavaidIcaoRegion;
    private Double arcRadius;
    private Double theta;
    private Double rho;
    private Double outboundMagneticCourse;
    private String routeHoldDistanceTime;
    private Duration holdTime;
    private Double routeDistance;
    private SectionCode recommendedNavaidSectionCode;
    private String recommendedNavaidSubSectionCode;
    private String legInboundOutboundIndicator;
    private String altitudeDescription;
    private Double minAltitude1;
    private Double minAltitude2;
    private Double transitionAltitude;
    private Integer speedLimit;
    private Double verticalAngle;
    private String centerFixIdentifier;
    private String centerFixIcaoRegion;
    private SectionCode centerFixSectionCode;
    private String centerFixSubSectionCode;
    private String gnssFmsIndicator;
    private String speedLimitDescription;
    private String routeTypeQualifier1;
    private String routeTypeQualifier2;
    private String routeTypeQualifier3;
    private String preferredMultipleApproachIndicator;
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

    public Builder sidStarIdentifier(String sidStarIdentifier) {
      this.sidStarIdentifier = sidStarIdentifier;
      return this;
    }

    public Builder routeType(String routeType) {
      this.routeType = routeType;
      return this;
    }

    public Builder transitionIdentifier(String transitionIdentifier) {
      this.transitionIdentifier = transitionIdentifier;
      return this;
    }

    public Builder categoryOrType(String categoryOrType) {
      this.categoryOrType = categoryOrType;
      return this;
    }

    public Builder sequenceNumber(Integer sequenceNumber) {
      this.sequenceNumber = sequenceNumber;
      return this;
    }

    public Builder fixIdentifier(String fixIdentifier) {
      this.fixIdentifier = fixIdentifier;
      return this;
    }

    public Builder fixIcaoRegion(String fixIcaoRegion) {
      this.fixIcaoRegion = fixIcaoRegion;
      return this;
    }

    public Builder fixSectionCode(SectionCode fixSectionCode) {
      this.fixSectionCode = fixSectionCode;
      return this;
    }

    public Builder fixSubSectionCode(String fixSubSectionCode) {
      this.fixSubSectionCode = fixSubSectionCode;
      return this;
    }

    public Builder continuationRecordNumber(String continuationRecordNumber) {
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder waypointDescription(String waypointDescription) {
      this.waypointDescription = waypointDescription;
      return this;
    }

    public Builder turnDirection(TurnDirection turnDirection) {
      this.turnDirection = turnDirection;
      return this;
    }

    public Builder rnp(Double rnp) {
      this.rnp = rnp;
      return this;
    }

    public Builder pathTerm(PathTerminator pathTerminator) {
      this.pathTerminator = pathTerminator;
      return this;
    }

    public Builder turnDirectionValid(Boolean turnDirectionValid) {
      this.turnDirectionValid = turnDirectionValid;
      return this;
    }

    public Builder recommendedNavaidIdentifier(String recommendedNavaidIdentifier) {
      this.recommendedNavaidIdentifier = recommendedNavaidIdentifier;
      return this;
    }

    public Builder recommendedNavaidIcaoRegion(String recommendedNavaidIcaoRegion) {
      this.recommendedNavaidIcaoRegion = recommendedNavaidIcaoRegion;
      return this;
    }

    public Builder legInboundOutboundIndicator(String legInboundOutboundIndicator) {
      this.legInboundOutboundIndicator = legInboundOutboundIndicator;
      return this;
    }

    public Builder arcRadius(Double arcRadius) {
      this.arcRadius = arcRadius;
      return this;
    }

    public Builder theta(Double theta) {
      this.theta = theta;
      return this;
    }

    public Builder rho(Double rho) {
      this.rho = rho;
      return this;
    }

    public Builder outboundMagneticCourse(Double outboundMagneticCourse) {
      this.outboundMagneticCourse = outboundMagneticCourse;
      return this;
    }

    public Builder routeHoldDistanceTime(String routeHoldDistanceTime) {
      this.routeHoldDistanceTime = routeHoldDistanceTime;
      return this;
    }

    public Builder holdTime(Duration holdTime) {
      this.holdTime = holdTime;
      return this;
    }

    public Builder routeDistance(Double routeDistance) {
      this.routeDistance = routeDistance;
      return this;
    }

    public Builder recommendedNavaidSectionCode(SectionCode recommendedNavaidSectionCode) {
      this.recommendedNavaidSectionCode = recommendedNavaidSectionCode;
      return this;
    }

    public Builder recommendedNavaidSubSectionCode(String recommendedNavaidSubSectionCode) {
      this.recommendedNavaidSubSectionCode = recommendedNavaidSubSectionCode;
      return this;
    }

    public Builder altitudeDescription(String altitudeDescription) {
      this.altitudeDescription = altitudeDescription;
      return this;
    }

    public Builder minAltitude1(Double minAltitude1) {
      this.minAltitude1 = minAltitude1;
      return this;
    }

    public Builder minAltitude2(Double minAltitide2) {
      this.minAltitude2 = minAltitide2;
      return this;
    }

    public Builder transitionAltitude(Double transitionAltitude) {
      this.transitionAltitude = transitionAltitude;
      return this;
    }

    public Builder speedLimit(Integer speedLimit) {
      this.speedLimit = speedLimit;
      return this;
    }

    public Builder verticalAngle(Double verticalAngle) {
      this.verticalAngle = verticalAngle;
      return this;
    }

    public Builder centerFixIdentifier(String centerFixIdentifier) {
      this.centerFixIdentifier = centerFixIdentifier;
      return this;
    }

    public Builder centerFixIcaoRegion(String centerFixIcaoRegion) {
      this.centerFixIcaoRegion = centerFixIcaoRegion;
      return this;
    }

    public Builder centerFixSectionCode(SectionCode centerFixSectionCode) {
      this.centerFixSectionCode = centerFixSectionCode;
      return this;
    }

    public Builder centerFixSubSectionCode(String centerFixSubSectionCode) {
      this.centerFixSubSectionCode = centerFixSubSectionCode;
      return this;
    }

    public Builder gnssFmsIndicator(String gnssFmsIndicator) {
      this.gnssFmsIndicator = gnssFmsIndicator;
      return this;
    }

    public Builder speedLimitDescription(String speedLimitDescription) {
      this.speedLimitDescription = speedLimitDescription;
      return this;
    }

    public Builder routeTypeQualifier1(String routeTypeQualifier1) {
      this.routeTypeQualifier1 = routeTypeQualifier1;
      return this;
    }

    public Builder routeTypeQualifier2(String routeTypeQualifier2) {
      this.routeTypeQualifier2 = routeTypeQualifier2;
      return this;
    }

    public Builder routeTypeQualifier3(String routeTypeQualifier3) {
      this.routeTypeQualifier3 = routeTypeQualifier3;
      return this;
    }

    public Builder preferredMultipleApproachIndicator(String preferredMultipleApproachIndicator) {
      this.preferredMultipleApproachIndicator = preferredMultipleApproachIndicator;
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

    public ArincProcedureLeg build() {
      return new ArincProcedureLeg(this);
    }
  }
}
