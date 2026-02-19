package org.mitre.tdp.boogie.dafif.assemble;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import org.mitre.tdp.boogie.dafif.FlyOverIndicator;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalParent;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;

import com.google.common.collect.Range;

public interface ProcedureAssemblyStrategy<P, T, L, F> {

  /**
   * Converts the incoming DAFIF representative procedure leg and the sequence of converted transition (plus derived equipage) to
   * the user-defined type.
   *
   * <p>The DAFIF spec provides fine-grained equipage characteristics for each leg of a procedure. This function provides alongside
   * that information an up-leveled required equipage to the basic categories most clients expect. Deriving this from the
   * DAFIF specification for all the legs in a procedure is non-trivial and easy to get wrong.
   *
   * @param parent                     the parent record which identifies information that applies to the whole procedure, but not enough that we don't still need a representative next.
   * @param representative             a 424 procedure leg elected the "representative" of the transition, provided at the top-level
   *                                   to allow client code easy access to procedure/transition metadata
   * @param transitions                the sequence of converted transition that make up the procedure, converted with {@code .convertTransition(...)}
   */
  P convertProcedure(DafifTerminalParent parent, DafifTerminalSegment representative, List<T> transitions);

  /**
   * Converts the incoming DAFIF representative procedure leg and the sequence of converted legs (plus derived transition type) to
   * the user-defined type.
   *
   * @param representative a DAFIF procedure leg elected the "representative" of the transition, provided at the top-level to allow
   *                       client code easy access to procedure/transition metadata
   * @param legs           the sequence of converted procedure legs that make up the transition, converted with {@code .convertLeg(...)}
   */
  T convertTransition(DafifTerminalSegment representative, List<L> legs);

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
   * @param leg               the DAFIF procedure leg to convert
   * @param associatedFix     the converted associated fix referenced by the leg (there may not be one)
   * @param recommendedNavaid the converted recommended navaid record referenced by the leg (there may not be one)
   * @param centerFix         the converted center fix record referenced by the leg (there may not be one)
   */
  L convertLeg(DafifTerminalSegment leg, @Nullable F associatedFix, @Nullable F recommendedNavaid, @Nullable F centerFix);

  final class Standard implements ProcedureAssemblyStrategy<Procedure, Transition, Leg, Fix> {

    private static final Logger LOG = LoggerFactory.getLogger(Standard.class);

    @Override
    public Procedure convertProcedure(DafifTerminalParent parent, DafifTerminalSegment representative, List<Transition> transitions) {
      ProcedureType procedureType = switch (parent.terminalProcedureType()) {
        case 1 -> ProcedureType.STAR; //yes sid/star are backwards compared to arinc
        case 2 -> ProcedureType.SID;
        case 3 -> ProcedureType.APPROACH;
        default -> throw new IllegalArgumentException("Unknown procedure type: " + parent.terminalProcedureType());
      };

      RequiredNavigationEquipage requiredNavigationEquipage = PbnEquipageEvaluator.INSTANCE.apply(representative);

      LOG.debug("Converting procedure {} at {} with {} transitions", parent.terminalIdentifier(), parent.airportIdentification(), transitions.size());

      return Procedure.builder()
          .procedureIdentifier(parent.terminalIdentifier().split(" ")[0])
          .airportIdentifier(parent.airportIdentification()) //this is the dafif ident not the e.g., icao code
          .procedureType(procedureType)
          .requiredNavigationEquipage(requiredNavigationEquipage)
          .transitions(transitions)
          .build();
    }

