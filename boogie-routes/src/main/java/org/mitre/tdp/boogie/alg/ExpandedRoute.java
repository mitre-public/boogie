package org.mitre.tdp.boogie.alg;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.alg.resolve.ResolvedLeg;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.fn.LeftMerger;

/**
 * Final output container for the expanded route information.
 *
 * Contains both the input route string targeted for expansion as well as the full leg expansion of the route labeled by section.
 */
public final class ExpandedRoute {

  private static final LeftMerger<ResolvedLeg> merger = LegMergerFactory.newSimilarSubsequentGraphableLegMerger();

  /**
   * The input route string used to generate this expansion.
   */
  private final String route;
  /**
   * Mapping from {@link SectionSplit} to {@link ResolvedSection} containing the candidate infrastructure elements which were
   * resolved for each portion of the route string.
   */
  private final TreeMap<SectionSplit, ResolvedSection> sectionMap;
  /**
   * Mapping from {@link SectionSplit} to ordered list of traversed {@link ResolvedLeg}s within that section of the route.
   */
  private final TreeMap<SectionSplit, List<ResolvedLeg>> legMap;

  // alternate view(s) of underlying data - don't want to have to copy the map values on each call
  private final List<ResolvedSection> sections;
  private final List<ResolvedLeg> legs;

  public ExpandedRoute(
      String route,
      TreeMap<SectionSplit, ResolvedSection> sectionMap,
      TreeMap<SectionSplit, List<ResolvedLeg>> legMap) {
    this.route = checkNotNull(route);
    this.sectionMap = checkNotNull(sectionMap);
    this.legMap = checkNotNull(legMap);

    this.sections = new ArrayList<>(sectionMap.values());
    this.legs = legMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
  }

  public String route() {
    return route;
  }

  /**
   * Returns the {@link ResolvedSection} associated with the given {@link SectionSplit}.
   */
  public ResolvedSection sectionFor(SectionSplit split) {
    return sectionMap.get(split);
  }

  public List<ResolvedSection> sections() {
    return sections;
  }

  /**
   * Returns the collection of legs associated with the given {@link SectionSplit} or {@link Collections#emptyList()} if there
   * were none resolved in the expansion.
   */
  public List<ResolvedLeg> legsFor(SectionSplit split) {
    return legMap.getOrDefault(split, Collections.emptyList());
  }

  /**
   * Returns the list of {@link ResolvedLeg}s for the given {@link ResolvedSection}, returning the empty list if none exist.
   */
  public List<ResolvedLeg> legsFor(ResolvedSection section) {
    return legMap.getOrDefault(section.sectionSplit(), Collections.emptyList());
  }

  public List<ResolvedLeg> legs() {
    return legs;
  }

  public List<ResolvedLeg> mergedLegs() {
    return merger.reduce(legs());
  }

  /**
   * Package-private method allowing for certain approved transforms to wholly replace the collection of legs underneath the
   * {@link ExpandedRoute}, e.g. via the {@link RunwayTransitionAppender}.
   */
  final ExpandedRoute replaceLegs(List<ResolvedLeg> legs) {
    this.legMap.clear();
    this.legMap.putAll(collectToMap(legs));

    this.legs.clear();
    this.legs.addAll(legs);
    return this;
  }

  /**
   * Generates a new {@link ExpandedRoute} based on the provided resolved route and the given set of {@link ResolvedLeg}s.
   */
  public static ExpandedRoute from(String route, List<ResolvedSection> resolvedSections, List<ResolvedLeg> resolvedLegs) {
    TreeMap<SectionSplit, ResolvedSection> sectionMap = new TreeMap<>();
    resolvedSections.forEach(section -> sectionMap.put(section.sectionSplit(), section));

    return new ExpandedRoute(route, sectionMap, collectToMap(resolvedLegs));
  }

  private static TreeMap<SectionSplit, List<ResolvedLeg>> collectToMap(List<ResolvedLeg> resolvedLegs) {
    return resolvedLegs.stream().collect(Collectors.groupingBy(ResolvedLeg::split, TreeMap::new, Collectors.toList()));
  }
}
