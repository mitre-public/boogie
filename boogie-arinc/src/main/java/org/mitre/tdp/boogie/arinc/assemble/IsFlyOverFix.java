package org.mitre.tdp.boogie.arinc.assemble;

import java.util.function.Predicate;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.WaypointDescription;

/**
 * This is used to fill out {@link Leg#isFlyOverFix()}.
 */
public final class IsFlyOverFix implements Predicate<ArincProcedureLeg> {

  public static final IsFlyOverFix INSTANCE = new IsFlyOverFix();

  @Override
  public boolean test(ArincProcedureLeg arincProcedureLeg) {
    return arincProcedureLeg.waypointDescription().filter(this::isFlyOverFix).isPresent();
  }

  /**
   * See {@link WaypointDescription}.
   */
  public boolean isFlyOverFix(String waypointDescription) {
    return waypointDescription.charAt(2) == 'B'
        || waypointDescription.charAt(2) == 'Y';
  }
}
