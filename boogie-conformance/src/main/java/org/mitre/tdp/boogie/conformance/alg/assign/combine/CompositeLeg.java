package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import java.util.LinkedHashSet;

import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;
import org.mitre.tdp.boogie.conformance.alg.assign.RouteAssigner;

/**
 * A composite leg represents a collection of related {@link FlyableLeg} members which can all be represented by the same
 * representative leg within the context of a {@link RouteAssigner} as decided by a {@link CombinationStrategy}.
 */
public final class CompositeLeg {

  private final LinkedHashSet<Route> routes;
  private final LinkedHashSet<FlyableLeg> members;

  public CompositeLeg(FlyableLeg representative) {
    this.routes = new LinkedHashSet<>();
    this.members = new LinkedHashSet<>();
    addMember(representative);
  }

  /**
   * Returns a {@link FlyableLeg} which is representative of all of the underlying {@link FlyableLeg}s but contains all of
   * their source routing information - so that it can be used in scoring.
   * <p>
   * e.g. a transition from leg {@code 1->2} gets higher score if they share a source route.
   */
  public FlyableLeg representative() {
    FlyableLeg representative = members.iterator().next();
    return new FlyableLeg(
        representative.previous().orElse(null),
        representative.current(),
        representative.next().orElse(null),
        routes
    );
  }

  public LinkedHashSet<FlyableLeg> members() {
    return members;
  }

  public CompositeLeg addMember(FlyableLeg linkedLeg) {
    this.routes.addAll(linkedLeg.routes());
    this.members.add(linkedLeg);
    return this;
  }

  public CompositeLeg unionWith(CompositeLeg compositeLeg) {
    this.routes.addAll(compositeLeg.routes);
    this.members.addAll(compositeLeg.members);
    return this;
  }
}
