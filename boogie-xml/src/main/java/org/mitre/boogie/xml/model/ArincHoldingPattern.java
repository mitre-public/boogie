package org.mitre.boogie.xml.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;

public final class ArincHoldingPattern {

  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;

  private final Boolean isEnroute;
  private final Double arcRadius;
  private final String holdingPatternName;
  private final Long holdingSpeed;
  private final String holdingTime;
  private final Double holdingDistance;

  private final Double inboundHoldingCourseValue;
  private final Boolean inboundHoldingCourseIsTrue;
  private final String legInboundOutboundIndicator;

  private final Integer holdMinAltitude;
  private final Integer holdMaxAltitude;
  private final Double rnp;
  private final Integer rvsmMinAltitude;
  private final Integer rvsmMaxAltitude;
  private final String turnDirection;
  private final Long verticalScaleFactor;
  private final String fixIdentifier;
  private final String fixRef;
  private final String portRef;

  private final Boolean isOnUndefined;
  private final Boolean isOnHigh;
  private final Boolean isOnLow;
  private final Boolean isOnSid;
  private final Boolean isOnStar;
  private final Boolean isOnApproach;
  private final Boolean isOnMissedApproach;

  private final Long multipleIndicator;
  private final String inboundCourseNavaid;
  private final Double inboundCourseTheta;

