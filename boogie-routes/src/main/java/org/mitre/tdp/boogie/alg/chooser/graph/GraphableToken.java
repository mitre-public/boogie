package org.mitre.tdp.boogie.alg.chooser.graph;

import java.util.Collection;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.FixRadialDistance;
import org.mitre.tdp.boogie.alg.chooser.GraphBasedRouteChooser;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

/**
 * This class largely mirrors much of the hierarchy of {@link ResolvedToken} but decorates them with behavior specific to rendering
 * as part of a graph (to support the route-choosing process).
 *
 * <p>
 *
 * <p>This class supports the {@link LinkingStrategy#standard()} implementation through use of double-dispatch to allow clean specific
 * control over of tokens of different types are linked together.
 */
interface GraphableToken extends LinkingVisitor {

  /**
   * See {@link ResolvedToken.StandardAirport} for details.
   */
  static GraphableToken standardAirport(Airport airport) {
    return new StandardAirport(airport);
  }

  /**
   * See {@link ResolvedToken.DirectToAirport} for details.
   */
  static GraphableToken directToAirport(Airport airport) {
    return new DirectToAirport(airport);
  }

  /**
   * See {@link ResolvedToken.StandardAirway} for details.
   */
  static GraphableToken standardAirway(Airway airway) {
    return new StandardAirway(airway);
  }

  /**
   * See {@link ResolvedToken.StandardApproach} for details.
   */
  static GraphableToken standardApproach(Procedure approach) {
    return new StandardApproach(approach);
  }

  /**
   * See {@link ResolvedToken.StandardFix} for details.
   */
  static GraphableToken standardFix(Fix fix) {
    return new StandardFix(fix);
  }

  /**
   * See {@link ResolvedToken.DirectToFix} for details.
   */
  static GraphableToken directToFix(Fix fix) {
    return new DirectToFix(fix);
  }

  /**
   * See {@link ResolvedToken.StandardLatLong} for details.
   */
  static GraphableToken standardLatLong(LatLong latLong) {
    return new StandardLatLong(latLong);
  }

  /**
   * See {@link ResolvedToken.DirectToLatLong} for details.
   */
  static GraphableToken directToLatLong(LatLong latLong) {
    return new DirectToLatLong(latLong);
  }

  /**
   * See {@link ResolvedToken.SidEnrouteCommon} for details.
   */
  static GraphableToken sidEnrouteCommon(Procedure sid) {
    return new SidEnrouteCommon(sid);
  }

  /**
   * See {@link ResolvedToken.SidRunway} for details.
   */
  static GraphableToken sidRunway(Procedure sid) {
    return new SidRunway(sid);
  }

  /**
   * See {@link ResolvedToken.StarEnrouteCommon} for details.
   */
  static GraphableToken starEnrouteCommon(Procedure star) {
    return new StarEnrouteCommon(star);
  }

  /**
   * See {@link ResolvedToken.StarRunway} for details.
   */
  static GraphableToken starRunway(Procedure star) {
    return new StarRunway(star);
  }

  /**
   * See {@link ResolvedToken.StandardFrd} for details.
   */
  static GraphableToken standardFrd(FixRadialDistance frd) {
    return new StandardFrd(frd);
  }

  /**
   * See {@link ResolvedToken.DirectToFrd} for details.
   */
  static GraphableToken directToFrd(FixRadialDistance frd) {
    return new DirectToFrd(frd);
  }

  /**
   * Returns a collection of {@link LinkedLegs} representing the token as a collection of edges in a graph.
   *
   * <p>This method backs {@link LinkingStrategy#graphRepresentation(ResolvedToken)} for the {@link LinkingStrategy#standard()}
   * and serves as the default representation of an object for use with the {@link GraphBasedRouteChooser}.
   */
  Collection<LinkedLegs> linkedLegs();

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
