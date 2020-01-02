package org.mitre.tdp.boogie.alg.resolve;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Ordering;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedRoute.SECTION_COMPARATOR;

public class TestResolvedRoute {

  private SectionSplit newSplit(String name, int index) {
    return new SectionSplit.Builder().setValue(name).setIndex(index).build();
  }

  private ResolvedSection newSection(String name, int index) {
    return new ResolvedSection(newSplit(name, index));
  }

  private boolean isOrdered(List<ResolvedSection> resolvedSections) {
    return Ordering.from(SECTION_COMPARATOR).isOrdered(resolvedSections);
  }

  @Test
  public void testSectionAtOutOfBounds() {
    ResolvedRoute route = new ResolvedRoute(singletonList(newSection("KATL", 0)));
    assertThrows(IllegalArgumentException.class, () -> route.sectionAt(1));
  }

  @Test
  public void testResolvedRouteSortedByIndex() {
    ResolvedRoute route = new ResolvedRoute(Arrays.asList(
        newSection("KATL", 0),
        newSection("GOLFR2", 2),
        newSection("WAYCO", 4)));

    assertTrue(isOrdered(route.sections()));
  }

  @Test
  public void testResolvedRouteInsertSorted() {
    ResolvedRoute route = new ResolvedRoute(Arrays.asList(
        newSection("KATL", 0),
        newSection("GOLFR2", 2),
        newSection("WAYCO", 4)));

    route.insert(newSection("J121", 5));
    assertTrue(isOrdered(route.sections()), "Insertion at end of route broken.");
    assertEquals("J121", route.sectionAt(3).sectionSplit().value(), "Element not inserted at end of route.");

    route.insert(newSection("KATL2", -1));
    assertTrue(isOrdered(route.sections()), "Insertion at start of route broken.");
    assertEquals("KATL2", route.sectionAt(0).sectionSplit().value(), "Element not inserted at start of route.");

    route.insert(newSection("WILCO", 4));
    assertTrue(isOrdered(route.sections()), "Insertion with tied index is broken.");
    assertEquals("WILCO", route.sectionAt(4).sectionSplit().value(), "Element not inserted at shared location of route.");

    route.insert(newSection("JIMBO", 1));
    assertTrue(isOrdered(route.sections()), "General insertion unsupported.");
    assertEquals("JIMBO", route.sectionAt(2).sectionSplit().value());
  }
}
