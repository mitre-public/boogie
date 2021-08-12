package org.mitre.tdp.boogie.model;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.TurnDirection;

import com.google.common.collect.Range;

/**
 * Immutable, buildable implementation of the boogie {@link Leg} interface - provided for convenience.
 */
public final class BoogieLeg implements Leg {

  private final Fix associatedFix;
  private final Fix recommendedNavaid;
  private final Fix centerFix;
  private final PathTerminator pathTerminator;
  private final int sequenceNumber;
  private final Double outboundMagneticCourse;
  private final Double rho;
  private final Double theta;
  private final Double rnp;
  private final Double routeDistance;
  private final Duration holdTime;
  private final Double verticalAngle;
  private final Range<Double> speedConstraint;
  private final Range<Double> altitudeConstraint;
  private final TurnDirection turnDirection;
  private final boolean isFlyOverFix;
  private final boolean isPublishedHoldingFix;

  private BoogieLeg(Builder builder) {
    this.associatedFix = builder.associatedFix;
    this.recommendedNavaid = builder.recommendedNavaid;
    this.centerFix = builder.centerFix;
    this.pathTerminator = builder.pathTerminator;
    this.sequenceNumber = builder.sequenceNumber;
    this.outboundMagneticCourse = builder.outboundMagneticCourse;
    this.rho = builder.rho;
    this.theta = builder.theta;
    this.rnp = builder.rnp;
    this.routeDistance = builder.routeDistance;
    this.holdTime = builder.holdTime;
    this.verticalAngle = builder.verticalAngle;
    this.speedConstraint = builder.speedConstraint;
    this.altitudeConstraint = builder.altitudeConstraint;
    this.turnDirection = builder.turnDirection;
    this.isFlyOverFix = builder.isFlyOverFix;
    this.isPublishedHoldingFix = builder.isPublishedHoldingFix;
  }

  @Override
  public Optional<Fix> associatedFix() {
    return Optional.ofNullable(associatedFix);
  }

  @Override
  public Optional<Fix> recommendedNavaid() {
    return Optional.ofNullable(recommendedNavaid);
  }

  @Override
  public Optional<Fix> centerFix() {
    return Optional.ofNullable(centerFix);
  }

  @Override
  public PathTerminator pathTerminator() {
    return pathTerminator;
  }

  @Override
  public int sequenceNumber() {
    return sequenceNumber;
  }

  @Override
  public Optional<Double> outboundMagneticCourse() {
    return Optional.ofNullable(outboundMagneticCourse);
  }

  @Override
  public Optional<Double> rho() {
    return Optional.ofNullable(rho);
  }

  @Override
  public Optional<Double> theta() {
    return Optional.ofNullable(theta);
  }

  @Override
  public Optional<Double> rnp() {
    return Optional.ofNullable(rnp);
  }

  @Override
  public Optional<Double> routeDistance() {
    return Optional.ofNullable(routeDistance);
  }

  @Override
  public Optional<Duration> holdTime() {
    return Optional.ofNullable(holdTime);
  }

  @Override
  public Optional<Double> verticalAngle() {
    return Optional.ofNullable(verticalAngle);
  }

  @Override
  public Range<java.lang.Double> speedConstraint() {
    return speedConstraint;
  }

  @Override
  public Range<java.lang.Double> altitudeConstraint() {
    return altitudeConstraint;
  }

  @Override
  public Optional<TurnDirection> turnDirection() {
    return Optional.ofNullable(turnDirection);
  }

  @Override
  public boolean isFlyOverFix() {
    return isFlyOverFix;
  }

  @Override
  public boolean isPublishedHoldingFix() {
    return isPublishedHoldingFix;
  }

  public Builder toBuilder() {
    return new Builder()
        .associatedFix(associatedFix().orElse(null))
        .recommendedNavaid(recommendedNavaid().orElse(null))
        .centerFix(centerFix().orElse(null))
        .pathTerminator(pathTerminator())
        .sequenceNumber(sequenceNumber())
        .outboundMagneticCourse(outboundMagneticCourse().orElse(null))
        .rho(rho().orElse(null))
        .theta(theta().orElse(null))
        .rnp(rnp().orElse(null))
        .routeDistance(routeDistance().orElse(null))
        .holdTime(holdTime().orElse(null))
        .verticalAngle(verticalAngle().orElse(null))
        .speedConstraint(speedConstraint())
        .altitudeConstraint(altitudeConstraint())
        .turnDirection(turnDirection().orElse(null))
        .isFlyOverFix(isFlyOverFix())
        .isPublishedHoldingFix(isPublishedHoldingFix());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoogieLeg boogieLeg = (BoogieLeg) o;
    return sequenceNumber == boogieLeg.sequenceNumber &&
        isFlyOverFix == boogieLeg.isFlyOverFix &&
        isPublishedHoldingFix == boogieLeg.isPublishedHoldingFix &&
        Objects.equals(associatedFix, boogieLeg.associatedFix) &&
        Objects.equals(recommendedNavaid, boogieLeg.recommendedNavaid) &&
        Objects.equals(centerFix, boogieLeg.centerFix) &&
        pathTerminator == boogieLeg.pathTerminator &&
        Objects.equals(outboundMagneticCourse, boogieLeg.outboundMagneticCourse) &&
        Objects.equals(rho, boogieLeg.rho) &&
        Objects.equals(theta, boogieLeg.theta) &&
        Objects.equals(rnp, boogieLeg.rnp) &&
        Objects.equals(routeDistance, boogieLeg.routeDistance) &&
        Objects.equals(holdTime, boogieLeg.holdTime) &&
        Objects.equals(verticalAngle, boogieLeg.verticalAngle) &&
        Objects.equals(speedConstraint, boogieLeg.speedConstraint) &&
        Objects.equals(altitudeConstraint, boogieLeg.altitudeConstraint) &&
        Objects.equals(turnDirection, boogieLeg.turnDirection);
  }

