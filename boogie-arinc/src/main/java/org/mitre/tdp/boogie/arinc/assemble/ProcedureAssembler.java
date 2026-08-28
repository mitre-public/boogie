package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.groupingBy;
import static org.mitre.caasd.commons.util.Partitioners.splitOnPairwiseChange;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

/**
 * Assembler class for converting collections of {@link ArincProcedureLeg} records into a client-defined output class of type
 * {@code P} representing a Procedure.
 *
 * <p>This class can be used with the {@link FixAssemblyStrategy#standard()} + {@link ProcedureAssemblyStrategy#standard()} to
 * generate lightweight Boogie-defined {@link Procedure} implementations that can be used with other Boogie algorithms.
 */
public interface ProcedureAssembler<P> {

  static ProcedureAssembler<Procedure> standard(ArincTerminalAreaDatabase terminalDatabase, ArincFixDatabase arincFixDatabase) {
    return withStrategy(terminalDatabase, arincFixDatabase, FixAssemblyStrategy.standard(), ProcedureAssemblyStrategy.standard());
  }

  static <P, T, L, F> ProcedureAssembler<P> withStrategy(ArincTerminalAreaDatabase terminalDatabase, ArincFixDatabase arincFixDatabase, FixAssemblyStrategy<F> fixStrategy, ProcedureAssemblyStrategy<P, T, L, F> procedureStrategy) {
    return new Standard<>(terminalDatabase, arincFixDatabase, fixStrategy, procedureStrategy);
  }

  Stream<P> assemble(Collection<ArincProcedureLeg> legs);

  final class Standard<P, T, L, F> implements ProcedureAssembler<P> {

    private static final ArincTransitionTypeClassifier transitionTypeClassifier = new ArincTransitionTypeClassifier();

    private static final ArincRequiredEquipageClassifier requiredEquipageClassifier = new ArincRequiredEquipageClassifier();

    private static final Comparator<ArincProcedureLeg> LEG_COMPARATOR = comparing(ArincProcedureLeg::sequenceNumber)
        .thenComparing(i -> i.categoryOrType().orElse("UNK"));

    private final ArincProcedureLegConverter<P, T, L, F> inflator;
    /**
     * Predicate for determining whether there should be any special splitting logic applied to sequential legs within a procedure
     * transition (e.g. to split the missed approach off of the final approach see - {@link IsFirstLegOfMissedApproach}).
     */
    private final BiPredicate<ArincProcedureLeg, ArincProcedureLeg> shouldSplitTransition;

    private final ProcedureAssemblyStrategy<P, T, L, F> strategy;

    private Standard(
        ArincTerminalAreaDatabase arincTerminalAreaDatabase,
        ArincFixDatabase arincFixDatabase,
        FixAssemblyStrategy<F> fixStrategy,
        ProcedureAssemblyStrategy<P, T, L, F> procedureStrategy
    ) {
      this.inflator = new ArincProcedureLegConverter<>(arincTerminalAreaDatabase, arincFixDatabase, procedureStrategy, fixStrategy);
      this.shouldSplitTransition = (l1, l2) -> IsFirstLegOfMissedApproach.INSTANCE.test(l2);
      this.strategy = requireNonNull(procedureStrategy);
    }

    @Override
    public Stream<P> assemble(Collection<ArincProcedureLeg> arincProcedureLegs) {
      return groupByProcedure(arincProcedureLegs).stream()
          .map(this::toProcedure);
    }

    /**
     * Groups legs into procedures without imposing an order across their transitions. Encounter order is retained inside each
     * procedure and is used as the stable tie-breaker when the individual transitions are sorted later.
     */
    static Collection<List<ArincProcedureLeg>> groupByProcedure(Collection<ArincProcedureLeg> arincProcedureLegs) {
      return arincProcedureLegs.stream()
          .collect(groupingBy(Standard::procedureGroupKey))
          .values();
    }

    public static final String DEFAULT_TRANSITION = "ALL";
    public static final String DEFAULT_CAT_TYPE = "ANY";
    private static final String DEFAULT_MISSED_APPROACH_VARIANT = "ANY";