  private ArincHoldingPattern(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.isEnroute = builder.isEnroute;
    this.arcRadius = builder.arcRadius;
    this.holdingPatternName = builder.holdingPatternName;
    this.holdingSpeed = builder.holdingSpeed;
    this.holdingTime = builder.holdingTime;
    this.holdingDistance = builder.holdingDistance;
    this.inboundHoldingCourseValue = builder.inboundHoldingCourseValue;
    this.inboundHoldingCourseIsTrue = builder.inboundHoldingCourseIsTrue;
    this.legInboundOutboundIndicator = builder.legInboundOutboundIndicator;
    this.holdMinAltitude = builder.holdMinAltitude;
    this.holdMaxAltitude = builder.holdMaxAltitude;
    this.rnp = builder.rnp;
    this.rvsmMinAltitude = builder.rvsmMinAltitude;
    this.rvsmMaxAltitude = builder.rvsmMaxAltitude;
    this.turnDirection = builder.turnDirection;
    this.verticalScaleFactor = builder.verticalScaleFactor;
    this.fixIdentifier = builder.fixIdentifier;
    this.fixRef = builder.fixRef;
    this.portRef = builder.portRef;
    this.isOnUndefined = builder.isOnUndefined;
    this.isOnHigh = builder.isOnHigh;
    this.isOnLow = builder.isOnLow;
    this.isOnSid = builder.isOnSid;
    this.isOnStar = builder.isOnStar;
    this.isOnApproach = builder.isOnApproach;
    this.isOnMissedApproach = builder.isOnMissedApproach;
    this.multipleIndicator = builder.multipleIndicator;
    this.inboundCourseNavaid = builder.inboundCourseNavaid;
    this.inboundCourseTheta = builder.inboundCourseTheta;
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

  public Optional<Boolean> isEnroute() {
    return Optional.ofNullable(isEnroute);
  }

  public Optional<Double> arcRadius() {
    return Optional.ofNullable(arcRadius);
  }

  public Optional<String> holdingPatternName() {
    return Optional.ofNullable(holdingPatternName);
  }

  public Optional<Long> holdingSpeed() {
    return Optional.ofNullable(holdingSpeed);
  }

  public Optional<String> holdingTime() {
    return Optional.ofNullable(holdingTime);
  }

  public Optional<Double> holdingDistance() {
    return Optional.ofNullable(holdingDistance);
  }

  public Optional<Double> inboundHoldingCourseValue() {
    return Optional.ofNullable(inboundHoldingCourseValue);
  }

  public Optional<Boolean> inboundHoldingCourseIsTrue() {
    return Optional.ofNullable(inboundHoldingCourseIsTrue);
  }

  public Optional<String> legInboundOutboundIndicator() {
    return Optional.ofNullable(legInboundOutboundIndicator);
  }

  public Optional<Integer> holdMinAltitude() {
    return Optional.ofNullable(holdMinAltitude);
  }

  public Optional<Integer> holdMaxAltitude() {
    return Optional.ofNullable(holdMaxAltitude);
  }

  public Optional<Double> rnp() {
    return Optional.ofNullable(rnp);
  }

  public Optional<Integer> rvsmMinAltitude() {
    return Optional.ofNullable(rvsmMinAltitude);
  }

  public Optional<Integer> rvsmMaxAltitude() {
    return Optional.ofNullable(rvsmMaxAltitude);
  }

  public Optional<String> turnDirection() {
    return Optional.ofNullable(turnDirection);
  }

  public Optional<Long> verticalScaleFactor() {
    return Optional.ofNullable(verticalScaleFactor);
  }

  public Optional<String> fixIdentifier() {
    return Optional.ofNullable(fixIdentifier);
  }

  public Optional<String> fixRef() {
    return Optional.ofNullable(fixRef);
  }

  public Optional<String> portRef() {
    return Optional.ofNullable(portRef);
  }

  public Optional<Boolean> isOnUndefined() {
    return Optional.ofNullable(isOnUndefined);
  }

  public Optional<Boolean> isOnHigh() {
    return Optional.ofNullable(isOnHigh);
  }

  public Optional<Boolean> isOnLow() {
    return Optional.ofNullable(isOnLow);
  }

  public Optional<Boolean> isOnSid() {
    return Optional.ofNullable(isOnSid);
  }

  public Optional<Boolean> isOnStar() {
    return Optional.ofNullable(isOnStar);
  }

  public Optional<Boolean> isOnApproach() {
    return Optional.ofNullable(isOnApproach);
  }

  public Optional<Boolean> isOnMissedApproach() {
    return Optional.ofNullable(isOnMissedApproach);
  }

  public Optional<Long> multipleIndicator() {
    return Optional.ofNullable(multipleIndicator);
  }

  public Optional<String> inboundCourseNavaid() {
    return Optional.ofNullable(inboundCourseNavaid);
  }

  public Optional<Double> inboundCourseTheta() {
    return Optional.ofNullable(inboundCourseTheta);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    ArincHoldingPattern that = (ArincHoldingPattern) o;
    return Objects.equals(baseInfo, that.baseInfo)
        && Objects.equals(recordInfo, that.recordInfo)
        && Objects.equals(isEnroute, that.isEnroute)
        && Objects.equals(arcRadius, that.arcRadius)
        && Objects.equals(holdingPatternName, that.holdingPatternName)
        && Objects.equals(holdingSpeed, that.holdingSpeed)
        && Objects.equals(holdingTime, that.holdingTime)
        && Objects.equals(holdingDistance, that.holdingDistance)
        && Objects.equals(inboundHoldingCourseValue, that.inboundHoldingCourseValue)
        && Objects.equals(inboundHoldingCourseIsTrue, that.inboundHoldingCourseIsTrue)
        && Objects.equals(legInboundOutboundIndicator, that.legInboundOutboundIndicator)
        && Objects.equals(holdMinAltitude, that.holdMinAltitude)
        && Objects.equals(holdMaxAltitude, that.holdMaxAltitude)
        && Objects.equals(rnp, that.rnp)
        && Objects.equals(rvsmMinAltitude, that.rvsmMinAltitude)
        && Objects.equals(rvsmMaxAltitude, that.rvsmMaxAltitude)
        && Objects.equals(turnDirection, that.turnDirection)
        && Objects.equals(verticalScaleFactor, that.verticalScaleFactor)
        && Objects.equals(fixIdentifier, that.fixIdentifier)
        && Objects.equals(fixRef, that.fixRef)
        && Objects.equals(portRef, that.portRef)
        && Objects.equals(isOnUndefined, that.isOnUndefined)
        && Objects.equals(isOnHigh, that.isOnHigh)
        && Objects.equals(isOnLow, that.isOnLow)
        && Objects.equals(isOnSid, that.isOnSid)
        && Objects.equals(isOnStar, that.isOnStar)
        && Objects.equals(isOnApproach, that.isOnApproach)
        && Objects.equals(isOnMissedApproach, that.isOnMissedApproach)
        && Objects.equals(multipleIndicator, that.multipleIndicator)
        && Objects.equals(inboundCourseNavaid, that.inboundCourseNavaid)
        && Objects.equals(inboundCourseTheta, that.inboundCourseTheta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        baseInfo,
        recordInfo,
        isEnroute,
        arcRadius,
        holdingPatternName,
        holdingSpeed,
        holdingTime,
        holdingDistance,
        inboundHoldingCourseValue,
        inboundHoldingCourseIsTrue,
        legInboundOutboundIndicator,
        holdMinAltitude,
        holdMaxAltitude,
        rnp,
        rvsmMinAltitude,
        rvsmMaxAltitude,
        turnDirection,
        verticalScaleFactor,
        fixIdentifier,
        fixRef,
        portRef,
        isOnUndefined,
        isOnHigh,
        isOnLow,
        isOnSid,
        isOnStar,
        isOnApproach,
        isOnMissedApproach,
        multipleIndicator,
        inboundCourseNavaid,
        inboundCourseTheta);
  }

  @Override
  public String toString() {
    return "ArincHoldingPattern{" +
        "baseInfo=" + baseInfo +
        ", recordInfo=" + recordInfo +
        ", isEnroute=" + isEnroute +
        ", arcRadius=" + arcRadius +
        ", holdingPatternName='" + holdingPatternName + '\'' +
        ", holdingSpeed=" + holdingSpeed +
        ", holdingTime='" + holdingTime + '\'' +
        ", holdingDistance=" + holdingDistance +
        ", inboundHoldingCourseValue=" + inboundHoldingCourseValue +
        ", inboundHoldingCourseIsTrue=" + inboundHoldingCourseIsTrue +
        ", legInboundOutboundIndicator='" + legInboundOutboundIndicator + '\'' +
        ", holdMinAltitude=" + holdMinAltitude +
        ", holdMaxAltitude=" + holdMaxAltitude +
        ", rnp=" + rnp +
        ", rvsmMinAltitude=" + rvsmMinAltitude +
        ", rvsmMaxAltitude=" + rvsmMaxAltitude +
        ", turnDirection='" + turnDirection + '\'' +
        ", verticalScaleFactor=" + verticalScaleFactor +
        ", fixIdentifier='" + fixIdentifier + '\'' +
        ", fixRef='" + fixRef + '\'' +
        ", portRef='" + portRef + '\'' +
        ", isOnUndefined=" + isOnUndefined +
        ", isOnHigh=" + isOnHigh +
        ", isOnLow=" + isOnLow +
        ", isOnSid=" + isOnSid +
        ", isOnStar=" + isOnStar +
        ", isOnApproach=" + isOnApproach +
        ", isOnMissedApproach=" + isOnMissedApproach +
        ", multipleIndicator=" + multipleIndicator +
        ", inboundCourseNavaid='" + inboundCourseNavaid + '\'' +
        ", inboundCourseTheta=" + inboundCourseTheta +
        '}';
  }

  public static final class Builder {
    private ArincBaseInfo baseInfo;
    private ArincRecordInfo recordInfo;
    private Boolean isEnroute;
    private Double arcRadius;
    private String holdingPatternName;
    private Long holdingSpeed;
    private String holdingTime;
    private Double holdingDistance;
    private Double inboundHoldingCourseValue;
    private Boolean inboundHoldingCourseIsTrue;
    private String legInboundOutboundIndicator;
    private Integer holdMinAltitude;
    private Integer holdMaxAltitude;
    private Double rnp;
    private Integer rvsmMinAltitude;
    private Integer rvsmMaxAltitude;
    private String turnDirection;
    private Long verticalScaleFactor;
    private String fixIdentifier;
    private String fixRef;
    private String portRef;
    private Boolean isOnUndefined;
    private Boolean isOnHigh;
    private Boolean isOnLow;
    private Boolean isOnSid;
    private Boolean isOnStar;
    private Boolean isOnApproach;
    private Boolean isOnMissedApproach;
    private Long multipleIndicator;
    private String inboundCourseNavaid;
    private Double inboundCourseTheta;

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

    public Builder isEnroute(Boolean isEnroute) {
      this.isEnroute = isEnroute;
      return this;
    }

    public Builder arcRadius(Double arcRadius) {
      this.arcRadius = arcRadius;
      return this;
    }

    public Builder holdingPatternName(String holdingPatternName) {
      this.holdingPatternName = holdingPatternName;
      return this;
    }

    public Builder holdingSpeed(Long holdingSpeed) {
      this.holdingSpeed = holdingSpeed;
      return this;
    }

    public Builder holdingTime(String holdingTime) {
      this.holdingTime = holdingTime;
      return this;
    }

    public Builder holdingDistance(Double holdingDistance) {
      this.holdingDistance = holdingDistance;
      return this;
    }

    public Builder inboundHoldingCourseValue(Double inboundHoldingCourseValue) {
      this.inboundHoldingCourseValue = inboundHoldingCourseValue;
      return this;
    }

    public Builder inboundHoldingCourseIsTrue(Boolean inboundHoldingCourseIsTrue) {
      this.inboundHoldingCourseIsTrue = inboundHoldingCourseIsTrue;
      return this;
    }

    public Builder legInboundOutboundIndicator(String legInboundOutboundIndicator) {
      this.legInboundOutboundIndicator = legInboundOutboundIndicator;
      return this;
    }

    public Builder holdMinAltitude(Integer holdMinAltitude) {
      this.holdMinAltitude = holdMinAltitude;
      return this;
    }

    public Builder holdMaxAltitude(Integer holdMaxAltitude) {
      this.holdMaxAltitude = holdMaxAltitude;
      return this;
    }

    public Builder rnp(Double rnp) {
      this.rnp = rnp;
      return this;
    }

    public Builder rvsmMinAltitude(Integer rvsmMinAltitude) {
      this.rvsmMinAltitude = rvsmMinAltitude;
      return this;
    }

    public Builder rvsmMaxAltitude(Integer rvsmMaxAltitude) {
      this.rvsmMaxAltitude = rvsmMaxAltitude;
      return this;
    }

    public Builder turnDirection(String turnDirection) {
      this.turnDirection = turnDirection;
      return this;
    }

    public Builder verticalScaleFactor(Long verticalScaleFactor) {
      this.verticalScaleFactor = verticalScaleFactor;
      return this;
    }

    public Builder fixIdentifier(String fixIdentifier) {
      this.fixIdentifier = fixIdentifier;
      return this;
    }

    public Builder fixRef(String fixRef) {
      this.fixRef = fixRef;
      return this;
    }

    public Builder portRef(String portRef) {
      this.portRef = portRef;
      return this;
    }

    public Builder isOnUndefined(Boolean isOnUndefined) {
      this.isOnUndefined = isOnUndefined;
      return this;
    }

    public Builder isOnHigh(Boolean isOnHigh) {
      this.isOnHigh = isOnHigh;
      return this;
    }

    public Builder isOnLow(Boolean isOnLow) {
      this.isOnLow = isOnLow;
      return this;
    }

    public Builder isOnSid(Boolean isOnSid) {
      this.isOnSid = isOnSid;
      return this;
    }

    public Builder isOnStar(Boolean isOnStar) {
      this.isOnStar = isOnStar;
      return this;
    }

    public Builder isOnApproach(Boolean isOnApproach) {
      this.isOnApproach = isOnApproach;
      return this;
    }

    public Builder isOnMissedApproach(Boolean isOnMissedApproach) {
      this.isOnMissedApproach = isOnMissedApproach;
      return this;
    }

    public Builder multipleIndicator(Long multipleIndicator) {
      this.multipleIndicator = multipleIndicator;
      return this;
    }

    public Builder inboundCourseNavaid(String inboundCourseNavaid) {
      this.inboundCourseNavaid = inboundCourseNavaid;
      return this;
    }

    public Builder inboundCourseTheta(Double inboundCourseTheta) {
      this.inboundCourseTheta = inboundCourseTheta;
      return this;
    }

    public ArincHoldingPattern build() {
      return new ArincHoldingPattern(this);
    }
  }
}
