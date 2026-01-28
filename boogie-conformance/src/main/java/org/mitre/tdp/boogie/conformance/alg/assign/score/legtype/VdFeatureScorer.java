package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

public final class VdFeatureScorer implements ViterbiFeatureVectorScorer {
  private final Function<Double, Double> offCourseWeight;
  private final Function<Double, Double> everHitsDmeWeight;

  public VdFeatureScorer() {
    this(
        simpleLogistic(15., 30.),
        simpleLogistic(.5, 2)
    );
  }

  public VdFeatureScorer(Function<Double, Double> offCourseWeight, Function<Double, Double> everHitsDmeWeight) {
    this.offCourseWeight = offCourseWeight;
    this.everHitsDmeWeight = everHitsDmeWeight;
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(VdFeatureExtractor.LEG_TYPE));

    double degreesOffCourse = viterbiFeatureVector.featureValue(VdFeatureExtractor.DEGREES_OFF_COURSE);
    double distanceToDme = viterbiFeatureVector.featureValue(VdFeatureExtractor.CURRENT_BEST_DME);

    return offCourseWeight.apply(degreesOffCourse) * everHitsDmeWeight.apply(distanceToDme);
  }
}

