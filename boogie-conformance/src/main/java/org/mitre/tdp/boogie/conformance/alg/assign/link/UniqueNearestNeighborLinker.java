package org.mitre.tdp.boogie.conformance.alg.assign.link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.util.Combinatorics;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedNextLegValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is generally considered to be a failover option for transitioning between disconnection portions of route graph which aren't
 * otherwise covered by other options.
 * <p>
 * This class is really attempting to (given two potentially large sets of potential legs) the unique nearest pairwise connections
 * between legs originating from each set (which have location information - obviously).
 */
final class UniqueNearestNeighborLinker implements LegsLinker {
  private static final PathTerminatorBasedNextLegValidator VALIDATOR = new PathTerminatorBasedNextLegValidator();
  private static final Logger LOG = LoggerFactory.getLogger(UniqueNearestNeighborLinker.class);

  @Override
  public Collection<Pair<FlyableLeg, FlyableLeg>> apply(Collection<FlyableLeg> first, Collection<FlyableLeg> second) {
    List<FlyableLeg> firstCandidates = first.stream()
        .filter(i -> i.current().associatedFix().isPresent())
        .toList();
    List<FlyableLeg> secondCandidates = second.stream()
        .filter(l -> l.current().associatedFix().isPresent())
        .toList();

    NavigableMap<Double, List<Pair<FlyableLeg, FlyableLeg>>> distanceMap = Combinatorics.cartesianProduct(firstCandidates, secondCandidates).stream()
        .collect(Collectors.groupingBy(
            p -> distanceBetween(p.first().current(), p.second().current()),
            TreeMap::new,
            Collectors.toList()
        ));

    List<Pair<FlyableLeg, FlyableLeg>> pairs = new ArrayList<>();
    HashSet<FlyableLeg> usedPairs = new HashSet<>();

    distanceMap.forEach((distance, list) -> list.forEach(pair -> {
      if (!usedPairs.contains(pair.first()) && !usedPairs.contains(pair.second())) {
        pairs.add(Pair.of(pair.first(), pair.second()));
      }
      usedPairs.add(pair.first());
      usedPairs.add(pair.second());
    }));

    LOG.info("Returning with {} total unique leg pairings.", pairs.size());
    return pairs;
  }

  double distanceBetween(Leg l1, Leg l2) {
    return l1.associatedFix().orElseThrow().latLong().distanceInNM(l2.associatedFix().orElseThrow().latLong());
  }

}
