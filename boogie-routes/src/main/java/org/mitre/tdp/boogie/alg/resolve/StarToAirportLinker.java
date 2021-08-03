package org.mitre.tdp.boogie.alg.resolve;

import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.finalStarTransitions;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.lastLegWithLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.util.TransitionSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class ensures that the only links from a STAR to an airport come from the <i>ends</i> of one of it's transitions.
 * <br>
 * Transitioning from a STAR to an airport (in the absence of an approach procedure - which is dependent on having a supplied
 * runway prediction) can get tricky due to the geometry of some STARs (if a simple nearest-fix approach is used).
 * <br>
 * In most cases the downwind portion of runway transitions runs parallel to one (or more) runways at an airport - in these
 * cases the closest leg to the airport is generally about halfway down the runway transition (as the transition passes the
 * physical airport location). See:
 * <br>
 * \*---*---*---*---*-> (runway transition)
 * :::::===|X|===:::::: (airport + runways, the :'s are filler I guess...)
 * /*---*---*---*---*-> (runway transition)
 * <br>
 * In these cases if we want to capture the <i>full</i> runway transition we need to modify some the linking logic from the STAR
 * to the Airport to only build links from the ends of such transitions.
 * <br>
 * Additional complexity comes in when we realize beyond that some procedures also have <i>no</i> runway transitions but the
 * common portion of the STAR comes down similar a runway in the above picture (typically these are unidirectional STARs).
 */
final class StarToAirportLinker implements BiFunction<StarElement, AirportElement, List<LinkedLegs>> {

  private static final Logger LOG = LoggerFactory.getLogger(StarToAirportLinker.class);

  static final StarToAirportLinker INSTANCE = new StarToAirportLinker();

  private StarToAirportLinker() {
  }

  @Override
  public List<LinkedLegs> apply(StarElement starElement, AirportElement airportElement) {

    if (starElement.toLinkedLegs().isEmpty()) {
      return Collections.emptyList();
    }

    List<Transition> finalTransitions = finalStarTransitions(starElement.procedure());
    LOG.info("Attempting to link {} transitions of type {} to target airport element.", finalTransitions.size(), finalTransitions.get(0).transitionType());

    LinkedLegs airportLegs = highlander(airportElement.toLinkedLegs()).orElseThrow(IllegalStateException::new);

    return finalTransitions.stream()
        .map(transition -> new LinkedLegs(transition.legs().get(transition.legs().size() - 1), airportLegs.source(), matchWeightFor(airportLegs.source(), transition)))
        .collect(Collectors.toList());
  }

  private double matchWeightFor(Leg airportLeg, Transition transition) {
    Optional<Leg> locationLeg = lastLegWithLocation(transition);

    double distanceScore = locationLeg.flatMap(Leg::associatedFix)
        .map(fix -> airportLeg.associatedFix().orElseThrow(IllegalStateException::new).distanceInNmTo(fix))
        .orElse(LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT);

    // longer transitions are better :shrug: - this should have minimal effect
    return distanceScore / locationLeg.map(transition.legs()::indexOf).map(i -> i + 1.).orElse(1.);
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
