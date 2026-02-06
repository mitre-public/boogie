package org.mitre.tdp.boogie.conformance.alg.assign.link;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLegAssembler;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;
import org.mitre.tdp.boogie.util.Combinatorics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.Beta;

/**
 * This class wants all route suppliers to be split up by phase of flight. Then there are "glue" sections added to the
 * route (e.g., departureTransitions). If the routes are not otherwise linked directly via shared fixes.
 * <p>
 * The attempt here is to link the routes and then add catches for when the published routes/fixds do not directly link.
 */
@Beta
public class AllPhasesOfFlightLinker implements LinkingStrategy, Serializable {
  private static final Logger LOG = LoggerFactory.getLogger(AllPhasesOfFlightLinker.class);
  private final Collection<Route> departureAirports;
  private final Collection<Route> sids;
  private final Collection<Route> departureTransition;
  private final Collection<Route> enroutes;
  private final Collection<Route> terminalArea1;
  private final Collection<Route> stars;
  private final Collection<Route> terminalArea2;
  private final Collection<Route> approaches;
  private final Collection<Route> arrivalAirports;

  public AllPhasesOfFlightLinker(Collection<Route> departureAirports, Collection<Route> sids, Collection<Route> departureTransition, Collection<Route> enroutes, Collection<Route> terminalArea1, Collection<Route> stars, Collection<Route> terminalArea2, Collection<Route> approaches, Collection<Route> arrivalAirports) {
    this.departureAirports = departureAirports;
    this.sids = sids;
    this.departureTransition = departureTransition;
    this.enroutes = enroutes;
    this.terminalArea1 = terminalArea1;
    this.stars = stars;
    this.terminalArea2 = terminalArea2;
    this.approaches = approaches;
    this.arrivalAirports = arrivalAirports;
  }

  private static Collection<Pair<FlyableLeg, FlyableLeg>> leftToRight(Collection<FlyableLeg> leftLegs, Collection<Route> downStreamRoutes) {
    if (downStreamRoutes.isEmpty()) {
      return List.of();
    }

    List<FlyableLeg> allDownStreamLegs = downStreamRoutes.stream()
        .map(FlyableLegAssembler::assemble)
        .flatMap(Collection::stream)
        .toList();
    List<FlyableLeg> firstDownStreamLegs = downStreamRoutes.stream()
        .map(AllPhasesOfFlightLinker::singletonLeg)
        .toList();

    //we want to link to the next thing's Fx legs which are generally at the start of the route and can fly direct.
    //we then want a link because these nearest neighbors can probably be flown to directly so.
    LegsLinker fixLinker = new NearestFxLinker().andThenApply(new UniqueNearestNeighborLinker());
    Collection<Pair<FlyableLeg, FlyableLeg>> fixLinks = fixLinker.apply(leftLegs, allDownStreamLegs);

    //we also want a link to the first thing there even if there is no fix.
    Collection<Pair<FlyableLeg, FlyableLeg>> firstLinks = Combinatorics.cartesianProduct(leftLegs, firstDownStreamLegs);

    return Stream.concat(firstLinks.stream(), fixLinks.stream()).collect(Collectors.toSet());
  }

  private static FlyableLeg singletonLeg(Route route) {
    return new FlyableLeg(null, route.legs().get(0), null, route);
  }

  private static Collection<Route> firstThingThere(Collection<Collection<Route>> downstream) {
    return downstream.stream()
        .filter(routes -> !routes.isEmpty())
        .findFirst()
        .orElseThrow();
  }

  @Override
  public Collection<Pair<FlyableLeg, FlyableLeg>> links(Collection<FlyableLeg> flyableLegs) {

    Collection<Pair<FlyableLeg, FlyableLeg>> departureAirportLinks = linkToDownstream(departureAirports, List.of(sids, departureTransition, enroutes, terminalArea1, stars, terminalArea2, approaches, arrivalAirports));
    Collection<Pair<FlyableLeg, FlyableLeg>> sidLinks = linkToDownstream(sids, List.of(departureTransition, enroutes, terminalArea1, stars, terminalArea2, approaches, arrivalAirports));
    Collection<Pair<FlyableLeg, FlyableLeg>> departureTransitionLinks = linkToDownstream(departureTransition, List.of(enroutes, terminalArea1, stars, terminalArea2, approaches, arrivalAirports));
    Collection<Pair<FlyableLeg, FlyableLeg>> enrouteLinks = linkToDownstream(enroutes, List.of(terminalArea1, stars, terminalArea2, approaches, arrivalAirports));
    Collection<Pair<FlyableLeg, FlyableLeg>> terminalArea1Links = linkToDownstream(terminalArea1, List.of(stars, terminalArea2, approaches, arrivalAirports));
    Collection<Pair<FlyableLeg, FlyableLeg>> starLinks = linkToDownstream(stars, List.of(terminalArea2, approaches, arrivalAirports));
    Collection<Pair<FlyableLeg, FlyableLeg>> terminalArea2Links = linkToDownstream(terminalArea2, List.of(approaches, arrivalAirports));
    Collection<Pair<FlyableLeg, FlyableLeg>> approachLinks = linkToDownstream(approaches, List.of(arrivalAirports));

    Collection<Pair<FlyableLeg, FlyableLeg>> links = Stream.of(departureAirportLinks, sidLinks, departureTransitionLinks, enrouteLinks, terminalArea1Links, starLinks, terminalArea2Links, approachLinks)
        .flatMap(Collection::stream)
        .toList();
    LOG.info("Returning all links with the following links: {}", links.size());
    return links;
  }

  Collection<Pair<FlyableLeg, FlyableLeg>> linkToDownstream(Collection<Route> left, Collection<Collection<Route>> downstream) {
    Collection<FlyableLeg> departureAirportLegs = left.stream()
        .map(FlyableLegAssembler::assemble)
        .flatMap(Collection::stream)
        .toList();

    Collection<Route> firstThingThere = firstThingThere(downstream);

    return leftToRight(departureAirportLegs, firstThingThere);
  }
}
