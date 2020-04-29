package org.mitre.tdp.boogie.conformance.alg;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.mitre.tdp.boogie.conformance.Scorable;
import org.mitre.tdp.boogie.conformance.Scored;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Wrapper class for convenience access to a collection {@link Scorable}s.
 */
public class Scorables<L extends Scorable> {
  /**
   * Map of legs by their hashcode for quick lookup.
   */
  private final Map<Integer, Scored<L>> legs;
  /**
   * BiMap of the pre-determined leg index (number from 1-legs.size())
   * used for searching for offsets in the distance matrices in the
   * object.
   *
   * Map direction is initially <Offset, HashCode>.
   */
  private final BiMap<Integer, Integer> index;

  private Scorables(Map<Integer, Scored<L>> h, BiMap<Integer, Integer> o) {
    this.legs = h;
    this.index = o;
  }

  public static <L extends Scorable> Scorables<L> of(List<Scored<L>> legs) {
    BiMap<Integer, Integer> biMap = HashBiMap.create();
    Map<Integer, Scored<L>> map = new HashMap<>();

    IntStream.range(0, legs.size())
        .forEach(i -> {
          Scored<L> leg = legs.get(i);
          int hash = leg.hashCode();
          biMap.put(i, hash);
          map.put(hash, leg);
        });
    return new Scorables<>(map, biMap);
  }

  public Map<Integer, Scored<L>> legs() {
    return legs;
  }

  public BiMap<Integer, Integer> index() {
    return index;
  }

  public BiMap<Integer, Integer> inverseIndex() {
    return index.inverse();
  }

  public List<Scored<L>> list() {
    return index.values().stream()
        // sort legs by index
        .sorted(Comparator.comparing(v -> inverseIndex().get(v)))
        // dereference legs
        .map(legs::get)
        .collect(Collectors.toList());
  }
}
