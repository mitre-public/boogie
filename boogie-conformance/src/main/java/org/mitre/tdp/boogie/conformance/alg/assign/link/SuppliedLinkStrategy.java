package org.mitre.tdp.boogie.conformance.alg.assign.link;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * Simple implementation of a {@link LinkingStrategy} where the links are manually supplied the the strategy a priori.
 */
public final class SuppliedLinkStrategy implements LinkingStrategy, Supplier<Collection<Pair<FlyableLeg, FlyableLeg>>> {

  private final Collection<Pair<FlyableLeg, FlyableLeg>> links;

  @SafeVarargs
  public SuppliedLinkStrategy(Pair<FlyableLeg, FlyableLeg>... links) {
    this(Arrays.asList(links));
  }

  public SuppliedLinkStrategy(Collection<Pair<FlyableLeg, FlyableLeg>> links) {
    this.links = links;
  }

  @Override
  public Collection<Pair<FlyableLeg, FlyableLeg>> get() {
    return links;
  }

  @Override
  public Collection<Pair<FlyableLeg, FlyableLeg>> links(Collection<FlyableLeg> flyableLegs) {
    return get();
  }
}
