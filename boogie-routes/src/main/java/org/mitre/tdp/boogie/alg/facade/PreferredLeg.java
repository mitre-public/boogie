package org.mitre.tdp.boogie.alg.facade;

import java.util.function.BiFunction;

import static org.mitre.tdp.boogie.alg.resolve.ElementType.*;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRWAY;

/**
 * This class takes the 'simple' element types and finds which leg should be used to keep constraints from.
 */
public final class PreferredLeg implements BiFunction<ExpandedRouteLeg, ExpandedRouteLeg, ExpandedRouteLeg> {
  public static final PreferredLeg INSTANCE = new PreferredLeg();
  private PreferredLeg() {}
  @Override
  public ExpandedRouteLeg apply(ExpandedRouteLeg previous, ExpandedRouteLeg next) {
    if (APPROACH.equals(next.elementType()) || STAR.equals(next.elementType())) {
      return next;
    } else if (SID.equals(previous.elementType())) {
      return previous;
    } else if (AIRWAY.equals(next.elementType())) {
      return next;
    } else if (AIRWAY.equals(previous.elementType())) {
      return previous;
    } else {
      return previous;
    }
  }
}
