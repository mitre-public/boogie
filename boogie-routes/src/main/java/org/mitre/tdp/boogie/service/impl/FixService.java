package org.mitre.tdp.boogie.service.impl;

import org.mitre.tdp.boogie.Fix;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

public final class FixService extends SimpleService<Fix> {

  private FixService(Multimap<String, Fix> map) {
    super(map);
  }

  public static FixService with(Iterable<? extends Fix> fixes) {
    Multimap<String, Fix> byId = LinkedHashMultimap.create();
    fixes.forEach(f -> byId.put(f.identifier(), f));
    return new FixService(byId);
  }
}
