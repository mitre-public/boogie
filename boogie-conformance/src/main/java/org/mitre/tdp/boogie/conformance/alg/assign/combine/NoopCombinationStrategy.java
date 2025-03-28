package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

public final class NoopCombinationStrategy implements CombinationStrategy {

  @Override
  public Map<FlyableLeg, CompositeLeg> combineSimilar(Collection<FlyableLeg> flyableLegs) {
    return flyableLegs.stream().collect(Collectors.toMap(Function.identity(), CompositeLeg::new));
  }
}
