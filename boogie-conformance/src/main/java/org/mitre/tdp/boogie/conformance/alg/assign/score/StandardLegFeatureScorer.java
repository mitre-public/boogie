package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.BiFunction;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.DelegatingFeatureVectorScorer;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

/**
 * A functional class which given a {@link ConformablePoint},{@link FlyableLeg} pair can be used to return the appropriate
 * {@link ViterbiFeatureVectorScorer} for the pair.
 * <p>
 * Generally speaking this class delegates feature extraction to different extractors based on the {@link Leg#pathTerminator()} of the
 * {@link FlyableLeg#current()} leg of the provided flyable leg.
 */
public final class StandardLegFeatureScorer implements BiFunction<ConformablePoint, FlyableLeg, ViterbiFeatureVectorScorer> {

  /**
   * The wrapped {@link DelegatingFeatureVectorScorer} containing the individual leg type based extractors and their delegation
   * functions.
   */
  private final DelegatingFeatureVectorScorer<ConformablePoint, FlyableLeg> scorer;

  public StandardLegFeatureScorer() {
    this.scorer = DelegatingFeatureVectorScorer.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureScorer(new AfDelegator(), new AfFeatureScorer())
        .addFeatureScorer(new RfDelegator(), new RfFeatureScorer())
        .addFeatureScorer(new DfDelegator(), new DfFeatureScorer())
        .addFeatureScorer(new IfDelegator(), new IfFeatureScorer())
        .addFeatureScorer(new VaDelegator(), new VaFeatureScorer())
        .addFeatureScorer(new CaDelegator(), new CaFeatureScorer())
        .addFeatureScorer(new ViDelegator(), new ViFeatureScorer())
        .addFeatureScorer(new VmDelegator(), new VmFeatureScorer())
        .addFeatureScorer(new CfDelegator(), new CfFeatureScorer())
        .addFeatureScorer(new TfDelegator(), new TfFeatureScorer())
        .addFeatureScorer(new CiDelegator(), new CiFeatureScorer())
        .addFeatureScorer(new FmDelegator(), new FmFeatureScorer())
        .addFeatureScorer(new VdDelegator(), new VdFeatureScorer())
        .addFeatureScorer(new CdDelegator(), new CdFeatureScorer())
        .addFeatureScorer(new FaDelegator(), new FaFeatureScorer())
        .addFeatureScorer(new FcDelegator(), new FcFeatureScorer())
        .addFeatureScorer(new FdDelegator(), new FdFeatureScorer())
        .addFeatureScorer(new VrDelegator(), new VrFeatureScorer())
        .addFeatureScorer(new CrDelegator(), new CrFeatureScorer())
        .addFeatureScorer((conformablePoint, flyableLeg) -> true, viterbiFeatureVector -> 1e-30)
        .build();
  }

  @Override
  public ViterbiFeatureVectorScorer apply(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return scorer.apply(conformablePoint, flyableLeg);
  }
}
