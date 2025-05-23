package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.util.Streams.triplesWithNulls;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.resolve.infer.KeepTransition;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.alg.split.RouteTokenizer;

import com.google.common.collect.ImmutableList;

/**
 * A {@link RouteTokenResolver} exists to resolve infrastructure elements which are considered to be associated with an input
 * {@link RouteToken} generated from a configured {@link RouteTokenizer}.
 *
 * <p>Resolvers allow for the context of neighboring tokens to be used in the resolution of the current token - though the previous
 * and next token may be null (if at the start or end of a route string).
 *
 * <p>The {@link SingleTokenResolver} is provided as a sub-interface for tokens which don't require the context of a neighboring
 * tokens to be resolved appropriately.
 */
@FunctionalInterface
public interface RouteTokenResolver {

  /**
   * Wrapped implementation of a token resolver which covers all the basic element types which can appear in a route string.
   *
   * <p>This pre-canned implementation will cover most use cases and requires access to a backing set of {@link LookupService}
   * implementations. This also serves as an example method for clients who wish to add their own custom resolvers.
   *
   * @param airportsByName   lookup service for airports by their identifier, e.g. {@code KATL} or {@code WSSS}
   * @param proceduresByName lookup service for procedures by their identifier, e.g. {@code CHPPR1} or {@code GNDLF2}
   * @param airwaysByName    lookup service for airways by their identifier, e.g. {@code J121} or {@code V76}
   * @param fixesByName      lookup service for fixes (navaids or waypoints) by their identifier, e.g. {@code JMACK} or {@code VNG}
   */
  static RouteTokenResolver standard(
      LookupService<Airport> airportsByName,
      LookupService<Procedure> proceduresByName,
      LookupService<Airway> airwaysByName,
      LookupService<Fix> fixesByName
  ) {
    return composite()
        .addResolver(airport(airportsByName))
        .addResolver(sid(proceduresByName))
        .addResolver(star(proceduresByName))
        .addResolver(airway(airwaysByName))
        .addResolver(fix(fixesByName))
        .addResolver(frd(fixesByName))
        .addResolver(latlong(null))
        .build();
  }

  /**
   * No-op implementations of a token resolver which returns an empty {@link ResolvedTokens} when given any route token as
   * an input.
   */
  static RouteTokenResolver noop() {
    return (left, middle, right) -> new ResolvedTokens(middle, List.of());
  }

  /**
   * Decorates a given {@link RouteTokenResolver} as a "surly" resolver. This changes the typical contract of the resolvers (which
   * by default allow no elements to be resolved) to instead throw exceptions if no matching infrastructure is found.
   *
   * <p>This is desirable in scenarios where the client is expected to be able to take corrective action to supplement configured
   * infrastructure data backing the resolvers.
   *
   * @param routeTokenResolver the resolver to decorate, generally this should be a composite resolver representing the results of
   *                           multiple resolvers combined.
   */
  static RouteTokenResolver surly(RouteTokenResolver routeTokenResolver) {
    return new SurlyTokenResolver(routeTokenResolver);
  }

  /**
   * A fluent, buildable, token resolver implementation allowing for multiple resolvers to be chained together to support the
   * resolution of elements of a variety of different types.
   */
  static RouteTokenResolver.Composite composite() {
    return new Composite();
  }

  /**
   * Returns a new {@link RouteTokenResolver} resolving route string tokens to concrete airports based on their identifier.
   *
   * @param airportsByIdentifier lookup service providing airports indexed by their identifier (e.g. KATL, KLGA) as they would be
   *                             referenced in an input route string.
   */
  static RouteTokenResolver airport(LookupService<Airport> airportsByIdentifier) {
    return new AirportResolver(airportsByIdentifier);
  }

  /**
   * Returns a new {@link RouteTokenResolver} resolving route string tokens to concrete procedure SID procedure definitions, note
   * that this resolver will mask the runway transitions of returned SIDs.
   *
   * @param proceduresByIdentifier lookup service providing procedures indexed by their identifier (e.g. CHPPR1, GNDLF2) as they
   *                               would be referenced in an input route string.
   */
  static RouteTokenResolver sid(LookupService<Procedure> proceduresByIdentifier) {
    return new SidResolver(proceduresByIdentifier);
  }

  /**
   * Returns a new {@link RouteTokenResolver} resolving route string tokens to concrete procedure STAR procedure definitions, note
   * that this resolver will mask the runway transitions of returned STARs.
   *
   * @param proceduresByIdentifier lookup service providing procedures indexed by their identifier (e.g. CHPPR1, GNDLF2) as they
   *                               would be referenced in an input route string.
   */
  static RouteTokenResolver star(LookupService<Procedure> proceduresByIdentifier) {
    return new StarResolver(proceduresByIdentifier);
  }

  /**
   * Returns a new {@link RouteTokenResolver} resolving route string tokens to concrete airways based on their identifier.
   *
   * @param airwaysByIdentifier lookup service providing airways indexed by their identifier (e.g. J121, Y200) as they would be
   *                            referenced in an input route string.
   */
  static RouteTokenResolver airway(LookupService<Airway> airwaysByIdentifier) {
    return new AirwayResolver(airwaysByIdentifier);
  }

