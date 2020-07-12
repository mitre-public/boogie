package org.mitre.tdp.boogie.alg.resolve;

import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.tailored;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.alg.RouteExpander;
import org.mitre.tdp.boogie.alg.graph.RouteLegGraph;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;
import org.mitre.tdp.boogie.alg.resolve.element.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.element.AirwayElement;
import org.mitre.tdp.boogie.alg.resolve.element.FixElement;
import org.mitre.tdp.boogie.alg.resolve.element.LatLonElement;
import org.mitre.tdp.boogie.alg.resolve.element.ProcedureElement;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.resolve.element.TailoredElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;
import org.mitre.tdp.boogie.service.LookupService;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

/**
 * The SectionResolver serves the purpose of taking the sections output by the
 * {@link SectionSplitter} and assigning to them matching infrastructure elements
 * returning a collection of {@link ResolvedSection}s which can be handed
 * off to the {@link RouteLegGraph} for additional analysis.
 *
 * <p>This will match the provided route section split/id to any and all elements
 * sharing a common identifier. The graph solution handles the resolution of
 * these multiple options into a single path.
 */
@FunctionalInterface
public interface SectionResolver {

  static SectionResolver with(RouteExpander routes) {
    return () -> routes;
  }

  /**
   * The route inflation object containing the configured infrastructure elements
   * to use in the expansion.
   */
  RouteExpander inflator();

  /**
   * The two empty split values cases are tailored waypoints and direct to
   * waypoints, in either case we want to push the wildcards from the blank
   * section onto the next.
   */
  default boolean pushWildcards(SectionSplit split) {
    return Strings.isNullOrEmpty(split.value());
  }

  /**
   * Resolves all of the {@link SectionSplit}s against the infrastructure information
   * returning for each section a set of possible infrastructure elements it might have
   * been referring to.
   *
   * <p>See {@link ResolvedSection}.
   */
  default ResolvedRoute resolve(List<SectionSplit> splits) {
    List<ResolvedSection> sections = new ArrayList<>();
    sections.add(resolve(splits.get(0)));
    IntStream.range(1, splits.size())
        .filter(i -> !pushWildcards(splits.get(i)))
        .forEach(i -> {
          SectionSplit c = splits.get(i);
          SectionSplit p = splits.get(i - 1);

          ResolvedSection section = resolve(c);

          if (pushWildcards(p)) {
            // if previous section is direct e.g. ""
            // if previous section indicates truncated e.g. "/"
            // tag next resolved section with wildcards
            section.sectionSplit()
                .setWildcards(section.sectionSplit().wildcards() + p.wildcards());
          }

          // previous section input is empty and this isn't a tailored waypoint this is a direct
          sections.add(section);
        });
    return new ResolvedRoute(sections);
  }

  /**
   * Returns a {@link ResolvedSection} containing all of the possible infrastructure
   * elements
   */
  default ResolvedSection resolve(SectionSplit split) {
    String id = split.value();
    ResolvedSection section = new ResolvedSection(split);
    return section.setElements(resolve(id));
  }

  default List<ResolvedElement<?>> resolve(String... ids) {
    return Stream.of(ids).map(this::resolve).flatMap(Collection::stream).collect(Collectors.toList());
  }

  /**
   * Resolves the provided identifier to a collection of {@link ResolvedElement}s based on the
   * various {@link LookupService}s configured in the referenced {@link RouteExpander}.
   */
  default List<ResolvedElement<?>> resolve(String id) {
    Iterable<ResolvedElement<?>> elements = Iterables.concat(
        airport(id),
        fix(id),
        procedure(id),
        airway(id),
        Collections.singletonList(latLon(id)));

    return StreamSupport.stream(elements.spliterator(), false)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  default List<ResolvedElement<?>> airport(String section) {
    return inflator().airportService()
        .allMatchingIdentifiers(section)
        .stream()
        .map(AirportElement::new)
        .collect(Collectors.toList());
  }

  /**
   * Attempts to find a fix which matches the id of the section.
   */
  default List<ResolvedElement<?>> fix(String section) {
    // check to see if the parsed section is a tailored waypoint reference
    // if so extract the course/distance suffix from the fix identifier
    String s = section.matches(tailored().pattern())
        ? section.substring(0, section.length() - 6)
        : section;

    return inflator().fixService()
        .allMatchingIdentifiers(s).stream()
        .map(fix -> section.equals(s)
            ? new FixElement(fix)
            : new TailoredElement(section, fix))
        .collect(Collectors.toList());
  }

  /**
   * Attempts to find a a procedure who's identifier matches the section.
   */
  default List<ResolvedElement<?>> procedure(String section) {
    Collection<ProcedureGraph> procedures = inflator().procedureService()
        .allMatchingIdentifiers(section);
    // Procedure objects dont handle multi-source, have to group by here
    return procedures.stream()
        // pre-filter approach procedures
        .filter(p -> !p.type().equals(ProcedureType.APPROACH))
        .map(ProcedureElement::new)
        .collect(Collectors.toList());
  }

  /**
   * Attempts to find and airway matching the section.
   */
  default List<ResolvedElement<?>> airway(String section) {
    return inflator().airwayService().allMatchingIdentifiers(section)
        .stream()
        .map(AirwayElement::new)
        .collect(Collectors.toList());
  }

  /**
   * Attempts to parse the section identifier as a Lat/Lon coordinate.
   */
  default ResolvedElement<?> latLon(String section) {
    boolean match = section.matches(SectionHeuristics.latLon().pattern());
    return match ? LatLonElement.from(section) : null;
  }
}
