package org.mitre.tdp.boogie.alg.resolve.infer;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.CategoryAndType;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.split.RouteToken;

final class DefaultSidInferrer implements SectionInferrer {

  private static final Predicate<Transition> RUNWAY = transition -> TransitionType.RUNWAY.equals(transition.transitionType());

  private final LookupService<Procedure> proceduresByName;

  private final String sid;

  private final KeepTransition keepTransition;

  DefaultSidInferrer(LookupService<Procedure> proceduresByName, String sid, CategoryAndType categoryAndType) {
    this.proceduresByName = requireNonNull(proceduresByName);
    this.sid = requireNonNull(sid);
    this.keepTransition = KeepTransition.of(requireNonNull(categoryAndType));
  }

  @Override
  public List<ResolvedTokens> inferBetween(ResolvedTokens left, ResolvedTokens right) {

    Optional<Airport> airport = left.resolvedTokens().stream()
        .flatMap(t -> ResolvedTokenVisitor.airport(t).stream())
        .findFirst();

    boolean missingSid = airport.isPresent()
        && right.resolvedTokens().stream().flatMap(t -> ResolvedTokenVisitor.sid(t).stream()).findFirst().isEmpty();

    if (missingSid) {

      return PreferProceduresAtAirport.lookup(proceduresByName, sid, airport.get().airportIdentifier()).stream()
          .map(p -> makeSection(p, left, right))
          .findAny().map(List::of).orElseGet(List::of); // collapse to one and return
    }

    return List.of();
  }

  private ResolvedTokens makeSection(Procedure sid, ResolvedTokens left, ResolvedTokens right) {
    RouteToken token = RouteToken.between(sid.procedureIdentifier(), left.routeToken(), right.routeToken());
    ResolvedToken thinned = Optional.of(sid)
        .map(procedure -> Procedure.maskTransitions(procedure, RUNWAY))
        .map(procedure -> Procedure.maskTransitions(procedure, keepTransition.negate()))
        .map(ResolvedToken::sidEnrouteCommon)
        .orElseThrow(() -> new IllegalArgumentException("Missing required procedure for the procedure that is default"));
    return new ResolvedTokens(token, List.of(thinned));
  }
}
