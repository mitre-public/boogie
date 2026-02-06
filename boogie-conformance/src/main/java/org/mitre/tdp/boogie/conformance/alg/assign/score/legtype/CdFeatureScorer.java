package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;


public final class CdFeatureScorer implements ViterbiFeatureVectorScorer {
  private final Function<Double, Double> offCourseWeight;
  private final Function<Double, Double> everHitsDmeWeight;

  public CdFeatureScorer() {
    this(
        simpleLogistic(5, 15),
        simpleLogistic(.1, .5)
    );
  }

  public CdFeatureScorer(Function<Double, Double> offCourseWeight, Function<Double, Double> everHitsDmeWeight) {
    this.offCourseWeight = offCourseWeight;
    this.everHitsDmeWeight = everHitsDmeWeight;
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(CdFeatureExtractor.LEG_TYPE));

    double degreesOffCourse = viterbiFeatureVector.featureValue(CdFeatureExtractor.DEGREES_OFF_COURSE);
    double distanceToDme = viterbiFeatureVector.featureValue(CdFeatureExtractor.CROSS_TRACK_DISTANCE);

    return offCourseWeight.apply(degreesOffCourse) * everHitsDmeWeight.apply(distanceToDme);
  }
}
