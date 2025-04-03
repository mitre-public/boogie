package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.BiFunction;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.DelegatingFeatureVectorExtractor;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

/**
 * A functional class which given a {@link ConformablePoint},{@link FlyableLeg} pair can be used to return the appropriate
 * {@link ViterbiFeatureVectorExtractor} for the pair.
 *
 * Generally speaking this class delegates feature extraction to different extractors based on the {@link Leg#pathTerminator()} of the
 * {@link FlyableLeg#current()} leg of the provided flyable leg.
 */
public final class StandardLegFeatureExtractor implements BiFunction<ConformablePoint, FlyableLeg, ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {

  /**
   * The wrapped {@link DelegatingFeatureVectorExtractor} containing the individual leg type based extractors and their delegation
   * functions.
   */
  private final DelegatingFeatureVectorExtractor<ConformablePoint, FlyableLeg> extractor;

  public StandardLegFeatureExtractor() {
    this.extractor = DelegatingFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(new AfDelegator(), AfFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new CaDelegator(), CaFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new CfDelegator(), CfFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new DfDelegator(), DfFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new IfDelegator(), IfFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new RfDelegator(), RfFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new TfDelegator(), TfFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new VaDelegator(), VaFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new ViDelegator(), ViFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new VmDelegator(), VmFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new FmDelegator(), FmFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor((point, flyableLeg) -> true, ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder().build())
        .build();
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> apply(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return extractor.apply(conformablePoint, flyableLeg);
  }
}
