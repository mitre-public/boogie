package org.mitre.tdp.boogie.service.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.mitre.tdp.boogie.Fix;

public final class FixService extends SimpleService<Fix> {

  private FixService(Multimap<String, Fix> map) {
    super(map);
  }

  public static FixService with(Iterable<? extends Fix> fixes) {
    Multimap<String, Fix> byId = HashMultimap.create();
    fixes.forEach(f -> byId.put(f.identifier(), f));
    return new FixService(byId);
  }
}
