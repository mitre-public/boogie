package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.caasd.commons.HasPosition;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * This is the default conformance scorer for {@link PathTerm#DF} legs.
 *
 * The class uses the projected distance offset from the DF path terminator to score the leg as being flown.
 */
public class DfScorer implements OnLegScorer {

  private final Function<Double, Double> distanceWeight;

  public DfScorer() {
    this.distanceWeight = simpleLogistic(1.0, 2.0);
  }

  public DfScorer(Function<Double, Double> distanceWeight) {
    this.distanceWeight = distanceWeight;
  }

  public Function<Double, Double> distanceWeight() {
    return distanceWeight;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint that, FlyableLeg legTriple) {
    Fix pathTerminator = legTriple.current().pathTerminator();

    double distance = that.distanceInNmTo(pathTerminator);
    double pointCourse = that.trueCourse().orElseThrow(supplier("Point Course"));

    HasPosition projectedPosition = that.projectOut(pointCourse, distance);
    return distanceWeight().apply(projectedPosition.distanceInNmTo(pathTerminator));
  }
}
