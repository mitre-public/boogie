package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;


public final class VrFeatureScorer implements ViterbiFeatureVectorScorer {
  private final Function<Double, Double> offCourseWeight;
  private final Function<Double, Double> everHitsDmeWeight;

  public VrFeatureScorer() {
    this(
        simpleLogistic(15., 30.),
        simpleLogistic(.1, .5)
    );
  }

  public VrFeatureScorer(Function<Double, Double> offCourseWeight, Function<Double, Double> everHitsDmeWeight) {
    this.offCourseWeight = offCourseWeight;
    this.everHitsDmeWeight = everHitsDmeWeight;
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(VrFeatureExtractor.LEG_TYPE));

    double degreesOffCourse = viterbiFeatureVector.featureValue(VrFeatureExtractor.DEGREES_OFF_COURSE);
    double distanceFromRadial = viterbiFeatureVector.featureValue(VrFeatureExtractor.NEAREST_YOU_GOT);

    return offCourseWeight.apply(degreesOffCourse) * everHitsDmeWeight.apply(distanceFromRadial);
  }
}