  /**
   * Returns a new {@link RouteTokenResolver} resolving route string tokens to concrete fixes based on their identifier (contains
   * handling for fixes that show up tailored e.g. {@code NAV123034})
   *
   * @param fixesByIdentifier lookup service providing fixes indexed by their identifier (e.g. JMACK, VNB) as they would show up
   *                          in an input route string.
   */
  static RouteTokenResolver fix(LookupService<Fix> fixesByIdentifier) {
    return new FixResolver(fixesByIdentifier);
  }

  /**
   * Returns a new {@link RouteTokenResolver} resolving literal lat/long values encoded in the underlying route string.
   *
   * <p>Supports both ICAO and FAA coordinate standards out-of-the-box.
   */
  static RouteTokenResolver latlong(Function<LatLong, Leg> toLeg) {
    return new LatLongResolver();
  }

  /**
   * Returns a new {@link RouteTokenResolver} resolving route string tokens to fix-radial-distances based on the format of their
   * identifier (i.e. {@code NAV123034}).
   *
   * @param fixesByIdentifier lookup service providing fixes indexed by their identifier (e.g. JMACK, VNB) as they would show up
   *                          in an input route string.
   */
  static RouteTokenResolver frd(LookupService<Fix> fixesByIdentifier) {
    return new FrdResolver(fixesByIdentifier);
  }

  /**
   * Attempts to resolve a list of candidate {@link ResolvedToken}s from the given current {@link RouteToken} as well as
   * the preceding and following route tokens.
   */
  ResolvedTokens resolve(@Nullable RouteToken previous, RouteToken current, @Nullable RouteToken next);

  default List<ResolvedTokens> applyTo(List<RouteToken> sectionSplits, CategoryAndType categoryAndType) {
    KeepTransition keepTransition = KeepTransition.of(categoryAndType);
    return triplesWithNulls(sectionSplits, this::resolve)
        .map(token -> thinAll(token, keepTransition))
        .toList();
  }

  /**
   * This method will remove transitions from any SID/STAR tokens that don't apply to the category/type
   * @param tokens the token to check and thin
   * @param keepTransition the predicate for keeping a transition
   * @return a thinned or not token
   */
  default ResolvedTokens thinAll(ResolvedTokens tokens, KeepTransition keepTransition) {
    List<ResolvedToken> list = tokens.resolvedTokens().stream()
        .map(token -> thin(token, keepTransition))
        .toList();
    return new ResolvedTokens(tokens.routeToken(), list);
  }

  default ResolvedToken thin(ResolvedToken token, KeepTransition keepTransition) {
    Optional<ResolvedToken> sid = ResolvedTokenVisitor.sid(token).map(i -> Procedure.maskTransitions(i,keepTransition.negate())).map(ResolvedToken::sidEnrouteCommon);
    Supplier<Optional<ResolvedToken>> star = () -> ResolvedTokenVisitor.star(token).map(i -> Procedure.maskTransitions(i,keepTransition.negate())).map(i -> (ResolvedToken) ResolvedToken.starEnrouteCommon(i));
    return sid.or(star).orElse(token);
  }

  /**
   * Combine this resolver with the provided resolver to create a new resolver returning the tokens resolved by this resolver
   * <em>and</em> the tokens from the provided resolver.
   *
   * @param that the other token resolver whose contents should be merged with this resolvers
   */
  default RouteTokenResolver and(RouteTokenResolver that) {
    requireNonNull(that, "That cannot be null.");
    return (p, c, n) -> {
      LinkedHashSet<ResolvedToken> allElements = new LinkedHashSet<>();

      ResolvedTokens thisSection = this.resolve(p, c, n);
      ResolvedTokens thatSection = that.resolve(p, c, n);

      checkArgument(thisSection.routeToken().equals(thatSection.routeToken()));

      allElements.addAll(thisSection.resolvedTokens());
      allElements.addAll(thatSection.resolvedTokens());

      return new ResolvedTokens(thisSection.routeToken(), allElements);
    };
  }

  /**
   * Combine this resolver with the provided resolver to create a new resolver returning the tokens of the first resolver if it
   * finds any <em>or</em> the tokens resolved by the provided resolver.
   *
   * @param that the other token resolver whose contents should be returned if the current resolver resolves nothing
   */
  default RouteTokenResolver or(RouteTokenResolver that) {
    requireNonNull(that, "That cannot be null.");
    return (p, c, n) -> {

      ResolvedTokens thisTokens = this.resolve(p, c, n);
      return !thisTokens.resolvedTokens().isEmpty() ? thisTokens : that.resolve(p, c, n);
    };
  }

  final class Composite {

    private final ImmutableList.Builder<RouteTokenResolver> resolvers = ImmutableList.builder();

    private Composite() {
    }

    public Composite addResolver(RouteTokenResolver resolver) {
      this.resolvers.add(requireNonNull(resolver));
      return this;
    }

    public Composite addResolvers(Collection<RouteTokenResolver> resolvers) {
      this.resolvers.addAll(resolvers);
      return this;
    }

    RouteTokenResolver build() {
      return resolvers.build().stream().reduce(RouteTokenResolver::and).orElseThrow(IllegalStateException::new);
    }
  }
}
