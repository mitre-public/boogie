package org.mitre.tdp.boogie.dafif.model;

import org.mitre.tdp.boogie.dafif.v81.field.AirportIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalProcedureType;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalSequenceNumber;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalApproachType;
import org.mitre.tdp.boogie.dafif.v81.field.TransitionIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.TrackDescriptionCode;
import org.mitre.tdp.boogie.dafif.v81.field.TermSegWaypointIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.CountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalWaypointDescriptionCode1Arpt;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalWaypointDescriptionCode2;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalWaypointDescriptionCode3;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalWaypointDescriptionCode4;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalSegmentTurnDirection;
import org.mitre.tdp.boogie.dafif.v81.field.Navaid12Identifier;
import org.mitre.tdp.boogie.dafif.v81.field.Navaid12Type;
import org.mitre.tdp.boogie.dafif.v81.field.Navaid12KeyCode;
import org.mitre.tdp.boogie.dafif.v81.field.Fix12Bearing;
import org.mitre.tdp.boogie.dafif.v81.field.Fix12Distance;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalMagneticCourse;
import org.mitre.tdp.boogie.dafif.v81.field.Distance;
import org.mitre.tdp.boogie.dafif.v81.field.AltitudeDescription;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalSegmentAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.RequiredNavPerformance;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.GeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.DegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalWaypointMagneticVariation;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalNavaidSlaveVariation;
import org.mitre.tdp.boogie.dafif.v81.field.Nav12DmeGeodeticLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.Nav12DmeDegreesLatitude;
import org.mitre.tdp.boogie.dafif.v81.field.Nav12DmeGeodeticLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.Nav12DmeDegreesLongitude;
import org.mitre.tdp.boogie.dafif.v81.field.SpeedLimit;
import org.mitre.tdp.boogie.dafif.v81.field.SpeedLimitAircraftType;
import org.mitre.tdp.boogie.dafif.v81.field.SpeedLimitAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.VerticalNavigationVnav;
import org.mitre.tdp.boogie.dafif.v81.field.ThresholdCrossingHeight;
import org.mitre.tdp.boogie.dafif.v81.field.ArcWaypointIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.ArcWaypointCountryCode;
import org.mitre.tdp.boogie.dafif.v81.field.ArcRadius;

import java.util.Objects;
import java.util.Optional;


