package org.mitre.tdp.boogie.alg.facade;

import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.RequiredNavigationEquipage;

/**
 * Wrapper class for the basic set additional features the {@link FluentRouteExpander} allows for specifying alongside the route
 * string.
 *
 * <p>This class provides a convenient place to tag on additional configuration parameters to toggle fluent expander functionality
 * without sacrificing API evolvability.
 */
public final class RouteDetails {

  private final String departureRunway;

  private final String arrivalRunway;

  private final List<RequiredNavigationEquipage> equipagePreference;

  private RouteDetails(Builder builder) {
    this.departureRunway = builder.departureRunway;
    this.arrivalRunway = builder.arrivalRunway;
    this.equipagePreference = builder.equipagePreference;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Optional<String> departureRunway() {
    return ofNullable(departureRunway);
  }

  public Optional<String> arrivalRunway() {
    return ofNullable(arrivalRunway);
  }

  public List<RequiredNavigationEquipage> equipagePreference() {
    return equipagePreference;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RouteDetails that = (RouteDetails) o;
    return Objects.equals(departureRunway, that.departureRunway)
        && Objects.equals(arrivalRunway, that.arrivalRunway)
        && Objects.equals(equipagePreference, that.equipagePreference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(departureRunway, arrivalRunway, equipagePreference);
  }

  @Override
  public String toString() {
    return "RouteDetails{" +
        "departureRunway='" + departureRunway + '\'' +
        ", arrivalRunway='" + arrivalRunway + '\'' +
        ", equipagePreference=" + equipagePreference +
        '}';
  }

  public static final class Builder {

    private String departureRunway;

    private String arrivalRunway;

    private List<RequiredNavigationEquipage> equipagePreference;

    private Builder() {
    }

    /**
     * @param departureRunway identifier for the departure runway the flight will be taking off from, e.g. {@code RW27L}
     */
    public Builder departureRunway(String departureRunway) {
      this.departureRunway = departureRunway;
      return this;
    }

    /**
     * @param arrivalRunway identifier for the arrival runway the flight will be arriving to, e.g. {@code RW28R}
     */
    public Builder arrivalRunway(String arrivalRunway) {
      this.arrivalRunway = arrivalRunway;
      return this;
    }

    /**
     * See {@link #equipagePreference(List)}.
     */
    public Builder equipagePreference(RequiredNavigationEquipage... equipagePreference) {
      return equipagePreference(equipagePreference == null ? List.of() : List.of(equipagePreference));
    }

    /**
     * Sets the equipage preferences for the flight. When provided alongside an arrival runway, these will be used to assign a
     * preferred approach procedure during expansion.
     *
     * @param equipagePreference ordered list of preferred equipage of approach to use.
     */
    public Builder equipagePreference(List<RequiredNavigationEquipage> equipagePreference) {
      this.equipagePreference = equipagePreference;
      return this;
    }

    public RouteDetails build() {
      return new RouteDetails(this);
    }
  }
}
