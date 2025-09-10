package org.mitre.tdp.boogie.alg.facade;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.alg.split.Wildcard;

import com.google.common.collect.Range;

public final class ExpandedRouteLeg implements Serializable, Leg {

  /**
   * The section of the route string that was used to resolve this leg.
   */
  private final String section;
  /**
   * A higher-level semantic tag for the type of {@link ResolvedToken} that the given expanded leg came from.
   */
  private final ElementType elementType;
  /**
   * Any of the {@link Wildcard}s from providing {@link RouteToken}s
   */
  private final String wildcards;
  /**
   * The leg as extracted from that element of the route.
   */
  private final Leg leg;

  public ExpandedRouteLeg(String section, ElementType elementType, String wildcards, Leg leg) {
    this.section = requireNonNull(section);
    this.elementType = requireNonNull(elementType);
    this.wildcards = requireNonNull(wildcards);
    this.leg = requireNonNull(leg);
  }

  public String section() {
    return section;
  }

  public ElementType elementType() {
    return elementType;
  }

  /**
   * Holdover from earlier version of the route expander, this will be removed in a future release.
   */
  public String wildcards() {
    return wildcards;
  }

  /**
   * Return the delegate leg.
   */
  public Leg leg() {
    return leg;
  }

  @Override
  public Optional<? extends Fix> associatedFix() {
    return leg.associatedFix();
  }

  @Override
  public Optional<? extends Fix> recommendedNavaid() {
    return leg.recommendedNavaid();
  }

  @Override
  public Optional<? extends Fix> centerFix() {
    return leg.centerFix();
  }

  @Override
  public PathTerminator pathTerminator() {
    return leg.pathTerminator();
  }

  @Override
  public int sequenceNumber() {
    return leg.sequenceNumber();
  }

  @Override
  public Optional<Double> outboundMagneticCourse() {
    return leg.outboundMagneticCourse();
  }

  @Override
  public Optional<Double> rho() {
    return leg.rho();
  }

  @Override
  public Optional<Double> theta() {
    return leg.theta();
  }

  @Override
  public Optional<Double> rnp() {
    return leg.rnp();
  }

  @Override
  public Optional<Double> routeDistance() {
    return leg.routeDistance();
  }

  @Override
  public Optional<Duration> holdTime() {
    return leg.holdTime();
  }

  @Override
  public Optional<Double> verticalAngle() {
    return leg.verticalAngle();
  }

  @Override
  public Range<Double> speedConstraint() {
    return leg.speedConstraint();
  }

  @Override
  public Range<Double> altitudeConstraint() {
    return leg.altitudeConstraint();
  }

  @Override
  public Optional<TurnDirection> turnDirection() {
    return leg.turnDirection();
  }

  @Override
  public boolean isFlyOverFix() {
    return leg.isFlyOverFix();
  }

  @Override
  public boolean isPublishedHoldingFix() {
    return leg.isPublishedHoldingFix();
  }

  @Override
  public Optional<Double> arcRadius() {
    return leg.arcRadius();
  }

  @Override
  public void accept(Visitor visitor) {
    leg.accept(visitor);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExpandedRouteLeg that = (ExpandedRouteLeg) o;
    return Objects.equals(section, that.section) &&
        elementType == that.elementType &&
        Objects.equals(wildcards, that.wildcards) &&
        Objects.equals(leg, that.leg);
  }

  @Override
  public int hashCode() {
    return Objects.hash(section, elementType, wildcards, leg);
  }

  @Override
  public String toString() {
    return "ExpandedRouteLeg{" +
        "section='" + section + '\'' +
        ", elementType=" + elementType +
        ", wildcards='" + wildcards + '\'' +
        ", leg=" + leg +
        '}';
  }
}
