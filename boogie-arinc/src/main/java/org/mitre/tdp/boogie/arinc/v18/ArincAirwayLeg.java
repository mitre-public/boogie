package org.mitre.tdp.boogie.arinc.v18;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.BoundaryCode;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

/**
 * Data class for representing structured/parsed content from within an ARINC format airway leg record.
 * <br>
 * Section/Subsection = ER
 * <br>
 * ARINC stores airways as contiguous records with each record representing a leg within the top level airway structure - this
 * class then mimics a single line of an ARINC airway record. These legs can then be sequenced to produce a full airway definition.
 */
public final class ArincAirwayLeg {

  private final RecordType recordType;
  private final CustomerAreaCode customerAreaCode;
  private final SectionCode sectionCode;
  private final String subSectionCode;
  private final String routeIdentifier;
  private final String sixthCharacter;
  private final Integer sequenceNumber;
  private final String fixIdentifier;
  private final String fixIcaoRegion;
  private final SectionCode fixSectionCode;
  private final String fixSubSectionCode;
  private final String continuationRecordNumber;
  private final String waypointDescription;
  private final BoundaryCode boundaryCode;
  private final String routeType;
  private final Level level;
  private final String directionRestriction;
  private final String cruiseTableIndicator;
  private final Boolean euIndicator;
  private final String recommendedNavaidIdentifier;
  private final String recommendedNavaidIcaoRegion;
  private final Double theta;
  private final Double rnp;
  private final Double rho;
  private final Double outboundMagneticCourse;
  private final String routeHoldDistanceTime;
  private final Duration holdTime;
  private final Double routeDistance;
  private final Double inboundMagneticCourse;
  private final Double minAltitude1;
  private final Double minAltitude2;
  private final Double maxAltitude;
  private final Double fixedRadiusTransitionIndicator;
  private final Integer fileRecordNumber;
  private final String lastUpdateCycle;

