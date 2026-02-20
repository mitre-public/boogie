package org.mitre.tdp.boogie.dafif.assemble;

import java.util.List;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;

/**
 * Strategy class for generating user-defined records from DAFIF ATS information. Used with {@link AirwayAssembler}.
 *
 * <p>Each DAFIF ATS segment contains two waypoints (start and end). An airway's full fix sequence is derived by
 * walking the segment chain: {@code [seg0.wpt1, seg0.wpt2, seg1.wpt2, ..., segN.wpt2]}. This produces N+1 legs from
 * N segments.
 *
 * @param <A> the airway type
 * @param <F> the fix type
 * @param <L> the leg type
 */
public interface AirwayAssemblyStrategy<A, F, L> {

  static AirwayAssemblyStrategy<Airway, Fix, Leg> standard() {
    return new Standard();
  }

  /**
   * Converts the incoming DAFIF representative ATS segment and the sequence of converted legs into the new user-defined type.
   *
   * @param representative a DAFIF ATS segment elected the "representative", provided at the top-level to allow client code
   *                       easy access to airway metadata
   * @param legs           the sequence of converted airway legs that make up the airway, converted with {@code .convertLeg(...)}
   */
  A convertAirway(DafifAirTrafficSegment representative, List<L> legs);

  /**
   * Converts the start of the airway chain into the first leg. The associated fix is waypoint1 of the first segment.
   *
   * @param firstSegment  the first ATS segment in the chain, provided for access to airway metadata
   * @param startFix      the converted start fix (waypoint1 of the first segment)
   */
  L convertStartLeg(DafifAirTrafficSegment firstSegment, @Nullable F startFix);

  /**
   * Converts the provided ATS segment into a leg. The associated fix is waypoint2 (end fix) of the segment, and the
   * segment provides the route distance, outbound course, and other metadata for the connection TO this fix.
   *
   * @param segment       the DAFIF ATS segment to convert
   * @param associatedFix the converted end fix (waypoint2) of the segment
   */
  L convertLeg(DafifAirTrafficSegment segment, @Nullable F associatedFix);

  final class Standard implements AirwayAssemblyStrategy<Airway, Fix, Leg> {

    private Standard() {
    }

    @Override
    public Airway convertAirway(DafifAirTrafficSegment representative, List<Leg> legs) {
      return Airway.builder()
          .airwayIdentifier(representative.atsIdentifier())
          .legs(legs)
          .build();
    }

    @Override
    public Leg convertStartLeg(DafifAirTrafficSegment firstSegment, @Nullable Fix startFix) {
      return Leg.builder(PathTerminator.IF, 0)
          .associatedFix(startFix)
          .build();
    }

    @Override
    public Leg convertLeg(DafifAirTrafficSegment segment, @Nullable Fix associatedFix) {
      return Leg.builder(PathTerminator.TF, segment.atsRouteSequenceNumber())
          .associatedFix(associatedFix)
          .outboundMagneticCourse(segment.atsRouteOutboundMagneticCourse().map(Standard::parseCourse).orElse(null))
          .routeDistance(segment.atsRouteDistance().orElse(null))
          .rnp(segment.requiredNavPerformance().map(Double::valueOf).orElse(null))
          .build();
    }

    private static Double parseCourse(String course) {
      String stripped = course.endsWith("T") ? course.substring(0, course.length() - 1) : course;
      return Double.valueOf(stripped);
    }
  }
}
