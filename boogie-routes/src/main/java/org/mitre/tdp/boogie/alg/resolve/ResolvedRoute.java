package org.mitre.tdp.boogie.alg.resolve;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import com.google.common.base.Preconditions;

import static org.mitre.tdp.boogie.utils.Collections.sort;

/**
 * A collection of {@link ResolvedSection}s tied to all of the splits in the filed route string.
 */
public class ResolvedRoute {

  /**
   * Comparator for {@link ResolvedSection}s within the route.
   */
  static Comparator<ResolvedSection> SECTION_COMPARATOR = Comparator.comparing(section -> section.sectionSplit().index());

  private final List<ResolvedSection> sections;

  public ResolvedRoute(List<ResolvedSection> sections) {
    this.sections = sort(sections, SECTION_COMPARATOR);
  }

  public List<ResolvedSection> sections() {
    return sections;
  }

  /**
   * Provides indexed and checked access to the underlying list of resolved sections.
   */
  public ResolvedSection sectionAt(int loc) {
    Preconditions.checkArgument(loc >= 0 && loc < sections.size(),
        "Index out of range: " + loc + ":" + sectionCount());
    return sections.get(loc);
  }

  /**
   * The total number of sections in the resolved route.
   */
  public Integer sectionCount() {
    return sections.size();
  }

  /**
   * Inserts the provided section at the appropriate index in the route based on the internal
   * index provided in the to-be-inserted section.
   */
  public void insert(ResolvedSection section) {
    OptionalInt insertionIndex = IntStream.range(0, sectionCount())
        .filter(i -> sectionAt(i).sectionSplit().index() > section.sectionSplit().index()).min();
    if (insertionIndex.isPresent()) {
      sections.add(insertionIndex.getAsInt(), section);
    } else {
      sections.add(section);
    }
  }
}
