package org.mitre.tdp.boogie.service;

import java.util.Collection;

import org.mitre.tdp.boogie.Infrastructure;

/**
 * Interface for querying infrastructure elements by their given {@link Infrastructure#identifier()}.
 */
@FunctionalInterface
public interface LookupService<I extends Infrastructure> {

  /**
   * Returns the set of infrastructure elements matching the given identifier.
   */
  Collection<I> allMatchingIdentifiers(String identifier);
}
