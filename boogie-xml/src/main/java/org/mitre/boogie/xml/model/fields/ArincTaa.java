package org.mitre.boogie.xml.model.fields;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ArincTaa {
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;
  private final List<String> approachTypeIdentifier;
  /**
   * See {@link MagneticTrueIndicator}
   */
  private final String magneticTrueIndicator;
  /**
   * See {@link TaaSectorIdentifier}
   */
  private final String taaFixPositionIndicator;
  private final String taaIafWaypointRef;
  private final String sectorBearingWaypointRef;
  private final List<ArincTaaSectorDetails> sectorDetails;

  private ArincTaa(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.approachTypeIdentifier = builder.approachTypeIdentifier;
    this.magneticTrueIndicator = builder.magneticTrueIndicator;
    this.taaFixPositionIndicator = builder.taaFixPositionIndicator;
    this.taaIafWaypointRef = builder.taaIafWaypointRef;
    this.sectorBearingWaypointRef = builder.sectorBearingWaypointRef;
    this.sectorDetails = builder.sectorDetails;
  }

  public static Builder builder() {
    return new Builder();
  }

  public ArincBaseInfo baseInfo() {
    return baseInfo;
  }

  public ArincRecordInfo recordInfo() {
    return recordInfo;
  }

  public List<String> approachTypeIdentifier() {
    return approachTypeIdentifier == null ? List.of() : approachTypeIdentifier;
  }

  public Optional<MagneticTrueIndicator> magneticTrueIndicator() {
    return Optional.ofNullable(magneticTrueIndicator).filter(MagneticTrueIndicator.VALID::contains).map(MagneticTrueIndicator::valueOf);
  }

  public Optional<TaaSectorIdentifier> taaFixPositionIndicator() {
    return Optional.ofNullable(taaFixPositionIndicator).filter(TaaSectorIdentifier.VALID::contains).map(TaaSectorIdentifier::valueOf);
  }

  public Optional<String> taaIafWaypointRef() {
    return Optional.ofNullable(taaIafWaypointRef);
  }

  public Optional<String> sectorBearingWaypointRef() {
    return Optional.ofNullable(sectorBearingWaypointRef);
  }

  public List<ArincTaaSectorDetails> sectorDetails() {
    return sectorDetails == null ? List.of() : sectorDetails;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincTaa that = (ArincTaa) o;
    return Objects.equals(baseInfo, that.baseInfo) && Objects.equals(recordInfo, that.recordInfo) && Objects.equals(approachTypeIdentifier, that.approachTypeIdentifier) && Objects.equals(magneticTrueIndicator, that.magneticTrueIndicator) && Objects.equals(taaFixPositionIndicator, that.taaFixPositionIndicator) && Objects.equals(taaIafWaypointRef, that.taaIafWaypointRef) && Objects.equals(sectorBearingWaypointRef, that.sectorBearingWaypointRef) && Objects.equals(sectorDetails, that.sectorDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, recordInfo, approachTypeIdentifier, magneticTrueIndicator, taaFixPositionIndicator, taaIafWaypointRef, sectorBearingWaypointRef, sectorDetails);
  }

  @Override
  public String toString() {
    return "ArincTaa{" +
        "baseInfo=" + baseInfo +
        ", recordInfo=" + recordInfo +
        ", approachTypeIdentifier=" + approachTypeIdentifier +
        ", magneticTrueIndicator='" + magneticTrueIndicator + '\'' +
        ", taaFixPositionIndicator='" + taaFixPositionIndicator + '\'' +
        ", taaIafWaypointRef='" + taaIafWaypointRef + '\'' +
        ", sectorBearingWaypointRef='" + sectorBearingWaypointRef + '\'' +
        ", sectorDetails=" + sectorDetails +
        '}';
  }

  public static final class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private List<String> approachTypeIdentifier;
    private String magneticTrueIndicator;
    private String taaFixPositionIndicator;
    private String taaIafWaypointRef;
    private String sectorBearingWaypointRef;
    private List<ArincTaaSectorDetails> sectorDetails;

    private Builder() {
    }

    public Builder baseInfo(ArincBaseInfo baseInfo) {
      this.baseInfo = baseInfo;
      return this;
    }

    public Builder recordInfo(ArincRecordInfo recordInfo) {
      this.recordInfo = recordInfo;
      return this;
    }

    public Builder approachTypeIdentifier(List<String> approachTypeIdentifier) {
      this.approachTypeIdentifier = approachTypeIdentifier;
      return this;
    }

    public Builder magneticTrueIndicator(String magneticTrueIndicator) {
      this.magneticTrueIndicator = magneticTrueIndicator;
      return this;
    }

    public Builder taaFixPositionIndicator(String taaFixPositionIndicator) {
      this.taaFixPositionIndicator = taaFixPositionIndicator;
      return this;
    }

    public Builder taaIafWaypointRef(String taaIafWaypointRef) {
      this.taaIafWaypointRef = taaIafWaypointRef;
      return this;
    }

    public Builder sectorBearingWaypointRef(String sectorBearingWaypointRef) {
      this.sectorBearingWaypointRef = sectorBearingWaypointRef;
      return this;
    }

    public Builder sectorDetails(List<ArincTaaSectorDetails> sectorDetails) {
      this.sectorDetails = sectorDetails;
      return this;
    }

    public ArincTaa build() {
      return new ArincTaa(this);
    }
  }
}
