package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.alg.split.RouteToken;

/**
 * Wrapper class for a section containing context flags from the filed route string itself (e.g. direct/suppressed) as well
 * as a collection of possible infrastructure elements that were determined to be associated with the section id via the
 * {@link RouteTokenResolver}.
 */
public final class ResolvedSection {

  /**
   * The split section with wildcards and etc associated with this collection of route elements.
   */
  private final RouteToken sectionSplit;
  /**
   * List of resolved infrastructure elements.
   *
   * <p>This is the superset of all potential elements the filed section of the route string could possible be referring to.
   */
  private final Collection<ResolvedElement> elements;

  public ResolvedSection(RouteToken split, Collection<ResolvedElement> resolvedElements) {
    this.sectionSplit = requireNonNull(split);
    this.elements = resolvedElements;
  }

  public RouteToken sectionSplit() {
    return sectionSplit;
  }

  public Collection<ResolvedElement> elements() {
    return elements;
  }

  public List<LinkedLegs> allLegs() {
    return elements.stream().flatMap(e -> e.toLinkedLegs().stream()).collect(Collectors.toList());
  }
}