  @Override
  public int hashCode() {
    return Objects.hash(associatedFix, recommendedNavaid, centerFix, pathTerminator, sequenceNumber, outboundMagneticCourse, rho, theta, rnp, routeDistance, holdTime, verticalAngle, speedConstraint, altitudeConstraint, turnDirection, isFlyOverFix, isPublishedHoldingFix);
  }

  @Override
  public String toString() {
    return "BoogieLeg{" +
        "associatedFix=" + associatedFix +
        ", recommendedNavaid=" + recommendedNavaid +
        ", centerFix=" + centerFix +
        ", pathTerminator=" + pathTerminator +
        ", sequenceNumber=" + sequenceNumber +
        ", outboundMagneticCourse=" + outboundMagneticCourse +
        ", rho=" + rho +
        ", theta=" + theta +
        ", rnp=" + rnp +
        ", routeDistance=" + routeDistance +
        ", holdTime=" + holdTime +
        ", verticalAngle=" + verticalAngle +
        ", speedConstraint=" + speedConstraint +
        ", altitudeConstraint=" + altitudeConstraint +
        ", turnDirection=" + turnDirection +
        ", isFlyOverFix=" + isFlyOverFix +
        ", isPublishedHoldingFix=" + isPublishedHoldingFix +
        '}';
  }

  public static final class Builder {
    private Fix associatedFix;
    private Fix recommendedNavaid;
    private Fix centerFix;
    private PathTerminator pathTerminator;
    private int sequenceNumber;
    private Double outboundMagneticCourse;
    private Double rho;
    private Double theta;
    private Double rnp;
    private Double routeDistance;
    private Duration holdTime;
    private Double verticalAngle;
    private Range<java.lang.Double> speedConstraint;
    private Range<java.lang.Double> altitudeConstraint;
    private TurnDirection turnDirection;
    private boolean isFlyOverFix;
    private boolean isPublishedHoldingFix;

    public Builder associatedFix(Fix associatedFix) {
      this.associatedFix = associatedFix;
      return this;
    }

    public Builder recommendedNavaid(Fix recommendedNavaid) {
      this.recommendedNavaid = recommendedNavaid;
      return this;
    }

    public Builder centerFix(Fix centerFix) {
      this.centerFix = centerFix;
      return this;
    }

    public Builder pathTerminator(PathTerminator pathTerminator) {
      this.pathTerminator = pathTerminator;
      return this;
    }

    public Builder sequenceNumber(int sequenceNumber) {
      this.sequenceNumber = sequenceNumber;
      return this;
    }

    public Builder outboundMagneticCourse(Double outboundMagneticCourse) {
      this.outboundMagneticCourse = outboundMagneticCourse;
      return this;
    }

    public Builder rho(Double rho) {
      this.rho = rho;
      return this;
    }

    public Builder theta(Double theta) {
      this.theta = theta;
      return this;
    }

    public Builder rnp(Double rnp) {
      this.rnp = rnp;
      return this;
    }

    public Builder routeDistance(Double routeDistance) {
      this.routeDistance = routeDistance;
      return this;
    }

    public Builder holdTime(Duration holdTime) {
      this.holdTime = holdTime;
      return this;
    }

    public Builder verticalAngle(Double verticalAngle) {
      this.verticalAngle = verticalAngle;
      return this;
    }

    public Builder speedConstraint(Range<java.lang.Double> speedConstraint) {
      this.speedConstraint = speedConstraint;
      return this;
    }

    public Builder altitudeConstraint(Range<java.lang.Double> altitudeConstraint) {
      this.altitudeConstraint = altitudeConstraint;
      return this;
    }

    public Builder turnDirection(TurnDirection turnDirection) {
      this.turnDirection = turnDirection;
      return this;
    }

    public Builder isFlyOverFix(boolean isFlyOverFix) {
      this.isFlyOverFix = isFlyOverFix;
      return this;
    }

    public Builder isPublishedHoldingFix(boolean isPublishedHoldingFix) {
      this.isPublishedHoldingFix = isPublishedHoldingFix;
      return this;
    }

    public BoogieLeg build() {
      return new BoogieLeg(this);
    }
  }
}
