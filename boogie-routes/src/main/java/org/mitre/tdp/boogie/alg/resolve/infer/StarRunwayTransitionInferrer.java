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
import org.mitre.tdp.boogie.alg.resolve.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.ResolvedElement;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.RunwayTransitionFilter;
import org.mitre.tdp.boogie.alg.resolve.StarElement;

final class StarRunwayTransitionInferrer implements SectionInferrer {

  private final LookupService<Procedure> proceduresByName;

  private final String arrivalRunway;

  StarRunwayTransitionInferrer(LookupService<Procedure> proceduresByName, String arrivalRunway) {
    this.proceduresByName = requireNonNull(proceduresByName);
    this.arrivalRunway = arrivalRunway;
  }

  @Override
  public List<ResolvedSection> inferBetween(ResolvedSection left, ResolvedSection right) {

    Optional<StarElement> star = left.elements().stream()
        .filter(e -> e instanceof StarElement)
        .map(StarElement.class::cast)
        .findFirst();

    Optional<AirportElement> airport = right.elements().stream()
        .filter(e -> e instanceof AirportElement)
        .map(AirportElement.class::cast)
        .findFirst();

    return star.isPresent() && airport.isPresent()
        ? List.of(new ResolvedSection(left.sectionSplit(), runwayTransitionElements(star.get(), airport.get())))
        : List.of();
  }

  private Collection<ResolvedElement> runwayTransitionElements(StarElement star, AirportElement airport) {

    Collection<Procedure> procedures = proceduresByName.apply(star.identifier());

    Collection<Procedure> proceduresAtAirport = procedures.stream()
        .filter(p -> p.airportIdentifier().equalsIgnoreCase(airport.identifier()))
        .collect(toList());

    Collection<Procedure> toUse = proceduresAtAirport.isEmpty() ? procedures : proceduresAtAirport;

    return toUse.stream()
        .map(procedure -> new TransitionMaskedProcedure(procedure, nonArrivalRunwayTransitionFilter()))
        .map(StarElement::new)
        .collect(toList());
  }

  /**
   * Constructs a filter that will drop all runway transitions who's identifiers don't match the arrival runway.
   */
  Predicate<Transition> nonArrivalRunwayTransitionFilter() {
    return arrivalRunway == null ? t -> false : new RunwayTransitionFilter(arrivalRunway).or(t -> !TransitionType.RUNWAY.equals(t.transitionType()));
  }
}
