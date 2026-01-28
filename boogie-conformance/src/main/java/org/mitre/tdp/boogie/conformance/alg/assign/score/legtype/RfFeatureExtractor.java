package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.function.Supplier;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.evaluate.LegPair;
import org.mitre.tdp.boogie.conformance.alg.evaluate.MaxOffTrackDistanceEvaluator;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

public final class RfFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {

  public static final RfFeatureExtractor INSTANCE = new RfFeatureExtractor();

  /**
   * Feature who's presence indicates the {@link ViterbiFeatureVector} is associated with an RF leg.
   */
  public static final String LEG_TYPE = "IsRf";
  /**
   * Feature indicating the delta between the actual distance from the center fix and the expected distance from the center fix
   * of the point.
   */
  public static final String OFF_TRACK_DISTANCE = "OffTrackDistance";

  private RfFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, f) -> 1.)
        .addFeatureExtractor(OFF_TRACK_DISTANCE, this::deriveOffTrackDistanceFeature)
        .build();
  }

  /**
   * Returns the off track distance of the point from the RF leg via the {@link MaxOffTrackDistanceEvaluator}.
   */
  double deriveOffTrackDistanceFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    LegPair asLegPair = new LegPair(flyableLeg.previous().orElseThrow(supplier("No previous leg")), flyableLeg.current());

    return MaxOffTrackDistanceEvaluator.offTrackDistance(conformablePoint, asLegPair)
        .map(Distance::inNauticalMiles)
        .orElseThrow(supplier("Unable to compute off track distance."));
  }

  static double tangentToRadial(Double tangentialTrueBearing, TurnDirection turnDirection) {
    return Spherical.mod(tangentialTrueBearing + (turnDirection.isLeft() ? 90 : -90), 360.);
  }

  static double radialToTangent(Double radialTrueBearing, TurnDirection turnDirection) {
    return Spherical.mod(radialTrueBearing - (turnDirection.isLeft() ? 90. : -90.), 360.);
  }
}
