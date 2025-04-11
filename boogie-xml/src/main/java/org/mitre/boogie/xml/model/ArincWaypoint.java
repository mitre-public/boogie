package org.mitre.boogie.xml.model;

import java.io.Serializable;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincFraInfo;
import org.mitre.boogie.xml.model.fields.ArincNameFormatIndicator;
import org.mitre.boogie.xml.model.fields.ArincWaypointType;
import org.mitre.boogie.xml.model.fields.ArincWaypointUsage;
import org.mitre.boogie.xml.model.infos.ArincPointInfo;
import org.mitre.boogie.xml.model.infos.ArincRecordInfo;

public final class ArincWaypoint implements Serializable {
  private final ArincRecordInfo recordInfo;
  private final ArincPointInfo pointInfo;
  private final ArincNameFormatIndicator nameFormatIndicator;
  private final ArincWaypointType waypointType;
  private final ArincWaypointUsage waypointUsage;
  private final ArincFraInfo fraInfo;
  private final boolean isVfrCheckPoint;

  private ArincWaypoint(Builder builder) {
    this.recordInfo = builder.recordInfo;
    this.pointInfo = builder.pointInfo;
    this.nameFormatIndicator = builder.nameFormatIndicator;
    this.waypointType = builder.waypointType;
    this.waypointUsage = builder.waypointUsage;
    this.fraInfo = builder.fraInfo;
    this.isVfrCheckPoint = builder.isVfrCheckPoint;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .recordInfo(recordInfo)
        .pointInfo(pointInfo)
        .nameFormatIndicator(nameFormatIndicator)
        .waypointType(waypointType)
        .waypointUsage(waypointUsage)
        .fraInfo(fraInfo)
        .vfrCheckPoint(isVfrCheckPoint);
  }

  public ArincRecordInfo recordInfo() {
    return recordInfo;
  }

  public ArincPointInfo pointInfo() {
    return pointInfo;
  }

  public ArincWaypointType waypointType() {
    return waypointType;
  }

  public ArincWaypointUsage waypointUsage() {
    return waypointUsage;
  }

  public boolean isVfrCheckPoint() {
    return isVfrCheckPoint;
  }

  public Optional<ArincNameFormatIndicator> nameFormatIndicator() {
    return Optional.ofNullable(nameFormatIndicator);
  }

  public Optional<ArincFraInfo> fraInfo() {
    return Optional.ofNullable(fraInfo);
  }

  public static class Builder {
    private ArincRecordInfo recordInfo;
    private ArincPointInfo pointInfo;
    private ArincNameFormatIndicator nameFormatIndicator;
    private ArincWaypointType waypointType;
    private ArincWaypointUsage waypointUsage;
    private ArincFraInfo fraInfo;
    private boolean isVfrCheckPoint = false;
    private Builder() {}

    public Builder recordInfo(ArincRecordInfo recordInfo) {
      this.recordInfo = recordInfo;
      return this;
    }

    public Builder pointInfo(ArincPointInfo pointInfo) {
      this.pointInfo = pointInfo;
      return this;
    }

    public Builder nameFormatIndicator(ArincNameFormatIndicator nameFormatIndicator) {
      this.nameFormatIndicator = nameFormatIndicator;
      return this;
    }

    public Builder waypointType(ArincWaypointType waypointType) {
      this.waypointType = waypointType;
      return this;
    }

    public Builder waypointUsage(ArincWaypointUsage waypointUsage) {
      this.waypointUsage = waypointUsage;
      return this;
    }

    public Builder fraInfo(ArincFraInfo fraInfo) {
      this.fraInfo = fraInfo;
      return this;
    }

    public Builder vfrCheckPoint(boolean vfrCheckPoint) {
      isVfrCheckPoint = vfrCheckPoint;
      return this;
    }

    public ArincWaypoint build() {
      return new ArincWaypoint(this);
    }
  }

}
