package org.mitre.tdp.boogie.conformance.alg.assign.score.source;

import java.util.Optional;
import java.util.function.Supplier;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.generate.AreaProximity;
import org.mitre.tdp.boogie.conformance.alg.assign.generate.AreaProximityVisitor;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

import com.google.common.annotations.Beta;

/**
 * Extracts features from a {@link AreaProximity} leg.
 */
@Beta
public final class AreaProximityFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {
  public static final AreaProximityFeatureExtractor INSTANCE = new AreaProximityFeatureExtractor();

  /**
   * Feature who's presence indicates the {@link ViterbiFeatureVector} is associated with an area proximity leg.
   */
  public static final String CLASS_TYPE = "IsAreaProximity";
  /**
   * Feature indicating the distance from the center of the area to the point.
   */
  public static final String DISTANCE_RATIO = "DistanceRatio";


  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(CLASS_TYPE, (c, f) -> 1.)
        .addFeatureExtractor(DISTANCE_RATIO, this::deriveDistanceRatioFromAirport)
        .build();
  }

  private Double deriveDistanceRatioFromAirport(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    AreaProximity area = Optional.of(flyableLeg.current())
        .flatMap(AreaProximityVisitor::getAreaProximity)
        .orElseThrow(() -> new IllegalArgumentException("Should not be here"));
    LatLong center = area.latLong();
    double distance = area.maxDistance();
    return center.distanceInNM(conformablePoint.latLong()) / distance;
  }
}
