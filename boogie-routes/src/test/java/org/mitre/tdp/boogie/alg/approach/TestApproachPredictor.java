package org.mitre.tdp.boogie.alg.approach;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.alg.RouteExpander;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

public class TestApproachPredictor {

  private ResolvedElement<?> resolvedElement(ElementType type) {
    ResolvedElement<?> element = mock(ResolvedElement.class);
    when(element.type()).thenReturn(type);
    return element;
  }

  private SectionSplit newSplit(String name, int index) {
    return new SectionSplit.Builder().setValue(name).setIndex(index).build();
  }

  private ResolvedSection newSection(String name, int index, List<ResolvedElement<?>> elements) {
    return new ResolvedSection(newSplit(name, index)).setElements(elements);
  }

  @Test
  public void testFilteringNonProcedureElements() {
    ResolvedSection returnSection = newSection("", 3, singletonList(resolvedElement(ElementType.AIRPORT)));

    ApproachPredictor predictor = new ApproachPredictor() {
      @Override
      public void configure(RouteExpander expander) {

      }

      @Override
      public ResolvedSection predictCandidateApproaches(ResolvedSection prev, ResolvedSection last) {
        return returnSection;
      }
    };

    ResolvedSection s1 = newSection("", 1, emptyList());
    ResolvedSection s2 = newSection("", 5, emptyList());

    ResolvedSection checked = predictor.predictAndCheck(s1, s2);
    assertEquals(0, checked.elements().size(), "Bad element types not filtered.");
  }

  @Test
  public void testSectionIndexModification() {
    ResolvedSection returnSection = newSection("", 7, singletonList(resolvedElement(ElementType.PROCEDURE)));

    ApproachPredictor predictor = new ApproachPredictor() {
      @Override
      public void configure(RouteExpander expander) {

      }

      @Override
      public ResolvedSection predictCandidateApproaches(ResolvedSection prev, ResolvedSection last) {
        return returnSection;
      }
    };

    ResolvedSection s1 = newSection("", 1, emptyList());
    ResolvedSection s2 = newSection("", 5, emptyList());

    ResolvedSection checked = predictor.predictAndCheck(s1, s2);
    assertEquals(1, checked.elements().size(), "Element should not have been filtered, index should have been adjusted.");
    assertEquals(5, checked.sectionSplit().index(), "Index not adjusted correctly for checked element.");
    assertEquals(6, s2.sectionSplit().index(), "Index not adjusted correctly for parameter element.");
  }
}
