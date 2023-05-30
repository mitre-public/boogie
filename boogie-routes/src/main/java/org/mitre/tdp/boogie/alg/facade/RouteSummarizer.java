package org.mitre.tdp.boogie.alg.facade;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.caasd.commons.util.Partitioners.newListCollector;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.TraversalOrderSorter;
import org.mitre.tdp.boogie.alg.chooser.RouteChooser;
import org.mitre.tdp.boogie.alg.resolve.ResolvedLeg;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokenVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Derives a {@link RouteSummary} from the sequence of {@link ResolvedLeg}s returned by a {@link RouteChooser} implementation (as
 * long as the contract of the chooser's choose method is met).
 *
 * <p>This class <i>does</i> make use of some knowledge of how elements are typically tokenized and filed for in a standard route
 * string format (e.g. {@code FIX.STAR.APT} to define an entry fix).
 *
 * <p>With some additional implementation work this class could be made independent of this...
 */
final class RouteSummarizer implements Function<List<ResolvedLeg>, Optional<RouteSummary>> {

  private static final Logger LOG = LoggerFactory.getLogger(RouteSummarizer.class);

  RouteSummarizer() {
  }

  @Override
  public Optional<RouteSummary> apply(List<ResolvedLeg> resolvedLegs) {
    checkArgument(!resolvedLegs.isEmpty(), "Input sequence of resolved legs cannot be empty");

    Optional<String> departureAirport = departureAirport(resolvedLegs.get(0));
    Optional<String> arrivalAirport = arrivalAirport(resolvedLegs.get(resolvedLegs.size() - 1));

    if (departureAirport.isPresent() || arrivalAirport.isPresent()) {

      RouteSummary.Builder builder = new RouteSummary.Builder()
          .arrivalAirport(arrivalAirport.orElse(null))
          .departureAirport(departureAirport.orElse(null));

      StarFeatureResolver.INSTANCE.apply(resolvedLegs).ifPresent(summary -> builder
          .star(summary.procedureName())
          .starEntryFix(summary.entryExitFix())
          .arrivalFix(summary.arrivalDepartureFix())
          .requiredStarEquipage(summary.equipage())
      );

      SidFeatureResolver.INSTANCE.apply(resolvedLegs).ifPresent(summary -> builder
          .sid(summary.procedureName())
          .sidExitFix(summary.entryExitFix())
          .departureFix(summary.arrivalDepartureFix())
          .requiredSidEquipage(summary.equipage())
      );

      ApproachFeatureResolver.INSTANCE.apply(resolvedLegs).ifPresent(summary -> builder
          .approach(summary.procedureName())
          .approachEntryFix(summary.entryExitFix())
          .requiredApproachEquipage(summary.equipage())
      );

      return Optional.of(builder.build());
    } else {
      LOG.warn("Unable to reliably summarize route without Arrival/Departure airport.");
      return Optional.empty();
    }
  }

  private Optional<String> departureAirport(ResolvedLeg initialLeg) {
    return Optional.of(initialLeg).flatMap(l -> ResolvedTokenVisitor.airport(l.resolvedToken())).map(Airport::airportIdentifier);
  }

  private Optional<String> arrivalAirport(ResolvedLeg finalLeg) {
    return Optional.of(finalLeg).flatMap(l -> ResolvedTokenVisitor.airport(l.resolvedToken())).map(Airport::airportIdentifier);
  }

  /**
   * Functional class for extracting common STAR features from an {@link ExpandedRoute}.
   */
  private static final class StarFeatureResolver implements Function<List<ResolvedLeg>, Optional<ProcedureSummary>> {

    private static final StarFeatureResolver INSTANCE = new StarFeatureResolver();

    @Override
    public Optional<ProcedureSummary> apply(List<ResolvedLeg> resolvedLegs) {
      return legsFromElement(resolvedLegs, e -> ResolvedTokenVisitor.star(e.resolvedToken()).isPresent())
          .stream()
          .reduce((l1, l2) -> l2)
          .map(starLegs -> {
            Procedure star = ResolvedTokenVisitor.star(starLegs.get(0).resolvedToken()).orElseThrow();
            Map<Leg, Transition> transitionEmbedding = transitionEmbeddingFor(starLegs);

            Optional<String> arrivalFix = starLegs.stream().filter(leg -> TransitionType.COMMON.equals(transitionEmbedding.get(leg.leg()).transitionType()))
                .findFirst().map(ResolvedLeg::leg).flatMap(Leg::associatedFix).map(Fix::fixIdentifier);

            return new ProcedureSummary.Builder()
                .procedureName(star.procedureIdentifier())
                .entryExitFix(starLegs.get(0).leg().associatedFix().map(Fix::fixIdentifier).orElse(null))
                .arrivalDepartureFix(arrivalFix.orElse(null))
                .equipage(star.requiredNavigationEquipage())
                .build();
          });
    }

    private Map<Leg, Transition> transitionEmbeddingFor(List<ResolvedLeg> resolvedLegs) {
      return resolvedLegs.stream()
          .flatMap(l -> ResolvedTokenVisitor.star(l.resolvedToken()).stream())
          .flatMap(this::transitionEmbeddingFor)
          .collect(elidingCollector(Pair::first, Pair::second));
    }

    private Stream<Pair<Leg, Transition>> transitionEmbeddingFor(Procedure star) {
      return TraversalOrderSorter.star().sort(star.transitions()).stream()
          .flatMap(Collection::stream)
          .flatMap(transition -> transition.legs().stream().map(leg -> Pair.of(leg, transition)));
    }
  }

  /**
   * Functional class for extracting common SID features from an {@link ExpandedRoute}.
   */
  private static final class SidFeatureResolver implements Function<List<ResolvedLeg>, Optional<ProcedureSummary>> {

    private static final SidFeatureResolver INSTANCE = new SidFeatureResolver();

    @Override
    public Optional<ProcedureSummary> apply(List<ResolvedLeg> resolvedLegs) {
      return legsFromElement(resolvedLegs, e -> ResolvedTokenVisitor.sid(e.resolvedToken()).isPresent())
          .stream()
          .findFirst()
          .map(sidLegs -> {
            Procedure sid = ResolvedTokenVisitor.sid(sidLegs.get(0).resolvedToken()).orElseThrow();
            Map<Leg, Transition> transitionEmbedding = transitionEmbeddingFor(sidLegs);

            Optional<String> departureFix = sidLegs.stream().filter(leg -> TransitionType.COMMON.equals(transitionEmbedding.get(leg.leg()).transitionType()))
                .findFirst().map(ResolvedLeg::leg).flatMap(Leg::associatedFix).map(Fix::fixIdentifier);

            return new ProcedureSummary.Builder()
                .procedureName(sid.procedureIdentifier())
                .entryExitFix(sidLegs.get(sidLegs.size() - 1).leg().associatedFix().map(Fix::fixIdentifier).orElse(null))
                .arrivalDepartureFix(departureFix.orElse(null))
                .equipage(sid.requiredNavigationEquipage())
                .build();
          });
    }

    private Map<Leg, Transition> transitionEmbeddingFor(List<ResolvedLeg> resolvedLegs) {
      return resolvedLegs.stream()
          .flatMap(l -> ResolvedTokenVisitor.sid(l.resolvedToken()).stream())
          .flatMap(this::transitionEmbeddingFor)
          .collect(elidingCollector(Pair::first, Pair::second));
    }

    private Stream<Pair<Leg, Transition>> transitionEmbeddingFor(Procedure sid) {
      return TraversalOrderSorter.sid().sort(sid.transitions()).stream()
          .flatMap(Collection::stream)
          .flatMap(transition -> transition.legs().stream().map(leg -> Pair.of(leg, transition)));
    }
  }

  /**
   * Collector which (in the case of key conflicts) elides subsequent encounters of a given key.
   */
  private static <T, K, U> Collector<T, ?, LinkedHashMap<K, U>> elidingCollector(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends U> valueMapper) {
    return Collectors.toMap(keyMapper, valueMapper, (a, b) -> a, LinkedHashMap::new);
  }

  /**
   * Functional class for extracting common Approach features from an {@link ExpandedRoute}.
   */
  private static final class ApproachFeatureResolver implements Function<List<ResolvedLeg>, Optional<ProcedureSummary>> {

    private static final ApproachFeatureResolver INSTANCE = new ApproachFeatureResolver();

    @Override
    public Optional<ProcedureSummary> apply(List<ResolvedLeg> resolvedLegs) {
      return legsFromElement(resolvedLegs, e -> ResolvedTokenVisitor.approach(e.resolvedToken()).isPresent())
          .stream()
          .reduce((l1, l2) -> l2)
          .map(approachLegs -> {
            Procedure approach = ResolvedTokenVisitor.approach(approachLegs.get(0).resolvedToken()).orElseThrow();

            return new ProcedureSummary.Builder()
                .procedureName(approach.procedureIdentifier())
                .entryExitFix(approachLegs.get(0).leg().associatedFix().map(Fix::fixIdentifier).orElse(null))
                .equipage(approach.requiredNavigationEquipage())
                .build();
          });
    }
  }

  private static List<List<ResolvedLeg>> legsFromElement(List<ResolvedLeg> resolvedLegs, Predicate<ResolvedLeg> isFromElement) {
    return resolvedLegs.stream()
        .collect(newListCollector(isFromElement)).stream()
        .filter(list -> isFromElement.test(list.get(0)))
        .collect(Collectors.toList());
  }

  /**
   * Container class for summarized procedure information.
   */
  private static final class ProcedureSummary {

    private final String procedureName;
    private final String entryExitFix;
    private final String arrivalDepartureFix;
    private final RequiredNavigationEquipage equipage;

    private ProcedureSummary(Builder builder) {
      this.procedureName = builder.procedureName;
      this.entryExitFix = builder.entryExitFix;
      this.arrivalDepartureFix = builder.arrivalDepartureFix;
      this.equipage = builder.equipage;
    }

    public String procedureName() {
      return procedureName;
    }

    public String entryExitFix() {
      return entryExitFix;
    }

    public String arrivalDepartureFix() {
      return arrivalDepartureFix;
    }

    public RequiredNavigationEquipage equipage() {
      return equipage;
    }

    public Builder toBuilder() {
      return new Builder()
          .procedureName(procedureName())
          .entryExitFix(entryExitFix())
          .arrivalDepartureFix(arrivalDepartureFix())
          .equipage(equipage());
    }

    public static final class Builder {
      private String procedureName;
      private String entryExitFix;
      private String arrivalDepartureFix;
      private RequiredNavigationEquipage equipage;

      public Builder procedureName(String procedureName) {
        this.procedureName = procedureName;
        return this;
      }

      public Builder entryExitFix(String entryExitFix) {
        this.entryExitFix = entryExitFix;
        return this;
      }

      public Builder arrivalDepartureFix(String arrivalDepartureFix) {
        this.arrivalDepartureFix = arrivalDepartureFix;
        return this;
      }

      public Builder equipage(RequiredNavigationEquipage equipage) {
        this.equipage = equipage;
        return this;
      }

      public ProcedureSummary build() {
        return new ProcedureSummary(this);
      }
    }
  }
}