  private ArincAirwayLeg(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.subSectionCode = builder.subSectionCode;
    this.routeIdentifier = builder.routeIdentifier;
    this.sixthCharacter = builder.sixthCharacter;
    this.sequenceNumber = builder.sequenceNumber;
    this.fixIdentifier = builder.fixIdentifier;
    this.fixIcaoRegion = builder.fixIcaoRegion;
    this.fixSectionCode = builder.fixSectionCode;
    this.fixSubSectionCode = builder.fixSubSectionCode;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.waypointDescription = builder.waypointDescription;
    this.boundaryCode = builder.boundaryCode;
    this.routeType = builder.routeType;
    this.level = builder.level;
    this.directionRestriction = builder.directionRestriction;
    this.cruiseTableIndicator = builder.cruiseTableIndicator;
    this.euIndicator = builder.euIndicator;
    this.recommendedNavaidIdentifier = builder.recommendedNavaidIdentifier;
    this.recommendedNavaidIcaoRegion = builder.recommendedNavaidIcaoRegion;
    this.theta = builder.theta;
    this.rnp = builder.rnp;
    this.rho = builder.rho;
    this.outboundMagneticCourse = builder.outboundMagneticCourse;
    this.routeHoldDistanceTime = builder.routeHoldDistanceTime;
    this.holdTime = builder.holdTime;
    this.routeDistance = builder.routeDistance;
    this.inboundMagneticCourse = builder.inboundMagneticCourse;
    this.minAltitude1 = builder.minAltitude1;
    this.minAltitude2 = builder.minAltitude2;
    this.maxAltitude = builder.maxAltitude;
    this.fixedRadiusTransitionIndicator = builder.fixedRadiusTransitionIndicator;
    this.fileRecordNumber = builder.fileRecordNumber;
    this.lastUpdateCycle = builder.lastUpdateCycle;
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

  public String subSectionCode() {
    return subSectionCode;
  }

  public String routeIdentifier() {
    return routeIdentifier;
  }

  public Optional<String> sixthCharacter() {
    return Optional.ofNullable(sixthCharacter);
  }

  public Integer sequenceNumber() {
    return sequenceNumber;
  }

  public String fixIdentifier() {
    return fixIdentifier;
  }

  public String fixIcaoRegion() {
    return fixIcaoRegion;
  }

  public SectionCode fixSectionCode() {
    return fixSectionCode;
  }

  public String fixSubSectionCode() {
    return fixSubSectionCode;
  }

  public String continuationRecordNumber() {
    return continuationRecordNumber;
  }

  public Optional<String> waypointDescription() {
    return Optional.ofNullable(waypointDescription);
  }

  public Optional<BoundaryCode> boundaryCode() {
    return Optional.ofNullable(boundaryCode);
  }

  public Optional<String> routeType() {
    return Optional.ofNullable(routeType);
  }

  public Optional<Level> level() {
    return Optional.ofNullable(level);
  }

  public Optional<String> directionRestriction() {
    return Optional.ofNullable(directionRestriction);
  }

  public Optional<String> cruiseTableIndicator() {
    return Optional.ofNullable(cruiseTableIndicator);
  }

  public Optional<Boolean> euIndicator() {
    return Optional.ofNullable(euIndicator);
  }

  public Optional<String> recommendedNavaidIdentifier() {
    return Optional.ofNullable(recommendedNavaidIdentifier);
  }

  public Optional<String> recommendedNavaidIcaoRegion() {
    return Optional.ofNullable(recommendedNavaidIcaoRegion);
  }

  public Optional<Double> theta() {
    return Optional.ofNullable(theta);
  }

  public Optional<Double> rnp() {
    return Optional.ofNullable(rnp);
  }

  public Optional<Double> rho() {
    return Optional.ofNullable(rho);
  }

  public Optional<Double> outboundMagneticCourse() {
    return Optional.ofNullable(outboundMagneticCourse);
  }

  public Optional<String >routeHoldDistanceTime() {
    return Optional.ofNullable(routeHoldDistanceTime);
  }

  public Optional<Duration> holdTime() {
    return Optional.ofNullable(holdTime);
  }

  public Optional<Double >routeDistance() {
    return Optional.ofNullable(routeDistance);
  }

  public Optional<Double> inboundMagneticCourse() {
    return Optional.ofNullable(inboundMagneticCourse);
  }

  public Optional<Double> minAltitude1() {
    return Optional.ofNullable(minAltitude1);
  }

  public Optional<Double> minAltitude2() {
    return Optional.ofNullable(minAltitude2);
  }

  public Optional<Double> maxAltitude() {
    return Optional.ofNullable(maxAltitude);
  }

  public Optional<Double> fixedRadiusTransitionIndicator() {
    return Optional.ofNullable(fixedRadiusTransitionIndicator);
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
        .customerAreaCode(customerAreaCode())
        .sectionCode(sectionCode())
        .subSectionCode(subSectionCode())
        .routeIdentifier(routeIdentifier())
        .sixthCharacter(sixthCharacter().orElse(null))
        .sequenceNumber(sequenceNumber())
        .fixIdentifier(fixIdentifier())
        .fixIcaoRegion(fixIcaoRegion())
        .fixSectionCode(fixSectionCode())
        .fixSubSectionCode(fixSubSectionCode())
        .continuationRecordNumber(continuationRecordNumber())
        .waypointDescription(waypointDescription().orElse(null))
        .boundaryCode(boundaryCode().orElse(null))
        .routeType(routeType().orElse(null))
        .level(level().orElse(null))
        .directionRestriction(directionRestriction().orElse(null))
        .cruiseTableIndicator(cruiseTableIndicator().orElse(null))
        .euIndicator(euIndicator().orElse(null))
        .recommendedNavaidIdentifier(recommendedNavaidIdentifier().orElse(null))
        .recommendedNavaidIcaoRegion(recommendedNavaidIcaoRegion().orElse(null))
        .theta(theta().orElse(null))
        .rnp(rnp().orElse(null))
        .rho(rho().orElse(null))
        .outboundMagneticCourse(outboundMagneticCourse().orElse(null))
        .routeHoldDistanceTime(routeHoldDistanceTime().orElse(null))
        .holdTime(holdTime().orElse(null))
        .routeDistance(routeDistance().orElse(null))
        .inboundMagneticCourse(inboundMagneticCourse().orElse(null))
        .minAltitude1(minAltitude1().orElse(null))
        .minAltitude2(minAltitude2().orElse(null))
        .maxAltitude(maxAltitude().orElse(null))
        .fixedRadiusTransitionIndicator(fixedRadiusTransitionIndicator().orElse(null))
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
    ArincAirwayLeg arincAir = (ArincAirwayLeg) o;
    return recordType == arincAir.recordType &&
        customerAreaCode == arincAir.customerAreaCode &&
        sectionCode == arincAir.sectionCode &&
        Objects.equals(subSectionCode, arincAir.subSectionCode) &&
        Objects.equals(routeIdentifier, arincAir.routeIdentifier) &&
        Objects.equals(sixthCharacter, arincAir.sixthCharacter) &&
        Objects.equals(sequenceNumber, arincAir.sequenceNumber) &&
        Objects.equals(fixIdentifier, arincAir.fixIdentifier) &&
        Objects.equals(fixIcaoRegion, arincAir.fixIcaoRegion) &&
        fixSectionCode == arincAir.fixSectionCode &&
        Objects.equals(fixSubSectionCode, arincAir.fixSubSectionCode) &&
        Objects.equals(continuationRecordNumber, arincAir.continuationRecordNumber) &&
        Objects.equals(waypointDescription, arincAir.waypointDescription) &&
        boundaryCode == arincAir.boundaryCode &&
        Objects.equals(routeType, arincAir.routeType) &&
        level == arincAir.level &&
        Objects.equals(directionRestriction, arincAir.directionRestriction) &&
        Objects.equals(cruiseTableIndicator, arincAir.cruiseTableIndicator) &&
        Objects.equals(euIndicator, arincAir.euIndicator) &&
        Objects.equals(recommendedNavaidIdentifier, arincAir.recommendedNavaidIdentifier) &&
        Objects.equals(recommendedNavaidIcaoRegion, arincAir.recommendedNavaidIcaoRegion) &&
        Objects.equals(theta, arincAir.theta) &&
        Objects.equals(rnp, arincAir.rnp) &&
        Objects.equals(rho, arincAir.rho) &&
        Objects.equals(outboundMagneticCourse, arincAir.outboundMagneticCourse) &&
        Objects.equals(routeHoldDistanceTime, arincAir.routeHoldDistanceTime) &&
        Objects.equals(holdTime, arincAir.holdTime) &&
        Objects.equals(routeDistance, arincAir.routeDistance) &&
        Objects.equals(inboundMagneticCourse, arincAir.inboundMagneticCourse) &&
        Objects.equals(minAltitude1, arincAir.minAltitude1) &&
        Objects.equals(minAltitude2, arincAir.minAltitude2) &&
        Objects.equals(maxAltitude, arincAir.maxAltitude) &&
        Objects.equals(fixedRadiusTransitionIndicator, arincAir.fixedRadiusTransitionIndicator) &&
        Objects.equals(fileRecordNumber, arincAir.fileRecordNumber) &&
        Objects.equals(lastUpdateCycle, arincAir.lastUpdateCycle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, subSectionCode, routeIdentifier, sixthCharacter, sequenceNumber, fixIdentifier, fixIcaoRegion, fixSectionCode, fixSubSectionCode, continuationRecordNumber, waypointDescription, boundaryCode, routeType, level, directionRestriction, cruiseTableIndicator, euIndicator, recommendedNavaidIdentifier, recommendedNavaidIcaoRegion, theta, rnp, rho, outboundMagneticCourse, routeHoldDistanceTime, holdTime, routeDistance, inboundMagneticCourse, minAltitude1, minAltitude2, maxAltitude, fixedRadiusTransitionIndicator, fileRecordNumber, lastUpdateCycle);
  }

  @Override
  public String toString() {
    return "ArincAirwayLeg{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode=" + sectionCode +
        ", subSectionCode='" + subSectionCode + '\'' +
        ", routeIdentifier='" + routeIdentifier + '\'' +
        ", sixthCharacter='" + sixthCharacter + '\'' +
        ", sequenceNumber=" + sequenceNumber +
        ", fixIdentifier='" + fixIdentifier + '\'' +
        ", fixIcaoRegion='" + fixIcaoRegion + '\'' +
        ", fixSectionCode=" + fixSectionCode +
        ", fixSubSectionCode='" + fixSubSectionCode + '\'' +
        ", continuationRecordNumber='" + continuationRecordNumber + '\'' +
        ", waypointDescription='" + waypointDescription + '\'' +
        ", boundaryCode=" + boundaryCode +
        ", routeType='" + routeType + '\'' +
        ", level=" + level +
        ", directionRestriction='" + directionRestriction + '\'' +
        ", cruiseTableIndicator='" + cruiseTableIndicator + '\'' +
        ", euIndicator='" + euIndicator + '\'' +
        ", recommendedNavaidIdentifier='" + recommendedNavaidIdentifier + '\'' +
        ", recommendedNavaidIcaoRegion='" + recommendedNavaidIcaoRegion + '\'' +
        ", theta=" + theta +
        ", rnp=" + rnp +
        ", rho=" + rho +
        ", outboundMagneticCourse=" + outboundMagneticCourse +
        ", routeHoldDistanceTime='" + routeHoldDistanceTime + '\'' +
        ", holdTime=" + holdTime +
        ", routeDistance=" + routeDistance +
        ", inboundMagneticCourse=" + inboundMagneticCourse +
        ", minAltitude1=" + minAltitude1 +
        ", minAltitude2=" + minAltitude2 +
        ", maxAltitude=" + maxAltitude +
        ", fixedRadiusTransitionIndicator=" + fixedRadiusTransitionIndicator +
        ", fileRecordNumber=" + fileRecordNumber +
        ", lastUpdateCycle='" + lastUpdateCycle + '\'' +
        '}';
  }

  public static final class Builder {
    private RecordType recordType;
    private CustomerAreaCode customerAreaCode;
    private SectionCode sectionCode;
    private String subSectionCode;
    private String routeIdentifier;
    private String sixthCharacter;
    private Integer sequenceNumber;
    private String fixIdentifier;
    private String fixIcaoRegion;
    private SectionCode fixSectionCode;
    private String fixSubSectionCode;
    private String continuationRecordNumber;
    private String waypointDescription;
    private BoundaryCode boundaryCode;
    private String routeType;
    private Level level;
    private String directionRestriction;
    private String cruiseTableIndicator;
    private Boolean euIndicator;
    private String recommendedNavaidIdentifier;
    private String recommendedNavaidIcaoRegion;
    private Double theta;
    private Double rnp;
    private Double rho;
    private Double outboundMagneticCourse;
    private String routeHoldDistanceTime;
    private Duration holdTime;
    private Double routeDistance;
    private Double inboundMagneticCourse;
    private Double minAltitude1;
    private Double minAltitude2;
    private Double maxAltitude;
    private Double fixedRadiusTransitionIndicator;
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

    public Builder subSectionCode(String subSectionCode) {
      this.subSectionCode = subSectionCode;
      return this;
    }

    public Builder routeIdentifier(String routeIdentifier) {
      this.routeIdentifier = routeIdentifier;
      return this;
    }

    public Builder sixthCharacter(String sixthCharacter) {
      this.sixthCharacter = sixthCharacter;
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

    public Builder boundaryCode(BoundaryCode boundaryCode) {
      this.boundaryCode = boundaryCode;
      return this;
    }

    public Builder routeType(String routeType) {
      this.routeType = routeType;
      return this;
    }

    public Builder level(Level level) {
      this.level = level;
      return this;
    }

    public Builder directionRestriction(String directionRestriction) {
      this.directionRestriction = directionRestriction;
      return this;
    }

    public Builder cruiseTableIndicator(String cruiseTableIndicator) {
      this.cruiseTableIndicator = cruiseTableIndicator;
      return this;
    }

    public Builder euIndicator(Boolean euIndicator) {
      this.euIndicator = euIndicator;
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

    public Builder theta(Double theta) {
      this.theta = theta;
      return this;
    }

    public Builder rnp(Double rnp) {
      this.rnp = rnp;
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

    public Builder inboundMagneticCourse(Double inboundMagneticCourse) {
      this.inboundMagneticCourse = inboundMagneticCourse;
      return this;
    }

    public Builder minAltitude1(Double minAltitude1) {
      this.minAltitude1 = minAltitude1;
      return this;
    }

    public Builder minAltitude2(Double minAltitude2) {
      this.minAltitude2 = minAltitude2;
      return this;
    }

    public Builder maxAltitude(Double maxAltitude) {
      this.maxAltitude = maxAltitude;
      return this;
    }

    public Builder fixedRadiusTransitionIndicator(Double fixedRadiusTransitionIndicator) {
      this.fixedRadiusTransitionIndicator = fixedRadiusTransitionIndicator;
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

    public ArincAirwayLeg build() {
      return new ArincAirwayLeg(this);
    }
  }
}
