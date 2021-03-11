package org.mitre.tdp.boogie.alg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.resolve.ResolvedRoute;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.element.AirportElement;
import org.mitre.tdp.boogie.alg.split.IfrFormatSectionSplitter;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

class TestRunwayTransitionAppender {

  private static final RunwayTransitionAppender appender = new RunwayTransitionAppender(Optional::empty, Optional::empty);

  @Test
  void testHasSidSectionCombination() {
    ResolvedSection s1 = new ResolvedSection(SID.get(0)).setElements(Collections.singletonList(airport()));
    ResolvedSection s2 = new ResolvedSection(SID.get(1));

    assertFalse(appender.hasSidSectionCombination(expandedRoute(s1, s2)));
  }

  @Test
  void testHasStarSectionCombination() {
    ResolvedSection s1 = new ResolvedSection(STAR.get(1));
    ResolvedSection s2 = new ResolvedSection(STAR.get(2)).setElements(Collections.singletonList(airport()));

    assertFalse(appender.hasStarSectionCombination(expandedRoute(s1, s2)));
  }

  private ExpandedRoute expandedRoute(ResolvedSection... sections) {
    return ExpandedRoute.from("", new ResolvedRoute(Arrays.asList(sections)), Collections.emptyList());
  }

  private AirportElement airport() {
    Airport airport = mock(Airport.class);
    return new AirportElement("", airport);
  }

  private static final IfrFormatSectionSplitter splitter = new IfrFormatSectionSplitter();

  private static final List<SectionSplit> SID = splitter.splits("KATL.WONZR5.DRSDN");
  private static final List<SectionSplit> STAR = splitter.splits("DRSDN.WONZR5.KATL");
}
