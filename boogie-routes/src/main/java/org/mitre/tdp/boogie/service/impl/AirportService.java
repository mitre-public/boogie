package org.mitre.tdp.boogie.service.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Runway;

public final class AirportService<R extends Runway, A extends Airport<R>> extends SimpleService<A> {

  private AirportService(Multimap<String, A> map) {
    super(map);
  }

  public static <R extends Runway, A extends Airport<R>> AirportService<R, A> with(Iterable<A> airports) {
    Multimap<String, A> byId = new HashMultimap<>();
    airports.forEach(a -> byId.put(a.identifier(), a));
    return new AirportService<>(byId);
  }
}
