package org.mitre.tdp.boogie.conformance.alg.assign.score.source;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.function.Supplier;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

/**
 * Class for returning an appropriate {@link ViterbiFeatureVector} for a {@link Airport} leg.
 */
public final class AirportFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {
  public static final  AirportFeatureExtractor INSTANCE = new AirportFeatureExtractor();
  /**
   * Feature who's presence indicates the {@link ViterbiFeatureVector} is associated with an airport leg.
   */
  public static final String CLASS_TYPE = "IsAirport";
  /**
   * Feature indicating the distance from the airport to the point.
   */
  public static final String DISTANCE_FROM = "DistanceFrom";

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(CLASS_TYPE, (c,f) -> 1.0)
        .addFeatureExtractor(DISTANCE_FROM, this::distance)
        .build();
  }

  /**
   * Extracts the distance between the airport and the point.
   */
  private double distance(ConformablePoint p1, FlyableLeg leg) {
    return leg.current().associatedFix()
        .map(n -> n.distanceInNmTo(p1))
        .orElseThrow(supplier("Leg must have a recommended navaid"));
  }
}
