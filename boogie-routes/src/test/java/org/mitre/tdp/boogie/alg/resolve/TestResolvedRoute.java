package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.resolve.ResolvedRoute.SECTION_COMPARATOR;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

import com.google.common.collect.Ordering;

class TestResolvedRoute {

  @Test
  void testResolvedRouteSortedByIndex() {
    ResolvedRoute route = new ResolvedRoute(Arrays.asList(
        newSection("KATL", 0),
        newSection("GOLFR2", 2),
        newSection("WAYCO", 4)));

    assertTrue(isOrdered(route.sections()));
  }

  private SectionSplit newSplit(String name, int index) {
    return new SectionSplit.Builder().setValue(name).setIndex(index).build();
  }

  private ResolvedSection newSection(String name, int index) {
    return new ResolvedSection(newSplit(name, index));
  }

  private boolean isOrdered(List<ResolvedSection> resolvedSections) {
    return Ordering.from(SECTION_COMPARATOR).isOrdered(resolvedSections);
  }
}
