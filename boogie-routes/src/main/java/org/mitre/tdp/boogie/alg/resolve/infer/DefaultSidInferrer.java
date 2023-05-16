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
import org.mitre.tdp.boogie.alg.resolve.SidElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

final class DefaultSidInferrer implements SectionInferrer {

  private final LookupService<Procedure> proceduresByName;

  private final String sid;

  DefaultSidInferrer(LookupService<Procedure> proceduresByName, String sid) {
    this.proceduresByName = requireNonNull(proceduresByName);
    this.sid = requireNonNull(sid);
  }

  @Override
  public List<ResolvedSection> inferBetween(ResolvedSection left, ResolvedSection right) {

    Optional<AirportElement> airport = left.elements().stream()
        .filter(e -> e instanceof AirportElement)
        .map(AirportElement.class::cast)
        .findFirst();

    boolean missingSid = airport.isPresent()
        && right.elements().stream().noneMatch(e -> e instanceof SidElement);

    if (missingSid) {

      return PreferProceduresAtAirport.lookup(proceduresByName, sid, airport.get().identifier()).stream()
          .map(p -> makeSection(p, left, right))
          .findAny().map(List::of).orElseGet(List::of); // collapse to one and return
    }

    return List.of();
  }

  private ResolvedSection makeSection(Procedure sid, ResolvedSection left, ResolvedSection right) {

    SectionSplit split = SectionSplit.builder()
        .setValue(sid.procedureIdentifier())
        .setIndex((left.sectionSplit().index() + right.sectionSplit().index()) / 2.)
        .build();

    return new ResolvedSection(split, List.of(new SidElement(new TransitionMaskedProcedure(sid, COMMON_OR_ENROUTE))));
  }


  private static final Predicate<Transition> COMMON_OR_ENROUTE = transition ->
      TransitionType.COMMON.equals(transition.transitionType()) || TransitionType.ENROUTE.equals(transition.transitionType());
}
