package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.groupingBy;
import static org.mitre.caasd.commons.util.Partitioners.splitOnPairwiseChange;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.arinc.database.FixDatabase;
import org.mitre.tdp.boogie.arinc.database.TerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.model.BoogieLeg;
import org.mitre.tdp.boogie.model.BoogieProcedure;
import org.mitre.tdp.boogie.model.BoogieTransition;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Range;

/**
 * Functional class for converting a collection of {@link ArincProcedureLeg}s into a sequence of {@link Procedure} records as
 * would be expected in downstream data classes in packages like boogie-routes.
 * <br>
 * This class leverages instances of the {@link TerminalAreaDatabase} and {@link FixDatabase} to lookup:
 * <br>
 * {@link Leg#associatedFix()}
 * {@link Leg#recommendedNavaid()}
 * {@link Leg#centerFix()}
 * <br>
 * The lookup is performed by the external class {@link LegFixDereferencer}.
 */
public final class ProcedureAssembler implements Function<Collection<ArincProcedureLeg>, Stream<Procedure>> {

  private static final ArincTransitionTypeClassifier transitionTypeClassifier = new ArincTransitionTypeClassifier();

  private static final ArincRequiredEquipageClassifier requiredEquipageClassifier = new ArincRequiredEquipageClassifier();

  private final ArincProcedureLegConverter inflator;
  /**
   * Predicate for determining whether there should be any special splitting logic applied to sequential legs within a procedure
   * transition (e.g. to split the missed approach off of the final approach).
   */
  private final BiPredicate<ArincProcedureLeg, ArincProcedureLeg> shouldSplitTransition;

  public ProcedureAssembler(TerminalAreaDatabase terminalAreaDatabase, FixDatabase fixDatabase) {
    this(terminalAreaDatabase, fixDatabase, (l1, l2) -> IsFirstLegOfMissedApproach.INSTANCE.test(l2));
  }

  public ProcedureAssembler(
      TerminalAreaDatabase terminalAreaDatabase,
      FixDatabase fixDatabase,
      BiPredicate<ArincProcedureLeg, ArincProcedureLeg> shouldSplitTransition) {
    this.inflator = new ArincProcedureLegConverter(terminalAreaDatabase, fixDatabase);
    this.shouldSplitTransition = requireNonNull(shouldSplitTransition);
  }

  @Override
  public Stream<Procedure> apply(Collection<ArincProcedureLeg> arincProcedureLegs) {
    return arincProcedureLegs.stream()
        .sorted(comparing(ArincProcedureLeg::sequenceNumber))
        .collect(groupingBy(this::procedureGroupKey))
        .values().stream()
        .map(this::toProcedure);
  }

