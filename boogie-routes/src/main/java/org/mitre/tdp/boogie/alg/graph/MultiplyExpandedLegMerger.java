package org.mitre.tdp.boogie.alg.graph;

import java.util.List;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.fn.LeftMerger;

/**
 * When performing the route expansion for simplicity in maintaining the graphical structure we expand fixes referenced as part
 * of airway and procedure sections multiple times. We want to merge these back to singleton references to clean up the final
 * expansions.
 *
 * e.g. HOBBT.GLAVN1.SMAUG -> HOBBT(DF)-HOBBT(TF)-GLAVN(TF)... we want to remove the duplicate HOBBT references as the addition
 * of the DF leg version adds no value in the context of the route even though it is convenient for the graphical expansion.
 */
public interface MultiplyExpandedLegMerger {

  /**
   * Applies a {@link LeftMerger} with internally defined merge criteria to the input collection of legs and reducing the overall
   * list of legs.
   */
  default List<GraphableLeg> mergeLegs(List<GraphableLeg> graphableLegs) {
    LeftMerger<GraphableLeg> leftMerger = new LeftMerger<>(this::mergeable, (l1, l2) -> l1);
    return leftMerger.reduce(graphableLegs);
  }

  /**
   * Indicates the legs are mergeable when the legs are concrete, have matching path terminators and appropriate leg types.
   */
  default boolean mergeable(GraphableLeg left, GraphableLeg right) {
    return left.leg().type().isConcrete() && right.leg().type().isConcrete()
        && matchingPathTerminators(left.leg().pathTerminator(), right.leg().pathTerminator())
        && matchingOrDFLegTypes(left.leg().type(), right.leg().type());
  }

  /**
   * Returns whether the path terminators are matching, or either is a {@link PathTerm#DF}.
   */
  default boolean matchingOrDFLegTypes(PathTerm left, PathTerm right) {
    return left.equals(PathTerm.DF) || right.equals(PathTerm.DF) || right.equals(left);
  }

  /**
   * Returns whether the path termination fixes are non-null and have the same identifier.
   */
  default boolean matchingPathTerminators(Fix left, Fix right) {
    return left != null && right != null && left.identifier() != null && left.identifier().equals(right.identifier());
  }

  static MultiplyExpandedLegMerger newInstance() {
    return new MultiplyExpandedLegMerger() {};
  }
}
