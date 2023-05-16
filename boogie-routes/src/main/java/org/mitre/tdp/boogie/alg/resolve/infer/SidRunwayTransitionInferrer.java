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
import org.mitre.tdp.boogie.alg.resolve.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.ResolvedElement;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.RunwayTransitionFilter;
import org.mitre.tdp.boogie.alg.resolve.SidElement;

final class SidRunwayTransitionInferrer implements SectionInferrer {

  private final LookupService<Procedure> proceduresByName;

  private final String departureRunway;

  SidRunwayTransitionInferrer(LookupService<Procedure> proceduresByName, @Nullable String departureRunway) {
    this.proceduresByName = requireNonNull(proceduresByName);
    this.departureRunway = departureRunway;
  }

  @Override
  public List<ResolvedSection> inferBetween(ResolvedSection left, ResolvedSection right) {

    Optional<AirportElement> airport = left.elements().stream()
        .filter(e -> e instanceof AirportElement)
        .map(AirportElement.class::cast)
        .findFirst();

    Optional<SidElement> sid = right.elements().stream()
        .filter(e -> e instanceof SidElement)
        .map(SidElement.class::cast)
        .findFirst();

    return airport.isPresent() && sid.isPresent()
        ? List.of(new ResolvedSection(right.sectionSplit(), runwayTransitionElements(airport.get(), sid.get())))
        : List.of();
  }

  private Collection<ResolvedElement> runwayTransitionElements(AirportElement airport, SidElement sid) {

    Collection<Procedure> procedures = PreferProceduresAtAirport.lookup(proceduresByName, sid.identifier(), airport.identifier());

    return procedures.stream()
        .map(procedure -> new TransitionMaskedProcedure(procedure, nonDepartureRunwayTransitionFilter()))
        .map(SidElement::new)
        .collect(toList());
  }

  /**
   * Constructs a filter that will drop all runway transitions who's identifiers don't match the departure runway.
   */
  Predicate<Transition> nonDepartureRunwayTransitionFilter() {
    return departureRunway == null ? t -> false : new RunwayTransitionFilter(departureRunway).or(t -> !TransitionType.RUNWAY.equals(t.transitionType()));
  }
}
