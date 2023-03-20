package org.mitre.tdp.boogie.contract.routes;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableTurnDirection.class)
@JsonDeserialize(as = ImmutableTurnDirection.class)
public interface TurnDirection {
  /**
   * True when the direction of the turn is to the left of the traversal path. Both true if either.
   */
  boolean isLeft();

  /**
   * True when the direction of the turn is to the right of the traversal path. Both true if either.
   */
  boolean isRight();
}
