package org.mitre.tdp.boogie.conformance.alg.assign.link;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose of the nearest Fx linker is to (within the context of the {@link PhaseOfFlightLinker}) link the legs from the
 * previous part of the envelope to their (potentially disconnected) downstream fixes from the second part of the envelope.
 */
final class NearestFxLinker implements LegsLinker, Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(NearestFxLinker.class);

  @Override
  public Collection<Pair<FlyableLeg, FlyableLeg>> apply(Collection<FlyableLeg> first, Collection<FlyableLeg> second) {
    List<FlyableLeg> firstCandidates = first.stream().filter(pair -> pair.current().associatedFix().isPresent()).collect(Collectors.toList());

    List<Pair<FlyableLeg, FlyableLeg>> pairs = second.stream()
        .filter(flyableLeg -> flyableLeg.current().associatedFix().isPresent() && flyableLeg.current().pathTerminator().isFixOriginating())
        .flatMap(flyableLeg -> {
          List<FlyableLeg> nearest = findNearest(firstCandidates, flyableLeg);
          // also include the legs that come after nearest, in case the route change happens near the start of a leg
          List<FlyableLeg> next = findNext(firstCandidates, nearest);

          return Stream.concat(nearest.stream(), next.stream())
              .distinct()
              .map(l -> Pair.of(l, flyableLeg));
        })
        .collect(Collectors.toList());

    LOG.info("Returning with an additional {} links.", pairs.size());
    return pairs;
  }

  private List<FlyableLeg> findNearest(List<FlyableLeg> firstCandidates, FlyableLeg flyableLeg) {
    Map.Entry<Double, List<FlyableLeg>> nearest = firstCandidates.stream()
        .collect(Collectors.groupingBy(
            candidate -> distanceBetween(candidate.current(), flyableLeg.current()),
            TreeMap::new,
            Collectors.toList()))
        .firstEntry();
    return nearest != null ? nearest.getValue() : Collections.emptyList();
  }

  private List<FlyableLeg> findNext(List<FlyableLeg> firstCandidates, List<FlyableLeg> nearest) {
    Set<Leg> nextLegs = nearest.stream().map(x -> x.next().orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet());
    return firstCandidates.stream()
        .filter(x -> nextLegs.contains(x.current()))
        .collect(Collectors.toList());
  }

  double distanceBetween(Leg l1, Leg l2) {
    return l1.associatedFix().orElseThrow().distanceInNmTo(l2.associatedFix().orElseThrow());
  }
}
