package org.mitre.boogie.xml.model.fields;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.*;

import com.google.common.collect.Range;

public final class ArincPortInfo {
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;
  private final ArincPointInfo pointInfo;
  private final Double elevation;
  private final String ataIataDesignator;
  private final Boolean daylightIndicator;
  private final Boolean isIfrCapable;
  private final String magneticTrueIndicator;
  private final String publicMilitaryIndicator;
  private final String recommendedNavaidId;
  private final Range<Double> speedLimit;
  private final Double speedLimitAltitude;
  private final UtcOffset utcOffset;
  private final Double transitionAltitude;
  private final List<ArincNdbNavaid> ndbNavaid;
  private final List<ArincProcedure> procedures;
  private final List<ArincWaypoint> terminalWaypoints;
  private final List<ArincTaa> taas;
  private final List<ArincAirportCommunications> communications;
  private final List<ArincHelipad> helipads;
  private final List<ArincLocalizerGlideslopeMarker> markers;
  private final List<ArincLocalizerGlideSlope> localizerGlideSlopes;
  private final List<ArincGnssLandingSystem> gnssLandingSystems;
  //skipping mls
  private final List<ArincMsa> msas;
  //skipping flight plan arr/dep record
  private final Boolean isVfrCheckpoint;
  private final String controlledAirspaceId;
  private final String controlledAirspaceIndicator;

  private ArincPortInfo(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.pointInfo = builder.pointInfo;
    this.elevation = builder.elevation;
    this.ataIataDesignator = builder.ataIataDesignator;
    this.daylightIndicator = builder.daylightIndicator;
    this.isIfrCapable = builder.isIfrCapable;
    this.magneticTrueIndicator = builder.magneticTrueIndicator;
    this.publicMilitaryIndicator = builder.publicMilitaryIndicator;
    this.recommendedNavaidId = builder.recommendedNavaidId;
    this.speedLimit = builder.speedLimit;
    this.speedLimitAltitude = builder.speedLimitAltitude;
    this.utcOffset = builder.utcOffset;
    this.transitionAltitude = builder.transitionAltitude;
    this.ndbNavaid = builder.ndbNavaid;
    this.procedures = builder.procedures;
    this.terminalWaypoints = builder.terminalWaypoints;
    this.taas = builder.taas;
    this.communications = builder.communications;
    this.helipads = builder.helipads;
    this.markers = builder.markers;
    this.localizerGlideSlopes = builder.localizerGlideSlopes;
    this.gnssLandingSystems = builder.gnssLandingSystems;
    this.msas = builder.msas;
    this.isVfrCheckpoint = builder.isVfrCheckpoint;
    this.controlledAirspaceId = builder.controlledAirspaceId;
    this.controlledAirspaceIndicator = builder.controlledAirspaceIndicator;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .baseInfo(baseInfo)
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .elevation(elevation)
        .ataIataDesignator(ataIataDesignator)
        .daylightIndicator(daylightIndicator)
        .ifrCapable(isIfrCapable)
        .magneticTrueIndicator(magneticTrueIndicator)
        .publicMilitaryIndicator(publicMilitaryIndicator)
        .recommendedNavaidId(recommendedNavaidId)
        .speedLimit(speedLimit)
        .speedLimitAltitude(speedLimitAltitude)
        .utcOffset(utcOffset)
        .transitionAltitude(transitionAltitude)
        .ndbNavaid(ndbNavaid)
        .procedures(procedures)
        .terminalWaypoints(terminalWaypoints)
        .taas(taas)
        .communications(communications)
        .helipads(helipads)
        .markers(markers)
        .localizerGlideSlopes(localizerGlideSlopes)
        .gnssLandingSystems(gnssLandingSystems)
        .msas(msas)
        .vfrCheckpoint(isVfrCheckpoint)
        .controlledAirspaceId(controlledAirspaceId)
        .controlledAirspaceIndicator(controlledAirspaceIndicator);
  }

  public ArincBaseInfo baseInfo() {
    return baseInfo;
  }

  public ArincRecordInfo recordInfo() {
    return recordInfo;
  }

