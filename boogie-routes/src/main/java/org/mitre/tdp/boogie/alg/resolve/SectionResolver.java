package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.mitre.tdp.boogie.util.Streams.triplesWithNulls;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;

/**
 * A {@link SectionResolver} exists to resolve infrastructure elements which are considered to be associated with an input
 * {@link SectionSplit} generated from a configured {@link SectionSplitter}.
 * <br>
 * Resolvers allow for the context of neighboring splits to be used in the resolution of the current split - though the previous
 * and next section may be null (if at the start or end of a route string).
 * <br>
 * The {@link SingleSplitSectionResolver} is provided as a sub-interface for sections of route strings which dont require the
 * context of a neighboring section to be resolved appropriately.
 */
@FunctionalInterface
public interface SectionResolver {

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
   * Returns a new {@link SectionResolver} resolving route string sections to concrete airports based on their identifier.
   *
   * @param airportsByIdentifier lookup service providing airports indexed by their identifier (e.g. KATL, KLGA) as they would be
   *                             referenced in an input route string.
   */
  static SectionResolver airport(LookupService<Airport> airportsByIdentifier) {
    return new AirportResolver(airportsByIdentifier);
  }

  /**
   * Returns a new {@link SectionResolver} resolving route string sections to concrete procedure SID/STAR procedure definitions,
   * note that this resolver will mask the
   *
   * @param proceduresByIdentifier lookup service providing procedures indexed by their identifier (e.g. CHPPR1, GNDLF2) as they
   *                               would be referenced in an input route string.
   */
  static SectionResolver sidStar(LookupService<Procedure> proceduresByIdentifier) {
    return new SidStarResolver(proceduresByIdentifier);
  }

  /**
   * Returns a new {@link SectionResolver} resolving route string sections to concrete airways based on their identifier.
   *
   * @param airwaysByIdentifier lookup service providing airways indexed by their identifier (e.g. J121, Y200) as they would be
   *                            referenced in an input route string.
   */
  static SectionResolver airway(LookupService<Airway> airwaysByIdentifier) {
    return new AirwayResolver(airwaysByIdentifier);
  }

  /**
   * Returns a new {@link SectionResolver} resolving route string sections to concrete fixes based on their identifier (contains
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
   * Attempts to resolve a list of candidate {@link ResolvedElement}s from the given current {@link SectionSplit} as well as
   * the preceding and following section splits.
   */
  ResolvedSection resolve(@Nullable SectionSplit previous, SectionSplit current, @Nullable SectionSplit next);

  default List<ResolvedSection> applyTo(List<SectionSplit> sectionSplits) {
    return triplesWithNulls(sectionSplits, this::resolve).collect(Collectors.toList());
  }

  /**
   * Composes the given {@link SectionResolver} with the provided {@link SectionResolver} to produce a new resolver which returns
   * the outputs of both calls to {@link SectionResolver#resolve(SectionSplit, SectionSplit, SectionSplit)} as a single list.
   */
  default SectionResolver compose(SectionResolver that) {
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

  /**
   * Returns the composition of all provided {@link SectionResolver}s as a single SectionResolver.
   */
  static SectionResolver composeAll(SectionResolver... sectionResolvers) {
    checkArgument(sectionResolvers.length > 0, "Cannot provide zero resolvers to composeAll().");
    return Arrays.stream(sectionResolvers).reduce(SectionResolver::compose).orElseThrow(IllegalStateException::new);
  }
}
