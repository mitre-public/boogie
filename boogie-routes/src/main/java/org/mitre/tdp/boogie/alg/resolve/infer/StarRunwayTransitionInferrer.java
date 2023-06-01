package org.mitre.tdp.boogie.alg.resolve.infer;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;

final class StarRunwayTransitionInferrer implements SectionInferrer {

  private final LookupService<Procedure> proceduresByName;

  private final String arrivalRunway;

  StarRunwayTransitionInferrer(LookupService<Procedure> proceduresByName, String arrivalRunway) {
    this.proceduresByName = requireNonNull(proceduresByName);
    this.arrivalRunway = arrivalRunway;
  }

  @Override
  public List<ResolvedTokens> inferBetween(ResolvedTokens left, ResolvedTokens right) {

    Optional<Procedure> star = left.resolvedTokens().stream()
        .flatMap(t -> ResolvedTokenVisitor.star(t).stream())
        .findFirst();

    Optional<Airport> airport = right.resolvedTokens().stream()
        .flatMap(t -> ResolvedTokenVisitor.airport(t).stream())
        .findFirst();

    return star.isPresent() && airport.isPresent()
        ? List.of(new ResolvedTokens(left.routeToken(), runwayTransitionElements(star.get(), airport.get())))
        : List.of();
  }

  private Collection<ResolvedToken> runwayTransitionElements(Procedure star, Airport airport) {

    Collection<Procedure> procedures = PreferProceduresAtAirport.lookup(proceduresByName, star.procedureIdentifier(), airport.airportIdentifier());

    return procedures.stream()
        .map(procedure -> Procedure.maskTransitions(procedure, nonArrivalRunwayTransitionFilter().negate()))
        .map(ResolvedToken::starRunway)
        .collect(toList());
  }

  /**
   * Constructs a filter that will drop all runway transitions who's identifiers don't match the arrival runway.
   */
  Predicate<Transition> nonArrivalRunwayTransitionFilter() {
    return arrivalRunway == null ? t -> false : new RunwayTransitionFilter(arrivalRunway).or(t -> !TransitionType.RUNWAY.equals(t.transitionType()));
  }
}
