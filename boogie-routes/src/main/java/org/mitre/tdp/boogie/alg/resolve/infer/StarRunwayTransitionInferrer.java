package org.mitre.tdp.boogie.alg.resolve.infer;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.TransitionMaskedProcedure;
import org.mitre.tdp.boogie.alg.resolve.AirportToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.resolve.RunwayTransitionFilter;
import org.mitre.tdp.boogie.alg.resolve.StarToken;

final class StarRunwayTransitionInferrer implements SectionInferrer {

  private final LookupService<Procedure> proceduresByName;

  private final String arrivalRunway;

  StarRunwayTransitionInferrer(LookupService<Procedure> proceduresByName, String arrivalRunway) {
    this.proceduresByName = requireNonNull(proceduresByName);
    this.arrivalRunway = arrivalRunway;
  }

  @Override
  public List<ResolvedTokens> inferBetween(ResolvedTokens left, ResolvedTokens right) {

    Optional<StarToken> star = left.resolvedTokens().stream()
        .filter(e -> e instanceof StarToken)
        .map(StarToken.class::cast)
        .findFirst();

    Optional<AirportToken> airport = right.resolvedTokens().stream()
        .filter(e -> e instanceof AirportToken)
        .map(AirportToken.class::cast)
        .findFirst();

    return star.isPresent() && airport.isPresent()
        ? List.of(new ResolvedTokens(left.routeToken(), runwayTransitionElements(star.get(), airport.get())))
        : List.of();
  }

  private Collection<ResolvedToken> runwayTransitionElements(StarToken star, AirportToken airport) {

    Collection<Procedure> procedures = PreferProceduresAtAirport.lookup(proceduresByName, star.identifier(), airport.identifier());

    return procedures.stream()
        .map(procedure -> new TransitionMaskedProcedure(procedure, nonArrivalRunwayTransitionFilter()))
        .map(StarToken::new)
        .collect(toList());
  }

  /**
   * Constructs a filter that will drop all runway transitions who's identifiers don't match the arrival runway.
   */
  Predicate<Transition> nonArrivalRunwayTransitionFilter() {
    return arrivalRunway == null ? t -> false : new RunwayTransitionFilter(arrivalRunway).or(t -> !TransitionType.RUNWAY.equals(t.transitionType()));
  }
}
