package org.mitre.tdp.boogie.service.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.mitre.tdp.boogie.Airway;

public final class AirwayService extends SimpleService<Airway> {

  private AirwayService(Multimap<String, Airway> map) {
    super(map);
  }

  public static AirwayService with(Iterable<? extends Airway> fixes) {
    Multimap<String, Airway> byId = new HashMultimap<>();
    fixes.forEach(a -> byId.put(a.identifier(), a));
    return new AirwayService(byId);
  }
}