    @Override
    public Transition convertTransition(DafifTerminalSegment representative, List<Leg> legs) {
      TransitionType transitionType = TransitionTypeEvaluator.INSTANCE.apply(representative);

      String identifier = TransitionType.MISSED.equals(transitionType)
          ? "MISSED"
          : org.mitre.tdp.boogie.util.StandardizedTransitionName.INSTANCE.apply(representative.transitionIdentifier());

      Set<CategoryOrType> categoryOrType = Set.of(CategoryOrType.NOT_SPECIFIED);

      LOG.debug("Converting transition {} ({}) with {} legs", identifier, transitionType, legs.size());

      return Transition.builder()
          .transitionIdentifier(identifier)
          .transitionType(transitionType)
          .legs(legs)
          .categoryOrTypes(categoryOrType)
          .build();
    }

    @Override
    public Leg convertLeg(DafifTerminalSegment leg, @Nullable Fix associatedFix, @Nullable Fix recommendedNavaid, @Nullable Fix centerFix) {

      PathTerminator pathTerminator = Optional.of(leg)
          .map(DafifTerminalSegment::trackDescriptionCode)
          .filter(PathTerminator.VALID::contains)
          .map(PathTerminator::valueOf)
          .orElseThrow(() -> new IllegalStateException("No valid path terminator for segment " + leg.terminalSequenceNumber() + " in " + leg.terminalIdentifier() + " at " + leg.airportIdentification() + " trackDescCode=" + leg.trackDescriptionCode()));

      LOG.trace("Converting leg seq={} pt={} fix={} at {}", leg.terminalSequenceNumber(), pathTerminator, leg.termSegWaypointIdentifier().orElse("none"), leg.airportIdentification());

      Range<Double> speedConstraint = leg.speedLimit1().map(SpeedLimitToRange.INSTANCE).orElse(Range.all());
      Double alt1 = leg.altitude1().filter(i -> !i.isEmpty()).map(Standard::parseAltitude).orElse(null);
      Double alt2 = leg.altitude2().filter(i -> !i.isEmpty()).map(Standard::parseAltitude).orElse(null);
      Range<Double> altitudeConstraint = leg.altitudeDescription().map(d -> AltitudeConstraintToRange.INSTANCE.apply(d, alt1, alt2))
          .orElse(Range.all());
      TurnDirection turnDirection = leg.terminalSegmentTurnDirection().map(TurnDirector.INSTANCE).orElse(TurnDirection.either());

      Boolean isFlyOverFix = leg.terminalWaypointDescriptionCode2().map(FlyOverIndicator.INSTANCE).orElse(false);

      return Leg.builder(pathTerminator, leg.terminalSequenceNumber())
          .associatedFix(associatedFix)
          .recommendedNavaid(recommendedNavaid)
          .centerFix(centerFix)
          .speedConstraint(speedConstraint)
          .altitudeConstraint(altitudeConstraint)
          .outboundMagneticCourse(leg.terminalMagneticCourse().map(Standard::parseCourse).orElse(null))
          .theta(leg.nav1Bearing().orElse(null))
          .rho(leg.nav1Distance().orElse(null))
          .rnp(leg.requiredNavPerformance().orElse(null))
          .verticalAngle(leg.verticalNavigationVnav().orElse(null))
          .routeDistance(leg.distance().orElse(null))
          .holdTime(null) //fixme not implemented yet.
          .turnDirection(turnDirection)
          .isPublishedHoldingFix(false) //fixme idk if supported.
          .isFlyOverFix(isFlyOverFix)
          .build();
    }
    private static Double parseCourse(String course) {
      String stripped = course.replaceAll("[^0-9.]", "");
      if (stripped.isEmpty()) {
        LOG.warn("Unable to parse course value: '{}'", course);
        return null;
      }
      try {
        return Double.valueOf(stripped);
      } catch (NumberFormatException e) {
        LOG.warn("Unable to parse course value: '{}'", course);
        return null;
      }
    }

    private static Double parseAltitude(String alt) {
      if (alt.startsWith("FL")) {
        return Double.valueOf(alt.substring(2)) * 100.0;
      }
      try {
        return Double.valueOf(alt);
      } catch (NumberFormatException e) {
        LOG.warn("Unable to parse altitude value: '{}'", alt);
        return null;
      }
    }
  }
}
