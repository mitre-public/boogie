package org.mitre.boogie.xml.assemble;

import java.util.List;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.boogie.xml.model.ArincProcedure;
import org.mitre.boogie.xml.model.ArincProcedureLeg;
import org.mitre.boogie.xml.model.ArincTransition;

/**
 * Strategy class for converting XML model procedure records into client-defined procedure data models.
 *
 * <p>This class can be used with {@link ProcedureAssemblyStrategy#standard()} to generate lightweight Boogie-defined
 * {@link Procedure} implementations.
 */
public interface ProcedureAssemblyStrategy<P, T, L, F> {

  static ProcedureAssemblyStrategy<Procedure, Transition, Leg, Fix> standard() {
    return new Standard();
  }

  /**
   * Converts an {@link ArincProcedure} and its sequence of already-converted transitions into a user-defined procedure.
   *
   * @param procedure         the XML procedure record
   * @param airportIdentifier the identifier of the parent airport/heliport
   * @param transitions       the converted transitions, produced by {@link #convertTransition}
   */
  P convertProcedure(ArincProcedure procedure, String airportIdentifier, List<T> transitions);

  /**
   * Converts an {@link ArincTransition} and its sequence of already-converted legs into a user-defined transition.
   *
   * @param procedure  the parent procedure, provided for access to procedure-level metadata
   * @param transition the XML transition record
   * @param legs       the converted legs, produced by {@link #convertLeg}
   */
  T convertTransition(ArincProcedure procedure, ArincTransition transition, List<L> legs);

  /**
   * Converts an {@link ArincProcedureLeg} and its resolved fixes into a user-defined leg.
   *
   * @param leg              the XML procedure leg record
   * @param associatedFix    the pre-assembled fix referenced by the leg's fixRef, may be null
   * @param recommendedNavaid the pre-assembled recommended navaid referenced by the leg's recNavaidRef, may be null
   * @param centerFix        the pre-assembled center fix referenced by the leg's centerFixRef, may be null
   */
  L convertLeg(ArincProcedureLeg leg, @Nullable F associatedFix, @Nullable F recommendedNavaid, @Nullable F centerFix);

  final class Standard implements ProcedureAssemblyStrategy<Procedure, Transition, Leg, Fix> {

    private Standard() {
    }

    @Override
    public Procedure convertProcedure(ArincProcedure procedure, String airportIdentifier, List<Transition> transitions) {
      return Procedure.builder()
          .procedureIdentifier(procedure.identifier())
          .airportIdentifier(airportIdentifier)
          .procedureType(toProcedureType(procedure.procedureType()))
          .requiredNavigationEquipage(toRequiredNavigationEquipage(procedure))
          .transitions(transitions)
          .build();
    }

    @Override
    public Transition convertTransition(ArincProcedure procedure, ArincTransition transition, List<Leg> legs) {
      return Transition.builder()
          .transitionIdentifier(transition.identifier().orElse(null))
          .transitionType(toTransitionType(transition.transitionType()))
          .legs(legs)
          .build();
    }

    @Override
    public Leg convertLeg(ArincProcedureLeg leg, @Nullable Fix associatedFix, @Nullable Fix recommendedNavaid, @Nullable Fix centerFix) {
      return Leg.builder(toPathTerminator(leg.pathAndTermination()), (int) leg.sequenceNumber())
          .associatedFix(associatedFix)
          .recommendedNavaid(recommendedNavaid)
          .centerFix(centerFix)
          .outboundMagneticCourse(leg.courseValue().orElse(null))
          .theta(leg.theta().orElse(null))
          .rho(leg.rho().orElse(null))
          .rnp(leg.rnp().orElse(null))
          .routeDistance(leg.legDistance().orElse(null))
          .verticalAngle(leg.verticalAngle().orElse(null))
          .turnDirection(leg.turnDirection().map(Standard::toTurnDirection).orElseGet(TurnDirection::either))
          .isFlyOverFix(leg.isFlyOver().orElse(false))
          .isPublishedHoldingFix(leg.isHolding().orElse(false))
          .isIntermediateOrInitialApproachFix(leg.isInitialApproachFix().orElse(false) || leg.isIntermediateApproachFix().orElse(false))
          .build();
    }

    private static ProcedureType toProcedureType(String procedureType) {
      return switch (procedureType) {
        case "Sid" -> ProcedureType.SID;
        case "Star" -> ProcedureType.STAR;
        case "Approach" -> ProcedureType.APPROACH;
        default -> throw new IllegalArgumentException("Unknown procedure type: " + procedureType);
      };
    }

    private static TransitionType toTransitionType(String transitionType) {
      return switch (transitionType) {
        case "SidRunwayTransition", "StarRunwayTransition" -> TransitionType.RUNWAY;
        case "SidCommonRoute", "StarCommonRoute" -> TransitionType.COMMON;
        case "SidEnrouteTransition", "StarEnrouteTransition" -> TransitionType.ENROUTE;
        case "ApproachTransition" -> TransitionType.APPROACH;
        case "FinalApproach" -> TransitionType.COMMON;
        case "MissedApproach" -> TransitionType.MISSED;
        default -> TransitionType.COMMON;
      };
    }

    private static RequiredNavigationEquipage toRequiredNavigationEquipage(ArincProcedure procedure) {
      if (procedure.isRnav().orElse(false)) {
        return RequiredNavigationEquipage.RNAV;
      }
      return RequiredNavigationEquipage.CONV;
    }

    private static PathTerminator toPathTerminator(String pathAndTermination) {
      try {
        return PathTerminator.valueOf(pathAndTermination);
      } catch (IllegalArgumentException e) {
        return PathTerminator.TF;
      }
    }

    private static TurnDirection toTurnDirection(String turnDirection) {
      return switch (turnDirection) {
        case "LEFT" -> TurnDirection.left();
        case "RIGHT" -> TurnDirection.right();
        default -> TurnDirection.either();
      };
    }
  }
}
