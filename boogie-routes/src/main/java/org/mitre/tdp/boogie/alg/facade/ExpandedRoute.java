package org.mitre.tdp.boogie.alg.facade;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;

/**
 * Container class for a {@link RouteSummary} (if one was generated) and a leg sequence as resolved by the {@link FluentRouteExpander}.
 */
public final class ExpandedRoute implements Serializable {

  /**
   * See {@link RouteSummary}.
   */
  private final RouteSummary routeSummary;
  private final List<ExpandedRouteLeg> legs;

  public ExpandedRoute(@Nullable RouteSummary routeSummary, List<ExpandedRouteLeg> expandedRouteLegs) {
    this.routeSummary = routeSummary;
    this.legs = requireNonNull(expandedRouteLegs);
  }

  public ExpandedRoute(Builder builder) {
    this(builder.routeSummary, builder.legs);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder toBuilder(ExpandedRoute route) {
    return builder().routeSummary(route.routeSummary).legs(route.legs);
  }

  public Optional<RouteSummary> routeSummary() {
    return Optional.ofNullable(routeSummary);
  }

  public List<ExpandedRouteLeg> legs() {
    return legs;
  }

  public ExpandedRoute postProcess(UnaryOperator<ExpandedRoute> postProcessor) {
    return postProcessor.apply(this);
  }

  public ExpandedRoute updateSummary(UnaryOperator<RouteSummary> summarizer) {
    return routeSummary().isPresent() ? new ExpandedRoute(summarizer.apply(routeSummary), legs) : this;
  }

  public static class Builder {

    private RouteSummary routeSummary = null;
    private List<ExpandedRouteLeg> legs = null;

    private Builder() {
    }

    public Builder routeSummary(RouteSummary routeSummary) {
      this.routeSummary = routeSummary;
      return this;
    }

    public Builder legs(List<ExpandedRouteLeg> legs) {
      this.legs = legs;
      return this;
    }

    public ExpandedRoute build() {
      return new ExpandedRoute(this);
    }
  }
}
