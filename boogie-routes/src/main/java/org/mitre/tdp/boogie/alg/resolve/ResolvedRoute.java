package org.mitre.tdp.boogie.alg.resolve;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

/**
 * A collection of {@link ResolvedSection}s tied to all of the splits in the filed route string.
 */
public final class ResolvedRoute {

  /**
   * Comparator for {@link ResolvedSection}s within the route.
   */
  static Comparator<ResolvedSection> SECTION_COMPARATOR = Comparator.comparing(section -> section.sectionSplit().index());

  private final TreeMap<Integer, ResolvedSection> sections;

  public ResolvedRoute(List<ResolvedSection> sections) {
    this.sections = new TreeMap<>();
    sections.forEach(section -> this.sections.put(section.sectionSplit().index(), section));
  }

  public List<ResolvedSection> sections() {
    return new ArrayList<>(sections.values());
  }

  /**
   * Provides indexed and checked access to the underlying list of resolved sections.
   */
  public Optional<ResolvedSection> sectionAt(int index) {
    return Optional.ofNullable(sections.get(index));
  }

  /**
   * The total number of sections in the resolved route.
   */
  public Integer sectionCount() {
    return sections.size();
  }
}
