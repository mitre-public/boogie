package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

import com.google.common.base.Preconditions;

/**
 * Wrapper class for a {@link PrecomputedAssigner} serving the pre-computed leg assignments as a {@link LegAssigner}.
 */
public class PrecomputedAssignerWrapper implements LegAssigner {

  private final PrecomputedAssigner assigner;
  private Map<ConformablePoint, FlyableLeg> assignments;

  public PrecomputedAssignerWrapper(PrecomputedAssigner assigner) {
    this.assigner = assigner;
    this.assignments = new HashMap<>();
  }

  public PrecomputedAssigner assigner() {
    return assigner;
  }

  @Override
  public FlyableLeg assignmentFor(ConformablePoint conformablePoint) {
    Preconditions.checkArgument(!assignments.isEmpty(), "Pre-compute step has not been run yet.");
    return assignments.get(conformablePoint);
  }

  /**
   * Pre-computes and saves the returned mapping as an internal field.
   */
  public void precompute(Collection<ConformablePoint> allPoints, Collection<FlyableLeg> allLegs) {
    this.assignments = assigner.assignments(allPoints, allLegs);
  }

  /**
   * Returns a new {@link PrecomputedAssigner} which has already had the {@link #precompute(Collection, Collection)} called.
   */
  public static PrecomputedAssignerWrapper wrapAndPrecompute(PrecomputedAssigner assigner, Collection<ConformablePoint> allPoints, Collection<FlyableLeg> allLegs) {
    PrecomputedAssignerWrapper wrapper = new PrecomputedAssignerWrapper(assigner);
    wrapper.precompute(allPoints, allLegs);
    return wrapper;
  }
}
