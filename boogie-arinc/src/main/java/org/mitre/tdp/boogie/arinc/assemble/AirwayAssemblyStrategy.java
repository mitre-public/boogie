package org.mitre.tdp.boogie.arinc.assemble;

import java.util.List;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;

import com.google.common.collect.Range;

/**
 * Strategy class for generating used-defined records from 424 information. Used with {@link AirwayAssembler}.
 */
public interface AirwayAssemblyStrategy<A, F, L> {

  /**
   * Airway assembly strategy for building {@link Airway.Standard} and {@link Leg.Standard} definitions from airway and fix-like
   * records defined in a 424 file.
   */
  static AirwayAssemblyStrategy<Airway, Fix, Leg> standard() {
    return new Standard();
  }

  /**
   * Converts the incoming 424 representative airway leg and the sequence of converted legs into the new user-defined type.
   *
   * @param representative a 424 airway leg elected the "representative", provided at the top-level to allow client code easy
   *                       access to airway metadata
   * @param legs           the sequence of converted airway legs that make up the airway, converted with {@code .convertLeg(...)}
   */
  A convertAirway(ArincAirwayLeg representative, List<L> legs);

  /**
   * Converts the provided airway leg and its associated fix + (optional) recommended navaid to the new user defined type.
   *
   * <p>The {@link AirwayAssembler} takes a {@link FixAssemblyStrategy} with this to convert the associated fix and recommended
   * navaids of type {@code F}.
   *
   * <p>Airway legs are never arc legs, so no center fix is provided.
   *
   * @param leg               the 424 airway leg record to convert
   * @param associatedFix     the converted associated fix record referenced by the leg
   * @param recommendedNavaid the converted recommended navaid record referenced by the leg (there may not be one)
   */
  L convertLeg(ArincAirwayLeg leg, F associatedFix, @Nullable F recommendedNavaid);

  final class Standard implements AirwayAssemblyStrategy<Airway, Fix, Leg> {

    private Standard() {
    }

    @Override
    public Airway convertAirway(ArincAirwayLeg representative, List<Leg> legs) {
      return Airway.builder().airwayIdentifier(representative.routeIdentifier()).legs(legs).build();
    }

    @Override
    public Leg convertLeg(ArincAirwayLeg leg, Fix associatedFix, @Nullable Fix recommendedNavaid) {

      Range<Double> altitudeConstraint = AirwayAltitudeRange.INSTANCE.apply(
          leg.minAltitude1().orElse(null),
          leg.minAltitude2().orElse(null),
          leg.maxAltitude().orElse(null)
      );

      return Leg.builder(PathTerminator.TF, leg.sequenceNumber())
          .associatedFix(associatedFix)
          .recommendedNavaid(recommendedNavaid)
          .altitudeConstraint(altitudeConstraint)
          .outboundMagneticCourse(leg.outboundMagneticCourse().orElse(null))
          .theta(leg.theta().orElse(null))
          .rho(leg.rho().orElse(null))
          .rnp(leg.rnp().orElse(null))
          .routeDistance(leg.routeDistance().orElse(null))
          .holdTime(leg.holdTime().orElse(null))
          .build();
    }
  }
}
