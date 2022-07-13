package org.mitre.tdp.boogie.alg.resolve;

import java.util.Optional;
import javax.annotation.Nullable;

/**
 * This class tests to see if an approach procedure name matches up with a runway.
 * You can see names like I03, H03RZ, R24, VOR-A, L04LZ
 */
public class IsApproachForRunway {

  private IsApproachForRunway() {

  }

  public static boolean test(String approachName, String runwayNumber, @Nullable String parallelDesignator) {
    return Optional.ofNullable(parallelDesignator)
        .map(pd -> withParallelDesignator(approachName, runwayNumber, parallelDesignator))
        .orElse(normalRunways(approachName, runwayNumber));
  }

  /**
   * Very important to skip the first letter of the approach name because it can be L and R, which is also a parallel designator
   */
  private static boolean withParallelDesignator(String approachName, String runwayNumber, @Nullable String parallelDesignator) {
    return Optional.ofNullable(parallelDesignator)
        .map(pd -> approachName.contains(runwayNumber) && approachName.substring(1).contains(pd))
        .orElse(false);
  }

  private static boolean normalRunways(String approachName, String runwayNumber) {
    return approachName.contains(runwayNumber);
  }
}
