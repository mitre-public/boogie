package org.mitre.tdp.boogie.alg;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedLeg;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.split.Wildcard;

import com.google.common.collect.Range;

/**
 * Container class for a {@link RouteSummary} (if one was generated) and a leg sequence as resolved by the {@link RouteExpander}.
 */
public final class ExpandedRoute implements Serializable {

  /**
   * See {@link RouteSummary}.
   */
  private final RouteSummary routeSummary;
  private final List<ExpandedRouteLeg> legs;

  public ExpandedRoute(@Nullable RouteSummary routeSummary, List<ResolvedLeg> resolvedLegs) {
    this.routeSummary = routeSummary;
    this.legs = resolvedLegs.stream()
        .map(resolvedLeg -> new ExpandedRouteLeg(
            resolvedLeg.split().value(),
            ElementType.fromResolvedElement(resolvedLeg.sourceElement()),
            resolvedLeg.split().wildcards(),
            resolvedLeg.leg())
        ).collect(Collectors.toList());
  }

  public Optional<RouteSummary> routeSummary() {
    return Optional.ofNullable(routeSummary);
  }

  public List<ExpandedRouteLeg> legs() {
    return legs;
  }

  public static final class ExpandedRouteLeg implements Serializable, Leg {

    /**
     * The section of the route string that was used to resolve this leg.
     */
    private final String section;
    private final ElementType elementType;
    /**
     * Any of the {@link Wildcard}s from the {@link SectionSplit#wildcards()} characters.
     */
    private final String wildcards;
    /**
     * The leg as extracted from that element of the route.
     */
    private final Leg leg;

    private ExpandedRouteLeg(String section, ElementType elementType, String wildcards, Leg leg) {
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

    public String wildcards() {
      return wildcards;
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
  }
}
