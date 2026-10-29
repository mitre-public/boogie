package org.mitre.tdp.boogie.alg.facade;

import org.mitre.tdp.boogie.Leg;

import java.util.function.BiPredicate;

/**
 * Identifies if the two legs have the same fix by identifier.  Use this after all the
 * namespacing is resolved and you don't need that level of assurance.
 */
public final class MatchingFixIdents implements BiPredicate<Leg, Leg> {
  public static final MatchingFixIdents INSTANCE = new MatchingFixIdents();
  private MatchingFixIdents() {}
  @Override
  public boolean test(Leg left, Leg right) {
    return left.associatedFix().isPresent()
        && right.associatedFix().isPresent()
        && left.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier()
        .equalsIgnoreCase(right.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier());
  }
}
