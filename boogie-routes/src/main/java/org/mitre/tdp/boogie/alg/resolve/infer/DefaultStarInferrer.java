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
import org.mitre.tdp.boogie.alg.resolve.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.StarElement;
import org.mitre.tdp.boogie.alg.split.RouteToken;

final class DefaultStarInferrer implements SectionInferrer {

  private final LookupService<Procedure> proceduresByName;

  private final String star;

  DefaultStarInferrer(LookupService<Procedure> proceduresByName, String star) {
    this.proceduresByName = requireNonNull(proceduresByName);
    this.star = requireNonNull(star);
  }

  @Override
  public List<ResolvedSection> inferBetween(ResolvedSection left, ResolvedSection right) {

    Optional<AirportElement> airport = right.elements().stream()
        .filter(e -> e instanceof AirportElement)
        .map(AirportElement.class::cast)
        .findFirst();

    boolean missingStar = airport.isPresent()
        && left.elements().stream().noneMatch(e -> e instanceof StarElement);

    if (missingStar) {

      return PreferProceduresAtAirport.lookup(proceduresByName, star, airport.get().identifier()).stream()
          .map(p -> makeSection(p, left, right))
          .findAny().map(List::of).orElseGet(List::of); // collapse to one and return
    }

    return List.of();
  }

  private ResolvedSection makeSection(Procedure star, ResolvedSection left, ResolvedSection right) {
    RouteToken token = RouteToken.between(star.procedureIdentifier(), left.sectionSplit(), right.sectionSplit());
    return new ResolvedSection(token, List.of(new StarElement(new TransitionMaskedProcedure(star, COMMON_OR_ENROUTE))));
  }


  private static final Predicate<Transition> COMMON_OR_ENROUTE = transition ->
      TransitionType.COMMON.equals(transition.transitionType()) || TransitionType.ENROUTE.equals(transition.transitionType());
}
