package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.List;

import org.mitre.tdp.boogie.Leg;

/**
 * Converts a collection of infrastructure elements into a collection of {@link ConsecutiveLegs}.
 */
@FunctionalInterface
public interface ConsecutiveLegAssembler {

  /**
   * Assembles a collection of consecutive legs from the input infrastructure data.
   */
  List<? extends ConsecutiveLegs> assemble(List<? extends Leg> legs, boolean biDirectional);
}