    private static final Function<ArincProcedureLeg, TransitionGroupKey> GROUPER = Standard::transitionGroupKey;
    /**
     * Converts the list of {@link ArincProcedureLeg}s known to be part of the same procedure into a composite {@link Procedure}
     * object. This method uses two helper classes to provided value-add features:
     * <br>
     * 1. {@link #transitionTypeClassifier} to assign COMMON/ENROUTE/RUNWAY, etc. types to transitions
     * 2. {@link #requiredEquipageClassifier} to up-level the detailed ARINC 424 procedure types (and qualifiers) into manageable
     * sub-categories of CONV/RNAV/RNP.
     */
    private P toProcedure(List<ArincProcedureLeg> arincProcedureLegs) {

      Collection<List<ArincProcedureLeg>> byTransition = groupByTransition(arincProcedureLegs);

      Multimap<TransitionType, List<ArincProcedureLeg>> byType = LinkedHashMultimap.create();

      byTransition.stream().map(this::repartition).flatMap(Collection::stream)
          .forEach(transition -> byType.put(transitionTypeClassifier.applySorted(transition), transition));

      RequiredNavigationEquipage equipage = requiredEquipageClassifier.apply(byType);

      List<T> transitions = byType.entries().stream()
          .map(entry -> strategy.convertTransition(
              entry.getValue().get(0),
              entry.getKey(),
              entry.getValue().stream().map(inflator).collect(Collectors.toList())
          ))
          .collect(Collectors.toList());

      ArincProcedureLeg representative = arincProcedureLegs.stream()
          .min(LEG_COMPARATOR)
          .orElseThrow(IllegalStateException::new);

      return strategy.convertProcedure(representative, equipage, transitions);
    }

    /**
     * Establishes transition membership before applying sequence ordering. Sorting each transition independently avoids comparing
     * unrelated legs and keeps encounter order as the stable tie-breaker for duplicate sequence numbers.
     */
    static Collection<List<ArincProcedureLeg>> groupByTransition(Collection<ArincProcedureLeg> procedureLegs) {
      return procedureLegs.stream()
          .collect(groupingBy(
              GROUPER,
              Collectors.collectingAndThen(Collectors.toList(), Standard::sortTransition)
          ))
          .values();
    }

    private static List<ArincProcedureLeg> sortTransition(List<ArincProcedureLeg> transition) {
      transition.sort(LEG_COMPARATOR);
      return transition;
    }

    /**
     * Re-partitions the name-grouped transition legs based on the configured {@link #shouldSplitTransition} predicate.
     */
    private List<List<ArincProcedureLeg>> repartition(List<ArincProcedureLeg> procedureLegs) {
      return splitOnPairwiseChange(procedureLegs, (ls, next) -> shouldSplitTransition.negate().test(ls.get(ls.size() - 1), next));
    }

    private static ProcedureGroupKey procedureGroupKey(ArincProcedureLeg arincProcedureLeg) {
      return new ProcedureGroupKey(
          arincProcedureLeg.airportIdentifier(),
          arincProcedureLeg.airportIcaoRegion(),
          arincProcedureLeg.sidStarIdentifier(),
          arincProcedureLeg.subSectionCode().orElseThrow(IllegalStateException::new)
      );
    }

    /**
     * Route type {@code Z} identifies an explicit missed-approach route and must not be combined with the ordinary approach route
     * that {@link #repartition(List)} may split at an {@code M} waypoint marker. Qualifier 2 further distinguishes primary,
     * secondary, and engine-out {@code Z} routes. Other route types deliberately ignore qualifier 2 because it can vary between
     * legs in one valid transition.
     */
    private static TransitionGroupKey transitionGroupKey(ArincProcedureLeg arincProcedureLeg) {
      String routeType = arincProcedureLeg.routeType();
      String missedApproachVariant = Optional.of(arincProcedureLeg)
          .filter(leg -> "Z".equals(leg.routeType()))
          .flatMap(ArincProcedureLeg::routeTypeQualifier2)
          .orElse(DEFAULT_MISSED_APPROACH_VARIANT);

      return new TransitionGroupKey(
          arincProcedureLeg.transitionIdentifier().orElse(DEFAULT_TRANSITION),
          arincProcedureLeg.categoryOrType().orElse(DEFAULT_CAT_TYPE),
          routeType,
          missedApproachVariant
      );
    }

    private record ProcedureGroupKey(
        String airportIdentifier,
        String airportIcaoRegion,
        String sidStarIdentifier,
        String subSectionCode
    ) {}

    private record TransitionGroupKey(
        String transitionIdentifier,
        String categoryOrType,
        String routeType,
        String missedApproachVariant
    ) {}

    /**
     * An {@link ArincProcedureLegConverter} performs the (complex) functionality of converting a procedure leg as coded in the
     * ARINC database into the more usable form expected by downstream algorithms (i.e. the {@link Leg}) interface.
     * <br>
     * Legs as provided by ARINC contain references to other records (primarily {@link Fix}-like) which are necessary to construct
     * the more complex interface implementation. This class leverages the {@link ArincFixDatabase} & {@link ArincTerminalAreaDatabase} to
     * identify and dereference these.
     */
    static final class ArincProcedureLegConverter<P, T, L, F> implements Function<ArincProcedureLeg, L> {

