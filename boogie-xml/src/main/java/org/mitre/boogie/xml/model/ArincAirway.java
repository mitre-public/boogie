package org.mitre.boogie.xml.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;

public final class ArincAirway {
  private final ArincBaseInfo baseInfo;
  private final String identifier;
  private final String recordType;
  private final String customerCode;
  private final String airwayRouteType;
  private final String qualifier1;
  private final String qualifier2;
  private final String rnavPbnNavSpec;
  private final String rnpPbnNavSpec;
  private final List<ArincAirwayLeg> legs;

  private ArincAirway(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.identifier = builder.identifier;
    this.recordType = builder.recordType;
    this.customerCode = builder.customerCode;
    this.airwayRouteType = builder.airwayRouteType;
    this.qualifier1 = builder.qualifier1;
    this.qualifier2 = builder.qualifier2;
    this.rnavPbnNavSpec = builder.rnavPbnNavSpec;
    this.rnpPbnNavSpec = builder.rnpPbnNavSpec;
    this.legs = builder.legs;
  }

  public static Builder builder() {
    return new Builder();
  }

  public ArincBaseInfo baseInfo() {
    return baseInfo;
  }

  public String identifier() {
    return identifier;
  }

  public Optional<String> recordType() {
    return Optional.ofNullable(recordType);
  }

  public Optional<String> customerCode() {
    return Optional.ofNullable(customerCode);
  }

  public String airwayRouteType() {
    return airwayRouteType;
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

  public List<ArincAirwayLeg> legs() {
    return legs == null ? List.of() : legs;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    ArincAirway that = (ArincAirway) o;
    return Objects.equals(baseInfo, that.baseInfo) && Objects.equals(identifier, that.identifier) && Objects.equals(recordType, that.recordType) && Objects.equals(customerCode, that.customerCode) && Objects.equals(airwayRouteType, that.airwayRouteType) && Objects.equals(qualifier1, that.qualifier1) && Objects.equals(qualifier2, that.qualifier2) && Objects.equals(rnavPbnNavSpec, that.rnavPbnNavSpec) && Objects.equals(rnpPbnNavSpec, that.rnpPbnNavSpec) && Objects.equals(legs, that.legs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, identifier, recordType, customerCode, airwayRouteType, qualifier1, qualifier2, rnavPbnNavSpec, rnpPbnNavSpec, legs);
  }

  @Override
  public String toString() {
    return "ArincAirway{" +
        "baseInfo=" + baseInfo +
        ", identifier='" + identifier + '\'' +
        ", recordType='" + recordType + '\'' +
        ", customerCode='" + customerCode + '\'' +
        ", airwayRouteType='" + airwayRouteType + '\'' +
        ", qualifier1='" + qualifier1 + '\'' +
        ", qualifier2='" + qualifier2 + '\'' +
        ", rnavPbnNavSpec='" + rnavPbnNavSpec + '\'' +
        ", rnpPbnNavSpec='" + rnpPbnNavSpec + '\'' +
        ", legs=" + legs +
        '}';
  }

  public static final class Builder {
    private ArincBaseInfo baseInfo;
    private String identifier;
    private String recordType;
    private String customerCode;
    private String airwayRouteType;
    private String qualifier1;
    private String qualifier2;
    private String rnavPbnNavSpec;
    private String rnpPbnNavSpec;
    private List<ArincAirwayLeg> legs;

    private Builder() {
    }

    public Builder baseInfo(ArincBaseInfo baseInfo) {
      this.baseInfo = baseInfo;
      return this;
    }

    public Builder identifier(String identifier) {
      this.identifier = identifier;
      return this;
    }

    public Builder recordType(String recordType) {
      this.recordType = recordType;
      return this;
    }

    public Builder customerCode(String customerCode) {
      this.customerCode = customerCode;
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

    public Builder legs(List<ArincAirwayLeg> legs) {
      this.legs = legs;
      return this;
    }

    public ArincAirway build() {
      return new ArincAirway(this);
    }
  }
}
