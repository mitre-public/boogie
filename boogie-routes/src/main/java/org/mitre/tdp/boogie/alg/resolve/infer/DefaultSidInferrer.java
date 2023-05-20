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
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.SidToken;
import org.mitre.tdp.boogie.alg.split.RouteToken;

final class DefaultSidInferrer implements SectionInferrer {

  private final LookupService<Procedure> proceduresByName;

  private final String sid;

  DefaultSidInferrer(LookupService<Procedure> proceduresByName, String sid) {
    this.proceduresByName = requireNonNull(proceduresByName);
    this.sid = requireNonNull(sid);
  }

  @Override
  public List<ResolvedSection> inferBetween(ResolvedSection left, ResolvedSection right) {

    Optional<AirportToken> airport = left.elements().stream()
        .filter(e -> e instanceof AirportToken)
        .map(AirportToken.class::cast)
        .findFirst();

    boolean missingSid = airport.isPresent()
        && right.elements().stream().noneMatch(e -> e instanceof SidToken);

    if (missingSid) {

      return PreferProceduresAtAirport.lookup(proceduresByName, sid, airport.get().identifier()).stream()
          .map(p -> makeSection(p, left, right))
          .findAny().map(List::of).orElseGet(List::of); // collapse to one and return
    }

    return List.of();
  }

  private ResolvedSection makeSection(Procedure sid, ResolvedSection left, ResolvedSection right) {
    RouteToken token = RouteToken.between(sid.procedureIdentifier(), left.sectionSplit(), right.sectionSplit());
    return new ResolvedSection(token, List.of(new SidToken(new TransitionMaskedProcedure(sid, COMMON_OR_ENROUTE))));
  }


  private static final Predicate<Transition> COMMON_OR_ENROUTE = transition ->
      TransitionType.COMMON.equals(transition.transitionType()) || TransitionType.ENROUTE.equals(transition.transitionType());
}
