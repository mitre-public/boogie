package org.mitre.boogie.xml.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;

/**
 * Data class for representing structured/parsed content from within a procedure leg record in the ARINC 424 XML schema.
 *
 * <p>This class flattens the XML class hierarchy ({@code Leg → ProcedureLeg → ApproachLeg/SidLeg/StarLeg}) into a
 * single model type. Fields from subclass-specific types (e.g. {@code ApproachLeg}, {@code SidLeg}) will be null when
 * the source leg is not of that type.
 */
public final class ArincProcedureLeg {

  // A424Record fields
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;

  // Leg fields
  private final long sequenceNumber;
  private final String fixRef;
  private final String fixIdent;
  private final String recNavaidIdent;
  private final String recNavaidRef;

  // ProcedureLeg fields
  private final String pathAndTermination;
  private final String turnDirection;
  private final Boolean isTurnDirectionValid;
  private final Double arcRadius;
  private final String atcIndicator;
  private final Double distance;
  private final Double legDistance;
  private final String legInboundIndicator;
  private final String legInboundOutboundIndicator;
  private final Double rho;
  private final Double rnp;
  private final Double theta;
  private final Double courseValue;
  private final Boolean courseIsTrue;
  private final String centerFix;
  private final String centerFixRef;
  private final Integer transitionsAltitudeLevel;
  private final Long verticalScaleFactor;
  private final String holdTime;
  private final String procedureDesignMagVarDirection;
  private final Double procedureDesignMagVarValue;

  // SpeedLimit (flattened)
  private final Long speedLimitAt;
  private final Long speedLimitAtOrAbove;
  private final Long speedLimitAtOrBelow;

  // AltitudeConstraint (flattened)
  private final Integer altitudeConstraintAt;
  private final Integer altitudeConstraintAtOrAbove;
  private final Integer altitudeConstraintAtOrBelow;

  // AltitudeTermination (flattened)
  private final String altitudeTerminationType;
  private final Integer altitudeTerminationValue;

  // RaceTrackAltitude (flattened)
  private final Integer raceTrackAltitude;

  // ProcedureWaypointDescription (flattened)
  private final Boolean isEssential;
  private final Boolean isFlyOver;
  private final Boolean isHolding;
  private final Boolean isNoProcedureTurn;
  private final Boolean isPhantomFix;
  private final Boolean isSourceProvidedEnrouteWaypoint;
  private final Boolean isTaaProcedureTurn;
  private final Boolean isAtcCompulsoryReportingPoint;

  // ApproachLeg fields
  private final Double verticalAngle;
  private final Integer glideSlopeCrossingAltitude;

  // ApproachWaypointDescription (flattened)
  private final Boolean isFacf;
  private final Boolean isMissedApproachPoint;
  private final Boolean isFaf;
  private final Boolean isFinalEndPoint;
  private final Boolean isFixTurningFinalApproach;
  private final Boolean isInitialApproachFix;
  private final Boolean isIntermediateApproachFix;
  private final String stepDownFix;

  // SidLeg fields
  private final Boolean isEngineOutDisarmPoint;
  private final Boolean isInitialDepartureFix;
  private final Boolean isQuietClimbRestorePoint;