  /**
   * Converts the list of {@link ArincProcedureLeg}s known to be part of the same procedure into a composite {@link Procedure}
   * object. This method uses two helper classes to provided value-add features:
   * <br>
   * 1. {@link #transitionTypeClassifier} to assign COMMON/ENROUTE/RUNWAY, etc. types to transitions
   * 2. {@link #requiredEquipageClassifier} to up-level the detailed ARINC 424 procedure types (and qualifiers) into manageable
   * sub-categories of CONV/RNAV/RNP.
   */
  private Procedure toProcedure(List<ArincProcedureLeg> arincProcedureLegs) {

    Collection<List<ArincProcedureLeg>> byTransition = arincProcedureLegs.stream()
        .collect(Collectors.groupingBy(leg -> leg.transitionIdentifier().orElse("ALL"))).values();

    Multimap<TransitionType, List<ArincProcedureLeg>> byType = LinkedHashMultimap.create();

    byTransition.stream().map(this::repartition).flatMap(Collection::stream)
        .forEach(transition -> byType.put(transitionTypeClassifier.apply(transition), transition));

    RequiredNavigationEquipage equipage = requiredEquipageClassifier.apply(byType);

    List<Transition> transitions = byType.entries().stream()
        .map(entry -> newTransitionFrom(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());

    return newProcedureFrom(equipage, transitions);
  }

  /**
   * Re-partitions the name-grouped transition legs based on the configured {@link #shouldSplitTransition} predicate.
   */
  private List<List<ArincProcedureLeg>> repartition(List<ArincProcedureLeg> procedureLegs) {
    return splitOnPairwiseChange(procedureLegs, (ls, next) -> shouldSplitTransition.negate().test(ls.get(ls.size() - 1), next));
  }

  /**
   * Converts the list of related {@link Transition}s into a {@link Procedure} which downstream algorithms can consume and operate
   * on more easily.
   */
  private Procedure newProcedureFrom(RequiredNavigationEquipage equipage, List<Transition> transitions) {
    Transition representative = transitions.get(0);
    return new BoogieProcedure.Builder()
        .procedureIdentifier(representative.procedureIdentifier())
        .airportIdentifier(representative.airportIdentifier())
        .airportRegion(representative.airportRegion())
        .procedureType(representative.procedureType())
        .requiredNavigationEquipage(equipage)
        .transitions(transitions)
        .build();
  }

  /**
   * Generates a new transition of the given type from the collection of input legs. {@link TransitionType#MISSED} transitions
   * will be given the name "MISSED".
   */
  private Transition newTransitionFrom(TransitionType transitionType, List<ArincProcedureLeg> procedureLegs) {
    ArincProcedureLeg representative = procedureLegs.get(0);
    return new BoogieTransition.Builder()
        .procedureIdentifier(representative.sidStarIdentifier())
        .airportIdentifier(representative.airportIdentifier())
        .airportRegion(representative.airportIcaoRegion())
        .transitionIdentifier(TransitionType.MISSED.equals(transitionType)
            ? "MISSED"
            : StandardizedTransitionName.INSTANCE.apply(representative.transitionIdentifier().orElse(null)))
        .procedureType(toProcedureType(representative.subSectionCode()))
        .transitionType(transitionType)
        .legs(procedureLegs.stream().map(inflator).collect(Collectors.toList()))
        .build();
  }

  /**
   * The subSection within the database determines whether the procedure is a SID/STAR/APPROACH record.
   */
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

  private String procedureGroupKey(ArincProcedureLeg arincProcedureLeg) {
    return arincProcedureLeg.airportIdentifier()
        .concat(arincProcedureLeg.airportIcaoRegion())
        .concat(arincProcedureLeg.sidStarIdentifier())
        .concat(arincProcedureLeg.subSectionCode());
  }

  /**
   * An {@link ArincProcedureLegConverter} performs the (complex) functionality of converting a procedure leg as coded in the
   * ARINC database into the more usable form expected by downstream algorithms (i.e. the {@link Leg}) interface.
   * <br>
   * Legs as provided by ARINC contain references to other records (primarily {@link Fix}-like) which are necessary to construct
   * the more complex interface implementation. This class leverages the {@link FixDatabase} & {@link TerminalAreaDatabase} to
   * identify and dereference these.
   */
  static final class ArincProcedureLegConverter implements Function<ArincProcedureLeg, Leg> {

    private static final SpeedLimitToRange speedLimitToRange = new SpeedLimitToRange();

    private static final AltitudeLimitToRange altitudeLimitToRange = new AltitudeLimitToRange();

    private final LegFixDereferencer legFixDereferencer;

    ArincProcedureLegConverter(TerminalAreaDatabase terminalAreaDatabase, FixDatabase fixDatabase) {
      this.legFixDereferencer = new LegFixDereferencer(terminalAreaDatabase, fixDatabase);
    }

    @Override
    public Leg apply(ArincProcedureLeg arincProcedureLeg) {
      Optional<Fix> associatedFix = associatedFix(arincProcedureLeg);
      Optional<Fix> recommendedNavaid = recommendedNavaid(arincProcedureLeg);
      Optional<Fix> centerFix = centerFix(arincProcedureLeg);

      return new BoogieLeg.Builder()
          .associatedFix(associatedFix.orElse(null))
          .recommendedNavaid(recommendedNavaid.orElse(null))
          .centerFix(centerFix.orElse(null))
          .pathTerminator(arincProcedureLeg.pathTerm())
          .sequenceNumber(arincProcedureLeg.sequenceNumber())
          .speedConstraint(arincProcedureLeg.speedLimitDescription()
              .map(s -> speedLimitToRange.apply(s, arincProcedureLeg.speedLimit().map(Integer::doubleValue).orElse(null))).orElse(Range.all()))
          .altitudeConstraint(arincProcedureLeg.altitudeDescription()
              .map(s -> altitudeLimitToRange.apply(s, arincProcedureLeg.minAltitude1().orElse(null), arincProcedureLeg.minAltitude2().orElse(null))).orElse(Range.all()))
          .outboundMagneticCourse(arincProcedureLeg.outboundMagneticCourse().orElse(null))
          .theta(arincProcedureLeg.theta().orElse(null))
          .rho(arincProcedureLeg.rho().orElse(null))
          .rnp(arincProcedureLeg.rnp().orElse(null))
          .verticalAngle(arincProcedureLeg.verticalAngle().orElse(null))
          .routeDistance(arincProcedureLeg.routeDistance().orElse(null))
          .holdTime(arincProcedureLeg.holdTime().orElse(null))
          .turnDirection(arincProcedureLeg.turnDirection().map(this::toTurnDirection).orElse(TurnDirection.either()))
          .isPublishedHoldingFix(IsPublishedHoldingFix.INSTANCE.test(arincProcedureLeg))
          .isFlyOverFix(IsFlyOverFix.INSTANCE.test(arincProcedureLeg))
          .build();
    }

    Optional<Fix> associatedFix(ArincProcedureLeg arincProcedureLeg) {
      if (arincProcedureLeg.fixIdentifier().isPresent() && arincProcedureLeg.fixIcaoRegion().isPresent() && arincProcedureLeg.fixSectionCode().isPresent()) {
        return legFixDereferencer.dereference(
            arincProcedureLeg.fixIdentifier().orElseThrow(IllegalStateException::new),
            arincProcedureLeg.airportIdentifier(),
            arincProcedureLeg.fixIcaoRegion().orElseThrow(IllegalStateException::new),
            arincProcedureLeg.fixSectionCode().orElseThrow(IllegalStateException::new),
            arincProcedureLeg.fixSubSectionCode().orElse(null)
        );
      }
      return Optional.empty();
    }

    Optional<Fix> recommendedNavaid(ArincProcedureLeg arincProcedureLeg) {
      if (arincProcedureLeg.recommendedNavaidIdentifier().isPresent() && arincProcedureLeg.recommendedNavaidIcaoRegion().isPresent() && arincProcedureLeg.recommendedNavaidSectionCode().isPresent()) {
        return legFixDereferencer.dereference(
            arincProcedureLeg.recommendedNavaidIdentifier().orElseThrow(IllegalStateException::new),
            arincProcedureLeg.airportIdentifier(),
            arincProcedureLeg.recommendedNavaidIcaoRegion().orElseThrow(IllegalStateException::new),
            arincProcedureLeg.recommendedNavaidSectionCode().orElseThrow(IllegalStateException::new),
            arincProcedureLeg.recommendedNavaidSubSectionCode().orElse(null)
        );
      }
      return Optional.empty();
    }

    Optional<Fix> centerFix(ArincProcedureLeg arincProcedureLeg) {
      if (arincProcedureLeg.centerFixIdentifier().isPresent() && arincProcedureLeg.centerFixIcaoRegion().isPresent() && arincProcedureLeg.centerFixSectionCode().isPresent()) {
        return legFixDereferencer.dereference(
            arincProcedureLeg.centerFixIdentifier().orElseThrow(IllegalStateException::new),
            arincProcedureLeg.airportIdentifier(),
            arincProcedureLeg.centerFixIcaoRegion().orElseThrow(IllegalStateException::new),
            arincProcedureLeg.centerFixSectionCode().orElseThrow(IllegalStateException::new),
            arincProcedureLeg.centerFixSubSectionCode().orElse(null)
        );
      }
      return Optional.empty();
    }

    TurnDirection toTurnDirection(org.mitre.tdp.boogie.arinc.v18.field.TurnDirection turnDirection) {
      switch (turnDirection) {
        case L:
          return TurnDirection.left();
        case R:
          return TurnDirection.right();
        default:
          return TurnDirection.either();
      }
    }
  }
}