public final class DafifTerminalSegment implements DafifModel {
  /**
   * {@link AirportIdentification}
   */
  private final String airportIdentification;
  /**
   * {@link TerminalProcedureType}
   */
  private final Integer terminalProcedureType;
  /**
   * {@link TerminalIdentifier}
   */
  private final String terminalIdentifier;
  /**
   * {@link TerminalSequenceNumber}
   */
  private final Integer terminalSequenceNumber;
  /**
   * {@link TerminalApproachType}
   */
  private final String terminalApproachType;
  /**
   * {@link TransitionIdentifier}
   */
  private final String transitionIdentifier;
  /**
   * {@link IcaoCode}
   */
  private final String icaoCode;
  /**
   * {@link TrackDescriptionCode}
   */
  private final String trackDescriptionCode;
  /**
   * {@link TermSegWaypointIdentifier}
   */
  private final String termSegWaypointIdentifier;
  /**
   * {@link CountryCode}
   */
  private final String waypointCountryCode;
  /**
   * {@link TerminalWaypointDescriptionCode1Arpt}
   */
  private final String terminalWaypointDescriptionCode1Arpt;
  /**
   * {@link TerminalWaypointDescriptionCode2}
   */
  private final String terminalWaypointDescriptionCode2;
  /**
   * {@link TerminalWaypointDescriptionCode3}
   */
  private final String terminalWaypointDescriptionCode3;
  /**
   * {@link TerminalWaypointDescriptionCode4}
   */
  private final String terminalWaypointDescriptionCode4;
  /**
   * {@link TerminalSegmentTurnDirection}
   */
  private final String terminalSegmentTurnDirection;
  /**
   * {@link Navaid12Identifier}
   */
  private final String navaid1Identifier;
  /**
   * {@link Navaid12Type}
   */
  private final String navaid1Type;
  /**
   * {@link CountryCode}
   */
  private final String navaid1CountryCode;
  /**
   * {@link Navaid12KeyCode}
   */
  private final Integer navaid1KeyCode;
  /**
   * {@link Fix12Bearing}
   */
  private final Double fix1Bearing;
  /**
   * {@link Fix12Distance}
   */
  private final Double fix1Distance;
  /**
   * {@link Navaid12Identifier}
   */
  private final String navaid2Identifier;
  /**
   * {@link Navaid12Type}
   */
  private final String navaid2Type;
  /**
   * {@link CountryCode}
   */
  private final String navaid2CountryCode;
  /**
   * {@link Navaid12KeyCode}
   */
  private final Integer navaid2KeyCode;
  /**
   * {@link Fix12Bearing}
   */
  private final Double fix2Bearing;
  /**
   * {@link Fix12Distance}
   */
  private final Double fix2Distance;
  /**
   * {@link TerminalMagneticCourse}
   */
  private final String terminalMagneticCourse;
  /**
   * {@link Distance}
   */
  private final Double distance;
  /**
   * {@link AltitudeDescription}
   */
  private final String altitudeDescription;
  /**
   * {@link TerminalSegmentAltitude}
   */
  private final String altitude1;
  /**
   * {@link TerminalSegmentAltitude}
   */
  private final String altitude2;
  /**
   * {@link RequiredNavPerformance}
   */
  private final Double requiredNavPerformance;
  /**
   * {@link CycleDate}
   */
  private final Integer cycleDate;
  /**
   * {@link GeodeticLatitude}
   */
  private final String waypointGeodeticLatitude;
  /**
   * {@link DegreesLatitude}
   */
  private final Double waypointDegreesLatitude;
  /**
   * {@link GeodeticLongitude}
   */
  private final String waypointGeodeticLongitude;
  /**
   * {@link DegreesLongitude}
   */
  private final Double waypointDegreesLongitude;
  /**
   * {@link TerminalWaypointMagneticVariation}
   */
  private final Double waypointMagneticVariation;
  /**
   * {@link GeodeticLatitude}
   */
  private final String navaid1GeodeticLatitude;
  /**
   * {@link DegreesLatitude}
   */
  private final Double navaid1DegreesLatitude;
  /**
   * {@link GeodeticLongitude}
   */
  private final String navaid1GeodeticLongitude;
  /**
   * {@link DegreesLongitude}
   */
  private final Double navaid1DegreesLongitude;
  /**
   * {@link TerminalNavaidSlaveVariation}
   */
  private final Double navaid1MagneticVariation;
  /**
   * {@link Nav12DmeGeodeticLatitude}
   */
  private final String navaid1DmeGeodeticLatitude;
  /**
   * {@link Nav12DmeDegreesLatitude}
   */
  private final Double navaid1DmeDegreesLatitude;
  /**
   * {@link Nav12DmeGeodeticLongitude}
   */
  private final String navaid1DmeGeodeticLongitude;
  /**
   * {@link Nav12DmeDegreesLongitude}
   */
  private final Double navaid1DmeDegreesLongitude;
  /**
   * {@link GeodeticLatitude}
   */
  private final String navaid2GeodeticLatitude;
  /**
   * {@link DegreesLatitude}
   */
  private final Double navaid2DegreesLatitude;
  /**
   * {@link GeodeticLongitude}
   */
  private final String navaid2GeodeticLongitude;
  /**
   * {@link DegreesLongitude}
   */
  private final Double navaid2DegreesLongitude;
  /**
   * {@link TerminalNavaidSlaveVariation}
   */
  private final Double navaid2MagneticVariation;
  /**
   * {@link Nav12DmeGeodeticLatitude}
   */
  private final String navaid2DmeGeodeticLatitude;
  /**
   * {@link Nav12DmeDegreesLatitude}
   */
  private final Double navaid2DmeDegreesLatitude;
  /**
   * {@link Nav12DmeGeodeticLongitude}
   */
  private final String navaid2DmeGeodeticLongitude;
  /**
   * {@link Nav12DmeDegreesLongitude}
   */
  private final Double navaid2DmeDegreesLongitude;
  /**
   * {@link SpeedLimit}
   */
  private final Double speedLimit1;
  /**
   * {@link SpeedLimitAircraftType}
   */
  private final String speedLimitAircraftType1;
  /**
   * {@link SpeedLimitAltitude}
   */
  private final String speedLimitAltitude1;
  /**
   * {@link SpeedLimit}
   */
  private final Double speedLimit2;
  /**
   * {@link SpeedLimitAircraftType}
   */
  private final String speedLimitAircraftType2;
  /**
   * {@link SpeedLimitAltitude}
   */
  private final String speedLimitAltitude2;
  /**
   * {@link VerticalNavigationVnav}
   */
  private final Double verticalNavigationVnav;
  /**
   * {@link ThresholdCrossingHeight}
   */
  private final Integer thresholdCrossingHeight;
  /**
   * {@link ArcWaypointIdentifier}
   */
  private final String arcWaypointIdentifier;
  /**
   * {@link ArcWaypointCountryCode}
   */
  private final String arcWaypointCountryCode;
  /**
   * {@link ArcRadius}
   */
  private final Double arcRadius;

