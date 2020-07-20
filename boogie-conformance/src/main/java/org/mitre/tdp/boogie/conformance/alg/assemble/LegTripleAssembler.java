package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.utils.Iterators;

public interface LegTripleAssembler extends ConsecutiveLegAssembler {

  /**
   * Generates a collection of leg triples from the input list of legs - optionally in a bidirectional
   * fashion.
   */
  @Override
  default List<LegTriple> assemble(List<? extends Leg> legs, boolean biDirectional) {
    if (legs.isEmpty()) {
      return Collections.emptyList();
    }

    List<LegTriple> res = new ArrayList<>();
    List<LegTriple> forward = new ArrayList<>();
    if (legs.size() == 1) {
      forward.add(new LegTriple(null, legs.get(0), null));
    } else if (legs.size() == 2) {
      forward.add(new LegTriple(legs.get(0), legs.get(1), null));
      forward.add(new LegTriple(null, legs.get(0), legs.get(1)));
    } else {
      forward.add(new LegTriple(null, legs.get(0), legs.get(1)));
      Iterators.triples(legs, (l1, l2, l3) -> forward.add(new LegTriple(l1, l2, l3)));
      forward.add(new LegTriple(legs.get(legs.size() - 2), legs.get(legs.size() - 1), null));
    }

    res.addAll(forward);
    if (biDirectional) {
      forward.forEach(triple -> res.add(triple.swap()));
    }
    return res;
  }

  /**
   * Assembles a list of sequential, bidirectional, {@link LegTriple}s from the given {@link Airway}.
   */
  default List<LegTriple> assembleFrom(Airway airway) {
    return assemble(airway.legs(), true);
  }

  /**
   * Assembles a list of sequential {@link LegTriple}s from the given {@link Transition}.
   */
  default List<LegTriple> assembleFrom(Transition transition) {
    return assemble(transition.legs(), false);
  }

  static LegTripleAssembler allTriples() {
    return new LegTripleAssembler() {};
  }
}
