package org.mitre.boogie.xml.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;

public final class ArincAirwayLeg {
  private final ArincBaseInfo baseInfo;
  private final ArincRecordInfo recordInfo;
  // Leg fields
  private final long sequenceNumber;
  private final String fixRef;
  private final String fixIdent;
  private final String recNavaidIdent;
  private final String recNavaidRef;
  // AirwayLeg fields
  private final String cruiseTableRef;
  private final String airwayRouteType;
  private final String qualifier1;
  private final String qualifier2;
  private final String rnavPbnNavSpec;
  private final String rnpPbnNavSpec;
  private final String legDirectionRestriction;
  private final Double routeDistanceFrom;
  private final String euIndicator;
  private final Double fixRadiusTransitionIndicator;
  private final Double inboundCourseValue;
  private final Boolean inboundCourseIsTrue;
  private final Double outboundCourseValue;
  private final Boolean outboundCourseIsTrue;
  private final String level;
  private final Double rho;
  private final Double rnp;
  private final Double theta;
  private final Long verticalScaleFactor;
  private final Integer rvsmMinAltitude;
  private final Integer rvsmMaxAltitude;

  private ArincAirwayLeg(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.recordInfo = builder.recordInfo;
    this.sequenceNumber = builder.sequenceNumber;
    this.fixRef = builder.fixRef;
    this.fixIdent = builder.fixIdent;
    this.recNavaidIdent = builder.recNavaidIdent;
    this.recNavaidRef = builder.recNavaidRef;
    this.cruiseTableRef = builder.cruiseTableRef;
    this.airwayRouteType = builder.airwayRouteType;
    this.qualifier1 = builder.qualifier1;
    this.qualifier2 = builder.qualifier2;
    this.rnavPbnNavSpec = builder.rnavPbnNavSpec;
    this.rnpPbnNavSpec = builder.rnpPbnNavSpec;
    this.legDirectionRestriction = builder.legDirectionRestriction;
    this.routeDistanceFrom = builder.routeDistanceFrom;
    this.euIndicator = builder.euIndicator;
    this.fixRadiusTransitionIndicator = builder.fixRadiusTransitionIndicator;
    this.inboundCourseValue = builder.inboundCourseValue;
    this.inboundCourseIsTrue = builder.inboundCourseIsTrue;
    this.outboundCourseValue = builder.outboundCourseValue;
    this.outboundCourseIsTrue = builder.outboundCourseIsTrue;
    this.level = builder.level;
    this.rho = builder.rho;
    this.rnp = builder.rnp;
    this.theta = builder.theta;
    this.verticalScaleFactor = builder.verticalScaleFactor;
    this.rvsmMinAltitude = builder.rvsmMinAltitude;
    this.rvsmMaxAltitude = builder.rvsmMaxAltitude;
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

  public Optional<String> cruiseTableRef() {
    return Optional.ofNullable(cruiseTableRef);
  }

  public Optional<String> airwayRouteType() {
    return Optional.ofNullable(airwayRouteType);
  }

  public Optional<String> qualifier1() {
    return Optional.ofNullable(qualifier1);
  }

  public Optional<String> qualifier2() {
    return Optional.ofNullable(qualifier2);
  }

  public Optional<String> rnavPbnNavSpec() {
    return Optional.ofNullable(rnavPbnNavSpec);
  }

  public Optional<String> rnpPbnNavSpec() {
    return Optional.ofNullable(rnpPbnNavSpec);
  }

  public Optional<String> legDirectionRestriction() {
    return Optional.ofNullable(legDirectionRestriction);
  }

  public Optional<Double> routeDistanceFrom() {
    return Optional.ofNullable(routeDistanceFrom);
  }

  public Optional<String> euIndicator() {
    return Optional.ofNullable(euIndicator);
  }

  public Optional<Double> fixRadiusTransitionIndicator() {
    return Optional.ofNullable(fixRadiusTransitionIndicator);
  }

  public Optional<Double> inboundCourseValue() {
    return Optional.ofNullable(inboundCourseValue);
  }

  public Optional<Boolean> inboundCourseIsTrue() {
    return Optional.ofNullable(inboundCourseIsTrue);
  }

  public Optional<Double> outboundCourseValue() {
    return Optional.ofNullable(outboundCourseValue);
  }

  public Optional<Boolean> outboundCourseIsTrue() {
    return Optional.ofNullable(outboundCourseIsTrue);
  }

  public Optional<String> level() {
    return Optional.ofNullable(level);
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

  public Optional<Long> verticalScaleFactor() {
    return Optional.ofNullable(verticalScaleFactor);
  }

  public Optional<Integer> rvsmMinAltitude() {
    return Optional.ofNullable(rvsmMinAltitude);
  }

  public Optional<Integer> rvsmMaxAltitude() {
    return Optional.ofNullable(rvsmMaxAltitude);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincAirwayLeg that = (ArincAirwayLeg) o;
    return sequenceNumber == that.sequenceNumber && Objects.equals(recordInfo, that.recordInfo) && Objects.equals(fixIdent, that.fixIdent) && Objects.equals(airwayRouteType, that.airwayRouteType) && Objects.equals(level, that.level);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordInfo, sequenceNumber, fixIdent, airwayRouteType, level);
  }

  @Override
  public String toString() {
    return "ArincAirwayLeg{" +
        "sequenceNumber=" + sequenceNumber +
        ", fixIdent='" + fixIdent + '\'' +
        ", level='" + level + '\'' +
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
    private String cruiseTableRef;
    private String airwayRouteType;
    private String qualifier1;
    private String qualifier2;
    private String rnavPbnNavSpec;
    private String rnpPbnNavSpec;
    private String legDirectionRestriction;
    private Double routeDistanceFrom;
    private String euIndicator;
    private Double fixRadiusTransitionIndicator;
    private Double inboundCourseValue;
    private Boolean inboundCourseIsTrue;
    private Double outboundCourseValue;
    private Boolean outboundCourseIsTrue;
    private String level;
    private Double rho;
    private Double rnp;
    private Double theta;
    private Long verticalScaleFactor;
    private Integer rvsmMinAltitude;
    private Integer rvsmMaxAltitude;

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

    public Builder cruiseTableRef(String cruiseTableRef) {
      this.cruiseTableRef = cruiseTableRef;
      return this;
    }

    public Builder airwayRouteType(String airwayRouteType) {
      this.airwayRouteType = airwayRouteType;
      return this;
    }

    public Builder qualifier1(String qualifier1) {
      this.qualifier1 = qualifier1;
      return this;
    }

    public Builder qualifier2(String qualifier2) {
      this.qualifier2 = qualifier2;
      return this;
    }

    public Builder rnavPbnNavSpec(String rnavPbnNavSpec) {
      this.rnavPbnNavSpec = rnavPbnNavSpec;
      return this;
    }

    public Builder rnpPbnNavSpec(String rnpPbnNavSpec) {
      this.rnpPbnNavSpec = rnpPbnNavSpec;
      return this;
    }

    public Builder legDirectionRestriction(String legDirectionRestriction) {
      this.legDirectionRestriction = legDirectionRestriction;
      return this;
    }

    public Builder routeDistanceFrom(Double routeDistanceFrom) {
      this.routeDistanceFrom = routeDistanceFrom;
      return this;
    }

    public Builder euIndicator(String euIndicator) {
      this.euIndicator = euIndicator;
      return this;
    }

    public Builder fixRadiusTransitionIndicator(Double fixRadiusTransitionIndicator) {
      this.fixRadiusTransitionIndicator = fixRadiusTransitionIndicator;
      return this;
    }

    public Builder inboundCourseValue(Double inboundCourseValue) {
      this.inboundCourseValue = inboundCourseValue;
      return this;
    }

    public Builder inboundCourseIsTrue(Boolean inboundCourseIsTrue) {
      this.inboundCourseIsTrue = inboundCourseIsTrue;
      return this;
    }

    public Builder outboundCourseValue(Double outboundCourseValue) {
      this.outboundCourseValue = outboundCourseValue;
      return this;
    }

    public Builder outboundCourseIsTrue(Boolean outboundCourseIsTrue) {
      this.outboundCourseIsTrue = outboundCourseIsTrue;
      return this;
    }

    public Builder level(String level) {
      this.level = level;
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

    public Builder verticalScaleFactor(Long verticalScaleFactor) {
      this.verticalScaleFactor = verticalScaleFactor;
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

    public ArincAirwayLeg build() {
      return new ArincAirwayLeg(this);
    }
  }
}
