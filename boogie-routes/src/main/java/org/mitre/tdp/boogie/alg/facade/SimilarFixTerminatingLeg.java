package org.mitre.tdp.boogie.alg.facade;

import org.mitre.tdp.boogie.Leg;

import java.util.function.BiPredicate;

public final class SimilarFixTerminatingLeg implements BiPredicate<Leg, Leg> {
  public static final SimilarFixTerminatingLeg INSTANCE = new SimilarFixTerminatingLeg();
  private static final MatchingFixIdents MATCHING_FIX_IDENTS = MatchingFixIdents.INSTANCE;
  private SimilarFixTerminatingLeg() {}
  @Override
  public boolean test(Leg previous, Leg next) {
    return previous.pathTerminator().isFixTerminating()
        && next.pathTerminator().isFixTerminating()
        && MATCHING_FIX_IDENTS.test(previous, next);
  }
}
