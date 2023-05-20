package org.mitre.tdp.boogie.alg.chooser;

import java.util.List;

import org.mitre.tdp.boogie.alg.ExpandedRoute;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;

/**
 * A {@link RouteChooser} is responsible for converting the {@link ResolvedSection}s from a route string and choosing the path
 * through all of them which is the most sensible.
 */
@FunctionalInterface
public interface RouteChooser {

  /**
   * Returns a new {@link RouteChooser} which will use a shortest-path graph-traversal algorithm to determine the most likely
   * sequence of elements traversed by the flight.
   */
  static RouteChooser graphical() {
    return new GraphBasedRouteChooser();
  }

  /**
   * Create a new {@link ExpandedRoute} from the provided ordered sequence of {@link ResolvedSection}s extracted from the route
   * string. Each section contains a list of candidate {@link ResolvedToken}s which must be chosen between.
   *
   * @param resolvedSections the sections of the route string (in the order they were filed)
   */
  ExpandedRoute chooseRoute(List<ResolvedSection> resolvedSections);
}
