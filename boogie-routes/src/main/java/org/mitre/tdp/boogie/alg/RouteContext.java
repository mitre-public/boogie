package org.mitre.tdp.boogie.alg;

import static java.util.Optional.ofNullable;

import java.util.List;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.CategoryAndType;
import org.mitre.tdp.boogie.alg.resolve.infer.SectionInferrer;

/**
 * Route context is additional configuration information the expander can use scoped to a single route to help in the expansion
 * process.
 *
 * <p>Rather than providing a <i>data contract</i> in the interface layer this provides a <i>functional contract</i> which can be
 * decorated or implemented such that it feels like a data contract. This gives us a lot of flexibility to grow cleanly.
 *
 * <p>The {@link #standard()} implementation of route context is provided for convenience and to give clients an idea of how the
 * above may be implemented.
 */
public interface RouteContext {

  /**
   * Returns a new route context object containing no information - suitable for testing purposes or in applications which don't
   * require additional information beyond what can be encoded in their incoming route format.
   */
  static RouteContext none() {
    return new RouteContext() {
      @Override
      public List<SectionInferrer> inferrers() {
        return List.of();
      }

      @Override
      public CategoryAndType categoryAndType() {
        return null;
      }
    };
  }

  /**
   * Representation of common "standard" route context which is supplemental to the basic information normally encoded in a route
   * string but which may be relevant for route expansion.
   *
   * <p>This presents a <i>mostly</i> data-driven contract for route expansion that a client may expect.
   *
   * <p>Note that none of the fields are required for the expander to operator, though specifying one field may imply that another
   * is present.
   */
  static Standard standard() {
    return new Standard();
  }

  /**
   * Defines an ordered sequence of {@link SectionInferrer}s which will be used during the expansion to add context to the route
   * beyond that which is specified in the base route string.
   *
   * <p>Common examples are arrival/departure runway information. However, clients may wish to specify other additional info such
   * as default SID/STARs for various airports. See options on {@link SectionInferrer}.
   *
   * <p>This collection is ordered because inferrers are applied sequentially in a step-wise fashion. This means that the inferred
   * sections resolved by earlier inferrers are visible to later ones.
   */
  List<SectionInferrer> inferrers();

  /**
   * The route will need to be expanded within the context of the category and type of the aircraft flying
   * @return the cat/type
   */
  CategoryAndType categoryAndType();

  final class Standard {

    private LookupService<Procedure> proceduresByName;

    private String defaultSid;

    private String defaultStar;

    private String departureRunway;

    private String arrivalRunway;

    private LookupService<Procedure> proceduresByAirport;

    private List<RequiredNavigationEquipage> equipagePreference;

    private CategoryAndType categoryAndType;

    private Standard() {
    }

    /**
     * A {@link LookupService} which can be used to resolve procedures by their identifier. This is expected to be present when
     * supplying {@link #arrivalRunway(String)} or {@link #departureRunway(String)}.
     *
     * @param proceduresByName lookup service for procedures by their identifier, e.g. {@code CHPPR1} or {@code GNDLF2}
     * @return this builder
     */
    public Standard proceduresByName(LookupService<Procedure> proceduresByName) {
      this.proceduresByName = proceduresByName;
      return this;
    }

    /**
     * @param defaultSid identifier for a default SID to use if one was omitted from the route string (or no SID was resolved
     *                   from the configured infrastructure)
     * @return this builder
     */
    public Standard defaultSid(String defaultSid) {
      this.defaultSid = defaultSid;
      return this;
    }

    /**
     * @param departureRunway identifier for the departure runway the flight will be taking off from, e.g. {@code RW27L}
     * @return this  builder
     */
    public Standard departureRunway(String departureRunway) {
      this.departureRunway = departureRunway;
      return this;
    }

    /**
     * @param defaultStar identifier for a default STAR to use if one was omitted from the route string (or no STAR was resolved
     *                    from the configured infrastructure)
     * @return this builder
     */
    public Standard defaultStar(String defaultStar) {
      this.defaultStar = defaultStar;
      return this;
    }

