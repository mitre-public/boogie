package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.TransitionMaskedProcedure;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

/**
 * Returns a new "fake" section which was resolved from the underlying route string containing a "masked" version of the SID with
 * only the runway transition matching the departure runway exposed.
 * <br>
 * Related to the {@link StarRunwayTransitionResolver}.
 */
public final class SidRunwayTransitionResolver implements SectionResolver {

  private final String departureRunway;
  private final SidStarResolver sidResolver;

  public SidRunwayTransitionResolver(@Nullable String departureRunway, LookupService<Procedure> procedureService) {
    this.departureRunway = departureRunway;
    this.sidResolver = new SidStarResolver(requireNonNull(procedureService).thenFilterWith(p -> ProcedureType.SID.equals(p.procedureType())));
  }

  @Override
  public ResolvedSection resolve(@Nullable SectionSplit previous, SectionSplit current, @Nullable SectionSplit next) {

    List<ResolvedElement> resolvedElements = sidResolver.proceduresFor(previous, current, next).stream()
        .map(procedure -> new TransitionMaskedProcedure(procedure, nonDepartureRunwayTransitionFilter()))
        .map(SidElement::new).collect(Collectors.toList());

    return new ResolvedSection(current, resolvedElements);
  }

  /**
   * Constructs a filter that will drop all runway transitions who's identifiers don't match the departure runway.
   */
  Predicate<Transition> nonDepartureRunwayTransitionFilter() {
    return departureRunway == null ? t -> false : new RunwayTransitionFilter(departureRunway).or(t -> !TransitionType.RUNWAY.equals(t.transitionType()));
  }
}