  public ArincPointInfo pointInfo() {
    return pointInfo;
  }

  public Optional<Double> elevation() {
    return Optional.ofNullable(elevation);
  }

  public Optional<String> ataIataDesignator() {
    return Optional.ofNullable(ataIataDesignator);
  }

  public Optional<Boolean> daylightIndicator() {
    return Optional.ofNullable(daylightIndicator);
  }

  public Optional<Boolean> ifrCapable() {
    return Optional.ofNullable(isIfrCapable);
  }

  public Optional<MagneticTrueIndicator> magneticTrueIndicator() {
    return Optional.ofNullable(magneticTrueIndicator)
        .filter(MagneticTrueIndicator.VALID::contains)
        .map(MagneticTrueIndicator::valueOf);
  }

  public Optional<PublicMilitaryIndicator> publicMilitaryIndicator() {
    return Optional.ofNullable(publicMilitaryIndicator)
        .filter(PublicMilitaryIndicator.VALID::contains)
        .map(PublicMilitaryIndicator::valueOf);
  }

  public Optional<String> recommendedNavaidId() {
    return Optional.ofNullable(recommendedNavaidId);
  }

  public Optional<Range<Double>> speedLimit() {
    return Optional.ofNullable(speedLimit);
  }

  public Optional<Double> speedLimitAltitude() {
    return Optional.ofNullable(speedLimitAltitude);
  }

  public Optional<UtcOffset> utcOffset() {
    return Optional.ofNullable(utcOffset);
  }

  public Optional<Double> transitionAltitude() {
    return Optional.ofNullable(transitionAltitude);
  }

  public Optional<List<ArincNdbNavaid>> ndbNavaid() {
    return Optional.ofNullable(ndbNavaid);
  }

  public Optional<List<ArincProcedure>> procedures() {
    return Optional.ofNullable(procedures);
  }

  public Optional<List<ArincWaypoint>> terminalWaypoints() {
    return Optional.ofNullable(terminalWaypoints);
  }

  public Optional<List<ArincTaa>> taas() {
    return Optional.ofNullable(taas);
  }

  public Optional<List<ArincAirportCommunications>> communications() {
    return Optional.ofNullable(communications);
  }

  public Optional<List<ArincHelipad>> helipads() {
    return Optional.ofNullable(helipads);
  }

  public Optional<List<ArincLocalizerGlideslopeMarker>> markers() {
    return Optional.ofNullable(markers);
  }

  public Optional<List<ArincLocalizerGlideSlope>> localizerGlideSlopes() {
    return Optional.ofNullable(localizerGlideSlopes);
  }

  public Optional<List<ArincGnssLandingSystem>> gnssLandingSystems() {
    return Optional.ofNullable(gnssLandingSystems);
  }

  public Optional<List<ArincMsa>> msas() {
    return Optional.ofNullable(msas);
  }

  public Optional<Boolean> vfrCheckpoint() {
    return Optional.ofNullable(isVfrCheckpoint);
  }

  public Optional<String> controlledAirspaceId() {
    return Optional.ofNullable(controlledAirspaceId);
  }

