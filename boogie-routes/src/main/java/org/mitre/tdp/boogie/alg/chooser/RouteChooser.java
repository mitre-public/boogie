package org.mitre.tdp.boogie.alg.chooser;

import java.util.List;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.ResolvedLeg;
import org.mitre.tdp.boogie.alg.chooser.graph.LinkableToken;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenMapper;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.split.RouteToken;

/**
 * A route chooser is responsible for choosing a path through a sequence of {@link ResolvedTokens} in according to the contract
 * laid out in {@link #chooseRoute(List)}.
 */
@FunctionalInterface
public interface RouteChooser {

  /**
   * Returns a new {@link RouteChooser} which will use a shortest-path graph-traversal algorithm to determine the most likely
   * sequence of elements traversed by the flight.
   *
   * @param mapper mapper class for converting incoming {@link ResolvedToken} implementations to their {@link LinkableToken} forms
   *               for use in graph generation
   */
  static RouteChooser graphical(TokenMapper mapper) {
    return new GraphicalRouteChooser(mapper);
  }

  /**
   * Returns a chosen path through the provided resolved tokens.
   *
   * <p>"Choosing" in this context has a twofold meaning:
   * <ol>
   *   <li><i>Choosing</i> exactly one {@link ResolvedToken} for each {@link RouteToken}</li>
   *   <li><i>Choosing</i> the sequence of {@link Leg}s from that {@link ResolvedToken} which were traversed and in what order</li>
   * </ol>
   *
   * <p>This method returns a sequence of {@link ResolvedLeg}s in flown order, where each input {@link RouteToken} is represented
   * by at least one resolved leg so long as it was resolved to a non-zero number of {@link ResolvedToken}s.
   *
   * @param resolvedTokens the ordered sequence of resolved tokens from the route string
   * @return the sequence of resolved legs in the order they're intended to be flown
   */
  List<ResolvedLeg> chooseRoute(List<ResolvedTokens> resolvedTokens);
}
