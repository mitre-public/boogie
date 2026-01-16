package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * This class allows for stacking of hash functions.
 */
public final class HybridHasher implements Function<FlyableLeg, Integer> {
  private final List<Function<FlyableLeg, Integer>> functions;

  private HybridHasher(List<Function<FlyableLeg, Integer>> functions) {
    this.functions = functions;
  }

  public static HybridHasher from(List<Function<FlyableLeg, Integer>> functions) {
    return new HybridHasher(functions);
  }

  @Override
  public Integer apply(FlyableLeg flyableLeg) {
    HashCodeBuilder builder = new HashCodeBuilder();
    functions.stream()
        .map(function -> function.apply(flyableLeg))
        .forEach(builder::append);
    return builder.toHashCode();
  }
}
