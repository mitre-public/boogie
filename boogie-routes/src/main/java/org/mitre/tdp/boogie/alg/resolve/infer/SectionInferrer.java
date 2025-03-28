package org.mitre.tdp.boogie.alg.resolve.infer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.caasd.commons.collect.HashedLinkedSequence;
import org.mitre.tdp.boogie.CategoryAndType;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.resolve.RouteTokenResolver;
import org.mitre.tdp.boogie.util.Streams;

/**
 * A section "inferrer" considers pairs of {@link ResolvedTokens}s which were resolved by {@link RouteTokenResolver} implementations
 * from a provided route string and generates new "implied" sections between directly resolved ones.
 *
 * <p>Typically this is done to facility the generation/resolution of additional implied routing infrastructure beyond that which
 * is defined in a standard route string (e.g. arrival/departure runways, approach procedures, etc.).
 *
 * <p>Inferrer implementations often require additional information to make their inference, as such they typically aren't created
 * when instantiating an expander, they are implicitly configured based on optional context provided alongside the route string.
 */
@FunctionalInterface
public interface SectionInferrer {

  /**
   * Implementation of a {@link SectionInferrer} which infers the existence of no new {@link ResolvedTokens}s.
   */
  static SectionInferrer noop() {
    return (left, right) -> Collections.emptyList();
  }

  /**
   * Infers the specified SID (if available in the provided lookup service) if no SID was filed in the underlying route string.
   *
   * <p>This lookup is deferred (i.e. this method doesn't take a {@link Procedure}) for a couple of reasons:
   * <ol>
   *   <li>It allows for potentially incomplete infrastructure data (e.g. the SID doesn't actually exist)</li>
   *   <li>If there are multiple versions of the procedure serving a set of airports it allows us to defer choosing which airports
   *   implementation we choose until the first-pass of the expansion has been done</li>
   * </ol>
   *
   * @param proceduresByName lookup service providing procedures indexed by their identifier (e.g. {@code CHPPR1, GNDLF2})
   * @param sid              the identifier of the SID to use
   * @param categoryAndType the category and type for this expansion
   * @return the inferrer for this expansion
   */
  static SectionInferrer defaultSid(LookupService<Procedure> proceduresByName, String sid, CategoryAndType categoryAndType) {
    return new DefaultSidInferrer(proceduresByName, sid, categoryAndType);
  }

  /**
   * Infers the specified SID (if available in the provided lookup service) if no SID was filed in the underlying route string.
   *
   * <p>This lookup is deferred (i.e. this method doesn't take a {@link Procedure}) for a couple of reasons:
   * <ol>
   *   <li>It allows for potentially incomplete infrastructure data (e.g. the SID doesn't actually exist)</li>
   *   <li>If there are multiple versions of the procedure serving a set of airports it allows us to defer choosing which airports
   *   implementation we choose until the first-pass of the expansion has been done</li>
   * </ol>
   *
   * @param proceduresByName lookup service providing procedures indexed by their identifier (e.g. {@code CHPPR1, GNDLF2})
   * @param star             star identifier of the STAR to use
   * @param categoryAndType the category and type for this expansion
   * @return the inferrer for this expansion
   */
  static SectionInferrer defaultStar(LookupService<Procedure> proceduresByName, String star, CategoryAndType categoryAndType) {
    return new DefaultStarInferrer(proceduresByName, star, categoryAndType);
  }

  /**
   * When provided a target runway identifier (assumed to be the departure runway) will search for an appropriate departure runway
   * transition to use between the airport and the resolved SID.
   *
   * @param proceduresByName lookup service providing procedures indexed by their identifier (e.g. {@code CHPPR1, GNDLF2})
   * @param departureRunway  the identifier of the intended departure runway (e.g. {@code RW28L})
   * @param categoryAndType the category and type for this expansion
   * @return the inferrer for this expansion
   */
  static SectionInferrer sidRunwayTransition(LookupService<Procedure> proceduresByName, String departureRunway, CategoryAndType categoryAndType) {
    return new SidRunwayTransitionInferrer(proceduresByName, departureRunway, categoryAndType);
  }

  /**
   * When provided a target runway identifier (assumed to be the arrival runway) will search for an appropriate arrival runway
   * transition to use between the STAR and the arrival airport.
   *
   * @param proceduresByName lookup service providing procedures indexed by their identifier (e.g. {@code CHPPR1, GNDLF2})
   * @param arrivalRunway    the identifier of the intended departure runway (e.g. {@code RW28L})
   * @param categoryAndType the category and type for this expansion
   * @return the inferrer for this expansion
   */
  static SectionInferrer starRunwayTransition(LookupService<Procedure> proceduresByName, String arrivalRunway, CategoryAndType categoryAndType) {
    return new StarRunwayTransitionInferrer(proceduresByName, arrivalRunway, categoryAndType);
  }

  /**
   * When provided a target runway identifier (assumed to be the arrival runway) and set of {@link RequiredNavigationEquipage}
   * preferences will search for an appropriate approach procedure to get from the STAR onto the runway.
   *
   * @param proceduresAtAirport lookup service providing procedures indexed by the airport they serve (e.g. {@code KATL})
   * @param arrivalRunway       the arrival runway for the flight
   * @param preferredEquipages  equipages for the approach in preference order
   * @param categoryAndType the category and type for this expansion
   * @return the inferrer for this expansion
   */
  static SectionInferrer approach(LookupService<Procedure> proceduresAtAirport, String arrivalRunway, List<RequiredNavigationEquipage> preferredEquipages, CategoryAndType categoryAndType) {
    return new ApproachInferrer(arrivalRunway, preferredEquipages, proceduresAtAirport, categoryAndType);
  }

  /**
   * Generates a new <i>ordered</i> list of {@link ResolvedTokens}s based on the preceding and following provided sections.
   *
   * @param left  the preceding {@link ResolvedTokens} and its associated elements
   * @param right the following {@link ResolvedTokens} and its associated elements
   * @return the token between the left and right
   */
  List<ResolvedTokens> inferBetween(ResolvedTokens left, ResolvedTokens right);

  /**
   * Iterates through a list of {@link ResolvedTokens}s pairwise searching for places where the inferrer returns a new section
   * (or list of sections).
   *
   * <p>Generally intended to be used with {@link HashedLinkedSequence#insertAfter(Object, Object)}.
   *
   * @param sections the list of currently resolved sections we wish to infer across
   * @return a mapping from a section to what new sections were inferred to exist after it
   */
  default Map<ResolvedTokens, List<ResolvedTokens>> inferAcross(HashedLinkedSequence<ResolvedTokens> sections) {
    return Streams.pairwise(sections).collect(Collectors.toMap(Pair::first, pair -> this.inferBetween(pair.first(), pair.second())));
  }
}
