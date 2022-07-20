package org.mitre.tdp.boogie.alg.resolve;

import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.distanceBetween;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.orElse;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Leg;

public class SectionToApproachLinker implements BiFunction<ResolvedElement, ApproachElement, List<LinkedLegs>> {

  public static final SectionToApproachLinker INSTANCE = new SectionToApproachLinker();

  private SectionToApproachLinker()  {

  }

  @Override
  public List<LinkedLegs> apply(ResolvedElement resolvedElement, ApproachElement approachElement) {
    List<Leg> elementLegs = withLocation(resolvedElement.toLinkedLegs());
    List<Leg> approachStartLegs = orElse(LinkingUtils.approachTransitions, LinkingUtils.finalApproach).apply(approachElement);

    return cartesianProduct(elementLegs, approachStartLegs).stream()
        .min(Comparator.comparing(LinkingUtils::distanceBetween))
        .map(pair -> new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)))
        .filter(pair -> pair.linkWeight() < Double.MAX_VALUE)
        .map(List::of)
        .orElse(Collections.emptyList());
  }

  private static List<Leg> withLocation(List<LinkedLegs> linkedLegs) {
    return linkedLegs.stream()
        .flatMap(linked -> Stream.of(linked.source(), linked.target()))
        .distinct()
        .filter(leg -> leg.associatedFix().isPresent())
        .collect(Collectors.toList());
  }
}
