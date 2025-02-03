package org.mitre.tdp.boogie.alg.resolve.infer;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.CategoryAndType;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;

final class SidRunwayTransitionInferrer implements SectionInferrer {

  private final LookupService<Procedure> proceduresByName;

  private final String departureRunway;

  private final KeepTransition keepTransition;

  SidRunwayTransitionInferrer(LookupService<Procedure> proceduresByName, @Nullable String departureRunway, CategoryAndType categoryAndType) {
    this.proceduresByName = requireNonNull(proceduresByName);
    this.departureRunway = departureRunway;
    this.keepTransition = KeepTransition.of(requireNonNull(categoryAndType));
  }

  @Override
  public List<ResolvedTokens> inferBetween(ResolvedTokens left, ResolvedTokens right) {

    Optional<Airport> airport = left.resolvedTokens().stream()
        .flatMap(t -> ResolvedTokenVisitor.airport(t).stream())
        .findFirst();

    Optional<Procedure> sid = right.resolvedTokens().stream()
        .flatMap(t -> ResolvedTokenVisitor.sid(t).stream())
        .findFirst();

    return airport.isPresent() && sid.isPresent()
        ? List.of(new ResolvedTokens(right.routeToken(), runwayTransitionElements(airport.get(), sid.get())))
        : List.of();
  }

  private Collection<ResolvedToken> runwayTransitionElements(Airport airport, Procedure sid) {

    Collection<Procedure> procedures = PreferProceduresAtAirport.lookup(proceduresByName, sid.procedureIdentifier(), airport.airportIdentifier());

    return procedures.stream()
        .map(procedure -> Procedure.maskTransitions(procedure, nonDepartureRunwayTransitionFilter().negate()))
        .map(procedure -> Procedure.maskTransitions(procedure, keepTransition.negate()))
        .map(ResolvedToken::sidRunway)
        .collect(toList());
  }

  /**
   * Constructs a filter that will drop all runway transitions who's identifiers don't match the departure runway.
   */
  Predicate<Transition> nonDepartureRunwayTransitionFilter() {
    return departureRunway == null ? t -> false : new RunwayTransitionFilter(departureRunway).or(t -> !TransitionType.RUNWAY.equals(t.transitionType()));
  }
}
