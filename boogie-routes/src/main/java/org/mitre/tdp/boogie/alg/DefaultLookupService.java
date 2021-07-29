package org.mitre.tdp.boogie.alg;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

/**
 * Default implementation of a {@link LookupService} which can be instantiated from a collection of already in-memory records
 * and a functional indexer taking record -> string.
 */
public final class DefaultLookupService<T> implements LookupService<T> {

  private final Multimap<String, T> lookup;

  private DefaultLookupService(Multimap<String, T> lookup) {
    this.lookup = lookup;
  }

  @Override
  public Collection<T> apply(String s) {
    requireNonNull(s);
    return lookup.get(s);
  }

  @SafeVarargs
  public static <T> LookupService<T> newLookupService(Function<T, String> indexer, T... records) {
    return newLookupService(indexer, Arrays.asList(records));
  }

  public static <T> LookupService<T> newLookupService(Function<T, String> indexer, Collection<T> records) {
    requireNonNull(indexer);
    LinkedHashMultimap<String, T> lookup = LinkedHashMultimap.create();
    records.forEach(record -> lookup.put(indexer.apply(record), record));
    return new DefaultLookupService<>(lookup);
  }
}
