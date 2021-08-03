package org.mitre.tdp.boogie.alg.resolve;

import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.distanceBetween;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.finalStarTransitions;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.util.TransitionSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class StarToApproachLinker implements BiFunction<StarElement, ApproachElement, List<LinkedLegs>> {

  private static final Logger LOG = LoggerFactory.getLogger(StarToApproachLinker.class);

  static final StarToApproachLinker INSTANCE = new StarToApproachLinker();

  private StarToApproachLinker() {
  }

  @Override
  public List<LinkedLegs> apply(StarElement starElement, ApproachElement approachElement) {

    if (starElement.toLinkedLegs().isEmpty() || approachElement.toLinkedLegs().isEmpty()) {
      return Collections.emptyList();
    }

    List<Transition> finalStarTransitions = finalStarTransitions(starElement.procedure());
    List<Transition> initialApproachTransitions = initialApproachTransitions(approachElement);
    LOG.debug("Building connections between {} final STAR transition and {} initial approach transitions.", finalStarTransitions.size(), initialApproachTransitions.size());

    List<Leg> terminalStarLegs = finalStarTransitions.stream()
        .filter(transition -> !transition.legs().isEmpty())
        .map(LinkingUtils::lastLegWithLocation)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toList());

    List<Leg> initialApproachLegs = initialApproachTransitions.stream()
        .filter(transition -> !transition.legs().isEmpty())
        .map(LinkingUtils::firstLegWithLocation)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(toList());

    LOG.debug("Connecting {} terminal STAR legs to {} initial approach legs.", terminalStarLegs.size(), initialApproachLegs.size());
    return cartesianProduct(terminalStarLegs, initialApproachLegs).stream()
        .map(pair -> new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)))
        .collect(toList());
  }

  private List<Transition> initialApproachTransitions(ApproachElement approachElement) {
    return TransitionSorter.INSTANCE
        .sortApproachTransitions(approachElement.procedure().transitions())
        .stream().filter(col -> !col.isEmpty())
        .findFirst().orElseThrow(IllegalStateException::new);
  }
}