  private DafifTerminalSegment(Builder builder) {
    this.airportIdentification = builder.airportIdentification;
    this.terminalProcedureType = builder.terminalProcedureType;
    this.terminalIdentifier = builder.terminalIdentifier;
    this.terminalSequenceNumber = builder.terminalSequenceNumber;
    this.terminalApproachType = builder.terminalApproachType;
    this.transitionIdentifier = builder.transitionIdentifier;
    this.icaoCode = builder.icaoCode;
    this.trackDescriptionCode = builder.trackDescriptionCode;
    this.termSegWaypointIdentifier = builder.termSegWaypointIdentifier;
    this.waypointCountryCode = builder.waypointCountryCode;
    this.terminalWaypointDescriptionCode1Arpt = builder.terminalWaypointDescriptionCode1Arpt;
    this.terminalWaypointDescriptionCode2 = builder.terminalWaypointDescriptionCode2;
    this.terminalWaypointDescriptionCode3 = builder.terminalWaypointDescriptionCode3;
    this.terminalWaypointDescriptionCode4 = builder.terminalWaypointDescriptionCode4;
    this.terminalSegmentTurnDirection = builder.terminalSegmentTurnDirection;
    this.navaid1Identifier = builder.navaid1Identifier;
    this.navaid1Type = builder.navaid1Type;
    this.navaid1CountryCode = builder.navaid1CountryCode;
    this.navaid1KeyCode = builder.navaid1KeyCode;
    this.fix1Bearing = builder.fix1Bearing;
    this.fix1Distance = builder.fix1Distance;
    this.navaid2Identifier = builder.navaid2Identifier;
    this.navaid2Type = builder.navaid2Type;
    this.navaid2CountryCode = builder.navaid2CountryCode;
    this.navaid2KeyCode = builder.navaid2KeyCode;
    this.fix2Bearing = builder.fix2Bearing;
    this.fix2Distance = builder.fix2Distance;
    this.terminalMagneticCourse = builder.terminalMagneticCourse;
    this.distance = builder.distance;
    this.altitudeDescription = builder.altitudeDescription;
    this.altitude1 = builder.altitude1;
    this.altitude2 = builder.altitude2;
    this.requiredNavPerformance = builder.requiredNavPerformance;
    this.cycleDate = builder.cycleDate;
    this.waypointGeodeticLatitude = builder.waypointGeodeticLatitude;
    this.waypointDegreesLatitude = builder.waypointDegreesLatitude;
    this.waypointGeodeticLongitude = builder.waypointGeodeticLongitude;
    this.waypointDegreesLongitude = builder.waypointDegreesLongitude;
    this.waypointMagneticVariation = builder.waypointMagneticVariation;
    this.navaid1GeodeticLatitude = builder.navaid1GeodeticLatitude;
    this.navaid1DegreesLatitude = builder.navaid1DegreesLatitude;
    this.navaid1GeodeticLongitude = builder.navaid1GeodeticLongitude;
    this.navaid1DegreesLongitude = builder.navaid1DegreesLongitude;
    this.navaid1MagneticVariation = builder.navaid1MagneticVariation;
    this.navaid1DmeGeodeticLatitude = builder.navaid1DmeGeodeticLatitude;
    this.navaid1DmeDegreesLatitude = builder.navaid1DmeDegreesLatitude;
    this.navaid1DmeGeodeticLongitude = builder.navaid1DmeGeodeticLongitude;
    this.navaid1DmeDegreesLongitude = builder.navaid1DmeDegreesLongitude;
    this.navaid2GeodeticLatitude = builder.navaid2GeodeticLatitude;
    this.navaid2DegreesLatitude = builder.navaid2DegreesLatitude;
    this.navaid2GeodeticLongitude = builder.navaid2GeodeticLongitude;
    this.navaid2DegreesLongitude = builder.navaid2DegreesLongitude;
    this.navaid2MagneticVariation = builder.navaid2MagneticVariation;
    this.navaid2DmeGeodeticLatitude = builder.navaid2DmeGeodeticLatitude;
    this.navaid2DmeDegreesLatitude = builder.navaid2DmeDegreesLatitude;
    this.navaid2DmeGeodeticLongitude = builder.navaid2DmeGeodeticLongitude;
    this.navaid2DmeDegreesLongitude = builder.navaid2DmeDegreesLongitude;
    this.speedLimit1 = builder.speedLimit1;
    this.speedLimitAircraftType1 = builder.speedLimitAircraftType1;
    this.speedLimitAltitude1 = builder.speedLimitAltitude1;
    this.speedLimit2 = builder.speedLimit2;
    this.speedLimitAircraftType2 = builder.speedLimitAircraftType2;
    this.speedLimitAltitude2 = builder.speedLimitAltitude2;
    this.verticalNavigationVnav = builder.verticalNavigationVnav;
    this.thresholdCrossingHeight = builder.thresholdCrossingHeight;
    this.arcWaypointIdentifier = builder.arcWaypointIdentifier;
    this.arcWaypointCountryCode = builder.arcWaypointCountryCode;
    this.arcRadius = builder.arcRadius;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    DafifTerminalSegment that = (DafifTerminalSegment) o;
    return Objects.equals(airportIdentification, that.airportIdentification) && Objects.equals(terminalProcedureType, that.terminalProcedureType) && Objects.equals(terminalIdentifier, that.terminalIdentifier) && Objects.equals(terminalSequenceNumber, that.terminalSequenceNumber) && Objects.equals(terminalApproachType, that.terminalApproachType) && Objects.equals(transitionIdentifier, that.transitionIdentifier) && Objects.equals(icaoCode, that.icaoCode) && Objects.equals(trackDescriptionCode, that.trackDescriptionCode) && Objects.equals(termSegWaypointIdentifier, that.termSegWaypointIdentifier) && Objects.equals(waypointCountryCode, that.waypointCountryCode) && Objects.equals(terminalWaypointDescriptionCode1Arpt, that.terminalWaypointDescriptionCode1Arpt) && Objects.equals(terminalWaypointDescriptionCode2, that.terminalWaypointDescriptionCode2) && Objects.equals(terminalWaypointDescriptionCode3, that.terminalWaypointDescriptionCode3) && Objects.equals(terminalWaypointDescriptionCode4, that.terminalWaypointDescriptionCode4) && Objects.equals(terminalSegmentTurnDirection, that.terminalSegmentTurnDirection) && Objects.equals(navaid1Identifier, that.navaid1Identifier) && Objects.equals(navaid1Type, that.navaid1Type) && Objects.equals(navaid1CountryCode, that.navaid1CountryCode) && Objects.equals(navaid1KeyCode, that.navaid1KeyCode) && Objects.equals(fix1Bearing, that.fix1Bearing) && Objects.equals(fix1Distance, that.fix1Distance) && Objects.equals(navaid2Identifier, that.navaid2Identifier) && Objects.equals(navaid2Type, that.navaid2Type) && Objects.equals(navaid2CountryCode, that.navaid2CountryCode) && Objects.equals(navaid2KeyCode, that.navaid2KeyCode) && Objects.equals(fix2Bearing, that.fix2Bearing) && Objects.equals(fix2Distance, that.fix2Distance) && Objects.equals(terminalMagneticCourse, that.terminalMagneticCourse) && Objects.equals(distance, that.distance) && Objects.equals(altitudeDescription, that.altitudeDescription) && Objects.equals(altitude1, that.altitude1) && Objects.equals(altitude2, that.altitude2) && Objects.equals(requiredNavPerformance, that.requiredNavPerformance) && Objects.equals(cycleDate, that.cycleDate) && Objects.equals(waypointGeodeticLatitude, that.waypointGeodeticLatitude) && Objects.equals(waypointDegreesLatitude, that.waypointDegreesLatitude) && Objects.equals(waypointGeodeticLongitude, that.waypointGeodeticLongitude) && Objects.equals(waypointDegreesLongitude, that.waypointDegreesLongitude) && Objects.equals(waypointMagneticVariation, that.waypointMagneticVariation) && Objects.equals(navaid1GeodeticLatitude, that.navaid1GeodeticLatitude) && Objects.equals(navaid1DegreesLatitude, that.navaid1DegreesLatitude) && Objects.equals(navaid1GeodeticLongitude, that.navaid1GeodeticLongitude) && Objects.equals(navaid1DegreesLongitude, that.navaid1DegreesLongitude) && Objects.equals(navaid1MagneticVariation, that.navaid1MagneticVariation) && Objects.equals(navaid1DmeGeodeticLatitude, that.navaid1DmeGeodeticLatitude) && Objects.equals(navaid1DmeDegreesLatitude, that.navaid1DmeDegreesLatitude) && Objects.equals(navaid1DmeGeodeticLongitude, that.navaid1DmeGeodeticLongitude) && Objects.equals(navaid1DmeDegreesLongitude, that.navaid1DmeDegreesLongitude) && Objects.equals(navaid2GeodeticLatitude, that.navaid2GeodeticLatitude) && Objects.equals(navaid2DegreesLatitude, that.navaid2DegreesLatitude) && Objects.equals(navaid2GeodeticLongitude, that.navaid2GeodeticLongitude) && Objects.equals(navaid2DegreesLongitude, that.navaid2DegreesLongitude) && Objects.equals(navaid2MagneticVariation, that.navaid2MagneticVariation) && Objects.equals(navaid2DmeGeodeticLatitude, that.navaid2DmeGeodeticLatitude) && Objects.equals(navaid2DmeDegreesLatitude, that.navaid2DmeDegreesLatitude) && Objects.equals(navaid2DmeGeodeticLongitude, that.navaid2DmeGeodeticLongitude) && Objects.equals(navaid2DmeDegreesLongitude, that.navaid2DmeDegreesLongitude) && Objects.equals(speedLimit1, that.speedLimit1) && Objects.equals(speedLimitAircraftType1, that.speedLimitAircraftType1) && Objects.equals(speedLimitAltitude1, that.speedLimitAltitude1) && Objects.equals(speedLimit2, that.speedLimit2) && Objects.equals(speedLimitAircraftType2, that.speedLimitAircraftType2) && Objects.equals(speedLimitAltitude2, that.speedLimitAltitude2) && Objects.equals(verticalNavigationVnav, that.verticalNavigationVnav) && Objects.equals(thresholdCrossingHeight, that.thresholdCrossingHeight) && Objects.equals(arcWaypointIdentifier, that.arcWaypointIdentifier) && Objects.equals(arcWaypointCountryCode, that.arcWaypointCountryCode) && Objects.equals(arcRadius, that.arcRadius);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airportIdentification, terminalProcedureType, terminalIdentifier, terminalSequenceNumber, terminalApproachType, transitionIdentifier, icaoCode, trackDescriptionCode, termSegWaypointIdentifier, waypointCountryCode, terminalWaypointDescriptionCode1Arpt, terminalWaypointDescriptionCode2, terminalWaypointDescriptionCode3, terminalWaypointDescriptionCode4, terminalSegmentTurnDirection, navaid1Identifier, navaid1Type, navaid1CountryCode, navaid1KeyCode, fix1Bearing, fix1Distance, navaid2Identifier, navaid2Type, navaid2CountryCode, navaid2KeyCode, fix2Bearing, fix2Distance, terminalMagneticCourse, distance, altitudeDescription, altitude1, altitude2, requiredNavPerformance, cycleDate, waypointGeodeticLatitude, waypointDegreesLatitude, waypointGeodeticLongitude, waypointDegreesLongitude, waypointMagneticVariation, navaid1GeodeticLatitude, navaid1DegreesLatitude, navaid1GeodeticLongitude, navaid1DegreesLongitude, navaid1MagneticVariation, navaid1DmeGeodeticLatitude, navaid1DmeDegreesLatitude, navaid1DmeGeodeticLongitude, navaid1DmeDegreesLongitude, navaid2GeodeticLatitude, navaid2DegreesLatitude, navaid2GeodeticLongitude, navaid2DegreesLongitude, navaid2MagneticVariation, navaid2DmeGeodeticLatitude, navaid2DmeDegreesLatitude, navaid2DmeGeodeticLongitude, navaid2DmeDegreesLongitude, speedLimit1, speedLimitAircraftType1, speedLimitAltitude1, speedLimit2, speedLimitAircraftType2, speedLimitAltitude2, verticalNavigationVnav, thresholdCrossingHeight, arcWaypointIdentifier, arcWaypointCountryCode, arcRadius);
  }

  public String airportIdentification() {
    return airportIdentification;
  }

  public Integer terminalProcedureType() {
    return terminalProcedureType;
  }

  public String terminalIdentifier() {
    return terminalIdentifier;
  }

  public Integer terminalSequenceNumber() {
    return terminalSequenceNumber;
  }

  public String terminalApproachType() {
    return terminalApproachType;
  }

  public String transitionIdentifier() {
    return transitionIdentifier;
  }

  public String icaoCode() {
    return icaoCode;
  }

  public String trackDescriptionCode() {
    return trackDescriptionCode;
  }

  public Integer cycleDate() {
    return cycleDate;
  }

  public Optional<String> termSegWaypointIdentifier() {
    return Optional.ofNullable(termSegWaypointIdentifier);
  }

  public Optional<String> waypointCountryCode() {
    return Optional.ofNullable(waypointCountryCode);
  }

  public Optional<String> terminalWaypointDescriptionCode1Arpt() {
    return Optional.ofNullable(terminalWaypointDescriptionCode1Arpt);
  }

  public Optional<String> terminalWaypointDescriptionCode2() {
    return Optional.ofNullable(terminalWaypointDescriptionCode2);
  }

  public Optional<String> terminalWaypointDescriptionCode3() {
    return Optional.ofNullable(terminalWaypointDescriptionCode3);
  }

  public Optional<String> terminalWaypointDescriptionCode4() {
    return Optional.ofNullable(terminalWaypointDescriptionCode4);
  }

  public Optional<String> terminalSegmentTurnDirection() {
    return Optional.ofNullable(terminalSegmentTurnDirection);
  }

  public Optional<String> navaid1Identifier() {
    return Optional.ofNullable(navaid1Identifier);
  }

  public Optional<String> navaid1Type() {
    return Optional.ofNullable(navaid1Type);
  }

  public Optional<String> navaid1CountryCode() {
    return Optional.ofNullable(navaid1CountryCode);
  }

  public Optional<Integer> navaid1KeyCode() {
    return Optional.ofNullable(navaid1KeyCode);
  }

  public Optional<Double> fix1Bearing() {
    return Optional.ofNullable(fix1Bearing);
  }

  public Optional<Double> fix1Distance() {
    return Optional.ofNullable(fix1Distance);
  }

  public Optional<String> navaid2Identifier() {
    return Optional.ofNullable(navaid2Identifier);
  }

  public Optional<String> navaid2Type() {
    return Optional.ofNullable(navaid2Type);
  }

  public Optional<String> navaid2CountryCode() {
    return Optional.ofNullable(navaid2CountryCode);
  }

  public Optional<Integer> navaid2KeyCode() {
    return Optional.ofNullable(navaid2KeyCode);
  }

  public Optional<Double> fix2Bearing() {
    return Optional.ofNullable(fix2Bearing);
  }

  public Optional<Double> fix2Distance() {
    return Optional.ofNullable(fix2Distance);
  }

  public Optional<String> terminalMagneticCourse() {
    return Optional.ofNullable(terminalMagneticCourse);
  }

  public Optional<Double> distance() {
    return Optional.ofNullable(distance);
  }

  public Optional<String> altitudeDescription() {
    return Optional.ofNullable(altitudeDescription);
  }

  public Optional<String> altitude1() {
    return Optional.ofNullable(altitude1);
  }

  public Optional<String> altitude2() {
    return Optional.ofNullable(altitude2);
  }

  public Optional<Double> requiredNavPerformance() {
    return Optional.ofNullable(requiredNavPerformance);
  }

  public Optional<String> waypointGeodeticLatitude() {
    return Optional.ofNullable(waypointGeodeticLatitude);
  }

  public Optional<Double> waypointDegreesLatitude() {
    return Optional.ofNullable(waypointDegreesLatitude);
  }

  public Optional<String> waypointGeodeticLongitude() {
    return Optional.ofNullable(waypointGeodeticLongitude);
  }

  public Optional<Double> waypointDegreesLongitude() {
    return Optional.ofNullable(waypointDegreesLongitude);
  }

  public Optional<Double> waypointMagneticVariation() {
    return Optional.ofNullable(waypointMagneticVariation);
  }

  public Optional<String> navaid1GeodeticLatitude() {
    return Optional.ofNullable(navaid1GeodeticLatitude);
  }

  public Optional<Double> navaid1DegreesLatitude() {
    return Optional.ofNullable(navaid1DegreesLatitude);
  }

  public Optional<String> navaid1GeodeticLongitude() {
    return Optional.ofNullable(navaid1GeodeticLongitude);
  }

  public Optional<Double> navaid1DegreesLongitude() {
    return Optional.ofNullable(navaid1DegreesLongitude);
  }

  public Optional<Double> navaid1MagneticVariation() {
    return Optional.ofNullable(navaid1MagneticVariation);
  }

  public Optional<String> navaid1DmeGeodeticLatitude() {
    return Optional.ofNullable(navaid1DmeGeodeticLatitude);
  }

  public Optional<Double> navaid1DmeDegreesLatitude() {
    return Optional.ofNullable(navaid1DmeDegreesLatitude);
  }

  public Optional<String> navaid1DmeGeodeticLongitude() {
    return Optional.ofNullable(navaid1DmeGeodeticLongitude);
  }

  public Optional<Double> navaid1DmeDegreesLongitude() {
    return Optional.ofNullable(navaid1DmeDegreesLongitude);
  }

  public Optional<String> navaid2GeodeticLatitude() {
    return Optional.ofNullable(navaid2GeodeticLatitude);
  }

  public Optional<Double> navaid2DegreesLatitude() {
    return Optional.ofNullable(navaid2DegreesLatitude);
  }

  public Optional<String> navaid2GeodeticLongitude() {
    return Optional.ofNullable(navaid2GeodeticLongitude);
  }

  public Optional<Double> navaid2DegreesLongitude() {
    return Optional.ofNullable(navaid2DegreesLongitude);
  }

  public Optional<Double> navaid2MagneticVariation() {
    return Optional.ofNullable(navaid2MagneticVariation);
  }

  public Optional<String> navaid2DmeGeodeticLatitude() {
    return Optional.ofNullable(navaid2DmeGeodeticLatitude);
  }

  public Optional<Double> navaid2DmeDegreesLatitude() {
    return Optional.ofNullable(navaid2DmeDegreesLatitude);
  }

  public Optional<String> navaid2DmeGeodeticLongitude() {
    return Optional.ofNullable(navaid2DmeGeodeticLongitude);
  }

  public Optional<Double> navaid2DmeDegreesLongitude() {
    return Optional.ofNullable(navaid2DmeDegreesLongitude);
  }

  public Optional<Double> speedLimit1() {
    return Optional.ofNullable(speedLimit1);
  }

  public Optional<String> speedLimitAircraftType1() {
    return Optional.ofNullable(speedLimitAircraftType1);
  }

  public Optional<String> speedLimitAltitude1() {
    return Optional.ofNullable(speedLimitAltitude1);
  }

  public Optional<Double> speedLimit2() {
    return Optional.ofNullable(speedLimit2);
  }

  public Optional<String> speedLimitAircraftType2() {
    return Optional.ofNullable(speedLimitAircraftType2);
  }

  public Optional<String> speedLimitAltitude2() {
    return Optional.ofNullable(speedLimitAltitude2);
  }

  public Optional<Double> verticalNavigationVnav() {
    return Optional.ofNullable(verticalNavigationVnav);
  }

  public Optional<Integer> thresholdCrossingHeight() {
    return Optional.ofNullable(thresholdCrossingHeight);
  }

  public Optional<String> arcWaypointIdentifier() {
    return Optional.ofNullable(arcWaypointIdentifier);
  }

  public Optional<String> arcWaypointCountryCode() {
    return Optional.ofNullable(arcWaypointCountryCode);
  }

  public Optional<Double> arcRadius() {
    return Optional.ofNullable(arcRadius);
  }

  @Override
  public DafifFileType getFileType() {
    return DafifFileType.TERMINAL_SEGMENT;
  }

  public static class Builder {
    private String airportIdentification;
    private Integer terminalProcedureType;
    private String terminalIdentifier;
    private Integer terminalSequenceNumber;
    private String terminalApproachType;
    private String transitionIdentifier;
    private String icaoCode;
    private String trackDescriptionCode;
    private String termSegWaypointIdentifier;
    private String waypointCountryCode;
    private String terminalWaypointDescriptionCode1Arpt;
    private String terminalWaypointDescriptionCode2;
    private String terminalWaypointDescriptionCode3;
    private String terminalWaypointDescriptionCode4;
    private String terminalSegmentTurnDirection;
    private String navaid1Identifier;
    private String navaid1Type;
    private String navaid1CountryCode;
    private Integer navaid1KeyCode;
    private Double fix1Bearing;
    private Double fix1Distance;
    private String navaid2Identifier;
    private String navaid2Type;
    private String navaid2CountryCode;
    private Integer navaid2KeyCode;
    private Double fix2Bearing;
    private Double fix2Distance;
    private String terminalMagneticCourse;
    private Double distance;
    private String altitudeDescription;
    private String altitude1;
    private String altitude2;
    private Double requiredNavPerformance;
    private Integer cycleDate;
    private String waypointGeodeticLatitude;
    private Double waypointDegreesLatitude;
    private String waypointGeodeticLongitude;
    private Double waypointDegreesLongitude;
    private Double waypointMagneticVariation;
    private String navaid1GeodeticLatitude;
    private Double navaid1DegreesLatitude;
    private String navaid1GeodeticLongitude;
    private Double navaid1DegreesLongitude;
    private Double navaid1MagneticVariation;
    private String navaid1DmeGeodeticLatitude;
    private Double navaid1DmeDegreesLatitude;
    private String navaid1DmeGeodeticLongitude;
    private Double navaid1DmeDegreesLongitude;
    private String navaid2GeodeticLatitude;
    private Double navaid2DegreesLatitude;
    private String navaid2GeodeticLongitude;
    private Double navaid2DegreesLongitude;
    private Double navaid2MagneticVariation;
    private String navaid2DmeGeodeticLatitude;
    private Double navaid2DmeDegreesLatitude;
    private String navaid2DmeGeodeticLongitude;
    private Double navaid2DmeDegreesLongitude;
    private Double speedLimit1;
    private String speedLimitAircraftType1;
    private String speedLimitAltitude1;
    private Double speedLimit2;
    private String speedLimitAircraftType2;
    private String speedLimitAltitude2;
    private Double verticalNavigationVnav;
    private Integer thresholdCrossingHeight;
    private String arcWaypointIdentifier;
    private String arcWaypointCountryCode;
    private Double arcRadius;

    public Builder airportIdentification(String airportIdentification) {
      this.airportIdentification = airportIdentification;
      return this;
    }

    public Builder terminalProcedureType(Integer terminalProcedureType) {
      this.terminalProcedureType = terminalProcedureType;
      return this;
    }

    public Builder terminalIdentifier(String terminalIdentifier) {
      this.terminalIdentifier = terminalIdentifier;
      return this;
    }

    public Builder terminalSequenceNumber(Integer terminalSequenceNumber) {
      this.terminalSequenceNumber = terminalSequenceNumber;
      return this;
    }

    public Builder terminalApproachType(String terminalApproachType) {
      this.terminalApproachType = terminalApproachType;
      return this;
    }

    public Builder transitionIdentifier(String transitionIdentifier) {
      this.transitionIdentifier = transitionIdentifier;
      return this;
    }

    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder trackDescriptionCode(String trackDescriptionCode) {
      this.trackDescriptionCode = trackDescriptionCode;
      return this;
    }

    public Builder termSegWaypointIdentifier(String termSegWaypointIdentifier) {
      this.termSegWaypointIdentifier = termSegWaypointIdentifier;
      return this;
    }

    public Builder waypointCountryCode(String waypointCountryCode) {
      this.waypointCountryCode = waypointCountryCode;
      return this;
    }

    public Builder terminalWaypointDescriptionCode1Arpt(String terminalWaypointDescriptionCode1Arpt) {
      this.terminalWaypointDescriptionCode1Arpt = terminalWaypointDescriptionCode1Arpt;
      return this;
    }

    public Builder terminalWaypointDescriptionCode2(String terminalWaypointDescriptionCode2) {
      this.terminalWaypointDescriptionCode2 = terminalWaypointDescriptionCode2;
      return this;
    }

    public Builder terminalWaypointDescriptionCode3(String terminalWaypointDescriptionCode3) {
      this.terminalWaypointDescriptionCode3 = terminalWaypointDescriptionCode3;
      return this;
    }

    public Builder terminalWaypointDescriptionCode4(String terminalWaypointDescriptionCode4) {
      this.terminalWaypointDescriptionCode4 = terminalWaypointDescriptionCode4;
      return this;
    }

    public Builder terminalSegmentTurnDirection(String terminalSegmentTurnDirection) {
      this.terminalSegmentTurnDirection = terminalSegmentTurnDirection;
      return this;
    }

    public Builder navaid1Identifier(String navaid1Identifier) {
      this.navaid1Identifier = navaid1Identifier;
      return this;
    }

    public Builder navaid1Type(String navaid1Type) {
      this.navaid1Type = navaid1Type;
      return this;
    }

    public Builder navaid1CountryCode(String navaid1CountryCode) {
      this.navaid1CountryCode = navaid1CountryCode;
      return this;
    }

    public Builder navaid1KeyCode(Integer navaid1KeyCode) {
      this.navaid1KeyCode = navaid1KeyCode;
      return this;
    }

    public Builder fix1Bearing(Double fix1Bearing) {
      this.fix1Bearing = fix1Bearing;
      return this;
    }

    public Builder fix1Distance(Double fix1Distance) {
      this.fix1Distance = fix1Distance;
      return this;
    }

    public Builder navaid2Identifier(String navaid2Identifier) {
      this.navaid2Identifier = navaid2Identifier;
      return this;
    }

    public Builder navaid2Type(String navaid2Type) {
      this.navaid2Type = navaid2Type;
      return this;
    }

    public Builder navaid2CountryCode(String navaid2CountryCode) {
      this.navaid2CountryCode = navaid2CountryCode;
      return this;
    }

    public Builder navaid2KeyCode(Integer navaid2KeyCode) {
      this.navaid2KeyCode = navaid2KeyCode;
      return this;
    }

    public Builder fix2Bearing(Double fix2Bearing) {
      this.fix2Bearing = fix2Bearing;
      return this;
    }

    public Builder fix2Distance(Double fix2Distance) {
      this.fix2Distance = fix2Distance;
      return this;
    }

    public Builder terminalMagneticCourse(String terminalMagneticCourse) {
      this.terminalMagneticCourse = terminalMagneticCourse;
      return this;
    }

    public Builder distance(Double distance) {
      this.distance = distance;
      return this;
    }

    public Builder altitudeDescription(String altitudeDescription) {
      this.altitudeDescription = altitudeDescription;
      return this;
    }

    public Builder altitude1(String altitude1) {
      this.altitude1 = altitude1;
      return this;
    }

    public Builder altitude2(String altitude2) {
      this.altitude2 = altitude2;
      return this;
    }

    public Builder requiredNavPerformance(Double requiredNavPerformance) {
      this.requiredNavPerformance = requiredNavPerformance;
      return this;
    }

    public Builder cycleDate(Integer cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }

    public Builder waypointGeodeticLatitude(String waypointGeodeticLatitude) {
      this.waypointGeodeticLatitude = waypointGeodeticLatitude;
      return this;
    }

    public Builder waypointDegreesLatitude(Double waypointDegreesLatitude) {
      this.waypointDegreesLatitude = waypointDegreesLatitude;
      return this;
    }

    public Builder waypointGeodeticLongitude(String waypointGeodeticLongitude) {
      this.waypointGeodeticLongitude = waypointGeodeticLongitude;
      return this;
    }

    public Builder waypointDegreesLongitude(Double waypointDegreesLongitude) {
      this.waypointDegreesLongitude = waypointDegreesLongitude;
      return this;
    }

    public Builder waypointMagneticVariation(Double waypointMagneticVariation) {
      this.waypointMagneticVariation = waypointMagneticVariation;
      return this;
    }

    public Builder navaid1GeodeticLatitude(String navaid1GeodeticLatitude) {
      this.navaid1GeodeticLatitude = navaid1GeodeticLatitude;
      return this;
    }

    public Builder navaid1DegreesLatitude(Double navaid1DegreesLatitude) {
      this.navaid1DegreesLatitude = navaid1DegreesLatitude;
      return this;
    }

    public Builder navaid1GeodeticLongitude(String navaid1GeodeticLongitude) {
      this.navaid1GeodeticLongitude = navaid1GeodeticLongitude;
      return this;
    }

    public Builder navaid1DegreesLongitude(Double navaid1DegreesLongitude) {
      this.navaid1DegreesLongitude = navaid1DegreesLongitude;
      return this;
    }

    public Builder navaid1MagneticVariation(Double navaid1MagneticVariation) {
      this.navaid1MagneticVariation = navaid1MagneticVariation;
      return this;
    }

    public Builder navaid1DmeGeodeticLatitude(String navaid1DmeGeodeticLatitude) {
      this.navaid1DmeGeodeticLatitude = navaid1DmeGeodeticLatitude;
      return this;
    }

    public Builder navaid1DmeDegreesLatitude(Double navaid1DmeDegreesLatitude) {
      this.navaid1DmeDegreesLatitude = navaid1DmeDegreesLatitude;
      return this;
    }

    public Builder navaid1DmeGeodeticLongitude(String navaid1DmeGeodeticLongitude) {
      this.navaid1DmeGeodeticLongitude = navaid1DmeGeodeticLongitude;
      return this;
    }

    public Builder navaid1DmeDegreesLongitude(Double navaid1DmeDegreesLongitude) {
      this.navaid1DmeDegreesLongitude = navaid1DmeDegreesLongitude;
      return this;
    }

    public Builder navaid2GeodeticLatitude(String navaid2GeodeticLatitude) {
      this.navaid2GeodeticLatitude = navaid2GeodeticLatitude;
      return this;
    }

    public Builder navaid2DegreesLatitude(Double navaid2DegreesLatitude) {
      this.navaid2DegreesLatitude = navaid2DegreesLatitude;
      return this;
    }

    public Builder navaid2GeodeticLongitude(String navaid2GeodeticLongitude) {
      this.navaid2GeodeticLongitude = navaid2GeodeticLongitude;
      return this;
    }

    public Builder navaid2DegreesLongitude(Double navaid2DegreesLongitude) {
      this.navaid2DegreesLongitude = navaid2DegreesLongitude;
      return this;
    }

    public Builder navaid2MagneticVariation(Double navaid2MagneticVariation) {
      this.navaid2MagneticVariation = navaid2MagneticVariation;
      return this;
    }

    public Builder navaid2DmeGeodeticLatitude(String navaid2DmeGeodeticLatitude) {
      this.navaid2DmeGeodeticLatitude = navaid2DmeGeodeticLatitude;
      return this;
    }

    public Builder navaid2DmeDegreesLatitude(Double navaid2DmeDegreesLatitude) {
      this.navaid2DmeDegreesLatitude = navaid2DmeDegreesLatitude;
      return this;
    }

    public Builder navaid2DmeGeodeticLongitude(String navaid2DmeGeodeticLongitude) {
      this.navaid2DmeGeodeticLongitude = navaid2DmeGeodeticLongitude;
      return this;
    }

    public Builder navaid2DmeDegreesLongitude(Double navaid2DmeDegreesLongitude) {
      this.navaid2DmeDegreesLongitude = navaid2DmeDegreesLongitude;
      return this;
    }

    public Builder speedLimit1(Double speedLimit1) {
      this.speedLimit1 = speedLimit1;
      return this;
    }

    public Builder speedLimitAircraftType1(String speedLimitAircraftType1) {
      this.speedLimitAircraftType1 = speedLimitAircraftType1;
      return this;
    }

    public Builder speedLimitAltitude1(String speedLimitAltitude1) {
      this.speedLimitAltitude1 = speedLimitAltitude1;
      return this;
    }

    public Builder speedLimit2(Double speedLimit2) {
      this.speedLimit2 = speedLimit2;
      return this;
    }

    public Builder speedLimitAircraftType2(String speedLimitAircraftType2) {
      this.speedLimitAircraftType2 = speedLimitAircraftType2;
      return this;
    }

    public Builder speedLimitAltitude2(String speedLimitAltitude2) {
      this.speedLimitAltitude2 = speedLimitAltitude2;
      return this;
    }

    public Builder verticalNavigationVnav(Double verticalNavigationVnav) {
      this.verticalNavigationVnav = verticalNavigationVnav;
      return this;
    }

    public Builder thresholdCrossingHeight(Integer thresholdCrossingHeight) {
      this.thresholdCrossingHeight = thresholdCrossingHeight;
      return this;
    }

    public Builder arcWaypointIdentifier(String arcWaypointIdentifier) {
      this.arcWaypointIdentifier = arcWaypointIdentifier;
      return this;
    }

    public Builder arcWaypointCountryCode(String arcWaypointCountryCode) {
      this.arcWaypointCountryCode = arcWaypointCountryCode;
      return this;
    }

    public Builder arcRadius(Double arcRadius) {
      this.arcRadius = arcRadius;
      return this;
    }

    public DafifTerminalSegment build() {
      return new DafifTerminalSegment(this);
    }
  }
}
