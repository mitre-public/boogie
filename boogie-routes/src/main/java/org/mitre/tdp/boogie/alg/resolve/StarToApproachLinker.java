package org.mitre.tdp.boogie.alg.resolve;

import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.distanceBetween;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.finalStarTransitions;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.orElse;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class StarToApproachLinker implements BiFunction<StarToken, ApproachToken, List<LinkedLegs>> {

  private static final Logger LOG = LoggerFactory.getLogger(StarToApproachLinker.class);

  static final StarToApproachLinker INSTANCE = new StarToApproachLinker();

  private StarToApproachLinker() {
  }

  @Override
  public List<LinkedLegs> apply(StarToken starElement, ApproachToken approachElement) {

    if (starElement.toLinkedLegs().isEmpty() || approachElement.toLinkedLegs().isEmpty()) {
      return Collections.emptyList();
    }

    List<Transition> finalStarTransitions = finalStarTransitions(starElement.procedure());

    List<Leg> terminalStarLegs = finalStarTransitions.stream()
        .filter(transition -> !transition.legs().isEmpty())
        .map(LinkingUtils::lastLegWithLocation)
        .flatMap(Optional::stream)
        .collect(toList());

    List<Leg> initialApproachLegs = orElse(LinkingUtils.approachTransitions, LinkingUtils.finalApproach).apply(approachElement);

    LOG.debug("Connecting {} terminal STAR legs to {} initial approach legs.", terminalStarLegs.size(), initialApproachLegs.size());
    return cartesianProduct(terminalStarLegs, initialApproachLegs).stream()
        .map(pair -> new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)))
        .collect(toList());
  }
}