    /**
     * @param arrivalRunway identifier for the arrival runway the flight will be arriving to, e.g. {@code RW28R}
     * @return this builder
     */
    public Standard arrivalRunway(String arrivalRunway) {
      this.arrivalRunway = arrivalRunway;
      return this;
    }

    /**
     * A {@link LookupService} which can be used to resolve the set of procedures available at a given airport. This is expected
     * to be present when using {@link #equipagePreference(List)}.
     *
     * @param proceduresByAirport lookup service for procedures at a given airport, e.g. {@code KATL} or {@code WSSS}
     * @return this builder
     */
    public Standard proceduresByAirport(LookupService<Procedure> proceduresByAirport) {
      this.proceduresByAirport = proceduresByAirport;
      return this;
    }

    /**
     * See {@link #equipagePreference(List)}.
     * @param equipagePreference the params
     * @return this builder
     */
    public Standard equipagePreference(RequiredNavigationEquipage... equipagePreference) {
      return equipagePreference(equipagePreference == null ? List.of() : List.of(equipagePreference));
    }

    /**
     * Sets the equipage preferences for the flight. When provided alongside an arrival runway, these will be used to assign a
     * preferred approach procedure during expansion.
     *
     * @param equipagePreference ordered list of preferred equipage of approach to use.
     * @return this builder
     */
    public Standard equipagePreference(List<RequiredNavigationEquipage> equipagePreference) {
      this.equipagePreference = equipagePreference;
      return this;
    }

    /**
     *  Sets the category and type of this context
     * @param categoryAndType the category/type information
     * @return this builder
     */
    public Standard categoryAndType(CategoryAndType categoryAndType) {
      this.categoryAndType = categoryAndType;
      return this;
    }

    /**
     * A section inferrer for the departing airport
     * @return the inferrer for the default star
     */
    private SectionInferrer defaultSid() {
      return ofNullable(defaultSid)
          .map(sid -> SectionInferrer.defaultSid(proceduresByName, sid, categoryAndType))
          .orElseGet(SectionInferrer::noop);
    }

    /**
     * The section interrer for the arrival airport
     * @return the inferrer for the default star
     */
    private SectionInferrer defaultStar() {
      return ofNullable(defaultStar)
          .map(star -> SectionInferrer.defaultStar(proceduresByName, star, categoryAndType))
          .orElseGet(SectionInferrer::noop);
    }

    /**
     * Infers the runway transition
     * @return inferrer
     */
    private SectionInferrer sidRunwayTransition() {
      return ofNullable(departureRunway)
          .map(runway -> SectionInferrer.sidRunwayTransition(proceduresByName, runway, categoryAndType))
          .orElseGet(SectionInferrer::noop);
    }

    /**
     * Infers the runway transition
     * @return inferrer
     */
    private SectionInferrer starRunwayTransition() {
      return ofNullable(arrivalRunway)
          .map(runway -> SectionInferrer.starRunwayTransition(proceduresByName, runway, categoryAndType))
          .orElseGet(SectionInferrer::noop);
    }

    /**
     * Infers the approach procedure
     * @return inferrer
     */
    private SectionInferrer approach() {
      return ofNullable(equipagePreference)
          .filter(e -> !e.isEmpty())
          .filter(e -> arrivalRunway != null)
          .map(equipages -> SectionInferrer.approach(proceduresByAirport, arrivalRunway, equipages, categoryAndType))
          .orElseGet(SectionInferrer::noop);
    }

    public RouteContext build() {
      return new RouteContext() {
        @Override
        public List<SectionInferrer> inferrers() {
          return List.of(defaultSid(), defaultStar(), sidRunwayTransition(), starRunwayTransition(), approach());
        }

        @Override
        public CategoryAndType categoryAndType() {
          return categoryAndType;
        }
      };
    }
  }
}
