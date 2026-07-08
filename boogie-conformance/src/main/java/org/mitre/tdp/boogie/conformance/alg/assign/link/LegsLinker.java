package org.mitre.tdp.boogie.conformance.alg.assign.link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiFunction;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

import com.google.common.annotations.Beta;

/**
 * This interface creates links between the first and second collection of legs.
 */
@FunctionalInterface
public interface LegsLinker extends BiFunction<Collection<FlyableLeg>, Collection<FlyableLeg>, Collection<Pair<FlyableLeg, FlyableLeg>>> {

  default LegsLinker orElseApply(LegsLinker legsLinker) {
    return (col1, col2) -> {
      Collection<Pair<FlyableLeg, FlyableLeg>> connections = apply(col1, col2);
      return connections.isEmpty() ? legsLinker.apply(col1, col2) : connections;
    };
  }

  default LegsLinker andThenApply(LegsLinker legsLinker) {
    return (col1, col2) -> {
      Collection<Pair<FlyableLeg, FlyableLeg>> pairs = new ArrayList<>();
      pairs.addAll(apply(col1, col2));
      pairs.addAll(legsLinker.apply(col1, col2));
      return pairs;
    };
  }
}
