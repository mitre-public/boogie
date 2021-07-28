package org.mitre.tdp.boogie.alg.resolve.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.graph.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.element.ProcedureElement;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.validate.RecordElectorFactory;

/**
 * Class for resolving candidate {@link ProcedureElement}s from a {@link SectionSplit} of the underlying route string.
 */
public final class ProcedureResolver implements SectionResolver {

  private final LookupService<Procedure> lookupService;

  public ProcedureResolver(LookupService<Procedure> lookupService) {
    this.lookupService = checkNotNull(lookupService);
  }

  @Override
  public List<ResolvedElement<?>> resolve(@Nullable SectionSplit previous, SectionSplit sectionSplit, @Nullable SectionSplit next) {
    Collection<Procedure> procedures = lookupService.apply(sectionSplit.value()).stream()
        // pre-filter approach procedures - we can add these back in later when we have more information
        // about how they should be zipped into the final graph
        .filter(p -> !ProcedureType.APPROACH.equals(p.procedureType()))
        .collect(Collectors.toList());

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
   * Returns a new {@link Procedure} filter to down-select to procedures who's {@link Procedure#airportIdentifier()} matches the
   * ones filed as arrival/departure in the route string.
   * <br>
   * This is necessary as most navigation data sources publish multiple <i>similar</i> versions of the same procedure if it serves
   * multiple airports within an area. These cases are explicitly not filtered in the {@link RecordElectorFactory} methods.
   * <br>
   * e.g. HOBBT2 serves KATL, SATELLITE1, SATELLITE2... we get a copy of HOBBT2 in the raw data for each of those airports, this
   * is meant to prefer the implementation who's {@link Procedure#airportIdentifier()} matches the filed arrival/departure airport.
   * <br>
   * Note this requires some upstream standardization of route strings (as many real route strings interchangeable use the ICAO/FAA
   * code for the arrival/departure airport).
   */
  Predicate<Procedure> newAirportFilter(@Nullable SectionSplit previous, @Nullable SectionSplit next) {
    return procedure -> {
      if (ProcedureType.SID.equals(procedure.procedureType())) {
        return Optional.ofNullable(previous)
            .filter(p -> p.value().equalsIgnoreCase(procedure.airportIdentifier())).isPresent();
      }
      if (ProcedureType.STAR.equals(procedure.procedureType())) {
        return Optional.ofNullable(next)
            .filter(n -> n.value().equalsIgnoreCase(procedure.airportIdentifier())).isPresent();
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
