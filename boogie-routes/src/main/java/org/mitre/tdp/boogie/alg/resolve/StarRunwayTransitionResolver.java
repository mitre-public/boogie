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
 * Returns a new "fake" section which was resolved from the underlying route string containing a "masked" version of the STAR with
 * only the runway transition matching the arrival runway exposed.
 * <br>
 * Related to the {@link SidRunwayTransitionResolver}.
 */
public final class StarRunwayTransitionResolver implements SectionResolver {

  private final String arrivalRunway;
  private final SidStarResolver starResolver;

  public StarRunwayTransitionResolver(@Nullable String arrivalRunway, LookupService<Procedure> procedureService) {
    this.arrivalRunway = arrivalRunway;
    this.starResolver = new SidStarResolver(requireNonNull(procedureService).thenFilterWith(p -> ProcedureType.STAR.equals(p.procedureType())));
  }

  @Override
  public ResolvedSection resolve(@Nullable SectionSplit previous, SectionSplit current, @Nullable SectionSplit next) {

    List<ResolvedElement> resolvedElements = starResolver.proceduresFor(previous, current, next).stream()
        .map(procedure -> new TransitionMaskedProcedure(procedure, nonArrivalRunwayTransitionFilter()))
        .map(StarElement::new).collect(Collectors.toList());

    return new ResolvedSection(current, resolvedElements);
  }

  /**
   * Constructs a filter that will drop all runway transitions who's identifiers don't match the arrival runway.
   */
  Predicate<Transition> nonArrivalRunwayTransitionFilter() {
    return arrivalRunway == null ? t -> false : new RunwayTransitionFilter(arrivalRunway).or(t -> !TransitionType.RUNWAY.equals(t.transitionType()));
  }
}
