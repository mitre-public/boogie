package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.function.Supplier;

import org.mitre.caasd.commons.HasPosition;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

public final class DfFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {

  /**
   * Singleton instance of the extractor for shared use.
   */
  public static final DfFeatureExtractor INSTANCE = new DfFeatureExtractor();

  /**
   * Feature who's presence indicates the {@link ViterbiFeatureVector} is associated with an DF leg.
   */
  public static final String LEG_TYPE = "IsDf";
  /**
   * Feature representing the distance from the fix the aircraft is at the projected crossing point based on the current course
   * of the aircraft and its current distance to the fix.
   */
  public static final String PROJECTED_DISTANCE_OFFSET = "ProjectedDistanceOffset";

  private DfFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, f) -> 1.)
        .addFeatureExtractor(PROJECTED_DISTANCE_OFFSET, this::deriveProjectedDistanceOffset)
        .build();
  }

  /**
   * Computes the projected offset of the aircraft from the direct-to fix given the current distance of the aircraft from the
   * fix and the current course of the aircraft.
   */
  double deriveProjectedDistanceOffset(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    Fix pathTerminator = flyableLeg.current().pathTerminator();

    double distance = conformablePoint.distanceInNmTo(pathTerminator);
    double pointCourse = conformablePoint.trueCourse().orElseThrow(supplier("Point Course"));

    HasPosition projectedPosition = conformablePoint.projectOut(pointCourse, distance);
    return projectedPosition.distanceInNmTo(pathTerminator);
  }
}
