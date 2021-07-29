package org.mitre.tdp.boogie.alg.graph;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.fn.LeftMerger;

public final class LegMergerFactory {

  private LegMergerFactory() {
    throw new IllegalStateException("Unable to instantiate static factory class.");
  }

  public static LeftMerger<GraphableLeg> newSimilarSubsequentGraphableLegMerger() {
    return new LeftMerger<>((g1, g2) -> subsequentSimilarLegs(g1.leg(), g2.leg()), (l1, l2) -> l1);
  }

  /**
   * When performing the route expansion for simplicity in maintaining the graphical structure we expand fixes referenced as part
   * of airway and procedure sections multiple times. We want to merge these back to singleton references to clean up the final
   * expansions.
   * <br>
   * e.g. HOBBT.GLAVN1.SMAUG -> HOBBT(DF)-HOBBT(TF)-GLAVN(TF)... we want to remove the duplicate HOBBT references as the addition
   * of the DF leg version adds no value in the context of the route even though it is convenient for the graphical expansion.
   */
  public static LeftMerger<Leg> newSimilarSubsequentLegMerger() {
    return new LeftMerger<>(LegMergerFactory::subsequentSimilarLegs, (l1, l2) -> l1);
  }

  private static boolean subsequentSimilarLegs(Leg left, Leg right) {
    boolean matchingOrDfPathTerminators = PathTerminator.DF.equals(left.pathTerminator())
        || PathTerminator.DF.equals(right.pathTerminator())
        || right.pathTerminator().equals(left.pathTerminator());

    boolean matchingAssociatedFixIdentifiers = left.associatedFix().isPresent()
        && right.associatedFix().isPresent()
        && left.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier()
        .equalsIgnoreCase(right.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier());

    return matchingOrDfPathTerminators && matchingAssociatedFixIdentifiers;
  }
}
