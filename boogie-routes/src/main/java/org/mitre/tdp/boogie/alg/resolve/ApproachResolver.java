package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.alg.chooser.PreferredProcedureFactory.withPreferredEquipage;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.RunwayNumberExtractor;
import org.mitre.tdp.boogie.alg.TransitionMaskedProcedure;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

/**
 * Class for resolving a preferred approach procedure at an airport.
 * <br>
 * This class takes an arrival runway, as well as a set of tiered equipage preferences it uses to down-select the available
 * approaches at the airport to just those for the target runway and with the preferred required equipage.
 * <br>
 * If no procedures match these filters no approach will be inserted and used in the resolution.
 */
public final class ApproachResolver implements Function<ResolvedSection, Optional<ResolvedSection>> {

  private final String arrivalRunway;
  private final Function<Collection<Procedure>, Collection<Procedure>> equippedProcedures;
  private final LookupService<Procedure> proceduresByAirport;

  public ApproachResolver(String arrivalRunway, RequiredNavigationEquipage[] requiredNavigationEquipage, LookupService<Procedure> proceduresByAirport) {
    this.arrivalRunway = requireNonNull(arrivalRunway);
    this.equippedProcedures = withPreferredEquipage(requiredNavigationEquipage);

    this.proceduresByAirport = requireNonNull(proceduresByAirport)
        .thenFilterWith(procedure -> ProcedureType.APPROACH.equals(procedure.procedureType()))
        // mask the missed-approach portions of the approach procedure
        .thenApply(procedure -> procedure.stream().map(p -> new TransitionMaskedProcedure(p, NON_MISSED)).collect(toList()));
  }

  @Override
  public Optional<ResolvedSection> apply(ResolvedSection resolvedSection) {
    Optional<String> runwayNumber = RunwayNumberExtractor.INSTANCE.runwayNumber(arrivalRunway);
    if (!runwayNumber.isPresent()) {
      return Optional.empty();
    } else {
      SectionSplit oldSplit = resolvedSection.sectionSplit();

      Optional<Procedure> resolvedApproach = resolveAirportElements(resolvedSection).stream()
          .map(airportElement -> proceduresByAirport.apply(resolvedSection.sectionSplit().value()))
          .map(procedures -> selectApproachesForRunway(procedures, runwayNumber.get()))
          .map(equippedProcedures)
          .flatMap(Collection::stream)
          // deterministic ordering for the procedures - by identifier alphabetically, if youre getting
          // the right requested equipage then we shouldn't care past this
          .min(comparing(Procedure::procedureIdentifier));

      return resolvedApproach.map(approach -> {
        SectionSplit newSplit = new SectionSplit.Builder()
            .setValue(approach.procedureIdentifier())
            .setWildcards("")
            .setEtaEet("")
            .setIndex(oldSplit.index() - .5)
            .build();
        return new ResolvedSection(newSplit, singletonList(new ApproachElement(approach)));
      });
    }
  }

  Collection<Procedure> selectApproachesForRunway(Collection<Procedure> procedures, String runwayNumber) {
    return procedures.stream().filter(procedure -> procedure.procedureIdentifier().contains(runwayNumber)).collect(toList());
  }

  /**
   * Resolves any airport elements from the provided resolved section for use in looking up approach procedures at the airport.
   */
  Collection<AirportElement> resolveAirportElements(ResolvedSection resolvedSection) {
    return resolvedSection.elements().stream().filter(element -> element instanceof AirportElement).map(AirportElement.class::cast).collect(toList());
  }

  private static final Predicate<Transition> NON_MISSED = t -> !TransitionType.MISSED.equals(t.transitionType());
}
