package org.mitre.tdp.boogie.alg.chooser.graph;

import java.util.Collection;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.FixRadialDistance;
import org.mitre.tdp.boogie.alg.chooser.GraphBasedRouteChooser;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

/**
 * Linkable tokens represent pieces of infrastructure encapsulated by
 */
interface LinkableToken extends LinkingVisitor {

  /**
   * A linkable token representing any airport.
   *
   * <p>This class defines a standard strategy for linking an {@link Airport} to other resolved infrastructure types regardless of
   * how it may have been filed.
   *
   * @param airport             typically from {@link ResolvedToken.StandardAirport} or {@link ResolvedToken.DirectToAirport}
   * @param graphRepresentation a collection of linked legs constituting the representation of this object in the graph
   */
  static LinkableToken anyAirport(Airport airport, Collection<LinkedLegs> graphRepresentation) {
    return new AnyAirport(airport, graphRepresentation);
  }

  /**
   * A linkable token representing any airway.
   *
   * <p>This class defines a standard strategy for linking an {@link Airway} to other resolved infrastructure types regardless of
   * how it may have been filed.
   *
   * @param airway typically from {@link ResolvedToken.StandardAirway}
   */
  static LinkableToken anyAirway(Airway airway, Collection<LinkedLegs> graphRepresentation) {
    return new AnyAirway(airway, graphRepresentation);
  }

  /**
   * See {@link ResolvedToken.StandardApproach} for details.
   */
  static LinkableToken anyApproach(Procedure approach, Collection<LinkedLegs> graphRepresentation) {
    return new AnyApproach(approach, graphRepresentation);
  }

  /**
   * See {@link ResolvedToken.StandardFix} for details.
   */
  static LinkableToken anyFix(Fix fix, Collection<LinkedLegs> graphRepresentation) {
    return new AnyFix(fix, graphRepresentation);
  }

  /**
   * See {@link ResolvedToken.StandardLatLong} for details.
   */
  static LinkableToken anyLatLong(LatLong latLong, Collection<LinkedLegs> graphRepresentation) {
    return new AnyLatLong(latLong, graphRepresentation);
  }

  /**
   * See {@link ResolvedToken.SidEnrouteCommon} for details.
   */
  static LinkableToken anySid(Procedure sid, Collection<LinkedLegs> graphRepresentation) {
    return new AnySid(sid, graphRepresentation);
  }

  /**
   * See {@link ResolvedToken.StarEnrouteCommon} for details.
   */
  static LinkableToken anyStar(Procedure star, Collection<LinkedLegs> graphRepresentation) {
    return new AnyStar(star, graphRepresentation);
  }

  /**
   * See {@link ResolvedToken.StandardFrd} for details.
   */
  static LinkableToken anyFrd(FixRadialDistance frd, Collection<LinkedLegs> graphRepresentation) {
    return new AnyFrd(frd, graphRepresentation);
  }

  /**
   * Returns a collection of {@link LinkedLegs} representing the token as a collection of edges in a graph.
   *
   * <p>This method backs {@link LinkingStrategy#graphRepresentation(ResolvedToken)} for the {@link LinkingStrategy#standard()}
   * and serves as the default representation of an object for use with the {@link GraphBasedRouteChooser}.
   */
  Collection<LinkedLegs> graphRepresentation();

  /**
   * Generic implementation of the visitor pattern for single-dispatch allowing various downstream collaborators to collect token
   * specific information (such as associated infrastructure, etc.).
   *
   * <p>While similar to {@link #accept(LinkingVisitor)}, that method is a specialization suitable for a double-dispatch approach
   * and therefore can't be unified under this abstraction...
   */
  void accept(LinkableTokenVisitor visitor);

  /**
   * Returns a {@link Linker} which can be used to summon links between this object and the visiting one. This implementation backs
   * {@link LinkingStrategy#links(ResolvedToken, ResolvedToken)} for the {@link LinkingStrategy#standard()}. Service as the default
   * linking strategy for the {@link GraphBasedRouteChooser}.
   *
   * <p>Tokens can accept other visitors and themselves are visitors of other tokens. This is used to generate links between pairs
   * of subsequent resolved tokens from the underlying route string.
   *
   * <p>This is an implementation of the <a href="https://refactoring.guru/design-patterns/visitor-double-dispatch">double-dispatch</a>
   * pattern whereby the behaviour of the linking is determined by the concrete types of both the caller and the callee at runtime.
   */
  Linker accept(LinkingVisitor visitor);
}
