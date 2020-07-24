package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.utils.Iterators;

/**
 * Assembles only the concrete legs pair from a given input piece of infrastructure.
 *
 * When we say concrete legs we refer solely to those which terminate in a concrete fix.
 */
@FunctionalInterface
public interface LegPairAssembler {

  /**
   * Returns whether the leg should be included or skipped when building the {@link LegPair}s.
   */
  boolean includeLeg(Leg leg);

  /**
   * Generates a collection of leg triples from the input list of legs - optionally in a bidirectional
   * fashion.
   */
  default List<LegPair> assemble(List<? extends Leg> legs) {
    if (legs.stream().filter(this::includeLeg).count() < 2) {
      return Collections.emptyList();
    }

    List<LegPair> res = new ArrayList<>();
    Iterators.fastslow2(legs, this::includeLeg, (l1, l2, skip) -> {
      LegPair pair = new LegPairImpl(l1, l2);
      res.add(pair);
    });
    return res;
  }

  /**
   * Returns a new {@link LegPairAssembler} which returns all consecutive leg pairs regardless of leg type.
   */
  static LegPairAssembler allPairs() {
    return leg -> true;
  }

  /**
   * Returns a new {@link LegPairAssembler} which only returns legs matching the given predicate.
   */
  static LegPairAssembler onlyAllow(Predicate<Leg> includeLeg) {
    return includeLeg::test;
  }
}
