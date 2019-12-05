package org.mitre.tdp.boogie.service.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;

public final class AirwayService<F extends Fix, L extends Leg<F>, A extends Airway<L>> extends SimpleService<A> {

  private AirwayService(Multimap<String, A> map) {
    super(map);
  }

  public static <F extends Fix, L extends Leg<F>, A extends Airway<L>> AirwayService<F, L, A> with(Iterable<A> fixes) {
    Multimap<String, A> byId = new HashMultimap<>();
    fixes.forEach(a -> byId.put(a.identifier(), a));
    return new AirwayService<>(byId);
  }
}
