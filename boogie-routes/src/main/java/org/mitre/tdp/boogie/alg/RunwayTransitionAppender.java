package org.mitre.tdp.boogie.alg;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.RunwayPredictor;
import org.mitre.tdp.boogie.alg.resolve.SidRunwayTransitionFilter;
import org.mitre.tdp.boogie.alg.resolve.StarRunwayTransitionFilter;
import org.mitre.tdp.boogie.alg.resolve.element.ProcedureElement;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

/**
 * This class inspects a provided {@link ExpandedRoute} introspecting the elements on either end of the route looking for:
 *
 * 1) APT -> SID and STAR -> APT combinations
 * 2) If found it then checks for a relevant runway prediction for either
 * 3) In the case where a prediction exists it introspects the source object for the element - appending the appropriate runway
 * transition in either case to the start/end of the expanded route
 *
 * This lets us modify our {@link ExpandedRoute} records with simple predictions about the arrival/departure runway.
 */
public final class RunwayTransitionAppender implements Function<ExpandedRoute, ExpandedRoute> {

  /**
   * A prediction of the departure runway of a flight.
   */
  private final RunwayPredictor departurePredictor;
  /**
   * A prediction of the arrival runway of a flight.
   */
  private final RunwayPredictor arrivalPredictor;

  public RunwayTransitionAppender(
      RunwayPredictor departurePredictor,
      RunwayPredictor arrivalPredictor
  ) {
    this.departurePredictor = checkNotNull(departurePredictor);
    this.arrivalPredictor = checkNotNull(arrivalPredictor);
  }

  @Override
  public ExpandedRoute apply(ExpandedRoute expandedRoute) {
    if (expandedRoute.legs().size() >= 2) {
      departurePredictor.predictedRunway().ifPresent(runway -> appendSidRunwayTransition(expandedRoute, runway));
      arrivalPredictor.predictedRunway().ifPresent(runway -> appendStarRunwayTransition(expandedRoute, runway));
    }
    return expandedRoute;
  }

  /**
   * Attempts to append the runway transition of the sid associated with the provided departure runway to the head of the
   * {@link ExpandedRoute}.
   */
  private void appendSidRunwayTransition(ExpandedRoute expandedRoute, String departureRunway) {
    if (hasSidSectionCombination(expandedRoute)) {
      ResolvedSection section = expandedRoute.sections().get(1);

      ProcedureElement element = (ProcedureElement) section.elements().stream().findFirst().orElseThrow(IllegalStateException::new);
      Procedure procedure = element.reference();

      SidRunwayTransitionFilter filter = new SidRunwayTransitionFilter(departureRunway);
      Optional<? extends Transition> runwayTransition = procedure.transitions().stream().filter(filter).findFirst();

      runwayTransition.ifPresent(transition -> {
        List<GraphableLeg> newLegs = new ArrayList<>();

        newLegs.addAll(expandedRoute.legsFor(expandedRoute.sections().get(0)));
        newLegs.addAll(toGraphableLegs(section, transition));

        expandedRoute.sections().subList(1, expandedRoute.sections().size())
            .forEach(s -> newLegs.addAll(expandedRoute.legsFor(s)));

        expandedRoute.replaceLegs(newLegs);
      });
    }
  }

  private boolean hasSidSectionCombination(ExpandedRoute expandedRoute) {
    ResolvedSection initial = expandedRoute.sections().get(0);
    ResolvedSection subsequent = expandedRoute.sections().get(1);

    return initial.elements().stream().allMatch(element -> element.type().equals(ElementType.AIRPORT))
        && subsequent.elements().stream().allMatch(element -> element.type().equals(ElementType.SID));
  }

  /**
   * Attempts to append the runway transition of the sid associated with the provided departure runway to the head of the
   * {@link ExpandedRoute}.
   */
  private void appendStarRunwayTransition(ExpandedRoute expandedRoute, String arrivalRunway) {
    if (hasStarSectionCombination(expandedRoute)) {
      ResolvedSection section = expandedRoute.sections().get(expandedRoute.sections().size() - 2);

      ProcedureElement element = (ProcedureElement) section.elements().stream().findFirst().orElseThrow(IllegalStateException::new);
      Procedure procedure = element.reference();

      StarRunwayTransitionFilter filter = new StarRunwayTransitionFilter(arrivalRunway);
      Optional<? extends Transition> runwayTransition = procedure.transitions().stream().filter(filter).findFirst();

      runwayTransition.ifPresent(transition -> {
        List<GraphableLeg> newLegs = new ArrayList<>();

        expandedRoute.sections().subList(0, expandedRoute.sections().size() - 1)
            .forEach(s -> newLegs.addAll(expandedRoute.legsFor(s)));

        newLegs.addAll(toGraphableLegs(section, transition));
        newLegs.addAll(expandedRoute.legsFor(expandedRoute.sections().get(expandedRoute.sections().size() - 1)));

        expandedRoute.replaceLegs(newLegs);
      });
    }
  }

  private boolean hasStarSectionCombination(ExpandedRoute expandedRoute) {
    int n = expandedRoute.sections().size();
    ResolvedSection terminal = expandedRoute.sections().get(n - 1);
    ResolvedSection previous = expandedRoute.sections().get(n - 2);

    return terminal.elements().stream().allMatch(element -> element.type().equals(ElementType.AIRPORT))
        && previous.elements().stream().allMatch(element -> element.type().equals(ElementType.STAR));
  }

  /**
   * Converts the given {@link Transition} and it's associated {@link ProcedureElement} to a collection of {@link GraphableLeg}s
   * which can be appended to the start or end of the {@link ExpandedRoute}.
   */
  private List<GraphableLeg> toGraphableLegs(ResolvedSection section, Transition transition) {
    SectionSplit split = section.sectionSplit();
    ResolvedElement<?> element = section.elements().iterator().next();

    return transition.legs().stream()
        .map(leg -> new GraphableLeg(leg).setSplit(split).setSourceElement(element))
        .collect(Collectors.toList());
  }
}
