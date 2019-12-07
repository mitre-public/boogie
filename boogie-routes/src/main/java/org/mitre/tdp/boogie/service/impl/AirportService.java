package org.mitre.tdp.boogie.service.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.mitre.tdp.boogie.Airport;

public final class AirportService extends SimpleService<Airport> {

  private AirportService(Multimap<String, Airport> map) {
    super(map);
  }

  public static AirportService with(Iterable<? extends Airport> airports) {
    Multimap<String, Airport> byId = HashMultimap.create();
    airports.forEach(a -> byId.put(a.identifier(), a));
    return new AirportService(byId);
  }
}
