package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.io.Serializable;
import java.util.function.BiFunction;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.FeatureBasedViterbiScoringStrategy;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

public final class StandardScoringStrategy implements FeatureBasedViterbiScoringStrategy<ConformablePoint, FlyableLeg>, Serializable {
  private final BiFunction<ConformablePoint, FlyableLeg, ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> extractor;
  private final BiFunction<ConformablePoint, FlyableLeg, ViterbiFeatureVectorScorer> scorer;

  public StandardScoringStrategy(BiFunction<ConformablePoint, FlyableLeg, ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> extractor, BiFunction<ConformablePoint, FlyableLeg, ViterbiFeatureVectorScorer> scorer) {
    this.extractor = extractor;
    this.scorer = scorer;
  }

  public static StandardScoringStrategy from(BiFunction<ConformablePoint, FlyableLeg, ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> extractor, BiFunction<ConformablePoint, FlyableLeg, ViterbiFeatureVectorScorer> scorer) {
    return new StandardScoringStrategy(extractor, scorer);
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> featureExtractor(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return extractor.apply(conformablePoint, flyableLeg);
  }

  @Override
  public ViterbiFeatureVectorScorer featureVectorScorer(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return scorer.apply(conformablePoint, flyableLeg);
  }
}
