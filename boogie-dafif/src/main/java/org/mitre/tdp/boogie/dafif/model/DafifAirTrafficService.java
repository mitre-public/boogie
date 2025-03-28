package org.mitre.tdp.boogie.dafif.model;

import org.mitre.tdp.boogie.dafif.v81.field.AtsIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteSequenceNumber;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteDirection;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteType;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.BiDirectional;
import org.mitre.tdp.boogie.dafif.v81.field.FrequencyClass;
import org.mitre.tdp.boogie.dafif.v81.field.Level;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteStatus;
import org.mitre.tdp.boogie.dafif.v81.field.NavaidType;
import org.mitre.tdp.boogie.dafif.v81.field.WaypointIdentifierWptIdent;
import org.mitre.tdp.boogie.dafif.v81.field.CountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.AtsWaypointDescriptionCode1;
import org.mitre.tdp.boogie.dafif.v81.field.AtsWaypointDescriptionCode2;
import org.mitre.tdp.boogie.dafif.v81.field.AtsWaypointDescriptionCode3;
import org.mitre.tdp.boogie.dafif.v81.field.AtsWaypointDescriptionCode4;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteOutboundMagneticCourse;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteDistance;
import org.mitre.tdp.boogie.dafif.v81.field.AtsRouteInboundMagneticCourse;
import org.mitre.tdp.boogie.dafif.v81.field.MinimumAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.UpperLimit;
import org.mitre.tdp.boogie.dafif.v81.field.LowerLimit;
import org.mitre.tdp.boogie.dafif.v81.field.MaxAuthorizedAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.CruiseLevelIndicator;
import org.mitre.tdp.boogie.dafif.v81.field.RequiredNavPerformance;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.AtsDesignator;

import java.util.Objects;
import java.util.Optional;

public final class DafifAirTrafficService {
  /**
   * {@link AtsIdentifier}
   */
  private final String atsIdentifier;
  /**
   * {@link AtsRouteSequenceNumber}
   */
  private final Integer atsRouteSequenceNumber;
  /**
   * {@link AtsRouteDirection}
   */
  private final String atsRouteDirection;
  /**
   * {@link AtsRouteType}
   */
  private final String atsRouteType;
  /**
   * {@link IcaoCode}
   */
  private final String icaoCode;
  /**
   * {@link BiDirectional}
   */
  private final String biDirectional;
  /**
   * {@link FrequencyClass}
   */
  private final String frequencyClass;
  /**
   * {@link Level}
   */
  private final String level;
  /**
   * {@link AtsRouteStatus}
   */
  private final String atsRouteStatus;
  /**
   * {@link IcaoCode}
   */
  private final String waypoint1IcaoCode;
  /**
   * {@link NavaidType}
   */
  private final Integer waypoint1NavaidType;
  /**
   * {@link WaypointIdentifierWptIdent}
   */
  private final String waypoint1WaypointIdentifierWptIdent;
  /**
   * {@link CountryCode}
   */
  private final String waypoint1CountryCode;
  /**
   * {@link AtsWaypointDescriptionCode1}
   */
  private final String waypoint1AtsWaypointDescriptionCode1;
  /**
   * {@link AtsWaypointDescriptionCode2}
   */
  private final String waypoint1AtsWaypointDescriptionCode2;
  /**
   * {@link AtsWaypointDescriptionCode3}
   */
  private final String waypoint1AtsWaypointDescriptionCode3;
  /**
   * {@link AtsWaypointDescriptionCode4}
   */
  private final String waypoint1AtsWaypointDescriptionCode4;
  /**
   * {@link GeodeticLatitude}
   */
  private final String waypoint1GeodeticLatitude;
  /**
   * {@link DegreesLatitude}
   */
  private final Double waypoint1DegreesLatitude;
  /**
   * {@link GeodeticLongitude}
   */
  private final String waypoint1GeodeticLongitude;
  /**
   * {@link DegreesLongitude}
   */
  private final Double waypoint1DegreesLongitude;
  /**
   * {@link IcaoCode}
   */
  private final String waypoint2IcaoCode;
  /**
   * {@link NavaidType}
   */
  private final Integer waypoint2NavaidType;
  /**
   * {@link WaypointIdentifierWptIdent}
   */
  private final String waypoint2WaypointIdentifierWptIdent;
  /**
   * {@link CountryCode}
   */
  private final String waypoint2CountryCode;
  /**
   * {@link AtsWaypointDescriptionCode1}
   */
  private final String waypoint2AtsWaypointDescriptionCode1;
  /**
   * {@link AtsWaypointDescriptionCode2}
   */
  private final String waypoint2AtsWaypointDescriptionCode2;
  /**
   * {@link AtsWaypointDescriptionCode3}
   */
  private final String waypoint2AtsWaypointDescriptionCode3;
  /**
   * {@link AtsWaypointDescriptionCode4}
   */
  private final String waypoint2AtsWaypointDescriptionCode4;
  /**
   * {@link GeodeticLatitude}
   */
  private final String waypoint2GeodeticLatitude;
  /**
   * {@link DegreesLatitude}
   */
  private final Double waypoint2DegreesLatitude;
  /**
   * {@link GeodeticLongitude}
   */
  private final String waypoint2GeodeticLongitude;
  /**
   * {@link DegreesLongitude}
   */
  private final Double waypoint2DegreesLongitude;
  /**
   * {@link AtsRouteOutboundMagneticCourse}
   */
  private final String atsRouteOutboundMagneticCourse;
  /**
   * {@link AtsRouteDistance}
   */
  private final Double atsRouteDistance;
  /**
   * {@link AtsRouteInboundMagneticCourse}
   */
  private final String atsRouteInboundMagneticCourse;
  /**
   * {@link MinimumAltitude}
   */
  private final String minimumAltitude;
  /**
   * {@link UpperLimit}
   */
  private final String upperLimit;
  /**
   * {@link LowerLimit}
   */
  private final String lowerLimit;
  /**
   * {@link MaxAuthorizedAltitude}
   */
  private final String maxAuthorizedAltitude;
  /**
   * {@link CruiseLevelIndicator}
   */
  private final String cruiseLevelIndicator;
  /**
   * {@link RequiredNavPerformance}
   */
  private final Integer requiredNavPerformance;
  /**
   * {@link CycleDate}
   */
  private final Integer cycleDate;
  /**
   * {@link AtsDesignator}
   */
  private final String atsDesignator;

