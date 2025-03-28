package org.mitre.tdp.boogie.arinc.model;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.arinc.v18.field.ArcRadius;
import org.mitre.tdp.boogie.arinc.v18.field.ContinuationRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Cycle;
import org.mitre.tdp.boogie.arinc.v18.field.DuplicateIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.FileRecordNumber;
import org.mitre.tdp.boogie.arinc.v18.field.FixIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.IcaoRegion;
import org.mitre.tdp.boogie.arinc.v18.field.InboundMagneticCourse;
import org.mitre.tdp.boogie.arinc.v18.field.LegLength;
import org.mitre.tdp.boogie.arinc.v18.field.LegTime;
import org.mitre.tdp.boogie.arinc.v18.field.MaxAltitude;
import org.mitre.tdp.boogie.arinc.v18.field.MinimumAltitude;
import org.mitre.tdp.boogie.arinc.v18.field.NameField;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.RegionCode;
import org.mitre.tdp.boogie.arinc.v18.field.Rnp;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimit;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v19.field.RvsmMaximumLevel;
import org.mitre.tdp.boogie.arinc.v19.field.RvsmMinimumLevel;
import org.mitre.tdp.boogie.arinc.v19.field.VerticalScaleFactor;
import org.mitre.tdp.boogie.arinc.v20.field.LegInboundOutboundIndicator;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public final class ArincHoldingPattern implements Comparable<ArincHoldingPattern> {
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
   * See {@link SubSectionCode}
   */
  private final String subSectionCode;
  /**
   * See {@link RegionCode}
   */
  private final String regionCode;
  /**
   * See {@link IcaoRegion}
   */
  private final String icaoRegion;
  /**
   * See {@link DuplicateIdentifier}
   */
  private final String duplicateIdentifier;
  /**
   * {@link FixIdentifier}
   */
  private final String fixIdentifier;
  /**
   * {@link IcaoRegion}
   */
  private final String fixIcaoRegion;
  /**
   * {@link SectionCode}
   */
  private final SectionCode fixSectionCode;
  /**
   * {@link SubSectionCode}
   */
  private final String fixSubsectionCode;
  /**
   * {@link ContinuationRecordNumber}
   */
  private final String continuationRecordNumber;
  /**
   * {@link InboundMagneticCourse}
   */
  private final Double inboundHoldingCourse;
  /**
   * {@link TurnDirection}
   */
  private final org.mitre.tdp.boogie.arinc.v18.field.TurnDirection turnDirection;
  /**
   * {@link LegLength}
   */
  private final Double legLength;
  /**
   * {@link LegTime}
   */
  private final Duration legTime;
  /**
   * {@link MinimumAltitude}
   */
  private final Double minimumAltitude;
  /**
   * {@link MaxAltitude}
   */
  private final Double maxAltitude;
  /**
   * {@link SpeedLimit}
   */
  private final Integer holdingSpeed;
  /**
   * {@link Rnp}
   */
  private final Double rnp;
  /**
   * {@link ArcRadius}
   */
  private final Double arcRadius;
  /**
   * {@link VerticalScaleFactor}
   */
  private final Integer verticalScaleFactor;
  /**
   * {@link RvsmMinimumLevel}
   */
  private final Integer rvsmMin;
  /**
   * {@link RvsmMaximumLevel}
   */
  private final Integer rvsmMax;
  /**
   * {@link LegInboundOutboundIndicator}
   */
  private final String inboundOutboundIndicator;
  /**
   * {@link NameField}
   */
  private final String holdingName;
  /**
   * {@link FileRecordNumber}
   */
  private final Integer fileRecordNumber;
  /**
   * {@link Cycle}
   */
  private final String lastUpdatedCycle;

  public ArincHoldingPattern(Builder builder) {
    this.recordType = builder.recordType;
    this.customerAreaCode = builder.customerAreaCode;
    this.sectionCode = builder.sectionCode;
    this.subSectionCode = builder.subSectionCode;
    this.regionCode = builder.regionCode;
    this.icaoRegion = builder.icaoRegion;
    this.duplicateIdentifier = builder.duplicateIdentifier;
    this.fixIdentifier = builder.fixIdentifier;
    this.fixIcaoRegion = builder.fixIcaoRegion;
    this.fixSectionCode = builder.fixSectionCode;
    this.fixSubsectionCode = builder.fixSubsectionCode;
    this.continuationRecordNumber = builder.continuationRecordNumber;
    this.inboundHoldingCourse = builder.inboundHoldingCourse;
    this.turnDirection = builder.turnDirection;
    this.legLength = builder.legLength;
    this.legTime = builder.legTime;
    this.minimumAltitude = builder.minimumAltitude;
    this.maxAltitude = builder.maxAltitude;
    this.holdingSpeed = builder.holdingSpeed;
    this.rnp = builder.rnp;
    this.arcRadius = builder.arcRadius;
    this.verticalScaleFactor = builder.verticalScaleFactor;
    this.rvsmMin = builder.rvsmMin;
    this.rvsmMax = builder.rvsmMax;
    this.inboundOutboundIndicator = builder.inboundOutboundIndicator;
    this.holdingName = builder.holdingName;
    this.fileRecordNumber = builder.fileRecordNumber;
    this.lastUpdatedCycle = builder.lastUpdatedCycle;
  }

  public Builder toBuilder() {
    return new Builder().recordType(this.recordType).customerAreaCode(this.customerAreaCode).sectionCode(this.sectionCode).subSectionCode(this.subSectionCode).regionCode(this.regionCode).icaoRegion(this.icaoRegion).duplicateIdentifier(this.duplicateIdentifier).fixIdentifier(this.fixIdentifier).fixIcaoRegion(this.fixIcaoRegion).fixSectionCode(this.fixSectionCode).fixSubsectionCode(this.fixSubsectionCode).continuationRecordNumber(this.continuationRecordNumber).inboundHoldingCourse(this.inboundHoldingCourse).turnDirection(this.turnDirection).legLength(this.legLength).legTime(this.legTime).minimumAltitude(this.minimumAltitude).maxAltitude(this.maxAltitude).holdingSpeed(this.holdingSpeed).rnp(this.rnp).arcRadius(this.arcRadius).verticalScaleFactor(this.verticalScaleFactor).rvsmMax(this.rvsmMax).rvsmMin(this.rvsmMin).holdingName(this.holdingName).fileRecordNumber(this.fileRecordNumber).lastUpdatedCycle(this.lastUpdatedCycle);
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

  public Optional<String> regionCode() {
    return Optional.ofNullable(regionCode);
  }

  public Optional<String> icaoRegion() {
    return Optional.ofNullable(icaoRegion);
  }

  public Optional<String> duplicateIdentifier() {
    return Optional.ofNullable(duplicateIdentifier);
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

  public String fixSubsectionCode() {
    return fixSubsectionCode;
  }

  public Optional<String> continuationRecordNumber() {
    return Optional.ofNullable(continuationRecordNumber);
  }

  public Double inboundHoldingCourse() {
    return inboundHoldingCourse;
  }

  public org.mitre.tdp.boogie.arinc.v18.field.TurnDirection turnDirection() {
    return turnDirection;
  }

  public Optional<Double> legLength() {
    return Optional.ofNullable(legLength);
  }

  public Optional<Duration> legTime() {
    return Optional.ofNullable(legTime);
  }

  public Optional<Double> minimumAltitude() {
    return Optional.ofNullable(minimumAltitude);
  }

  public Optional<Double> maxAltitude() {
    return Optional.ofNullable(maxAltitude);
  }

  public Optional<Integer> holdingSpeed() {
    return Optional.ofNullable(holdingSpeed);
  }

  public Optional<Double> rnp() {
    return Optional.ofNullable(rnp);
  }

  public Optional<Double> arcRadius() {
    return Optional.ofNullable(arcRadius);
  }

  public Optional<Integer> verticalScaleFactor() {
    return Optional.ofNullable(verticalScaleFactor);
  }

  public Optional<Integer> rvsmMin() {
    return Optional.ofNullable(rvsmMin);
  }

  public Optional<Integer> rvsmMax() {
    return Optional.ofNullable(rvsmMax);
  }

  public Optional<String> inboundOutboundIndicator() {
    return Optional.ofNullable(inboundOutboundIndicator);
  }

  public Optional<String> holdingName() {
    return Optional.ofNullable(holdingName);
  }

  public Optional<Integer> fileRecordNumber() {
    return Optional.ofNullable(fileRecordNumber);
  }

  public Optional<String> lastUpdatedCycle() {
    return Optional.ofNullable(lastUpdatedCycle);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ArincHoldingPattern that = (ArincHoldingPattern) o;
    return recordType == that.recordType
        && customerAreaCode == that.customerAreaCode
        && sectionCode == that.sectionCode
        && Objects.equals(subSectionCode, that.subSectionCode)
        && Objects.equals(regionCode, that.regionCode)
        && Objects.equals(icaoRegion, that.icaoRegion)
        && Objects.equals(duplicateIdentifier, that.duplicateIdentifier)
        && Objects.equals(fixIdentifier, that.fixIdentifier)
        && Objects.equals(fixIcaoRegion, that.fixIcaoRegion)
        && fixSectionCode == that.fixSectionCode
        && Objects.equals(fixSubsectionCode, that.fixSubsectionCode)
        && Objects.equals(continuationRecordNumber, that.continuationRecordNumber)
        && Objects.equals(inboundHoldingCourse, that.inboundHoldingCourse)
        && Objects.equals(turnDirection, that.turnDirection)
        && Objects.equals(legLength, that.legLength)
        && Objects.equals(legTime, that.legTime)
        && Objects.equals(minimumAltitude, that.minimumAltitude)
        && Objects.equals(maxAltitude, that.maxAltitude)
        && Objects.equals(holdingSpeed, that.holdingSpeed)
        && Objects.equals(rnp, that.rnp)
        && Objects.equals(arcRadius, that.arcRadius)
        && Objects.equals(verticalScaleFactor, that.verticalScaleFactor)
        && Objects.equals(rvsmMin, that.rvsmMin)
        && Objects.equals(rvsmMax, that.rvsmMax)
        && Objects.equals(inboundOutboundIndicator, that.inboundOutboundIndicator)
        && Objects.equals(holdingName, that.holdingName)
        && Objects.equals(fileRecordNumber, that.fileRecordNumber)
        && Objects.equals(lastUpdatedCycle, that.lastUpdatedCycle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordType, customerAreaCode, sectionCode, subSectionCode, regionCode, icaoRegion, duplicateIdentifier, fixIdentifier, fixIcaoRegion, fixSectionCode, fixSubsectionCode, continuationRecordNumber, inboundHoldingCourse, turnDirection, legLength, legTime, minimumAltitude, maxAltitude, holdingSpeed, rnp, arcRadius, verticalScaleFactor, rvsmMin, rvsmMax, inboundOutboundIndicator, holdingName, fileRecordNumber, lastUpdatedCycle);
  }

  @Override
  public String toString() {
    return "ArincHoldingPattern{" +
        "recordType=" + recordType +
        ", customerAreaCode=" + customerAreaCode +
        ", sectionCode=" + sectionCode +
        ", subSectionCode=" + subSectionCode +
        ", regionCode=" + regionCode +
        ", icaoRegion='" + icaoRegion + '\'' +
        ", duplicateIdentifier='" + duplicateIdentifier + '\'' +
        ", fixIdentifier='" + fixIdentifier + '\'' +
        ", fixIcaoRegion='" + fixIcaoRegion + '\'' +
        ", fixSectionCode=" + fixSectionCode +
        ", fixSubsectionCode=" + fixSubsectionCode +
        ", continuationRecordNumber=" + continuationRecordNumber +
        ", inboundHoldingCourse=" + inboundHoldingCourse +
        ", turnDirection=" + turnDirection +
        ", legLength=" + legLength +
        ", legTime=" + legTime +
        ", minimumAltitude=" + minimumAltitude +
        ", maxAltitude=" + maxAltitude +
        ", holdingSpeed=" + holdingSpeed +
        ", rnp=" + rnp +
        ", arcRadius=" + arcRadius +
        ", verticalScaleFactor='" + verticalScaleFactor + '\'' +
        ", rvsmMin='" + rvsmMin + '\'' +
        ", rvsmMax='" + rvsmMax + '\'' +
        ", inboundOutboundIndicator='" + inboundOutboundIndicator + '\'' +
        ", name='" + holdingName + '\'' +
        ", fileRecordNumber=" + fileRecordNumber +
        ", lastUpdatedCycle='" + lastUpdatedCycle + '\'' +
        '}';
  }

  /**
   * This is intended to get a list that can be sorted according to the holds at the same fix
   * then by the duplicate identifier. Then if somehow the duplicate code fails us
   * it will sort on the fix section/subsection with nulls last because screw navaids
   *
   * @param o the object to be compared.
   * @return compare result
   */
  @Override
  public int compareTo(ArincHoldingPattern o) {
    return ComparisonChain.start()
        .compare(recordType, o.recordType)
        .compare(customerAreaCode, o.customerAreaCode)
        .compare(regionCode, o.regionCode)
        .compare(fixIdentifier, o.fixIdentifier)
        .compare(fixIcaoRegion, o.fixIcaoRegion)
        .compare(duplicateIdentifier, o.duplicateIdentifier)
        .compare(fixSectionCode, o.fixSectionCode)
        .compare(fixSubsectionCode, fixSubsectionCode, Ordering.natural().nullsLast())
        .result();
  }

  public static class Builder {
    private RecordType recordType;
    private CustomerAreaCode customerAreaCode;
    private SectionCode sectionCode;
    private String subSectionCode;
    private String regionCode;
    private String icaoRegion;
    private String duplicateIdentifier;
    private String fixIdentifier;
    private String fixIcaoRegion;
    private SectionCode fixSectionCode;
    private String fixSubsectionCode;
    private String continuationRecordNumber;
    private Double inboundHoldingCourse;
    private org.mitre.tdp.boogie.arinc.v18.field.TurnDirection turnDirection;
    private Double legLength;
    private Duration legTime;
    private Double minimumAltitude;
    private Double maxAltitude;
    private Integer holdingSpeed;
    private Double rnp;
    private Double arcRadius;
    private Integer verticalScaleFactor;
    private Integer rvsmMin;
    private Integer rvsmMax;
    private String inboundOutboundIndicator;
    private String holdingName;
    private Integer fileRecordNumber;
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

    public Builder subSectionCode(String subSectionCode) {
      this.subSectionCode = subSectionCode;
      return this;
    }

    public Builder regionCode(String regionCode) {
      this.regionCode = regionCode;
      return this;
    }

    public Builder icaoRegion(String icaoRegion) {
      this.icaoRegion = icaoRegion;
      return this;
    }

    public Builder duplicateIdentifier(String duplicateIdentifier) {
      this.duplicateIdentifier = duplicateIdentifier;
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

    public Builder fixSubsectionCode(String fixSubsectionCode) {
      this.fixSubsectionCode = fixSubsectionCode;
      return this;
    }

    public Builder continuationRecordNumber(String continuationRecordNumber) {
      this.continuationRecordNumber = continuationRecordNumber;
      return this;
    }

    public Builder inboundHoldingCourse(Double inboundHoldingCourse) {
      this.inboundHoldingCourse = inboundHoldingCourse;
      return this;
    }

    public Builder turnDirection(org.mitre.tdp.boogie.arinc.v18.field.TurnDirection turnDirection) {
      this.turnDirection = turnDirection;
      return this;
    }

    public Builder legLength(Double legLength) {
      this.legLength = legLength;
      return this;
    }

    public Builder legTime(Duration legTime) {
      this.legTime = legTime;
      return this;
    }

    public Builder minimumAltitude(Double minimumAltitude) {
      this.minimumAltitude = minimumAltitude;
      return this;
    }

    public Builder maxAltitude(Double maxAltitude) {
      this.maxAltitude = maxAltitude;
      return this;
    }

    public Builder holdingSpeed(Integer holdingSpeed) {
      this.holdingSpeed = holdingSpeed;
      return this;
    }

    public Builder rnp(Double rnp) {
      this.rnp = rnp;
      return this;
    }

    public Builder arcRadius(Double arcRadius) {
      this.arcRadius = arcRadius;
      return this;
    }

    public Builder verticalScaleFactor(Integer verticalScaleFactor) {
      this.verticalScaleFactor = verticalScaleFactor;
      return this;
    }

    public Builder rvsmMin(Integer rvsmMin) {
      this.rvsmMin = rvsmMin;
      return this;
    }

    public Builder rvsmMax(Integer rvsmMax) {
      this.rvsmMax = rvsmMax;
      return this;
    }

    public Builder inboundOutboundIndicator(String inboundOutboundIndicator) {
      this.inboundOutboundIndicator = inboundOutboundIndicator;
      return this;
    }

    public Builder holdingName(String name) {
      this.holdingName = name;
      return this;
    }

    public Builder fileRecordNumber(Integer fileRecordNumber) {
      this.fileRecordNumber = fileRecordNumber;
      return this;
    }

    public Builder lastUpdatedCycle(String lastUpdatedCycle) {
      this.lastUpdatedCycle = lastUpdatedCycle;
      return this;
    }

    public ArincHoldingPattern build() {
      return new ArincHoldingPattern(this);
    }
  }
}
