package org.mitre.tdp.boogie.alg.resolve.infer;

import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.resolve.IsApproachForRunway;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.fn.TriFunction;

final class ApproachInferrer implements SectionInferrer {

  private static final Predicate<Transition> MISSED = t -> TransitionType.MISSED.equals(t.transitionType());

  private static final TriFunction<Collection<Procedure>, String, String, Collection<Procedure>> approachesForRunway =
      (procedures, runwayNumber, parallelIndicator) -> procedures.stream().filter(p -> IsApproachForRunway.test(p.procedureIdentifier(), runwayNumber, parallelIndicator)).collect(toList());

  private final String arrivalRunway;
  private final Function<Collection<Procedure>, Collection<Procedure>> equippedProcedures;
  private final LookupService<Procedure> proceduresByAirport;

  ApproachInferrer(String arrivalRunway, List<RequiredNavigationEquipage> requiredNavigationEquipages, LookupService<Procedure> proceduresByAirport) {
    this.arrivalRunway = requireNonNull(arrivalRunway);
    this.equippedProcedures = PreferredProcedures.equipagePreference(requiredNavigationEquipages);
    this.proceduresByAirport = requireNonNull(proceduresByAirport)
        .thenFilterWith(procedure -> ProcedureType.APPROACH.equals(procedure.procedureType()))
        // mask the missed-approach portions of the approach procedure
        .thenApply(procedure -> procedure.stream().map(p -> Procedure.maskTransitions(p, MISSED)).collect(toList()));
  }

  @Override
  public List<ResolvedTokens> inferBetween(ResolvedTokens left, ResolvedTokens right) {
    return RunwayIdExtractor.runwayNumber(arrivalRunway)
        .flatMap(extractedNumber -> resolve(right, extractedNumber))
        .map(approach -> makeResolvedSection(approach, left.routeToken(), right.routeToken()))
        .map(List::of).orElseGet(List::of);
  }

  private ResolvedTokens makeResolvedSection(Procedure approach, RouteToken left, RouteToken right) {
    RouteToken token = RouteToken.between(approach.procedureIdentifier(), left, right);
    return new ResolvedTokens(token, singletonList(ResolvedToken.standardApproach(approach)));
  }

  private Optional<Procedure> resolve(ResolvedTokens resolvedTokens, String extractedNumber) {
    return resolvedTokens.resolvedTokens().stream()
        .flatMap(token -> ResolvedTokenVisitor.airport(token).stream().map(Airport::airportIdentifier))
        .map(proceduresByAirport)
        .map(procedures -> approachesForRunway.apply(procedures, extractedNumber, RunwayIdExtractor.parallelDesignator(arrivalRunway).orElse(null)))
        .map(equippedProcedures)
        .flatMap(Collection::stream)
        // deterministic ordering for the procedures - by identifier alphabetically, if you are getting
        // the right requested equipage then we shouldn't care past this
        .min(comparing(Procedure::procedureIdentifier));
  }

  static final class PreferredProcedures implements UnaryOperator<Collection<Procedure>> {

    private final List<Predicate<Procedure>> tieredPredicates;

    public PreferredProcedures(List<Predicate<Procedure>> tieredPredicates) {
      this.tieredPredicates = tieredPredicates;
    }

    static PreferredProcedures equipagePreference(List<RequiredNavigationEquipage> equipages) {

      List<Predicate<Procedure>> preferences = equipages.stream()
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
