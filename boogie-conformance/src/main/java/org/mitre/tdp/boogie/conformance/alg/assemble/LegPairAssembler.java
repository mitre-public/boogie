package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.utils.Iterators;

/**
 * Assembly logic for converting an ordered sequence of legs into a set of leg pairs.
 */
public class LegPairAssembler {
  /**
   * A filter predicate for removing legs which are skippable/not required from the collection of legs to assemble.
   */
  private Predicate<Leg> legFilter = leg -> true;

  public LegPairAssembler setLegFilter(Predicate<Leg> legFilter) {
    this.legFilter = legFilter;
    return this;
  }

  public List<LegPairImpl> assemble(List<? extends Leg> legs) {
    return assemble(legs, LegPairImpl::new);
  }

  /**
   * Generates a collection of leg pairs from the input collection of sequentially ordered legs and the provided templated
   * leg assembler function.
   */
  public <L extends Leg, P extends LegPair> List<P> assemble(List<L> legs, BiFunction<L, L, P> assembler) {
    List<L> filteredLegs = legs.stream().filter(legFilter).collect(Collectors.toList());

    if (filteredLegs.size() < 2) {
      return Collections.emptyList();
    }

    List<P> res = new ArrayList<>();

    Iterators.pairwise(filteredLegs, (l1, l2) -> {
      P pair = assembler.apply(l1, l2);
      res.add(pair);
    });

    return res;
  }
}
