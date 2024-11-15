package org.mitre.tdp.boogie.arinc.assemble;

import java.util.function.Predicate;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.WaypointDescription;

/**
 * This is used to fill out the {@link Leg#isPublishedHoldingFix()} field.
 */
public final class IsPublishedHoldingFix implements Predicate<ArincProcedureLeg> {

  public static final IsPublishedHoldingFix INSTANCE = new IsPublishedHoldingFix();

  @Override
  public boolean test(ArincProcedureLeg arincProcedureLeg) {
    return arincProcedureLeg.waypointDescription().filter(this::isPublishedHoldingFix).isPresent();
  }

  /**
   * See {@link WaypointDescription}.
   */
  boolean isPublishedHoldingFix(String waypointDescription) {
    return waypointDescription.charAt(3) == 'C'
        || waypointDescription.charAt(3) == 'H';
  }
}
