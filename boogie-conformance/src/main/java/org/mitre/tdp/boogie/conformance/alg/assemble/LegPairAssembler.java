package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.utils.Iterators;

/**
 * Assembles only the concrete legs pair from a given input piece of infrastructure.
 *
 * When we say concrete legs we refer solely to those which terminate in a concrete fix.
 */
@FunctionalInterface
public interface LegPairAssembler extends ConsecutiveLegAssembler {

  /**
   * Returns whether the leg should be included or skipped when building the {@link LegPair}s.
   */
  boolean includeLeg(Leg leg);

  /**
   * Generates a collection of leg triples from the input list of legs - optionally in a bidirectional
   * fashion.
   */
  @Override
  default List<LegPair> assemble(List<? extends Leg> legs, boolean biDirectional) {
    if (legs.size() < 2) {
      return Collections.emptyList();
    }

    List<LegPair> res = new ArrayList<>();
    Iterators.fastslow2(legs, this::includeLeg, (l1, l2, skip) -> {
      LegPair pair = new LegPair(l1, l2, !skip.isEmpty());
      if (biDirectional) {
        res.add(pair.swap());
      }
      res.add(pair);
    });
    return res;
  }

  /**
   * Assembles a list of sequential, bidirectional, {@link LegTriple}s from the given {@link Airway}.
   */
  default List<LegPair> assembleFrom(Airway airway) {
    return assemble(airway.legs(), true);
  }

  /**
   * Assembles a list of sequential {@link LegTriple}s from the given {@link Transition}.
   */
  default List<LegPair> assembleFrom(Transition transition) {
    return assemble(transition.legs(), false);
  }

  /**
   * Returns a new {@link LegPairAssembler} which only returns leg pairs between concrete legs.
   */
  static LegPairAssembler concretePairs() {
    return leg -> leg.type().isConcrete();
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
