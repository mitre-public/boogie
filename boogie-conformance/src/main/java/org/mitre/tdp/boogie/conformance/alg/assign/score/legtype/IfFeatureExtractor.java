package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.function.Supplier;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

public final class IfFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {

  public static final IfFeatureExtractor INSTANCE = new IfFeatureExtractor();

  /**
   * Feature who's presence indicates the {@link ViterbiFeatureVector} is associated with an IF leg.
   */
  public static final String LEG_TYPE = "IsIf";
  /**
   * Feature representing the distance (in nm) from the fix of the aircraft.
   */
  public static final String DISTANCE_FROM_FIX = "DistanceFromFix";

  private IfFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, l) -> 1.)
        .addFeatureExtractor(DISTANCE_FROM_FIX, this::deriveDistanceFromFix)
        .build();
  }

  double deriveDistanceFromFix(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return conformablePoint.distanceInNmTo(flyableLeg.current().associatedFix().orElseThrow(supplier("Associated Fix")));
  }
}
