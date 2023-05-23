package org.mitre.tdp.boogie.alg.resolve.infer;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.TransitionMaskedProcedure;
import org.mitre.tdp.boogie.alg.resolve.AirportToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.resolve.RunwayTransitionFilter;
import org.mitre.tdp.boogie.alg.resolve.SidToken;

final class SidRunwayTransitionInferrer implements SectionInferrer {

  private final LookupService<Procedure> proceduresByName;

  private final String departureRunway;

  SidRunwayTransitionInferrer(LookupService<Procedure> proceduresByName, @Nullable String departureRunway) {
    this.proceduresByName = requireNonNull(proceduresByName);
    this.departureRunway = departureRunway;
  }

  @Override
  public List<ResolvedTokens> inferBetween(ResolvedTokens left, ResolvedTokens right) {

    Optional<AirportToken> airport = left.resolvedTokens().stream()
        .filter(e -> e instanceof AirportToken)
        .map(AirportToken.class::cast)
        .findFirst();

    Optional<SidToken> sid = right.resolvedTokens().stream()
        .filter(e -> e instanceof SidToken)
        .map(SidToken.class::cast)
        .findFirst();

    return airport.isPresent() && sid.isPresent()
        ? List.of(new ResolvedTokens(right.routeToken(), runwayTransitionElements(airport.get(), sid.get())))
        : List.of();
  }

  private Collection<ResolvedToken> runwayTransitionElements(AirportToken airport, SidToken sid) {

    Collection<Procedure> procedures = PreferProceduresAtAirport.lookup(proceduresByName, sid.identifier(), airport.identifier());

    return procedures.stream()
        .map(procedure -> new TransitionMaskedProcedure(procedure, nonDepartureRunwayTransitionFilter()))
        .map(SidToken::new)
        .collect(toList());
  }

  /**
   * Constructs a filter that will drop all runway transitions who's identifiers don't match the departure runway.
   */
  Predicate<Transition> nonDepartureRunwayTransitionFilter() {
    return departureRunway == null ? t -> false : new RunwayTransitionFilter(departureRunway).or(t -> !TransitionType.RUNWAY.equals(t.transitionType()));
  }
}
