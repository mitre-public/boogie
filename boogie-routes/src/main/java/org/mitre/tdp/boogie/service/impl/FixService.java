package org.mitre.tdp.boogie.service.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.mitre.tdp.boogie.Fix;

public final class FixService<F extends Fix> extends SimpleService<F> {

  private FixService(Multimap<String, F> map) {
    super(map);
  }

  public static <F extends Fix> FixService<F> with(Iterable<F> fixes) {
    Multimap<String, F> byId = new HashMultimap<>();
    fixes.forEach(f -> byId.put(f.identifier(), f));
    return new FixService<>(byId);
  }
}
