package org.mitre.tdp.boogie.arinc.assemble;

import java.util.function.Predicate;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.WaypointDescription;

/**
 * This is used to fill out the {@link Leg#isIntermediateOrInitialApproachFix()} field.
 */
public final class IsIntermediateOrInitialApproachFix implements Predicate<ArincProcedureLeg> {

  public static final IsIntermediateOrInitialApproachFix INSTANCE = new IsIntermediateOrInitialApproachFix();

  private IsIntermediateOrInitialApproachFix() {
  }

  @Override
  public boolean test(ArincProcedureLeg arincProcedureLeg) {
    return arincProcedureLeg.waypointDescription().filter(this::isIntermediateOrInitialApproachFix).isPresent();
  }

  /**
   * See {@link WaypointDescription} column 4: A = Initial approach fix, B = Intermediate approach fix,
   * C = Holding at initial approach fix, D = Initial approach fix at FACF.
   */
  boolean isIntermediateOrInitialApproachFix(String waypointDescription) {
    char col4 = waypointDescription.charAt(3);
    return col4 == 'A' || col4 == 'B' || col4 == 'C' || col4 == 'D';
  }
}
