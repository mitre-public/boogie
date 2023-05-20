package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.split.RouteToken;

/**
 * This class represents a graph node as a {@link RouteToken} as extracted from the top level route
 * string, a top level resolved infrastructure element associated with that split {@link ResolvedToken}
 * and a leg indicating both what the fix is as well as an indication of how to fly to it.
 *
 * <p>e.g. For airways the expanded legs are {@link PathTerminator#TF} while for ..FIX.. elements the leg type is
 * indicated as {@link PathTerminator#IF} while for procedures they are as declared in the procedure definition.
 */
public final class ResolvedLeg {
  /**
   * The {@link RouteToken} from the route string used to generate this reference.
   */
  private final RouteToken sectionSplit;
  /**
   * A pointer to the {@link ResolvedToken} which generated the given leg for the top level split.
   */
  private final ResolvedToken resolvedToken;
  /**
   * The actual resolved leg of the route.
   */
  private final Leg leg;

  public ResolvedLeg(RouteToken sectionSplit, ResolvedToken resolvedToken, Leg leg) {
    this.sectionSplit = requireNonNull(sectionSplit);
    this.resolvedToken = requireNonNull(resolvedToken);
    this.leg = requireNonNull(leg);
  }

  public RouteToken split() {
    return sectionSplit;
  }

  public Leg leg() {
    return leg;
  }

  public ResolvedToken sourceElement() {
    return resolvedToken;
  }
}
