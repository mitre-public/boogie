package org.mitre.tdp.boogie.arinc.assemble;

import java.util.function.Predicate;

import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

/**
 * Per the 424 specification a 'M' in the second character of the fix description of a leg indicates its the first leg of a missed
 * approach. Generally these legs are coded into the same transition as the final approach - but it's often valuable to have them
 * split out into their own {@link Transition} records and tagged with {@link TransitionType#MISSED} in downstream applications.
 */
public final class IsFirstLegOfMissedApproach implements Predicate<ArincProcedureLeg> {

  public static final IsFirstLegOfMissedApproach INSTANCE = new IsFirstLegOfMissedApproach();

  @Override
  public boolean test(ArincProcedureLeg arincProcedureLeg) {
    return arincProcedureLeg.waypointDescription().filter(description -> description.charAt(2) == 'M').isPresent();
  }
}
