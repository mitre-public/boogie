package org.mitre.tdp.boogie.alg;

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;

/**
 * Class encapsulating required and option input parameters for expansion of a single route in via a {@link RouteExpander2}.
 */
public final class RouteDetails {

  private final String route;

  private final String departureRunway;

  private final String arrivalRunway;

  private final List<RequiredNavigationEquipage> preferredEquipage;

  private RouteDetails(Builder builder) {
    this.route = requireNonNull(builder.route);
    this.departureRunway = requireNonNull(builder.departureRunway);
    this.arrivalRunway = requireNonNull(builder.arrivalRunway);
    this.preferredEquipage = requireNonNull(builder.preferredEquipage);
  }

  public static Builder builder() {
    return new org.mitre.tdp.boogie.alg.RouteDetails.Builder();
  }

  public String route() {
    return route;
  }

  public String departureRunway() {
    return departureRunway;
  }

  public String arrivalRunway() {
    return arrivalRunway;
  }

  public List<RequiredNavigationEquipage> preferredEquipage() {
    return preferredEquipage;
  }

  public Builder toBuilder() {
    return builder()
        .route(route())
        .departureRunway(departureRunway())
        .arrivalRunway(arrivalRunway())
        .preferredEquipage(preferredEquipage());
  }

  public static final class Builder {
    private String route;

    private String departureRunway;

    private String arrivalRunway;

    private List<RequiredNavigationEquipage> preferredEquipage;

    private Builder() {
    }

    public RouteDetails build() {
      return new org.mitre.tdp.boogie.alg.RouteDetails(this);
    }

    /**
     * Set the route of flight to use in the expansion, format will depend on the {@link RouteExpander2}s {@link SectionSplitter}
     * implementation.
     *
     * @param route the route to expand, SID/STAR expansion will start/stop at the beginning/end of the common portions of the
     *              procedures
     */
    public Builder route(String route) {
      this.route = route;
      return this;
    }

    /**
     * Set the departure runway to help select an appropriate SID runway transition to use. Otherwise, the expansion will start at
     * the start of the common portion of the SID.
     *
     * @param departureRunway the departure runway used, if provided the appropriate departure runway transition will be included
     *                        in the final expanded route (e.g. {@code RW18L}, {@code RW27}, etc.)
     */
    public Builder departureRunway(String departureRunway) {
      this.departureRunway = departureRunway;
      return this;
    }

    /**
     * Set the arrival runway to help select an appropriate STAR runway transition to use. Otherwise, the expansion will stop at
     * the end of the common portion of the STAR.
     *
     * @param arrivalRunway the arrival runway used, if provided the appropriate arrival runway transition will be included in the
     *                      final expanded route (e.g. {@code RW18L}, {@code RW27}, etc.)
     */
    public Builder arrivalRunway(String arrivalRunway) {
      this.arrivalRunway = arrivalRunway;
      return this;
    }

    /**
     * Set of equipage preferences used in tandem with the {@link #arrivalRunway(String)} to help select a preferred approach for
     * expansion across the runway threshold.
     *
     * @param preferredEquipage the equipage of the aircraft, if provided (along with an arrival runway) this will determine the
     *                          type of approach procedure included in the final expansion. The list represents the preference
     *                          order (e.g. RNP > RNAV), if the listing is incomplete (doesn't cover all options) procedures with
     *                          unlisted equipages will be ignored (not returned as candidates) and not be included in the final
     *                          expansion
     */
    public Builder preferredEquipage(List<RequiredNavigationEquipage> preferredEquipage) {
      this.preferredEquipage = preferredEquipage;
      return this;
    }
  }
}
