package org.mitre.tdp.boogie.alg.resolve.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.resolve.element.ProcedureElement;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.graph.LinkedLegs;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.service.ProcedureService;

/**
 * Class for resolving candidate {@link ProcedureElement}s from a {@link SectionSplit} of the underlying route string.
 */
public final class ProcedureResolver implements SectionResolver {

  private final ProcedureService lookupService;

  public ProcedureResolver(ProcedureService lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedElement<?>> resolve(@Nullable SectionSplit previous, SectionSplit sectionSplit, @Nullable SectionSplit next) {
    Collection<Procedure> procedures = lookupService.allMatchingIdentifier(sectionSplit.value()).stream()
        // pre-filter approach procedures - we can add these back in later when we have more information
        // about how they should be zipped into the final graph
        .filter(p -> !p.type().equals(ProcedureType.APPROACH)).collect(Collectors.toList());

    // Down-select to procedures matching the filed arr/dep airport (assuming we have them)
    Predicate<Procedure> airportFilter = newAirportFilter(previous, next);
    Collection<Procedure> proceduresAtAirport = procedures.stream().filter(airportFilter).collect(Collectors.toList());

    // We want to down-select where we can, but something is better than nothing, so allow the no-match
    // case to return anything that matched - the algo is generic enough to handle it smartly
    return proceduresAtAirport.isEmpty()
        ? convertToResolvedElements(procedures)
        : convertToResolvedElements(proceduresAtAirport);
  }

  /**
   * Converts the provided procedures into {@link ProcedureElement}s attaching the {@link #transitionFilter)} to them as they
   * are generated and shipped off.
   */
  List<ResolvedElement<?>> convertToResolvedElements(Collection<Procedure> procedures) {
    return procedures.stream()
        .map(ProcedureElement::new)
        .map(procedureElement -> procedureElement.setTransitionFilter(transitionFilter))
        .collect(Collectors.toList());
  }

  /**
   * Returns a new {@link Procedure} filter to down-select to procedures who's {@link Procedure#airport()} matches the ones
   * filed as arrival/departure in the route string.
   *
   * This is necessary as CIFP/LIDO/Jepp all publish multiple copies of same SID/STARs for each airport they serve.
   *
   * e.g. If HOBBT2 serves KATL, SAT1, SAT2... we get a copy of HOBBT2 in the raw data for each of those airports.
   */
  Predicate<Procedure> newAirportFilter(@Nullable SectionSplit previous, @Nullable SectionSplit next) {
    return procedure -> {
      if (ProcedureType.SID.equals(procedure.type())) {
        return previous != null && previous.value().equals(procedure.airport());
      }
      if (ProcedureType.STAR.equals(procedure.type())) {
        return next != null && next.value().equals(procedure.airport());
      }
      return false;
    };
  }

  /**
   * A transition filter to apply the the transitions of the resolved {@link ProcedureElement} prior the {@link LinkedLegs}
   * generation and insertion into the graph. This lets us remove things like runway transitions which confuse the resolution
   * algorithm while preserving the full procedure context in the resolved elements so we can inspect them later to decide if
   * we want to add those runway transitions back in.
   */
  private static final Predicate<Transition> transitionFilter = new CommonOrEnrouteTransitionFilter();
}
