package org.mitre.tdp.boogie.alg.resolve.infer;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.TransitionMaskedProcedure;
import org.mitre.tdp.boogie.alg.resolve.AirportToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.resolve.StarToken;
import org.mitre.tdp.boogie.alg.split.RouteToken;

final class DefaultStarInferrer implements SectionInferrer {

  private final LookupService<Procedure> proceduresByName;

  private final String star;

  DefaultStarInferrer(LookupService<Procedure> proceduresByName, String star) {
    this.proceduresByName = requireNonNull(proceduresByName);
    this.star = requireNonNull(star);
  }

  @Override
  public List<ResolvedTokens> inferBetween(ResolvedTokens left, ResolvedTokens right) {

    Optional<AirportToken> airport = right.resolvedTokens().stream()
        .filter(e -> e instanceof AirportToken)
        .map(AirportToken.class::cast)
        .findFirst();

    boolean missingStar = airport.isPresent()
        && left.resolvedTokens().stream().noneMatch(e -> e instanceof StarToken);

    if (missingStar) {

      return PreferProceduresAtAirport.lookup(proceduresByName, star, airport.get().identifier()).stream()
          .map(p -> makeSection(p, left, right))
          .findAny().map(List::of).orElseGet(List::of); // collapse to one and return
    }

    return List.of();
  }

  private ResolvedTokens makeSection(Procedure star, ResolvedTokens left, ResolvedTokens right) {
    RouteToken token = RouteToken.between(star.procedureIdentifier(), left.routeToken(), right.routeToken());
    return new ResolvedTokens(token, List.of(new StarToken(new TransitionMaskedProcedure(star, COMMON_OR_ENROUTE))));
  }


  private static final Predicate<Transition> COMMON_OR_ENROUTE = transition ->
      TransitionType.COMMON.equals(transition.transitionType()) || TransitionType.ENROUTE.equals(transition.transitionType());
}
