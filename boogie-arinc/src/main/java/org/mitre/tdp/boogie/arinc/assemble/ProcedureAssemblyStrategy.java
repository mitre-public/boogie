package org.mitre.tdp.boogie.arinc.assemble;

import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.CategoryOrType;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;

/**
 * Strategy class for generating used-defined records from 424 procedure information. Used with {@link ProcedureAssembler}.
 */
public interface ProcedureAssemblyStrategy<P, T, L, F> {

  /**
   * Procedure assembly strategy for building {@link Procedure.Standard}, {@link Transition.Standard}, and {@link Leg.Standard}
   * definitions from procedure records defined in a 424 file.
   */
  static ProcedureAssemblyStrategy<Procedure, Transition, Leg, Fix> standard() {
    return new Standard();
  }

  /**
   * Converts the incoming 424 representative procedure leg and the sequence of converted transition (plus derived equipage) to
   * the user-defined type.
   *
   * <p>The 424 spec provides fine-grained equipage characteristics for each leg of a procedure. This function provides alongside
   * that information an up-leveled required equipage to the basic categories most clients expect. Deriving this from the
   * 424 specification for all the legs in a procedure is non-trivial and easy to get wrong.
   *
   * @param representative             a 424 procedure leg elected the "representative" of the transition, provided at the top-level
   *                                   to allow client code easy access to procedure/transition metadata
   * @param requiredNavigationEquipage the inferred minimum required navigation equipage for the procedure, this is derived from
   *                                   the various fine-grained transition-level equipage codes provided in the 424
   * @param transitions                the sequence of converted transition that make up the procedure, converted with {@code .convertTransition(...)}
   */
  P convertProcedure(ArincProcedureLeg representative, RequiredNavigationEquipage requiredNavigationEquipage, List<T> transitions);

  /**
   * Converts the incoming 424 representative procedure leg and the sequence of converted legs (plus derived transition type) to
   * the user-defined type.
   *
   * @param representative a 424 procedure leg elected the "representative" of the transition, provided at the top-level to allow
   *                       client code easy access to procedure/transition metadata
   * @param transitionType the inferred transition type, the transition type is useful for sequencing likely transition transit
   *                       order (e.g. {@code RUNWAY->COMMON->ENROUTE} for a SID)
   * @param legs           the sequence of converted procedure legs that make up the transition, converted with {@code .convertLeg(...)}
   */
  T convertTransition(ArincProcedureLeg representative, TransitionType transitionType, List<L> legs);

  /**
   * Converts the provided procedure leg and its (optional) associated fix, (optional) recommended navaid, and (optional) center
   * fix to the new user-defined type.
   *
   * <p>The {@link ProcedureAssembler} takes a {@link FixAssemblyStrategy} with this to convert the associated fix, recommended
   * navaid, and center fixes of type {@code F}.
   *
   * <p>Each of the provided fix types may be null as procedures can use any of the {@link PathTerminator} types, many of which
   * don't terminate in fixes or require knowledge of a navaid to fly.
   *
   * @param leg               the 424 procedure leg to convert
   * @param associatedFix     the converted associated fix referenced by the leg (there may not be one)
   * @param recommendedNavaid the converted recommended navaid record referenced by the leg (there may not be one)
   * @param centerFix         the converted center fix record referenced by the leg (there may not be one)
   */
  L convertLeg(ArincProcedureLeg leg, @Nullable F associatedFix, @Nullable F recommendedNavaid, @Nullable F centerFix);

  final class Standard implements ProcedureAssemblyStrategy<Procedure, Transition, Leg, Fix> {

    private Standard() {
    }

    @Override
    public Leg convertLeg(ArincProcedureLeg leg, @Nullable Fix associatedFix, @Nullable Fix recommendedNavaid, @Nullable Fix centerFix) {

      Range<Double> speedConstraint = leg.speedLimitDescription()
          .map(s -> SpeedLimitToRange.INSTANCE.apply(s, leg.speedLimit().map(Integer::doubleValue).orElse(null))).orElse(Range.all());

      Range<Double> altitudeConstraint = leg.altitudeDescription()
          .map(s -> AltitudeLimitToRange.INSTANCE.apply(s, leg.minAltitude1().orElse(null), leg.minAltitude2().orElse(null))).orElse(Range.all());

      return Leg.builder(leg.pathTerm(), leg.sequenceNumber())
          .associatedFix(associatedFix)
          .recommendedNavaid(recommendedNavaid)
          .centerFix(centerFix)
          .speedConstraint(speedConstraint)
          .altitudeConstraint(altitudeConstraint)
          .outboundMagneticCourse(leg.outboundMagneticCourse().orElse(null))
          .theta(leg.theta().orElse(null))
          .rho(leg.rho().orElse(null))
          .rnp(leg.rnp().orElse(null))
          .verticalAngle(leg.verticalAngle().orElse(null))
          .routeDistance(leg.routeDistance().orElse(null))
          .holdTime(leg.holdTime().orElse(null))
          .turnDirection(leg.turnDirection().map(this::toTurnDirection).orElseGet(TurnDirection::either))
          .isPublishedHoldingFix(IsPublishedHoldingFix.INSTANCE.test(leg))
          .isFlyOverFix(IsFlyOverFix.INSTANCE.test(leg))
          .build();
    }

    private static final Set<CategoryOrType> DEFAULT = ImmutableSet.of(CategoryOrType.NOT_SPECIFIED);

    @Override
    public Transition convertTransition(ArincProcedureLeg representative, TransitionType transitionType, List<Leg> legs) {

      String identifier = TransitionType.MISSED.equals(transitionType)
          ? "MISSED"
          : org.mitre.tdp.boogie.util.StandardizedTransitionName.INSTANCE.apply(representative.transitionIdentifier().orElse(null));

      Set<CategoryOrType> categoryOrType = representative.categoryOrType().map(CategoryOrTypeClassifier.INSTANCE).orElse(DEFAULT);

      return Transition.builder()
          .transitionIdentifier(identifier)
          .transitionType(transitionType)
          .legs(legs)
          .categoryOrTypes(categoryOrType)
          .build();
    }

    @Override
    public Procedure convertProcedure(ArincProcedureLeg representative, RequiredNavigationEquipage requiredNavigationEquipage, List<Transition> transitions) {
      return Procedure.builder()
          .procedureIdentifier(representative.sidStarIdentifier()) // or approach...
          .airportIdentifier(representative.airportIdentifier())
          .procedureType(toProcedureType(representative.subSectionCode().orElseThrow()))
          .requiredNavigationEquipage(requiredNavigationEquipage)
          .transitions(transitions)
          .build();
    }

    private TurnDirection toTurnDirection(org.mitre.tdp.boogie.arinc.v18.field.TurnDirection turnDirection) {
      return switch (turnDirection) {
        case L -> TurnDirection.left();
        case R -> TurnDirection.right();
        default -> TurnDirection.either();
      };
    }

    private ProcedureType toProcedureType(String subSectionCode) {
      if ("F".equals(subSectionCode)) {
        return ProcedureType.APPROACH;
      } else if ("E".equals(subSectionCode)) {
        return ProcedureType.STAR;
      } else if ("D".equals(subSectionCode)) {
        return ProcedureType.SID;
      } else {
        throw new IllegalArgumentException("Unable to classify subSectionCode as a ProcedureType: ".concat(subSectionCode));
      }
    }
  }
}
