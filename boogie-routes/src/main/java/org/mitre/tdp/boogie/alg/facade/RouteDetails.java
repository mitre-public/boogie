package org.mitre.tdp.boogie.alg.facade;

import static java.util.Optional.ofNullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.CategoryAndType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;

/**
 * Wrapper class for the basic set additional features the {@link FluentRouteExpander} allows for specifying alongside the route
 * string.
 *
 * <p>This class provides a convenient place to tag on additional configuration parameters to toggle fluent expander functionality
 * without sacrificing API evolvability.
 */
public final class RouteDetails {

  /**
   * The name of the departure runway e.g., RW26
   */
  private final String departureRunway;

  /**
   * The name of the arrival runway e.g., RW34
   */
  private final String arrivalRunway;

  /**
   * The name of the sid to be used as a default if no sid is in the flight plan.
   */
  private final String defaultSid;

  /**
   * The name of the star to be used as a default if no star is in the flight plan.
   */
  private final String defaultStar;

  /**
   * The list of equipages we want to support for approach expansion
   */
  private final List<RequiredNavigationEquipage> equipagePreference;

  /**
   * The list of categories or types we want to support.
   */
  private final CategoryAndType categoryAndType;

  private RouteDetails(Builder builder) {
    this.departureRunway = builder.departureRunway;
    this.arrivalRunway = builder.arrivalRunway;
    this.equipagePreference = builder.equipagePreference;
    this.defaultSid = builder.defaultSid;
    this.defaultStar = builder.defaultStar;
    this.categoryAndType = builder.categoryAndType;
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
    return Optional.ofNullable(equipagePreference).orElse(Collections.emptyList());
  }

  public Optional<String> defaultSid() {
    return ofNullable(defaultSid);
  }

  public Optional<String> defaultStar() {
    return ofNullable(defaultStar);
  }

  public Optional<CategoryAndType> categoryAndType() {
    return ofNullable(categoryAndType);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    RouteDetails that = (RouteDetails) o;
    return Objects.equals(departureRunway, that.departureRunway) && Objects.equals(arrivalRunway, that.arrivalRunway)
        && Objects.equals(defaultSid, that.defaultSid) && Objects.equals(defaultStar, that.defaultStar)
        && Objects.equals(equipagePreference, that.equipagePreference)
        && Objects.equals(categoryAndType, that.categoryAndType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(departureRunway, arrivalRunway, defaultSid, defaultStar, equipagePreference, categoryAndType);
  }

  @Override
  public String
  toString() {
    return "RouteDetails{" +
        "departureRunway='" + departureRunway + '\'' +
        ", arrivalRunway='" + arrivalRunway + '\'' +
        ", defaultSid='" + defaultSid + '\'' +
        ", defaultStar='" + defaultStar + '\'' +
        ", equipagePreference=" + equipagePreference +
        ", categoryOrTypes=" + categoryAndType +
        '}';
  }

  public static final class Builder {

    private String departureRunway;

    private String arrivalRunway;

    private String defaultSid;

    private String defaultStar;

    private List<RequiredNavigationEquipage> equipagePreference;

    private CategoryAndType categoryAndType;

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

    public Builder defaultSid(String defaultSid) {
      this.defaultSid = defaultSid;
      return this;
    }

    public Builder defaultStar(String defaultStar) {
      this.defaultStar = defaultStar;
      return this;
    }

    /**
     * Sets the category or type preferences for the flight plan expansion.
     * @param categoryAndType the set of types that we want to capture
     * @return this builder
     */
    public Builder categoryOrTypes(CategoryAndType categoryAndType) {
      this.categoryAndType = categoryAndType;
      return this;
    }

    public RouteDetails build() {
      return new RouteDetails(this);
    }
  }
}
