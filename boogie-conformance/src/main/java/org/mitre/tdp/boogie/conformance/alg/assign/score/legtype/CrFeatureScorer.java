package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

public final class CrFeatureScorer implements ViterbiFeatureVectorScorer {
  private final Function<Double, Double> offCourseWeight;
  private final Function<Double, Double> everHitsDmeWeight;

  public CrFeatureScorer() {
    this(
        simpleLogistic(5., 15),
        simpleLogistic(.1, .5)
    );
  }

  public CrFeatureScorer(Function<Double, Double> offCourseWeight, Function<Double, Double> everHitsDmeWeight) {
    this.offCourseWeight = offCourseWeight;
    this.everHitsDmeWeight = everHitsDmeWeight;
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(CrFeatureExtractor.LEG_TYPE));

    double degreesOffCourse = viterbiFeatureVector.featureValue(CrFeatureExtractor.DEGREES_OFF_COURSE);
    double distanceFromRadial = viterbiFeatureVector.featureValue(CrFeatureExtractor.NEAREST_YOU_GOT);

    return offCourseWeight.apply(degreesOffCourse) * everHitsDmeWeight.apply(distanceFromRadial);
  }
}

