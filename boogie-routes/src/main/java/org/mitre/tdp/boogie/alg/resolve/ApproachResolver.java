package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.RunwayIdExtractor;
import org.mitre.tdp.boogie.alg.TransitionMaskedProcedure;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.fn.TriFunction;

/**
 * Class for resolving a preferred approach procedure at an airport.
 * <br>
 * This class takes an arrival runway, as well as a set of tiered equipage preferences it uses to down-select the available
 * approaches at the airport to just those for the target runway and with the preferred required equipage.
 * <br>
 * If no procedures match these filters no approach will be inserted and used in the resolution.
 */
public final class ApproachResolver implements Function<ResolvedSection, Optional<ResolvedSection>> {

  private static final Predicate<Transition> NON_MISSED = t -> !TransitionType.MISSED.equals(t.transitionType());

  private static final BiFunction<Procedure, SectionSplit, ResolvedSection> resolvedSectionOf = (approach, oldSplit) -> {
    SectionSplit newSplit = new SectionSplit.Builder()
        .setValue(approach.procedureIdentifier())
        .setWildcards("")
        .setEtaEet("")
        .setIndex(oldSplit.index() - .5)
        .build();
    return new ResolvedSection(newSplit, singletonList(new ApproachElement(approach)));
  };

  private static final TriFunction<Collection<Procedure>, String, String, Collection<Procedure>> approachesForRunway =
      (procedures, runwayNumber, parallelIndicator) -> procedures.stream().filter(p -> IsApproachForRunway.test(p.procedureIdentifier(), runwayNumber, parallelIndicator)).collect(toList());

  private final String arrivalRunway;
  private final Function<Collection<Procedure>, Collection<Procedure>> equippedProcedures;
  private final LookupService<Procedure> proceduresByAirport;

  public ApproachResolver(String arrivalRunway, RequiredNavigationEquipage[] requiredNavigationEquipage, LookupService<Procedure> proceduresByAirport) {
    this.arrivalRunway = requireNonNull(arrivalRunway);
    this.equippedProcedures = PreferredProcedures.equipagePreference(requiredNavigationEquipage);
    this.proceduresByAirport = requireNonNull(proceduresByAirport)
        .thenFilterWith(procedure -> ProcedureType.APPROACH.equals(procedure.procedureType()))
        // mask the missed-approach portions of the approach procedure
        .thenApply(procedure -> procedure.stream().map(p -> new TransitionMaskedProcedure(p, NON_MISSED)).collect(toList()));
  }

  @Override
  public Optional<ResolvedSection> apply(ResolvedSection resolvedSection) {
    return RunwayIdExtractor.runwayNumber(arrivalRunway).flatMap(extractedNumber -> resolve(resolvedSection, extractedNumber));
  }

  private Optional<ResolvedSection> resolve(ResolvedSection resolvedSection, String extractedNumber) {
    SectionSplit oldSplit = resolvedSection.sectionSplit();
    return resolvedSection.elements().stream()
        .filter(AirportElement.class::isInstance)
        .map(AirportElement.class::cast)
        .map(airportElement -> proceduresByAirport.apply(airportElement.airport().airportIdentifier()))
        .map(procedures -> approachesForRunway.apply(procedures, extractedNumber, RunwayIdExtractor.parallelDesignator(arrivalRunway).orElse(null)))
        .map(equippedProcedures)
        .flatMap(Collection::stream)
        // deterministic ordering for the procedures - by identifier alphabetically, if you are getting
        // the right requested equipage then we shouldn't care past this
        .min(comparing(Procedure::procedureIdentifier))
        .map(approach -> resolvedSectionOf.apply(approach, oldSplit));
  }

  static final class PreferredProcedures implements UnaryOperator<Collection<Procedure>> {

    private final List<Predicate<Procedure>> tieredPredicates;

    public PreferredProcedures(List<Predicate<Procedure>> tieredPredicates) {
      this.tieredPredicates = tieredPredicates;
    }

    static PreferredProcedures equipagePreference(RequiredNavigationEquipage... equipages) {

      List<Predicate<Procedure>> preferences = Stream.of(equipages)
          .map(PreferredProcedures::hasEquipage)
          .collect(Collectors.toList());

      return new PreferredProcedures(preferences);
    }

    static Predicate<Procedure> hasEquipage(RequiredNavigationEquipage equipage) {
      return procedure -> equipage.equals(procedure.requiredNavigationEquipage());
    }

    @Override
    public Collection<Procedure> apply(Collection<Procedure> procedures) {
      return tieredPredicates.stream()
          .map(predicate -> procedures.stream().filter(predicate).collect(toList()))
          .filter(col -> !col.isEmpty())
          .findFirst()
          .orElse(Collections.emptyList());
    }
  }
}
