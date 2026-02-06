package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.BiFunction;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.AfDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.AfFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CaDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CaFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CdDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CdFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CfDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CfFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CiDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CiFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CrDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.CrFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.DfDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.DfFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FaDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FaFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FcDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FcFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FdDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FdFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FmDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.FmFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.IfDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.IfFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.RfDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.RfFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.TfDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.TfFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VaDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VaFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VdDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VdFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.ViDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.ViFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VmDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VmFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VrDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.VrFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.source.AirportDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.source.AirportFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.source.AreaProximityDelegator;
import org.mitre.tdp.boogie.conformance.alg.assign.score.source.AreaProximityScorer;
import org.mitre.tdp.boogie.viterbi.DelegatingFeatureVectorScorer;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

/**
 * A functional class which given a {@link ConformablePoint},{@link FlyableLeg} pair can be used to return the appropriate
 * {@link ViterbiFeatureVectorScorer} for the pair.
 * <p>
 * Generally speaking this class delegates feature extraction to different extractors based on the {@link Leg#pathTerminator()} of the
 * {@link FlyableLeg#current()} leg of the provided flyable leg.
 * <p>
 * The class leaves out the holds and procedure turns as they look pretty different then these legs and we can find them by exclusion.
 */
public final class StandardLegFeatureScorer implements BiFunction<ConformablePoint, FlyableLeg, ViterbiFeatureVectorScorer> {

  /**
   * The wrapped {@link DelegatingFeatureVectorScorer} containing the individual leg type based extractors and their delegation
   * functions.
   */
  private final DelegatingFeatureVectorScorer<ConformablePoint, FlyableLeg> scorer;

  public StandardLegFeatureScorer(DelegatingFeatureVectorScorer<ConformablePoint, FlyableLeg> scorer) {
    this.scorer = scorer;
  }

  public StandardLegFeatureScorer() {
    this.scorer = DelegatingFeatureVectorScorer.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureScorer(new AreaProximityDelegator(), new AreaProximityScorer())
        .addFeatureScorer(new AirportDelegator(), new AirportFeatureScorer())
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

  public DelegatingFeatureVectorScorer<ConformablePoint, FlyableLeg> scorer() {
    return scorer;
  }

  @Override
  public ViterbiFeatureVectorScorer apply(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return scorer.apply(conformablePoint, flyableLeg);
  }
}
