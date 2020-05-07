package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.Map;

import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;

/**
 * Wrapper class for a {@link PrecomputedAssigner} serving the pre-computed leg assignments as a {@link LegAssigner}.
 */
public class PrecomputedAssignerWrapper implements LegAssigner {

  private final PrecomputedAssigner assigner;
  private final Map<ConformablePoint, ConsecutiveLegs> assignments;

  public PrecomputedAssignerWrapper(PrecomputedAssigner assigner, Map<ConformablePoint, ConsecutiveLegs> assignments) {
    this.assigner = assigner;
    this.assignments = assignments;
  }

  public PrecomputedAssigner assigner() {
    return assigner;
  }

  @Override
  public ConsecutiveLegs assignmentFor(ConformablePoint conformablePoint) {
    return assignments.get(conformablePoint);
  }
}
