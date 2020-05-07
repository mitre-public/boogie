package org.mitre.tdp.boogie.service.impl;

import org.mitre.tdp.boogie.Airway;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public final class AirwayService extends SimpleService<Airway> {

  private AirwayService(Multimap<String, Airway> map) {
    super(map);
  }

  public static AirwayService with(Iterable<? extends Airway> fixes) {
    Multimap<String, Airway> byId = HashMultimap.create();
    fixes.forEach(a -> byId.put(a.identifier(), a));
    return new AirwayService(byId);
  }
}
