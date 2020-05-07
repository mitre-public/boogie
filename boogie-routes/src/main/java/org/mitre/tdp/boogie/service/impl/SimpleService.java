package org.mitre.tdp.boogie.service.impl;

import java.util.Collection;

import org.mitre.tdp.boogie.Infrastructure;
import org.mitre.tdp.boogie.service.LookupService;

import com.google.common.collect.Multimap;

public abstract class SimpleService<I extends Infrastructure> implements LookupService<I> {

  private final Multimap<String, I> byIdentifier;

  protected SimpleService(Multimap<String, I> map) {
    this.byIdentifier = map;
  }

  @Override
  public Collection<I> allMatchingIdentifiers(String identifier) {
    return byIdentifier.get(identifier);
  }
}
