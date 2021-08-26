package org.mitre.tdp.boogie.alg.resolve;

import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.firstLegWithLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.util.TransitionSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The airport to SID linker ensures that airport elements are only linked to the ends of transitions on the adjacent SID element.
 * <br>
 * This class is conceptually similar (though not exactly so) to the {@link StarToAirportLinker}.
 */
public final class AirportToSidLinker implements BiFunction<AirportElement, SidElement, List<LinkedLegs>> {

  private static final Logger LOG = LoggerFactory.getLogger(AirportToSidLinker.class);

  static final AirportToSidLinker INSTANCE = new AirportToSidLinker();

  private AirportToSidLinker() {
  }

  @Override
  public List<LinkedLegs> apply(AirportElement airportElement, SidElement sidElement) {

    if (sidElement.toLinkedLegs().isEmpty()) {
      return Collections.emptyList();
    }

    List<Transition> initialTransitions = initialTransitions(sidElement);
    LOG.info("Attempting to link {} transitions of type {} to target airport element.", initialTransitions.size(), initialTransitions.get(0).transitionType());

    LinkedLegs airportLegs = highlander(airportElement.toLinkedLegs()).orElseThrow(IllegalStateException::new);

    return initialTransitions.stream()
        .map(transition -> new LinkedLegs(airportLegs.target(), transition.legs().get(0), matchWeightFor(airportLegs.target(), transition)))
        .collect(Collectors.toList());
  }

  /**
   * Returns the "initial" transitions to the SID - these are taken in sorted order by {@link TransitionType}, that is to say
   * they will be the RUNWAY < COMMON < ENROUTE transitions.
   */
  private List<Transition> initialTransitions(SidElement sidElement) {
    return TransitionSorter.INSTANCE
        .sortSidTransitions(sidElement.procedure.transitions())
        .stream().filter(col -> !col.isEmpty())
        .findFirst().orElseThrow(IllegalStateException::new);
  }

  private double matchWeightFor(Leg airportLeg, Transition transition) {
    Optional<Leg> locationLeg = firstLegWithLocation(transition);

    double distanceScore = locationLeg.flatMap(Leg::associatedFix)
        .map(fix -> airportLeg.associatedFix().orElseThrow(IllegalStateException::new).distanceInNmTo(fix))
        .orElse(LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT);

    return (distanceScore * transitionTypeModifier(transition.transitionType()))
        / locationLeg.map(transition.legs()::indexOf).map(i -> i + 1.).orElse(1.);
  }

  private double transitionTypeModifier(TransitionType transitionType) {
    return TransitionType.RUNWAY.equals(transitionType) ? .01 : 1.;
  }

  /**
   * "There can only be one"
   * <br>
   * https://www.youtube.com/watch?v=_J3VeogFUOs
   */
  private <T> Optional<T> highlander(Collection<T> col) {
    return col.size() == 1 ? Optional.of(col.iterator().next()) : Optional.empty();
  }
}
