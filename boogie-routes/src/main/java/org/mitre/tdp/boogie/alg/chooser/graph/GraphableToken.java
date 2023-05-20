package org.mitre.tdp.boogie.alg.chooser.graph;

import java.util.Collection;

import org.mitre.tdp.boogie.alg.resolve.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.AirwayElement;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;

public interface GraphableToken extends LinkingVisitor {

  /**
   * Implementation of a {@link GraphableToken} which wraps a an {@link AirportElement} for linking.
   */
  static GraphableToken airport(AirportElement airport) {
    return new GraphableDirectToAirport(airport);
  }

  /**
   * Implementation of {@link GraphableToken} which wraps an {@link AirwayElement} for linking.
   */
  static GraphableToken airway(AirwayElement airway) {
    return new GraphableAirway(airway);
  }

  /**
   * Returns a view of the token as a collection of linked legs (representing a previous and next leg between which the token
   * indicates there is a valid transition).
   */
  Collection<LinkedLegs> toLinkedLegs();

  /**
   * Tokens can accept other visitors and themselves are visitors of other tokens. This is used to generate links between pairs
   * of subsequent resolved tokens from the underlying route string.
   *
   * <p>This is an implementation of the <a href="https://refactoring.guru/design-patterns/visitor-double-dispatch">double-dispatch</a>
   * pattern whereby the behaviour of the linking is determined by the concrete types of both the caller and the callee at runtime.
   */
  Linker accept(LinkingVisitor visitor);
}
