package org.mitre.tdp.boogie.alg.resolve;

import static java.util.stream.Collectors.toList;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.distanceBetween;
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

public class SidToApproachLinker implements BiFunction<SidToken, ApproachToken, List<LinkedLegs>> {

  private static final Logger LOG = LoggerFactory.getLogger(SidToApproachLinker.class);

  public static final SidToApproachLinker INSTANCE = new SidToApproachLinker();

  private SidToApproachLinker() {

  }

  @Override
  public List<LinkedLegs> apply(SidToken sidElement, ApproachToken approachElement) {
    if (sidElement.toLinkedLegs().isEmpty() || approachElement.toLinkedLegs().isEmpty()) {
      return Collections.emptyList();
    }

    List<Transition> finalSidTransitions = LinkingUtils.finalSidTransitions(sidElement.procedure());

    List<Leg> sidLegs = finalSidTransitions.stream()
        .filter(transition -> !transition.legs().isEmpty())
        .map(LinkingUtils::lastLegWithLocation)
        .flatMap(Optional::stream)
        .collect(toList());

    List<Leg> initialApproachLegs = orElse(LinkingUtils.approachTransitions, LinkingUtils.finalApproach).apply(approachElement);

    LOG.debug("Connecting {} final SID legs to {} initial approach legs.", sidLegs.size(), initialApproachLegs.size());
    List<LinkedLegs> linkedLegs = cartesianProduct(sidLegs, initialApproachLegs).stream()
        .map(pair -> new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)))
        .collect(toList());

    return linkedLegs;
  }

}
