package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.util.Streams.triplesWithNulls;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.alg.split.RouteTokenizer;

import com.google.common.collect.ImmutableList;

/**
 * A {@link SectionResolver} exists to resolve infrastructure elements which are considered to be associated with an input
 * {@link RouteToken} generated from a configured {@link RouteTokenizer}.
 *
 * <p>Resolvers allow for the context of neighboring splits to be used in the resolution of the current split - though the previous
 * and next section may be null (if at the start or end of a route string).
 *
 * <p>The {@link SingleSplitSectionResolver} is provided as a sub-interface for sections of route strings which don't require the
 * context of a neighboring section to be resolved appropriately.
 */
@FunctionalInterface
public interface SectionResolver {

  /**
   * Wrapped implementation of a section resolver which covers all the basic element types which can appear in a route string.
   *
   * <p>This pre-canned implementation will cover most use cases and requires access to a backing set of {@link LookupService}
   * implementations. This also serves as an example method for clients who wish to add their own custom resolvers.
   *
   * @param airportsByName   lookup service for airports by their identifier, e.g. {@code KATL} or {@code WSSS}
   * @param proceduresByName lookup service for procedures by their identifier, e.g. {@code CHPPR1} or {@code GNDLF2}
   * @param airwaysByName    lookup service for airways by their identifier, e.g. {@code J121} or {@code V76}
   * @param fixesByName      lookup service for fixes (navaids or waypoints) by their identifier, e.g. {@code JMACK} or {@code VNG}
   */
  static SectionResolver standard(
      LookupService<Airport> airportsByName,
      LookupService<Procedure> proceduresByName,
      LookupService<Airway> airwaysByName,
      LookupService<Fix> fixesByName
  ) {
    return composite()
        .addResolver(airport(airportsByName))
        .addResolver(sidStar(proceduresByName))
        .addResolver(airway(airwaysByName))
        .addResolver(fix(fixesByName))
        .addResolver(latlong(null))
        .build();
  }

  /**
   * No-op implementations of a section resolver which returns an empty {@link ResolvedSection} when given any route token as
   * an input.
   */
  static SectionResolver noop() {
    return (left, middle, right) -> new ResolvedSection(middle, List.of());
  }

  /**
   * Decorates a given {@link SectionResolver} as a "surly" resolver. This changes the typical contract of the resolvers (which
   * by default allow no elements to be resolved) to instead throw exceptions if no matching infrastructure is found.
   *
   * <p>This is desirable in scenarios where the client is expected to be able to take corrective action to supplement configured
   * infrastructure data backing the resolvers.
   *
   * @param sectionResolver the resolver to decorate, generally this should be a composite resolver representing the results of
   *                        multiple resolvers combined.
   */
  static SectionResolver surly(SectionResolver sectionResolver) {
    return new SurlySectionResolver(sectionResolver);
  }

  /**
   * A fluent, buildable, section resolver implementation allowing for multiple resolvers to be chained together to support the
   * resolution of elements of a variety of different types.
   */
  static SectionResolver.Composite composite() {
    return new Composite();
  }

  /**
   * Returns a new {@link SectionResolver} resolving route string tokens to concrete airports based on their identifier.
   *
   * @param airportsByIdentifier lookup service providing airports indexed by their identifier (e.g. KATL, KLGA) as they would be
   *                             referenced in an input route string.
   */
  static SectionResolver airport(LookupService<Airport> airportsByIdentifier) {
    return new AirportResolver(airportsByIdentifier);
  }

  /**
   * Returns a new {@link SectionResolver} resolving route string tokens to concrete procedure SID/STAR procedure definitions,
   * note that this resolver will mask the
   *
   * @param proceduresByIdentifier lookup service providing procedures indexed by their identifier (e.g. CHPPR1, GNDLF2) as they
   *                               would be referenced in an input route string.
   */
  static SectionResolver sidStar(LookupService<Procedure> proceduresByIdentifier) {
    return new SidStarResolver(proceduresByIdentifier);
  }

  /**
   * Returns a new {@link SectionResolver} resolving route string tokens to concrete airways based on their identifier.
   *
   * @param airwaysByIdentifier lookup service providing airways indexed by their identifier (e.g. J121, Y200) as they would be
   *                            referenced in an input route string.
   */
  static SectionResolver airway(LookupService<Airway> airwaysByIdentifier) {
    return new AirwayResolver(airwaysByIdentifier);
  }

  /**
   * Returns a new {@link SectionResolver} resolving route string tokens to concrete fixes based on their identifier (contains
   * handling for fixes that show up tailored e.g. {@code NAV123034})
   *
   * @param fixesByIdentifier lookup service providing fixes indexed by their identifier (e.g. JMACK, VNB) as they would show up
   *                          in an input route string.
   */
  static SectionResolver fix(LookupService<Fix> fixesByIdentifier) {
    return new FixResolver(fixesByIdentifier);
  }

  /**
   * Returns a new {@link SectionResolver} resolving literal lat/long values encoded in the underlying route string.
   *
   * <p>Supports both ICAO and FAA coordinate standards out-of-the-box.
   */
  static SectionResolver latlong(Function<LatLong, Leg> toLeg) {
    return new LatLonResolver();
  }

  /**
   * Attempts to resolve a list of candidate {@link ResolvedElement}s from the given current {@link RouteToken} as well as
   * the preceding and following route tokens.
   */
  ResolvedSection resolve(@Nullable RouteToken previous, RouteToken current, @Nullable RouteToken next);

  default List<ResolvedSection> applyTo(List<RouteToken> sectionSplits) {
    return triplesWithNulls(sectionSplits, this::resolve).collect(Collectors.toList());
  }

  /**
   * Composes the given {@link SectionResolver} with the provided {@link SectionResolver} to produce a new resolver which returns
   * the outputs of both calls to {@link SectionResolver#resolve(RouteToken, RouteToken, RouteToken)} as a single list.
   */
  default SectionResolver and(SectionResolver that) {
    checkNotNull(that, "Input resolver cannot be null.");
    return (p, c, n) -> {
      LinkedHashSet<ResolvedElement> allElements = new LinkedHashSet<>();

      ResolvedSection thisSection = this.resolve(p, c, n);
      ResolvedSection thatSection = that.resolve(p, c, n);

      checkArgument(thisSection.sectionSplit().equals(thatSection.sectionSplit()));

      allElements.addAll(thisSection.elements());
      allElements.addAll(thatSection.elements());

      return new ResolvedSection(thisSection.sectionSplit(), allElements);
    };
  }

  final class Composite {

    private final ImmutableList.Builder<SectionResolver> resolvers = ImmutableList.builder();

    private Composite() {
    }

    public Composite addResolver(SectionResolver resolver) {
      this.resolvers.add(requireNonNull(resolver));
      return this;
    }

    public Composite addResolvers(Collection<SectionResolver> resolvers) {
      this.resolvers.addAll(resolvers);
      return this;
    }

    SectionResolver build() {
      return resolvers.build().stream().reduce(SectionResolver::and).orElseThrow(IllegalStateException::new);
    }
  }
}