  public Optional<ControlledAirspaceIndicator> controlledAirspaceIndicator() {
    return Optional.ofNullable(controlledAirspaceIndicator)
        .filter(ControlledAirspaceIndicator.VALID::contains)
        .map(ControlledAirspaceIndicator::valueOf);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    ArincPortInfo that = (ArincPortInfo) o;
    return Objects.equals(baseInfo, that.baseInfo) && Objects.equals(recordInfo, that.recordInfo) && Objects.equals(pointInfo, that.pointInfo) && Objects.equals(elevation, that.elevation) && Objects.equals(ataIataDesignator, that.ataIataDesignator) && Objects.equals(daylightIndicator, that.daylightIndicator) && Objects.equals(isIfrCapable, that.isIfrCapable) && Objects.equals(magneticTrueIndicator, that.magneticTrueIndicator) && Objects.equals(publicMilitaryIndicator, that.publicMilitaryIndicator) && Objects.equals(recommendedNavaidId, that.recommendedNavaidId) && Objects.equals(speedLimit, that.speedLimit) && Objects.equals(speedLimitAltitude, that.speedLimitAltitude) && Objects.equals(utcOffset, that.utcOffset) && Objects.equals(transitionAltitude, that.transitionAltitude) && Objects.equals(ndbNavaid, that.ndbNavaid) && Objects.equals(procedures, that.procedures) && Objects.equals(terminalWaypoints, that.terminalWaypoints) && Objects.equals(taas, that.taas) && Objects.equals(communications, that.communications) && Objects.equals(helipads, that.helipads) && Objects.equals(markers, that.markers) && Objects.equals(localizerGlideSlopes, that.localizerGlideSlopes) && Objects.equals(gnssLandingSystems, that.gnssLandingSystems) && Objects.equals(msas, that.msas) && Objects.equals(isVfrCheckpoint, that.isVfrCheckpoint) && Objects.equals(controlledAirspaceId, that.controlledAirspaceId) && Objects.equals(controlledAirspaceIndicator, that.controlledAirspaceIndicator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, recordInfo, pointInfo, elevation, ataIataDesignator, daylightIndicator, isIfrCapable, magneticTrueIndicator, publicMilitaryIndicator, recommendedNavaidId, speedLimit, speedLimitAltitude, utcOffset, transitionAltitude, ndbNavaid, procedures, terminalWaypoints, taas, communications, helipads, markers, localizerGlideSlopes, gnssLandingSystems, msas, isVfrCheckpoint, controlledAirspaceId, controlledAirspaceIndicator);
  }

  @Override
  public String toString() {
    return "ArincPortInfo{" +
        "baseInfo=" + baseInfo +
        ", recordInfo=" + recordInfo +
        ", pointInfo=" + pointInfo +
        ", elevation=" + elevation +
        ", ataIataDesignator='" + ataIataDesignator + '\'' +
        ", daylightIndicator=" + daylightIndicator +
        ", isIfrCapable=" + isIfrCapable +
        ", magneticTrueIndicator='" + magneticTrueIndicator + '\'' +
        ", publicMilitaryIndicator='" + publicMilitaryIndicator + '\'' +
        ", recommendedNavaidId='" + recommendedNavaidId + '\'' +
        ", speedLimit=" + speedLimit +
        ", speedLimitAltitude=" + speedLimitAltitude +
        ", utcOffset=" + utcOffset +
        ", transitionAltitude=" + transitionAltitude +
        ", ndbNavaid=" + ndbNavaid +
        ", procedures=" + procedures +
        ", terminalWaypoints=" + terminalWaypoints +
        ", taas=" + taas +
        ", communications=" + communications +
        ", helipads=" + helipads +
        ", markers=" + markers +
        ", localizerGlideSlopes=" + localizerGlideSlopes +
        ", gnssLandingSystems=" + gnssLandingSystems +
        ", msas=" + msas +
        ", isVfrCheckpoint=" + isVfrCheckpoint +
        ", controlledAirspaceId='" + controlledAirspaceId + '\'' +
        ", controlledAirspaceIndicator='" + controlledAirspaceIndicator + '\'' +
        '}';
  }

  public static class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private ArincPointInfo pointInfo;
    private Double elevation;
    private String ataIataDesignator;
    private Boolean daylightIndicator;
    private Boolean isIfrCapable;
    private String magneticTrueIndicator;
    private String publicMilitaryIndicator;
    private String recommendedNavaidId;
    private Range<Double> speedLimit;
    private Double speedLimitAltitude;
    private UtcOffset utcOffset;
    private Double transitionAltitude;
    private List<ArincNdbNavaid> ndbNavaid;
    private List<ArincProcedure> procedures;
    private List<ArincWaypoint> terminalWaypoints;
    private List<ArincTaa> taas;
    private List<ArincAirportCommunications> communications;
    private List<ArincHelipad> helipads;
    private List<ArincLocalizerGlideslopeMarker> markers;
    private List<ArincLocalizerGlideSlope> localizerGlideSlopes;
    private List<ArincGnssLandingSystem> gnssLandingSystems;
    //skipping mls
    private List<ArincMsa> msas;
    //skipping flight plan arr/dep record
    private Boolean isVfrCheckpoint;
    private String controlledAirspaceId;
    private String controlledAirspaceIndicator;

