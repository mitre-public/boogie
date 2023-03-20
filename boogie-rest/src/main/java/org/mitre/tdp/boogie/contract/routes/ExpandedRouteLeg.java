package org.mitre.tdp.boogie.contract.routes;

import org.immutables.value.Value;
import org.mitre.tdp.boogie.alg.resolve.ElementType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableExpandedRouteLeg.class)
@JsonDeserialize(as = ImmutableExpandedRouteLeg.class)
public interface ExpandedRouteLeg {

  String section();

  ElementType elementType();

  String wildcards();

  /**
   * Return the delegate leg.
   */
  Leg leg();
}
