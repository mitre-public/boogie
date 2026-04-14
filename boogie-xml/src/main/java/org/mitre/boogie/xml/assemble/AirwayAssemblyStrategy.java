package org.mitre.boogie.xml.assemble;

import java.util.List;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.model.ArincAirwayLeg;

/**
 * Strategy class for generating user-defined records from XML airway information. Used with {@link AirwayAssembler}.
 */
public interface AirwayAssemblyStrategy<A, F, L> {

  static AirwayAssemblyStrategy<Airway, Fix, Leg> standard() {
    return new Standard();
  }

  /**
   * Converts an XML airway record and its sequence of converted legs into a user-defined type.
   *
   * @param airway the XML airway definition record, provided for access to airway-level metadata
   * @param legs   the sequence of converted airway legs, converted with {@code convertLeg}
   */
  A convertAirway(ArincAirway airway, List<L> legs);

  /**
   * Converts a single XML airway leg and its associated fix + (optional) recommended navaid to a user-defined type.
   *
   * @param leg               the XML airway leg record to convert
   * @param associatedFix     the pre-assembled fix referenced by the leg, may be null if unresolved
   * @param recommendedNavaid the pre-assembled recommended navaid referenced by the leg, may be null
   */
  L convertLeg(ArincAirwayLeg leg, @Nullable F associatedFix, @Nullable F recommendedNavaid);

  final class Standard implements AirwayAssemblyStrategy<Airway, Fix, Leg> {

    private Standard() {
    }

    @Override
    public Airway convertAirway(ArincAirway airway, List<Leg> legs) {
      return Airway.builder()
          .airwayIdentifier(airway.identifier())
          .legs(legs)
          .build();
    }

    @Override
    public Leg convertLeg(ArincAirwayLeg leg, @Nullable Fix associatedFix, @Nullable Fix recommendedNavaid) {
      return Leg.builder(PathTerminator.TF, (int) leg.sequenceNumber())
          .associatedFix(associatedFix)
          .recommendedNavaid(recommendedNavaid)
          .outboundMagneticCourse(leg.outboundCourseValue().orElse(null))
          .theta(leg.theta().orElse(null))
          .rho(leg.rho().orElse(null))
          .rnp(leg.rnp().orElse(null))
          .routeDistance(leg.routeDistanceFrom().orElse(null))
          .build();
    }
  }
}