      private final ProcedureAssemblyStrategy<P, T, L, F> strategy;
      private final FixDereferencer<F> fixDereferencer;

      ArincProcedureLegConverter(
          ArincTerminalAreaDatabase arincTerminalAreaDatabase,
          ArincFixDatabase arincFixDatabase,
          ProcedureAssemblyStrategy<P, T, L, F> procedureStrategy,
          FixAssemblyStrategy<F> fixStrategy) {
        this.strategy = requireNonNull(procedureStrategy);
        this.fixDereferencer = new FixDereferencer<>(FixAssembler.withStrategy(fixStrategy), arincTerminalAreaDatabase, arincFixDatabase);
      }

      @Override
      public L apply(ArincProcedureLeg arincProcedureLeg) {
        Optional<F> associatedFix = associatedFix(arincProcedureLeg);
        Optional<F> recommendedNavaid = recommendedNavaid(arincProcedureLeg);
        Optional<F> centerFix = centerFix(arincProcedureLeg);

        return strategy.convertLeg(arincProcedureLeg, associatedFix.orElse(null), recommendedNavaid.orElse(null), centerFix.orElse(null));
      }

      Optional<F> associatedFix(ArincProcedureLeg arincProcedureLeg) {
        if (arincProcedureLeg.fixIdentifier().isPresent() && arincProcedureLeg.fixIcaoRegion().isPresent() && arincProcedureLeg.fixSectionCode().isPresent()) {
          //if the waypoint is terminal (aka 'P') its more reliable to use the airports icao region
          String icaoRegion = Optional.of(arincProcedureLeg.airportIcaoRegion())
              .filter(i -> arincProcedureLeg.fixSectionCode().filter(s -> s.equals(SectionCode.P)).isPresent())
              .or(arincProcedureLeg::fixIcaoRegion)
              .orElseThrow(IllegalStateException::new);

          return fixDereferencer.dereference(
              arincProcedureLeg.fixIdentifier().orElseThrow(IllegalStateException::new),
              arincProcedureLeg.airportIdentifier(),
              icaoRegion,
              arincProcedureLeg.fixSectionCode().orElseThrow(IllegalStateException::new),
              arincProcedureLeg.fixSubSectionCode().orElse(null)
          );
        }
        return Optional.empty();
      }

      Optional<F> recommendedNavaid(ArincProcedureLeg arincProcedureLeg) {
        if (arincProcedureLeg.recommendedNavaidIdentifier().isPresent() && arincProcedureLeg.recommendedNavaidIcaoRegion().isPresent() && arincProcedureLeg.recommendedNavaidSectionCode().isPresent()) {
          return fixDereferencer.dereference(
              arincProcedureLeg.recommendedNavaidIdentifier().orElseThrow(IllegalStateException::new),
              arincProcedureLeg.airportIdentifier(),
              arincProcedureLeg.recommendedNavaidIcaoRegion().orElseThrow(IllegalStateException::new),
              arincProcedureLeg.recommendedNavaidSectionCode().orElseThrow(IllegalStateException::new),
              arincProcedureLeg.recommendedNavaidSubSectionCode().orElse(null)
          );
        }
        return Optional.empty();
      }

      Optional<F> centerFix(ArincProcedureLeg arincProcedureLeg) {
        if (arincProcedureLeg.centerFixIdentifier().isPresent() && arincProcedureLeg.centerFixIcaoRegion().isPresent() && arincProcedureLeg.centerFixSectionCode().isPresent()) {
          //if the waypoint is terminal (aka 'P') its more reliable to use the airports icao region
          String icaoRegion = Optional.of(arincProcedureLeg.airportIcaoRegion())
              .filter(i -> arincProcedureLeg.centerFixSectionCode().filter(s -> s.equals(SectionCode.P)).isPresent())
              .or(arincProcedureLeg::centerFixIcaoRegion)
              .orElseThrow(IllegalStateException::new);
          return fixDereferencer.dereference(
              arincProcedureLeg.centerFixIdentifier().orElseThrow(IllegalStateException::new),
              arincProcedureLeg.airportIdentifier(),
              icaoRegion,
              arincProcedureLeg.centerFixSectionCode().orElseThrow(IllegalStateException::new),
              arincProcedureLeg.centerFixSubSectionCode().orElse(null)
          );
        }
        return Optional.empty();
      }
    }
  }
}