  private ArincProcedureLeg(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.sequenceNumber = builder.sequenceNumber;
    this.fixRef = builder.fixRef;
    this.fixIdent = builder.fixIdent;
    this.recNavaidIdent = builder.recNavaidIdent;
    this.recNavaidRef = builder.recNavaidRef;
    this.pathAndTermination = builder.pathAndTermination;
    this.turnDirection = builder.turnDirection;
    this.isTurnDirectionValid = builder.isTurnDirectionValid;
    this.arcRadius = builder.arcRadius;
    this.atcIndicator = builder.atcIndicator;
    this.distance = builder.distance;
    this.legDistance = builder.legDistance;
    this.legInboundIndicator = builder.legInboundIndicator;
    this.legInboundOutboundIndicator = builder.legInboundOutboundIndicator;
    this.rho = builder.rho;
    this.rnp = builder.rnp;
    this.theta = builder.theta;
    this.courseValue = builder.courseValue;
    this.courseIsTrue = builder.courseIsTrue;
    this.centerFix = builder.centerFix;
    this.centerFixRef = builder.centerFixRef;
    this.transitionsAltitudeLevel = builder.transitionsAltitudeLevel;
    this.verticalScaleFactor = builder.verticalScaleFactor;
    this.holdTime = builder.holdTime;
    this.procedureDesignMagVarDirection = builder.procedureDesignMagVarDirection;
    this.procedureDesignMagVarValue = builder.procedureDesignMagVarValue;
    this.speedLimitAt = builder.speedLimitAt;
    this.speedLimitAtOrAbove = builder.speedLimitAtOrAbove;
    this.speedLimitAtOrBelow = builder.speedLimitAtOrBelow;
    this.altitudeConstraintAt = builder.altitudeConstraintAt;
    this.altitudeConstraintAtOrAbove = builder.altitudeConstraintAtOrAbove;
    this.altitudeConstraintAtOrBelow = builder.altitudeConstraintAtOrBelow;
    this.altitudeTerminationType = builder.altitudeTerminationType;
    this.altitudeTerminationValue = builder.altitudeTerminationValue;
    this.raceTrackAltitude = builder.raceTrackAltitude;
    this.isEssential = builder.isEssential;
    this.isFlyOver = builder.isFlyOver;
    this.isHolding = builder.isHolding;
    this.isNoProcedureTurn = builder.isNoProcedureTurn;
    this.isPhantomFix = builder.isPhantomFix;
    this.isSourceProvidedEnrouteWaypoint = builder.isSourceProvidedEnrouteWaypoint;
    this.isTaaProcedureTurn = builder.isTaaProcedureTurn;
    this.isAtcCompulsoryReportingPoint = builder.isAtcCompulsoryReportingPoint;
    this.verticalAngle = builder.verticalAngle;
    this.glideSlopeCrossingAltitude = builder.glideSlopeCrossingAltitude;
    this.isFacf = builder.isFacf;
    this.isMissedApproachPoint = builder.isMissedApproachPoint;
    this.isFaf = builder.isFaf;
    this.isFinalEndPoint = builder.isFinalEndPoint;
    this.isFixTurningFinalApproach = builder.isFixTurningFinalApproach;
    this.isInitialApproachFix = builder.isInitialApproachFix;
    this.isIntermediateApproachFix = builder.isIntermediateApproachFix;
    this.stepDownFix = builder.stepDownFix;
    this.isEngineOutDisarmPoint = builder.isEngineOutDisarmPoint;
    this.isInitialDepartureFix = builder.isInitialDepartureFix;
    this.isQuietClimbRestorePoint = builder.isQuietClimbRestorePoint;
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

  public long sequenceNumber() {
    return sequenceNumber;
  }

  public Optional<String> fixRef() {
    return Optional.ofNullable(fixRef);
  }

  public Optional<String> fixIdent() {
    return Optional.ofNullable(fixIdent);
  }

  public Optional<String> recNavaidIdent() {
    return Optional.ofNullable(recNavaidIdent);
  }

  public Optional<String> recNavaidRef() {
    return Optional.ofNullable(recNavaidRef);
  }

  public String pathAndTermination() {
    return pathAndTermination;
  }

  public Optional<String> turnDirection() {
    return Optional.ofNullable(turnDirection);
  }

  public Optional<Boolean> isTurnDirectionValid() {
    return Optional.ofNullable(isTurnDirectionValid);
  }

  public Optional<Double> arcRadius() {
    return Optional.ofNullable(arcRadius);
  }

  public Optional<String> atcIndicator() {
    return Optional.ofNullable(atcIndicator);
  }

  public Optional<Double> distance() {
    return Optional.ofNullable(distance);
  }

  public Optional<Double> legDistance() {
    return Optional.ofNullable(legDistance);
  }

  public Optional<String> legInboundIndicator() {
    return Optional.ofNullable(legInboundIndicator);
  }

  public Optional<String> legInboundOutboundIndicator() {
    return Optional.ofNullable(legInboundOutboundIndicator);
  }

  public Optional<Double> rho() {
    return Optional.ofNullable(rho);
  }

  public Optional<Double> rnp() {
    return Optional.ofNullable(rnp);
  }

  public Optional<Double> theta() {
    return Optional.ofNullable(theta);
  }

  public Optional<Double> courseValue() {
    return Optional.ofNullable(courseValue);
  }

  public Optional<Boolean> courseIsTrue() {
    return Optional.ofNullable(courseIsTrue);
  }

  public Optional<String> centerFix() {
    return Optional.ofNullable(centerFix);
  }

  public Optional<String> centerFixRef() {
    return Optional.ofNullable(centerFixRef);
  }

  public Optional<Integer> transitionsAltitudeLevel() {
    return Optional.ofNullable(transitionsAltitudeLevel);
  }

  public Optional<Long> verticalScaleFactor() {
    return Optional.ofNullable(verticalScaleFactor);
  }

  public Optional<String> holdTime() {
    return Optional.ofNullable(holdTime);
  }

  public Optional<String> procedureDesignMagVarDirection() {
    return Optional.ofNullable(procedureDesignMagVarDirection);
  }

  public Optional<Double> procedureDesignMagVarValue() {
    return Optional.ofNullable(procedureDesignMagVarValue);
  }

  public Optional<Long> speedLimitAt() {
    return Optional.ofNullable(speedLimitAt);
  }

  public Optional<Long> speedLimitAtOrAbove() {
    return Optional.ofNullable(speedLimitAtOrAbove);
  }

  public Optional<Long> speedLimitAtOrBelow() {
    return Optional.ofNullable(speedLimitAtOrBelow);
  }

  public Optional<Integer> altitudeConstraintAt() {
    return Optional.ofNullable(altitudeConstraintAt);
  }

  public Optional<Integer> altitudeConstraintAtOrAbove() {
    return Optional.ofNullable(altitudeConstraintAtOrAbove);
  }

  public Optional<Integer> altitudeConstraintAtOrBelow() {
    return Optional.ofNullable(altitudeConstraintAtOrBelow);
  }

  public Optional<String> altitudeTerminationType() {
    return Optional.ofNullable(altitudeTerminationType);
  }

  public Optional<Integer> altitudeTerminationValue() {
    return Optional.ofNullable(altitudeTerminationValue);
  }

  public Optional<Integer> raceTrackAltitude() {
    return Optional.ofNullable(raceTrackAltitude);
  }

  public Optional<Boolean> isEssential() {
    return Optional.ofNullable(isEssential);
  }

  public Optional<Boolean> isFlyOver() {
    return Optional.ofNullable(isFlyOver);
  }

  public Optional<Boolean> isHolding() {
    return Optional.ofNullable(isHolding);
  }

  public Optional<Boolean> isNoProcedureTurn() {
    return Optional.ofNullable(isNoProcedureTurn);
  }

  public Optional<Boolean> isPhantomFix() {
    return Optional.ofNullable(isPhantomFix);
  }

  public Optional<Boolean> isSourceProvidedEnrouteWaypoint() {
    return Optional.ofNullable(isSourceProvidedEnrouteWaypoint);
  }

  public Optional<Boolean> isTaaProcedureTurn() {
    return Optional.ofNullable(isTaaProcedureTurn);
  }

  public Optional<Boolean> isAtcCompulsoryReportingPoint() {
    return Optional.ofNullable(isAtcCompulsoryReportingPoint);
  }

  public Optional<Double> verticalAngle() {
    return Optional.ofNullable(verticalAngle);
  }

  public Optional<Integer> glideSlopeCrossingAltitude() {
    return Optional.ofNullable(glideSlopeCrossingAltitude);
  }

  public Optional<Boolean> isFacf() {
    return Optional.ofNullable(isFacf);
  }

  public Optional<Boolean> isMissedApproachPoint() {
    return Optional.ofNullable(isMissedApproachPoint);
  }

  public Optional<Boolean> isFaf() {
    return Optional.ofNullable(isFaf);
  }

  public Optional<Boolean> isFinalEndPoint() {
    return Optional.ofNullable(isFinalEndPoint);
  }

  public Optional<Boolean> isFixTurningFinalApproach() {
    return Optional.ofNullable(isFixTurningFinalApproach);
  }

  public Optional<Boolean> isInitialApproachFix() {
    return Optional.ofNullable(isInitialApproachFix);
  }

  public Optional<Boolean> isIntermediateApproachFix() {
    return Optional.ofNullable(isIntermediateApproachFix);
  }

  public Optional<String> stepDownFix() {
    return Optional.ofNullable(stepDownFix);
  }

  public Optional<Boolean> isEngineOutDisarmPoint() {
    return Optional.ofNullable(isEngineOutDisarmPoint);
  }

  public Optional<Boolean> isInitialDepartureFix() {
    return Optional.ofNullable(isInitialDepartureFix);
  }

  public Optional<Boolean> isQuietClimbRestorePoint() {
    return Optional.ofNullable(isQuietClimbRestorePoint);
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
    return sequenceNumber == that.sequenceNumber
        && Objects.equals(baseInfo, that.baseInfo)
        && Objects.equals(recordInfo, that.recordInfo)
        && Objects.equals(fixRef, that.fixRef)
        && Objects.equals(fixIdent, that.fixIdent)
        && Objects.equals(recNavaidIdent, that.recNavaidIdent)
        && Objects.equals(recNavaidRef, that.recNavaidRef)
        && Objects.equals(pathAndTermination, that.pathAndTermination)
        && Objects.equals(turnDirection, that.turnDirection)
        && Objects.equals(isTurnDirectionValid, that.isTurnDirectionValid)
        && Objects.equals(arcRadius, that.arcRadius)
        && Objects.equals(atcIndicator, that.atcIndicator)
        && Objects.equals(distance, that.distance)
        && Objects.equals(legDistance, that.legDistance)
        && Objects.equals(legInboundIndicator, that.legInboundIndicator)
        && Objects.equals(legInboundOutboundIndicator, that.legInboundOutboundIndicator)
        && Objects.equals(rho, that.rho)
        && Objects.equals(rnp, that.rnp)
        && Objects.equals(theta, that.theta)
        && Objects.equals(courseValue, that.courseValue)
        && Objects.equals(courseIsTrue, that.courseIsTrue)
        && Objects.equals(centerFix, that.centerFix)
        && Objects.equals(centerFixRef, that.centerFixRef)
        && Objects.equals(transitionsAltitudeLevel, that.transitionsAltitudeLevel)
        && Objects.equals(verticalScaleFactor, that.verticalScaleFactor)
        && Objects.equals(holdTime, that.holdTime)
        && Objects.equals(procedureDesignMagVarDirection, that.procedureDesignMagVarDirection)
        && Objects.equals(procedureDesignMagVarValue, that.procedureDesignMagVarValue)
        && Objects.equals(speedLimitAt, that.speedLimitAt)
        && Objects.equals(speedLimitAtOrAbove, that.speedLimitAtOrAbove)
        && Objects.equals(speedLimitAtOrBelow, that.speedLimitAtOrBelow)
        && Objects.equals(altitudeConstraintAt, that.altitudeConstraintAt)
        && Objects.equals(altitudeConstraintAtOrAbove, that.altitudeConstraintAtOrAbove)
        && Objects.equals(altitudeConstraintAtOrBelow, that.altitudeConstraintAtOrBelow)
        && Objects.equals(altitudeTerminationType, that.altitudeTerminationType)
        && Objects.equals(altitudeTerminationValue, that.altitudeTerminationValue)
        && Objects.equals(raceTrackAltitude, that.raceTrackAltitude)
        && Objects.equals(isEssential, that.isEssential)
        && Objects.equals(isFlyOver, that.isFlyOver)
        && Objects.equals(isHolding, that.isHolding)
        && Objects.equals(isNoProcedureTurn, that.isNoProcedureTurn)
        && Objects.equals(isPhantomFix, that.isPhantomFix)
        && Objects.equals(isSourceProvidedEnrouteWaypoint, that.isSourceProvidedEnrouteWaypoint)
        && Objects.equals(isTaaProcedureTurn, that.isTaaProcedureTurn)
        && Objects.equals(isAtcCompulsoryReportingPoint, that.isAtcCompulsoryReportingPoint)
        && Objects.equals(verticalAngle, that.verticalAngle)
        && Objects.equals(glideSlopeCrossingAltitude, that.glideSlopeCrossingAltitude)
        && Objects.equals(isFacf, that.isFacf)
        && Objects.equals(isMissedApproachPoint, that.isMissedApproachPoint)
        && Objects.equals(isFaf, that.isFaf)
        && Objects.equals(isFinalEndPoint, that.isFinalEndPoint)
        && Objects.equals(isFixTurningFinalApproach, that.isFixTurningFinalApproach)
        && Objects.equals(isInitialApproachFix, that.isInitialApproachFix)
        && Objects.equals(isIntermediateApproachFix, that.isIntermediateApproachFix)
        && Objects.equals(stepDownFix, that.stepDownFix)
        && Objects.equals(isEngineOutDisarmPoint, that.isEngineOutDisarmPoint)
        && Objects.equals(isInitialDepartureFix, that.isInitialDepartureFix)
        && Objects.equals(isQuietClimbRestorePoint, that.isQuietClimbRestorePoint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, recordInfo, sequenceNumber, fixRef, fixIdent, recNavaidIdent, recNavaidRef,
        pathAndTermination, turnDirection, isTurnDirectionValid, arcRadius, atcIndicator, distance, legDistance,
        legInboundIndicator, legInboundOutboundIndicator, rho, rnp, theta, courseValue, courseIsTrue,
        centerFix, centerFixRef, transitionsAltitudeLevel, verticalScaleFactor, holdTime,
        procedureDesignMagVarDirection, procedureDesignMagVarValue, speedLimitAt, speedLimitAtOrAbove,
        speedLimitAtOrBelow, altitudeConstraintAt, altitudeConstraintAtOrAbove, altitudeConstraintAtOrBelow,
        altitudeTerminationType, altitudeTerminationValue, raceTrackAltitude, isEssential, isFlyOver, isHolding,
        isNoProcedureTurn, isPhantomFix, isSourceProvidedEnrouteWaypoint, isTaaProcedureTurn,
        isAtcCompulsoryReportingPoint, verticalAngle, glideSlopeCrossingAltitude, isFacf, isMissedApproachPoint,
        isFaf, isFinalEndPoint, isFixTurningFinalApproach, isInitialApproachFix, isIntermediateApproachFix,
        stepDownFix, isEngineOutDisarmPoint, isInitialDepartureFix, isQuietClimbRestorePoint);
  }

  @Override
  public String toString() {
    return "ArincProcedureLeg{" +
        "sequenceNumber=" + sequenceNumber +
        ", pathAndTermination='" + pathAndTermination + '\'' +
        ", fixIdent='" + fixIdent + '\'' +
        ", fixRef='" + fixRef + '\'' +
        ", recNavaidIdent='" + recNavaidIdent + '\'' +
        ", recNavaidRef='" + recNavaidRef + '\'' +
        ", turnDirection='" + turnDirection + '\'' +
        ", isTurnDirectionValid=" + isTurnDirectionValid +
        ", arcRadius=" + arcRadius +
        ", atcIndicator='" + atcIndicator + '\'' +
        ", distance=" + distance +
        ", legDistance=" + legDistance +
        ", legInboundIndicator='" + legInboundIndicator + '\'' +
        ", legInboundOutboundIndicator='" + legInboundOutboundIndicator + '\'' +
        ", rho=" + rho +
        ", rnp=" + rnp +
        ", theta=" + theta +
        ", courseValue=" + courseValue +
        ", courseIsTrue=" + courseIsTrue +
        ", centerFix='" + centerFix + '\'' +
        ", centerFixRef='" + centerFixRef + '\'' +
        ", transitionsAltitudeLevel=" + transitionsAltitudeLevel +
        ", verticalScaleFactor=" + verticalScaleFactor +
        ", holdTime='" + holdTime + '\'' +
        ", procedureDesignMagVarDirection='" + procedureDesignMagVarDirection + '\'' +
        ", procedureDesignMagVarValue=" + procedureDesignMagVarValue +
        ", speedLimitAt=" + speedLimitAt +
        ", speedLimitAtOrAbove=" + speedLimitAtOrAbove +
        ", speedLimitAtOrBelow=" + speedLimitAtOrBelow +
        ", altitudeConstraintAt=" + altitudeConstraintAt +
        ", altitudeConstraintAtOrAbove=" + altitudeConstraintAtOrAbove +
        ", altitudeConstraintAtOrBelow=" + altitudeConstraintAtOrBelow +
        ", altitudeTerminationType='" + altitudeTerminationType + '\'' +
        ", altitudeTerminationValue=" + altitudeTerminationValue +
        ", raceTrackAltitude=" + raceTrackAltitude +
        ", verticalAngle=" + verticalAngle +
        ", glideSlopeCrossingAltitude=" + glideSlopeCrossingAltitude +
        '}';
  }

  public static final class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private long sequenceNumber;
    private String fixRef;
    private String fixIdent;
    private String recNavaidIdent;
    private String recNavaidRef;
    private String pathAndTermination;
    private String turnDirection;
    private Boolean isTurnDirectionValid;
    private Double arcRadius;
    private String atcIndicator;
    private Double distance;
    private Double legDistance;
    private String legInboundIndicator;
    private String legInboundOutboundIndicator;
    private Double rho;
    private Double rnp;
    private Double theta;
    private Double courseValue;
    private Boolean courseIsTrue;
    private String centerFix;
    private String centerFixRef;
    private Integer transitionsAltitudeLevel;
    private Long verticalScaleFactor;
    private String holdTime;
    private String procedureDesignMagVarDirection;
    private Double procedureDesignMagVarValue;
    private Long speedLimitAt;
    private Long speedLimitAtOrAbove;
    private Long speedLimitAtOrBelow;
    private Integer altitudeConstraintAt;
    private Integer altitudeConstraintAtOrAbove;
    private Integer altitudeConstraintAtOrBelow;
    private String altitudeTerminationType;
    private Integer altitudeTerminationValue;
    private Integer raceTrackAltitude;
    private Boolean isEssential;
    private Boolean isFlyOver;
    private Boolean isHolding;
    private Boolean isNoProcedureTurn;
    private Boolean isPhantomFix;
    private Boolean isSourceProvidedEnrouteWaypoint;
    private Boolean isTaaProcedureTurn;
    private Boolean isAtcCompulsoryReportingPoint;
    private Double verticalAngle;
    private Integer glideSlopeCrossingAltitude;
    private Boolean isFacf;
    private Boolean isMissedApproachPoint;
    private Boolean isFaf;
    private Boolean isFinalEndPoint;
    private Boolean isFixTurningFinalApproach;
    private Boolean isInitialApproachFix;
    private Boolean isIntermediateApproachFix;
    private String stepDownFix;
    private Boolean isEngineOutDisarmPoint;
    private Boolean isInitialDepartureFix;
    private Boolean isQuietClimbRestorePoint;

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

    public Builder sequenceNumber(long sequenceNumber) {
      this.sequenceNumber = sequenceNumber;
      return this;
    }

    public Builder fixRef(String fixRef) {
      this.fixRef = fixRef;
      return this;
    }

    public Builder fixIdent(String fixIdent) {
      this.fixIdent = fixIdent;
      return this;
    }

    public Builder recNavaidIdent(String recNavaidIdent) {
      this.recNavaidIdent = recNavaidIdent;
      return this;
    }

    public Builder recNavaidRef(String recNavaidRef) {
      this.recNavaidRef = recNavaidRef;
      return this;
    }

    public Builder pathAndTermination(String pathAndTermination) {
      this.pathAndTermination = pathAndTermination;
      return this;
    }

    public Builder turnDirection(String turnDirection) {
      this.turnDirection = turnDirection;
      return this;
    }

    public Builder isTurnDirectionValid(Boolean isTurnDirectionValid) {
      this.isTurnDirectionValid = isTurnDirectionValid;
      return this;
    }

    public Builder arcRadius(Double arcRadius) {
      this.arcRadius = arcRadius;
      return this;
    }

    public Builder atcIndicator(String atcIndicator) {
      this.atcIndicator = atcIndicator;
      return this;
    }

    public Builder distance(Double distance) {
      this.distance = distance;
      return this;
    }

    public Builder legDistance(Double legDistance) {
      this.legDistance = legDistance;
      return this;
    }

    public Builder legInboundIndicator(String legInboundIndicator) {
      this.legInboundIndicator = legInboundIndicator;
      return this;
    }

    public Builder legInboundOutboundIndicator(String legInboundOutboundIndicator) {
      this.legInboundOutboundIndicator = legInboundOutboundIndicator;
      return this;
    }

    public Builder rho(Double rho) {
      this.rho = rho;
      return this;
    }

    public Builder rnp(Double rnp) {
      this.rnp = rnp;
      return this;
    }

    public Builder theta(Double theta) {
      this.theta = theta;
      return this;
    }

    public Builder courseValue(Double courseValue) {
      this.courseValue = courseValue;
      return this;
    }

    public Builder courseIsTrue(Boolean courseIsTrue) {
      this.courseIsTrue = courseIsTrue;
      return this;
    }

    public Builder centerFix(String centerFix) {
      this.centerFix = centerFix;
      return this;
    }

    public Builder centerFixRef(String centerFixRef) {
      this.centerFixRef = centerFixRef;
      return this;
    }

    public Builder transitionsAltitudeLevel(Integer transitionsAltitudeLevel) {
      this.transitionsAltitudeLevel = transitionsAltitudeLevel;
      return this;
    }

    public Builder verticalScaleFactor(Long verticalScaleFactor) {
      this.verticalScaleFactor = verticalScaleFactor;
      return this;
    }

    public Builder holdTime(String holdTime) {
      this.holdTime = holdTime;
      return this;
    }

    public Builder procedureDesignMagVarDirection(String procedureDesignMagVarDirection) {
      this.procedureDesignMagVarDirection = procedureDesignMagVarDirection;
      return this;
    }

    public Builder procedureDesignMagVarValue(Double procedureDesignMagVarValue) {
      this.procedureDesignMagVarValue = procedureDesignMagVarValue;
      return this;
    }

    public Builder speedLimitAt(Long speedLimitAt) {
      this.speedLimitAt = speedLimitAt;
      return this;
    }

    public Builder speedLimitAtOrAbove(Long speedLimitAtOrAbove) {
      this.speedLimitAtOrAbove = speedLimitAtOrAbove;
      return this;
    }

    public Builder speedLimitAtOrBelow(Long speedLimitAtOrBelow) {
      this.speedLimitAtOrBelow = speedLimitAtOrBelow;
      return this;
    }

    public Builder altitudeConstraintAt(Integer altitudeConstraintAt) {
      this.altitudeConstraintAt = altitudeConstraintAt;
      return this;
    }

    public Builder altitudeConstraintAtOrAbove(Integer altitudeConstraintAtOrAbove) {
      this.altitudeConstraintAtOrAbove = altitudeConstraintAtOrAbove;
      return this;
    }

    public Builder altitudeConstraintAtOrBelow(Integer altitudeConstraintAtOrBelow) {
      this.altitudeConstraintAtOrBelow = altitudeConstraintAtOrBelow;
      return this;
    }

    public Builder altitudeTerminationType(String altitudeTerminationType) {
      this.altitudeTerminationType = altitudeTerminationType;
      return this;
    }

    public Builder altitudeTerminationValue(Integer altitudeTerminationValue) {
      this.altitudeTerminationValue = altitudeTerminationValue;
      return this;
    }

    public Builder raceTrackAltitude(Integer raceTrackAltitude) {
      this.raceTrackAltitude = raceTrackAltitude;
      return this;
    }

    public Builder isEssential(Boolean isEssential) {
      this.isEssential = isEssential;
      return this;
    }

    public Builder isFlyOver(Boolean isFlyOver) {
      this.isFlyOver = isFlyOver;
      return this;
    }

    public Builder isHolding(Boolean isHolding) {
      this.isHolding = isHolding;
      return this;
    }

    public Builder isNoProcedureTurn(Boolean isNoProcedureTurn) {
      this.isNoProcedureTurn = isNoProcedureTurn;
      return this;
    }

    public Builder isPhantomFix(Boolean isPhantomFix) {
      this.isPhantomFix = isPhantomFix;
      return this;
    }

    public Builder isSourceProvidedEnrouteWaypoint(Boolean isSourceProvidedEnrouteWaypoint) {
      this.isSourceProvidedEnrouteWaypoint = isSourceProvidedEnrouteWaypoint;
      return this;
    }

    public Builder isTaaProcedureTurn(Boolean isTaaProcedureTurn) {
      this.isTaaProcedureTurn = isTaaProcedureTurn;
      return this;
    }

    public Builder isAtcCompulsoryReportingPoint(Boolean isAtcCompulsoryReportingPoint) {
      this.isAtcCompulsoryReportingPoint = isAtcCompulsoryReportingPoint;
      return this;
    }

    public Builder verticalAngle(Double verticalAngle) {
      this.verticalAngle = verticalAngle;
      return this;
    }

    public Builder glideSlopeCrossingAltitude(Integer glideSlopeCrossingAltitude) {
      this.glideSlopeCrossingAltitude = glideSlopeCrossingAltitude;
      return this;
    }

    public Builder isFacf(Boolean isFacf) {
      this.isFacf = isFacf;
      return this;
    }

    public Builder isMissedApproachPoint(Boolean isMissedApproachPoint) {
      this.isMissedApproachPoint = isMissedApproachPoint;
      return this;
    }

    public Builder isFaf(Boolean isFaf) {
      this.isFaf = isFaf;
      return this;
    }

    public Builder isFinalEndPoint(Boolean isFinalEndPoint) {
      this.isFinalEndPoint = isFinalEndPoint;
      return this;
    }

    public Builder isFixTurningFinalApproach(Boolean isFixTurningFinalApproach) {
      this.isFixTurningFinalApproach = isFixTurningFinalApproach;
      return this;
    }

    public Builder isInitialApproachFix(Boolean isInitialApproachFix) {
      this.isInitialApproachFix = isInitialApproachFix;
      return this;
    }

    public Builder isIntermediateApproachFix(Boolean isIntermediateApproachFix) {
      this.isIntermediateApproachFix = isIntermediateApproachFix;
      return this;
    }

    public Builder stepDownFix(String stepDownFix) {
      this.stepDownFix = stepDownFix;
      return this;
    }

    public Builder isEngineOutDisarmPoint(Boolean isEngineOutDisarmPoint) {
      this.isEngineOutDisarmPoint = isEngineOutDisarmPoint;
      return this;
    }

    public Builder isInitialDepartureFix(Boolean isInitialDepartureFix) {
      this.isInitialDepartureFix = isInitialDepartureFix;
      return this;
    }

    public Builder isQuietClimbRestorePoint(Boolean isQuietClimbRestorePoint) {
      this.isQuietClimbRestorePoint = isQuietClimbRestorePoint;
      return this;
    }

    public ArincProcedureLeg build() {
      return new ArincProcedureLeg(this);
    }
  }
}
