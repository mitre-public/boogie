package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.BiFunction;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.AfDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.AfFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CaDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CaFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CdDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CdFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CfDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CfFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CiDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CiFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CrDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CrFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.DfDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.DfFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FaDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FaFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FcDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FcFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FdDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FdFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FmDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FmFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.IfDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.IfFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.RfDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.RfFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.TfDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.TfFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VaDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VaFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VdDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VdFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.ViDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.ViFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VmDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VmFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VrDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VrFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.source.AirportDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.source.AirportFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.source.AreaProximityDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.source.AreaProximityFeatureExtractor;
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

  public StandardLegFeatureExtractor(DelegatingFeatureVectorExtractor<ConformablePoint, FlyableLeg> extractor) {
    this.extractor = extractor;
  }

  public StandardLegFeatureExtractor() {
    this.extractor = DelegatingFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(new AreaProximityDelegator(), AreaProximityFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new AirportDelegator(), AirportFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new AfDelegator(), AfFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new RfDelegator(), RfFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new DfDelegator(), DfFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new IfDelegator(), IfFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new VaDelegator(), VaFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new CaDelegator(), CaFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new ViDelegator(), ViFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new VmDelegator(), VmFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new CfDelegator(), CfFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new TfDelegator(), TfFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new CiDelegator(), CiFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new FmDelegator(), FmFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new CdDelegator(), CdFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new VdDelegator(), VdFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new FaDelegator(), FaFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new FcDelegator(), FcFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new FdDelegator(), FdFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new VrDelegator(), VrFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor(new CrDelegator(), CrFeatureExtractor.INSTANCE.get())
        .addFeatureExtractor((point, flyableLeg) -> true, ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder().build())
        .build();
  }

  public DelegatingFeatureVectorExtractor<ConformablePoint, FlyableLeg> extractor() {
    return extractor;
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> apply(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return extractor.apply(conformablePoint, flyableLeg);
  }
}
