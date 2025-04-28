package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

final class SidResolver implements RouteTokenResolver {

  private final LookupService<Procedure> lookupService;

  SidResolver(LookupService<Procedure> lookupService) {
    this.lookupService = requireNonNull(lookupService)
        .filtered(p -> ProcedureType.SID.equals(p.procedureType()));
  }

  @Override
  public ResolvedTokens resolve(@Nullable RouteToken previous, RouteToken current, @Nullable RouteToken next) {
    return new ResolvedTokens(
        current,
        proceduresFor(previous, current, next).stream().map(ResolvedToken::sidEnrouteCommon).collect(toList())
    );
  }

  /**
   * Returns the collection of procedures matching the current section split.
   *
   * <p>Internally this applies some lightweight filtering to the returned set of SIDs assuming they're either book-ended by:
   * <ol>
   *   <li>The airport they service</li>
   *   <li>Fixes contained in the procedure definition</li>
   * </ol>
   *
   * <p>This pares down the results to a reasonable subset of procedure definitions in the case where we have reasonable context.
   */
  Collection<Procedure> proceduresFor(@Nullable RouteToken previous, RouteToken current, @Nullable RouteToken next) {

    Collection<Procedure> sids = lookupService.apply(current.infrastructureName());

    Collection<Procedure> fromDepartureAirport = sids.stream()
        .filter(p -> isFromDepartureAirport(p, previous))
        .toList();

    if (!fromDepartureAirport.isEmpty()) {
      return fromDepartureAirport;
    }

    Collection<Procedure> containingExitFix = sids.stream()
        .filter(p -> containsWaypoint(p, next))
        .toList();

    if (!containingExitFix.isEmpty()) {
      return containingExitFix;
    }

    if (isNull(previous)) {
      return sids;
    }

    return List.of();
  }

  /**
   * Returns true if the provided procedure is for an airport matching the candidate "departure airport" token.
   *
   * <p>Often navigation databases will contain copies of the same procedure serving different satellite airports around a major
   * one, this helps ensure we select the correct one.
   *
   * <p>e.g. HOBBT2 serves KATL, SATELLITE1, SATELLITE2... we get a copy of HOBBT2 in the raw data for each of those airports, this
   * is mean't to prefer the implementation who's {@link Procedure#airportIdentifier()} matches the filed arrival/departure airport.
   */
  private boolean isFromDepartureAirport(Procedure procedure, @Nullable RouteToken previous) {
    return ofNullable(previous)
        .filter(p -> p.infrastructureName().equalsIgnoreCase(procedure.airportIdentifier()))
        .isPresent();
  }

  /**
   * On the off-chance that this SID is filed without an airport for context we want to still scope the returns such that we only
   * return those that at least contain the entry fix waypoint in them.
   *
   * <p>Opening it up to all procedures (when procedures can have names identical to some fix names) allows too much leeway in the
   * matching process to return quality results.
   */
  private boolean containsWaypoint(Procedure procedure, @Nullable RouteToken next) {
    return ofNullable(next)
        .filter(p -> procedure.transitions().stream()
            .anyMatch(t -> t.legs().stream()
                .anyMatch(l -> next.infrastructureName().equalsIgnoreCase(l.associatedFix().map(Fix::fixIdentifier).orElse(null)))))
        .isPresent();
  }
}