  private DafifAirTrafficService(Builder builder) {
    this.atsIdentifier = builder.atsIdentifier;
    this.atsRouteSequenceNumber = builder.atsRouteSequenceNumber;
    this.atsRouteDirection = builder.atsRouteDirection;
    this.atsRouteType = builder.atsRouteType;
    this.icaoCode = builder.icaoCode;
    this.biDirectional = builder.biDirectional;
    this.frequencyClass = builder.frequencyClass;
    this.level = builder.level;
    this.atsRouteStatus = builder.atsRouteStatus;
    this.waypoint1IcaoCode = builder.waypoint1IcaoCode;
    this.waypoint1NavaidType = builder.waypoint1NavaidType;
    this.waypoint1WaypointIdentifierWptIdent = builder.waypoint1WaypointIdentifierWptIdent;
    this.waypoint1CountryCode = builder.waypoint1CountryCode;
    this.waypoint1AtsWaypointDescriptionCode1 = builder.waypoint1AtsWaypointDescriptionCode1;
    this.waypoint1AtsWaypointDescriptionCode2 = builder.waypoint1AtsWaypointDescriptionCode2;
    this.waypoint1AtsWaypointDescriptionCode3 = builder.waypoint1AtsWaypointDescriptionCode3;
    this.waypoint1AtsWaypointDescriptionCode4 = builder.waypoint1AtsWaypointDescriptionCode4;
    this.waypoint1GeodeticLatitude = builder.waypoint1GeodeticLatitude;
    this.waypoint1DegreesLatitude = builder.waypoint1DegreesLatitude;
    this.waypoint1GeodeticLongitude = builder.waypoint1GeodeticLongitude;
    this.waypoint1DegreesLongitude = builder.waypoint1DegreesLongitude;
    this.waypoint2IcaoCode = builder.waypoint2IcaoCode;
    this.waypoint2NavaidType = builder.waypoint2NavaidType;
    this.waypoint2WaypointIdentifierWptIdent = builder.waypoint2WaypointIdentifierWptIdent;
    this.waypoint2CountryCode = builder.waypoint2CountryCode;
    this.waypoint2AtsWaypointDescriptionCode1 = builder.waypoint2AtsWaypointDescriptionCode1;
    this.waypoint2AtsWaypointDescriptionCode2 = builder.waypoint2AtsWaypointDescriptionCode2;
    this.waypoint2AtsWaypointDescriptionCode3 = builder.waypoint2AtsWaypointDescriptionCode3;
    this.waypoint2AtsWaypointDescriptionCode4 = builder.waypoint2AtsWaypointDescriptionCode4;
    this.waypoint2GeodeticLatitude = builder.waypoint2GeodeticLatitude;
    this.waypoint2DegreesLatitude = builder.waypoint2DegreesLatitude;
    this.waypoint2GeodeticLongitude = builder.waypoint2GeodeticLongitude;
    this.waypoint2DegreesLongitude = builder.waypoint2DegreesLongitude;
    this.atsRouteOutboundMagneticCourse = builder.atsRouteOutboundMagneticCourse;
    this.atsRouteDistance = builder.atsRouteDistance;
    this.atsRouteInboundMagneticCourse = builder.atsRouteInboundMagneticCourse;
    this.minimumAltitude = builder.minimumAltitude;
    this.upperLimit = builder.upperLimit;
    this.lowerLimit = builder.lowerLimit;
    this.maxAuthorizedAltitude = builder.maxAuthorizedAltitude;
    this.cruiseLevelIndicator = builder.cruiseLevelIndicator;
    this.requiredNavPerformance = builder.requiredNavPerformance;
    this.cycleDate = builder.cycleDate;
    this.atsDesignator = builder.atsDesignator;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    DafifAirTrafficService that = (DafifAirTrafficService) o;
    return Objects.equals(atsIdentifier, that.atsIdentifier) && Objects.equals(atsRouteSequenceNumber, that.atsRouteSequenceNumber) && Objects.equals(atsRouteDirection, that.atsRouteDirection) && Objects.equals(atsRouteType, that.atsRouteType) && Objects.equals(icaoCode, that.icaoCode) && Objects.equals(biDirectional, that.biDirectional) && Objects.equals(frequencyClass, that.frequencyClass) && Objects.equals(level, that.level) && Objects.equals(atsRouteStatus, that.atsRouteStatus) && Objects.equals(waypoint1IcaoCode, that.waypoint1IcaoCode) && Objects.equals(waypoint1NavaidType, that.waypoint1NavaidType) && Objects.equals(waypoint1WaypointIdentifierWptIdent, that.waypoint1WaypointIdentifierWptIdent) && Objects.equals(waypoint1CountryCode, that.waypoint1CountryCode) && Objects.equals(waypoint1AtsWaypointDescriptionCode1, that.waypoint1AtsWaypointDescriptionCode1) && Objects.equals(waypoint1AtsWaypointDescriptionCode2, that.waypoint1AtsWaypointDescriptionCode2) && Objects.equals(waypoint1AtsWaypointDescriptionCode3, that.waypoint1AtsWaypointDescriptionCode3) && Objects.equals(waypoint1AtsWaypointDescriptionCode4, that.waypoint1AtsWaypointDescriptionCode4) && Objects.equals(waypoint1GeodeticLatitude, that.waypoint1GeodeticLatitude) && Objects.equals(waypoint1DegreesLatitude, that.waypoint1DegreesLatitude) && Objects.equals(waypoint1GeodeticLongitude, that.waypoint1GeodeticLongitude) && Objects.equals(waypoint1DegreesLongitude, that.waypoint1DegreesLongitude) && Objects.equals(waypoint2IcaoCode, that.waypoint2IcaoCode) && Objects.equals(waypoint2NavaidType, that.waypoint2NavaidType) && Objects.equals(waypoint2WaypointIdentifierWptIdent, that.waypoint2WaypointIdentifierWptIdent) && Objects.equals(waypoint2CountryCode, that.waypoint2CountryCode) && Objects.equals(waypoint2AtsWaypointDescriptionCode1, that.waypoint2AtsWaypointDescriptionCode1) && Objects.equals(waypoint2AtsWaypointDescriptionCode2, that.waypoint2AtsWaypointDescriptionCode2) && Objects.equals(waypoint2AtsWaypointDescriptionCode3, that.waypoint2AtsWaypointDescriptionCode3) && Objects.equals(waypoint2AtsWaypointDescriptionCode4, that.waypoint2AtsWaypointDescriptionCode4) && Objects.equals(waypoint2GeodeticLatitude, that.waypoint2GeodeticLatitude) && Objects.equals(waypoint2DegreesLatitude, that.waypoint2DegreesLatitude) && Objects.equals(waypoint2GeodeticLongitude, that.waypoint2GeodeticLongitude) && Objects.equals(waypoint2DegreesLongitude, that.waypoint2DegreesLongitude) && Objects.equals(atsRouteOutboundMagneticCourse, that.atsRouteOutboundMagneticCourse) && Objects.equals(atsRouteDistance, that.atsRouteDistance) && Objects.equals(atsRouteInboundMagneticCourse, that.atsRouteInboundMagneticCourse) && Objects.equals(minimumAltitude, that.minimumAltitude) && Objects.equals(upperLimit, that.upperLimit) && Objects.equals(lowerLimit, that.lowerLimit) && Objects.equals(maxAuthorizedAltitude, that.maxAuthorizedAltitude) && Objects.equals(cruiseLevelIndicator, that.cruiseLevelIndicator) && Objects.equals(requiredNavPerformance, that.requiredNavPerformance) && Objects.equals(cycleDate, that.cycleDate) && Objects.equals(atsDesignator, that.atsDesignator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(atsIdentifier, atsRouteSequenceNumber, atsRouteDirection, atsRouteType, icaoCode, biDirectional, frequencyClass, level, atsRouteStatus, waypoint1IcaoCode, waypoint1NavaidType, waypoint1WaypointIdentifierWptIdent, waypoint1CountryCode, waypoint1AtsWaypointDescriptionCode1, waypoint1AtsWaypointDescriptionCode2, waypoint1AtsWaypointDescriptionCode3, waypoint1AtsWaypointDescriptionCode4, waypoint1GeodeticLatitude, waypoint1DegreesLatitude, waypoint1GeodeticLongitude, waypoint1DegreesLongitude, waypoint2IcaoCode, waypoint2NavaidType, waypoint2WaypointIdentifierWptIdent, waypoint2CountryCode, waypoint2AtsWaypointDescriptionCode1, waypoint2AtsWaypointDescriptionCode2, waypoint2AtsWaypointDescriptionCode3, waypoint2AtsWaypointDescriptionCode4, waypoint2GeodeticLatitude, waypoint2DegreesLatitude, waypoint2GeodeticLongitude, waypoint2DegreesLongitude, atsRouteOutboundMagneticCourse, atsRouteDistance, atsRouteInboundMagneticCourse, minimumAltitude, upperLimit, lowerLimit, maxAuthorizedAltitude, cruiseLevelIndicator, requiredNavPerformance, cycleDate, atsDesignator);
  }

  public String atsIdentifier() {
    return atsIdentifier;
  }

  public Integer atsRouteSequenceNumber() {
    return atsRouteSequenceNumber;
  }

  public String atsRouteDirection() {
    return atsRouteDirection;
  }

  public String atsRouteType() {
    return atsRouteType;
  }

  public String icaoCode() {
    return icaoCode;
  }

  public String frequencyClass() {
    return frequencyClass;
  }

  public String level() {
    return level;
  }

  public String atsRouteStatus() {
    return atsRouteStatus;
  }

  public String waypoint1IcaoCode() {
    return waypoint1IcaoCode;
  }

  public String waypoint1WaypointIdentifierWptIdent() {
    return waypoint1WaypointIdentifierWptIdent;
  }

  public String waypoint1CountryCode() {
    return waypoint1CountryCode;
  }

  public String waypoint1AtsWaypointDescriptionCode1() {
    return waypoint1AtsWaypointDescriptionCode1;
  }

  public String waypoint2IcaoCode() {
    return waypoint2IcaoCode;
  }

  public String waypoint2WaypointIdentifierWptIdent() {
    return waypoint2WaypointIdentifierWptIdent;
  }

  public String waypoint2CountryCode() {
    return waypoint2CountryCode;
  }

  public String waypoint2AtsWaypointDescriptionCode1() {
    return waypoint2AtsWaypointDescriptionCode1;
  }

  public Optional<String> biDirectional() {
    return Optional.ofNullable(biDirectional);
  }

  public Optional<Integer> waypoint1NavaidType() {
    return Optional.ofNullable(waypoint1NavaidType);
  }

  public Optional<String> waypoint1AtsWaypointDescriptionCode2() {
    return Optional.ofNullable(waypoint1AtsWaypointDescriptionCode2);
  }

  public Optional<String> waypoint1AtsWaypointDescriptionCode3() {
    return Optional.ofNullable(waypoint1AtsWaypointDescriptionCode3);
  }

  public Optional<String> waypoint1AtsWaypointDescriptionCode4() {
    return Optional.ofNullable(waypoint1AtsWaypointDescriptionCode4);
  }

  public Optional<String> waypoint1GeodeticLatitude() {
    return Optional.ofNullable(waypoint1GeodeticLatitude);
  }

  public Optional<Double> waypoint1DegreesLatitude() {
    return Optional.ofNullable(waypoint1DegreesLatitude);
  }

  public Optional<String> waypoint1GeodeticLongitude() {
    return Optional.ofNullable(waypoint1GeodeticLongitude);
  }

  public Optional<Double> waypoint1DegreesLongitude() {
    return Optional.ofNullable(waypoint1DegreesLongitude);
  }

  public Optional<Integer> waypoint2NavaidType() {
    return Optional.ofNullable(waypoint2NavaidType);
  }

  public Optional<String> waypoint2AtsWaypointDescriptionCode2() {
    return Optional.ofNullable(waypoint2AtsWaypointDescriptionCode2);
  }

  public Optional<String> waypoint2AtsWaypointDescriptionCode3() {
    return Optional.ofNullable(waypoint2AtsWaypointDescriptionCode3);
  }

  public Optional<String> waypoint2AtsWaypointDescriptionCode4() {
    return Optional.ofNullable(waypoint2AtsWaypointDescriptionCode4);
  }

  public Optional<String> waypoint2GeodeticLatitude() {
    return Optional.ofNullable(waypoint2GeodeticLatitude);
  }

  public Optional<Double> waypoint2DegreesLatitude() {
    return Optional.ofNullable(waypoint2DegreesLatitude);
  }

  public Optional<String> waypoint2GeodeticLongitude() {
    return Optional.ofNullable(waypoint2GeodeticLongitude);
  }

  public Optional<Double> waypoint2DegreesLongitude() {
    return Optional.ofNullable(waypoint2DegreesLongitude);
  }

  public Optional<String> atsRouteOutboundMagneticCourse() {
    return Optional.ofNullable(atsRouteOutboundMagneticCourse);
  }

  public Optional<Double> atsRouteDistance() {
    return Optional.ofNullable(atsRouteDistance);
  }

  public Optional<String> atsRouteInboundMagneticCourse() {
    return Optional.ofNullable(atsRouteInboundMagneticCourse);
  }

  public Optional<String> minimumAltitude() {
    return Optional.ofNullable(minimumAltitude);
  }

  public Optional<String> upperLimit() {
    return Optional.ofNullable(upperLimit);
  }

  public Optional<String> lowerLimit() {
    return Optional.ofNullable(lowerLimit);
  }

  public Optional<String> maxAuthorizedAltitude() {
    return Optional.ofNullable(maxAuthorizedAltitude);
  }

  public Optional<String> cruiseLevelIndicator() {
    return Optional.ofNullable(cruiseLevelIndicator);
  }

  public Optional<Integer> requiredNavPerformance() {
    return Optional.ofNullable(requiredNavPerformance);
  }

  public Optional<Integer> cycleDate() {
    return Optional.ofNullable(cycleDate);
  }

  public Optional<String> atsDesignator() {
    return Optional.ofNullable(atsDesignator);
  }

  public static final class Builder {
    private String atsIdentifier;
    private Integer atsRouteSequenceNumber;
    private String atsRouteDirection;
    private String atsRouteType;
    private String icaoCode;
    private String biDirectional;
    private String frequencyClass;
    private String level;
    private String atsRouteStatus;
    private String waypoint1IcaoCode;
    private Integer waypoint1NavaidType;
    private String waypoint1WaypointIdentifierWptIdent;
    private String waypoint1CountryCode;
    private String waypoint1AtsWaypointDescriptionCode1;
    private String waypoint1AtsWaypointDescriptionCode2;
    private String waypoint1AtsWaypointDescriptionCode3;
    private String waypoint1AtsWaypointDescriptionCode4;
    private String waypoint1GeodeticLatitude;
    private Double waypoint1DegreesLatitude;
    private String waypoint1GeodeticLongitude;
    private Double waypoint1DegreesLongitude;
    private String waypoint2IcaoCode;
    private Integer waypoint2NavaidType;
    private String waypoint2WaypointIdentifierWptIdent;
    private String waypoint2CountryCode;
    private String waypoint2AtsWaypointDescriptionCode1;
    private String waypoint2AtsWaypointDescriptionCode2;
    private String waypoint2AtsWaypointDescriptionCode3;
    private String waypoint2AtsWaypointDescriptionCode4;
    private String waypoint2GeodeticLatitude;
    private Double waypoint2DegreesLatitude;
    private String waypoint2GeodeticLongitude;
    private Double waypoint2DegreesLongitude;
    private String atsRouteOutboundMagneticCourse;
    private Double atsRouteDistance;
    private String atsRouteInboundMagneticCourse;
    private String minimumAltitude;
    private String upperLimit;
    private String lowerLimit;
    private String maxAuthorizedAltitude;
    private String cruiseLevelIndicator;
    private Integer requiredNavPerformance;
    private Integer cycleDate;
    private String atsDesignator;

    public Builder atsIdentifier(String atsIdentifier) {
      this.atsIdentifier = atsIdentifier;
      return this;
    }

    public Builder atsRouteSequenceNumber(Integer atsRouteSequenceNumber) {
      this.atsRouteSequenceNumber = atsRouteSequenceNumber;
      return this;
    }

    public Builder atsRouteDirection(String atsRouteDirection) {
      this.atsRouteDirection = atsRouteDirection;
      return this;
    }

    public Builder atsRouteType(String atsRouteType) {
      this.atsRouteType = atsRouteType;
      return this;
    }

    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder biDirectional(String biDirectional) {
      this.biDirectional = biDirectional;
      return this;
    }

    public Builder frequencyClass(String frequencyClass) {
      this.frequencyClass = frequencyClass;
      return this;
    }

    public Builder level(String level) {
      this.level = level;
      return this;
    }

    public Builder atsRouteStatus(String atsRouteStatus) {
      this.atsRouteStatus = atsRouteStatus;
      return this;
    }

    public Builder waypoint1IcaoCode(String waypoint1IcaoCode) {
      this.waypoint1IcaoCode = waypoint1IcaoCode;
      return this;
    }

    public Builder waypoint1NavaidType(Integer waypoint1NavaidType) {
      this.waypoint1NavaidType = waypoint1NavaidType;
      return this;
    }

    public Builder waypoint1WaypointIdentifierWptIdent(String waypoint1WaypointIdentifierWptIdent) {
      this.waypoint1WaypointIdentifierWptIdent = waypoint1WaypointIdentifierWptIdent;
      return this;
    }

    public Builder waypoint1CountryCode(String waypoint1CountryCode) {
      this.waypoint1CountryCode = waypoint1CountryCode;
      return this;
    }

    public Builder waypoint1AtsWaypointDescriptionCode1(String waypoint1AtsWaypointDescriptionCode1) {
      this.waypoint1AtsWaypointDescriptionCode1 = waypoint1AtsWaypointDescriptionCode1;
      return this;
    }

    public Builder waypoint1AtsWaypointDescriptionCode2(String waypoint1AtsWaypointDescriptionCode2) {
      this.waypoint1AtsWaypointDescriptionCode2 = waypoint1AtsWaypointDescriptionCode2;
      return this;
    }

    public Builder waypoint1AtsWaypointDescriptionCode3(String waypoint1AtsWaypointDescriptionCode3) {
      this.waypoint1AtsWaypointDescriptionCode3 = waypoint1AtsWaypointDescriptionCode3;
      return this;
    }

    public Builder waypoint1AtsWaypointDescriptionCode4(String waypoint1AtsWaypointDescriptionCode4) {
      this.waypoint1AtsWaypointDescriptionCode4 = waypoint1AtsWaypointDescriptionCode4;
      return this;
    }

    public Builder waypoint1GeodeticLatitude(String waypoint1GeodeticLatitude) {
      this.waypoint1GeodeticLatitude = waypoint1GeodeticLatitude;
      return this;
    }

    public Builder waypoint1DegreesLatitude(Double waypoint1DegreesLatitude) {
      this.waypoint1DegreesLatitude = waypoint1DegreesLatitude;
      return this;
    }

    public Builder waypoint1GeodeticLongitude(String waypoint1GeodeticLongitude) {
      this.waypoint1GeodeticLongitude = waypoint1GeodeticLongitude;
      return this;
    }

    public Builder waypoint1DegreesLongitude(Double waypoint1DegreesLongitude) {
      this.waypoint1DegreesLongitude = waypoint1DegreesLongitude;
      return this;
    }

    public Builder waypoint2IcaoCode(String waypoint2IcaoCode) {
      this.waypoint2IcaoCode = waypoint2IcaoCode;
      return this;
    }

    public Builder waypoint2NavaidType(Integer waypoint2NavaidType) {
      this.waypoint2NavaidType = waypoint2NavaidType;
      return this;
    }

    public Builder waypoint2WaypointIdentifierWptIdent(String waypoint2WaypointIdentifierWptIdent) {
      this.waypoint2WaypointIdentifierWptIdent = waypoint2WaypointIdentifierWptIdent;
      return this;
    }

    public Builder waypoint2CountryCode(String waypoint2CountryCode) {
      this.waypoint2CountryCode = waypoint2CountryCode;
      return this;
    }

    public Builder waypoint2AtsWaypointDescriptionCode1(String waypoint2AtsWaypointDescriptionCode1) {
      this.waypoint2AtsWaypointDescriptionCode1 = waypoint2AtsWaypointDescriptionCode1;
      return this;
    }

    public Builder waypoint2AtsWaypointDescriptionCode2(String waypoint2AtsWaypointDescriptionCode2) {
      this.waypoint2AtsWaypointDescriptionCode2 = waypoint2AtsWaypointDescriptionCode2;
      return this;
    }

    public Builder waypoint2AtsWaypointDescriptionCode3(String waypoint2AtsWaypointDescriptionCode3) {
      this.waypoint2AtsWaypointDescriptionCode3 = waypoint2AtsWaypointDescriptionCode3;
      return this;
    }

    public Builder waypoint2AtsWaypointDescriptionCode4(String waypoint2AtsWaypointDescriptionCode4) {
      this.waypoint2AtsWaypointDescriptionCode4 = waypoint2AtsWaypointDescriptionCode4;
      return this;
    }

    public Builder waypoint2GeodeticLatitude(String waypoint2GeodeticLatitude) {
      this.waypoint2GeodeticLatitude = waypoint2GeodeticLatitude;
      return this;
    }

    public Builder waypoint2DegreesLatitude(Double waypoint2DegreesLatitude) {
      this.waypoint2DegreesLatitude = waypoint2DegreesLatitude;
      return this;
    }

    public Builder waypoint2GeodeticLongitude(String waypoint2GeodeticLongitude) {
      this.waypoint2GeodeticLongitude = waypoint2GeodeticLongitude;
      return this;
    }

    public Builder waypoint2DegreesLongitude(Double waypoint2DegreesLongitude) {
      this.waypoint2DegreesLongitude = waypoint2DegreesLongitude;
      return this;
    }

    public Builder atsRouteOutboundMagneticCourse(String atsRouteOutboundMagneticCourse) {
      this.atsRouteOutboundMagneticCourse = atsRouteOutboundMagneticCourse;
      return this;
    }

    public Builder atsRouteDistance(Double atsRouteDistance) {
      this.atsRouteDistance = atsRouteDistance;
      return this;
    }

    public Builder atsRouteInboundMagneticCourse(String atsRouteInboundMagneticCourse) {
      this.atsRouteInboundMagneticCourse = atsRouteInboundMagneticCourse;
      return this;
    }

    public Builder minimumAltitude(String minimumAltitude) {
      this.minimumAltitude = minimumAltitude;
      return this;
    }

    public Builder upperLimit(String upperLimit) {
      this.upperLimit = upperLimit;
      return this;
    }

    public Builder lowerLimit(String lowerLimit) {
      this.lowerLimit = lowerLimit;
      return this;
    }

    public Builder maxAuthorizedAltitude(String maxAuthorizedAltitude) {
      this.maxAuthorizedAltitude = maxAuthorizedAltitude;
      return this;
    }

    public Builder cruiseLevelIndicator(String cruiseLevelIndicator) {
      this.cruiseLevelIndicator = cruiseLevelIndicator;
      return this;
    }

    public Builder requiredNavPerformance(Integer requiredNavPerformance) {
      this.requiredNavPerformance = requiredNavPerformance;
      return this;
    }

    public Builder cycleDate(Integer cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }

    public Builder atsDesignator(String atsDesignator) {
      this.atsDesignator = atsDesignator;
      return this;
    }

    public DafifAirTrafficService build() {
      return new DafifAirTrafficService(this);
    }
  }
}
