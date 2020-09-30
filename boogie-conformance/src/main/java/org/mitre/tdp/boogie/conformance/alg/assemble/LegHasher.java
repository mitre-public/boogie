package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Optional;

import org.mitre.tdp.boogie.Leg;

/**
 * Leg hashers are intended to be used in conjunction with the {@link GraphicalLegReducer} to determine which legs map to
 * common vertices in the graph of traversable infrastructure.
 */
@FunctionalInterface
public interface LegHasher {

  /**
   * Returns a hash for the given leg provided the leg contains the appropriate features to build the hash.
   */
  Optional<Integer> hash(Leg leg);

  /**
   * Chains the calling leg hasher with the provided leg hasher such that the provided will be used if the current cannot.
   */
  default LegHasher orElseBy(LegHasher legHasher) {
    return leg -> hash(leg).isPresent() ? hash(leg) : legHasher.hash(leg);
  }

  /**
   * Chains the calling leg hasher with the provided leg hasher such that they produce a composite hash value - if either
   * can't be computed then the composite hasher returns {@link Optional#empty()}.
   */
  default LegHasher andThenBy(LegHasher legHasher) {
    return leg -> hash(leg).filter(hash -> legHasher.hash(leg).isPresent()).map(hash -> hash * legHasher.hash(leg).orElseThrow(RuntimeException::new));
  }
}
