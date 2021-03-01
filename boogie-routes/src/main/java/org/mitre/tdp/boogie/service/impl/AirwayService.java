package org.mitre.tdp.boogie.service.impl;

import org.mitre.tdp.boogie.Airway;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

public final class AirwayService extends SimpleService<Airway> {

  public AirwayService(Multimap<String, Airway> map) {
    super(map);
  }

  public static AirwayService with(Iterable<? extends Airway> fixes) {
    Multimap<String, Airway> byId = LinkedHashMultimap.create();
    fixes.forEach(a -> byId.put(a.identifier(), a));
    return new AirwayService(byId);
  }
}
