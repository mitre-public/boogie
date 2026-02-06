package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.function.Supplier;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

/**
 * Class for returning an appropriate {@link ViterbiFeatureVector} for a {@link PathTerminator#AF} leg.
 */
public final class AfFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {

  /**
   * Singleton instance of the extractor for shared use.
   */
  public static final AfFeatureExtractor INSTANCE = new AfFeatureExtractor();

  /**
   * Feature who's presence indicates the {@link ViterbiFeatureVector} is associated with an AF leg.
   */
  public static final String LEG_TYPE = "IsAf";
  /**
   * Feature indicating whether or not the point falls within the radial angle of the center fix defined by the leg.
   */
  public static final String IN_RADIAL_ANGLE = "InRadialAngle";
  /**
   * Feature indicating the delta between the actual distance from the center fix and the expected distance from the center fix
   * of the point.
   */
  public static final String OFF_TRACK_DISTANCE = "OffTrackDistance";

  private AfFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, f) -> 1.)
        .addFeatureExtractor(IN_RADIAL_ANGLE, this::deriveRadialAngleFeature)
        .addFeatureExtractor(OFF_TRACK_DISTANCE, this::deriveOffTrackFeature)
        .build();
  }

  /**
   * Derives a boolean feature based on the angle from the recommended navaid (used as the center point of the AF arc) to the
   * supplied {@link ConformablePoint} and whether or not it falls between the boundary radials (found in the theta and outbound
   * magnetic course fields of the leg).
   */
  double deriveRadialAngleFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    double fixRadial = flyableLeg.current().theta().orElseThrow(supplier("Theta"));
    double boundaryRadial = flyableLeg.current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));
    TurnDirection turnDirection = flyableLeg.current().turnDirection().orElseThrow(supplier("Turn Direction"));

    Fix navaid = flyableLeg.current().recommendedNavaid().orElseThrow(supplier("Recommended Navaid"));
    MagneticVariation localVariation = MagneticVariationResolver.getInstance().magneticVariation(conformablePoint, flyableLeg);
    double pointRadial = localVariation.trueToMagnetic(navaid.courseInDegrees(conformablePoint));

    return new RadialAngle(boundaryRadial, fixRadial, turnDirection).contains(pointRadial) ? 1. : 0.;
  }

  /**
   * Derives a continuous feature based on the delta between the distance of the point from the arc center and the expected
   * radius of the arc as declared in the {@link Leg#rho()} field.
   */
  double deriveOffTrackFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    Fix navaid = flyableLeg.current().recommendedNavaid().orElseThrow(supplier("Recommended Navaid"));
    double radius = flyableLeg.current().rho().orElseThrow(supplier("Rho"));

    double pointDistance = navaid.distanceInNmTo(conformablePoint);
    return abs(pointDistance - radius);
  }
}
