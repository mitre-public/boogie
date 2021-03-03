package org.mitre.tdp.boogie.alg;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.alg.resolve.RunwayPredictor;
import org.mitre.tdp.boogie.alg.resolve.SidRunwayTransitionFilter;
import org.mitre.tdp.boogie.alg.resolve.StarRunwayTransitionFilter;
import org.mitre.tdp.boogie.alg.resolve.element.ProcedureElement;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.Procedure;

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
    GraphableLeg initial = expandedRoute.legs().get(0);
    GraphableLeg subsequent = expandedRoute.legs().get(1);

    if (initial.sourceElement().type().equals(ElementType.AIRPORT) && subsequent.sourceElement().type().equals(ElementType.SID)) {
      ProcedureElement element = (ProcedureElement) subsequent.sourceElement();
      Procedure procedure = element.reference();

      SidRunwayTransitionFilter filter = new SidRunwayTransitionFilter(departureRunway);
      Optional<? extends Transition> runwayTransition = procedure.transitions().stream().filter(filter).findFirst();

      runwayTransition.ifPresent(transition -> {
        List<GraphableLeg> newLegs = new ArrayList<>();

        newLegs.add(initial);
        newLegs.addAll(toGraphableLegs(subsequent, transition));
        newLegs.addAll(expandedRoute.legs().subList(1, expandedRoute.legs().size()));

        expandedRoute.replaceLegs(newLegs);
      });
    }
  }

  /**
   * Attempts to append the runway transition of the sid associated with the provided departure runway to the head of the
   * {@link ExpandedRoute}.
   */
  private void appendStarRunwayTransition(ExpandedRoute expandedRoute, String arrivalRunway) {
    int n = expandedRoute.legs().size();
    GraphableLeg terminal = expandedRoute.legs().get(n - 1);
    GraphableLeg previous = expandedRoute.legs().get(n - 2);

    if (terminal.sourceElement().type().equals(ElementType.AIRPORT) && previous.sourceElement().type().equals(ElementType.STAR)) {
      ProcedureElement element = (ProcedureElement) previous.sourceElement();
      Procedure procedure = element.reference();

      StarRunwayTransitionFilter filter = new StarRunwayTransitionFilter(arrivalRunway);
      Optional<? extends Transition> runwayTransition = procedure.transitions().stream().filter(filter).findFirst();

      runwayTransition.ifPresent(transition -> {
        List<GraphableLeg> newLegs = new ArrayList<>();

        newLegs.addAll(expandedRoute.legs().subList(0, expandedRoute.legs().size() - 1));
        newLegs.addAll(toGraphableLegs(previous, transition));
        newLegs.add(terminal);

        expandedRoute.replaceLegs(newLegs);
      });
    }
  }

  /**
   * Converts the given {@link Transition} and it's associated {@link ProcedureElement} to a collection of {@link GraphableLeg}s
   * which can be appended to the start or end of the {@link ExpandedRoute}.
   */
  private List<GraphableLeg> toGraphableLegs(GraphableLeg source, Transition transition) {
    SectionSplit split = source.split();
    ResolvedElement<?> element = source.sourceElement();

    return transition.legs().stream()
        .map(leg -> new GraphableLeg(leg).setSplit(split).setSourceElement(element))
        .collect(Collectors.toList());
  }
}
