package org.mitre.tdp.boogie.pathtermination;

import java.io.Serializable;

import org.mitre.caasd.commons.LatLong;

import com.google.common.collect.Range;

public final class PathAndTermination implements Serializable {
  private final LatLong startOfLeg;
  private final LatLong endOfLeg;
  private final Range<Double> startAltitudeFt;
  private final Range<Double> endAltitudeFt;
  private final Double lengthNm;

  private PathAndTermination(Builder builder) {
    this.startOfLeg = builder.startOfLeg;
    this.endOfLeg = builder.endOfLeg;
    this.startAltitudeFt = builder.startAltitudeFt;
    this.endAltitudeFt = builder.endAltitudeFt;
    this.lengthNm = builder.lengthNm;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .startOfLeg(startOfLeg)
        .endOfLeg(endOfLeg)
        .startAltitudeFt(startAltitudeFt)
        .endAltitudeFt(endAltitudeFt)
        .lengthNm(lengthNm);
  }

  public LatLong startOfLeg() {
    return startOfLeg;
  }

  public LatLong endOfLeg() {
    return endOfLeg;
  }

  public Range<Double> startAltitudeFt() {
    return startAltitudeFt;
  }

  public Range<Double> endAltitudeFt() {
    return endAltitudeFt;
  }

  public Double lengthNm() {
    return lengthNm;
  }

  public static class Builder {
    private LatLong startOfLeg;
    private LatLong endOfLeg;
    private Range<Double> startAltitudeFt;
    private Range<Double> endAltitudeFt;
    private Double lengthNm;
    private Builder() {}

    public Builder startOfLeg(LatLong startOfLeg) {
      this.startOfLeg = startOfLeg;
      return this;
    }

    public Builder endOfLeg(LatLong endOfLeg) {
      this.endOfLeg = endOfLeg;
      return this;
    }

    public Builder startAltitudeFt(Range<Double> startAltitudeFt) {
      this.startAltitudeFt = startAltitudeFt;
      return this;
    }

    public Builder endAltitudeFt(Range<Double> endAltitudeFt) {
      this.endAltitudeFt = endAltitudeFt;
      return this;
    }

    public Builder lengthNm(Double lengthNm) {
      this.lengthNm = lengthNm;
      return this;
    }

    public PathAndTermination build() {
      return new PathAndTermination(this);
    }
  }
}
