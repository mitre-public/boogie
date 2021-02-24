package org.mitre.tdp.boogie.alg.approach;

import static org.mitre.tdp.boogie.util.Collections.filter;

import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;

/**
 * The job of an approach predictor is to, given the context of the last and second to last sections
 * of the route string, determine the collection of candidate final approach procedures.
 */
@FunctionalInterface
public interface ApproachPredictor {

  /**
   * Takes the last two resolved sections of the route string and attempts to resolve an in-between
   * section for the final approach procedure.
   */
  Optional<ResolvedSection> predictCandidateApproaches(ResolvedSection prev, ResolvedSection last);

  /**
   * Runs the approach prediction and then checks the output meets the expected contract for the
   * resolved section and elements.
   *
   * {@link ApproachPredictor#checkResolvedElementTypes(ResolvedSection)}
   * {@link ApproachPredictor#checkSectionSplitInformation(ResolvedSection, ResolvedSection, ResolvedSection)}
   */
  default Optional<ResolvedSection> predictAndCheck(ResolvedSection prev, ResolvedSection last) {
    return predictCandidateApproaches(prev, last)
        .map(this::checkResolvedElementTypes)
        .map(resolvedSection -> checkSectionSplitInformation(resolvedSection, prev, last));
  }

  /**
   * Checks that the resolved section contains elements tagged as {@link ElementType#APPROACH}
   * filtering any non-approach elements (i.e. you cant assign a STAR with the approach predictor).
   */
  default ResolvedSection checkResolvedElementTypes(ResolvedSection resolvedSection) {
    List<ResolvedElement<?>> allowedElements = filter(
        resolvedSection.elements(),
        element -> element.type().equals(ElementType.APPROACH));
    return resolvedSection.setElements(allowedElements);
  }

  /**
   * Checks that the section split index has is between the prev/last resolution sections provided
   * to the prediction logic.
   */
  default ResolvedSection checkSectionSplitInformation(ResolvedSection resolvedSection, ResolvedSection prev, ResolvedSection last) {
    int prevIndex = prev.sectionSplit().index();
    int lastIndex = last.sectionSplit().index();
    int approachIndex = resolvedSection.sectionSplit().index();
    if (approachIndex <= prevIndex || approachIndex >= lastIndex) {
      last.setSectionSplit(last.sectionSplit().builder().setIndex(lastIndex + 1).build());
      resolvedSection.setSectionSplit(last.sectionSplit().builder().setIndex(lastIndex).build());
    }
    return resolvedSection;
  }
}
