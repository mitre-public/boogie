package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.LegPair;
import org.mitre.tdp.boogie.utils.Iterators;

public final class LegPairAssembler {

  private LegPairAssembler() {
    throw new IllegalStateException();
  }

  public static List<LegPair> assemble(List<? extends Leg> legs) {
    return assemble(legs, LegPair::new);
  }

  public static <L extends Leg, P extends LegPair> List<P> assemble(List<L> legs, BiFunction<L, L, P> assembler) {
    return assemble(legs, l -> true, assembler);
  }

  /**
   * Generates a collection of leg pairs from the input collection of sequentially ordered legs and the provided templated
   * leg assembler function.
   */
  public static <L extends Leg, P extends LegPair> List<P> assemble(List<L> legs, Predicate<L> legFilter, BiFunction<L, L, P> assembler) {
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