    private Builder() {}

    public Builder baseInfo(ArincBaseInfo baseInfo) {
      this.baseInfo = baseInfo;
      return this;
    }

    public Builder recordInfo(ArincRecordInfo recordInfo) {
      this.recordInfo = recordInfo;
      return this;
    }

    public Builder pointInfo(ArincPointInfo pointInfo) {
      this.pointInfo = pointInfo;
      return this;
    }

    public Builder elevation(Double elevation) {
      this.elevation = elevation;
      return this;
    }

    public Builder ataIataDesignator(String ataIataDesignator) {
      this.ataIataDesignator = ataIataDesignator;
      return this;
    }

    public Builder daylightIndicator(Boolean daylightIndicator) {
      this.daylightIndicator = daylightIndicator;
      return this;
    }

    public Builder ifrCapable(Boolean ifrCapable) {
      isIfrCapable = ifrCapable;
      return this;
    }

    public Builder magneticTrueIndicator(String magneticTrueIndicator) {
      this.magneticTrueIndicator = magneticTrueIndicator;
      return this;
    }

    public Builder publicMilitaryIndicator(String publicMilitaryIndicator) {
      this.publicMilitaryIndicator = publicMilitaryIndicator;
      return this;
    }

    public Builder recommendedNavaidId(String recommendedNavaidId) {
      this.recommendedNavaidId = recommendedNavaidId;
      return this;
    }

    public Builder speedLimit(Range<Double> speedLimit) {
      this.speedLimit = speedLimit;
      return this;
    }

    public Builder speedLimitAltitude(Double speedLimitAltitude) {
      this.speedLimitAltitude = speedLimitAltitude;
      return this;
    }

    public Builder utcOffset(UtcOffset utcOffset) {
      this.utcOffset = utcOffset;
      return this;
    }

    public Builder transitionAltitude(Double transitionAltitude) {
      this.transitionAltitude = transitionAltitude;
      return this;
    }

    public Builder ndbNavaid(List<ArincNdbNavaid> ndbNavaid) {
      this.ndbNavaid = ndbNavaid;
      return this;
    }

    public Builder procedures(List<ArincProcedure> procedures) {
      this.procedures = procedures;
      return this;
    }

    public Builder terminalWaypoints(List<ArincWaypoint> terminalWaypoints) {
      this.terminalWaypoints = terminalWaypoints;
      return this;
    }

    public Builder taas(List<ArincTaa> taas) {
      this.taas = taas;
      return this;
    }

    public Builder communications(List<ArincAirportCommunications> communications) {
      this.communications = communications;
      return this;
    }

    public Builder helipads(List<ArincHelipad> helipads) {
      this.helipads = helipads;
      return this;
    }

    public Builder markers(List<ArincLocalizerGlideslopeMarker> markers) {
      this.markers = markers;
      return this;
    }

    public Builder localizerGlideSlopes(List<ArincLocalizerGlideSlope> localizerGlideSlopes) {
      this.localizerGlideSlopes = localizerGlideSlopes;
      return this;
    }

    public Builder gnssLandingSystems(List<ArincGnssLandingSystem> gnssLandingSystems) {
      this.gnssLandingSystems = gnssLandingSystems;
      return this;
    }

    public Builder msas(List<ArincMsa> msas) {
      this.msas = msas;
      return this;
    }

    public Builder vfrCheckpoint(Boolean vfrCheckpoint) {
      isVfrCheckpoint = vfrCheckpoint;
      return this;
    }

    public Builder controlledAirspaceId(String controlledAirspaceId) {
      this.controlledAirspaceId = controlledAirspaceId;
      return this;
    }

    public Builder controlledAirspaceIndicator(String controlledAirspaceIndicator) {
      this.controlledAirspaceIndicator = controlledAirspaceIndicator;
      return this;
    }

    public ArincPortInfo build() {
      return new ArincPortInfo(this);
    }
  }
}
