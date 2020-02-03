package org.mitre.tdp.boogie.alg.approach;

import java.util.List;

import org.mitre.tdp.boogie.alg.RouteExpander;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;

import static org.mitre.tdp.boogie.utils.Collections.filter;

/**
 * The job of an approach predictor is to, given the context of the last and second to last sections
 * of the route string, determine the collection of candidate final approach procedures.
 */
public interface ApproachPredictor {

  /**
   * Provides access to a configured {@link RouteExpander} object for internal use.
   */
  void configure(RouteExpander expander);

  /**
   * Takes the last two resolved sections of the route string and attempts to resolve an in-between
   * section for the final approach procedure.
   */
  ResolvedSection predictCandidateApproaches(ResolvedSection prev, ResolvedSection last);

  /**
   * Runs the approach prediction and then checks the output meets the expected contract for the
   * resolved section and elements.
   *
   * {@link ApproachPredictor#checkResolvedElementTypes(ResolvedSection)}
   * {@link ApproachPredictor#checkSectionSplitInformation(ResolvedSection, ResolvedSection, ResolvedSection)}
   */
  default ResolvedSection predictAndCheck(ResolvedSection prev, ResolvedSection last) {
    ResolvedSection result = predictCandidateApproaches(prev, last);

    checkResolvedElementTypes(result);
    checkSectionSplitInformation(result, prev, last);

    return result;
  }

  /**
   * Checks that the resolved section contains elements tagged as {@link ElementType#PROCEDURE}
   * filtering any non-procedure elements.
   */
  default void checkResolvedElementTypes(ResolvedSection resolvedSection) {
    List<ResolvedElement<?>> allowedElements = filter(
        resolvedSection.elements(),
        element -> element.type().equals(ElementType.PROCEDURE));
    resolvedSection.setElements(allowedElements);
  }

  /**
   * Checks that the section split index has is between the prev/last resolution sections provided
   * to the prediction logic.
   */
  default void checkSectionSplitInformation(ResolvedSection resolvedSection, ResolvedSection prev, ResolvedSection last) {
    int prevIndex = prev.sectionSplit().index();
    int lastIndex = last.sectionSplit().index();
    int approachIndex = resolvedSection.sectionSplit().index();
    if (approachIndex <= prevIndex || approachIndex >= lastIndex) {
      last.setSectionSplit(last.sectionSplit().builder().setIndex(lastIndex + 1).build());
      resolvedSection.setSectionSplit(last.sectionSplit().builder().setIndex(lastIndex).build());
    }
  }
}
