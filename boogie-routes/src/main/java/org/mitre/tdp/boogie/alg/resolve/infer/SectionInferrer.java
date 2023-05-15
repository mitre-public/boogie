package org.mitre.tdp.boogie.alg.resolve.infer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.caasd.commons.collect.HashedLinkedSequence;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.resolve.ResolvedElement;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.SectionResolver;
import org.mitre.tdp.boogie.util.Streams;

/**
 * A section "inferrer" considers pairs of {@link ResolvedSection}s which were resolved by {@link SectionResolver} implementations
 * from a provided route string and generates new "implied" sections between directly resolved ones.
 *
 * <p>Typically this is done to facility the generation/resolution of additional implied routing infrastructure beyond that which
 * is defined in a standard route string (e.g. arrival/departure runways, approach procedures, etc.).
 *
 * <p>TODO - {@link ResolvedElement}s need to have a cleaned-up identifier that allow them to be looked up again via service, i.e.
 * "the name you would find me as in a {@link LookupService}"
 *
 * <p>TODO - change expander signature to be {@code expand(String route, Context context)} where the context is
 *
 * <p>Other "inferrer" implementations to consider include:
 * <ol>
 *   <li>(Default) a SID if one wasn't specified in the flightplan (e.g. KATL..DRSDN)</li>
 *   <li>(Default) a STAR if one wasn't specified in the flightplan (e.g. SMAUG..KATL)</li>
 * </ol>
 *
 * <p>Inferrer implementations often require additional information to make their inference, as such they typically aren't created
 * when instantiating an expander, they are implicitly configured based on optional context provided alongside the route string.
 */
@FunctionalInterface
public interface SectionInferrer {

  /**
   * Implementation of a {@link SectionInferrer} which infers the existence of no new {@link ResolvedSection}s.
   */
  static SectionInferrer noop() {
    return (left, right) -> Collections.emptyList();
  }

  /**
   * Configures a {@link SectionInferrer} which always provides the given {@link Procedure} as the SID if one was omitted from
   * the underlying route string (or unable to be located in the infrastructure data).
   */
  static SectionInferrer defaultSid(Procedure sid) {
    return new DefaultSidInferrer(sid);
  }

  /**
   * Configures a {@link SectionInferrer} which always provides the given {@link Procedure} as the STAR if one was omitted from
   * the underlying route string (or unable to be located in the infrastructure data).
   */
  static SectionInferrer defaultStar(Procedure star) {
    return new DefaultStarInferrer(star);
  }

  /**
   * When provided a target runway identifier (assumed to be the departure runway) will search for an appropriate departure runway
   * transition to use between the airport and the resolved SID.
   *
   * @param proceduresByName lookup service providing procedures indexed by their identifier (e.g. {@code CHPPR1, GNDLF2})
   * @param departureRunway  the identifier of the intended departure runway (e.g. {@code RW28L})
   */
  static SectionInferrer sidRunwayTransition(LookupService<Procedure> proceduresByName, String departureRunway) {
    return new SidRunwayTransitionInferrer(proceduresByName, departureRunway);
  }

  /**
   * When provided a target runway identifier (assumed to be the arrival runway) will search for an appropriate arrival runway
   * transition to use between the STAR and the arrival airport.
   *
   * @param proceduresByName lookup service providing procedures indexed by their identifier (e.g. {@code CHPPR1, GNDLF2})
   * @param arrivalRunway    the identifier of the intended departure runway (e.g. {@code RW28L})
   */
  static SectionInferrer starRunwayTransition(LookupService<Procedure> proceduresByName, String arrivalRunway) {
    return new StarRunwayTransitionInferrer(proceduresByName, arrivalRunway);
  }

  /**
   * When provided a target runway identifier (assumed to be the arrival runway) and set of {@link RequiredNavigationEquipage}
   * preferences will search for an appropriate approach procedure to get from the STAR onto the runway.
   *
   * @param proceduresAtAirport lookup service providing procedures indexed by the airport they serve (e.g. {@code KATL})
   * @param arrivalRunway       the arrival runway for the flight
   * @param preferredEquipages  equipages for the approach in preference order
   */
  static SectionInferrer approach(LookupService<Procedure> proceduresAtAirport, String arrivalRunway, List<RequiredNavigationEquipage> preferredEquipages) {
    return new ApproachInferrer(arrivalRunway, preferredEquipages, proceduresAtAirport);
  }

  /**
   * Generates a new <i>ordered</i> list of {@link ResolvedSection}s based on the preceding and following provided sections.
   *
   * @param left  the preceding {@link ResolvedSection} and its associated elements
   * @param right the following {@link ResolvedSection} and its associated elements
   */
  List<ResolvedSection> inferBetween(ResolvedSection left, ResolvedSection right);

  /**
   * Iterates through a list of {@link ResolvedSection}s pairwise searching for places where the inferrer returns a new section
   * (or list of sections).
   *
   * <p>Generally intended to be used with {@link HashedLinkedSequence#insertAfter(Object, Object)}.
   *
   * @param sections the list of currently resolved sections we wish to infer across
   * @return a mapping from a section to what new sections were inferred to exist after it
   */
  default Map<ResolvedSection, List<ResolvedSection>> inferAcross(HashedLinkedSequence<ResolvedSection> sections) {
    return Streams.pairwise(sections).collect(Collectors.toMap(Pair::first, pair -> this.inferBetween(pair.first(), pair.second())));
  }
}
